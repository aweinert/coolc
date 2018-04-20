package net.alexweinert.coolc.processors.cool.frontend;

import java_cup.runtime.Symbol;

public class LineSymbol extends Symbol {
    private final int lineno;

    public LineSymbol(int sym_num, int lineno) {
        super(sym_num);
        this.lineno = lineno;
    }

    public LineSymbol(int sym_num, int lineno, Object object) {
        super(sym_num, object);
        this.lineno = lineno;
    }

    public int get_lineno() { return this.lineno; }
}
