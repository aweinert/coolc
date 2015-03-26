package net.alexweinert.coolc.representations.bytecode;

import java.util.LinkedList;
import java.util.List;

public class Method {

    public static class Builder {
        private final String returnType;
        private final String id;
        private final List<TypedId> parameters = new LinkedList<>();
        private final List<TypedId> localVars = new LinkedList<>();
        private final List<LabeledInstruction> instruction = new LinkedList<>();

        public Builder(String returnType, String id) {
            this.returnType = returnType;
            this.id = id;
        }

        public Builder addParameter(String type, String id) {
            this.parameters.add(new TypedId(type, id));
            return this;
        }

        public Builder addLocalVar(String type, String id) {
            this.localVars.add(new TypedId(type, id));
            return this;
        }

        public Builder addInstruction(LabeledInstruction instruction) {
            this.instruction.add(instruction);
            return this;
        }

        public Method build() {
            return new Method(returnType, id, parameters, localVars, instruction);
        }
    }

    private final String returnType;
    private final String id;
    private final List<TypedId> parameters;
    private final List<TypedId> localVars;
    private final List<LabeledInstruction> instruction;

    private Method(String returnType, String id, List<TypedId> parameters, List<TypedId> localVars,
            List<LabeledInstruction> instruction) {
        this.returnType = returnType;
        this.id = id;
        this.parameters = parameters;
        this.localVars = localVars;
        this.instruction = instruction;
    }

    public String getReturnType() {
        return returnType;
    }

    public String getId() {
        return id;
    }

    public List<TypedId> getParameters() {
        return parameters;
    }

    public List<TypedId> getLocalVars() {
        return localVars;
    }

    public List<LabeledInstruction> getInstruction() {
        return instruction;
    }
}
