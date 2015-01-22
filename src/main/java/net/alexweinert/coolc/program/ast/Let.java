package net.alexweinert.coolc.program.ast;

import java.io.PrintStream;

import net.alexweinert.coolc.program.Utilities;
import net.alexweinert.coolc.program.ast.visitors.ASTVisitor;
import net.alexweinert.coolc.program.symboltables.AbstractSymbol;
import net.alexweinert.coolc.program.symboltables.ClassTable;
import net.alexweinert.coolc.program.symboltables.FeatureTable;
import net.alexweinert.coolc.program.symboltables.TreeConstants;

/**
 * Defines AST constructor 'let'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
public class Let extends Expression {
    final protected AbstractSymbol identifier;
    final protected AbstractSymbol type_decl;
    final protected Expression init;
    final protected Expression body;

    /**
     * Creates "let" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for identifier
     * @param a1
     *            initial value for type_decl
     * @param a2
     *            initial value for init
     * @param a3
     *            initial value for body
     */
    public Let(int lineNumber, AbstractSymbol a1, AbstractSymbol a2, Expression a3, Expression a4) {
        super(lineNumber);
        identifier = a1;
        type_decl = a2;
        init = a3;
        body = a4;
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "let\n");
        dump_AbstractSymbol(out, n + 2, identifier);
        dump_AbstractSymbol(out, n + 2, type_decl);
        init.dump(out, n + 2);
        body.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_let");
        dump_AbstractSymbol(out, n + 2, identifier);
        dump_AbstractSymbol(out, n + 2, type_decl);
        init.dump_with_types(out, n + 2);
        body.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    @Override
    protected AbstractSymbol inferType(Class enclosingClass, ClassTable classTable, FeatureTable featureTable) {
        // Make sure that we do not try to bindlet
        if (this.identifier.equals(TreeConstants.self)) {
            String errorString = "'self' cannot be bound in a 'let' expression.";
            classTable.semantError(enclosingClass.getFilename(), this).println(errorString);
        }

        // Typecheck the initializer with the current environment
        AbstractSymbol initializerType = this.init.typecheck(enclosingClass, classTable, featureTable);

        if (!classTable.conformsTo(enclosingClass.getName(), initializerType, this.type_decl)) {
            String errorString = String.format(
                    "Inferred type %s of initialization of %s does not conform to identifier's declared type %s.",
                    initializerType, this.identifier, this.type_decl);
            classTable.semantError(enclosingClass.getFilename(), this).println(errorString);
        }

        FeatureTable extendedTable = featureTable.copyAndExtend(enclosingClass.getName(), this.identifier,
                this.type_decl);
        return this.body.typecheck(enclosingClass, classTable, extendedTable);
    }

    @Override
    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visitLetPreorder(this);
        this.init.acceptVisitor(visitor);
        visitor.visitLetInorder(this);
        this.body.acceptVisitor(visitor);
        visitor.visitLetPostorder(this);
    }

}