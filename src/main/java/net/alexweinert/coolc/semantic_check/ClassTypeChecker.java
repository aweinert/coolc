package net.alexweinert.coolc.semantic_check;

import java.util.Map;

import net.alexweinert.coolc.program.ast.Attribute;
import net.alexweinert.coolc.program.ast.Method;
import net.alexweinert.coolc.program.ast.NoExpression;
import net.alexweinert.coolc.program.ast.visitors.ASTVisitor;
import net.alexweinert.coolc.program.information.ClassHierarchy;
import net.alexweinert.coolc.program.information.DefinedClassSignature;
import net.alexweinert.coolc.program.symboltables.IdSymbol;

class ClassTypeChecker extends ASTVisitor {
    final private IdSymbol classId;
    final private VariablesScope classScope;
    final private Map<IdSymbol, DefinedClassSignature> definedSignatures;
    final private ClassHierarchy hierarchy;
    final private SemanticErrorReporter err;

    ClassTypeChecker(IdSymbol classId, Map<IdSymbol, DefinedClassSignature> definedSignatures,
            ClassHierarchy hierarchy, SemanticErrorReporter err) {
        this.classId = classId;
        this.classScope = VariablesScope.createFromClassSignature(definedSignatures.get(classId));
        this.definedSignatures = definedSignatures;
        this.hierarchy = hierarchy;
        this.err = err;
    }

    @Override
    public void visitAttributePostorder(Attribute attribute) {
        if (attribute.getInitializer() instanceof NoExpression) {
            return;
        }
        final ExpressionTypeChecker checker = new ExpressionTypeChecker(classId, classScope, definedSignatures, err);
        attribute.getInitializer().acceptVisitor(checker);
        final IdSymbol initializerType = checker.getResultType().getTypeId(this.classId);
        final IdSymbol declaredType = attribute.getDeclaredType();
        if (!hierarchy.conformsTo(initializerType, declaredType)) {
            err.reportAttributeInitializerTypeError(attribute, checker.getResultType());
        }
    }

    @Override
    public void visitMethodPostorder(Method method) {
        final VariablesScope methodScope = VariablesScope.createFromMethod(classScope, method);
        final ExpressionTypeChecker checker = new ExpressionTypeChecker(classId, methodScope, definedSignatures, err);
        method.getBody().acceptVisitor(checker);
        final IdSymbol bodyType = checker.getResultType().getTypeId(this.classId);
        final IdSymbol declaredType = method.getReturnType();
        if (!hierarchy.conformsTo(bodyType, declaredType)) {
            err.reportMethodBodyTypeError(method, checker.getResultType());
        }
    }
}
