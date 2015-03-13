package net.alexweinert.coolc.representations.cool.program.parsed;

public class ParsedProgramVisitor {

    public void visitProgramPreorder(ParsedProgram program) {}

    public void visitProgramPostorder(ParsedProgram program) {}

    public void visitClassesPreorder(ParsedClasses classes) {}

    public void visitClassesInorder(ParsedClasses classes) {}

    public void visitClassesPostorder(ParsedClasses classes) {}

    public void visitClassPreorder(ParsedClass classNode) {}

    public void visitClassPostorder(ParsedClass classNode) {}

    public void visitFeaturesPreorder(ParsedFeatures features) {}

    public void visitFeaturesInorder(ParsedFeatures features) {}

    public void visitFeaturesPostorder(ParsedFeatures features) {}

    public void visitAttribute(ParsedAttribute attribute) {}

    public void visitMethod(ParsedMethod method) {}
}
