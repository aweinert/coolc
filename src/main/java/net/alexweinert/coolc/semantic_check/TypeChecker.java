package net.alexweinert.coolc.semantic_check;

import java.util.Map;

import net.alexweinert.coolc.program.ast.ClassNode;
import net.alexweinert.coolc.program.ast.Program;
import net.alexweinert.coolc.program.information.ClassHierarchy;
import net.alexweinert.coolc.program.information.DefinedClassSignature;
import net.alexweinert.coolc.program.symboltables.IdSymbol;

public class TypeChecker {
    public static void typecheck(Program program, ClassHierarchy hierarchy,
            Map<IdSymbol, DefinedClassSignature> definedSignatures, SemanticErrorReporter err) {
        for (ClassNode classNode : program.getClasses()) {
            classNode.acceptVisitor(new ClassTypeChecker(classNode.getIdentifier(), definedSignatures, hierarchy, err));
        }
    }

}
