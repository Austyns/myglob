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
package net.vexelon.myglob.users;

import java.util.Date;

import net.vexelon.myglob.configuration.Defs;
import net.vexelon.myglob.utils.DateUtils;
import android.content.SharedPreferences;

public class User {
	
	private String _accountName;
	private AccountType _accountType;
	private String _phoneNumber;
	private String _encodedPassword;
	
	// XXX
	// Not really prefs, these should be moved to SQLite
	private long _checksToday = 0;
	private long _checksTotal = 0;
	private long _trafficToday = 0L;
	private long _trafficTotal = 0L;
	private long _lastCheckDateTime = new Date().getTime();
	private String _lastCheckData = "";
	
	public User() {
		// Empty
	}
	
	public User load(int id, SharedPreferences prefs) {
		_accountName = prefs.getString(Defs.PREFS_USER_NAME + id, "");
		_phoneNumber = prefs.getString(Defs.PREFS_USER_PHONENUMBER + id, "");
		_encodedPassword = prefs.getString(Defs.PREFS_USER_PASSWORD + id, "");
		// 20.03
		_checksToday = prefs.getLong(Defs.PREFS_USER_CHECKSTODAY + id, 0);
		_checksTotal = prefs.getLong(Defs.PREFS_USER_CHECKSTOTAL + id, 0);
		_trafficToday = prefs.getLong(Defs.PREFS_USER_TRAFFICTODAY + id, 0);
		_trafficTotal = prefs.getLong(Defs.PREFS_USER_TRAFFICTOTAL + id, 0);
		_lastCheckDateTime = prefs.getLong(Defs.PREFS_USER_LASTCHECKDATETIME + id, new Date().getTime());
		_lastCheckData = prefs.getString(Defs.PREFS_USER_LASTCHECKDATA + id, "");
		return this;
	}
	
	public User save(int id, SharedPreferences.Editor editor) {
		editor.putString(Defs.PREFS_USER_NAME + id, _accountName);
		editor.putString(Defs.PREFS_USER_PHONENUMBER + id, _phoneNumber);
		editor.putString(Defs.PREFS_USER_PASSWORD + id, _encodedPassword);
		// 20.03
		editor.putLong(Defs.PREFS_USER_CHECKSTODAY + id, _checksToday);
		editor.putLong(Defs.PREFS_USER_CHECKSTOTAL + id, _checksTotal);
		editor.putLong(Defs.PREFS_USER_TRAFFICTODAY + id, _trafficToday);
		editor.putLong(Defs.PREFS_USER_TRAFFICTOTAL + id, _trafficTotal);
		editor.putLong(Defs.PREFS_USER_LASTCHECKDATETIME + id, _lastCheckDateTime);
		editor.putString(Defs.PREFS_USER_LASTCHECKDATA + id, _lastCheckData);
		return this;
	}
	
	public String getEncodedPassword() {
		return _encodedPassword;
	}
	
	public User setEncodedPassword(String encodedPassword) {
		_encodedPassword = encodedPassword;
		return this;
	}
	
	public String getPhoneNumber() {
		return _phoneNumber;
	}
	
	public User setPhoneNumber(String phoneNumber) {
		_phoneNumber = phoneNumber;
		return this;
	}
	
	public String getAccountName() {
		return _accountName;
	}
	
	public User setAccountName(String accountName) {
		_accountName = accountName;
		return this;
	}
	
	public AccountType getAccountType() {
		return _accountType;
	}

	public User setAccountType(AccountType accountType) {
		_accountType = accountType;
		return this;
	}	
	
	// --- 20.03 ---
	public void updateChecks(Date when, int amount) {
		setChecksToday(getChecksToday(when) + amount);
		setChecksTotal(getChecksTotal() + amount);
	}
	
	public void updateTraffic(Date when, long amount) {
		setTrafficToday(getTrafficToday(when) + amount);
		setTrafficTotal(getTrafficTotal() + amount);
	}	
	
	public long getChecksToday(Date when) {
		if (DateUtils.equalDates(when, new Date(this._lastCheckDateTime))) {
			return _checksToday;
		}
		// new day
		return 0;
	}

	public void setChecksToday(long checksToday) {
		this._checksToday = checksToday;
	}

	public long getChecksTotal() {
		return _checksTotal;
	}

	public void setChecksTotal(long checksTotal) {
		this._checksTotal = checksTotal;
	}

	public long getTrafficToday(Date when) {
		if (DateUtils.equalDates(when, new Date(this._lastCheckDateTime))) {
			return _trafficToday;
		}
		// new day
		return 0;		
	}

	public void setTrafficToday(long trafficToday) {
		this._trafficToday = trafficToday;
	}

	public long getTrafficTotal() {
		return _trafficTotal;
	}

	public void setTrafficTotal(long trafficTotal) {
		this._trafficTotal = trafficTotal;
	}

	public long getLastCheckDateTime() {
		return _lastCheckDateTime;
	}

	public void setLastCheckDateTime(long lastCheckDateTime) {
		this._lastCheckDateTime = lastCheckDateTime;
	}	
	
	public String getLastCheckData() {
		return this._lastCheckData;
	}
	
	/**
	 * 
	 * @param data
	 * @remark Maximum is 8192 characters (8k)
	 */
	public void setLastCheckData(String data) {
		this._lastCheckData = data;
	}

}
