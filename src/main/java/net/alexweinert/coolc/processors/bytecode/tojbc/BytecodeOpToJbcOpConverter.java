package net.alexweinert.coolc.processors.bytecode.tojbc;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.alexweinert.coolc.processors.jbc.JbcEncoding;
import net.alexweinert.coolc.representations.bytecode.LabeledInstruction;
import net.alexweinert.coolc.representations.bytecode.TypedId;
import net.alexweinert.coolc.representations.bytecode.Visitor;
import net.alexweinert.coolc.representations.jbc.ConstantPoolEntry;
import net.alexweinert.coolc.representations.jbc.JbcClass;
import net.alexweinert.coolc.representations.jbc.instructions.OpCode;

class BytecodeOpToJbcOpConverter extends Visitor {

    public static BytecodeOpToJbcOpConverter create(List<TypedId> parameters, List<TypedId> localVariables,
            JbcClass.Builder classBuilder, JbcEncoding encoding) {
        final Map<String, Character> variableNameToNumber = new HashMap<>();
        variableNameToNumber.put("self", (char) 0);
        char id = 1;
        for (; id - 1 < parameters.size(); ++id) {
            variableNameToNumber.put(parameters.get(id - 1).getId(), id);
        }
        for (; id - 1 - parameters.size() < localVariables.size(); ++id) {
            variableNameToNumber.put(localVariables.get(id - 1 - parameters.size()).getId(), id);
        }
        return new BytecodeOpToJbcOpConverter(variableNameToNumber, new OpCodeAssembler(encoding), classBuilder);
    }

    private Map<String, Character> variableNameToNumber;
    private final OpCodeAssembler assembler;
    private final JbcClass.Builder classBuilder;

    private int usedLabels = 0;

    BytecodeOpToJbcOpConverter(Map<String, Character> variableNameToNumber, OpCodeAssembler assembler,
            JbcClass.Builder classBuilder) {
        this.variableNameToNumber = variableNameToNumber;
        this.assembler = assembler;
        this.classBuilder = classBuilder;
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
        this.assembler.addALoad((char) this.variableNameToNumber.get(lhs));
        this.assembler.addInvokeVirtual(getValueMethodRefId);
        this.assembler.addALoad(this.variableNameToNumber.get(rhs));
        this.assembler.addInvokeVirtual(getValueMethodRefId);
        final String labelTrue = "bcToJbc" + this.usedLabels++;
        this.assembler.addIfICmpLt(labelTrue);
        this.assembler.addIConst0();
        final String labelAfter = "bcToJbc" + this.usedLabels++;
        this.assembler.addGoto(labelAfter);
        this.assembler.addIConst1(labelTrue);
        this.assembler.addNop(labelAfter);

        this.initializeNewInstance("CoolBool");
        this.assembler.addAStore(this.variableNameToNumber.get(target));
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
        this.assembler.addALoad((char) this.variableNameToNumber.get(lhs));
        this.assembler.addInvokeVirtual(getValueMethodRefId);
        this.assembler.addALoad(this.variableNameToNumber.get(rhs));
        this.assembler.addInvokeVirtual(getValueMethodRefId);
        final String labelTrue = "bcToJbc" + this.usedLabels++;
        this.assembler.addIfICmpLe(labelTrue);
        this.assembler.addIConst0();
        final String labelAfter = "bcToJbc" + this.usedLabels++;
        this.assembler.addGoto(labelAfter);
        this.assembler.addIConst1(labelTrue);
        this.assembler.addNop(labelAfter);

        this.initializeNewInstance("CoolBool");
        this.assembler.addAStore(this.variableNameToNumber.get(target));
    }

