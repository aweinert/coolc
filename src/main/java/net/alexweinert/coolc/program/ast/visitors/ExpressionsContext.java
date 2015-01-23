package net.alexweinert.coolc.program.ast.visitors;

import java.util.Stack;

public class ExpressionsContext {
    final private Stack<Integer> context = new Stack<>();

    public void pushFunctionCallContext() {
        this.context.push(0);
    }

    public void pushBlockContext() {
        this.context.push(1);
    }

    public void popContext() {
        this.context.pop();
    }

    public boolean inFunctionCall() {
        return this.context.peek() == 0;
    }

    public boolean inBlock() {
        return this.context.peek() == 1;
    }
}
