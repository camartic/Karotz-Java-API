/*
 * The MIT License
 *
 * Copyright (c) 2011, Seiji Sogabe
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.peripheralware.karotz.action;

import java.util.Map;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.*;
import org.junit.Test;
import org.peripheralware.karotz.action.led.LedColor;
import org.peripheralware.karotz.action.led.LedFadeAction;
import org.peripheralware.karotz.action.led.LedLightAction;

/**
 * Test for LedFadeAction
 *
 * @author Martin Ritchie
 */
public class LedFadeActionTest {

    @Test
    public void testGetParameters_valuesSet_areReturned() {
        LedFadeAction action = new LedFadeAction("FF0000", 1000);

        Map<String, String> params = action.getParameters();
        assertThat(params).isNotNull();

        assertThat(params.size()).isEqualTo(3);
        assertThat(params.get("action")).isEqualTo("fade");
        assertThat(params.get("color")).isEqualTo("FF0000");
        assertThat(params.get("period")).isEqualTo("1000");
    }

    @Test
    public void testGetParameters_setViaLedColor_setCorrectString() {
        LedFadeAction action = new LedFadeAction(LedColor.RED, 1000);

        Map<String, String> params = action.getParameters();
        assertThat(params).isNotNull();
        assertEquals("FF0000", params.get("color"));
    }

    @Test
    public void testGetParameters_setNullString_actionIsSetNoColor() {
        LedFadeAction action = new LedFadeAction((String)null, 100);

        Map<String, String> params = action.getParameters();
        assertThat(params).isNotNull();

        assertThat(params.get("color")).isNull();
    }

}
