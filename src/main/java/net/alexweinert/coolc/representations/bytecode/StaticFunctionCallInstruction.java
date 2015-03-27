package net.alexweinert.coolc.representations.bytecode;

import java.util.List;

class StaticFunctionCallInstruction extends FunctionCallInstruction {

    private final String staticType;

    public StaticFunctionCallInstruction(String label, String resultVariable, String dispatchVariable,
            String staticType, String functionId, List<String> arguments) {
        super(label, resultVariable, dispatchVariable, functionId, arguments);
        this.staticType = staticType;
    }

    public String getStaticType() {
        return staticType;
    }

}
