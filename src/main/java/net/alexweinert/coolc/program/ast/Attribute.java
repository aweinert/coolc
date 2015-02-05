package net.alexweinert.coolc.program.ast;

import java.io.PrintStream;

import net.alexweinert.coolc.program.Utilities;
import net.alexweinert.coolc.program.ast.visitors.ASTVisitor;
import net.alexweinert.coolc.program.symboltables.IdSymbol;
import net.alexweinert.coolc.program.symboltables.ClassTable;
import net.alexweinert.coolc.program.symboltables.FeatureTable;
import net.alexweinert.coolc.program.symboltables.TreeConstants;

/**
 * Defines AST constructor 'attr'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
public class Attribute extends Feature {
    final protected IdSymbol type_decl;
    final protected Expression init;

    /**
     * Creates "attr" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for name
     * @param a1
     *            initial value for type_decl
     * @param a2
     *            initial value for init
     */
    public Attribute(String filename, int lineNumber, IdSymbol a1, IdSymbol a2, Expression a3) {
        super(filename, lineNumber, a1);
        this.type_decl = a2;
        this.init = a3;
    }

    public IdSymbol getName() {
        return this.name;
    }

    @Override
    public void typecheck(Class enclosingClass, ClassTable classTable, FeatureTable featureTable) {
        if (this.init instanceof NoExpression) {
            // If we have no initializer, we simply believe whatever the declaration tells us
            return;
        } else {
            FeatureTable extendedFeatureTable = featureTable.copyAndExtend(enclosingClass.getIdentifier(),
                    TreeConstants.self, TreeConstants.SELF_TYPE);
            IdSymbol initializerType = this.init.typecheck(enclosingClass, classTable, extendedFeatureTable);
            // Could also use the featureTable at this point, but taking the declared type directly saves us one
            // indirection
            IdSymbol declaredType = this.type_decl;
            if (!classTable.conformsTo(enclosingClass.getIdentifier(), initializerType, declaredType)) {
                String errorString = String.format(
                        "Inferred type %s of initialization of attribute %s does not conform to declared type %s.",
                        initializerType, this.name, declaredType);
                classTable.semantError(enclosingClass.getFilename(), this).println(errorString);
            }
        }
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "attr\n");
        dump_IdSymbol(out, n + 2, name);
        dump_IdSymbol(out, n + 2, type_decl);
        init.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_attr");
        dump_IdSymbol(out, n + 2, name);
        dump_IdSymbol(out, n + 2, type_decl);
        init.dump_with_types(out, n + 2);
    }

    public IdSymbol getTypeDecl() {
        return this.type_decl;
    }

    @Override
    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visitAttributePreorder(this);
        this.init.acceptVisitor(visitor);
        visitor.visitAttributePostorder(this);
    }

    public IdSymbol getDeclaredType() {
        return type_decl;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((init == null) ? 0 : init.hashCode());
        result = prime * result + ((type_decl == null) ? 0 : type_decl.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Attribute other = (Attribute) obj;
        if (init == null) {
            if (other.init != null) {
                return false;
            }
        } else if (!init.equals(other.init)) {
            return false;
        }
        if (type_decl == null) {
            if (other.type_decl != null) {
                return false;
            }
        } else if (!type_decl.equals(other.type_decl)) {
            return false;
        }
        return true;
    }

}