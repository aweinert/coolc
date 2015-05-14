package net.alexweinert.coolc.processors.bytecode.tojbc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.alexweinert.coolc.processors.jbc.JbcEncoding;
import net.alexweinert.coolc.representations.bytecode.ByteClass;
import net.alexweinert.coolc.representations.bytecode.LabeledInstruction;
import net.alexweinert.coolc.representations.bytecode.TypedId;
import net.alexweinert.coolc.representations.bytecode.Visitor;
import net.alexweinert.coolc.representations.jbc.ConstantPoolEntry;
import net.alexweinert.coolc.representations.jbc.JbcClass;
import net.alexweinert.coolc.representations.jbc.instructions.OpCode;

class BytecodeOpToJbcOpConverter extends Visitor {

    public static BytecodeOpToJbcOpConverter create(ByteClass enclosingClass, List<TypedId> parameters,
            List<TypedId> localVariables, JbcClass.Builder classBuilder, JbcEncoding encoding) {
        final Map<String, Character> variableNameToNumber = new HashMap<>();
        variableNameToNumber.put("self", (char) 0);
        char id = 1;
        for (; id - 1 < parameters.size(); ++id) {
            variableNameToNumber.put(parameters.get(id - 1).getId(), id);
        }
        for (; id - 1 - parameters.size() < localVariables.size(); ++id) {
            variableNameToNumber.put(localVariables.get(id - 1 - parameters.size()).getId(), id);
        }
        return new BytecodeOpToJbcOpConverter(variableNameToNumber, new OpCodeAssembler(encoding), classBuilder,
                enclosingClass);
    }

    private Map<String, Character> variableNameToNumber;
    private final OpCodeAssembler assembler;
    private final JbcClass.Builder classBuilder;
    private final ByteClass enclosingClass;

    private int usedLabels = 0;

    BytecodeOpToJbcOpConverter(Map<String, Character> variableNameToNumber, OpCodeAssembler assembler,
            JbcClass.Builder classBuilder, ByteClass enclosingClass) {
        this.variableNameToNumber = variableNameToNumber;
        this.assembler = assembler;
        this.classBuilder = classBuilder;
        this.enclosingClass = enclosingClass;
    }

    public List<OpCode> convert(List<LabeledInstruction> list) {
        for (LabeledInstruction instr : list) {
            instr.acceptVisitor(this);
        }
        return assembler.assemble();
    }

    @Override
    public void visitLtInstruction(String label, String target, String lhs, String rhs) {
        final char getValueMethodRefId = addMethodRefConst("CoolInt", "getValue", "()I");

        final char coolBoolClassRefIndex = this.addClassRefConst("CoolBool");
        if (label != null) {
            this.assembler.addNew(label, coolBoolClassRefIndex);
        } else {
            this.assembler.addNew(coolBoolClassRefIndex);
        }
        this.assembler.addDup();
        this.loadVariable(lhs);
        this.assembler.addInvokeVirtual(getValueMethodRefId);
        this.loadVariable(rhs);
        this.assembler.addInvokeVirtual(getValueMethodRefId);
        final String labelTrue = "bcToJbc" + this.usedLabels++;
        this.assembler.addIfICmpLt(labelTrue);
        this.assembler.addIConst0();
        final String labelAfter = "bcToJbc" + this.usedLabels++;
        this.assembler.addGoto(labelAfter);
        this.assembler.addIConst1(labelTrue);
        this.assembler.addNop(labelAfter);

        this.initializeNewInstance("CoolBool");
        this.storeVariable(target);
    }

    @Override
    public void visitLteInstruction(String label, String target, String lhs, String rhs) {
        final char getValueMethodRefId = addMethodRefConst("CoolInt", "getValue", "()I");

        final char coolBoolClassRefIndex = this.addClassRefConst("CoolBool");
        if (label != null) {
            this.assembler.addNew(label, coolBoolClassRefIndex);
        } else {
            this.assembler.addNew(coolBoolClassRefIndex);
        }
        this.assembler.addDup();
        this.loadVariable(lhs);
        this.assembler.addInvokeVirtual(getValueMethodRefId);
        this.loadVariable(rhs);
        this.assembler.addInvokeVirtual(getValueMethodRefId);
        final String labelTrue = "bcToJbc" + this.usedLabels++;
        this.assembler.addIfICmpLe(labelTrue);
        this.assembler.addIConst0();
        final String labelAfter = "bcToJbc" + this.usedLabels++;
        this.assembler.addGoto(labelAfter);
        this.assembler.addIConst1(labelTrue);
        this.assembler.addNop(labelAfter);

        this.initializeNewInstance("CoolBool");
        this.storeVariable(target);
    }

