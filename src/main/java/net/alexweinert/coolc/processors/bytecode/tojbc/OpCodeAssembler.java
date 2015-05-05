package net.alexweinert.coolc.processors.bytecode.tojbc;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.alexweinert.coolc.processors.jbc.JbcEncoding;
import net.alexweinert.coolc.representations.jbc.instructions.OpCode;

public class OpCodeAssembler {

    private static class BranchPrototype {
        private enum Condition {
            NONE, EQZERO, LT, NEZERO, LE, REFEQNULL
        }

        private final Condition condition;
        private final String target;

        private final OpCode.Factory opCodeFactory = new OpCode.Factory();

        private BranchPrototype(Condition condition, String target) {
            this.condition = condition;
            this.target = target;
        }

        public String getTarget() {
            return target;
        }

        public int getLength(JbcEncoding encoding) {
            return 3;
        }

        public OpCode toOpcode(char target) {
            switch (this.condition) {
            case EQZERO:
                return opCodeFactory.buildIfEq(target);
            case LE:
                return opCodeFactory.buildIfIcmpLe(target);
            case LT:
                return opCodeFactory.buildIfIcmpLt(target);
            case NEZERO:
                return opCodeFactory.buildIfNeq(target);
            case NONE:
                return opCodeFactory.buildGoto(target);
            case REFEQNULL:
                return opCodeFactory.buildIfNull(target);
            default:
                assert false;
                return null;
            }
        }
    }

    private final JbcEncoding encoding;
    private final OpCode.Factory opCodeFactory = new OpCode.Factory();

    private final List<OpCode> opCodes = new LinkedList<>();
    private final List<Character> positions = new LinkedList<>();
    private final Map<Integer, BranchPrototype> branchPrototypes = new HashMap<>();
    private final Map<String, Character> labelToPos = new HashMap<>();

    private char byteCounter = 0;

    public OpCodeAssembler(JbcEncoding encoding) {
        this.encoding = encoding;
    }

    private void registerLabel(final String label) {
        this.labelToPos.put(label, this.byteCounter);
    }

    public List<OpCode> assemble() {
        final List<OpCode> returnValue = new LinkedList<>();
        for (int i = 0; i < this.opCodes.size(); ++i) {
            final OpCode opCode = this.opCodes.get(i);
            if (opCode != null) {
                returnValue.add(opCode);
            } else {
                final BranchPrototype prot = this.branchPrototypes.get(i);
                final char currentAddress = this.positions.get(i);
                final char targetAddr = this.labelToPos.get(prot.getTarget());
                returnValue.add(prot.toOpcode((char) (targetAddr - currentAddress)));
            }
        }
        return returnValue;
    }

    public void addGoto(final String label, final String target) {
        this.registerLabel(label);
        this.addGoto(target);
    }

    public void addGoto(final String target) {
        final BranchPrototype prot = new BranchPrototype(BranchPrototype.Condition.NONE, target);
        this.branchPrototypes.put(this.opCodes.size(), prot);
        this.opCodes.add(null);
        this.positions.add(this.byteCounter);
        this.byteCounter += prot.getLength(this.encoding);
    }

    public void addBranchIfEqZero(final String label, final String target) {
        this.registerLabel(label);
        this.addBranchIfEqZero(target);
    }

    public void addBranchIfEqZero(final String target) {
        final BranchPrototype prot = new BranchPrototype(BranchPrototype.Condition.EQZERO, target);
        this.branchPrototypes.put(this.opCodes.size(), prot);
        this.opCodes.add(null);
        this.positions.add(this.byteCounter);
        this.byteCounter += prot.getLength(this.encoding);
    }

    public void addALoad(String label, char varIndex) {
        this.registerLabel(label);
        this.addALoad(varIndex);
    }

    public void addALoad(char varIndex) {
        final OpCode opCode = opCodeFactory.buildALoad(varIndex);
        this.opCodes.add(opCode);
        this.positions.add(this.byteCounter);
        this.byteCounter += opCode.getLength(this.encoding);
    }

