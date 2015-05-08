package net.alexweinert.coolc.processors.bytecode.fromcool;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import net.alexweinert.coolc.infrastructure.ProcessorException;
import net.alexweinert.coolc.processors.cool.tohighlevel.CoolBackendBuilder;
import net.alexweinert.coolc.representations.bytecode.Attribute;
import net.alexweinert.coolc.representations.bytecode.ByteClass;
import net.alexweinert.coolc.representations.bytecode.LabeledInstruction;
import net.alexweinert.coolc.representations.bytecode.Method;
import net.alexweinert.coolc.representations.cool.ast.Formal;
import net.alexweinert.coolc.representations.cool.ast.Formals;
import net.alexweinert.coolc.representations.cool.symboltables.BoolSymbol;
import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;
import net.alexweinert.coolc.representations.cool.symboltables.IntSymbol;
import net.alexweinert.coolc.representations.cool.symboltables.StringSymbol;

public class FromCoolBuilder implements CoolBackendBuilder<ByteClass, List<ByteClass>> {

    private final NameGenerator nameGen = new NameGenerator();

    private ByteClass.Builder classBuilder;
    private Method.Builder methodBuilder;
    private LabeledInstruction.Factory instructionFactory = new LabeledInstruction.Factory();

    private final Stack<String> labels = new Stack<>();

    @Override
    public void beginClass(IdSymbol idSymbol, IdSymbol parentSymbol) {
        this.classBuilder = new ByteClass.Builder(idSymbol.getString(), parentSymbol.getString());
    }

    @Override
    public void endClass() {}

    @Override
    public ByteClass build() {
        return this.classBuilder.build();
    }

    @Override
    public Collection<ByteClass> buildBasicClasses() throws ProcessorException {
        return Collections.emptySet();
    }

    @Override
    public void beginAttributeDefinition(IdSymbol typeSymbol, IdSymbol idSymbol) {
        final String initializerId = this.nameGen.getIdForInitializer(typeSymbol, idSymbol);
        this.classBuilder.addAttribute(new Attribute(typeSymbol.getString(), idSymbol.getString(), initializerId));
        this.methodBuilder = new Method.Builder(typeSymbol.getString(), initializerId);
    }

    @Override
    public void endAttributeDefinition(String variable) {
        this.methodBuilder.addInstruction(instructionFactory.buildReturn(variable));
        this.classBuilder.addMethod(this.methodBuilder.build());
        this.methodBuilder = null;
    }

    @Override
    public String declareVariable(IdSymbol typeSymbol) {
        final String id = this.nameGen.getFreshVariableId();
        this.declareVariable(typeSymbol, id);
        return id;
    }

    @Override
    public void declareVariable(IdSymbol typeSymbol, String id) {
        this.methodBuilder.addLocalVar(typeSymbol.getString(), id);

    }

    @Override
    public void loadVoid(String target) {
        this.methodBuilder.addInstruction(this.instructionFactory.buildLoadVoid(target));
    }

    @Override
    public void loadBoolean(String target, BoolSymbol value) {
        final boolean boolValue = value.getString().equals("true");
        this.methodBuilder.addInstruction(this.instructionFactory.buildLoadBool(target, boolValue));
    }

    @Override
    public void loadInt(String target, IntSymbol value) {
        this.methodBuilder.addInstruction(this.instructionFactory.buildLoadInt(target,
                Integer.parseInt(value.getString())));
    }

    @Override
    public void loadString(String target, StringSymbol value) {
        this.methodBuilder.addInstruction(this.instructionFactory.buildLoadString(target, value.getString()));
    }

    @Override
    public void loadVariable(String varName, IdSymbol variableIdentifier) {
        this.methodBuilder.addInstruction(this.instructionFactory.buildAssign(varName, variableIdentifier.getString()));
    }

    @Override
    public void startMethodDefinition(IdSymbol returnTypeSymbol, IdSymbol idSymbol, Formals formals) {
        this.methodBuilder = new Method.Builder(returnTypeSymbol.getString(), idSymbol.getString());
        for (Formal formal : formals) {
            this.methodBuilder.addParameter(formal.getDeclaredType().getString(), formal.getIdentifier().getString());
        }
    }

    @Override
    public void endMethodDefinition(String returnVariable) {
        this.methodBuilder.addInstruction(this.instructionFactory.buildReturn(returnVariable));
        this.classBuilder.addMethod(this.methodBuilder.build());
    }

    @Override
    public void arithNeg(String result, String arg) {
        this.methodBuilder.addInstruction(this.instructionFactory.buildArithNeg(result, arg));
    }

    @Override
    public void add(String result, String lhs, String rhs) {
        this.methodBuilder.addInstruction(this.instructionFactory.buildAdd(result, lhs, rhs));
    }

    @Override
    public void sub(String result, String lhs, String rhs) {
        this.methodBuilder.addInstruction(this.instructionFactory.buildSub(result, lhs, rhs));
    }

    @Override
    public void mul(String result, String lhs, String rhs) {
        this.methodBuilder.addInstruction(this.instructionFactory.buildMul(result, lhs, rhs));
    }

    @Override
    public void div(String result, String lhs, String rhs) {
        this.methodBuilder.addInstruction(this.instructionFactory.buildDiv(result, lhs, rhs));
    }

    @Override
    public void lt(String result, String lhs, String rhs) {
        this.methodBuilder.addInstruction(this.instructionFactory.buildLt(result, lhs, rhs));
    }

