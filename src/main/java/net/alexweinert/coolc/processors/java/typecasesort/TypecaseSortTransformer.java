package net.alexweinert.coolc.processors.java.typecasesort;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import net.alexweinert.coolc.representations.cool.ast.Case;
import net.alexweinert.coolc.representations.cool.ast.Cases;
import net.alexweinert.coolc.representations.cool.ast.Expression;
import net.alexweinert.coolc.representations.cool.ast.Transformer;
import net.alexweinert.coolc.representations.cool.ast.Typecase;
import net.alexweinert.coolc.representations.cool.information.ClassHierarchy;
import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;

class TypecaseSortTransformer extends Transformer {

    final private ClassHierarchy hierarchy;

    public TypecaseSortTransformer(ClassHierarchy hierarchy) {
        this.hierarchy = hierarchy;
    }

    private class TypecaseComparator implements Comparator<Case> {

        @Override
        public int compare(Case lhsCase, Case rhsCase) {
            final IdSymbol lhsType = lhsCase.getDeclaredType();
            final IdSymbol rhsType = rhsCase.getDeclaredType();

            if (hierarchy.conformsTo(lhsType, rhsType)) {
                return -1;
            } else if (hierarchy.conformsTo(rhsType, lhsType)) {
                return 1;
            } else {
                return 0;
            }
        }

    }

    @Override
    public Expression transformTypecase(Typecase typecase) {
        final List<Case> cases = new LinkedList<>();
        for (Case caseNode : typecase.getCases()) {
            cases.add(caseNode);
        }

        Collections.sort(cases, new TypecaseComparator());
        final Cases newCases = new Cases(typecase.getCases().getFilename(), typecase.getCases().getLineNumber(), cases);
        return new Typecase(typecase.getFilename(), typecase.getLineNumber(), typecase.getExpression(), newCases);
    }
}
