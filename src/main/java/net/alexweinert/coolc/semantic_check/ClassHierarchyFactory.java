package net.alexweinert.coolc.semantic_check;

import net.alexweinert.coolc.program.ast.Class;
import net.alexweinert.coolc.program.ast.Program;
import net.alexweinert.coolc.program.ast.TreeNode;
import net.alexweinert.coolc.program.ast.visitors.ASTVisitor;
import net.alexweinert.coolc.program.symboltables.IdTable;

class ClassHierarchyFactory extends ASTVisitor {
    final private ClassHierarchyBuilder builder = new ClassHierarchyBuilder();

    public static ClassHierarchy buildHierarchy(TreeNode ast) {
        final ClassHierarchyFactory visitor = new ClassHierarchyFactory();
        ast.acceptVisitor(visitor);
        return visitor.builder.buildHierarchy();
    }

    @Override
    public void visitProgramPreorder(Program program) {
        // Install predefined classes
        this.builder.addInheritance(IdTable.getInstance().addString("IO"), IdTable.getInstance().addString("Object"));
        this.builder.addInheritance(IdTable.getInstance().addString("Int"), IdTable.getInstance().addString("Object"));
        this.builder.addInheritance(IdTable.getInstance().addString("String"), IdTable.getInstance()
                .addString("Object"));
        this.builder.addInheritance(IdTable.getInstance().addString("Bool"), IdTable.getInstance().addString("Object"));
    }

    public void visitClassPreorder(Class classNode) {
        this.builder.addInheritance(classNode.getIdentifier(), classNode.getParent());
    }
}
