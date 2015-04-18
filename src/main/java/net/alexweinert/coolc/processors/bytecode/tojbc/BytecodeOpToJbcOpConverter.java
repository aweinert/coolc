package net.alexweinert.coolc.processors.bytecode.tojbc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.alexweinert.coolc.representations.bytecode.LabeledInstruction;
import net.alexweinert.coolc.representations.bytecode.TypedId;
import net.alexweinert.coolc.representations.bytecode.Visitor;
import net.alexweinert.coolc.representations.jbc.instructions.OpCode;

class BytecodeOpToJbcOpConverter extends Visitor {

    public static BytecodeOpToJbcOpConverter create(List<TypedId> localVariables) {
        final Map<String, Integer> variableNameToNumber = new HashMap<>();
        int id = 0;
        for (TypedId typedId : localVariables) {
            variableNameToNumber.put(typedId.getId(), id++);
        }
        return new BytecodeOpToJbcOpConverter(variableNameToNumber, new OpCodeAssembler());
    }

    private Map<String, Integer> variableNameToNumber;
    private final OpCodeAssembler assembler;

    BytecodeOpToJbcOpConverter(Map<String, Integer> variableNameToNumber, OpCodeAssembler assembler) {
        this.variableNameToNumber = variableNameToNumber;
        this.assembler = assembler;
    }

    public List<OpCode> convert(List<LabeledInstruction> list) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void visitLtInstruction(String label, String target, String lhs, String rhs) {
        // TODO Auto-generated method stub
        super.visitLtInstruction(label, target, lhs, rhs);
    }

    @Override
    public void visitLteInstruction(String label, String target, String lhs, String rhs) {
        // TODO Auto-generated method stub
        super.visitLteInstruction(label, target, lhs, rhs);
    }

    @Override
    public void visitBoolNegInstruction(String label, String target, String arg) {
        // TODO Auto-generated method stub
        super.visitBoolNegInstruction(label, target, arg);
    }

    @Override
    public void visitBranchIfFalseInstruction(String label, String target, String conditionVariable) {
        // TODO Auto-generated method stub
        super.visitBranchIfFalseInstruction(label, target, conditionVariable);
    }

    @Override
    public void visitBranchIfNotInstanceOfInstruction(String label, String target, String expressionVariable) {
        // TODO Auto-generated method stub
        super.visitBranchIfNotInstanceOfInstruction(label, target, expressionVariable);
    }

    @Override
    public void visitAddInstruction(String label, String target, String lhs, String rhs) {
        // TODO Auto-generated method stub
        super.visitAddInstruction(label, target, lhs, rhs);
    }

    @Override
    public void visitDivInstruction(String label, String target, String lhs, String rhs) {
        // TODO Auto-generated method stub
        super.visitDivInstruction(label, target, lhs, rhs);
    }

    @Override
    public void visitMulInstruction(String label, String target, String lhs, String rhs) {
        // TODO Auto-generated method stub
        super.visitMulInstruction(label, target, lhs, rhs);
    }

    @Override
    public void visitSubInstruction(String label, String target, String lhs, String rhs) {
        // TODO Auto-generated method stub
        super.visitSubInstruction(label, target, lhs, rhs);
    }

    @Override
    public void visitEqInstruction(String label, String target, String lhs, String rhs) {
        // TODO Auto-generated method stub
        super.visitEqInstruction(label, target, lhs, rhs);
    }

    @Override
    public void visitFunctionCallInstruction(String label, String target, String dispatchVariable, String methodId,
            List<String> arguments) {
        // TODO Auto-generated method stub
        super.visitFunctionCallInstruction(label, target, dispatchVariable, methodId, arguments);
    }

    @Override
    public void visitIsVoidInstruction(String label, String target, String arg) {
        // TODO Auto-generated method stub
        super.visitIsVoidInstruction(label, target, arg);
    }

    @Override
    public void visitLoadBoolInstruction(String label, String target, boolean value) {
        // TODO Auto-generated method stub
        super.visitLoadBoolInstruction(label, target, value);
    }

    @Override
    public void visitLoadIntInstruction(String label, String target, int value) {
        // TODO Auto-generated method stub
        super.visitLoadIntInstruction(label, target, value);
    }

    @Override
    public void visitLoadStringInstruction(String label, String target, String value) {
        // TODO Auto-generated method stub
        super.visitLoadStringInstruction(label, target, value);
    }

    @Override
    public void visitLoadVariableInstruction(String label, String target, String source) {
        // TODO Auto-generated method stub
        super.visitLoadVariableInstruction(label, target, source);
    }

    @Override
    public void visitLoadVoidInstruction(String label, String target) {
        // TODO Auto-generated method stub
        super.visitLoadVoidInstruction(label, target);
    }

    @Override
    public void visitNewInstruction(String label, String target, String type) {
        // TODO Auto-generated method stub
        super.visitNewInstruction(label, target, type);
    }

    @Override
    public void visitReturnInstruction(String label, String returnVariable) {
        // TODO Auto-generated method stub
        super.visitReturnInstruction(label, returnVariable);
    }

    @Override
    public void visitBranchInstruction(String label, String target) {
        // TODO Auto-generated method stub
        super.visitBranchInstruction(label, target);
    }

}
