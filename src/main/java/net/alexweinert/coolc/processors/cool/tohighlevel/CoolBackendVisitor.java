package net.alexweinert.coolc.processors.cool.tohighlevel;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import net.alexweinert.pipelines.ProcessorException;
import net.alexweinert.coolc.representations.cool.ast.Addition;
import net.alexweinert.coolc.representations.cool.ast.ArithmeticNegation;
import net.alexweinert.coolc.representations.cool.ast.Assign;
import net.alexweinert.coolc.representations.cool.ast.Attribute;
import net.alexweinert.coolc.representations.cool.ast.BlockExpressions;
import net.alexweinert.coolc.representations.cool.ast.BoolConst;
import net.alexweinert.coolc.representations.cool.ast.BooleanNegation;
import net.alexweinert.coolc.representations.cool.ast.Case;
import net.alexweinert.coolc.representations.cool.ast.ClassNode;
import net.alexweinert.coolc.representations.cool.ast.Division;
import net.alexweinert.coolc.representations.cool.ast.Equality;
import net.alexweinert.coolc.representations.cool.ast.Expression;
import net.alexweinert.coolc.representations.cool.ast.FunctionCall;
import net.alexweinert.coolc.representations.cool.ast.If;
import net.alexweinert.coolc.representations.cool.ast.IntConst;
import net.alexweinert.coolc.representations.cool.ast.IsVoid;
import net.alexweinert.coolc.representations.cool.ast.LessThan;
import net.alexweinert.coolc.representations.cool.ast.LessThanOrEquals;
import net.alexweinert.coolc.representations.cool.ast.Let;
import net.alexweinert.coolc.representations.cool.ast.Loop;
import net.alexweinert.coolc.representations.cool.ast.Method;
import net.alexweinert.coolc.representations.cool.ast.Multiplication;
import net.alexweinert.coolc.representations.cool.ast.New;
import net.alexweinert.coolc.representations.cool.ast.NoExpression;
import net.alexweinert.coolc.representations.cool.ast.ObjectReference;
import net.alexweinert.coolc.representations.cool.ast.Program;
import net.alexweinert.coolc.representations.cool.ast.StaticFunctionCall;
import net.alexweinert.coolc.representations.cool.ast.StringConst;
import net.alexweinert.coolc.representations.cool.ast.Subtraction;
import net.alexweinert.coolc.representations.cool.ast.Typecase;
import net.alexweinert.coolc.representations.cool.ast.Visitor;
import net.alexweinert.coolc.representations.cool.information.ClassHierarchy;
import net.alexweinert.coolc.representations.cool.information.DeclaredClassSignature;
import net.alexweinert.coolc.representations.cool.information.DefinedClassSignature;
import net.alexweinert.coolc.representations.cool.information.MethodSignature;
import net.alexweinert.coolc.representations.cool.symboltables.BoolSymbol;
import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;
import net.alexweinert.coolc.representations.cool.symboltables.IdTable;
import net.alexweinert.coolc.representations.cool.symboltables.IntTable;
import net.alexweinert.coolc.representations.cool.symboltables.StringTable;

public class CoolBackendVisitor<T, U> extends Visitor {

    private final Stack<String> variables = new Stack<>();

    private final CoolBackendBuilderFactory<T, U> factory;
    private CoolBackendBuilder<T, U> builder;

    private List<T> javaClasses = new LinkedList<>();
    private Map<IdSymbol, DefinedClassSignature> methods;

    public CoolBackendVisitor(CoolBackendBuilderFactory<T, U> factory) {
        this.factory = factory;
    }

    public U process(Program input) throws ProcessorException {
        this.builder = this.factory.createBuilder(null);
        this.javaClasses.addAll(this.builder.buildBasicClasses());

        this.methods = this.createMethodMap(input);

        input.acceptVisitor(this);
        return this.builder.buildProgram(this.javaClasses);
    }

