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
		if (Defs.LOG_ENABLED)
			Log.d(Defs.LOG_TAG, "onReceive() called");
		
		int widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
		if (AppWidgetManager.INVALID_APPWIDGET_ID == widgetId) {
			super.onReceive(context, intent);
		} else {
			this.onUpdate(context, AppWidgetManager.getInstance(context), new int[] {widgetId});
		}
	}
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		if (Defs.LOG_ENABLED)
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
