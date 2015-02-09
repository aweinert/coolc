package net.alexweinert.coolc.semantic_check;

import java.util.List;

import net.alexweinert.coolc.program.ast.Attribute;
import net.alexweinert.coolc.program.ast.Class;
import net.alexweinert.coolc.program.ast.Method;
import net.alexweinert.coolc.program.symboltables.IdSymbol;

public interface ISemanticErrorReporter {
    void reportMultipleAttributes(Class classNode, List<Attribute> attributes);

    void reportMultipleMethods(Class classNode, List<Method> methods);

    void reportOverriddenAttribute(Attribute originalAttribute, Attribute offendingAttribute);

    void reportWronglyOverriddenMethod(Method originalMethod, Method offendingMethod);

    void reportRedefinitionOfBuiltInClass(IdSymbol identifier, Class classNode);

    void reportBaseClassInheritance(Class classNode);

    void reportUndefinedParentClass(Class classNode);
}
