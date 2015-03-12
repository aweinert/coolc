package net.alexweinert.coolc.representations.cool.program.hierarchichal;

import net.alexweinert.coolc.representations.cool.program.parsed.ClassNode;
import net.alexweinert.coolc.representations.cool.program.parsed.Program;
import net.alexweinert.coolc.representations.cool.symboltables.IdTable;
import net.alexweinert.coolc.representations.cool.util.TreeNode;
import net.alexweinert.coolc.representations.cool.util.Visitor;

public class ClassHierarchyFactory extends Visitor {
    final private ClassHierarchyBuilder builder = new ClassHierarchyBuilder();

    public static ClassHierarchy buildHierarchy(TreeNode ast) {
        final ClassHierarchyFactory visitor = new ClassHierarchyFactory();
        ast.acceptVisitor(visitor);
        return visitor.builder.buildHierarchy();
    }

    @Override
    public void visitProgramPreorder(Program program) {
        // Install predefined classes
        this.builder.addInheritance(IdTable.getInstance().getIOSymbol(), IdTable.getInstance().getObjectSymbol());
        this.builder.addInheritance(IdTable.getInstance().getIntSymbol(), IdTable.getInstance().getObjectSymbol());
        this.builder.addInheritance(IdTable.getInstance().getStringSymbol(), IdTable.getInstance().getObjectSymbol());
        this.builder.addInheritance(IdTable.getInstance().getBoolSymbol(), IdTable.getInstance().getObjectSymbol());
    }

    public void visitClassPreorder(ClassNode classNode) {
        this.builder.addInheritance(classNode.getIdentifier(), classNode.getParent());
    }
}
