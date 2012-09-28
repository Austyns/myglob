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


import net.vexelon.myglob.configuration.Defs;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class WidgetProvider extends AppWidgetProvider {
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

		Log.d(Defs.LOG_TAG, "onUpdate() called");
		
		// get all widgets ids
		ComponentName thisWidget = new ComponentName(context,
				WidgetProvider.class);
		int[] widgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
		
		// build intent and call service
		Intent serviceIntent = new Intent(context.getApplicationContext(), UpdateWidgetService.class);
		serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, widgetIds);
		
		// update widgets via the service
		context.startService(serviceIntent);
		
//		for (int widgetId : allWidgetIds) {
//			// Create some random data
//			int number = (new Random().nextInt(100));
//
//			RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
//					R.layout.widget_layout);
//			Log.w("WidgetExample", String.valueOf(number));
//			// Set the text
//			remoteViews.setTextViewText(R.id.update, String.valueOf(number));
//
//			// Register an onClickListener
//			Intent intent = new Intent(context, WidgetProvider.class);
//
//			intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
//			intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
//
//			PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
//					0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//			remoteViews.setOnClickPendingIntent(R.id.update, pendingIntent);
//			appWidgetManager.updateAppWidget(widgetId, remoteViews);
//		}
	}

}
