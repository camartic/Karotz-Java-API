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
package org.peripheralware.karotz.action.ear;

import java.util.HashMap;
import java.util.Map;

import org.peripheralware.karotz.action.KarotzAction;

/**
 * Ear Action.
 * 
 * <pre>
 * 
 * Move Karotz Ears to a position or continuously
 * 
 *     left (optional): position of left ear, must be an integer
 *     right (optional): position of right ear, must be an integer
 *     relative (optional): move is relative to current position, must be true or false
 *     reset (optional): reset position, must be true or false
 * 
 * </pre>
 * 
 * @author Thomas Wenzlaff
 * @see http://www.wenzlff.de
 * @since 07.07.2012
 */
public class EarAction implements KarotzAction {

	private int left;

	private int right;

	private boolean relative;

	private final boolean reset;

	public EarAction() {
		reset = true;
	}

	public EarAction(int left, int right) {
		this.left = left;
		this.right = right;
		this.relative = true;
		this.reset = false;
	}

	public EarAction(int left, int right, boolean relative, boolean reset) {
		this.left = left;
		this.right = right;
		this.relative = relative;
		this.reset = reset;
	}

	@Override
	public String getURL() {
		return "http://api.karotz.com/api/karotz/ears";
	}

	@Override
	public Map<String, String> getParameters() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("action", "move");
		params.put("left", String.valueOf(left));
		params.put("right", String.valueOf(right));
		params.put("relative", String.valueOf(relative));
		params.put("reset", String.valueOf(reset));
		return params;
	}

}
