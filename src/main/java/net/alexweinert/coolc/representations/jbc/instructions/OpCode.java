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
            // TODO Auto-generated method stub
            return null;
        }

        public OpCode buildInvokeDynamic(char methodRefId) {
            // TODO Auto-generated method stub
            return null;
        }

        public OpCode buildIConst0() {
            // TODO Auto-generated method stub
            return null;
        }

        public OpCode buildIConst1() {
            // TODO Auto-generated method stub
            return null;
        }

        public OpCode buildNew() {
            // TODO Auto-generated method stub
            return null;
        }

        public OpCode buildAReturn() {
            // TODO Auto-generated method stub
            return null;
        }

        public OpCode buildAStore(char varId) {
            // TODO Auto-generated method stub
            return null;
        }

        public OpCode buildAConstNull() {
            // TODO Auto-generated method stub
            return null;
        }

        public OpCode buildIAdd() {
            // TODO Auto-generated method stub
            return null;
        }

        public OpCode buildIDiv() {
            // TODO Auto-generated method stub
            return null;
        }

        public OpCode buildISub() {
            // TODO Auto-generated method stub
            return null;
        }

        public OpCode buildIMul() {
            // TODO Auto-generated method stub
            return null;
        }

        public OpCode buildInstanceOf(char classRefId) {
            // TODO Auto-generated method stub
            return null;
        }

        public OpCode buildPushShort(int value) {
            // TODO Auto-generated method stub
            return null;
        }

        public OpCode buildLdc(char stringRef) {
            // TODO Auto-generated method stub
            return null;
        }

    }

    public abstract void encode(JbcEncoder encoder);

    public abstract int getLength(JbcEncoding encoding);
}
