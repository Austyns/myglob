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
package net.vexelon.myglob.users;

import net.vexelon.myglob.configuration.Defs;
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
	private long _lastCheckDateTime = 0L;
	
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
		_lastCheckDateTime = prefs.getLong(Defs.PREFS_USER_LASTCHECKDATETIME + id, 0);
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
	
	public long getChecksToday() {
		return _checksToday;
	}

	public void setChecksToday(long _checksToday) {
		this._checksToday = _checksToday;
	}

	public long getChecksTotal() {
		return _checksTotal;
	}

	public void setChecksTotal(long _checksTotal) {
		this._checksTotal = _checksTotal;
	}

	public long getTrafficToday() {
		return _trafficToday;
	}

	public void setTrafficToday(long _trafficToday) {
		this._trafficToday = _trafficToday;
	}

	public long getTrafficTotal() {
		return _trafficTotal;
	}

	public void setTrafficTotal(long _trafficTotal) {
		this._trafficTotal = _trafficTotal;
	}

	public long getLastCheckDateTime() {
		return _lastCheckDateTime;
	}

	public void setLastCheckDateTime(long _lastCheckDateTime) {
		this._lastCheckDateTime = _lastCheckDateTime;
	}	

}
