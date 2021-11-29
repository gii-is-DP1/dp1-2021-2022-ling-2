package org.springframework.samples.ntfh.exceptions;

import org.springframework.core.NestedRuntimeException;
import org.springframework.lang.Nullable;

public class BannedUserException extends NestedRuntimeException {

    public BannedUserException(String message) {
        super(message);
    }

    public BannedUserException(@Nullable String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }
}
