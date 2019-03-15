package org.jiuwo.fastel.exception;

/**
 * @author Steven Han
 */
public class FastElException extends RuntimeException {
    private static final long serialVersionUID = 5490125795257377954L;

    public FastElException() {
    }

    public FastElException(String message) {
        super(message);
    }

    public FastElException(String message, Throwable cause) {
        super(message, cause);
    }

    public FastElException(Throwable cause) {
        super(cause);
    }
}
