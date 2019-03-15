package org.jiuwo.fastel.exception;

/**
 * @author Steven Han
 */
public class ExpressionSyntaxException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public ExpressionSyntaxException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExpressionSyntaxException(String message) {
        super(message);
    }
}
