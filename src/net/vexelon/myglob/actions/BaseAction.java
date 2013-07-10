/*
 * The MIT License
 *
 * Copyright (c) 2013 Petar Petrov
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
