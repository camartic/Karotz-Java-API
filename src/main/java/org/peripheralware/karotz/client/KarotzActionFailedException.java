package org.peripheralware.karotz.client;

public class KarotzActionFailedException extends UnableToPerformRequestException {
    private String code;

    public KarotzActionFailedException (String message, String code) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
