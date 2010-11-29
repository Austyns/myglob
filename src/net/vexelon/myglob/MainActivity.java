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
package net.vexelon.myglob;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.vexelon.glbclient.GLBClient;
import net.vexelon.glbclient.GLBHttpClientImpl;
import net.vexelon.glbclient.exceptions.GLBSecureCodeRequiredException;
import net.vexelon.glbclient.exceptions.GLBInvalidCredentialsException;
import net.vexelon.myglob.R;
import net.vexelon.myglob.configuration.Defs;
import net.vexelon.myglob.configuration.Settings;
import net.vexelon.myglob.users.UsersManager;
import net.vexelon.myglob.utils.Utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	/* Milestones TODO list
	 * 
	 * Milestone 02
	 * 1. Add spinner for user accounts
	 * 2. Add user account class and move methods for handling account info
	 * 3. Refactor LoginActivity - NewAccountActivity 
	 * 4. Add AccountManagement menu button for general account management (Spinner of accounts + sepearet Activity for options)
	 * 5. Refactor MainActivity class with proper actions for updating account info
	 * 6. Solve problem with security codes on GLB site 
	 * 
	 * Milestone 01
	 * 1. [DONE] Complete Spinner actions
	 * 2. [DONE] Test secure code image occurrence
	 * 3. [DONE] Test saving/loading of options
	 * 4. [DONE] Add/Finish About activity
	 * 5. [DONE] Add Progress dialog(s)
	 * 6. [DONE] Add strings to resources
	 * 7. [DONE] Test error message screens
	 * 8. [DONE] Add images and fix/adjust layout
	 * 9. [DONE] What to do if key-create fails ??
	 * 10. Protect IV
	 * 11. [DONE] Remove log tags
	 */
	
	public enum Operations {
		CHECK_CURRENT_BALANCE(R.string.operation_check_balance),
		CHECK_AVAIL_MINUTES(R.string.operation_check_avail_minutes),
		CHECK_AVAIL_DATA(R.string.operation_check_avail_data),
		CHECK_SMS_PACKAGE(R.string.operation_check_sms_pack),
		CHECK_CREDIT_LIMIT(R.string.operation_check_credit_limit),
		CHECK_ALL(R.string.operation_check_all);
		
		private int resId = -1;
		
		Operations(int resourceId) {
			this.resId = resourceId;
		}
		
		public String getName(Context context) {
			return context.getString(this.resId);
		}
	};
	
	private Activity _activity = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        _activity = this;
        
        /**
         * load preferences
         */
        SharedPreferences prefs = this.getSharedPreferences(Defs.PREFS_USER_PREFS, 0);
        UsersManager.getInstance().reloadUsers(prefs);
        
        SharedPreferences prefsGeneral = this.getSharedPreferences(Defs.PREFS_ALL_PREFS, 0);
        Settings.getInstance().init(prefsGeneral);

        /**
         * initialize UI
         */
        
        this.setTitle(getResString(R.string.app_name) + " - " + getResString(R.string.about_tagline));

        // init options spinner
        Spinner spinnerOptions = (Spinner) findViewById(R.id.SpinnerOptions);
        OperationsArrayAdapter adapter = new OperationsArrayAdapter(this, android.R.layout.simple_spinner_item, 
        		new Operations[]{
        		Operations.CHECK_CURRENT_BALANCE, 
				Operations.CHECK_AVAIL_MINUTES,
				Operations.CHECK_AVAIL_DATA,
				Operations.CHECK_SMS_PACKAGE,
				Operations.CHECK_CREDIT_LIMIT,
				Operations.CHECK_ALL,
        });
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOptions.setAdapter(adapter);
        
        // init accounts spinner
        Spinner spinnerAccounts = (Spinner) findViewById(R.id.SpinnerUserAccounts);
        //TODO: add accounts to spinner
        
        // create update button
        Button btnUpdate = (Button) findViewById(R.id.ButtonUpdate);
        btnUpdate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				updateSelectedStatus();
			}
		});
        
        //btnUpdate.getBackground().setColorFilter(0x2212FF00, Mode.LIGHTEN);
        btnUpdate.getBackground().setColorFilter(Defs.CLR_BUTTON_UPDATE, Mode.MULTIPLY);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if ( requestCode == Defs.INTENT_SIGNIN_RQ) {
//    		if (resultCode == RESULT_OK) {
//    			saveSettings(data.getExtras());
//    			
//    			// Update button was clicked
//    			if ( _updateAfterSignIn ) {
//    				updateSelectedStatus();
//    				_updateAfterSignIn = false;
//    			}
//    		}
    	}
    }
    
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		return initMenu(menu);
//	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return initMenu(menu);
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		
		Intent intent = null;
		
		switch(item.getItemId()) {
//		case Defs.MENU_SIGNIN:
//			showSignInWindow();
//			break;
//			
//		case Defs.MENU_SIGNOUT:
//			clearSettings();
//			break;
			
		case Defs.MENU_ABOUT:
			intent = new Intent(this, AboutActivity.class);
			startActivity(intent);
			break;
		}
		
		return true;
	}
	
	private boolean initMenu(Menu menu) {
		menu.clear();
		
//		if (!isCredentialsAvailable())
//			menu.add(0, Defs.MENU_SIGNIN, 0, getResString(R.string.menu_signin) ).setIcon(R.drawable.key);
//		else
//			menu.add(0, Defs.MENU_SIGNOUT, 0, getResString(R.string.menu_signout)).setIcon(R.drawable.door_out);
		
		menu.add(1, Defs.MENU_ADD_ACCOUNT, 0, "Add account");
		menu.add(1, Defs.MENU_MANAGE_ACCOUNTS, 0, "Manage accounts");
		menu.add(1, Defs.MENU_ABOUT, 15, getResString(R.string.menu_about)).setIcon(R.drawable.help);
		
		return true;
	}
	
	/**
	 * Get selected spinner option and update view
	 */
	private void updateSelectedStatus() {
		
		Spinner spinnerAccounts = (Spinner) findViewById(R.id.SpinnerUserAccounts);
		
		if (UsersManager.getInstance().size() > 0) {
			
			Spinner spinnerOptions = (Spinner) findViewById(R.id.SpinnerOptions);
			final Operations operation = (Operations) spinnerOptions.getSelectedItem();
			final TextView tx = (TextView) _activity.findViewById(R.id.TextContent);
			
			// show progress
			final ProgressDialog myProgress = ProgressDialog.show(this, getResString(R.string.dlg_progress_title), getResString(R.string.dlg_progress_message), true);

			// do work
			new Thread() {
				public void run() {
					
					try {
						final String data = getAccountStatus(operation);
//						saveLastResult(data); // keep in storage

						// update text field
						_activity.runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								//tx.setText(data);
								tx.setText(Html.fromHtml(data));
								//WebView wv = (WebView) _activity.findViewById(R.id.TextContent);
								//wv.loadData(data, "text/html", "utf-8");							
							}
						});						
					}
					catch (GLBInvalidCredentialsException e) {
						// Show error dialog
						Utils.showAlertDialog(_activity, R.string.dlg_error_msg_invalid_credentials, R.string.dlg_error_msg_title);
					}
					catch (GLBSecureCodeRequiredException e) {
						// Show error dialog						
						Utils.showAlertDialog(_activity, R.string.dlg_error_msg_securecode, R.string.dlg_error_msg_title);
					}
					catch (Exception e) {
						// Show error dialog						
						final String msg = e.getMessage();
						Utils.showAlertDialog(_activity, msg, getResString(R.string.dlg_error_msg_title));
					}
					
					// close progress bar dialog
					_activity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							myProgress.dismiss();
						}
					});						
				};
			}.start();
		}
		else  {
			
			// show add user screen


		} // end if		
	}
	
	private String getAccountStatus(Operations operation) throws Exception {
		String result = "<td class=\"txt_order_SMS\">" +
        		 "<p>Вашата текуща сметка:<span style=\"color: rgb(221, 0, 57); font-weight: bold;\"> 1,45 лв.</span> без ДДС</p>" +
                 "<p>Задължения по ф-ра за периода 18.08-17.09.2010г:<span style=\"color: rgb(221, 0, 57); font-weight: bold;\"> -0,23 лв.</span> с ДДС</p>" +
                 "<p>Данните са актуални към:<span style=\"font-weight: bold;\"> 07 Октомври, 21:15ч.</span>" +
                 "</p></td>" +
                 "<td class=\"txt_order_SMS\">" +
        		 "<p>Вашата текуща сметка:<span style=\"color: rgb(221, 0, 57); font-weight: bold;\"> 1,45 лв.</span> без ДДС</p>" +
                 "<p>Задължения по ф-ра за периода 18.08-17.09.2010г:<span style=\"color: rgb(221, 0, 57); font-weight: bold;\"> -0,23 лв.</span> с ДДС</p>" +
                 "<p>Данните са актуални към:<span style=\"font-weight: bold;\"> 07 Октомври, 21:15ч.</span>" +
                 "</p></td>" +
                 "<td class=\"txt_order_SMS\">" +
        		 "<p>Вашата текуща сметка:<span style=\"color: rgb(221, 0, 57); font-weight: bold;\"> 1,45лв.</span> без ДДС</p>" +
                 "<p>Задължения по ф-ра за периода 18.08-17.09.2010г:<span style=\"color: rgb(221, 0, 57); font-weight: bold;\"> -0,23 лв.</span> с ДДС</p>" +
                 "<p>Данните са актуални към:<span style=\"font-weight: bold;\"> 07 Октомври, 21:15ч.</span>" +
                 "</p></td>";  
		
		result = result.replaceAll("(<.[^>]*>)|(</.[^>]*>)", "");
		result = result.replaceAll("\\t|\\n|\\r", "");	
		result = result.trim();
		
		Pattern p = Pattern.compile("(-*\\d+,\\d+\\s*лв\\.*)", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(result);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, "<b><font color=\"#1FAF1F\">" + m.group() + "</font></b>");
			//Log.v(Defs.LOG_TAG, "GR: " + sb.toString());
		}
		m.appendTail(sb);
		//Log.v(Defs.LOG_TAG, "GR: " + sb.toString());
		
		return sb.toString();
	}
    
