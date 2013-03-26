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
package net.vexelon.myglob;

import net.vexelon.myglob.actions.AccountStatusAction;
import net.vexelon.myglob.actions.Action;
import net.vexelon.myglob.actions.ActionExecuteException;
import net.vexelon.myglob.actions.ActionResult;
import net.vexelon.myglob.configuration.Defs;
import net.vexelon.myglob.configuration.GlobalSettings;
import net.vexelon.myglob.users.User;
import net.vexelon.myglob.users.UsersManager;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.IBinder;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

public class UpdateWidgetService extends Service {
	
	private static String URI_SCHEME = "MYGB";
	
	private String getLastAccountStatus() {
		
        final SharedPreferences prefsUsers = this.getSharedPreferences(Defs.PREFS_USER_PREFS, 0);
        UsersManager.getInstance().reloadUsers(prefsUsers);

        SharedPreferences prefsGeneral = this.getSharedPreferences(Defs.PREFS_ALL_PREFS, 0);
        GlobalSettings.getInstance().init(prefsGeneral);
        
        String account = GlobalSettings.getInstance().getLastSelectedPhoneNumber();
        if (Defs.LOG_ENABLED) {
        	Log.d(Defs.LOG_TAG, "Last acccount loaded = " + account);
        }
        
        String resultData = GlobalSettings.NO_INFO;
        
    	if (account == GlobalSettings.NO_ACCOUNT) {
    		resultData = getResString(R.string.text_account_no_account);
    		
    	} else if (UsersManager.getInstance().isUserExists(account)) {
    		
    		User user = UsersManager.getInstance().getUserByPhoneNumber(account);
            Action action = new AccountStatusAction(
            		this,
            		GlobalSettings.getInstance().getLastSelectedOperation(),
            		user);
            
            // get last known good info
            resultData = user.getLastCheckData();
        	
            try {
				ActionResult actionResult = action.execute();
				resultData = actionResult.getString();
				
            } catch (ActionExecuteException e) {
            	Log.e(Defs.LOG_TAG, "Error updating widget status!", e);
            	
            	// only show error if no user info is available
            	if (resultData.length() == 0) {
	            	if (e.isErrorResIdAvailable()) {
	            		resultData = getString(e.getErrorResId());
	            	} else {
	            		resultData = getResString(R.string.dlg_error_msg_title);
	//            		resultData = e.getMessage();
	            	}
            	}
            }                
            
    	} else {
    		resultData = getResString(R.string.text_account_invalid);
    	}
    	
//        resultData = account + "<br>" + resultData;
        
        return resultData;
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		
		Context context = this.getApplicationContext();
		
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
		int[] widgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
		if (widgetIds == null) {
			Log.w(Defs.LOG_TAG, "No widgets found to update!");
			return;
		}
		
		if (Defs.LOG_ENABLED) {
			ComponentName thisWidget = new ComponentName(context, WidgetProvider.class);
			int[] allWidgetIds2 = appWidgetManager.getAppWidgetIds(thisWidget);			
			Log.d(Defs.LOG_TAG, "From intent: " + String.valueOf(widgetIds.length));
			Log.d(Defs.LOG_TAG, "Direct: " + String.valueOf(allWidgetIds2.length));
		}
		
		String lastSelectedAccountStatus = getLastAccountStatus();
		
		if (Defs.LOG_ENABLED) {
			Log.i(Defs.LOG_TAG, "Result = " + lastSelectedAccountStatus);
		}
		
		for (int widgetId : widgetIds) {
			// get all views inside this widget
			RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
			// update text			
			remoteViews.setTextViewText(R.id.widgetText, Html.fromHtml(lastSelectedAccountStatus));
			
			if (Defs.LOG_ENABLED) {
				Log.d(Defs.LOG_TAG, "Updating id=" + widgetId);
			}
			
			// onClick listener
			Uri data = Uri.withAppendedPath(
				    Uri.parse(URI_SCHEME + "://widget/id/"), String.valueOf(widgetId));
			Intent updateIntent = new Intent(context, WidgetProvider.class);
			updateIntent.setData(data);
			
			updateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
//			updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[] {widgetId});
			updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, updateIntent, 
					PendingIntent.FLAG_UPDATE_CURRENT);
			remoteViews.setOnClickPendingIntent(R.id.refreshButton, pendingIntent);
			
			// openApp listener
			Intent openIntent = new Intent(context, MainActivity.class);
			pendingIntent = PendingIntent.getActivity(context, 0, openIntent, 0);
			remoteViews.setOnClickPendingIntent(R.id.layout, pendingIntent);
			
			// Refresh button now visible again
			remoteViews.setViewVisibility(R.id.refreshButton, View.VISIBLE);
			
			appWidgetManager.updateAppWidget(widgetId, remoteViews);
		}

		stopSelf();
		super.onStart(intent, startId);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private String getResString(int id) {
		return this.getResources().getString(id);
	}	

}
