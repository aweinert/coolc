package net.alexweinert.coolc.infrastructure;

class CompositeFrontend<T, U> extends Frontend<U> {
    final Frontend<T> frontend;
    final Processor<T, U> processor;

    CompositeFrontend(Frontend<T> frontend, Processor<T, U> processor) {
        this.frontend = frontend;
        this.processor = processor;
    }

    @Override
    public U process() throws ProcessorException {
        final T frontendResult = this.frontend.process();
        assert frontendResult != null;
        return this.processor.process(frontendResult);
    }
}