    private Map<IdSymbol, DefinedClassSignature> createMethodMap(Program program) {
        final Map<IdSymbol, DeclaredClassSignature> declaredSignatures = new HashMap<>();
        declaredSignatures.put(IdTable.getInstance().getObjectSymbol(), DeclaredClassSignature.createObjectSignature());
        declaredSignatures.put(IdTable.getInstance().getBoolSymbol(), DeclaredClassSignature.createBoolSignature());
        declaredSignatures.put(IdTable.getInstance().getIntSymbol(), DeclaredClassSignature.createIntSignature());
        declaredSignatures.put(IdTable.getInstance().getStringSymbol(), DeclaredClassSignature.createStringSignature());
        declaredSignatures.put(IdTable.getInstance().getIOSymbol(), DeclaredClassSignature.createIOSignature());

        for (ClassNode classNode : program.getClasses()) {
            declaredSignatures.put(classNode.getIdentifier(), DeclaredClassSignature.create(classNode));
        }

        final ClassHierarchy hierarchy = ClassHierarchy.create(program);
        final Map<IdSymbol, DefinedClassSignature> returnValue = new HashMap<>();
        returnValue.put(IdTable.getInstance().getObjectSymbol(),
                DefinedClassSignature.create(IdTable.getInstance().getObjectSymbol(), hierarchy, declaredSignatures));
        returnValue.put(IdTable.getInstance().getBoolSymbol(),
                DefinedClassSignature.create(IdTable.getInstance().getBoolSymbol(), hierarchy, declaredSignatures));
        returnValue.put(IdTable.getInstance().getIntSymbol(),
                DefinedClassSignature.create(IdTable.getInstance().getIntSymbol(), hierarchy, declaredSignatures));
        returnValue.put(IdTable.getInstance().getStringSymbol(),
                DefinedClassSignature.create(IdTable.getInstance().getStringSymbol(), hierarchy, declaredSignatures));
        returnValue.put(IdTable.getInstance().getIOSymbol(),
                DefinedClassSignature.create(IdTable.getInstance().getIOSymbol(), hierarchy, declaredSignatures));
        for (ClassNode classNode : program.getClasses()) {
            returnValue.put(classNode.getIdentifier(),
                    DefinedClassSignature.create(classNode.getIdentifier(), hierarchy, declaredSignatures));
        }

        return returnValue;
    }

    @Override
    public void visitClassPreorder(ClassNode classNode) {
        this.builder = this.factory.createBuilder(classNode.getIdentifier());

        this.builder.beginClass(classNode.getIdentifier(), classNode.getParent());
    }

    @Override
    public void visitClassPostorder(ClassNode classNode) {
        this.builder.endClass();
        this.javaClasses.add(this.builder.build());
    }

    @Override
    public void visitAttributePreorder(Attribute attribute) {
        this.builder.beginAttributeDefinition(attribute.getDeclaredType(), attribute.getName());
        if (attribute.getInitializer() instanceof NoExpression) {
            final String initializer = this.builder.declareVariable(attribute.getDeclaredType());
            if (attribute.getTypeDecl().equals(IdTable.getInstance().getBoolSymbol())) {
                this.builder.loadBoolean(initializer, BoolSymbol.falsebool);
            } else if (attribute.getTypeDecl().equals(IdTable.getInstance().getIntSymbol())) {
                this.builder.loadInt(initializer, IntTable.getInstance().addInt(0));
            } else if (attribute.getTypeDecl().equals(IdTable.getInstance().getStringSymbol())) {
                this.builder.loadString(initializer, StringTable.getInstance().addString(""));
            } else {
                this.builder.loadVoid(initializer);
            }
            this.variables.push(initializer);
        }
    }

    @Override
    public void visitAttributePostorder(Attribute attribute) {
        this.builder.endAttributeDefinition(this.variables.pop());
    }