    @Override
    public void visitEqInstruction(String label, String target, String lhs, String rhs) {
        final char equalsMethodRefId = addMethodRefConst("CoolObject", "equals", "(LCoolObject;)I");
        final char coolBoolClassRefId = addClassRefConst("CoolBool");
        if (label != null) {
            this.assembler.addNew(label, coolBoolClassRefId);
        } else {
            this.assembler.addNew(coolBoolClassRefId);
        }
        this.assembler.addDup();
        this.assembler.addALoad((char) this.variableNameToNumber.get(lhs));
        this.assembler.addALoad((char) this.variableNameToNumber.get(rhs));
        this.assembler.addInvokeVirtual(equalsMethodRefId);
        this.initializeNewInstance("CoolBool");
        this.assembler.addAStore((char) this.variableNameToNumber.get(target));
    }

    @Override
    public void visitBoolNegInstruction(String label, String target, String arg) {
        final char getValueMethodRefId = addMethodRefConst("CoolBool", "getValue", "()I");

        final char coolBoolClassRefIndex = this.addClassRefConst("CoolBool");
        if (label != null) {
            this.assembler.addNew(label, coolBoolClassRefIndex);
        } else {
            this.assembler.addNew(coolBoolClassRefIndex);
        }
        this.assembler.addDup();
        this.assembler.addALoad((char) this.variableNameToNumber.get(arg));
        this.assembler.addInvokeVirtual(getValueMethodRefId);
        final String labelFalse = "bcToJbc" + this.usedLabels++;
        this.assembler.addIfEq(labelFalse);
        this.assembler.addIConst1();
        final String labelAfter = "bcToJbc" + this.usedLabels++;
        this.assembler.addGoto(labelAfter);
        this.assembler.addIConst0(labelFalse);
        this.assembler.addNop(labelAfter);

        this.initializeNewInstance("CoolBool");
        this.assembler.addAStore(this.variableNameToNumber.get(target));
    }

    @Override
    public void visitBranchIfFalseInstruction(String label, String target, String conditionVariable) {
        final char getValueMethodRefId = addMethodRefConst("CoolBool", "getValue", "()I");

        if (label != null) {
            this.assembler.addALoad(label, (char) this.variableNameToNumber.get(conditionVariable));
        } else {
            this.assembler.addALoad((char) this.variableNameToNumber.get(conditionVariable));
        }

        this.assembler.addInvokeVirtual(getValueMethodRefId);
        this.assembler.addIfNe(target);
    }

