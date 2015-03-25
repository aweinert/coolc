package net.alexweinert.coolc.processors.java.fromcool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import net.alexweinert.coolc.infrastructure.ProcessorException;
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
import net.alexweinert.coolc.representations.cool.symboltables.BoolSymbol;
import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;
import net.alexweinert.coolc.representations.cool.symboltables.IdTable;
import net.alexweinert.coolc.representations.cool.symboltables.IntTable;
import net.alexweinert.coolc.representations.cool.symboltables.StringTable;
import net.alexweinert.coolc.representations.java.JavaClass;
import net.alexweinert.coolc.representations.java.JavaProgram;

public class CoolToJavaVisitor extends Visitor {

    private final NameGenerator nameGen = new NameGenerator();
    private final Stack<String> variables = new Stack<>();

    private JavaClassBuilder writer;

    private List<JavaClass> javaClasses = new LinkedList<>();

    public JavaProgram process(Program input) throws ProcessorException {
        try {
            this.copyResource("CoolObject");
            this.copyResource("CoolBool");
            this.copyResource("CoolInt");
            this.copyResource("CoolString");
            this.copyResource("CoolIO");
        } catch (IOException e) {
            throw new ProcessorException(e);
        }
        input.acceptVisitor(this);
        return new JavaProgram(this.javaClasses);
    }

    private void copyResource(String fileName) throws IOException {
        BufferedReader sourceFileReader = new BufferedReader(new FileReader(new File(this.getClass().getClassLoader()
                .getResource(fileName + ".java").getFile())));
        final StringBuilder builder = new StringBuilder();
        String currentLine = sourceFileReader.readLine();

        while (currentLine != null) {
            builder.append(currentLine + "\n");
            currentLine = sourceFileReader.readLine();
        }
        sourceFileReader.close();
        this.javaClasses.add(new JavaClass(fileName, builder.toString()));
    }

    @Override
    public void visitClassPreorder(ClassNode classNode) {
        this.writer = new JavaClassBuilder(this.nameGen.getJavaNameForClass(classNode.getIdentifier()));

        this.writer.beginClass(classNode.getIdentifier(), classNode.getParent());

    }

    @Override
    public void visitClassPostorder(ClassNode classNode) {
        this.writer.endClass();
        this.javaClasses.add(this.writer.build());
    }

    @Override
    public void visitAttributePreorder(Attribute attribute) {
        this.writer.beginAttributeDefinition(attribute.getDeclaredType(), attribute.getName());
        if (attribute.getInitializer() instanceof NoExpression) {
            final String initializer = this.writer.declareVariable(attribute.getDeclaredType());
            if (attribute.getTypeDecl().equals(IdTable.getInstance().getBoolSymbol())) {
                this.writer.loadBoolean(initializer, BoolSymbol.falsebool);
            } else if (attribute.getTypeDecl().equals(IdTable.getInstance().getIntSymbol())) {
                this.writer.loadInt(initializer, IntTable.getInstance().addInt(0));
            } else if (attribute.getTypeDecl().equals(IdTable.getInstance().getStringSymbol())) {
                this.writer.loadString(initializer, StringTable.getInstance().addString(""));
            } else {
                this.writer.loadVoid(initializer);
            }
            this.variables.push(initializer);
        }
    }

    @Override
    public void visitAttributePostorder(Attribute attribute) {
        this.writer.endAttributeDefinition(this.variables.pop());
    }

    @Override
    public void visitMethodPreorder(Method method) {
        this.writer.startMethodDefinition(method.getReturnType(), method.getName(), method.getFormals());
    }

    @Override
    public void visitMethodPostorder(Method method) {
        this.writer.endMethodDefinition(this.variables.pop());
    }

    @Override
    public void visitBoolConst(BoolConst boolConst) {
        final String varName = this.writer.declareVariable(IdTable.getInstance().getBoolSymbol());
        this.writer.loadBoolean(varName, boolConst.getValue() ? BoolSymbol.truebool : BoolSymbol.falsebool);
        this.variables.push(varName);
    }

    @Override
    public void visitIntConst(IntConst intConst) {
        final String varName = this.writer.declareVariable(IdTable.getInstance().getIntSymbol());
        this.writer.loadInt(varName, intConst.getValue());
        this.variables.push(varName);
    }

