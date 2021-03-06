package net.alexweinert.coolc.processors.cool.typecheck;

import java.util.Map;

import net.alexweinert.coolc.representations.cool.ast.Attribute;
import net.alexweinert.coolc.representations.cool.ast.Method;
import net.alexweinert.coolc.representations.cool.ast.NoExpression;
import net.alexweinert.coolc.representations.cool.ast.Visitor;
import net.alexweinert.coolc.representations.cool.information.ClassHierarchy;
import net.alexweinert.coolc.representations.cool.information.DefinedClassSignature;
import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;

class ClassTypeChecker extends Visitor {
    final private IdSymbol classId;
    final private VariablesScope classScope;
    final private Map<IdSymbol, DefinedClassSignature> definedSignatures;
    final private ClassHierarchy hierarchy;
    final private TypeErrorReporter err;

    ClassTypeChecker(IdSymbol classId, Map<IdSymbol, DefinedClassSignature> definedSignatures,
            ClassHierarchy hierarchy, TypeErrorReporter err) {
        this.classId = classId;
        this.classScope = VariablesScope.createFromClassSignature(classId, definedSignatures.get(classId));
        this.definedSignatures = definedSignatures;
        this.hierarchy = hierarchy;
        this.err = err;
    }

    @Override
    public void visitAttributePostorder(Attribute attribute) {
        if (attribute.getInitializer() instanceof NoExpression) {
            return;
        }
        final ExpressionTypeChecker checker = new ExpressionTypeChecker(classId, classScope, hierarchy,
                definedSignatures, err);
        attribute.getInitializer().acceptVisitor(checker);
        final ExpressionType initializerType = checker.getResultType();
        final ExpressionType declaredType = ExpressionType.create(attribute.getDeclaredType(), this.classId);
        if (!initializerType.conformsTo(declaredType, this.hierarchy)) {
            err.reportAttributeInitializerTypeError(attribute, checker.getResultType());
        }
    }

    @Override
    public void visitMethodPostorder(Method method) {
        final VariablesScope methodScope = VariablesScope.createFromMethod(this.classId, classScope, method);
        final ExpressionTypeChecker checker = new ExpressionTypeChecker(classId, methodScope, hierarchy,
                definedSignatures, err);
        method.getBody().acceptVisitor(checker);
        final ExpressionType bodyType = checker.getResultType();
        final ExpressionType declaredType = ExpressionType.create(method.getReturnType(), this.classId);
        if (!bodyType.conformsTo(declaredType, this.hierarchy)) {
            err.reportMethodBodyTypeError(method, checker.getResultType());
        }
    }
}
