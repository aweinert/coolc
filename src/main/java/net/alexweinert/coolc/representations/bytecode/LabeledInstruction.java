package net.alexweinert.coolc.representations.bytecode;

import java.util.List;

public abstract class LabeledInstruction {
    public static class Factory {

        public void setLabel(String label) {
            // TODO Auto-generated method stub
        }

        public LabeledInstruction buildLoadVoid(String target) {
            // TODO Auto-generated method stub
            return null;
        }

        public LabeledInstruction buildLoadBool(String target, boolean value) {
            // TODO Auto-generated method stub
            return null;
        }

        public LabeledInstruction buildLoadInt(String target, int value) {
            // TODO Auto-generated method stub
            return null;
        }

        public LabeledInstruction buildLoadString(String target, String value) {
            // TODO Auto-generated method stub
            return null;
        }

        public LabeledInstruction buildAssign(String target, String source) {
            // TODO Auto-generated method stub
            return null;
        }

        public LabeledInstruction buildAdd(String result, String lhs, String rhs) {
            // TODO Auto-generated method stub
            return null;
        }

        public LabeledInstruction buildSub(String result, String lhs, String rhs) {
            // TODO Auto-generated method stub
            return null;
        }

        public LabeledInstruction buildMul(String result, String lhs, String rhs) {
            // TODO Auto-generated method stub
            return null;
        }

        public LabeledInstruction buildDiv(String result, String lhs, String rhs) {
            // TODO Auto-generated method stub
            return null;
        }

        public LabeledInstruction buildArithNeg(String result, String arg) {
            // TODO Auto-generated method stub
            return null;
        }

        public LabeledInstruction buildIsVoid(String result, String arg) {
            // TODO Auto-generated method stub
            return null;
        }

        public LabeledInstruction buildBoolNeg(String result, String arg) {
            // TODO Auto-generated method stub
            return null;
        }

        public LabeledInstruction buildLt(String result, String lhs, String rhs) {
            // TODO Auto-generated method stub
            return null;
        }

        public LabeledInstruction buildLte(String result, String lhs, String rhs) {
            // TODO Auto-generated method stub
            return null;
        }

        public LabeledInstruction buildEq(String result, String lhs, String rhs) {
            // TODO Auto-generated method stub
            return null;
        }

        public LabeledInstruction buildNew(String returnVariable, String string) {
            // TODO Auto-generated method stub
            return null;
        }

        public LabeledInstruction buildFunctionCall(String resultVariable, String dispatchVariable, String string,
                List<String> arguments) {
            // TODO Auto-generated method stub
            return null;
        }

        public LabeledInstruction buildStaticFunctionCall(String resultVariable, String dispatchVariable,
                String string, String string2, List<String> arguments) {
            // TODO Auto-generated method stub
            return null;
        }

        public LabeledInstruction buildBranch(String afterLabel) {
            // TODO Auto-generated method stub
            return null;
        }

        public LabeledInstruction buildBranchIfFalse(String conditionVariable, String afterLabel) {
            // TODO Auto-generated method stub
            return null;
        }

        public LabeledInstruction buildBranchIfNotInstanceOf(String expressionVariable, String nextLabel) {
            // TODO Auto-generated method stub
            return null;
        }

        public LabeledInstruction buildReturn(String returnVariable) {
            // TODO Auto-generated method stub
            return null;
        }

    }

    private final String label;

    public LabeledInstruction(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
