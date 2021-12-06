package org.springframework.ntfh.exceptions;

import org.springframework.core.NestedRuntimeException;
import org.springframework.lang.Nullable;

/**
 * This exception is thrown when a user tries to modify a resource that is
 * accessible by his/her token, but the user who the modification is being done
 * by does not match with the one of the token. For instance, if a user tries to
 * edit another user's profile (Even if he passes a valid JWT token
 * authenticating him as a user, the token is not valid because it is not the
 * one who corresponds to the user whose profile is being edited)
 * 
 * @author andrsdt
 */
public class NonMatchingTokenException extends NestedRuntimeException {

    /**
     * Constructor for DataAccessException.
     * 
     * @param msg the detail message
     */
    public NonMatchingTokenException(String message) {
        super(message);
    }

    /**
     * Constructor for DataAccessException.
     * 
     * @param msg   the detail message
     * @param cause the root cause (usually from using a underlying data access API
     *              such as JDBC)
     */
    public NonMatchingTokenException(@Nullable String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }
}
