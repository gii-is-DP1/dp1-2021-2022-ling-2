package org.springframework.ntfh.exceptions;

import org.springframework.core.NestedRuntimeException;
import org.springframework.lang.Nullable;

public class InvalidValueException extends NestedRuntimeException {
    // TODO remove and use IllegalArgumentException instead

    public InvalidValueException(String message) {
        super(message);
    }

    public InvalidValueException(@Nullable String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }
}
