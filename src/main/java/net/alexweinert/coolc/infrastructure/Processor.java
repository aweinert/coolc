package net.alexweinert.coolc.infrastructure;

public abstract class Processor<T, U> {
    public abstract U process(T input) throws ProcessorException;

    public <V> Processor<T, V> append(Processor<U, V> appendix) {
        return CompositeProcessor.create(this, appendix);
    }

    public Backend<T> append(Backend<U> unitProcessor) {
        return CompositeBackend.create(this, unitProcessor);
    }
}
