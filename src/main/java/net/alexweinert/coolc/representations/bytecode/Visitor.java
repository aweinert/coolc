package net.alexweinert.coolc.representations.bytecode;

import java.util.List;

public class Visitor {

    public void visitArithNegInstruction(String label, String target, String arg) {}

    public void visitLtInstruction(String label, String target, String lhs, String rhs) {}

    public void visitLteInstruction(String label, String target, String lhs, String rhs) {}

    public void visitBoolNegInstruction(String label, String target, String arg) {}

    public void visitBranchIfFalseInstruction(String label, String target, String conditionVariable) {}

    public void visitBranchIfNotInstanceOfInstruction(String label, String target, String expressionVariable,
            String type) {}

    public void visitAddInstruction(String label, String target, String lhs, String rhs) {}

    public void visitDivInstruction(String label, String target, String lhs, String rhs) {}

    public void visitMulInstruction(String label, String target, String lhs, String rhs) {}

    public void visitSubInstruction(String label, String target, String lhs, String rhs) {}

    public void visitEqInstruction(String label, String target, String lhs, String rhs) {}

    public void visitFunctionCallInstruction(String label, String target, String dispatchVariable,
            String dispatchVariableType, String methodId, String returnType, List<String> arguments,
            List<String> argumentTypes) {}

    public void visitIsVoidInstruction(String label, String target, String arg) {}

    public void visitLoadBoolInstruction(String label, String target, boolean value) {}

    public void visitLoadIntInstruction(String label, String target, int value) {}

    public void visitLoadStringInstruction(String label, String target, String value) {}

    public void visitLoadVariableInstruction(String label, String target, String source) {}

    public void visitLoadVoidInstruction(String label, String target) {}

    public void visitNewInstruction(String label, String target, String type) {}

    public void visitReturnInstruction(String label, String returnVariable) {}

    public void visitBranchInstruction(String label, String target) {}

    public void visitNopInstruction(String label) {}

    public void visitCastInstruction(String label, String variable, String type) {}

}
