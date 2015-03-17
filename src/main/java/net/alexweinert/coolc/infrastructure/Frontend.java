package net.alexweinert.coolc.infrastructure;

public abstract class Frontend<T> {
    public abstract T process() throws ProcessorException;

    public <U> Frontend<U> append(Processor<T, U> processor) {
        return new CompositeFrontend(this, processor);
    }

    public Compiler<T> append(Backend<T> backend) {
        return new Compiler<T>(this, backend);
    }
}
