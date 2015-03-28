package net.alexweinert.coolc.representations.bytecode;

import java.util.List;

class FunctionCallInstruction extends AssignInstruction {

    private final String dispatchVariable;
    private final String methodId;
    private final List<String> arguments;

    public FunctionCallInstruction(String label, String resultVariable, String dispatchVariable, String functionId,
            List<String> arguments) {
        super(label, resultVariable);
        this.dispatchVariable = dispatchVariable;
        this.methodId = functionId;
        this.arguments = arguments;
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
        visitor.visitFunctionCallInstruction(this.getLabel(), this.getTarget(), this.dispatchVariable, this.methodId,
                this.arguments);
    }

}
