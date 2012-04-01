package org.peripheralware.karotz.action;

import java.util.Map;

public interface KarotzAction {
    String getURL();

    Map<String, String> getParameters();
}