    @Override
    public void visitMethodPreorder(Method method) {
        this.builder.startMethodDefinition(method.getReturnType(), method.getName(), method.getFormals());
    }

    @Override
    public void visitMethodPostorder(Method method) {
        this.builder.endMethodDefinition(this.variables.pop());
    }

    @Override
    public void visitBoolConst(BoolConst boolConst) {
        final String varName = this.builder.declareVariable(IdTable.getInstance().getBoolSymbol());
        this.builder.loadBoolean(varName, boolConst.getValue() ? BoolSymbol.truebool : BoolSymbol.falsebool);
        this.variables.push(varName);
    }

    @Override
    public void visitIntConst(IntConst intConst) {
        final String varName = this.builder.declareVariable(IdTable.getInstance().getIntSymbol());
        this.builder.loadInt(varName, intConst.getValue());
        this.variables.push(varName);
    }

    @Override
    public void visitStringConstant(StringConst stringConst) {
        final String varName = this.builder.declareVariable(IdTable.getInstance().getStringSymbol());
        this.builder.loadString(varName, stringConst.getValue());
        this.variables.push(varName);
    }

    @Override
    public void visitObjectReference(ObjectReference objectReference) {
        final String varName = this.builder.declareVariable(objectReference.getType());
        this.builder.loadVariable(varName, objectReference.getVariableIdentifier());
        this.variables.push(varName);
    }

    @Override
    public void visitAdditionPostorder(Addition addition) {
        final String varName = this.builder.declareVariable(IdTable.getInstance().getIntSymbol());
        final String rhsVariable = this.variables.pop();
        final String lhsVariable = this.variables.pop();
        this.builder.add(varName, lhsVariable, rhsVariable);
        this.variables.push(varName);
    }

    @Override
    public void visitBooleanNegationPostorder(BooleanNegation booleanNegation) {
        final String resultVariable = this.builder.declareVariable(IdTable.getInstance().getBoolSymbol());
        final String argVariable = this.variables.pop();
        this.builder.boolNeg(resultVariable, argVariable);
        this.variables.push(resultVariable);
    }

    @Override
    public void visitDivisionPostorder(Division division) {
        final String varName = this.builder.declareVariable(IdTable.getInstance().getIntSymbol());
        final String rhsVariable = this.variables.pop();
        final String lhsVariable = this.variables.pop();
        this.builder.div(varName, lhsVariable, rhsVariable);
        this.variables.push(varName);
    }

    @Override
    public void visitEqualityPostorder(Equality equality) {
        final String varName = this.builder.declareVariable(IdTable.getInstance().getBoolSymbol());
        final String rhsVariable = this.variables.pop();
        final String lhsVariable = this.variables.pop();
        this.builder.eq(varName, lhsVariable, rhsVariable);
        this.variables.push(varName);
    }

    @Override
    public void visitIsVoidPostorder(IsVoid isVoid) {
        final String varName = this.builder.declareVariable(IdTable.getInstance().getBoolSymbol());
        final String arg = this.variables.pop();
        this.builder.isVoid(varName, arg);
        this.variables.push(varName);
    }

    @Override
    public void visitLessThanPostorder(LessThan lessThan) {
        final String varName = this.builder.declareVariable(IdTable.getInstance().getBoolSymbol());
        final String rhsArg = this.variables.pop();
        final String lhsArg = this.variables.pop();
        this.builder.lt(varName, lhsArg, rhsArg);
        this.variables.push(varName);
    }

    @Override
    public void visitLessThanOrEqualsPostorder(LessThanOrEquals lessThanOrEquals) {
        final String varName = this.builder.declareVariable(IdTable.getInstance().getBoolSymbol());
        final String rhsArg = this.variables.pop();
        final String lhsArg = this.variables.pop();
        this.builder.lte(varName, lhsArg, rhsArg);
        this.variables.push(varName);
    }

