package net.alexweinert.coolc.processors;

import java.nio.file.Paths;

import net.alexweinert.coolc.Output;
import net.alexweinert.coolc.infrastructure.Compiler;
import net.alexweinert.coolc.infrastructure.Frontend;
import net.alexweinert.coolc.processors.bytecode.fromcool.FromCoolBuilderFactory;
import net.alexweinert.coolc.processors.bytecode.tograph.BytecodeToGraphProcessor;
import net.alexweinert.coolc.processors.bytecode.tojbc.BytecodeToJbcProcessor;
import net.alexweinert.coolc.processors.bytecode.tostring.ToStringProcessor;
import net.alexweinert.coolc.processors.cool.frontend.CoolParser;
import net.alexweinert.coolc.processors.cool.hierarchycheck.CoolHierarchyChecker;
import net.alexweinert.coolc.processors.cool.selftyperemoval.SelfTypeRemover;
import net.alexweinert.coolc.processors.cool.tohighlevel.CoolBackendProcessor;
import net.alexweinert.coolc.processors.cool.typecheck.CoolTypeChecker;
import net.alexweinert.coolc.processors.cool.unparser.CoolUnparser;
import net.alexweinert.coolc.processors.graph.GraphToFileProcessor;
import net.alexweinert.coolc.processors.io.FileOpener;
import net.alexweinert.coolc.processors.io.StringDumper;
import net.alexweinert.coolc.processors.java.dump.JavaDumper;
import net.alexweinert.coolc.processors.java.fromcool.JavaClassBuilderFactory;
import net.alexweinert.coolc.processors.java.jarcompile.JarCompiler;
import net.alexweinert.coolc.processors.java.typecasesort.TypecaseSortProcessor;
import net.alexweinert.coolc.processors.java.variablerenaming.VariableRenamer;
import net.alexweinert.coolc.processors.jbc.JbcToFileProcessor;
import net.alexweinert.coolc.processors.util.UsageFrontend;
import net.alexweinert.coolc.representations.java.JavaProgram;

import com.beust.jcommander.JCommander;

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

    public ProcessorBuilder coolToJava() {
        this.frontend = this.frontend.append(new CoolBackendProcessor<>(new JavaClassBuilderFactory()));
        return this;
    }

    public ProcessorBuilder sortTypecase() {
        this.frontend = this.frontend.append(new TypecaseSortProcessor());
        return this;
    }

    public Compiler<JavaProgram> dumpJava(String folder) {
        return this.frontend.append(new JavaDumper(Paths.get(folder)));
    }

    public Compiler<JavaProgram> compileJar(String jarFile) {
        return this.frontend.append(new JarCompiler(jarFile));
    }

    public ProcessorBuilder parseAndCheckCool() {
        return this.parseCool().hierarchyCheck().typeCheck();
    }

    public ProcessorBuilder compileToJava() {
        return this.hierarchyCheck().sortTypecase().hierarchyCheck().removeSelfType().removeShadowing()
                .hierarchyCheck().typeCheck().coolToJava();
    }

    public ProcessorBuilder coolToBytecode() {
        this.hierarchyCheck().sortTypecase().hierarchyCheck().removeSelfType().removeShadowing().hierarchyCheck()
                .typeCheck();
        this.frontend = this.frontend.append(new CoolBackendProcessor<>(new FromCoolBuilderFactory()));
        return this;
    }

    public ProcessorBuilder bytecodeToString() {
        this.frontend = this.frontend.append(new ToStringProcessor());
        return this;
    }

    public Compiler<?> stringToConsole() {
        return this.frontend.append(new StringDumper());
    }

    public Compiler<String> dumpToConsole() {
        this.frontend = this.frontend.append(new CoolUnparser());
        return this.frontend.append(new StringDumper());
    }

    public Compiler<?> showHelp(JCommander jCommander) {
        return new UsageFrontend(jCommander).append(new StringDumper());
    }

    public ProcessorBuilder bytecodeToGraphs() {
        this.frontend = this.frontend.append(new BytecodeToGraphProcessor());
        return this;
    }

    public Compiler<?> graphsToFile(String outputFolder) {
        return this.frontend.append(new GraphToFileProcessor(Paths.get(outputFolder)));
    }

    public ProcessorBuilder bytecodeToJbc() {
        this.frontend = this.frontend.append(new BytecodeToJbcProcessor());
        return this;
    }

    public Compiler<?> jbcToFile(String outputFolder) {
        return this.frontend.append(new JbcToFileProcessor(Paths.get(outputFolder)));
    }
}
