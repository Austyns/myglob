package net.vexelon.myglob;

import java.security.NoSuchAlgorithmException;

import org.apache.http.util.EncodingUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		//requestWindowFeature(Window.FEATURE_LEFT_ICON);
		
		setResult(RESULT_CANCELED); // default
		
		Intent intent = this.getIntent(); // get passed data
		final byte[] keyData = intent.getByteArrayExtra(Defs.INTENT_EXTRA_KEY);
		
		final TextView tvErrorMessage = (TextView) findViewById(R.id.TextErrorMsg);
		tvErrorMessage.setText("");
		
		final EditText etUsername = (EditText) findViewById(R.id.EditTextUsername);
		etUsername.setText(intent.getStringExtra(Defs.INTENT_EXTRA_USERNAME));
		
		final EditText etPassword = (EditText) findViewById(R.id.EditTextPassword);
		etPassword.setText(intent.getStringExtra(Defs.INTENT_EXTRA_PASSWORD));
		
		final CheckBox cbSaveCredentials = (CheckBox) findViewById(R.id.CheckBoxSaveCredentials);
//		cbSaveCredentials.setChecked(intent.getBooleanExtra(Defs.INTENT_EXTRA_SAVECREDENTIALS, false));
		
		// login button pressed
		Button btnLogin = (Button) findViewById(R.id.ButtonLogin);
		btnLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent();
				
				if (TextUtils.isEmpty(etUsername.getText())) {
					tvErrorMessage.setText("Username is not specified!");
				}
				else if (TextUtils.isEmpty(etPassword.getText())) {
					tvErrorMessage.setText("Password's not specified!");
				}
				else {
					// (try to) encrypt password, only if it's not the dummy password
					if ( ! etPassword.getText().equals(Defs.DUMMY_PASSWORD) ) {
						byte[] rawPassword = etPassword.getText().toString().getBytes();
						byte[] encryptedPassword = null;
						
						Crypto crypto = CryptAESImpl.getInstance();
						
						try {
							encryptedPassword = crypto.encrypt(rawPassword, keyData);
						}
						catch (Exception e) {
							Log.e(Defs.LOG_TAG, "Password could not be encrypted!", e);
							//TODO: tell user that password was not encrypted
							encryptedPassword = rawPassword;
						}
						
						intent.putExtra(Defs.INTENT_EXTRA_PASSWORD, Base64.encodeBytes(encryptedPassword));
					}
					else {
						intent.putExtra(Defs.INTENT_EXTRA_PASSWORD, Defs.DUMMY_PASSWORD);
					}

					// prep results & quit
					intent.putExtra(Defs.INTENT_EXTRA_USERNAME, etUsername.getText());
					intent.putExtra(Defs.INTENT_EXTRA_SAVECREDENTIALS, cbSaveCredentials.isChecked());
					setResult(RESULT_OK, intent);
					finish();
				}
			}
		});
		 
	}

}
