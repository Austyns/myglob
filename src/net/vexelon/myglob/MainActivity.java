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
	
	enum Operations {
		CHECK_CURRENT_BALANCE,
		CHECK_AVAIL_MINUTES,
		CHECK_AVAIL_DATA,
		CHECK_SMS_PACKAGE,
		CHECK_CREDIT_LIMIT
	};
	
	private Activity _context = null;
	
	private String _username = "";
	private String _password = "";
	private boolean _saveCredentials = false;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        _context = this;
        
        loadSettings();
        
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
				
				if (!isCredentialsAvailable()) {
					showSignInWindow();
				}
				else {
					// sign in and show requested data
					String data = getAccountStatus(Operations.CHECK_CURRENT_BALANCE);
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
			clearSettings();
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
	
	private String getDecryptedPassword(String encodedPassword) {
		String result = encodedPassword;
		byte[] keyData = loadKey();
		
		if (!TextUtils.isEmpty(encodedPassword) && keyData != null) {
			Crypto crypto = CryptAESImpl.getInstance();
			try {
				byte[] rawPassword = Base64.decode(encodedPassword);
				byte[] decryptedPassword = crypto.decrypt(rawPassword, keyData);
				result = new String(decryptedPassword);
			}
			catch (Exception e) {
				Log.e(Defs.LOG_TAG, "Password could not be decrypted!", e);
				//TODO: tell user that password was not decrypted
			}
		}
		
		return result;
	}
	
	/**
	 * Clear saved account preferences
	 */
	private void clearSettings() {
		Bundle bundle = new Bundle();
		bundle.putString(Defs.INTENT_EXTRA_USERNAME, "");
		bundle.putString(Defs.INTENT_EXTRA_PASSWORD, "");
		bundle.putBoolean(Defs.INTENT_EXTRA_SAVECREDENTIALS, true);
		saveSettings(bundle);		
	}
	
	/**
	 * Load account preferences and encrypted/b64 password
	 */
	private void loadSettings() {
		SharedPreferences prefs = this.getSharedPreferences(Defs.PREFS_NAME, 0);
		_username = prefs.getString(Defs.INTENT_EXTRA_USERNAME, _username);
		_password = prefs.getString(Defs.INTENT_EXTRA_PASSWORD, _password);
		_saveCredentials = !TextUtils.isEmpty(_username) && !TextUtils.isEmpty(_password);
	}
	
	/**
	 * Save account settings from data in a bundle
	 * @param bundle
	 */
	private void saveSettings(Bundle bundle) {
		Log.v(Defs.LOG_TAG, "Saving username: " + bundle.getString(Defs.INTENT_EXTRA_USERNAME));
		
		_username = bundle.getString(Defs.INTENT_EXTRA_USERNAME);
		String password = bundle.getString(Defs.INTENT_EXTRA_PASSWORD);
		if ( !password.equals(Defs.DUMMY_PASSWORD) ) {
			_password = password;
			Log.v(Defs.LOG_TAG, "Saving password: " + bundle.getString(Defs.INTENT_EXTRA_PASSWORD));
		}
		
		boolean save = bundle.getBoolean(Defs.INTENT_EXTRA_SAVECREDENTIALS);
		if (save) {
			Log.v(Defs.LOG_TAG, "Saving to prefs ...");
			//_saveCredentials = save;
			SharedPreferences prefs = this.getSharedPreferences(Defs.PREFS_NAME, 0);
			SharedPreferences.Editor editor = prefs.edit();
			
			editor.putString(Defs.INTENT_EXTRA_USERNAME, bundle.getString(Defs.INTENT_EXTRA_USERNAME));
			editor.putString(Defs.INTENT_EXTRA_PASSWORD, password);
			
			editor.commit();
		}
	}
	
	/**
	 * Saves secret key into preferences
	 * @param keyData - Byte array of key data contents
	 */
	private void saveKey(byte[] keyData) {
		SharedPreferences prefs = this.getSharedPreferences(Defs.PREFS_NAME, 0);
		SharedPreferences.Editor editor = prefs.edit();
		
		Log.v(Defs.LOG_TAG, "Saving key -" + Base64.encodeBytes(keyData));
		editor.putString(Defs.INTENT_EXTRA_KEY, Base64.encodeBytes(keyData));
		
		editor.commit();		
	}	
	
	/**
	 * Loads secret key from preferences
	 * @return Byte array of key data contents
	 */
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
	
	private boolean isCredentialsAvailable() {
		return !TextUtils.isEmpty(_username) && !TextUtils.isEmpty(_password);
	}
	
	/**
	 * Pops up the Sign-In window
	 */
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
		intent.putExtra(Defs.INTENT_EXTRA_USERNAME, _username);
		intent.putExtra(Defs.INTENT_EXTRA_PASSWORD, TextUtils.isEmpty(_password) ? _password : Defs.DUMMY_PASSWORD );
		intent.putExtra(Defs.INTENT_EXTRA_SAVECREDENTIALS, _saveCredentials);
		startActivityForResult(intent, Defs.INTENT_SIGNIN_RQ);
	}
    
	public String getAccountStatus(Operations operation) {
		
//		String ret = "<td class=\"txt_order_SMS\">" +
//        		 "<p>Вашата текуща сметка:<span style=\"color: rgb(221, 0, 57); font-weight: bold;\"> 1,45 лв.</span> без ДДС</p>" +
//                 "<p>Задължения по ф-ра за периода 18.08-17.09.2010г:<span style=\"color: rgb(221, 0, 57); font-weight: bold;\"> -0,23 лв.</span> с ДДС</p>" +
//                 "<p>Данните са актуални към:<span style=\"font-weight: bold;\"> 07 Октомври, 21:15ч.</span>" +
//                 "</p></td>";
//		
//		return ret;
    
		String result = "";
		GLBClient client = new GLBHttpClientImpl(_username, getDecryptedPassword(_password));
		Log.v(Defs.LOG_TAG, "Loging in using " + _username + " and pass: " + getDecryptedPassword(_password));
		
		try {
			client.login();
			
			switch(operation) {
			case CHECK_CURRENT_BALANCE:
				result = client.getCurrentBalance();
				break;
			case CHECK_AVAIL_MINUTES:
				result = client.getAvailableMinutes();
				break;
			case CHECK_CREDIT_LIMIT:
				result = client.getCreditLimit();
				break;
			case CHECK_AVAIL_DATA:
				result = client.getAvailableInternetBandwidth();
				break;
			case CHECK_SMS_PACKAGE:
				result = "NOT AVAIL!";
				break;
			}

			client.logout();
		}
		catch(Exception e) {
			Log.e(Defs.LOG_TAG, "Failed to login!", e);
		}
		finally {
			client.close();
		}  		
		
		return result;
	}    
}