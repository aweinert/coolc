package net.alexweinert.coolc.representations.bytecode;

import java.util.List;

class StaticFunctionCallInstruction extends FunctionCallInstruction {

    private final String staticType;

    public StaticFunctionCallInstruction(String label, String resultVariable, String dispatchVariable,
            String staticType, String functionId, List<String> arguments) {
        super(label, resultVariable, dispatchVariable, functionId, arguments);
        this.staticType = staticType;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((staticType == null) ? 0 : staticType.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        StaticFunctionCallInstruction other = (StaticFunctionCallInstruction) obj;
        if (staticType == null) {
            if (other.staticType != null) {
                return false;
            }
        } else if (!staticType.equals(other.staticType)) {
            return false;
        }
        return true;
    }

    public String getStaticType() {
        return staticType;
    }

}
