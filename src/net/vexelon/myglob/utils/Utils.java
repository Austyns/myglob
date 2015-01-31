/*
 * MyGlob Android Application
 * 
 * Copyright (C) 2010 Petar Petrov
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
package net.vexelon.myglob.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Random;

import net.vexelon.myglob.R;

import org.apache.http.util.ByteArrayBuffer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;

public class Utils {
	
	public static final int BUFFER_PAGE_SIZE = 4096; // 4k
	
	private static Random _random = null;
	
	public static String emptyIfNull(String value) {
		return value == null ? "" : value;
	}
	
	public static String scaleNumber(BigDecimal number, int n) {
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
	 * Write input stream data to PRIVATE internal storage file.
	 * @param context
	 * @param source
	 * @param internalStorageName
	 * @throws IOException
	 */
	public static void writeToInternalStorage(Context context, InputStream source, String internalStorageName) 
			throws IOException {
		
		FileOutputStream fos = null;
		try {
			fos = context.openFileOutput(internalStorageName, Context.MODE_PRIVATE);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			
			BufferedInputStream bis = new BufferedInputStream(source);
			byte[] buffer = new byte[4096];
			int read = -1;
			while((read = bis.read(buffer)) != -1) {
				bos.write(buffer, 0, read);
			}
			bos.flush();
			bos.close();
		} catch (FileNotFoundException e) {
			throw new IOException(e.getMessage());
		} finally {
			try { if ( fos != null ) fos.close(); } catch (IOException e) { }
			try { if ( source != null ) source.close(); } catch (IOException e) { }
		}		
	}
	
	/**
	 * Reads an input stream into a byte array
	 * @param source
	 * @return Byte array of input stream data
	 * @throws IOException
	 */
	public static byte[] read(InputStream source) throws IOException {
		ReadableByteChannel srcChannel = Channels.newChannel(source);
		ByteArrayOutputStream baos = new ByteArrayOutputStream(
				source.available() > 0 ? source.available() : BUFFER_PAGE_SIZE);
		WritableByteChannel destination = Channels.newChannel(baos);
		
		try {
			ByteBuffer buffer = ByteBuffer.allocate(BUFFER_PAGE_SIZE);
			while(srcChannel.read(buffer) > 0) {
				buffer.flip();
				while(buffer.hasRemaining()) {
					destination.write(buffer);
				}
				buffer.clear();
			}
			return baos.toByteArray();
		} catch(IOException e) {
			throw e;
		} finally {
			try { if ( srcChannel != null ) srcChannel.close(); } catch (IOException e) { }
			try { if ( source != null ) source.close(); } catch (IOException e) { }
			try { if ( destination != null ) destination.close(); } catch (IOException e) { }
		}
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
				R.drawable.ic_dialog_alert).setOnKeyListener(
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
