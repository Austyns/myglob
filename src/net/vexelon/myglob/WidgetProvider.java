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

import java.util.Arrays;

import net.vexelon.myglob.configuration.Defs;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

public class WidgetProvider extends AppWidgetProvider {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(Defs.LOG_TAG, "onReceive() called");
		
		int widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
		if (AppWidgetManager.INVALID_APPWIDGET_ID == widgetId) {
			super.onReceive(context, intent);
		} else {
			this.onUpdate(context, AppWidgetManager.getInstance(context), new int[] {widgetId});
		}
	}
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

		Log.d(Defs.LOG_TAG, "onUpdate() called - " + Arrays.toString(appWidgetIds));
		
		// get all widgets ids
//		ComponentName thisWidget = new ComponentName(context, WidgetProvider.class);
//		int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
		
		// build intent and call service
		Intent serviceIntent = new Intent(context.getApplicationContext(), UpdateWidgetService.class);
		serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
		
		// update widgets via the service
		context.startService(serviceIntent);
		
		// show animation
		for (int widgetId : appWidgetIds) {
			RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
			remoteViews.setTextViewText(R.id.widgetText, context.getString(R.string.dlg_progress_message));
			// prevent multiple clicks
			remoteViews.setViewVisibility(R.id.refreshButton, View.INVISIBLE);
			
//			ImageView imageView = new ImageView(context);
//			imageView.setImageResource(R.drawable.loading);
//			AnimationDrawable loadingAnimation = (AnimationDrawable)imageView.getDrawable();
//			remoteViews.addView(imageView.getId(), imageView);
//			remoteViews.setImageViewResource(viewId, srcId)
			appWidgetManager.updateAppWidget(widgetId, remoteViews);
		}
	}

}
