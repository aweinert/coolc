package net.alexweinert.coolc.infrastructure;

class CompositeBackend<T, U> implements Backend<T> {
    final private Processor<T, U> previousProcessor;
    final private Backend<U> finalProcessor;

    public static <T, U> CompositeBackend<T, U> create(Processor<T, U> previousProcessor,
            Backend<U> finalProcessor) {
        return new CompositeBackend<>(previousProcessor, finalProcessor);
    }

    private CompositeBackend(Processor<T, U> previousProcessor, Backend<U> finalProcessor) {
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
