package net.alexweinert.coolc.processors.cool.hierarchycheck;

import net.alexweinert.coolc.representations.cool.ast.Visitor;
import net.alexweinert.coolc.representations.cool.ast.ClassNode;
import net.alexweinert.coolc.representations.cool.ast.Classes;
import net.alexweinert.coolc.representations.cool.ast.Program;
import net.alexweinert.coolc.representations.cool.symboltables.IdTable;
import org.springframework.stereotype.Component;

@Component
class BuiltinInheritanceChecker extends Visitor {

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
    public void visitClassPostorder(ClassNode classNode) {
        final boolean inheritsInt = classNode.getParent().equals(IdTable.getInstance().getIntSymbol());
        final boolean inheritsBool = classNode.getParent().equals(IdTable.getInstance().getBoolSymbol());
        final boolean inheritsString = classNode.getParent().equals(IdTable.getInstance().getStringSymbol());
        final boolean inheritsIO = classNode.getParent().equals(IdTable.getInstance().getIOSymbol());
        final boolean forbiddenInheritance = inheritsInt || inheritsBool || inheritsString || inheritsIO;
        if (forbiddenInheritance) {
            out.reportBaseClassInheritance(classNode);
            this.classes = this.classes.remove(classNode).add(
                    classNode.setParent(IdTable.getInstance().getObjectSymbol()));
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
