package net.alexweinert.coolc.processors.cool.hierarchycheck;

import java.util.Collection;
import java.util.LinkedList;

import net.alexweinert.coolc.representations.cool.program.parsed.ClassNode;
import net.alexweinert.coolc.representations.cool.program.parsed.Classes;
import net.alexweinert.coolc.representations.cool.program.parsed.ParsedProgram;
import net.alexweinert.coolc.representations.cool.program.parsed.ParsedProgramVisitor;
import net.alexweinert.coolc.representations.cool.symboltables.IdTable;

class BuiltinInheritanceChecker extends ParsedProgramVisitor {

    private final SemanticErrorReporter out;
    private ParsedProgram program;
    private Classes classes;

    private BuiltinInheritanceChecker(SemanticErrorReporter out) {
        this.out = out;
    }

    /**
     * Finds all classes that inherit from the builtin classes and sets their parent to Object instead
     */
    public static ParsedProgram checkBuiltinInheritance(ParsedProgram program, SemanticErrorReporter out) {
        ParsedProgram returnProgram = program;
        final Collection<ClassNode> violatingClasses = new LinkedList<>();
        for (ClassNode classNode : program.getClasses()) {
            if (inheritsBaseClass(classNode)) {
                out.reportBaseClassInheritance(classNode);
                returnProgram = program;
                program = program.replaceClass(classNode, classNode.setParent(program.getIdTable().getObjectSymbol()));
            }
        }
        return returnProgram;
    }

    public static boolean inheritsBaseClass(ClassNode classNode) {
        final boolean inheritsInt = classNode.getParent().equals(IdTable.getInstance().getIntSymbol());
        final boolean inheritsBool = classNode.getParent().equals(IdTable.getInstance().getBoolSymbol());
        final boolean inheritsString = classNode.getParent().equals(IdTable.getInstance().getStringSymbol());
        final boolean inheritsIO = classNode.getParent().equals(IdTable.getInstance().getIOSymbol());
        final boolean forbiddenInheritance = inheritsInt || inheritsBool || inheritsString || inheritsIO;
        return forbiddenInheritance;
    }
}
