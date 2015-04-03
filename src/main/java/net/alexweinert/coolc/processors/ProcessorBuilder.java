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
import net.alexweinert.coolc.processors.io.FileDumper;
import net.alexweinert.coolc.processors.io.FileOpener;
import net.alexweinert.coolc.processors.io.StringDumper;
import net.alexweinert.coolc.processors.java.dump.JavaDumper;
import net.alexweinert.coolc.processors.java.fromcool.JavaClassBuilderFactory;
import net.alexweinert.coolc.processors.java.jarcompile.JarCompiler;
import net.alexweinert.coolc.processors.java.tofiles.JavaToFilesProcessor;
import net.alexweinert.coolc.processors.java.typecasesort.TypecaseSortProcessor;
import net.alexweinert.coolc.processors.java.variablerenaming.VariableRenamer;
import net.alexweinert.coolc.processors.jbc.JbcToFileProcessor;
import net.alexweinert.coolc.processors.util.UsageFrontend;
import net.alexweinert.coolc.representations.java.JavaProgram;

import com.beust.jcommander.JCommander;

public class ProcessorBuilder {
    private Frontend frontend = null;
    private final Output output = new Output();

    public ProcessorBuilder helpToString(JCommander parser) {
        this.frontend = new UsageFrontend(parser);
        return this;
    }

    public ProcessorBuilder openFile(String path) {
        this.frontend = new FileOpener(path);
        return this;
    }

    public ProcessorBuilder fileToCool() {
        this.frontend = this.frontend.append(new CoolParser());
        return this;
    }

    public ProcessorBuilder checkCool() {
        this.frontend = this.frontend.append(new CoolHierarchyChecker());
        this.frontend = this.frontend.append(new CoolTypeChecker(output));
        return this;
    }

    public ProcessorBuilder coolToBytecode() {
        this.frontend = this.frontend.append(new CoolHierarchyChecker());
        this.frontend = this.frontend.append(new TypecaseSortProcessor());

        this.frontend = this.frontend.append(new CoolHierarchyChecker());
        this.frontend = this.frontend.append(new SelfTypeRemover());

        this.frontend = this.frontend.append(new CoolHierarchyChecker());
        this.frontend = this.frontend.append(new CoolTypeChecker(output));
        this.frontend = this.frontend.append(new CoolBackendProcessor<>(new FromCoolBuilderFactory()));

        return this;
    }

    public ProcessorBuilder bytecodeToJbc() {
        this.frontend = this.frontend.append(new BytecodeToJbcProcessor());
        return this;
    }

    public ProcessorBuilder jbcToFiles() {
        this.frontend = this.frontend.append(new JbcToFileProcessor());
        return this;
    }

    public ProcessorBuilder coolToJava() {
        this.frontend = this.frontend.append(new CoolHierarchyChecker());
        this.frontend = this.frontend.append(new TypecaseSortProcessor());

        this.frontend = this.frontend.append(new CoolHierarchyChecker());
        this.frontend = this.frontend.append(new SelfTypeRemover());

        this.frontend = this.frontend.append(new CoolHierarchyChecker());
        this.frontend = this.frontend.append(new CoolTypeChecker(output));
        this.frontend = this.frontend.append(new CoolBackendProcessor<>(new JavaClassBuilderFactory()));

        return this;
    }

    public ProcessorBuilder javaToFiles() {
        this.frontend = this.frontend.append(new JavaToFilesProcessor());
        return this;
    }

    public ProcessorBuilder javaToJar() {
        // TODO Auto-generated method stub
        return null;
    }

    public ProcessorBuilder jarToFile() {
        // TODO Auto-generated method stub
        return null;
    }

    public Compiler<?> stringToConsole() {
        return this.frontend.append(new StringDumper());
    }

    public Compiler<?> filesToHarddrive(String outputFolder) {
        return this.frontend.append(new FileDumper(Paths.get(outputFolder)));
    }
}
