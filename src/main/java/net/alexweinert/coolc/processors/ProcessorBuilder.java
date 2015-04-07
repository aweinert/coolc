package net.alexweinert.coolc.processors;

import java.io.Reader;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;

import net.alexweinert.coolc.Output;
import net.alexweinert.coolc.infrastructure.Compiler;
import net.alexweinert.coolc.infrastructure.Frontend;
import net.alexweinert.coolc.processors.bytecode.fromcool.FromCoolBuilderFactory;
import net.alexweinert.coolc.processors.bytecode.tojbc.BytecodeToJbcProcessor;
import net.alexweinert.coolc.processors.cool.frontend.CoolParser;
import net.alexweinert.coolc.processors.cool.hierarchycheck.CoolHierarchyChecker;
import net.alexweinert.coolc.processors.cool.selftyperemoval.SelfTypeRemover;
import net.alexweinert.coolc.processors.cool.tohighlevel.CoolBackendProcessor;
import net.alexweinert.coolc.processors.cool.typecheck.CoolTypeChecker;
import net.alexweinert.coolc.processors.io.FileDumper;
import net.alexweinert.coolc.processors.io.FileOpener;
import net.alexweinert.coolc.processors.io.StringDumper;
import net.alexweinert.coolc.processors.jar.Jar;
import net.alexweinert.coolc.processors.java.fromcool.JavaClassBuilderFactory;
import net.alexweinert.coolc.processors.java.typecasesort.TypecaseSortProcessor;
import net.alexweinert.coolc.processors.jbc.JbcToFileProcessor;
import net.alexweinert.coolc.processors.util.UsageFrontend;
import net.alexweinert.coolc.representations.bytecode.ByteClass;
import net.alexweinert.coolc.representations.cool.ast.Program;
import net.alexweinert.coolc.representations.io.File;
import net.alexweinert.coolc.representations.java.JavaProgram;
import net.alexweinert.coolc.representations.jbc.JbcClass;

import com.beust.jcommander.JCommander;

public abstract class ProcessorBuilder<T> {
    protected final Frontend<T> frontend;
    protected final Output output = new Output();

    private ProcessorBuilder(Frontend<T> frontend) {
        this.frontend = frontend;
    }

    public static class FrontendBuilder {
        public StringCompilerBuilder helpToString(JCommander parser) {
            return new StringCompilerBuilder(new UsageFrontend(parser));
        }

        public ReaderCompilerBuilder openFile(String path) {
            return new ReaderCompilerBuilder(new FileOpener(path));
        }
    }

    public static class StringCompilerBuilder extends ProcessorBuilder<String> {
        private StringCompilerBuilder(Frontend<String> frontend) {
            super(frontend);
        }

        public Compiler<String> stringToConsole() {
            return this.frontend.append(new StringDumper());
        }
    }

    public static class ReaderCompilerBuilder extends ProcessorBuilder<Reader> {

        private ReaderCompilerBuilder(final Frontend<Reader> frontend) {
            super(frontend);
        }

        public CoolProgramCompilerBuilder fileToCool() {
            return new CoolProgramCompilerBuilder(this.frontend.append(new CoolParser()));
        }

    }

    public static class CoolProgramCompilerBuilder extends ProcessorBuilder<Program> {
        private CoolProgramCompilerBuilder(Frontend<Program> frontend) {
            super(frontend);
        }

        public CoolProgramCompilerBuilder checkCool() {
            return new CoolProgramCompilerBuilder(this.frontend.append(new CoolHierarchyChecker()).append(
                    new CoolTypeChecker(output)));
        }

        public BytecodeCompilerBuilder coolToBytecode() {
            Frontend<Program> newFrontend = this.frontend;
            newFrontend = newFrontend.append(new CoolHierarchyChecker());
            newFrontend = newFrontend.append(new TypecaseSortProcessor());

            newFrontend = newFrontend.append(new CoolHierarchyChecker());
            newFrontend = newFrontend.append(new SelfTypeRemover());

            newFrontend = newFrontend.append(new CoolHierarchyChecker());
            newFrontend = newFrontend.append(new CoolTypeChecker(output));
            return new BytecodeCompilerBuilder(newFrontend.append(new CoolBackendProcessor<>(
                    new FromCoolBuilderFactory())));
        }

        public JavaCompilerBuilder coolToJava() {
            Frontend<Program> newFrontend = this.frontend;
            newFrontend = newFrontend.append(new CoolHierarchyChecker());
            newFrontend = newFrontend.append(new TypecaseSortProcessor());

            newFrontend = newFrontend.append(new CoolHierarchyChecker());
            newFrontend = newFrontend.append(new SelfTypeRemover());

            newFrontend = newFrontend.append(new CoolHierarchyChecker());
            newFrontend = newFrontend.append(new CoolTypeChecker(output));
            return new JavaCompilerBuilder(
                    newFrontend.append(new CoolBackendProcessor<>(new JavaClassBuilderFactory())));
        }
    }

    public static class BytecodeCompilerBuilder extends ProcessorBuilder<List<ByteClass>> {
        private BytecodeCompilerBuilder(final Frontend<List<ByteClass>> frontend) {
            super(frontend);
        }

        public JbcCompilerBuilder bytecodeToJbc() {
            return new JbcCompilerBuilder(this.frontend.append(new BytecodeToJbcProcessor()));
        }
    }

    public static class JavaCompilerBuilder extends ProcessorBuilder<JavaProgram> {
        private JavaCompilerBuilder(final Frontend<JavaProgram> frontend) {
            super(frontend);
        }

        public FilesCompilerBuilder javaToFiles() {
            // TODO return newFilesCompilerBuilder(this.frontend.append(new JavaToFilesProcessor()));
            return null;
        }

        public JarCompilerBuilder javaToJar(String relativeJarPath) {
            // TODO new JarCompilerBuilder(this.frontend.append(new JavaToJarProcessor(Paths.get(relativeJarPath))));
            return null;
        }
    }

    public static class JarCompilerBuilder extends ProcessorBuilder<Jar> {
        private JarCompilerBuilder(final Frontend<Jar> frontend) {
            super(frontend);
        }

        public FileCompilerBuilder jarToFile() {
            // TODO Auto-generated method stub
            return null;
        }
    }

    public static class JbcCompilerBuilder extends ProcessorBuilder<Collection<JbcClass>> {
        private JbcCompilerBuilder(final Frontend<Collection<JbcClass>> frontend) {
            super(frontend);
        }

        public FilesCompilerBuilder jbcToFiles() {
            return new FilesCompilerBuilder(this.frontend.append(new JbcToFileProcessor()));
        }
    }

    public static class FileCompilerBuilder extends ProcessorBuilder<File> {
        private FileCompilerBuilder(final Frontend<File> frontend) {
            super(frontend);
        }

        public Compiler<File> fileToHarddrive(String outputFolder) {
            // TODO return this.frontend.append(new FileDumper(Paths.get(outputFolder)));
            return null;
        }
    }

    public static class FilesCompilerBuilder extends ProcessorBuilder<Collection<File>> {
        private FilesCompilerBuilder(final Frontend<Collection<File>> frontend) {
            super(frontend);
        }

        public Compiler<Collection<File>> filesToHarddrive(String outputFolder) {
            return this.frontend.append(new FileDumper(Paths.get(outputFolder)));
        }
    }
}
