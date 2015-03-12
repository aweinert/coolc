package net.alexweinert.coolc.representations.cool.program.parsed;

public class ParsedProgramVisitor {

    public void visitProgramPreorder(Program program) {}

    public void visitProgramPostorder(Program program) {}

    public void visitClassesPreorder(Classes classes) {}

    public void visitClassesInorder(Classes classes) {}

    public void visitClassesPostorder(Classes classes) {}

    public void visitClassPreorder(ClassNode classNode) {}

    public void visitClassPostorder(ClassNode classNode) {}

    public void visitFeaturesPreorder(Features features) {}

    public void visitFeaturesInorder(Features features) {}

    public void visitFeaturesPostorder(Features features) {}

    public void visitAttribute(Attribute attribute) {}

    public void visitMethod(Method method) {}
}