    @Override
    public void visitStringConstant(StringConst stringConst) {
        final String varName = this.writer.declareVariable(IdTable.getInstance().getStringSymbol());
        this.writer.loadString(varName, stringConst.getValue());
        this.variables.push(varName);
    }

    @Override
    public void visitObjectReference(ObjectReference objectReference) {
        final String varName = this.writer.declareVariable(objectReference.getType());
        this.writer.loadVariable(varName, objectReference.getVariableIdentifier());
        this.variables.push(varName);
    }

    @Override
    public void visitAdditionPostorder(Addition addition) {
        final String varName = this.writer.declareVariable(IdTable.getInstance().getIntSymbol());
        final String rhsVariable = this.variables.pop();
        final String lhsVariable = this.variables.pop();
        this.writer.add(varName, lhsVariable, rhsVariable);
        this.variables.push(varName);
    }

    @Override
    public void visitBooleanNegationPostorder(BooleanNegation booleanNegation) {
        final String resultVariable = this.writer.declareVariable(IdTable.getInstance().getBoolSymbol());
        final String argVariable = this.variables.pop();
        this.writer.boolNeg(resultVariable, argVariable);
        this.variables.push(resultVariable);
    }

    @Override
    public void visitDivisionPostorder(Division division) {
        final String varName = this.writer.declareVariable(IdTable.getInstance().getIntSymbol());
        final String rhsVariable = this.variables.pop();
        final String lhsVariable = this.variables.pop();
        this.writer.div(varName, lhsVariable, rhsVariable);
        this.variables.push(varName);
    }

    @Override
    public void visitEqualityPostorder(Equality equality) {
        final String varName = this.writer.declareVariable(IdTable.getInstance().getBoolSymbol());
        final String rhsVariable = this.variables.pop();
        final String lhsVariable = this.variables.pop();
        this.writer.eq(varName, lhsVariable, rhsVariable);
        this.variables.push(varName);
    }

    @Override
    public void visitIsVoidPostorder(IsVoid isVoid) {
        final String varName = this.writer.declareVariable(IdTable.getInstance().getBoolSymbol());
        final String arg = this.variables.pop();
        this.writer.isVoid(varName, arg);
        this.variables.push(varName);
    }

    @Override
    public void visitLessThanPostorder(LessThan lessThan) {
        final String varName = this.writer.declareVariable(IdTable.getInstance().getBoolSymbol());
        final String rhsArg = this.variables.pop();
        final String lhsArg = this.variables.pop();
        this.writer.lt(varName, lhsArg, rhsArg);
        this.variables.push(varName);
    }

    @Override
    public void visitLessThanOrEqualsPostorder(LessThanOrEquals lessThanOrEquals) {
        final String varName = this.writer.declareVariable(IdTable.getInstance().getBoolSymbol());
        final String rhsArg = this.variables.pop();
        final String lhsArg = this.variables.pop();
        this.writer.lte(varName, lhsArg, rhsArg);
        this.variables.push(varName);
    }

    @Override
    public void visitMultiplicationPostorder(Multiplication multiplication) {
        final String varName = this.writer.declareVariable(IdTable.getInstance().getIntSymbol());
        final String rhsVariable = this.variables.pop();
        final String lhsVariable = this.variables.pop();
        this.writer.mul(varName, lhsVariable, rhsVariable);
        this.variables.push(varName);
    }

    @Override
    public void visitSubtractionPostorder(Subtraction subtraction) {
        final String varName = this.writer.declareVariable(IdTable.getInstance().getIntSymbol());
        final String rhsVariable = this.variables.pop();
        final String lhsVariable = this.variables.pop();
        this.writer.sub(varName, lhsVariable, rhsVariable);
        this.variables.push(varName);
    }

    @Override
    public void visitArithmeticNegationPostOrder(ArithmeticNegation arithmeticNegation) {
        final String varName = this.writer.declareVariable(IdTable.getInstance().getIntSymbol());
        final String arg = this.variables.pop();
        this.writer.arithNeg(varName, arg);
        this.variables.push(varName);
    }

    @Override
    public void visitAssignPostorder(Assign assign) {
        final String expressionVariable = this.variables.pop();
        final String assigneeVariable = this.writer.toVariable(assign.getVariableIdentifier());
        this.writer.assign(assigneeVariable, expressionVariable);
        this.variables.push(assigneeVariable);
    }

