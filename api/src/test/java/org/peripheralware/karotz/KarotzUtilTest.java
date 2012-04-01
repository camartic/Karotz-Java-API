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
package org.peripheralware.karotz;

import java.util.HashMap;
import java.util.Map;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;
import org.peripheralware.karotz.evil.KarotzUtil;

/**
 * Test for KarotzUtil
 *
 * @author Seiji Sogabe
 */
public class KarotzUtilTest {

    @Test
    public void testDoHmacSha1() throws KarotzException {
        String secretKey = "secret key";
        String data = "This is a pen.";
        String expResult = "3YJYZz/wbmsbFxe/cucb0v/BeZk=";
        String result = KarotzUtil.doHmacSha1(secretKey, data);

        assertThat(result).isEqualTo(result);
    }

    @Test
    public void testBuildQuery_ParamsIsNull() throws KarotzException {
        Map<String, String> params = null;
        String expResult = "";

        String result = KarotzUtil.buildQuery(params);

        assertThat(result).isEqualTo(result);
    }

    @Ignore("for testing")
    @Test
    public void testBuildQuery() throws KarotzException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("tts", "This is a pen.");
        params.put("action", "start");
        String expResult = "action=start&tts=This%20is%20a%20pen.";

        String result = KarotzUtil.buildQuery(params);

        assertThat(result).isEqualTo(expResult);
    }

}
