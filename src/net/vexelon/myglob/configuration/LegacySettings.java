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

import java.io.IOException;

import net.vexelon.myglob.crypto.PasswordEngine;
import net.vexelon.myglob.crypto.PasswordEngineImpl1;
import net.vexelon.myglob.utils.Base64;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;

/**
 *  
 * @author petarov
 *
 * The purpose of this class is to load previously saved (1.1.0) users and convert them
 * to the new User class structure. This is a one time only operation, after which the previous
 * saved user is deleted.
 */
public class LegacySettings {
	
	private SharedPreferences _prefs = null;
	
	public void init(SharedPreferences prefs) {
		_prefs = prefs;
	}
	
	public String getPhoneNumber() {
		return _prefs.getString(Defs.LEGACY_INTENT_EXTRA_USERNAME, null);
	}
	
	public String getPassword() {
		String encodedPassword = _prefs.getString(Defs.LEGACY_INTENT_EXTRA_PASSWORD, "");
		String encodedKey = _prefs.getString(Defs.LEGACY_INTENT_EXTRA_KEY, "");
		String rawPassword = null;

		try {
			PasswordEngine passwordEngine = PasswordEngineImpl1.getInstance(encodedKey);
			rawPassword = passwordEngine.decodeAndDecrypt(encodedPassword);
		}
		catch (Exception e) {
//			Log.e(Defs.LOG_TAG, "Failed to load key!");
		}
		
		return rawPassword;
	}
	
	public void clear() {
		SharedPreferences.Editor editor = _prefs.edit();
		editor.remove(Defs.LEGACY_INTENT_EXTRA_USERNAME);
		editor.remove(Defs.LEGACY_INTENT_EXTRA_PASSWORD);
		editor.commit();
	}
	
}
