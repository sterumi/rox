package rox.main.exception;

public class IllegalVariableChangeException extends Exception {

    public IllegalVariableChangeException() {
        super();
    }

    public IllegalVariableChangeException(String message) {
        super(message);
    }

    public IllegalVariableChangeException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalVariableChangeException(Throwable cause) {
        super(cause);
    }

    private static final long serialVersionUID = 67406745986794563L;

}