    @Override
    public void visitEqInstruction(String label, String target, String lhs, String rhs) {
        final char equalsMethodRefId = addMethodRefConst("CoolObject", "equals", "(Ljava/lang/Object;)Z");
        final char coolBoolClassRefId = addClassRefConst("CoolBool");
        if (label != null) {
            this.assembler.addNew(label, coolBoolClassRefId);
        } else {
            this.assembler.addNew(coolBoolClassRefId);
        }
        this.assembler.addDup();
        this.loadVariable(lhs);
        this.loadVariable(rhs);
        this.assembler.addInvokeVirtual(equalsMethodRefId);
        this.initializeNewInstance("CoolBool");
        this.storeVariable(target);
    }

    @Override
    public void visitBoolNegInstruction(String label, String target, String arg) {
        final char getValueMethodRefId = addMethodRefConst("CoolBool", "getValue", "()Z");

        final char coolBoolClassRefIndex = this.addClassRefConst("CoolBool");
        if (label != null) {
            this.assembler.addNew(label, coolBoolClassRefIndex);
        } else {
            this.assembler.addNew(coolBoolClassRefIndex);
        }
        this.assembler.addDup();
        this.loadVariable(arg);
        this.assembler.addInvokeVirtual(getValueMethodRefId);
        final String labelFalse = "bcToJbc" + this.usedLabels++;
        this.assembler.addIfEq(labelFalse);
        this.assembler.addIConst1();
        final String labelAfter = "bcToJbc" + this.usedLabels++;
        this.assembler.addGoto(labelAfter);
        this.assembler.addIConst0(labelFalse);
        this.assembler.addNop(labelAfter);

        this.initializeNewInstance("CoolBool");
        this.storeVariable(target);
    }

    @Override
    public void visitBranchIfFalseInstruction(String label, String target, String conditionVariable) {
        final char getValueMethodRefId = addMethodRefConst("CoolBool", "getValue", "()Z");

        if (label != null) {
            this.loadVariable(label, conditionVariable);
        } else {
            this.loadVariable(conditionVariable);
        }

        this.assembler.addInvokeVirtual(getValueMethodRefId);
        this.assembler.addIfNe(target);
    }

    @Override
    public void visitBranchIfNotInstanceOfInstruction(String label, String target, String expressionVariable,
            String type) {
        if (label != null) {
            this.loadVariable(label, expressionVariable);
        } else {
            this.loadVariable(expressionVariable);
        }

        final char classRefId = this.addClassRefConst("Cool" + type);
        this.assembler.addInstanceof(classRefId);
        this.assembler.addIfNe(target);
    }

    @Override
    public void visitAddInstruction(String label, String target, String lhs, String rhs) {
        final char getValueMethodRefId = addMethodRefConst("CoolInt", "getValue", "()I");

        final char coolIntClassRefIndex = this.addClassRefConst("CoolInt");
        if (label != null) {
            this.assembler.addNew(label, coolIntClassRefIndex);
        } else {
            this.assembler.addNew(coolIntClassRefIndex);
        }
        this.assembler.addDup();
        this.loadVariable(lhs);
        this.assembler.addInvokeVirtual(getValueMethodRefId);
        this.loadVariable(rhs);
        this.assembler.addInvokeVirtual(getValueMethodRefId);

        this.assembler.addIAdd();

        this.initializeNewInstance("CoolInt");
        this.storeVariable(target);
    }

