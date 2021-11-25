package org.springframework.samples.ntfh.exceptions;

import org.springframework.core.NestedRuntimeException;
import org.springframework.lang.Nullable;

public class UserAlreadyInLobbyException extends NestedRuntimeException {

    public UserAlreadyInLobbyException(String message) {
        super(message);
    }

    public UserAlreadyInLobbyException(@Nullable String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }
}
