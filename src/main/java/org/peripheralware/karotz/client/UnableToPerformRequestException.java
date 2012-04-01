package org.peripheralware.karotz.client;

import org.peripheralware.karotz.KarotzException;

import java.io.IOException;

public class UnableToPerformRequestException extends KarotzException {
    public UnableToPerformRequestException(Throwable cause) {
        super(cause);
    }

    public UnableToPerformRequestException(String message) {
        super(message);
    }
}
