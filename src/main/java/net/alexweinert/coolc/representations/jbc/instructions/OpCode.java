package net.alexweinert.coolc.representations.jbc.instructions;

import net.alexweinert.coolc.processors.jbc.JbcEncoder;
import net.alexweinert.coolc.processors.jbc.JbcEncoding;

public abstract class OpCode {

    public static class Factory {
        public OpCode buildNop() {
            return new Nop();
        }

        public OpCode buildGoto(final char target) {
            return new GotoInstruction(target);
        }

        public OpCode buildALoad(char varIndex) {
            return new ALoadInstruction(varIndex);
        }

        public OpCode buildInvokeDynamic(char methodRefId) {
            return new InvokeDynamicInstruction(methodRefId);
        }

        public OpCode buildIConst0() {
            return new IConst0Instruction();
        }

        public OpCode buildIConst1() {
            return new IConst1Instruction();
        }

        public OpCode buildNew(char classRefIndex) {
            return new NewInstruction(classRefIndex);
        }

        public OpCode buildAReturn() {
            return new AReturnInstruction();
        }

        public OpCode buildAStore(char varId) {
            return new AStoreInstruction(varId);
        }

        public OpCode buildAConstNull() {
            return new AConstNullInstruction();
        }

        public OpCode buildIAdd() {
            return new IAddInstruction();
        }

        public OpCode buildIDiv() {
            return new IDivInstruction();
        }

        public OpCode buildISub() {
            return new ISubInstruction();
        }

        public OpCode buildIMul() {
            return new IMulInstruction();
        }

        public OpCode buildInstanceOf(char classRefId) {
            return new InstanceOfInstruction(classRefId);
        }

        public OpCode buildPushShort(int value) {
            return new PushShortInstruction(value);
        }

        public OpCode buildLdc(char stringRef) {
            return new LdcWInstruction(stringRef);
        }

        public OpCode buildIfEq(char target) {
            return new IfEqInstruction(target);
        }

        public OpCode buildIfIcmpLe(char target) {
            return new IfIcmpLeInstruction(target);
        }

        public OpCode buildIfIcmpLt(char target) {
            return new IfIcmpLtInstruction(target);
        }

        public OpCode buildIfNeq(char target) {
            return new IfNeqInstruction(target);
        }

        public OpCode buildIfNull(char target) {
            return new IfNullInstruction(target);
        }

        public OpCode buildReturn() {
            return new ReturnInstruction();
        }

        public OpCode buildInvokeVirtual(char methodRefId) {
            return new InvokeVirtual(methodRefId);
        }

        public OpCode buildDup() {
            return new Dup();
        }

        public OpCode buildInvokeSpecial(char methodRefId) {
            return new InvokeSpecial(methodRefId);
        }

        public OpCode buildPop() {
            return new Pop();
        }

        public OpCode buildGetField(char fieldRefId) {
            return new GetField(fieldRefId);
        }

        public OpCode buildPutField(char fieldRefId) {
            return new PutField(fieldRefId);
        }

        public OpCode buildSwap() {
            return new Swap();
        }

    }

    public abstract void encode(JbcEncoder encoder);

    public abstract int getLength(JbcEncoding encoding);
}
