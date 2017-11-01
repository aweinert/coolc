package net.alexweinert.coolc.processors.bytecode.tograph;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import net.alexweinert.pipelines.Processor;
import net.alexweinert.pipelines.ProcessorException;
import net.alexweinert.coolc.representations.bytecode.BranchInstruction;
import net.alexweinert.coolc.representations.bytecode.ByteClass;
import net.alexweinert.coolc.representations.bytecode.LabeledInstruction;
import net.alexweinert.coolc.representations.bytecode.Method;
import net.alexweinert.coolc.representations.graph.Graph;

public class BytecodeToGraphProcessor extends Processor<Collection<ByteClass>, Collection<Graph<LabeledInstruction>>> {

    @Override
    public Collection<Graph<LabeledInstruction>> process(Collection<ByteClass> input) throws ProcessorException {
        final Collection<Graph<LabeledInstruction>> returnValue = new HashSet<>();
        for (ByteClass byteClass : input) {
            for (Method method : byteClass.getMethods()) {
                final String graphId = byteClass.getId() + "$" + method.getId();
                final Graph.Builder<LabeledInstruction> builder = new Graph.Builder<>(graphId);
                final Map<LabeledInstruction, String> jumps = new HashMap<>();

                LabeledInstruction lastInstruction = null;
                for (LabeledInstruction instruction : method.getInstruction()) {
                    if (lastInstruction != null) {
                        builder.addEdge(lastInstruction, instruction);
                    }
                    if (instruction instanceof BranchInstruction) {
                        jumps.put(instruction, ((BranchInstruction) instruction).getTarget());
                    }
                    lastInstruction = instruction;
                }
                for (Map.Entry<LabeledInstruction, String> jump : jumps.entrySet()) {
                    final LabeledInstruction source = jump.getKey();
                    final LabeledInstruction target = method.getLabeledInstruction(jump.getValue());
                    builder.addEdge(source, target);
                }

                returnValue.add(builder.build());
            }
        }

        return returnValue;
    }

}
