package net.alexweinert.coolc.infrastructure;

public class ProcessorException extends Exception {

    private static final long serialVersionUID = 6670038456067801785L;

    private final Exception cause;

    public ProcessorException(Exception cause) {
        this.cause = cause;
    }

    public Exception getCause() {
        return cause;
    }

}
