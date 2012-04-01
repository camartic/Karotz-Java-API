package org.peripheralware.karotz.evil;

import org.peripheralware.karotz.KarotzException;

import java.io.UnsupportedEncodingException;

public class UnableToBuildKarotzWebAPIURLException extends KarotzException {
    public UnableToBuildKarotzWebAPIURLException(UnsupportedEncodingException e) {
        super(e);
    }
}
