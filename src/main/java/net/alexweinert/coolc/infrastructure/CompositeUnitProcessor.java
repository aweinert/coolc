package net.alexweinert.coolc.infrastructure;

class CompositeUnitProcessor<T, U> implements UnitProcessor<T> {
    final private Processor<T, U> previousProcessor;
    final private UnitProcessor<U> finalProcessor;

    public static <T, U> CompositeUnitProcessor<T, U> create(Processor<T, U> previousProcessor,
            UnitProcessor<U> finalProcessor) {
        return new CompositeUnitProcessor<>(previousProcessor, finalProcessor);
    }

    private CompositeUnitProcessor(Processor<T, U> previousProcessor, UnitProcessor<U> finalProcessor) {
        this.previousProcessor = previousProcessor;
        this.finalProcessor = finalProcessor;
    }

    @Override
    public void process(T input) {
        final U intermediate = previousProcessor.process(input);
        if (intermediate != null) {
            finalProcessor.process(intermediate);
        }
    }

}
