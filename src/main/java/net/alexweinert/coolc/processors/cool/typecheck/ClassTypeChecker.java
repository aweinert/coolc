package net.alexweinert.coolc.processors.cool.typecheck;

import java.util.Map;

import net.alexweinert.coolc.representations.cool.expressions.untyped.NoExpression;
import net.alexweinert.coolc.representations.cool.program.hierarchichal.ClassHierarchy;
import net.alexweinert.coolc.representations.cool.program.hierarchichal.DefinedClassSignature;
import net.alexweinert.coolc.representations.cool.program.parsed.Attribute;
import net.alexweinert.coolc.representations.cool.program.parsed.Method;
import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;
import net.alexweinert.coolc.representations.cool.util.Visitor;

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
        final IdSymbol initializerType = checker.getResultType().getTypeId(this.classId);
        final IdSymbol declaredType = attribute.getDeclaredType();
        if (!hierarchy.conformsTo(initializerType, declaredType)) {
            err.reportAttributeInitializerTypeError(attribute, checker.getResultType());
        }
    }

    @Override
    public void visitMethodPostorder(Method method) {
        final VariablesScope methodScope = VariablesScope.createFromMethod(classScope, method);
        final ExpressionTypeChecker checker = new ExpressionTypeChecker(classId, methodScope, hierarchy,
                definedSignatures, err);
        method.getBody().acceptVisitor(checker);
        final IdSymbol bodyType = checker.getResultType().getTypeId(this.classId);
        final IdSymbol declaredType = method.getReturnType();
        if (!hierarchy.conformsTo(bodyType, declaredType)) {
            err.reportMethodBodyTypeError(method, checker.getResultType());
        }
    }
}