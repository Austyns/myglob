/*
 * MyGlob Android Application
 * 
 * Copyright (C) 2013 Petar Petrov
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
package net.vexelon.myglob.actions;

import net.vexelon.mobileops.GLBClient;
import net.vexelon.mobileops.HttpClientException;
import net.vexelon.mobileops.IClient;
import net.vexelon.mobileops.InvalidCredentialsException;
import net.vexelon.mobileops.SecureCodeRequiredException;
import net.vexelon.myglob.R;
import net.vexelon.myglob.configuration.Defs;
import net.vexelon.myglob.users.User;
import net.vexelon.myglob.users.UsersManager;
import android.content.Context;
import android.content.SharedPreferences;

public abstract class BaseAction implements Action {
	
	protected Context _context;
	protected User _user;
	
	public BaseAction(Context context, User user) {
		this._context = context;
		this._user = user;
	}
	
	protected IClient newClient() throws ActionExecuteException {
		IClient client;
		try {
			client = new GLBClient(_user.getPhoneNumber(), UsersManager.getInstance().getUserPassword(_user));
		} catch (Exception e) {
			throw new ActionExecuteException(R.string.dlg_error_msg_decrypt_failed, e);
		}
		
		return client;
	}
	
	protected void clientLogin(IClient client) throws ActionExecuteException {
		try {
			client.login();
		} catch (InvalidCredentialsException e) {
			throw new ActionExecuteException(R.string.dlg_error_msg_invalid_credentials, 
					R.string.dlg_error_msg_title);
		} catch (SecureCodeRequiredException e) {
			throw new ActionExecuteException(R.string.dlg_error_msg_securecode, 
					R.string.dlg_error_msg_title);			
		} catch (HttpClientException e) {
			throw new ActionExecuteException(e);	
		}
	}
	
	protected void clientLogout(IClient client) throws ActionExecuteException {
		try {
			client.logout();
		} catch (HttpClientException e) {
			throw new ActionExecuteException(e);	
		} finally {
			if (client != null)
				client.close();
		}		
	}
	
	protected void updateUserResult(ActionResult result) {
		// update user info
		UsersManager.getInstance().setUserResult(_user, result);
		SharedPreferences prefs = _context.getSharedPreferences(Defs.PREFS_USER_PREFS, 0);
		UsersManager.getInstance().save(prefs);			
	}
	
}
