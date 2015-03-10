package net.alexweinert.coolc.processors.cool.frontend.semantic_check;

import java.util.Map;

import net.alexweinert.coolc.representations.cool.ast.ClassNode;
import net.alexweinert.coolc.representations.cool.ast.Program;
import net.alexweinert.coolc.representations.cool.information.ClassHierarchy;
import net.alexweinert.coolc.representations.cool.information.DefinedClassSignature;
import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;

public class TypeChecker {
    public static void typecheck(Program program, ClassHierarchy hierarchy,
            Map<IdSymbol, DefinedClassSignature> definedSignatures, SemanticErrorReporter err) {
        for (ClassNode classNode : program.getClasses()) {
            classNode.acceptVisitor(new ClassTypeChecker(classNode.getIdentifier(), definedSignatures, hierarchy, err));
        }
    }

}