    @Override
    public void lte(String result, String lhs, String rhs) {
        this.methodBuilder.addInstruction(this.instructionFactory.buildLte(result, lhs, rhs));
    }

    @Override
    public void eq(String result, String lhs, String rhs) {
        this.methodBuilder.addInstruction(this.instructionFactory.buildEq(result, lhs, rhs));
    }

    @Override
    public void isVoid(String result, String arg) {
        this.methodBuilder.addInstruction(this.instructionFactory.buildIsVoid(result, arg));
    }

    @Override
    public void boolNeg(String result, String arg) {
        this.methodBuilder.addInstruction(this.instructionFactory.buildBoolNeg(result, arg));
    }

    @Override
    public void assign(String result, String arg) {
        this.methodBuilder.addInstruction(this.instructionFactory.buildAssign(result, arg));
    }

    @Override
    public String toVariable(IdSymbol arg) {
        return arg.getString();
    }

    @Override
    public void New(String returnVariable, IdSymbol typeSymbol) {
        this.methodBuilder.addInstruction(this.instructionFactory.buildNew(returnVariable, typeSymbol.getString()));
    }

    @Override
    public void beginIf() {}

    @Override
    public void beginThen(String conditionVariable) {
        final String elseLabel = this.nameGen.getFreshLabel("Else");
        this.labels.push(elseLabel);
        this.methodBuilder.addInstruction(this.instructionFactory.buildBranchIfFalse(conditionVariable, elseLabel));
    }

    @Override
    public void beginElse() {
        final String elseLabel = this.labels.pop();
        final String afterIfLabel = this.nameGen.getFreshLabel("AfterIf");
        this.labels.push(afterIfLabel);
        this.methodBuilder.addInstruction(this.instructionFactory.buildBranch(afterIfLabel));
        this.instructionFactory.setLabel(elseLabel);
    }

    @Override
    public void endIf() {
        final String afterIfLabel = this.labels.pop();
        this.instructionFactory.setLabel(afterIfLabel);
    }

    @Override
    public void beginLoop() {
        final String conditionLabel = this.nameGen.getFreshLabel("Condition");
        this.labels.push(conditionLabel);
        this.instructionFactory.setLabel(conditionLabel);
    }

    @Override
    public void endLoopCondition(String conditionVariable) {
        final String afterLabel = this.nameGen.getFreshLabel("AfterLoop");
        this.labels.push(afterLabel);
        this.methodBuilder.addInstruction(this.instructionFactory.buildBranchIfFalse(conditionVariable, afterLabel));
    }

    @Override
    public void endLoop() {
        final String afterLabel = this.labels.pop();
        final String conditionLabel = this.labels.pop();
        this.methodBuilder.addInstruction(this.instructionFactory.buildBranch(conditionLabel));
        this.instructionFactory.setLabel(afterLabel);
    }

    @Override
    public void functionCall(String resultVariable, String dispatchVariable, String dispatchVariableType,
            IdSymbol functionIdentifier, IdSymbol returnType, List<String> arguments, List<String> argumentTypes) {
        this.methodBuilder
                .addInstruction(this.instructionFactory.buildFunctionCall(resultVariable, dispatchVariable,
                        dispatchVariableType, functionIdentifier.getString(), returnType.getString(), arguments,
                        argumentTypes));
    }

    @Override
    public void staticFunctionCall(String resultVariable, String dispatchVariable, IdSymbol functionIdentifier,
            IdSymbol staticType, IdSymbol returnType, List<String> arguments, List<String> argumentTypes) {
        this.methodBuilder.addInstruction(this.instructionFactory.buildStaticFunctionCall(resultVariable,
                dispatchVariable, staticType.getString(), functionIdentifier.getString(), returnType.getString(),
                arguments, argumentTypes));
    }

    @Override
    public void beginTypecase(String expressionVariable) {
        final String afterLabel = this.nameGen.getFreshLabel("AfterTypeCase");
        this.labels.push(afterLabel);
        final String nextCaseLabel = this.nameGen.getFreshLabel("TypeCase");
        this.labels.push(nextCaseLabel);
    }

    @Override
    public void beginCase(String expressionVariable, IdSymbol typeSymbol, IdSymbol idSymbol) {
        this.instructionFactory.setLabel(this.labels.pop());
        final String nextLabel = this.nameGen.getFreshLabel("TypeCase");
        this.methodBuilder.addInstruction(this.instructionFactory.buildBranchIfNotInstanceOf(nextLabel,
                expressionVariable, typeSymbol.getString()));
        this.labels.push(nextLabel);
    }

    @Override
    public void endCase() {
        // Layout of the stack: [..., afterLabel, nextCaseLabel]
        final String afterLabel = this.labels.get(this.labels.size() - 2);
        this.methodBuilder.addInstruction(this.instructionFactory.buildBranch(afterLabel));
    }

    @Override
    public void endTypecase() {
        // TODO Properly handle non-matches, for now, just pop the final (catch-all) label
        this.instructionFactory.setLabel(this.labels.pop());
        this.methodBuilder.addInstruction(this.instructionFactory.buildNop());
        final String afterLabel = this.labels.pop();
        this.instructionFactory.setLabel(afterLabel);
    }

    @Override
    public List<ByteClass> buildProgram(List<ByteClass> classes) {
        return classes;
    }

}
