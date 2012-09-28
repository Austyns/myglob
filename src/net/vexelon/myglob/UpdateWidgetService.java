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

import net.vexelon.myglob.actions.AccountStatusAction;
import net.vexelon.myglob.actions.Action;
import net.vexelon.myglob.configuration.Defs;
import net.vexelon.myglob.configuration.GlobalSettings;
import net.vexelon.myglob.users.UsersManager;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.text.Html;
import android.util.Log;
import android.widget.RemoteViews;

public class UpdateWidgetService extends Service {
	
	private String getLastAccountStatus() {
		
        final SharedPreferences prefsUsers = this.getSharedPreferences(Defs.PREFS_USER_PREFS, 0);
        UsersManager.getInstance().reloadUsers(prefsUsers);

        SharedPreferences prefsGeneral = this.getSharedPreferences(Defs.PREFS_ALL_PREFS, 0);
        GlobalSettings.getInstance().init(prefsGeneral);
        
        Log.d(Defs.LOG_TAG, "Last acc is " + GlobalSettings.getInstance().getLastSelectedAccount());
        
        Action action = new AccountStatusAction(
        		GlobalSettings.getInstance().getLastSelectedOperation(),
        		UsersManager.getInstance().getUserByPhoneNumber(
        				GlobalSettings.getInstance().getLastSelectedAccount())
        				);
        
        try {
        	return action.execute().getStringResult();
        } catch (Exception e) {
        	Log.e(Defs.LOG_TAG, "Error retrieving account status!", e);
        }
        
        return "Error!";
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		
		Context context = this.getApplicationContext();
		
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
		int[] widgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
		
		ComponentName thisWidget = new ComponentName(context, WidgetProvider.class);
		int[] allWidgetIds2 = appWidgetManager.getAppWidgetIds(thisWidget);
		Log.w(Defs.LOG_TAG, "From intent: " + String.valueOf(widgetIds.length));
		Log.w(Defs.LOG_TAG, "Direct: " + String.valueOf(allWidgetIds2.length));
		
		String lastSelectedAccountStatus = getLastAccountStatus();
		
		for (int widgetId : widgetIds) {
			// get all views inside this widget
			RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
			
			// update text
			remoteViews.setTextViewText(R.id.update, Html.fromHtml(lastSelectedAccountStatus));	
			
			// onClick listener
			Intent clickIntent = new Intent(context, WidgetProvider.class);
			clickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
			clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, widgetIds);

			PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, clickIntent, 
					PendingIntent.FLAG_UPDATE_CURRENT);
//			remoteViews.setOnClickPendingIntent(R.id.update, pendingIntent);
			remoteViews.setOnClickPendingIntent(R.id.layout, pendingIntent);
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

}
