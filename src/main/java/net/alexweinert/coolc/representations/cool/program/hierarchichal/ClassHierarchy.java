package net.alexweinert.coolc.representations.cool.program.hierarchichal;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.alexweinert.coolc.representations.cool.program.parsed.ClassNode;
import net.alexweinert.coolc.representations.cool.program.parsed.ParsedProgramVisitor;
import net.alexweinert.coolc.representations.cool.program.parsed.Program;
import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;
import net.alexweinert.coolc.representations.cool.symboltables.IdTable;

public class ClassHierarchy {

    private static class Builder {
        private class Pair<T, U> {
            final private T x;
            final private U y;

            public Pair(T x, U y) {
                this.x = x;
                this.y = y;
            }
        }

        final private Collection<Pair<IdSymbol, IdSymbol>> inheritanceInfo = new HashSet<>();

        void addInheritance(IdSymbol childClass, IdSymbol parentClass) {
            this.inheritanceInfo.add(new Pair<>(childClass, parentClass));
        }

        ClassHierarchy buildHierarchy() {
            final Map<IdSymbol, List<IdSymbol>> hierarchy = new HashMap<>();
            hierarchy.put(IdTable.getInstance().getObjectSymbol(),
                    Collections.singletonList(IdTable.getInstance().getObjectSymbol()));
            for (Pair<IdSymbol, IdSymbol> singleInheritance : this.inheritanceInfo) {
                final List<IdSymbol> ancestors = this.computeAncestors(singleInheritance.x);
                hierarchy.put(singleInheritance.x, ancestors);
            }
            return new ClassHierarchy(hierarchy);
        }

        private List<IdSymbol> computeAncestors(IdSymbol root) {
            final List<IdSymbol> ancestors = new LinkedList<>();
            ancestors.add(root);
            Pair<IdSymbol, IdSymbol> singleInheritance = this.findSingleInheritance(root);
            while (singleInheritance != null) {
                ancestors.add(singleInheritance.y);
                singleInheritance = this.findSingleInheritance(singleInheritance.y);
            }
            return ancestors;
        }

        private Pair<IdSymbol, IdSymbol> findSingleInheritance(IdSymbol lhs) {
            for (Pair<IdSymbol, IdSymbol> singleInheritance : this.inheritanceInfo) {
                if (singleInheritance.x.equals(lhs)) {
                    return singleInheritance;
                }
            }
            return null;
        }

    }

    private static class Factory extends ParsedProgramVisitor {
        final private Builder builder = new Builder();

        public static ClassHierarchy buildHierarchy(Program ast) {
            final Factory visitor = new Factory();
            ast.acceptVisitor(visitor);
            return visitor.builder.buildHierarchy();
        }

        @Override
        public void visitProgramPreorder(Program program) {
            // Install predefined classes
            this.builder.addInheritance(IdTable.getInstance().getIOSymbol(), IdTable.getInstance().getObjectSymbol());
            this.builder.addInheritance(IdTable.getInstance().getIntSymbol(), IdTable.getInstance().getObjectSymbol());
            this.builder.addInheritance(IdTable.getInstance().getStringSymbol(), IdTable.getInstance()
                    .getObjectSymbol());
            this.builder.addInheritance(IdTable.getInstance().getBoolSymbol(), IdTable.getInstance().getObjectSymbol());
        }

        public void visitClassPreorder(ClassNode classNode) {
            this.builder.addInheritance(classNode.getIdentifier(), classNode.getParent());
        }

    }

    final private Map<IdSymbol, List<IdSymbol>> parentClasses;

    /**
     * The given program may not contain circular inheritance, redefinitions of base classes, or classes that inherit
     * from base classes
     */
    public static ClassHierarchy create(Program program) {
        return Factory.buildHierarchy(program);
    }

    /**
     * @param parentClasses
     *            Contains a mapping of classes to all classes that they conform to, in order. The list must contain the
     *            class itself.
     */
    ClassHierarchy(Map<IdSymbol, List<IdSymbol>> parentClasses) {
        this.parentClasses = new HashMap<>(parentClasses);
    }

    public boolean conformsTo(IdSymbol classOne, IdSymbol classTwo) {
        return this.parentClasses.get(classOne).contains(classTwo);
    }

    public IdSymbol getLeastUpperBound(IdSymbol classOne, IdSymbol classTwo) {
        final List<IdSymbol> commonParents = new LinkedList<>(this.parentClasses.get(classOne));
        commonParents.retainAll(this.parentClasses.get(classTwo));

        return commonParents.get(0);
    }

    /**
     * Returns all ancestors of the given class, including the class itself
     */
    public List<IdSymbol> getWeakAncestors(IdSymbol classIdentifier) {
        return new LinkedList<>(this.parentClasses.get(classIdentifier));
    }

    /**
     * Returns all ancestors of the given class, excluding the class itself
     */
    public List<IdSymbol> getStrictAncestors(IdSymbol classIdentifier) {
        final List<IdSymbol> weakAncestors = this.getWeakAncestors(classIdentifier);
        weakAncestors.remove(0);
        return weakAncestors;
    }
}
