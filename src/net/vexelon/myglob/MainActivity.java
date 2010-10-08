package net.vexelon.myglob;

import net.vexelon.glbclient.GLBClient;
import net.vexelon.glbclient.GLBHttpClientImpl;
import net.vexelon.myglob.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private Activity _context = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        _context = this;
        
        // initialize items
        
        Spinner spinnerOptions = (Spinner) findViewById(R.id.SpinnerOptions);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, 
        		new String[] {"Проверка на текуща сметка и задължения за минал период", 
        					  "Проверка на пакет минути *",
        					  "Проверка на пакет данни (МВ) * ",
        					  "Проверка на пакет SMS/MMS * ",
        					  "Проверка на кредитен лимит "
        					  });
        spinnerOptions.setAdapter(adapter);
        
        // create update button
        Button btnUpdate = (Button) findViewById(R.id.ButtonUpdate);
        btnUpdate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if (isCredentialsAvailable()) {
					showSignInWindow();
				}
				else {
					// sign in and show requested data
					String data = test_();
					TextView tx = (TextView) _context.findViewById(R.id.TextContent);
					data = data.replaceAll("(<.[^>]*>)|(</.[^>]*>)", "");
					data = data.replaceAll("\\t|\\n|\\r", "");	
					tx.setText(data);
					//tx.setText(Html.fromHtml(data));
	
					//WebView wv = (WebView) _context.findViewById(R.id.TextContent);
					//wv.loadData(data, "text/html", "utf-8");
				}
				
			}
		});

        //Button btn = (Button)findViewById(R.id.ButtonLogin);
        //btn.getBackground().setColorFilter(0x4F00FF00, Mode.MULTIPLY);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if ( requestCode == Defs.INTENT_SIGNIN_RQ) {
    		if (resultCode == RESULT_OK) {
    			saveSettings(data.getExtras());
    		}
    	}
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		initMenu(menu);
		return true;
	} 
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		
		Intent intent = null;
		
		switch(item.getItemId()) {
		case Defs.MENU_SIGNIN:
			showSignInWindow();
			break;
			
		case Defs.MENU_SIGNOUT:
			break;
			
		case Defs.MENU_ABOUT:
			intent = new Intent(this, AboutActivity.class);
			startActivity(intent);
			break;
		}
		
		return true;
	}
	
	private void initMenu(Menu menu) {
		menu.clear();
		
		//menu.add(0, Defs.MENU_EN_RATES, 0, R.string.menu_en_rates).setIcon(R.drawable.gb);
		if (!isCredentialsAvailable())
			menu.add(0, Defs.MENU_SIGNIN, 0, "Sign In").setIcon(R.drawable.key);
		else
			menu.add(0, Defs.MENU_SIGNOUT, 0, "Sign Out").setIcon(R.drawable.door_out);
		
		menu.add(1, Defs.MENU_ABOUT, 15, "About").setIcon(R.drawable.help);		
	}
	
	private void saveSettings(Bundle bundle) {
		SharedPreferences prefs = this.getSharedPreferences(Defs.PREFS_NAME, 0);
		SharedPreferences.Editor editor = prefs.edit();
		
		boolean save = bundle.getBoolean(Defs.INTENT_EXTRA_SAVECREDENTIALS);
		if (save) {
			Log.v(Defs.LOG_TAG, "Saving username -" + bundle.getString(Defs.INTENT_EXTRA_USERNAME));
			Log.v(Defs.LOG_TAG, "Saving password -" + bundle.getString(Defs.INTENT_EXTRA_PASSWORD));
			
//			editor.putString(Defs.INTENT_EXTRA_USERNAME, bundle.getString(Defs.INTENT_EXTRA_USERNAME));
//			
//			String password = bundle.getString(Defs.INTENT_EXTRA_PASSWORD);
//			if ( !password.equals(Defs.DUMMY_PASSWORD) )
//				editor.putString(Defs.INTENT_EXTRA_PASSWORD, password);
		}

		editor.commit();		
	}
	
	private void saveKey(byte[] keyData) {
		SharedPreferences prefs = this.getSharedPreferences(Defs.PREFS_NAME, 0);
		SharedPreferences.Editor editor = prefs.edit();
		
		Log.v(Defs.LOG_TAG, "Saving key -" + Base64.encodeBytes(keyData));
//		editor.putString(Defs.INTENT_EXTRA_KEY, Base64.encodeBytes(keyData));
		
		editor.commit();		
	}	
	
	private byte[] loadKey() {
		
		byte[] ret = null;
		SharedPreferences prefs = this.getSharedPreferences(Defs.PREFS_NAME, 0);
		String key = prefs.getString(Defs.INTENT_EXTRA_KEY, "");
		
		try {
			ret = TextUtils.isEmpty(key) ? null : Base64.decode(key);
		}
		catch (Exception e) {
			Log.e(Defs.LOG_TAG, "Failed to load key!");
		}
		
		return ret;
	}
	
	private String loadUsername() {
		SharedPreferences prefs = this.getSharedPreferences(Defs.PREFS_NAME, 0);
		return prefs.getString(Defs.INTENT_EXTRA_USERNAME, "");
	}	
	
	private String loadPassword() {
		SharedPreferences prefs = this.getSharedPreferences(Defs.PREFS_NAME, 0);
		return prefs.getString(Defs.INTENT_EXTRA_PASSWORD, "");
	}
	
	private boolean isCredentialsAvailable() {
//		SharedPreferences prefs = this.getSharedPreferences(Defs.PREFS_NAME, 0);
//		return prefs.getBoolean(Defs.INTENT_EXTRA_SAVECREDENTIALS, false);
		return !TextUtils.isEmpty(loadUsername()) && !TextUtils.isEmpty(loadPassword());
	}
	
	private void showSignInWindow() {
		
		// try to load key from preferences
		byte[] keyData = loadKey();
		if (keyData == null) {
			// generate new key
			Crypto crypto = CryptAESImpl.getInstance();
			try {
				keyData = crypto.createSecretKey();
				saveKey(keyData); // save the key only once!
			}
			catch (Exception e) {
				Log.e(Defs.LOG_TAG, "Key could not be created!");
				// TODO: tell user key could not be created
			}
		}
		
		// prep login activity
		Intent intent = new Intent(this, LoginActivity.class);
		intent.putExtra(Defs.INTENT_EXTRA_KEY, keyData);
		intent.putExtra(Defs.INTENT_EXTRA_USERNAME, loadUsername());
		String password = loadPassword();
		intent.putExtra(Defs.INTENT_EXTRA_PASSWORD, TextUtils.isEmpty(password) ? password : Defs.DUMMY_PASSWORD );
		intent.putExtra(Defs.INTENT_EXTRA_SAVECREDENTIALS, isCredentialsAvailable());
		startActivityForResult(intent, Defs.INTENT_SIGNIN_RQ);
	}
    
	public String test_() {
		
		String ret = "<td class=\"txt_order_SMS\">" +
        		 "<p>Вашата текуща сметка:<span style=\"color: rgb(221, 0, 57); font-weight: bold;\"> 1,45 лв.</span> без ДДС</p>" +
                 "<p>Задължения по ф-ра за периода 18.08-17.09.2010г:<span style=\"color: rgb(221, 0, 57); font-weight: bold;\"> -0,23 лв.</span> с ДДС</p>" +
                 "<p>Данните са актуални към:<span style=\"font-weight: bold;\"> 07 Октомври, 21:15ч.</span>" +
                 "</p></td>";
		
		return ret;
    
//		GLBClient client = new GLBHttpClientImpl("", "");
//		
//		try {
//			client.login();
//			
//			String content = client.getCurrentBalance();
//			//Log.d(Defs.LOG_TAG, content);
//
//			//String content = client.getAvailableMinutes();
//			//Log.d(Defs.LOG_TAG, content);
//			
//			client.logout();
//			
//			return content;
//		}
//		catch(Exception e) {
//			Log.e(Defs.LOG_TAG, e.toString());
//			//System.err.println(e.toString());
//		}
//		finally {
//			client.close();
//		}  		
//		
//		return "";
	}    
}