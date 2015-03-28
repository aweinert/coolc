package net.alexweinert.coolc.representations.bytecode;

import java.util.List;

public abstract class LabeledInstruction {
    public static class Factory {
        private String label = null;

        public void setLabel(String label) {
            this.label = label;
        }

        public LabeledInstruction buildLoadVoid(String target) {
            final LabeledInstruction returnValue = new LoadVoidInstruction(this.label, target);
            this.label = null;
            return returnValue;
        }

        public LabeledInstruction buildLoadBool(String target, boolean value) {
            final LabeledInstruction returnValue = new LoadBoolInstruction(this.label, target, value);
            this.label = null;
            return returnValue;
        }

        public LabeledInstruction buildLoadInt(String target, int value) {
            final LabeledInstruction returnValue = new LoadIntInstruction(this.label, target, value);
            this.label = null;
            return returnValue;
        }

        public LabeledInstruction buildLoadString(String target, String value) {
            final LabeledInstruction returnValue = new LoadStringInstruction(this.label, target, value);
            this.label = null;
            return returnValue;
        }

        public LabeledInstruction buildAssign(String target, String source) {
            final LabeledInstruction returnValue = new LoadVariableInstruction(this.label, target, source);
            this.label = null;
            return returnValue;
        }

        public LabeledInstruction buildAdd(String result, String lhs, String rhs) {
            final LabeledInstruction returnValue = BinaryArithmeticInstruction.createAddInstruction(this.label, result,
                    lhs, rhs);
            this.label = null;
            return returnValue;
        }

        public LabeledInstruction buildSub(String result, String lhs, String rhs) {
            final LabeledInstruction returnValue = BinaryArithmeticInstruction.createSubInstruction(this.label, result,
                    lhs, rhs);
            this.label = null;
            return returnValue;
        }

        public LabeledInstruction buildMul(String result, String lhs, String rhs) {
            final LabeledInstruction returnValue = BinaryArithmeticInstruction.createMulInstruction(this.label, result,
                    lhs, rhs);
            this.label = null;
            return returnValue;
        }

        public LabeledInstruction buildDiv(String result, String lhs, String rhs) {
            final LabeledInstruction returnValue = BinaryArithmeticInstruction.createDivInstruction(this.label, result,
                    lhs, rhs);
            this.label = null;
            return returnValue;
        }

        public LabeledInstruction buildArithNeg(String result, String arg) {
            final LabeledInstruction returnValue = new ArithNegInstruction(this.label, result, arg);
            this.label = null;
            return returnValue;
        }

        public LabeledInstruction buildIsVoid(String result, String arg) {
            final LabeledInstruction returnValue = new IsVoidInstruction(this.label, result, arg);
            this.label = null;
            return returnValue;
        }

        public LabeledInstruction buildBoolNeg(String result, String arg) {
            final LabeledInstruction returnValue = new BoolNegInstruction(this.label, result, arg);
            this.label = null;
            return returnValue;
        }

        public LabeledInstruction buildLt(String result, String lhs, String rhs) {
            final LabeledInstruction returnValue = ArithCompInstruction.createLtInstruction(this.label, result, lhs,
                    rhs);
            this.label = null;
            return returnValue;
        }

        public LabeledInstruction buildLte(String result, String lhs, String rhs) {
            final LabeledInstruction returnValue = ArithCompInstruction.createLteInstruction(this.label, result, lhs,
                    rhs);
            this.label = null;
            return returnValue;
        }

        public LabeledInstruction buildEq(String result, String lhs, String rhs) {
            final LabeledInstruction returnValue = new EqInstruction(this.label, result, lhs, rhs);
            this.label = null;
            return returnValue;
        }

        public LabeledInstruction buildNew(String returnVariable, String type) {
            final LabeledInstruction returnValue = new NewInstruction(this.label, returnVariable, type);
            this.label = null;
            return returnValue;
        }

        public LabeledInstruction buildFunctionCall(String resultVariable, String dispatchVariable, String functionId,
                List<String> arguments) {
            final LabeledInstruction returnValue = new FunctionCallInstruction(this.label, resultVariable,
                    dispatchVariable, functionId, arguments);
            this.label = null;
            return returnValue;
        }

        public LabeledInstruction buildStaticFunctionCall(String resultVariable, String dispatchVariable,
                String staticType, String functionId, List<String> arguments) {
            final LabeledInstruction returnValue = new StaticFunctionCallInstruction(this.label, resultVariable,
                    dispatchVariable, staticType, functionId, arguments);
            this.label = null;
            return returnValue;
        }

        public LabeledInstruction buildBranch(String target) {
            final LabeledInstruction returnValue = new UnconditionalBranchInstruction(this.label, target);
            this.label = null;
            return returnValue;
        }

        public LabeledInstruction buildBranchIfFalse(String conditionVariable, String target) {
            final LabeledInstruction returnValue = new BranchIfFalseInstruction(this.label, conditionVariable, target);
            this.label = null;
            return returnValue;
        }

        public LabeledInstruction buildBranchIfNotInstanceOf(String expressionVariable, String target) {
            final LabeledInstruction returnValue = new BranchIfNotInstanceOfInstruction(this.label, expressionVariable,
                    target);
            this.label = null;
            return returnValue;
        }

        public LabeledInstruction buildReturn(String returnVariable) {
            final LabeledInstruction returnValue = new ReturnInstruction(this.label, returnVariable);
            this.label = null;
            return returnValue;
        }

    }

    private final String label;

    public LabeledInstruction(String label) {
        this.label = label;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((label == null) ? 0 : label.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        LabeledInstruction other = (LabeledInstruction) obj;
        if (label == null) {
            if (other.label != null) {
                return false;
            }
        } else if (!label.equals(other.label)) {
            return false;
        }
        return true;
    }

    public String getLabel() {
        return label;
    }

    public abstract void acceptVisitor(Visitor visitor);
}
