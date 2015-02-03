package net.alexweinert.coolc.semantic_check;

import net.alexweinert.coolc.Output;
import net.alexweinert.coolc.program.ast.Class;
import net.alexweinert.coolc.program.ast.Classes;
import net.alexweinert.coolc.program.ast.Program;
import net.alexweinert.coolc.program.ast.visitors.ASTVisitor;
import net.alexweinert.coolc.program.symboltables.AbstractTable;

class BuiltinInheritanceChecker extends ASTVisitor {

    private final Output out;
    private Program program;
    private Classes classes;

    private BuiltinInheritanceChecker(Output out) {
        this.out = out;
    }

    /**
     * Finds all classes that inherit from the builtin classes and sets their parent to Object instead
     */
    public static Program checkBuiltinInheritance(Program program, Output out) {
        final BuiltinInheritanceChecker checker = new BuiltinInheritanceChecker(out);
        program.acceptVisitor(checker);
        return checker.program;
    }

    @Override
    public void visitClassPostorder(Class classNode) {
        final boolean inheritsInt = classNode.getParent().equals(AbstractTable.stringtable.addString("Int"));
        final boolean inheritsBool = classNode.getParent().equals(AbstractTable.stringtable.addString("Bool"));
        final boolean inheritsString = classNode.getParent().equals(AbstractTable.stringtable.addString("String"));
        final boolean inheritsIO = classNode.getParent().equals(AbstractTable.stringtable.addString("IO"));
        final String formatString = "Class %s inherits from base class %s at %s:%d";
        if (inheritsInt) {
            out.error(String.format(formatString, classNode.getIdentifier(), "Int", classNode.getFilename(),
                    classNode.getLineNumber()));
        } else if (inheritsBool) {
            out.error(String.format(formatString, classNode.getIdentifier(), "Bool", classNode.getFilename(),
                    classNode.getLineNumber()));
        } else if (inheritsString) {
            out.error(String.format(formatString, classNode.getIdentifier(), "String", classNode.getFilename(),
                    classNode.getLineNumber()));
        } else if (inheritsIO) {
            out.error(String.format(formatString, classNode.getIdentifier(), "IO", classNode.getFilename(),
                    classNode.getLineNumber()));
        }

        final boolean forbiddenInheritance = inheritsInt || inheritsBool || inheritsString || inheritsIO;
        if (forbiddenInheritance) {
            this.classes.remove(classNode).add(classNode.setParent(AbstractTable.stringtable.addString("Object")));
        }
    }

    @Override
    public void visitClassesPreorder(Classes classes) {
        this.classes = classes;
    }

    @Override
    public void visitProgramPostorder(Program program) {
        this.program = program.setClasses(this.classes);
    }

}
