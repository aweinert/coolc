package net.alexweinert.coolc.semantic_check;

import java.util.LinkedList;
import java.util.Map;
import java.util.Stack;

import net.alexweinert.coolc.program.ast.visitors.ASTVisitor;
import net.alexweinert.coolc.program.information.DefinedClassSignature;
import net.alexweinert.coolc.program.symboltables.IdSymbol;

class ExpressionTypeChecker extends ASTVisitor {

    final private Stack<ExpressionType> argumentTypes = new Stack<>();

    final private IdSymbol classId;
    final private Stack<VariablesScope> variablesScopes;
    final private Map<IdSymbol, DefinedClassSignature> definedSignatures;
    final private SemanticErrorReporter err;

    public ExpressionTypeChecker(IdSymbol classId, VariablesScope initialScope,
            Map<IdSymbol, DefinedClassSignature> definedSignatures, SemanticErrorReporter err) {
        this.classId = classId;
        this.variablesScopes = new Stack<>();
        this.variablesScopes.add(initialScope);
        this.definedSignatures = definedSignatures;
        this.err = err;
    }

    public ExpressionType getResultType() {
        assert this.argumentTypes.size() == 1;
        return this.argumentTypes.peek();
    }
}
