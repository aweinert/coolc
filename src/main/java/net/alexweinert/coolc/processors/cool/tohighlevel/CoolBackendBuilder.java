package net.alexweinert.coolc.processors.cool.tohighlevel;

import java.util.Collection;
import java.util.List;

import net.alexweinert.coolc.infrastructure.ProcessorException;
import net.alexweinert.coolc.representations.cool.ast.Formals;
import net.alexweinert.coolc.representations.cool.symboltables.BoolSymbol;
import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;
import net.alexweinert.coolc.representations.cool.symboltables.IntSymbol;
import net.alexweinert.coolc.representations.cool.symboltables.StringSymbol;

public interface CoolBackendBuilder<T, U> {

    U buildProgram(List<T> classes);

    void beginClass(IdSymbol idSymbol, IdSymbol parentSymbol);

    void endClass();

    T build();

    Collection<T> buildBasicClasses() throws ProcessorException;

    void beginAttributeDefinition(IdSymbol typeSymbol, IdSymbol idSymbol);

    void endAttributeDefinition(String variable);

    String declareVariable(IdSymbol typeSymbol);

    void declareVariable(IdSymbol typeSymbol, String id);

    void loadVoid(String target);

    void loadBoolean(String target, BoolSymbol value);

    void loadInt(String target, IntSymbol value);

    void loadString(String target, StringSymbol value);

    void loadVariable(String varName, IdSymbol variableIdentifier);

    void startMethodDefinition(IdSymbol returnTypeSymbol, IdSymbol idSymbol, Formals formals);

    void endMethodDefinition(String returnVariable);

    void arithNeg(String result, String arg);

    void add(String result, String lhs, String rhs);

    void sub(String result, String lhs, String rhs);

    void mul(String result, String lhs, String rhs);

    void div(String result, String lhs, String rhs);

    void lt(String result, String lhs, String rhs);

    void lte(String result, String lhs, String rhs);

    void eq(String result, String lhs, String rhs);

    void isVoid(String result, String arg);

    void boolNeg(String result, String arg);

    void assign(String result, String arg);

    String toVariable(IdSymbol arg);

    void New(String returnVariable, IdSymbol typeSymbol);

    void beginIf();

    void beginThen(String conditionVariable);

    void beginElse();

    void endIf();

    void beginLoop();

    void endLoopCondition(String conditionVariable);

    void endLoop();

    void functionCall(String resultVariable, String dispatchVariable, String dispatchVariableType,
            IdSymbol functionIdentifier, IdSymbol returnType, List<String> arguments, List<String> argumentTypes);

    void staticFunctionCall(String resultVariable, String dispatchVariable, IdSymbol functionIdentifier,
            IdSymbol staticType, IdSymbol returnType, List<String> arguments, List<String> argumentTypes);

    void beginTypecase(String expressionVariable);

    void beginCase(String expressionVariable, IdSymbol typeSymbol, IdSymbol idSymbol);

    void endCase();

    void endTypecase();

}