package net.alexweinert.coolc.semantic_check;

import net.alexweinert.coolc.Output;
import net.alexweinert.coolc.program.ast.Program;

class InterfaceChecker {

    /**
     * Checks for all classes that they only define one attribute of each identifier and only one method of each
     * identifier
     */
    public static Program checkInterfaces(Program program, SemanticErrorReporter err) {
        return DoubleFeatureRemover.removeDoubleFeatures(program, err);
    }

}
