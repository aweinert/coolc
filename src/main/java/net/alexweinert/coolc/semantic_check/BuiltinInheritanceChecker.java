package net.alexweinert.coolc.semantic_check;

import net.alexweinert.coolc.program.ast.Class;
import net.alexweinert.coolc.program.ast.Classes;
import net.alexweinert.coolc.program.ast.Program;
import net.alexweinert.coolc.program.ast.visitors.ASTVisitor;
import net.alexweinert.coolc.program.symboltables.IdTable;

class BuiltinInheritanceChecker extends ASTVisitor {

    private final SemanticErrorReporter out;
    private Program program;
    private Classes classes;

    private BuiltinInheritanceChecker(SemanticErrorReporter out) {
        this.out = out;
    }

    /**
     * Finds all classes that inherit from the builtin classes and sets their parent to Object instead
     */
    public static Program checkBuiltinInheritance(Program program, SemanticErrorReporter out) {
        final BuiltinInheritanceChecker checker = new BuiltinInheritanceChecker(out);
        program.acceptVisitor(checker);
        return checker.program;
    }

    @Override
    public void visitClassPostorder(Class classNode) {
        final boolean inheritsInt = classNode.getParent().equals(IdTable.getInstance().addString("Int"));
        final boolean inheritsBool = classNode.getParent().equals(IdTable.getInstance().addString("Bool"));
        final boolean inheritsString = classNode.getParent().equals(IdTable.getInstance().addString("String"));
        final boolean inheritsIO = classNode.getParent().equals(IdTable.getInstance().addString("IO"));
        final boolean forbiddenInheritance = inheritsInt || inheritsBool || inheritsString || inheritsIO;
        if (forbiddenInheritance) {
            out.reportBaseClassInheritance(classNode);
            this.classes = this.classes.remove(classNode).add(
                    classNode.setParent(IdTable.getInstance().addString("Object")));
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