    @Override
    public void visitMultiplicationPostorder(Multiplication multiplication) {
        final String varName = this.builder.declareVariable(IdTable.getInstance().getIntSymbol());
        final String rhsVariable = this.variables.pop();
        final String lhsVariable = this.variables.pop();
        this.builder.mul(varName, lhsVariable, rhsVariable);
        this.variables.push(varName);
    }

    @Override
    public void visitSubtractionPostorder(Subtraction subtraction) {
        final String varName = this.builder.declareVariable(IdTable.getInstance().getIntSymbol());
        final String rhsVariable = this.variables.pop();
        final String lhsVariable = this.variables.pop();
        this.builder.sub(varName, lhsVariable, rhsVariable);
        this.variables.push(varName);
    }

    @Override
    public void visitArithmeticNegationPostOrder(ArithmeticNegation arithmeticNegation) {
        final String varName = this.builder.declareVariable(IdTable.getInstance().getIntSymbol());
        final String arg = this.variables.pop();
        this.builder.arithNeg(varName, arg);
        this.variables.push(varName);
    }

    @Override
    public void visitAssignPostorder(Assign assign) {
        final String expressionVariable = this.variables.pop();
        final String assigneeVariable = this.builder.toVariable(assign.getVariableIdentifier());
        this.builder.assign(assigneeVariable, expressionVariable);
        this.variables.push(assigneeVariable);
    }

    @Override
    public void visitNew(New newNode) {
        final String returnVariable = this.builder.declareVariable(newNode.getType());
        this.builder.New(returnVariable, newNode.getTypeIdentifier());
        this.variables.push(returnVariable);
    }

    @Override
    public void visitBlockExpressionsInorder(BlockExpressions expressions) {
        this.variables.pop();
    }

    @Override
    public void visitIfPreorder(If ifNode) {
        this.builder.beginIf();
    }

    @Override
    public void visitIfPreorderOne(If ifNode) {
        final String conditionVariable = this.variables.pop();
        final String resultVariable = this.builder.declareVariable(ifNode.getType());
        this.builder.beginThen(conditionVariable);
        this.variables.push(resultVariable);
    }

    @Override
    public void visitIfPreorderTwo(If ifNode) {
        final String thenVariable = this.variables.pop();
        final String resultVariable = this.variables.peek();
        this.builder.assign(resultVariable, thenVariable);
        this.builder.beginElse();
    }

    @Override
    public void visitIfPostorder(If ifNode) {
        final String elseVariable = this.variables.pop();
        final String resultVariable = this.variables.peek();
        this.builder.assign(resultVariable, elseVariable);
        this.builder.endIf();
    }

    @Override
    public void visitLoopPreorder(Loop loop) {
        this.builder.beginLoop();
    }

    @Override
    public void visitLoopInorder(Loop loop) {
        final String conditionVariable = this.variables.pop();
        this.builder.endLoopCondition(conditionVariable);
    }

    @Override
    public void visitLoopPostorder(Loop loop) {
        this.variables.pop();
        this.builder.endLoop();
        // According to the manual, loops return void, i.e., null
        final String loopResultVar = this.builder.declareVariable(IdTable.getInstance().getObjectSymbol());
        this.builder.loadVoid(loopResultVar);
        this.variables.push(loopResultVar);
    }

    @Override
    public void visitTypecaseInorder(Typecase typecase) {
        final String expressionVariable = this.variables.peek();
        final String resultVariable = this.builder.declareVariable(typecase.getType());
        this.builder.beginTypecase(expressionVariable);
        this.variables.push(resultVariable);
    }

    @Override
    public void visitTypecasePostorder(Typecase typecase) {
        final String resultVariable = this.variables.pop();
        this.builder.endTypecase(resultVariable);

        // Pop the expression variable
        this.variables.pop();
        this.variables.push(resultVariable);
    }

