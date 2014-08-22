/*
 * MyGlob Android Application
 * 
 * Copyright (C) 2010 Petar Petrov
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package net.vexelon.myglob.configuration;


import net.vexelon.myglob.crypto.PasswordEngine;
import net.vexelon.myglob.crypto.PasswordEngineImpl1;
import android.content.SharedPreferences;

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
