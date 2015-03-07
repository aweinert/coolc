package net.alexweinert.coolc.infrastructure;

public class Tee<T> extends Processor<T, T> {
    private final Backend<T> backend;

    public Tee(Backend<T> backend) {
        this.backend = backend;
    }

    @Override
    public T process(T input) {
        this.backend.process(input);
        return input;
    }

}
