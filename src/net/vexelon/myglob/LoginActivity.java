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
import net.vexelon.myglob.crypto.CryptAESImpl;
import net.vexelon.myglob.crypto.Crypto;
import net.vexelon.myglob.utils.Base64;
import net.vexelon.myglob.utils.Utils;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.InputFilter.LengthFilter;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {
	
	private AlertDialog _alert = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		
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
		cbSaveCredentials.setChecked(intent.getBooleanExtra(Defs.INTENT_EXTRA_SAVECREDENTIALS, false));
		
		final Activity activity = this;
		final Context context = this;
		
		// SSL pressed
		final TextView tvSSL = (TextView) findViewById(R.id.TextSSL);
		tvSSL.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(context, R.string.dlg_msg_ssl, Toast.LENGTH_LONG).show();
			}
		});		
		
		// login button pressed
		Button btnLogin = (Button) findViewById(R.id.ButtonLogin);
		btnLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent();
				
				if (TextUtils.isEmpty(etUsername.getText())) {
					tvErrorMessage.setText(getResString(R.string.err_msg_username_missing));
				}
				else if (TextUtils.isEmpty(etPassword.getText())) {
					tvErrorMessage.setText(getResString(R.string.err_msg_password_missing));
				}
				else {
					// (try to) encrypt password, only if it's not the dummy password
					if ( ! etPassword.getText().toString().equals(Defs.DUMMY_PASSWORD) ) {
						byte[] rawPassword = etPassword.getText().toString().getBytes();
						byte[] encryptedPassword = null;
						
						Crypto crypto = CryptAESImpl.getInstance();
						
						try {
							//throw new Exception("ERROR necrrypt"); //TODO: remove this line
							encryptedPassword = crypto.encrypt(rawPassword, keyData);
						}
						catch (Exception e) {
							//Log.e(Defs.LOG_TAG, "Password could not be encrypted!", e);
							encryptedPassword = rawPassword;
							
							// show error msg
							activity.runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									_alert = Utils.createAlertDialog(context, R.string.dlg_error_msg_encrypt_failed, R.string.dlg_error_msg_title);
									_alert.setOnDismissListener(new OnDismissListener() {
										@Override
										public void onDismiss(DialogInterface dialog) {
											activity.finish();
										}
									});
									
									_alert.setButton(AlertDialog.BUTTON_NEGATIVE, getResString(R.string.dlg_msg_no), new DialogInterface.OnClickListener() {
									
										@Override
										public void onClick(DialogInterface dialog, int which) {
											activity.setResult(RESULT_CANCELED);
											//_alert.dismiss();
										}
									});
									_alert.setButton(AlertDialog.BUTTON_POSITIVE, getResString(R.string.dlg_msg_yes), new DialogInterface.OnClickListener() {
										
										@Override
										public void onClick(DialogInterface dialog, int which) {
											//_alert.dismiss();
										}
									});									
									
									_alert.show();
								}
							});
						}
						
						//Log.v(Defs.LOG_TAG, "Intent password: " + Base64.encodeBytes(encryptedPassword));
						intent.putExtra(Defs.INTENT_EXTRA_PASSWORD, Base64.encodeBytes(encryptedPassword));
					}
					else {
						//Log.v(Defs.LOG_TAG, "Intent password: " + Defs.DUMMY_PASSWORD);
						intent.putExtra(Defs.INTENT_EXTRA_PASSWORD, Defs.DUMMY_PASSWORD);
					}

					// prep results & quit
					intent.putExtra(Defs.INTENT_EXTRA_USERNAME, etUsername.getText().toString());
					intent.putExtra(Defs.INTENT_EXTRA_SAVECREDENTIALS, cbSaveCredentials.isChecked());
					setResult(RESULT_OK, intent);
					
					// if there are no alert dialogs on screen
					if ( _alert == null )
						finish();
				}
			}
		});
		 
	}
	
	private String getResString(int id) {
		return this.getResources().getString(id);
	}		
}
