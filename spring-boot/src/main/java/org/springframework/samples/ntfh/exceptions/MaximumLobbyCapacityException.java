package org.springframework.samples.ntfh.exceptions;

import org.springframework.core.NestedRuntimeException;
import org.springframework.lang.Nullable;

public class MaximumLobbyCapacityException extends NestedRuntimeException {

    /**
     * Constructor for DataAccessException.
     * 
     * @param msg the detail message
     */
    public MaximumLobbyCapacityException(String message) {
        super(message);
    }

    /**
     * Constructor for DataAccessException.
     * 
     * @param msg   the detail message
     * @param cause the root cause (usually from using a underlying data access API
     *              such as JDBC)
     */
    public MaximumLobbyCapacityException(@Nullable String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }
}
