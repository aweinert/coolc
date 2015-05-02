package net.alexweinert.coolc.representations.bytecode;

import java.util.List;

class FunctionCallInstruction extends AssignInstruction {

    private final String dispatchVariable;
    private final String dispatchVariableType;
    private final String methodId;
    private final List<String> arguments;
    private final List<String> argumentTypes;

    public FunctionCallInstruction(String label, String resultVariable, String dispatchVariable,
            String dispatchVariableType, String functionId, List<String> arguments, List<String> argumentTypes) {
        super(label, resultVariable);
        this.dispatchVariable = dispatchVariable;
        this.dispatchVariableType = dispatchVariableType;
        this.methodId = functionId;
        this.arguments = arguments;
        this.argumentTypes = argumentTypes;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((arguments == null) ? 0 : arguments.hashCode());
        result = prime * result + ((dispatchVariable == null) ? 0 : dispatchVariable.hashCode());
        result = prime * result + ((methodId == null) ? 0 : methodId.hashCode());
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
        FunctionCallInstruction other = (FunctionCallInstruction) obj;
        if (arguments == null) {
            if (other.arguments != null) {
                return false;
            }
        } else if (!arguments.equals(other.arguments)) {
            return false;
        }
        if (dispatchVariable == null) {
            if (other.dispatchVariable != null) {
                return false;
            }
        } else if (!dispatchVariable.equals(other.dispatchVariable)) {
            return false;
        }
        if (methodId == null) {
            if (other.methodId != null) {
                return false;
            }
        } else if (!methodId.equals(other.methodId)) {
            return false;
        }
        return true;
    }

    public String getDispatchVariable() {
        return dispatchVariable;
    }

    public String getMethodId() {
        return methodId;
    }

    public List<String> getArguments() {
        return arguments;
    }

    @Override
    public void acceptVisitor(Visitor visitor) {
        visitor.visitFunctionCallInstruction(this.getLabel(), this.getTarget(), this.dispatchVariable,
                this.dispatchVariableType, this.methodId, this.arguments);
    }

}
