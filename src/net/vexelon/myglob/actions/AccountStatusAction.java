/*
 * The MIT License
 *
 * Copyright (c) 2012 Petar Petrov
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

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.SharedPreferences;

import net.vexelon.mobileops.HttpClientException;
import net.vexelon.mobileops.IClient;
import net.vexelon.mobileops.GLBClient;
import net.vexelon.mobileops.InvalidCredentialsException;
import net.vexelon.mobileops.SecureCodeRequiredException;
import net.vexelon.myglob.Operations;
import net.vexelon.myglob.R;
import net.vexelon.myglob.configuration.Defs;
import net.vexelon.myglob.users.User;
import net.vexelon.myglob.users.UsersManager;
import net.vexelon.myglob.utils.Utils;

public class AccountStatusAction implements Action {
	
	protected Context _context;
	protected Operations _operation;
	protected User _user;
	
	public AccountStatusAction(Context context, Operations operation, User user) {
		_context = context;
		_operation = operation;
		_user = user;
	}

	@Override
	public ActionResult execute() throws ActionExecuteException {
		
		ActionResult result = new ActionResult();
		String tmpResult = "";
		IClient client;
		
		try {
			client = new GLBClient(_user.getPhoneNumber(), UsersManager.getInstance().getUserPassword(_user));
		} catch (Exception e) {
			throw new ActionExecuteException(R.string.dlg_error_msg_decrypt_failed, e);
		}
		
//		Log.v(Defs.LOG_TAG, "Logging in using " + user.getPhoneNumber() 
//				+ " and pass: " + UsersManager.getInstance().getUserPassword(user));
		
		// start checking Now
		result.setCheckedOn(new Date());
		
		try {
			client.login();

			switch(_operation) {
			case CHECK_CURRENT_BALANCE:
				tmpResult = client.getCurrentBalance();
				tmpResult = Utils.stripHtml(tmpResult);
				break;
			case CHECK_AVAIL_MINUTES:
				tmpResult = client.getAvailableMinutes();
				tmpResult = Utils.stripHtml(tmpResult);
				break;
			case CHECK_CREDIT_LIMIT:
				tmpResult = client.getCreditLimit();
				tmpResult = Utils.stripHtml(tmpResult);
				break;
			case CHECK_AVAIL_DATA:
				tmpResult = client.getAvailableInternetBandwidth();
				tmpResult = Utils.stripHtml(tmpResult);
				break;
			case CHECK_SMS_PACKAGE:
				tmpResult = client.getAvailableMSPackage();
				tmpResult = Utils.stripHtml(tmpResult);
				break;
			case CHECK_ALL:
				StringBuilder sb = new StringBuilder(500);
				sb.append(Utils.stripHtml(client.getCurrentBalance()))
				.append("<br><br>")
				.append(Utils.stripHtml(client.getAvailableMinutes()))
				.append("<br><br>")
				.append(Utils.stripHtml(client.getCreditLimit()))
				.append("<br><br>")
				.append(Utils.stripHtml(client.getAvailableInternetBandwidth()))
				.append("<br><br>")
				.append(Utils.stripHtml(client.getAvailableMSPackage()));
				tmpResult = sb.toString();
				break;
			}

			// colorfy important values
			Pattern p = Pattern.compile("(-*\\d+(,\\d+)*\\s*лв\\.*)|(\\d+:\\d+\\s*(ч\\.*|мин\\.*))", Pattern.CASE_INSENSITIVE);
			Matcher m = p.matcher(tmpResult);
			StringBuffer sb = new StringBuffer(tmpResult.length() + tmpResult.length());
			while (m.find()) {
				m.appendReplacement(sb, "<b><font color=\"" + Defs.CLR_TEXT_HIGHLIGHT + "\">" + m.group() + "</font></b>");
				//Log.v(Defs.LOG_TAG, "GR: " + sb.toString());
			}
			m.appendTail(sb);
			
			result.setBytesCount(client.getDownloadedBytesCount());
			result.setResult(sb.toString());

			client.logout();
			
			// update user info
			UsersManager.getInstance().setUserResult(_user, result);
			SharedPreferences prefs = _context.getSharedPreferences(Defs.PREFS_USER_PREFS, 0);
			UsersManager.getInstance().save(prefs);			
			
		} catch (InvalidCredentialsException e) {
			throw new ActionExecuteException(R.string.dlg_error_msg_invalid_credentials, 
					R.string.dlg_error_msg_title);
		} catch (SecureCodeRequiredException e) {
			throw new ActionExecuteException(R.string.dlg_error_msg_securecode, 
					R.string.dlg_error_msg_title);			
		} catch(HttpClientException e) {
			throw new ActionExecuteException(e);	
		} finally {
			if (client != null)
				client.close();
		}
		
		return result;
	}
	
//	private String getAccountStatus(Operations operation, User user) throws Exception {
//	String result = "<td class=\"txt_order_SMS\">" +
//    		 "<p>Вашата текуща сметка:<span style=\"color: rgb(221, 0, 57); font-weight: bold;\"> 1,45 лв.</span> без ДДС</p>" +
//             "<p>Задължения по ф-ра за периода 18.08-17.09.2010г:<span style=\"color: rgb(221, 0, 57); font-weight: bold;\"> -0,23 лв.</span> с ДДС</p>" +
//             "<p>Данните са актуални към:<span style=\"font-weight: bold;\"> 07 Октомври, 21:15ч.</span>" +
//             "</p></td>" +
//             "<td class=\"txt_order_SMS\">" +
//    		 "<p>Вашата текуща сметка:<span style=\"color: rgb(221, 0, 57); font-weight: bold;\"> 1,45 лв.</span> без ДДС</p>" +
//             "<p>Задължения по ф-ра за периода 18.08-17.09.2010г:<span style=\"color: rgb(221, 0, 57); font-weight: bold;\"> -0,23 лв.</span> с ДДС</p>" +
//             "<p>Данните са актуални към:<span style=\"font-weight: bold;\"> 07 Октомври, 21:15ч.</span>" +
//             "</p></td>" +
//             "<td class=\"txt_order_SMS\">" +
//    		 "<p>Вашата текуща сметка:<span style=\"color: rgb(221, 0, 57); font-weight: bold;\"> 1,45лв.</span> без ДДС</p>" +
//             "<p>Задължения по ф-ра за периода 18.08-17.09.2010г:<span style=\"color: rgb(221, 0, 57); font-weight: bold;\"> -0,23 лв.</span> с ДДС</p>" +
//             "<p>Данните са актуални към:<span style=\"font-weight: bold;\"> 07 Октомври, 21:15ч.</span>" +
//             "</p></td>" +
//             "<td class=\"txt_order_SMS\">" +
//    		 "<p>Вашата текуща сметка:<span style=\"color: rgb(221, 0, 57); font-weight: bold;\"> 1,45 лв.</span> без ДДС</p>" +
//             "<p>Задължения по ф-ра за периода 18.08-17.09.2010г:<span style=\"color: rgb(221, 0, 57); font-weight: bold;\"> -0,23 лв.</span> с ДДС</p>" +
//             "<p>Данните са актуални към:<span style=\"font-weight: bold;\"> 07 Октомври, 21:15ч.</span>" +
//             "</p></td>" +
//             "<td class=\"txt_order_SMS\">" +
//    		 "<p>Вашата текуща сметка:<span style=\"color: rgb(221, 0, 57); font-weight: bold;\"> 1,45 лв.</span> без ДДС</p>" +
//             "<p>Задължения по ф-ра за периода 18.08-17.09.2010г:<span style=\"color: rgb(221, 0, 57); font-weight: bold;\"> -0,23 лв.</span> с ДДС</p>" +
//             "<p>Данните са актуални към:<span style=\"font-weight: bold;\"> 07 Октомври, 21:15ч.</span>" +
//             "</p></td>" +
//             "<td class=\"txt_order_SMS\">" +
//    		 "<p>Вашата текуща сметка:<span style=\"color: rgb(221, 0, 57); font-weight: bold;\"> 1,45лв.</span> без ДДС</p>" +
//             "<p>Задължения по ф-ра за периода 18.08-17.09.2010г:<span style=\"color: rgb(221, 0, 57); font-weight: bold;\"> -0,23 лв.</span> с ДДС</p>" +
//             "<p>Данните са актуални към:<span style=\"font-weight: bold;\"> 07 Октомври, 21:15ч.</span>" +
//             "</p></td>";
//
//	result = result.replaceAll("(<.[^>]*>)|(</.[^>]*>)", "");
//	result = result.replaceAll("\\t|\\n|\\r", "");
//	result = result.trim();
//
//	Pattern p = Pattern.compile("(-*\\d+,\\d+\\s*лв\\.*)", Pattern.CASE_INSENSITIVE);
//	Matcher m = p.matcher(result);
//	StringBuffer sb = new StringBuffer();
//	while (m.find()) {
//		m.appendReplacement(sb, "<b><font color=\"#1FAF1F\">" + m.group() + "</font></b>");
//		//Log.v(Defs.LOG_TAG, "GR: " + sb.toString());
//	}
//	m.appendTail(sb);
//	//Log.v(Defs.LOG_TAG, "GR: " + sb.toString());
//
//	return sb.toString();
//}	
}
