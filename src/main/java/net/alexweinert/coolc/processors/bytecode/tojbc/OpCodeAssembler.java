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
            NONE, EQZERO, NEQZERO
        }

        private final Condition condition;
        private final String target;

        private BranchPrototype(Condition condition, String target) {
            this.condition = condition;
            this.target = target;
        }

        public Condition getCondition() {
            return condition;
        }

        public String getTarget() {
            return target;
        }

        public int getLength(JbcEncoding encoding) {
            return 3;
        }

        public OpCode toOpcode(char target) {
            return null;
        }
    }

    private final JbcEncoding encoding;

    private final List<OpCode> opCodes = new LinkedList<>();
    private final Map<Integer, BranchPrototype> branchPrototypes = new HashMap<>();
    private final Map<String, Character> labelToPos = new HashMap<>();

    private char byteCounter = 0;

    public OpCodeAssembler(JbcEncoding encoding) {
        this.encoding = encoding;
    }

    public void addGoto(final String label, final String target) {
        this.labelToPos.put(label, this.byteCounter);
        this.addGoto(target);
    }

    public void addGoto(final String target) {
        final BranchPrototype prot = new BranchPrototype(BranchPrototype.Condition.NONE, target);
        this.branchPrototypes.put(this.opCodes.size(), prot);
        this.opCodes.add(null);
        this.byteCounter += prot.getLength(this.encoding);
    }

    public void addBranchIfEqZero(final String label, final String target) {
        this.labelToPos.put(label, this.byteCounter);
        this.addBranchIfEqZero(target);
    }

    public void addBranchIfEqZero(final String target) {
        final BranchPrototype prot = new BranchPrototype(BranchPrototype.Condition.EQZERO, target);
        this.branchPrototypes.put(this.opCodes.size(), prot);
        this.opCodes.add(null);
        this.byteCounter += prot.getLength(this.encoding);
    }

    public void addBranchIfNeqZero(final String label, final String target) {
        this.labelToPos.put(label, this.byteCounter);
        this.addBranchIfNeqZero(target);
    }

    public void addBranchIfNeqZero(final String target) {
        final BranchPrototype prot = new BranchPrototype(BranchPrototype.Condition.NEQZERO, target);
        this.branchPrototypes.put(this.opCodes.size(), prot);
        this.opCodes.add(null);
        this.byteCounter += prot.getLength(this.encoding);
    }

    public List<OpCode> assemble() {
        final List<OpCode> returnValue = new LinkedList<>();
        for (int i = 0; i < this.opCodes.size(); ++i) {
            final OpCode opCode = this.opCodes.get(i);
            if (opCode != null) {
                returnValue.add(opCode);
            } else {
                final BranchPrototype prot = this.branchPrototypes.get(i);
                final char targetAddr = this.labelToPos.get(prot.getTarget());
                returnValue.add(prot.toOpcode(targetAddr));
            }
        }
        return returnValue;
    }

    public void addALoad(String label, char integer) {
        // TODO Auto-generated method stub

    }

    public void addALoad(char character) {
        // TODO Auto-generated method stub

    }

    public void addInvokeDynamic(char getValueMethodIdIndex) {
        // TODO Auto-generated method stub

    }

    public void addIfICmpLt(String labelTrue) {
        // TODO Auto-generated method stub

    }

    public void addIConst0(String label) {
        // TODO Auto-generated method stub

    }

    public void addIConst0() {
        // TODO Auto-generated method stub

    }

    public void addIConst1(String label) {
        // TODO Auto-generated method stub

    }

    public void addIConst1() {
        // TODO Auto-generated method stub

    }

    public void addNop(String labelAfter) {
        // TODO Auto-generated method stub

    }

    public void addNew(String label, char coolBoolClassRefIndex) {
        // TODO Auto-generated method stub

    }

    public void addNew(char coolBoolIndex) {
        // TODO Auto-generated method stub

    }

    public void addIfICmpLe(String labelTrue) {
        // TODO Auto-generated method stub

    }

    public void addIfEq(String labelTrue) {
        // TODO Auto-generated method stub

    }

    public void addIfNe(String target) {
        // TODO Auto-generated method stub

    }

    public void addAReturn() {
        // TODO Auto-generated method stub

    }

    public void addAStore(Character character) {
        // TODO Auto-generated method stub

    }

    public void addAConstNull(String label) {
        // TODO Auto-generated method stub

    }

    public void addAConstNull() {
        // TODO Auto-generated method stub

    }

    public void addIAdd() {
        // TODO Auto-generated method stub

    }

    public void addIDiv() {
        // TODO Auto-generated method stub

    }

    public void addISub() {
        // TODO Auto-generated method stub

    }

    public void addIMul() {
        // TODO Auto-generated method stub

    }

}
