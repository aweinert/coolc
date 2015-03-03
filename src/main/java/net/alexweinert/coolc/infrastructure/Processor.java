package net.alexweinert.coolc.infrastructure;

public abstract class Processor<T, U> {
    public abstract U process(T input);

    public <V> Processor<T, V> append(Processor<U, V> appendix) {
        return CompositeProcessor.create(this, appendix);
    }

    public UnitProcessor<T> append(UnitProcessor<U> unitProcessor) {
        return CompositeUnitProcessor.create(this, unitProcessor);
    }
}
