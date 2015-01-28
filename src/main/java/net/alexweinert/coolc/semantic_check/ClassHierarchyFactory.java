package net.alexweinert.coolc.semantic_check;

import net.alexweinert.coolc.program.ast.Class;
import net.alexweinert.coolc.program.ast.Program;
import net.alexweinert.coolc.program.ast.TreeNode;
import net.alexweinert.coolc.program.ast.visitors.ASTVisitor;
import net.alexweinert.coolc.program.symboltables.AbstractTable;

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
        this.builder.addInheritance(AbstractTable.stringtable.addString("IO"),
                AbstractTable.stringtable.addString("Object"));
        this.builder.addInheritance(AbstractTable.stringtable.addString("Int"),
                AbstractTable.stringtable.addString("Object"));
        this.builder.addInheritance(AbstractTable.stringtable.addString("String"),
                AbstractTable.stringtable.addString("Object"));
        this.builder.addInheritance(AbstractTable.stringtable.addString("Bool"),
                AbstractTable.stringtable.addString("Object"));
    }

    public void visitClassPreorder(Class classNode) {
        this.builder.addInheritance(classNode.getIdentifier(), classNode.getParent());
    }
}