    public void addInvokeVirtual(char methodRefId) {
        final OpCode opCode = opCodeFactory.buildInvokeVirtual(methodRefId);
        this.opCodes.add(opCode);
        this.positions.add(this.byteCounter);
        this.byteCounter += opCode.getLength(this.encoding);
    }

    public void addIfICmpLt(String target) {
        final BranchPrototype prot = new BranchPrototype(BranchPrototype.Condition.LT, target);
        this.branchPrototypes.put(this.opCodes.size(), prot);
        this.opCodes.add(null);
        this.positions.add(this.byteCounter);
        this.byteCounter += prot.getLength(this.encoding);
    }

    public void addIConst0(String label) {
        this.registerLabel(label);
        this.addIConst0();
    }

    public void addIConst0() {
        final OpCode opCode = opCodeFactory.buildIConst0();
        this.opCodes.add(opCode);
        this.positions.add(this.byteCounter);
        this.byteCounter += opCode.getLength(this.encoding);
    }

    public void addIConst1(String label) {
        this.registerLabel(label);
        this.addIConst1();
    }

    public void addIConst1() {
        final OpCode opCode = opCodeFactory.buildIConst1();
        this.opCodes.add(opCode);
        this.positions.add(this.byteCounter);
        this.byteCounter += opCode.getLength(this.encoding);
    }

    public void addNop(String label) {
        this.registerLabel(label);
        this.addNop();
    }

    public void addNop() {
        final OpCode opCode = opCodeFactory.buildNop();
        this.opCodes.add(opCode);
        this.positions.add(this.byteCounter);
        this.byteCounter += opCode.getLength(this.encoding);
    }

    public void addNew(String label, char classRefIndex) {
        this.registerLabel(label);
        this.addNew(classRefIndex);
    }

    public void addNew(char classRefIndex) {
        final OpCode opCode = opCodeFactory.buildNew(classRefIndex);
        this.opCodes.add(opCode);
        this.positions.add(this.byteCounter);
        this.byteCounter += opCode.getLength(this.encoding);
    }

    public void addIfICmpLe(String target) {
        final BranchPrototype prot = new BranchPrototype(BranchPrototype.Condition.LE, target);
        this.branchPrototypes.put(this.opCodes.size(), prot);
        this.opCodes.add(null);
        this.positions.add(this.byteCounter);
        this.byteCounter += prot.getLength(this.encoding);
    }

    public void addIfEq(String target) {
        final BranchPrototype prot = new BranchPrototype(BranchPrototype.Condition.EQZERO, target);
        this.branchPrototypes.put(this.opCodes.size(), prot);
        this.opCodes.add(null);
        this.positions.add(this.byteCounter);
        this.byteCounter += prot.getLength(this.encoding);
    }

    public void addIfNe(String target) {
        final BranchPrototype prot = new BranchPrototype(BranchPrototype.Condition.NEZERO, target);
        this.branchPrototypes.put(this.opCodes.size(), prot);
        this.opCodes.add(null);
        this.positions.add(this.byteCounter);
        this.byteCounter += prot.getLength(this.encoding);
    }

    public void addAReturn() {
        final OpCode opCode = opCodeFactory.buildAReturn();
        this.opCodes.add(opCode);
        this.positions.add(this.byteCounter);
        this.byteCounter += opCode.getLength(this.encoding);
    }

    public void addAStore(char varId) {
        final OpCode opCode = opCodeFactory.buildAStore(varId);
        this.opCodes.add(opCode);
        this.positions.add(this.byteCounter);
        this.byteCounter += opCode.getLength(this.encoding);
    }

    public void addAConstNull(String label) {
        this.registerLabel(label);
        this.addAConstNull();
    }

    public void addAConstNull() {
        final OpCode opCode = opCodeFactory.buildAConstNull();
        this.opCodes.add(opCode);
        this.positions.add(this.byteCounter);
        this.byteCounter += opCode.getLength(this.encoding);
    }

    public void addIAdd() {
        final OpCode opCode = opCodeFactory.buildIAdd();
        this.opCodes.add(opCode);
        this.positions.add(this.byteCounter);
        this.byteCounter += opCode.getLength(this.encoding);
    }

