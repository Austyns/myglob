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

import java.util.ArrayList;

import net.vexelon.myglob.actions.ActionResult;
import net.vexelon.myglob.configuration.Defs;
import net.vexelon.myglob.crypto.PasswordEngine;
import net.vexelon.myglob.crypto.PasswordEngineImpl2;

import android.content.SharedPreferences;
import android.text.TextUtils;

public class UsersManager {
	
	private static UsersManager _INSTANCE = null;
	
	public static UsersManager getInstance() {
		if (_INSTANCE == null)
			_INSTANCE = new UsersManager();
		
		return _INSTANCE;
	}
	
	private ArrayList<User> _users = new ArrayList<User>();
	private PasswordEngine _passwordEngine = PasswordEngineImpl2.getInstance();

	protected UsersManager() {
		// empty
	}
	
	public int size() {
		return _users.size();
	}
	
	public void reloadUsers(SharedPreferences prefs) {
		// clear previous list
		if (_users != null) {
			_users.clear();
			//_users = null;
		}

		// read num of users and load in list
		int totalUsers = prefs.getInt(Defs.PREFS_USER_COUNT, 0);
		for(int i = 0; i < totalUsers; i++) {
			addUser(new User().load(i, prefs));
		}
	}
	
	public void save(SharedPreferences prefs) {
		SharedPreferences.Editor editor = prefs.edit();
		editor.clear(); // clean all previously saved user data
		editor.putInt(Defs.PREFS_USER_COUNT, _users.size());
		for(int i = 0; i < _users.size(); i++) {
			_users.get(i).save(i, editor);
		}
		editor.commit();
	}
	
	public User createUser(String accountName, String phoneNumber, AccountType accountType) {
		User user = new User();
		user.setAccountName(accountName);
		user.setPhoneNumber(phoneNumber);
		user.setAccountType(accountType);
		return user;
	}
	
	public void addUser(User user) {
		_users.add(user);
	}
	
	public boolean isUserExists(String phoneNumber) {
		return getUserByPhoneNumber(phoneNumber) != null;
	}
	
	/**
	 * 
	 * @param phoneNumber
	 * @return {@link User} object or <code>null</code>
	 */
	public User getUserByPhoneNumber(String phoneNumber) {
		for (User user : _users) {
			if (user.getPhoneNumber().equals(phoneNumber)) {
				return user;
			}
		}
		
		return null;
	}
	
	public String[] getUsersPhoneNumbersList() {
		String items[] = null;
		
		if (_users.size() > 0) {
			items = new String[_users.size()];
			for(int i = 0; i < _users.size(); i++)
				items[i] = _users.get(i).getPhoneNumber();
		}
		
		return items;
	}
	
	public User removeUser(String phoneNumber) {
		User user = getUserByPhoneNumber(phoneNumber);
		_users.remove(user);
		return user;
	}
	
	public int getUsersCount() {
		return _users.size();
	}
	
	/**
	 * 
	 * @param user
	 * @return Password string or <code>null</code>
	 * @throws Exception
	 */
	public String getUserPassword(User user) throws Exception {
		if (!TextUtils.isEmpty(user.getEncodedPassword())) {
			return _passwordEngine.decodeAndDecrypt(user.getEncodedPassword());
		}

		return null;
	}
	
	public void setUserPassword(User user, String rawPassword) throws Exception {
		user.setEncodedPassword(_passwordEngine.encryptAndEncode(rawPassword));
	}
	
	/**
	 * 
	 * @param user
	 * @param result
	 */
	public void setUserResult(User user, ActionResult result) {
		user.updateChecks(result.getCheckedOn(), 1);
		user.updateTraffic(result.getCheckedOn(), result.getBytesCount());
		user.setLastCheckDateTime(result.getCheckedOn().getTime());
		
		if (result.getString() != null && result.getString().length() > 0)
			user.setLastCheckData(result.getString());
	}
	
//	private byte[] loadKey() throws Exception {
//		
//		byte[] ret = null;
////		String savedKey = prefs.getString(Defs.PREFS_KEY_TEMPLATE + _id, "");
////		
////		if (TextUtils.isEmpty(savedKey)) {
////			// generate new key
////			Crypto crypto = CryptAESImpl.getInstance();
////			try {
////				// line
////				ret = crypto.createSecretKey();
////			} catch (Exception e) {
////				// Log.e(Defs.LOG_TAG, "Key could not be created!");
////				// AlertDialog alert = Utils.createAlertDialog(this,
////				// R.string.dlg_error_msg_create_key_failed,
////				// R.string.dlg_error_msg_title);
////				// alert.show();
////			}
////		}
////		else {
////			ret = Base64.decode(savedKey);
////		}
//		
//		return ret;
//	}		

}