//	private String getAccountStatus(Operations operation) throws Exception {
//    
//		String result = "";
//		GLBClient client = new GLBHttpClientImpl(_username, getDecryptedPassword(_password));
////		Log.v(Defs.LOG_TAG, "Logging in using " + _username + " and pass: " + getDecryptedPassword(_password));
//		
//		try {
//			client.login();
//			
//			switch(operation) {
//			case CHECK_CURRENT_BALANCE:
//				result = client.getCurrentBalance();
//				result = Utils.stripHtml(result);
//				break;
//			case CHECK_AVAIL_MINUTES:
//				result = client.getAvailableMinutes();
//				result = Utils.stripHtml(result);
//				break;
//			case CHECK_CREDIT_LIMIT:
//				result = client.getCreditLimit();
//				result = Utils.stripHtml(result);
//				break;
//			case CHECK_AVAIL_DATA:
//				result = client.getAvailableInternetBandwidth();
//				result = Utils.stripHtml(result);
//				break;
//			case CHECK_SMS_PACKAGE:
//				result = client.getAvailableMSPackage();
//				result = Utils.stripHtml(result);
//				break;
//			case CHECK_ALL:
//				StringBuffer sb = new StringBuffer(500);
//				sb.append(Utils.stripHtml(client.getCurrentBalance()));
//				sb.append("<br><br>");
//				sb.append(Utils.stripHtml(client.getAvailableMinutes()));
//				sb.append("<br><br>");
//				sb.append(Utils.stripHtml(client.getCreditLimit()));
//				sb.append("<br><br>");
//				sb.append(Utils.stripHtml(client.getAvailableInternetBandwidth()));
//				sb.append("<br><br>");
//				sb.append(Utils.stripHtml(client.getAvailableMSPackage()));
//				result = sb.toString();
//				break;
//			}
//
//			// colorfy money values
//			Pattern p = Pattern.compile("(-*\\d+(,\\d+)*\\s*лв\\.*)|(\\d+:\\d+\\s*(ч\\.*|мин\\.*))", Pattern.CASE_INSENSITIVE);
//			Matcher m = p.matcher(result);
//			StringBuffer sb = new StringBuffer(result.length() + result.length());
//			while (m.find()) {
//				m.appendReplacement(sb, "<b><font color=\"" + Defs.CLR_TEXT_HIGHLIGHT + "\">" + m.group() + "</font></b>");
//				//Log.v(Defs.LOG_TAG, "GR: " + sb.toString());
//			}
//			m.appendTail(sb);	
//			result = sb.toString();
//			
//			client.logout();
//		}
//		catch(Exception e) {
//			//Log.e(Defs.LOG_TAG, "Login exception!", e);
//			throw e;
//		}
//		finally {
//			client.close();
//		}  		
//		
//		return result;
//	}    
	
	private String getResString(int id) {
		return this.getResources().getString(id);
	}	
}