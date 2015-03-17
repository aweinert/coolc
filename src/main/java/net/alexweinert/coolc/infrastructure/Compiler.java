package net.alexweinert.coolc.infrastructure;

public class Compiler<T> {
    private final Frontend<T> frontend;
    private final Backend<T> backend;

    Compiler(Frontend<T> frontend, Backend<T> backend) {
        this.frontend = frontend;
        this.backend = backend;
    }

    public void compile() throws ProcessorException {
        final T frontendResult = frontend.process();
        assert frontendResult != null;
        this.backend.process(frontendResult);
    }
}
