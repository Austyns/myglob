/*
 * The MIT License
 * 
 * Copyright (c) 2010 Petar Petrov
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
package net.vexelon.myglob.configuration;

import net.vexelon.myglob.Operations;
import android.content.SharedPreferences;

public class GlobalSettings {
	
	private static GlobalSettings _INSTANCE = null;
	
	public static GlobalSettings getInstance() {
		if (_INSTANCE == null)
			_INSTANCE = new GlobalSettings();
		
		return _INSTANCE;
	}
	
	private SharedPreferences _prefs = null;
	
	public GlobalSettings() {
	}
	
	public void init(SharedPreferences prefs) {
		_prefs = prefs;
	}
	
	public String getLastSelectedAccount() {
		return _prefs.getString(Defs.PREFS_LAST_SELECTED_ACCOUNT, "");
	}
	
	public void putLastSelectedAccount(String value) {
		SharedPreferences.Editor editor = _prefs.edit();
		editor.putString(Defs.PREFS_LAST_SELECTED_ACCOUNT, value);
		editor.commit();
	}
	
	/**
	 * 
	 * @return Last saved operation. Default is <code>CHECK_CURRENT_BALANCE</code>
	 */
	public Operations getLastSelectedOperation() {
		return Operations.valueOf(
				_prefs.getString(Defs.PREFS_LAST_SELECTED_OPERATION, Operations.CHECK_CURRENT_BALANCE.name()));
	}
	
	public void putLastSelectedOperation(Operations operation) {
		SharedPreferences.Editor editor = _prefs.edit();
		editor.putString(Defs.PREFS_LAST_SELECTED_OPERATION, operation.name());
		editor.commit();
	}
	
}