    @Override
    public void visitBranchIfNotInstanceOfInstruction(String label, String target, String expressionVariable,
            String type) {
        if (label != null) {
            this.assembler.addALoad(label, (char) this.variableNameToNumber.get(expressionVariable));
        } else {
            this.assembler.addALoad((char) this.variableNameToNumber.get(expressionVariable));
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
        this.assembler.addALoad((char) this.variableNameToNumber.get(lhs));
        this.assembler.addInvokeVirtual(getValueMethodRefId);
        this.assembler.addALoad((char) this.variableNameToNumber.get(rhs));
        this.assembler.addInvokeVirtual(getValueMethodRefId);

        this.assembler.addIAdd();

        this.initializeNewInstance("CoolInt");
        this.assembler.addAStore(this.variableNameToNumber.get(target));
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
        this.assembler.addALoad((char) this.variableNameToNumber.get(lhs));
        this.assembler.addInvokeVirtual(getValueMethodRefId);
        this.assembler.addALoad((char) this.variableNameToNumber.get(rhs));
        this.assembler.addInvokeVirtual(getValueMethodRefId);

        this.assembler.addIDiv();

        this.initializeNewInstance("CoolInt");
        this.assembler.addAStore(this.variableNameToNumber.get(target));
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
        this.assembler.addALoad((char) this.variableNameToNumber.get(lhs));
        this.assembler.addInvokeVirtual(getValueMethodRefId);
        this.assembler.addALoad((char) this.variableNameToNumber.get(rhs));
        this.assembler.addInvokeVirtual(getValueMethodRefId);

        this.assembler.addIMul();

        this.initializeNewInstance("CoolInt");
        this.assembler.addAStore(this.variableNameToNumber.get(target));
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
        this.assembler.addALoad((char) this.variableNameToNumber.get(lhs));
        this.assembler.addInvokeVirtual(getValueMethodRefId);
        this.assembler.addALoad((char) this.variableNameToNumber.get(rhs));
        this.assembler.addInvokeVirtual(getValueMethodRefId);

        this.assembler.addISub();

        this.initializeNewInstance("CoolInt");
        this.assembler.addAStore(this.variableNameToNumber.get(target));
    }

    @Override
    public void visitFunctionCallInstruction(String label, String target, String dispatchVariable, String methodId,
            List<String> arguments) {
        // TODO Auto-generated method stub
        super.visitFunctionCallInstruction(label, target, dispatchVariable, methodId, arguments);
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
        this.assembler.addALoad((char) this.variableNameToNumber.get(arg));

        final String trueLabel = "bcToJbc" + usedLabels++;
        this.assembler.addIfNull(trueLabel);
        this.assembler.addIConst0();
        final String continueLabel = "bcToJbc" + usedLabels++;
        this.assembler.addGoto(continueLabel);
        this.assembler.addIConst1(trueLabel);
        this.assembler.addNop(continueLabel);

        this.initializeNewInstance("CoolBool");

        this.assembler.addAStore(this.variableNameToNumber.get(target));
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
        this.assembler.addAStore((char) this.variableNameToNumber.get(target));
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
        this.assembler.addAStore((char) this.variableNameToNumber.get(target));
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
        this.assembler.addAStore((char) this.variableNameToNumber.get(target));
    }

    @Override
    public void visitLoadVariableInstruction(String label, String target, String source) {
        if (label != null) {
            this.assembler.addALoad(label, (char) this.variableNameToNumber.get(source));
        } else {
            this.assembler.addALoad((char) this.variableNameToNumber.get(source));
        }
        this.assembler.addAStore((char) this.variableNameToNumber.get(target));
    }

    @Override
    public void visitLoadVoidInstruction(String label, String target) {
        if (label != null) {
            this.assembler.addAConstNull(label);
        } else {
            this.assembler.addAConstNull();
        }
        this.assembler.addAStore(this.variableNameToNumber.get(target));
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
        initializeNewInstance(type);
        this.assembler.addAStore(this.variableNameToNumber.get(target));
    }

    private void initializeNewInstance(String type) {
        if (type.equals("CoolInt") || type.equals("CoolBool")) {
            this.assembler.addInvokeSpecial(this.addMethodRefConst(type, "<init>", "(I)V"));
        } else if (type.equals("CoolString")) {
            this.assembler.addInvokeSpecial(this.addMethodRefConst(type, "<init>", "(java/lang/String)V"));
        } else {
            this.assembler.addInvokeSpecial(this.addMethodRefConst(type, "<init>", "()V"));
        }
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
                this.assembler.addALoad(label, (char) this.variableNameToNumber.get(returnVariable));
            } else {
                this.assembler.addALoad((char) this.variableNameToNumber.get(returnVariable));
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

    private char addMethodRefConst(final String classId, final String methodId, final String methodType) {
        final char coolIntClassRefIndex = addClassRefConst(classId);

        final char getValueMethodNameAndTypeId = addNameAndTypeConst(methodId, methodType);

        final ConstantPoolEntry getValueMethodRef = this.classBuilder.getConstantBuilder().buildMethodRef(
                coolIntClassRefIndex, getValueMethodNameAndTypeId);
        final char getValueMethodRefId = this.classBuilder.addConstant(getValueMethodRef);
        return getValueMethodRefId;
    }

    private char addNameAndTypeConst(final String name, final String type) {
        final char getValueMethodNameStringId = addUtf8Const(name);

        final char getValueMethodTypeStringId = addUtf8Const(type);

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

}
