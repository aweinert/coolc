package net.alexweinert.coolc.processors;

import net.alexweinert.coolc.Output;
import net.alexweinert.coolc.infrastructure.Frontend;
import net.alexweinert.coolc.infrastructure.Compiler;
import net.alexweinert.coolc.processors.cool.frontend.CoolParser;
import net.alexweinert.coolc.processors.cool.hierarchycheck.CoolHierarchyChecker;
import net.alexweinert.coolc.processors.cool.typecheck.CoolTypeChecker;
import net.alexweinert.coolc.processors.cool.unparser.CoolUnparser;
import net.alexweinert.coolc.processors.io.fileopener.FileOpener;
import net.alexweinert.coolc.processors.io.stringdumper.StringDumper;

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

    public ProcessorBuilder parseAndCheckCool() {
        this.parseCool().hierarchyCheck().typeCheck();
        return this;
    }

    public Compiler<String> dumpToConsole() {
        this.frontend = this.frontend.append(new CoolUnparser());
        return this.frontend.append(new StringDumper());
    }
}