    @Override
    public void visitDivInstruction(String label, String target, String lhs, String rhs) {
        final char getValueMethodRefId = addMethodRefConst("CoolInt", "getValue", "()I");

        final char coolIntClassRefIndex = this.addClassRefConst("CoolInt");
        if (label != null) {
            this.assembler.addNew(label, coolIntClassRefIndex);
        } else {
            this.assembler.addNew(coolIntClassRefIndex);
        }
        this.assembler.addDup();
        this.loadVariable(lhs);
        this.assembler.addInvokeVirtual(getValueMethodRefId);
        this.loadVariable(rhs);
        this.assembler.addInvokeVirtual(getValueMethodRefId);

        this.assembler.addIDiv();

        this.initializeNewInstance("CoolInt");
        this.storeVariable(target);
    }

    @Override
    public void visitMulInstruction(String label, String target, String lhs, String rhs) {
        final char getValueMethodRefId = addMethodRefConst("CoolInt", "getValue", "()I");

        final char coolIntClassRefIndex = this.addClassRefConst("CoolInt");
        if (label != null) {
            this.assembler.addNew(label, coolIntClassRefIndex);
        } else {
            this.assembler.addNew(coolIntClassRefIndex);
        }
        this.assembler.addDup();
        this.loadVariable(lhs);
        this.assembler.addInvokeVirtual(getValueMethodRefId);
        this.loadVariable(rhs);
        this.assembler.addInvokeVirtual(getValueMethodRefId);

        this.assembler.addIMul();

        this.initializeNewInstance("CoolInt");
        this.storeVariable(target);
    }

    @Override
    public void visitSubInstruction(String label, String target, String lhs, String rhs) {
        final char getValueMethodRefId = addMethodRefConst("CoolInt", "getValue", "()I");

        final char coolIntClassRefIndex = this.addClassRefConst("CoolInt");
        if (label != null) {
            this.assembler.addNew(label, coolIntClassRefIndex);
        } else {
            this.assembler.addNew(coolIntClassRefIndex);
        }
        this.assembler.addDup();
        this.loadVariable(lhs);
        this.assembler.addInvokeVirtual(getValueMethodRefId);
        this.loadVariable(rhs);
        this.assembler.addInvokeVirtual(getValueMethodRefId);

        this.assembler.addISub();

        this.initializeNewInstance("CoolInt");
        this.storeVariable(target);
    }

    @Override
    public void visitFunctionCallInstruction(String label, String target, String dispatchVariable,
            String dispatchVariableType, String methodId, String returnType, List<String> arguments,
            List<String> argumentTypes) {
        if (label != null) {
            this.loadVariable(label, dispatchVariable);
        } else {
            this.loadVariable(dispatchVariable);
        }
        for (String argumentVariable : arguments) {
            this.loadVariable(argumentVariable);
        }
        final StringBuilder methodDeclBuilder = new StringBuilder("(");
        for (String argumentType : argumentTypes) {
            methodDeclBuilder.append("L");
            methodDeclBuilder.append("Cool" + argumentType);
            methodDeclBuilder.append(";");
        }
        methodDeclBuilder.append(")L");
        methodDeclBuilder.append("Cool" + returnType);
        methodDeclBuilder.append(";");
        final char methodRefId = this.addMethodRefConst("Cool" + dispatchVariableType, methodId,
                methodDeclBuilder.toString());
        this.assembler.addInvokeVirtual(methodRefId);
        this.storeVariable(target);
    }

    @Override
    public void visitIsVoidInstruction(String label, String target, String arg) {
        final char coolBoolClassRefId = this.addClassRefConst("CoolBool");
        if (label != null) {
            this.assembler.addNew(label, coolBoolClassRefId);
        } else {
            this.assembler.addNew(coolBoolClassRefId);
        }
        this.assembler.addDup();
        this.loadVariable(arg);

        final String trueLabel = "bcToJbc" + usedLabels++;
        this.assembler.addIfNull(trueLabel);
        this.assembler.addIConst0();
        final String continueLabel = "bcToJbc" + usedLabels++;
        this.assembler.addGoto(continueLabel);
        this.assembler.addIConst1(trueLabel);
        this.assembler.addNop(continueLabel);

        this.initializeNewInstance("CoolBool");

        this.storeVariable(target);
    }

    @Override
    public void visitLoadBoolInstruction(String label, String target, boolean value) {
        final char coolBoolClassRefId = this.addClassRefConst("CoolBool");
        if (label != null) {
            this.assembler.addNew(label, coolBoolClassRefId);
        } else {
            this.assembler.addNew(coolBoolClassRefId);
        }
        this.assembler.addDup();
        if (value) {
            this.assembler.addIConst1();
        } else {
            this.assembler.addIConst0();
        }
        this.initializeNewInstance("CoolBool");
        this.storeVariable(target);
    }

