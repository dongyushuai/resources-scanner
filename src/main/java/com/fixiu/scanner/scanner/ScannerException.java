package com.fixiu.scanner.scanner;

/**
 * Exception thrown when Scanner encounters a problem.
 */
public class ScannerException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
     * Creates a new ScannerException with this message and this cause.
     *
     * @param message The exception message.
     * @param cause   The exception cause.
     */
    public ScannerException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new ScannerException with this cause. For use in subclasses that override getMessage().
     *
     * @param cause   The exception cause.
     */
    public ScannerException(Throwable cause) {
        super(cause);
    }

    /**
     * Creates a new ScannerException with this message.
     *
     * @param message The exception message.
     */
    public ScannerException(String message) {
        super(message);
    }

    /**
     * Creates a new ScannerException. For use in subclasses that override getMessage().
     */
    public ScannerException() {
        super();
    }
}