    public void addIDiv() {
        final OpCode opCode = opCodeFactory.buildIDiv();
        this.opCodes.add(opCode);
        this.positions.add(this.byteCounter);
        this.byteCounter += opCode.getLength(this.encoding);
    }

    public void addISub() {
        final OpCode opCode = opCodeFactory.buildISub();
        this.opCodes.add(opCode);
        this.positions.add(this.byteCounter);
        this.byteCounter += opCode.getLength(this.encoding);
    }

    public void addIMul() {
        final OpCode opCode = opCodeFactory.buildIMul();
        this.opCodes.add(opCode);
        this.positions.add(this.byteCounter);
        this.byteCounter += opCode.getLength(this.encoding);
    }

    public void addInstanceof(char classRefId) {
        final OpCode opCode = opCodeFactory.buildInstanceOf(classRefId);
        this.opCodes.add(opCode);
        this.positions.add(this.byteCounter);
        this.byteCounter += opCode.getLength(this.encoding);
    }

    public void addIfNull(String target) {
        final BranchPrototype prot = new BranchPrototype(BranchPrototype.Condition.REFEQNULL, target);
        this.branchPrototypes.put(this.opCodes.size(), prot);
        this.opCodes.add(null);
        this.positions.add(this.byteCounter);
        this.byteCounter += prot.getLength(this.encoding);
    }

    public void addPushShort(String label, int value) {
        this.registerLabel(label);
        this.addPushShort(value);
    }

    public void addPushShort(int value) {
        final OpCode opCode = opCodeFactory.buildPushShort(value);
        this.opCodes.add(opCode);
        this.positions.add(this.byteCounter);
        this.byteCounter += opCode.getLength(this.encoding);
    }

    public void pushLdc(String label, char stringRef) {
        this.registerLabel(label);
        this.pushLdc(stringRef);
    }

    public void pushLdc(char stringRef) {
        final OpCode opCode = opCodeFactory.buildLdc(stringRef);
        this.opCodes.add(opCode);
        this.positions.add(this.byteCounter);
        this.byteCounter += opCode.getLength(this.encoding);
    }

    public void addReturn(String label) {
        this.registerLabel(label);
        this.addReturn();
    }

    public void addReturn() {
        final OpCode opCode = opCodeFactory.buildReturn();
        this.opCodes.add(opCode);
        this.positions.add(this.byteCounter);
        this.byteCounter += opCode.getLength(this.encoding);
    }

    public void addDup() {
        final OpCode opCode = opCodeFactory.buildDup();
        this.opCodes.add(opCode);
        this.positions.add(this.byteCounter);
        this.byteCounter += opCode.getLength(this.encoding);
    }

    public void addInvokeSpecial(char methodRefId) {
        final OpCode opCode = opCodeFactory.buildInvokeSpecial(methodRefId);
        this.opCodes.add(opCode);
        this.positions.add(this.byteCounter);
        this.byteCounter += opCode.getLength(this.encoding);
    }

    public void addPop() {
        final OpCode opCode = opCodeFactory.buildPop();
        this.opCodes.add(opCode);
        this.positions.add(this.byteCounter);
        this.byteCounter += opCode.getLength(this.encoding);
    }

    public void addPutField(String label, char fieldRefId) {
        this.registerLabel(label);
        this.addPutField(fieldRefId);
    }

    public void addPutField(char fieldRefId) {
        final OpCode opCode = opCodeFactory.buildPutField(fieldRefId);
        this.opCodes.add(opCode);
        this.positions.add(this.byteCounter);
        this.byteCounter += opCode.getLength(this.encoding);
    }

    public void addGetField(String label, char fieldRefId) {
        this.registerLabel(label);
        this.addGetField(fieldRefId);
    }

    public void addGetField(char fieldRefId) {
        final OpCode opCode = opCodeFactory.buildGetField(fieldRefId);
        this.opCodes.add(opCode);
        this.positions.add(this.byteCounter);
        this.byteCounter += opCode.getLength(this.encoding);
    }

    public void addAStore(String label, Character character) {
        this.registerLabel(label);
        this.addAStore(character);
    }
}
