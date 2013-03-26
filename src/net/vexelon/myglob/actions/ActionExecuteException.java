/*
 * The MIT License
 *
 * Copyright (c) 2012 Petar Petrov
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
package net.vexelon.myglob.actions;

import net.vexelon.myglob.R;

@SuppressWarnings("serial")
public class ActionExecuteException extends Exception {
	
	protected int errorResId;
	protected int errorTitleResId;
	
//	public ActionExecuteException(String message) {
//		super(message);
//	}
	
	public ActionExecuteException(int errorResId, int errorTitleResId) {
		super("");
		this.errorResId = errorResId;
		this.errorTitleResId = errorTitleResId;
	}
	
	public ActionExecuteException(int errorResId, int errorTitleResId, Throwable t) {
		super("", t);
		this.errorResId = errorResId;
		this.errorTitleResId = errorTitleResId;		
	}
	
	public ActionExecuteException(int errorResId) {
		super("");
		this.errorResId = errorResId;
		this.errorTitleResId = R.string.dlg_error_msg_title;
	}
	
	public ActionExecuteException(int errorResId, Throwable t) {
		super("", t);
		this.errorResId = errorResId;
		this.errorTitleResId = R.string.dlg_error_msg_title;		
	}	
	
	public int getErrorResId() {
		return this.errorResId;
	}
	
	public int getErrorTitleResId() {
		return this.errorTitleResId;
	}	
}
