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

    public static BytecodeOpToJbcOpConverter create(List<TypedId> localVariables, JbcClass.Builder classBuilder,
            JbcEncoding encoding) {
        final Map<String, Character> variableNameToNumber = new HashMap<>();
        char id = 0;
        for (TypedId typedId : localVariables) {
            variableNameToNumber.put(typedId.getId(), id++);
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

        this.assembler.addALoad(label, (char) this.variableNameToNumber.get(lhs));
        this.assembler.addInvokeDynamic(getValueMethodRefId);
        this.assembler.addALoad(this.variableNameToNumber.get(rhs));
        this.assembler.addInvokeDynamic(getValueMethodRefId);
        final String labelTrue = "bcToJbc" + this.usedLabels++;
        this.assembler.addIfICmpLt(labelTrue);
        this.assembler.addIConst0();
        final String labelAfter = "bcToJbc" + this.usedLabels++;
        this.assembler.addGoto(labelAfter);
        this.assembler.addIConst1(labelTrue);
        this.assembler.addNop(labelAfter);

        final char coolBoolClassRefIndex = this.addClassRefConst("CoolBool");
        this.assembler.addNew(coolBoolClassRefIndex);
    }

    @Override
    public void visitLteInstruction(String label, String target, String lhs, String rhs) {
        final char getValueMethodRefId = addMethodRefConst("CoolInt", "getValue", "()I");

        this.assembler.addALoad(label, (char) this.variableNameToNumber.get(lhs));
        this.assembler.addInvokeDynamic(getValueMethodRefId);
        this.assembler.addALoad(this.variableNameToNumber.get(rhs));
        this.assembler.addInvokeDynamic(getValueMethodRefId);
        final String labelTrue = "bcToJbc" + this.usedLabels++;
        this.assembler.addIfICmpLe(labelTrue);
        this.assembler.addIConst0();
        final String labelAfter = "bcToJbc" + this.usedLabels++;
        this.assembler.addGoto(labelAfter);
        this.assembler.addIConst1(labelTrue);
        this.assembler.addNop(labelAfter);

        final char coolBoolClassRefIndex = this.addClassRefConst("CoolBool");
        this.assembler.addNew(coolBoolClassRefIndex);
    }

    @Override
    public void visitBoolNegInstruction(String label, String target, String arg) {

    }

    @Override
    public void visitBranchIfFalseInstruction(String label, String target, String conditionVariable) {
        // TODO Auto-generated method stub
        super.visitBranchIfFalseInstruction(label, target, conditionVariable);
    }

    @Override
    public void visitBranchIfNotInstanceOfInstruction(String label, String target, String expressionVariable) {
        // TODO Auto-generated method stub
        super.visitBranchIfNotInstanceOfInstruction(label, target, expressionVariable);
    }

    @Override
    public void visitAddInstruction(String label, String target, String lhs, String rhs) {
        // TODO Auto-generated method stub
        super.visitAddInstruction(label, target, lhs, rhs);
    }

    @Override
    public void visitDivInstruction(String label, String target, String lhs, String rhs) {
        // TODO Auto-generated method stub
        super.visitDivInstruction(label, target, lhs, rhs);
    }

    @Override
    public void visitMulInstruction(String label, String target, String lhs, String rhs) {
        // TODO Auto-generated method stub
        super.visitMulInstruction(label, target, lhs, rhs);
    }

    @Override
    public void visitSubInstruction(String label, String target, String lhs, String rhs) {
        // TODO Auto-generated method stub
        super.visitSubInstruction(label, target, lhs, rhs);
    }

    @Override
    public void visitEqInstruction(String label, String target, String lhs, String rhs) {
        // TODO Auto-generated method stub
        super.visitEqInstruction(label, target, lhs, rhs);
    }

    @Override
    public void visitFunctionCallInstruction(String label, String target, String dispatchVariable, String methodId,
            List<String> arguments) {
        // TODO Auto-generated method stub
        super.visitFunctionCallInstruction(label, target, dispatchVariable, methodId, arguments);
    }

    @Override
    public void visitIsVoidInstruction(String label, String target, String arg) {
        // TODO Auto-generated method stub
        super.visitIsVoidInstruction(label, target, arg);
    }

    @Override
    public void visitLoadBoolInstruction(String label, String target, boolean value) {
        // TODO Auto-generated method stub
        super.visitLoadBoolInstruction(label, target, value);
    }

    @Override
    public void visitLoadIntInstruction(String label, String target, int value) {
        // TODO Auto-generated method stub
        super.visitLoadIntInstruction(label, target, value);
    }

    @Override
    public void visitLoadStringInstruction(String label, String target, String value) {
        // TODO Auto-generated method stub
        super.visitLoadStringInstruction(label, target, value);
    }

    @Override
    public void visitLoadVariableInstruction(String label, String target, String source) {
        // TODO Auto-generated method stub
        super.visitLoadVariableInstruction(label, target, source);
    }

    @Override
    public void visitLoadVoidInstruction(String label, String target) {
        // TODO Auto-generated method stub
        super.visitLoadVoidInstruction(label, target);
    }

    @Override
    public void visitNewInstruction(String label, String target, String type) {
        // TODO Auto-generated method stub
        super.visitNewInstruction(label, target, type);
    }

    @Override
    public void visitReturnInstruction(String label, String returnVariable) {
        // TODO Auto-generated method stub
        super.visitReturnInstruction(label, returnVariable);
    }

    @Override
    public void visitBranchInstruction(String label, String target) {
        // TODO Auto-generated method stub
        super.visitBranchInstruction(label, target);
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

}
