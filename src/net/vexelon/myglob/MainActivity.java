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
	private boolean _credentialsAvailable = false;
	private String _username = "";
	
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
				
				if (!_credentialsAvailable) {
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
    			Bundle bundle = data.getExtras();
    			String username = bundle.getString(Defs.INTENT_EXTRA_USERNAME);
    			String password = bundle.getString(Defs.INTENT_EXTRA_PASSWORD);
    			boolean saveCredentials = bundle.getBoolean(Defs.INTENT_EXTRA_SAVECREDENTIALS);
    			//TODO save these
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

		//menu.add(0, Defs.MENU_EN_RATES, 0, R.string.menu_en_rates).setIcon(R.drawable.gb);
		if (!_credentialsAvailable)
			menu.add(0, Defs.MENU_SIGNIN, 0, "Sign In").setIcon(R.drawable.key);
		else
			menu.add(0, Defs.MENU_SIGNOUT, 0, "Sign Out").setIcon(R.drawable.door_out);
		
		menu.add(1, Defs.MENU_ABOUT, 15, "About").setIcon(R.drawable.help);		
	}
	
	private void loadSettings() {
		SharedPreferences prefs = this.getSharedPreferences(Defs.PREFS_NAME, 0);
		//String lastUpdateTime = prefs.getString(Defs.PREFS_KEY_LASTUPDATE_TIME, "");		
	}
	
	private void saveSettings() {
		SharedPreferences prefs = this.getSharedPreferences(Defs.PREFS_NAME, 0);
		SharedPreferences.Editor editor = prefs.edit();
		//TODO:
		editor.commit();		
	}
	
	private void loadPassword() {
		SharedPreferences prefs = this.getSharedPreferences(Defs.PREFS_NAME, 0);
		
	}
	
	private void showSignInWindow() {
		Intent intent = new Intent(this, LoginActivity.class);
		startActivityForResult(intent, Defs.INTENT_SIGNIN_RQ);
	}
    
	public String test_() {
		
		String ret = "<td class=\"txt_order_SMS\">" +
        		 "<p>Вашата текуща сметка:<span style=\"color: rgb(221, 0, 57); font-weight: bold;\"> 1,45 лв.</span> без ДДС</p>" +
                 "<p>Задължения по ф-ра за периода 18.08-17.09.2010г:<span style=\"color: rgb(221, 0, 57); font-weight: bold;\"> -0,23 лв.</span> с ДДС</p>" +
                 "<p>Данните са актуални към:<span style=\"font-weight: bold;\"> 07 Октомври, 21:15ч.</span>" +
                 "</p></td>";
		
		return ret;
    
//		GLBClient client = new GLBHttpClientImpl("0899516879", "bornin83");
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