    @Override
    public void visitCasePreorder(Case caseNode) {
        // Current layout of the stack: [..., expressionVar, resultVariable]
        final String expressionVar = this.variables.get(this.variables.size() - 2);
        this.builder.declareVariable(caseNode.getDeclaredType(), caseNode.getVariableIdentifier().getString());
        this.builder.beginCase(expressionVar, caseNode.getDeclaredType(), caseNode.getVariableIdentifier());
    }

    @Override
    public void visitCasePostorder(Case caseNode) {
        final String expressionVariable = this.variables.pop();
        final String resultVariable = this.variables.peek();
        this.builder.assign(resultVariable, expressionVariable);
        this.builder.endCase();
    }

    @Override
    public void visitFunctionCallPostorder(FunctionCall functionCall) {
        final List<String> arguments = new LinkedList<>();
        for (int argIndex = 0; argIndex < functionCall.getArguments().size(); ++argIndex) {
            arguments.add(0, this.variables.pop());
        }

        final String dispatchVariable = this.variables.pop();
        final String resultVariable = this.builder.declareVariable(functionCall.getType());
        final List<String> argumentTypes = new LinkedList<>();
        final DefinedClassSignature definingClassSignature = this.methods.get(functionCall.getDispatchExpression()
                .getType());
        final MethodSignature calledMethodSignature = definingClassSignature.getMethodSignature(functionCall
                .getFunctionIdentifier());
        for (IdSymbol argType : calledMethodSignature.getArgumentTypes()) {
            argumentTypes.add(argType.toString());
        }
        this.builder.functionCall(resultVariable, dispatchVariable, functionCall.getDispatchExpression().getType()
                .toString(), functionCall.getFunctionIdentifier(), functionCall.getType(), arguments, argumentTypes);
        this.variables.push(resultVariable);
    }

    @Override
    public void visitStaticFunctionCallPostorder(StaticFunctionCall staticFunctionCall) {
        final List<String> arguments = new LinkedList<>();
        for (int argIndex = 0; argIndex < staticFunctionCall.getArguments().size(); ++argIndex) {
            arguments.add(0, this.variables.pop());
        }

        final String dispatchVariable = this.variables.pop();
        final String resultVariable = this.builder.declareVariable(staticFunctionCall.getType());
        final IdSymbol staticType = staticFunctionCall.getStaticType();
        final List<String> argumentTypes = new LinkedList<>();
        for (Expression arg : staticFunctionCall.getArguments()) {
            argumentTypes.add(arg.getType().toString());
        }
        this.builder.staticFunctionCall(resultVariable, dispatchVariable, staticFunctionCall.getFunctionIdentifier(),
                staticType, staticFunctionCall.getType(), arguments, argumentTypes);
        this.variables.push(resultVariable);
    }

    @Override
    public void visitLetPreorder(Let let) {
        if (let.getInitializer() instanceof NoExpression) {
            final String initializerVariable = this.builder.declareVariable(let.getDeclaredType());
            this.variables.push(initializerVariable);
            if (let.getDeclaredType().equals(IdTable.getInstance().getBoolSymbol())) {
                this.builder.loadBoolean(initializerVariable, BoolSymbol.falsebool);
            } else if (let.getDeclaredType().equals(IdTable.getInstance().getIntSymbol())) {
                this.builder.loadInt(initializerVariable, IntTable.getInstance().addInt(0));
            } else if (let.getDeclaredType().equals(IdTable.getInstance().getStringSymbol())) {
                this.builder.loadString(initializerVariable, StringTable.getInstance().addString(""));
            } else {
                this.builder.loadVoid(initializerVariable);
            }
        }
    }

    @Override
    public void visitLetInorder(Let let) {
        final String letVariable = this.builder.toVariable(let.getVariableIdentifier());
        this.builder.declareVariable(let.getDeclaredType(), letVariable);

        final String initializerVariable = this.variables.pop();
        this.builder.assign(letVariable, initializerVariable);
    }
}