    @Override
    public void visitLoadIntInstruction(String label, String target, int value) {
        final char coolIntClassRefId = this.addClassRefConst("CoolInt");
        if (label != null) {
            this.assembler.addNew(label, coolIntClassRefId);
        } else {
            this.assembler.addNew(coolIntClassRefId);
        }
        this.assembler.addDup();
        this.assembler.addPushShort(value);
        this.initializeNewInstance("CoolInt");
        this.storeVariable(target);
    }

    @Override
    public void visitLoadStringInstruction(String label, String target, String value) {
        final char stringRef = this.addStringConst(value);

        final char coolStringClassRefId = this.addClassRefConst("CoolString");

        if (label != null) {
            this.assembler.addNew(label, coolStringClassRefId);
        } else {
            this.assembler.addNew(coolStringClassRefId);
        }
        this.assembler.addDup();
        this.assembler.pushLdc(stringRef);
        this.initializeNewInstance("CoolString");
        this.storeVariable(target);
    }

    @Override
    public void visitLoadVariableInstruction(String label, String target, String source) {
        if (label != null) {
            this.loadVariable(label, source);
        } else {
            this.loadVariable(source);
        }
        this.storeVariable(target);
    }

    @Override
    public void visitLoadVoidInstruction(String label, String target) {
        if (label != null) {
            this.assembler.addAConstNull(label);
        } else {
            this.assembler.addAConstNull();
        }
        this.storeVariable(target);
    }

    @Override
    public void visitNewInstruction(String label, String target, String type) {
        final char typeReferenceId = this.addClassRefConst("Cool" + type);
        if (label != null) {
            this.assembler.addNew(label, typeReferenceId);
        } else {
            this.assembler.addNew(typeReferenceId);
        }
        this.assembler.addDup();
        initializeNewInstance("Cool" + type);
        this.storeVariable(target);
    }

    private void initializeNewInstance(String type) {
        if (type.equals("CoolInt")) {
            this.assembler.addInvokeSpecial(this.addMethodRefConst(type, "<init>", "(I)V"));
        } else if (type.equals("CoolBool")) {
            this.assembler.addInvokeSpecial(this.addMethodRefConst(type, "<init>", "(Z)V"));
        } else if (type.equals("CoolString")) {
            this.assembler.addInvokeSpecial(this.addMethodRefConst(type, "<init>", "(Ljava/lang/String;)V"));
        } else {
            this.assembler.addInvokeSpecial(this.addMethodRefConst(type, "<init>", "()V"));
        }
    }

    private void loadVariable(String label, String id) {
        assert label != null;
        if (this.variableNameToNumber.containsKey(id)) {
            this.assembler.addALoad(label, this.variableNameToNumber.get(id));
        } else {
            this.assembler.addALoad(label, (char) 0);
            this.assembler.addGetField(this.addFieldRefConst("Cool" + this.enclosingClass.getId(), id, "Cool"
                    + this.enclosingClass.getAttribute(id).getType()));
        }
    }

    private void loadVariable(String id) {
        if (this.variableNameToNumber.containsKey(id)) {
            this.assembler.addALoad(this.variableNameToNumber.get(id));
        } else {
            this.assembler.addALoad((char) 0);
            this.assembler.addGetField(this.addFieldRefConst("Cool" + this.enclosingClass.getId(), id, "Cool"
                    + this.enclosingClass.getAttribute(id).getType()));
        }
    }

    private void storeVariable(String label, String id) {
        assert label != null;
        if (this.variableNameToNumber.containsKey(id)) {
            this.assembler.addAStore(label, this.variableNameToNumber.get(id));
        } else {
            this.assembler.addALoad(label, (char) 0);
            this.assembler.addSwap();
            this.assembler.addPutField(this.addFieldRefConst("Cool" + this.enclosingClass.getId(), id, "Cool"
                    + this.enclosingClass.getAttribute(id).getType()));
        }
    }

