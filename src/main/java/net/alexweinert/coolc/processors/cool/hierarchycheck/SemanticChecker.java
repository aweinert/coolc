package net.alexweinert.coolc.processors.cool.hierarchycheck;

import java.util.HashMap;
import java.util.Map;

import net.alexweinert.pipelines.ProcessorException;
import net.alexweinert.coolc.representations.cool.ast.ClassNode;
import net.alexweinert.coolc.representations.cool.ast.Program;
import net.alexweinert.coolc.representations.cool.information.ClassHierarchy;
import net.alexweinert.coolc.representations.cool.information.DeclaredClassSignature;
import net.alexweinert.coolc.representations.cool.information.DefinedClassSignature;
import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;
import net.alexweinert.coolc.representations.cool.symboltables.IdTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@Component
@ComponentScan("net.alexweinert.coolc.processors.cool.hierarchycheck")
class SemanticChecker {
    private final MultipleClassesRemover classRemover;
    private final BuiltinRedefinitionRemover redefinitionRemover;
    private final BuiltinInheritanceChecker inheritanceChecker;
    private final ParentDefinednessChecker definednessChecker;
    private final CircularInheritanceRemover inheritanceRemover;
    private final InterfaceChecker interfaceChecker;
    private final OverridingChecker overridingChecker;

    @Autowired
    SemanticChecker(
            final MultipleClassesRemover classRemover,
            final BuiltinRedefinitionRemover redefinitionRemover,
            final BuiltinInheritanceChecker inheritanceChecker,
            final ParentDefinednessChecker definednessChecker,
            final CircularInheritanceRemover inheritanceRemover,
            final InterfaceChecker interfaceChecker,
            final OverridingChecker overridingChecker) {
        this.classRemover = classRemover;
        this.redefinitionRemover = redefinitionRemover;
        this.inheritanceChecker = inheritanceChecker;
        this.definednessChecker = definednessChecker;
        this.inheritanceRemover = inheritanceRemover;
        this.interfaceChecker = interfaceChecker;
        this.overridingChecker = overridingChecker;
    }

    public Program checkSemantics(Program program) throws ProcessorException {
        final SemanticErrorReporter error = new SemanticErrorReporter();

        program = classRemover.removeMultipleClassDefinitions(program);
        program = redefinitionRemover.removeBuiltinRedefinition(program, error);
        program = inheritanceChecker.checkBuiltinInheritance(program, error);
        program = definednessChecker.checkParentDefinedness(program, error);
        program = inheritanceRemover.removeCircularInheritance(program, error);
        program = interfaceChecker.checkInterfaces(program, error);
        program = overridingChecker.checkOverriding(program, error);

        if (error.hasErrors()) {
            for (String errorMessage : error.getErrors()) {
                System.out.println(errorMessage);
            }
            throw new ProcessorException(null);
        }

        program.setHierarchy(ClassHierarchy.create(program));

        return program;
    }

    private static Map<IdSymbol, DeclaredClassSignature> createDeclaredSignatures(Program program) {
        final Map<IdSymbol, DeclaredClassSignature> declaredSignatures = new HashMap<>();
        declaredSignatures.put(IdTable.getInstance().getObjectSymbol(), DeclaredClassSignature.createObjectSignature());
        declaredSignatures.put(IdTable.getInstance().getIntSymbol(), DeclaredClassSignature.createIntSignature());
        declaredSignatures.put(IdTable.getInstance().getBoolSymbol(), DeclaredClassSignature.createBoolSignature());
        declaredSignatures.put(IdTable.getInstance().getStringSymbol(), DeclaredClassSignature.createStringSignature());
        declaredSignatures.put(IdTable.getInstance().getIOSymbol(), DeclaredClassSignature.createIOSignature());

        for (ClassNode classNode : program.getClasses()) {
            declaredSignatures.put(classNode.getIdentifier(), DeclaredClassSignature.create(classNode));
        }
        return declaredSignatures;
    }

    private static Map<IdSymbol, DefinedClassSignature> createDefinedSignatures(Program program,
            ClassHierarchy hierarchy, Map<IdSymbol, DeclaredClassSignature> declaredSignatures) {

        final Map<IdSymbol, DefinedClassSignature> definedSignatures = new HashMap<>();
        final IdSymbol objectSymbol = IdTable.getInstance().getObjectSymbol();
        definedSignatures.put(objectSymbol, DefinedClassSignature.create(objectSymbol, hierarchy, declaredSignatures));

        final IdSymbol boolSymbol = IdTable.getInstance().getBoolSymbol();
        definedSignatures.put(boolSymbol, DefinedClassSignature.create(boolSymbol, hierarchy, declaredSignatures));

        final IdSymbol intSymbol = IdTable.getInstance().getIntSymbol();
        definedSignatures.put(intSymbol, DefinedClassSignature.create(intSymbol, hierarchy, declaredSignatures));

        final IdSymbol stringSymbol = IdTable.getInstance().getStringSymbol();
        definedSignatures.put(stringSymbol, DefinedClassSignature.create(stringSymbol, hierarchy, declaredSignatures));

        final IdSymbol ioSymbol = IdTable.getInstance().getIOSymbol();
        definedSignatures.put(ioSymbol, DefinedClassSignature.create(ioSymbol, hierarchy, declaredSignatures));

        for (ClassNode classNode : program.getClasses()) {
            definedSignatures.put(classNode.getIdentifier(),
                    DefinedClassSignature.create(classNode.getIdentifier(), hierarchy, declaredSignatures));
        }
        return definedSignatures;
    }
}
