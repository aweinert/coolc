package net.alexweinert.coolc.processors.cool.typechecking;

import net.alexweinert.coolc.Output;
import net.alexweinert.coolc.representations.cool.ast.Attribute;
import net.alexweinert.coolc.representations.cool.ast.FunctionCall;
import net.alexweinert.coolc.representations.cool.ast.Method;
import net.alexweinert.coolc.representations.cool.ast.ObjectReference;
import net.alexweinert.coolc.representations.cool.ast.TreeNode;
import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;

class TypeErrorReporter {
    private final Output out;

    public TypeErrorReporter(Output out) {
        this.out = out;
    }

    public void reportAttributeInitializerTypeError(Attribute attribute, ExpressionType initializerType) {
        final String formatString = "Type error in initialization of attribute %s at %s:%d\n  Declared Type: %s\n  Type of initializer: %s";
        out.error(String.format(formatString, attribute.getName(), attribute.getFilename(), attribute.getLineNumber(),
                attribute.getDeclaredType(), initializerType));

    }

    public void reportMethodBodyTypeError(Method method, ExpressionType resultType) {
        final String formatString = "Type error in definition of method %s at %s:%d\n  Declared Type: %s\n  Type of body expression: %s";
        out.error(String.format(formatString, method.getName(), method.getFilename(), method.getLineNumber(),
                method.getReturnType(), resultType));

    }

    public void reportTypeMismatch(TreeNode expression, IdSymbol actualType, IdSymbol expexctedType) {
        final String formatString = "Type mismatch in operands of expression at %s:%d\n  Expected Type: %s\n  Actual Type: %s\n";
        out.error(String.format(formatString, expression.getFilename(), expression.getLineNumber(), expexctedType,
                actualType));
    }

    public void reportVariableOutOfScope(ObjectReference reference) {
        final String formatString = "Variable %s does not exist in current scope at %s:%d";
        out.error(String.format(formatString, reference.getVariableIdentifier(), reference.getFilename(),
                reference.getLineNumber()));
    }

    public void reportUndefinedMethod(FunctionCall call, IdSymbol calleeType) {
        final String formatString = "Call of undefined method at %s:%d\n  Type %s of callee expression does not define method %s";
        out.error(String.format(formatString, call.getFilename(), call.getLineNumber(), calleeType.toString(), call
                .getFunctionIdentifier().toString()));
    }

    public void reportWrongNumberOfFunctionArguments(FunctionCall call, int expectedNumberOfArguments) {
        final String formatString = "Wrong invocation of method at %s:%d\n  Expected number of arguments: %d\n  Given number of arguments: %d";
        out.error(String.format(formatString, call.getFilename(), call.getLineNumber(), expectedNumberOfArguments, call
                .getArguments().size()));
    }

}
