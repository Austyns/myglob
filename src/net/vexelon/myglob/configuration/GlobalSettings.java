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

import net.vexelon.myglob.Operations;
import android.content.SharedPreferences;

public class GlobalSettings {
	
	public static final String NO_ACCOUNT = "";
	public static final String NO_INFO = "";
	
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
	
	public String getLastSelectedPhoneNumber() {
		return _prefs.getString(Defs.PREFS_LAST_SELECTED_ACCOUNT, NO_ACCOUNT);
	}
	
	public void setLastSelectedPhoneNumber(String value) {
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
	
	public void setLastSelectedOperation(Operations operation) {
		SharedPreferences.Editor editor = _prefs.edit();
		editor.putString(Defs.PREFS_LAST_SELECTED_OPERATION, operation.name());
		editor.commit();
	}
	
}
