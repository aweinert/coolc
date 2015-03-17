package net.alexweinert.coolc.processors;

import java.nio.file.Paths;

import net.alexweinert.coolc.Output;
import net.alexweinert.coolc.infrastructure.Compiler;
import net.alexweinert.coolc.infrastructure.Frontend;
import net.alexweinert.coolc.processors.cool.frontend.CoolParser;
import net.alexweinert.coolc.processors.cool.hierarchycheck.CoolHierarchyChecker;
import net.alexweinert.coolc.processors.cool.selftyperemoval.SelfTypeRemover;
import net.alexweinert.coolc.processors.cool.typecheck.CoolTypeChecker;
import net.alexweinert.coolc.processors.cool.unparser.CoolUnparser;
import net.alexweinert.coolc.processors.io.fileopener.FileOpener;
import net.alexweinert.coolc.processors.io.stringdumper.StringDumper;
import net.alexweinert.coolc.processors.java.fromcool.JavaBackend;
import net.alexweinert.coolc.processors.java.variablerenaming.VariableRenamer;
import net.alexweinert.coolc.representations.cool.ast.Program;

public class ProcessorBuilder {
    private Frontend frontend = null;
    private final Output output = new Output();

    public ProcessorBuilder openFile(String path) {
        this.frontend = new FileOpener(path);
        return this;
    }

    public ProcessorBuilder parseCool() {
        this.frontend = this.frontend.append(new CoolParser());
        return this;
    }

    public ProcessorBuilder hierarchyCheck() {
        this.frontend = this.frontend.append(new CoolHierarchyChecker());
        return this;
    }

    public ProcessorBuilder typeCheck() {
        this.frontend = this.frontend.append(new CoolTypeChecker(this.output));
        return this;
    }

    public ProcessorBuilder removeSelfType() {
        this.frontend = this.frontend.append(new SelfTypeRemover());
        return this;
    }

    public ProcessorBuilder removeShadowing() {
        this.frontend = this.frontend.append(new VariableRenamer());
        return this;
    }

    public Compiler<Program> dumpJava(String folder) {
        return this.frontend.append(new JavaBackend(Paths.get(folder)));
    }

    public ProcessorBuilder parseAndCheckCool() {
        return this.parseCool().hierarchyCheck().typeCheck();
    }

    public Compiler<Program> unparseToJava(String folder) {
        return this.removeSelfType().removeShadowing().hierarchyCheck().typeCheck().dumpJava(folder);
    }

    public Compiler<String> dumpToConsole() {
        this.frontend = this.frontend.append(new CoolUnparser());
        return this.frontend.append(new StringDumper());
    }
}
