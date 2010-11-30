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
package net.vexelon.myglob.utils;


import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;

import net.vexelon.myglob.R;
import net.vexelon.myglob.R.drawable;

import org.apache.http.util.ByteArrayBuffer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;


public class Utils {
	
	private static Random _random = null;
	
	public static String scaleNumber(BigDecimal number, int n ) {
		return number.setScale(n, BigDecimal.ROUND_HALF_UP).toPlainString();
	}
	
	public static String roundNumber(BigDecimal number, int n) {
		return number.round(new MathContext(n, RoundingMode.HALF_UP)).toPlainString();
	}
	
	public static String stripHtml(String html, boolean stripWhiteSpace) {
		html = html.replaceAll("(<.[^>]*>)|(</.[^>]*>)", "");
		if ( stripWhiteSpace )
			html = html.replaceAll("\\t|\\n|\\r", "");	
		html = html.trim();	
		return html;
	}
	
	public static String stripHtml(String html) {
		return stripHtml(html, true);
	}
	
	/**
	 * Downloads a file given URL to specified destination
	 * @param url
	 * @param destFile
	 * @return
	 */
	//public static boolean downloadFile(Context context, String url, String destFile) {
	public static boolean downloadFile(Context context, String url, File destFile) {
		//Log.v(TAG, "@downloadFile()");
		//Log.d(TAG, "Downloading " + url);
		
		boolean ret = false;
		
		BufferedInputStream bis = null;
		FileOutputStream fos = null;
		InputStream is = null;
		
		try {
			URL myUrl = new URL(url);
			URLConnection connection = myUrl.openConnection();
			
			is = connection.getInputStream();
			bis = new BufferedInputStream(is);
			ByteArrayBuffer baf = new ByteArrayBuffer(1024);
			
			int n = 0;
			while( (n = bis.read()) != -1 )
				baf.append((byte) n);
			
			// save to internal storage
			//Log.v(TAG, "Saving downloaded file ...");
			fos = new FileOutputStream(destFile); 
				//context.openFileOutput(destFile, context.MODE_PRIVATE);
			fos.write(baf.toByteArray());
			fos.close();
			//Log.v(TAG, "File saved successfully.");
			
			ret = true;
		}
		catch(Exception e) {
			//Log.e(TAG, "Error while downloading and saving file !", e);
		}
		finally {
			try {
				if ( fos != null ) fos.close();
			} catch( IOException e ) {}
			try {
				if ( bis != null ) bis.close();
			} catch( IOException e ) {}
			try {
				if ( is != null ) is.close();
			} catch( IOException e ) {}
		}
		
		return ret;
	}

	/**
	 * Move a file stored in the cache to the internal storage of the specified context
	 * @param context
	 * @param cacheFile
	 * @param internalStorageName
	 */
	public static boolean moveCacheFile(Context context, File cacheFile, String internalStorageName) {
		
		boolean ret = false;
		FileInputStream fis = null;
		FileOutputStream fos = null;
		
		try {
			fis = new FileInputStream(cacheFile);
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
			byte[] buffer = new byte[1024];
			int read = -1;
			while( (read = fis.read(buffer) ) != -1 ) {
				baos.write(buffer, 0, read);
			}
			baos.close();
			fis.close();

			fos = context.openFileOutput(internalStorageName, Context.MODE_PRIVATE);
			baos.writeTo(fos);
			fos.close();
			
			// delete cache
			cacheFile.delete();
			
			ret = true;
		}
		catch(Exception e) {
			//Log.e(TAG, "Error saving previous rates!");
		}
		finally {
			try { if ( fis != null ) fis.close(); } catch (IOException e) { }
			try { if ( fos != null ) fos.close(); } catch (IOException e) { }
		}
		
		return ret;
	}
	
	/**
	 * Get random integer value
	 * @param max
	 * @return
	 */
	public static int getRandomInt(int max) {
		if (_random == null )
			_random = new Random(System.currentTimeMillis());
		
		return _random.nextInt(max);
	}
	
	
	/**
	 * Display an alert dialog using resource IDs
	 * @param context
	 * @param messageResId
	 * @param titleResId
	 */
	public static void showAlertDialog(Activity activity, int messageResId, int titleResId) {
		showAlertDialog(activity, activity.getResources().getString(messageResId), 
				activity.getResources().getString(titleResId));
	}

	/**
	 * Display alert dialog using string message
	 * @param context
	 * @param message
	 * @param titleResId
	 */
	public static void showAlertDialog(Activity activity, String message, String title) {
		final Activity act = activity;
		final String s1 = message, s2 = title;
		activity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				AlertDialog alert = createAlertDialog(act, s1, s2);
				alert.show();
			}
		});
	}	
	
	/**
	 * Create alert dialog using resource IDs
	 * @param context
	 * @param messageResId
	 * @param titleResId
	 * @return
	 */
	public static AlertDialog createAlertDialog(Context context, int messageResId, int titleResId) {
		return createAlertDialog(context, context.getResources().getString(messageResId), 
				context.getResources().getString(titleResId));
	}
	
	/**
	 * Create an alert dialog without showing it on screen
	 * @param context
	 * @param message
	 * @param titleResId
	 * @return
	 */
	public static AlertDialog createAlertDialog(Context context, String message, String title) {
		
		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
		
		return alertBuilder.setTitle(title).setMessage(message).setIcon(
				R.drawable.alert).setOnKeyListener(
				new DialogInterface.OnKeyListener() {

					@Override
					public boolean onKey(DialogInterface dialog,
							int keyCode, KeyEvent event) {
						dialog.dismiss();
						return false;
					}
				}).create();
	}
	
}