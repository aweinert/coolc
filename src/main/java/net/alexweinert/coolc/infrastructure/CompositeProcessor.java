package net.alexweinert.coolc.infrastructure;

class CompositeProcessor<T, U, V> extends Processor<T, V> {
    final private Processor<T, U> firstProcessor;
    final private Processor<U, V> secondProcessor;

    public static <T, U, V> CompositeProcessor<T, U, V> create(Processor<T, U> processorOne,
            Processor<U, V> processorTwo) {
        return new CompositeProcessor<>(processorOne, processorTwo);
    }

    private CompositeProcessor(Processor<T, U> firstProcessor, Processor<U, V> secondProcessor) {
        this.firstProcessor = firstProcessor;
        this.secondProcessor = secondProcessor;
    }

    public V process(T input) throws ProcessorException {
        final U intermediate = firstProcessor.process(input);
        assert intermediate != null : this.firstProcessor.getClass().getSimpleName() + " returned null";
        final V result = secondProcessor.process(intermediate);
        assert result != null : this.secondProcessor.getClass().getSimpleName() + " returned null";
        return result;
    }

    public String toString() {
        return this.firstProcessor + " -> " + this.secondProcessor;
    }
}