    @Override
    public void visitNew(New newNode) {
        final String returnVariable = this.writer.declareVariable(newNode.getType());
        this.writer.New(returnVariable, newNode.getTypeIdentifier());
        this.variables.push(returnVariable);
    }

    @Override
    public void visitBlockExpressionsInorder(BlockExpressions expressions) {
        this.variables.pop();
    }

    @Override
    public void visitIfPreorder(If ifNode) {
        this.writer.beginIf();
    }

    @Override
    public void visitIfPreorderOne(If ifNode) {
        final String conditionVariable = this.variables.pop();
        final String resultVariable = this.writer.declareVariable(ifNode.getType());
        this.writer.beginThen(conditionVariable);
        this.variables.push(resultVariable);
    }

    @Override
    public void visitIfPreorderTwo(If ifNode) {
        final String thenVariable = this.variables.pop();
        final String resultVariable = this.variables.peek();
        this.writer.assign(resultVariable, thenVariable);
        this.writer.beginElse();
    }

    @Override
    public void visitIfPostorder(If ifNode) {
        final String elseVariable = this.variables.pop();
        final String resultVariable = this.variables.peek();
        this.writer.assign(resultVariable, elseVariable);
        this.writer.endIf();
    }

    @Override
    public void visitLoopPreorder(Loop loop) {
        this.writer.beginLoop();
    }

    @Override
    public void visitLoopInorder(Loop loop) {
        final String conditionVariable = this.variables.pop();
        this.writer.endLoopCondition(conditionVariable);
    }

    @Override
    public void visitLoopPostorder(Loop loop) {
        this.writer.endLoop();
        // According to the manual, loops return void, i.e., null
        this.writer.loadVoid(this.variables.peek());
    }

    @Override
    public void visitTypecaseInorder(Typecase typecase) {
        final String expressionVariable = this.variables.peek();
        final String resultVariable = this.writer.declareVariable(typecase.getType());
        this.writer.beginTypecase(expressionVariable);
        this.variables.push(resultVariable);
    }

    @Override
    public void visitTypecasePostorder(Typecase typecase) {
        this.writer.endTypecase();
        final String resultVariable = this.variables.pop();

        // Pop the expression variable
        this.variables.pop();
        this.variables.push(resultVariable);
    }

    @Override
    public void visitCasePreorder(Case caseNode) {
        // Current layout of the stack: [..., expressionVar, resultVariable]
        final String expressionVar = this.variables.get(this.variables.size() - 2);
        this.writer.beginCase(expressionVar, caseNode.getDeclaredType(), caseNode.getVariableIdentifier());
    }

    @Override
    public void visitCasePostorder(Case caseNode) {
        final String expressionVariable = this.variables.pop();
        final String resultVariable = this.variables.peek();
        this.writer.assign(resultVariable, expressionVariable);
        this.writer.endCase();
    }

    @Override
    public void visitFunctionCallPostorder(FunctionCall functionCall) {
        final List<String> arguments = new LinkedList<>();
        for (int argIndex = 0; argIndex < functionCall.getArguments().size(); ++argIndex) {
            arguments.add(0, this.variables.pop());
        }

        final String dispatchVariable = this.variables.pop();
        final String resultVariable = this.writer.declareVariable(functionCall.getType());
        this.writer.functionCall(resultVariable, dispatchVariable, functionCall.getFunctionIdentifier(), arguments);
        this.variables.push(resultVariable);
    }

    @Override
    public void visitStaticFunctionCallPostorder(StaticFunctionCall staticFunctionCall) {
        final List<String> arguments = new LinkedList<>();
        for (int argIndex = 0; argIndex < staticFunctionCall.getArguments().size(); ++argIndex) {
            arguments.add(0, this.variables.pop());
        }

        final String dispatchVariable = this.variables.pop();
        final String resultVariable = this.writer.declareVariable(staticFunctionCall.getType());
        final IdSymbol staticType = staticFunctionCall.getStaticType();
        this.writer.staticFunctionCall(resultVariable, dispatchVariable, staticFunctionCall.getFunctionIdentifier(),
                staticType, arguments);
        this.variables.push(resultVariable);
    }

    @Override
    public void visitLetInorder(Let let) {
        final String letVariable = this.writer.toVariable(let.getVariableIdentifier());
        this.writer.declareVariable(let.getDeclaredType(), letVariable);

        final String initializerVariable = this.variables.pop();
        this.writer.assign(letVariable, initializerVariable);
    }
}
