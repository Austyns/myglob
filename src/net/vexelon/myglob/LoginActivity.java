package net.vexelon.myglob;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
		
		final TextView tvErrorMessage = (TextView) findViewById(R.id.TextErrorMsg);
		final EditText etUsername = (EditText) findViewById(R.id.EditTextUsername);
		final EditText etPassword = (EditText) findViewById(R.id.EditTextPassword);
		final CheckBox cbSaveCredentials = (CheckBox) findViewById(R.id.CheckBoxSaveCredentials);
		
		// login button pressed
		Button btnLogin = (Button) findViewById(R.id.ButtonLogin);
		btnLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (TextUtils.isEmpty(etUsername.getText())) {
					tvErrorMessage.setText("Username is empty!");
				}
				else if (TextUtils.isEmpty(etPassword.getText())) {
					tvErrorMessage.setText("Password's not specified!");
				}
				else {
					// prep results & quit
					Intent intent = new Intent();
					intent.putExtra(Defs.INTENT_EXTRA_USERNAME, etUsername.getText());
					intent.putExtra(Defs.INTENT_EXTRA_PASSWORD, etPassword.getText());
					intent.putExtra(Defs.INTENT_EXTRA_SAVECREDENTIALS, cbSaveCredentials.isChecked());
					setResult(RESULT_OK, intent);
					finish();
				}
			}
		});
		 
	}

}
