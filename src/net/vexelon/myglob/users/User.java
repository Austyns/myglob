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
	
	public User() {
	}
	
	public User load(int id, SharedPreferences prefs) {
		_accountName = prefs.getString(Defs.PREFS_USER_NAME + id, "");
		_phoneNumber = prefs.getString(Defs.PREFS_USER_PHONENUMBER + id, "");
		_encodedPassword = prefs.getString(Defs.PREFS_USER_PASSWORD + id, "");
		return this;
	}
	
	public User save(int id, SharedPreferences.Editor editor) {
		editor.putString(Defs.PREFS_USER_NAME + id, _accountName);
		editor.putString(Defs.PREFS_USER_PHONENUMBER + id, _phoneNumber);
		editor.putString(Defs.PREFS_USER_PASSWORD + id, _encodedPassword);
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

}
