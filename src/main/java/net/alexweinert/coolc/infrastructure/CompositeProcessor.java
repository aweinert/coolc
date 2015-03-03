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

    public V process(T input) {
        return secondProcessor.process(firstProcessor.process(input));
    }
}