    private void storeVariable(String id) {
        if (this.variableNameToNumber.containsKey(id)) {
            this.assembler.addAStore(this.variableNameToNumber.get(id));
        } else {
            this.assembler.addALoad((char) 0);
            this.assembler.addSwap();
            this.assembler.addPutField(this.addFieldRefConst("Cool" + this.enclosingClass.getId(), id, "Cool"
                    + this.enclosingClass.getAttribute(id).getType()));
        }
    }

    private char addFieldRefConst(String classId, String fieldId, String fieldType) {
        final char classIdRef = this.addClassRefConst(classId);
        final char fieldIdAndTypeRef = this.addNameAndTypeConst(fieldId, "L" + fieldType + ";");
        final ConstantPoolEntry fieldRefConst = this.classBuilder.getConstantBuilder().buildFieldRef(classIdRef,
                fieldIdAndTypeRef);
        return this.classBuilder.addConstant(fieldRefConst);
    }

    @Override
    public void visitReturnInstruction(String label, String returnVariable) {
        if (returnVariable == null) {
            if (label != null) {
                this.assembler.addReturn(label);
            } else {
                this.assembler.addReturn();
            }
        } else {
            if (label != null) {
                this.loadVariable(label, returnVariable);
            } else {
                this.loadVariable(returnVariable);
            }
            this.assembler.addAReturn();
        }
    }

    @Override
    public void visitBranchInstruction(String label, String target) {
        if (label != null) {
            this.assembler.addGoto(label, target);
        } else {
            this.assembler.addGoto(target);
        }
    }

    private char addMethodRefConst(final String classId, final String methodId, final String methodDescriptor) {
        final char coolIntClassRefIndex = addClassRefConst(classId);

        final char getValueMethodNameAndTypeId = addNameAndTypeConst(methodId, methodDescriptor);

        final ConstantPoolEntry getValueMethodRef = this.classBuilder.getConstantBuilder().buildMethodRef(
                coolIntClassRefIndex, getValueMethodNameAndTypeId);
        final char getValueMethodRefId = this.classBuilder.addConstant(getValueMethodRef);
        return getValueMethodRefId;
    }

    private char addNameAndTypeConst(final String name, final String descriptor) {
        final char getValueMethodNameStringId = addUtf8Const(name);

        final char getValueMethodTypeStringId = addUtf8Const(descriptor);

        final ConstantPoolEntry getValueMethodNameAndType = this.classBuilder.getConstantBuilder().buildNameAndType(
                getValueMethodNameStringId, getValueMethodTypeStringId);
        final char getValueMethodNameAndTypeId = this.classBuilder.addConstant(getValueMethodNameAndType);
        return getValueMethodNameAndTypeId;
    }

    private char addUtf8Const(final String name) {
        final ConstantPoolEntry getValueMethodNameString = this.classBuilder.getConstantBuilder().buildUtf8Constant(
                name);
        final char getValueMethodNameStringId = this.classBuilder.addConstant(getValueMethodNameString);
        return getValueMethodNameStringId;
    }

    private char addClassRefConst(final String classId) {
        final char coolIntTypeStringIndex = addUtf8Const(classId);

        final ConstantPoolEntry coolIntClassRef = this.classBuilder.getConstantBuilder().buildClassConstant(
                coolIntTypeStringIndex);
        final char coolIntClassRefIndex = this.classBuilder.addConstant(coolIntClassRef);
        return coolIntClassRefIndex;
    }

    private char addStringConst(final String value) {
        final char utf8Index = addUtf8Const(value);
        final ConstantPoolEntry stringRef = this.classBuilder.getConstantBuilder().buildStringConstant(utf8Index);
        final char stringRefId = this.classBuilder.addConstant(stringRef);
        return stringRefId;
    }

    @Override
    public void visitNopInstruction(String label) {
        if (label != null) {
            this.assembler.addNop(label);
        } else {
            this.assembler.addNop();
        }
    }

    @Override
    public void visitCastInstruction(String label, String variable, String type) {
        if (label != null) {
            this.loadVariable(label, variable);
        } else {
            this.loadVariable(variable);
        }
        final char classRefId = this.addClassRefConst("Cool" + type);
        this.assembler.addCheckCast(classRefId);
        this.storeVariable(variable);
    }

}
