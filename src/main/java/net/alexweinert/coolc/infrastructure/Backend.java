package net.alexweinert.coolc.infrastructure;

public interface Backend<T> {
    void process(T input);
}
