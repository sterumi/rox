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

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    @Override
    public StackTraceElement[] getStackTrace() {
        return super.getStackTrace();
    }

    @Override
    public String getLocalizedMessage() {
        return super.getLocalizedMessage();
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    @Override
    public synchronized Throwable getCause() {
        return super.getCause();
    }

    @Override
    public void setStackTrace(StackTraceElement[] stackTrace) {
        super.setStackTrace(stackTrace);
    }
}
