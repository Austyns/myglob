/*
 * MyGlob Android Application
 * 
 * Copyright (C) 2012 Petar Petrov
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

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;

import net.vexelon.mobileops.HttpClientException;
import net.vexelon.mobileops.IClient;
import net.vexelon.myglob.Operations;
import net.vexelon.myglob.configuration.Defs;
import net.vexelon.myglob.users.User;
import net.vexelon.myglob.utils.Utils;

public class AccountStatusAction extends BaseAction {
	
	protected Operations _operation;
	
	public AccountStatusAction(Context context, Operations operation, User user) {
		super(context, user);
		_operation = operation;
	}

	@Override
	public ActionResult execute() throws ActionExecuteException {
		
		String tmpResult = "";
		
		ActionResult result = new ActionResult();
		result.setCheckedOn(new Date());
		
		IClient client = newClient();
//		Log.v(Defs.LOG_TAG, "Logging in using " + user.getPhoneNumber() 
//				+ " and pass: " + UsersManager.getInstance().getUserPassword(user));
		clientLogin(client);
		
		try {
			switch(_operation) {
			case CHECK_CURRENT_BALANCE:
				tmpResult = client.getCurrentBalance();
//				tmpResult = tmpResult.replace("<div class=\"row\">", "\n--------------" );
//				tmpResult = Utils.stripHtml(tmpResult);
//				tmpResult = tmpResult.replace("---", "\n---");
				break;
//			case CHECK_AVAIL_MINUTES:
//				tmpResult = client.getAvailableMinutes();
//				tmpResult = Utils.stripHtml(tmpResult);
//				break;
//			case CHECK_CREDIT_LIMIT:
//				tmpResult = client.getCreditLimit();
//				tmpResult = Utils.stripHtml(tmpResult);
//				break;
//			case CHECK_AVAIL_DATA:
//				tmpResult = client.getAvailableInternetBandwidth();
//				tmpResult = Utils.stripHtml(tmpResult);
//				break;
//			case CHECK_AVAIL_TRAVELNSURF:
//				tmpResult = client.getTravelAndSurfBandwidth();
//				tmpResult = Utils.stripHtml(tmpResult);				
//				break;
//			case CHECK_SMS_PACKAGE:
//				tmpResult = client.getAvailableMSPackage();
//				tmpResult = Utils.stripHtml(tmpResult);
//				break;
//			case CHECK_ALL:
//				StringBuilder sb = new StringBuilder(500);
//				sb.append(Utils.stripHtml(client.getCurrentBalance()))
//				.append("<br><br>")
//				.append(Utils.stripHtml(client.getAvailableMinutes()))
//				.append("<br><br>")
//				.append(Utils.stripHtml(client.getCreditLimit()))
//				.append("<br><br>")
//				.append(Utils.stripHtml(client.getAvailableInternetBandwidth()))
//				.append("<br><br>")
//				.append(Utils.stripHtml(client.getTravelAndSurfBandwidth()))				
//				.append("<br><br>")
//				.append(Utils.stripHtml(client.getAvailableMSPackage()));
//				tmpResult = sb.toString();
//				break;
			}

			// colorify important values
			Pattern p = Pattern.compile("(-*\\d+((,|.)\\d+)*\\s*лв\\.*)|(\\d+:\\d+\\s*(ч\\.*|мин\\.*))", Pattern.CASE_INSENSITIVE);
			Matcher m = p.matcher(tmpResult);
			StringBuffer sb = new StringBuffer(tmpResult.length() + tmpResult.length());
			while (m.find()) {
				m.appendReplacement(sb, "<b><font color=\"" + Defs.CLR_TEXT_HIGHLIGHT + "\">" + m.group() + "</font></b>");
				//Log.v(Defs.LOG_TAG, "GR: " + sb.toString());
			}
			m.appendTail(sb);
			
			result.setBytesCount(client.getDownloadedBytesCount());
			result.setResult(sb.toString());
			// update user info
			updateUserResult(result);
			
		} catch (HttpClientException e) {
			throw new ActionExecuteException(e);
		} finally {
			// Make sure we (attempt to) logout.
			clientLogout(client);
		}
		
		return result;
	}
}
