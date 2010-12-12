package net.vexelon.myglob.configuration;

import net.vexelon.myglob.R;
import net.vexelon.myglob.users.AccountType;
import net.vexelon.myglob.users.User;
import net.vexelon.myglob.users.UsersManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceClickListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;

public class AccountPreferencesActivity extends PreferenceActivity {
	
	EditTextPreference _accountNamePref;
	EditTextPreference _accountNumberPref;
	ListPreference _accountOperatorPref;
	EditTextPreference _accountPasswordPref;
	CheckBoxPreference _accountSavePasswordPref;
	Preference _accountDeletePref;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setResult(RESULT_CANCELED);
		addPreferencesFromResource(R.xml.prefs);
		
		// get components refs
		_accountNamePref = (EditTextPreference) findPreference(getResString(R.string.pref_account_name));
		_accountOperatorPref = (ListPreference) findPreference(getResString(R.string.pref_account_operator));
		_accountNumberPref = (EditTextPreference) findPreference(getResString(R.string.pref_account_phonenumber));
		_accountPasswordPref = (EditTextPreference) findPreference(getResString(R.string.pref_account_password));
//		_accountSavePasswordPref = (CheckBoxPreference) findPreference(getResString(R.string.pref_account_save_password)); 
		_accountDeletePref = (Preference) findPreference(getResString(R.string.pref_account_delete));
		
		Intent intent = getIntent();
		if (intent.getBooleanExtra(Defs.INTENT_ACCOUNT_ADD, false)) {
			_accountNamePref.setText("");
			_accountNumberPref.setText("");
			_accountOperatorPref.setValueIndex(0);
			_accountPasswordPref.setText("");
//			_accountSavePasswordPref.setChecked(false);
			_accountDeletePref.setEnabled(false);
		}
		else if (intent.getBooleanExtra(Defs.INTENT_ACCOUNT_EDIT, false)) {
			// edit account
			String phone = intent.getStringExtra(Defs.INTENT_ACCOUNT_PHONENUMBER);
			User user = UsersManager.getInstance().getUserByPhoneNumber(phone);
			_accountNamePref.setText(user.getAccountName());
			_accountNumberPref.setText(user.getPhoneNumber());
			_accountNumberPref.setEnabled(false); // NO NUMBER EDITING ALLOWED !
			_accountOperatorPref.setValueIndex(0);
			
			final Activity activity = this;
			_accountDeletePref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
				@Override
				public boolean onPreferenceClick(Preference preference) {
					UsersManager.getInstance().removeUser(_accountNumberPref.getText());
					
					// save all users
					SharedPreferences prefs = activity.getSharedPreferences(Defs.PREFS_USER_PREFS, 0);
					UsersManager.getInstance().save(prefs);
					activity.setResult(Defs.INTENT_RESULT_ACCOUT_DELETED);
					activity.finish();
					return false;
				}
			});
		}
		else {
			// no proper operation type passed
			setResult(RESULT_CANCELED);
			finish();
		}
	}
	
	private void saveSettings() {
		// save all account data
		
		if (getIntent().getBooleanExtra(Defs.INTENT_ACCOUNT_ADD, false)) {
			UsersManager.getInstance().addUser(
					new User().setAccountName(_accountNamePref.getText())
					.setPhoneNumber(_accountNumberPref.getText())
					.setAccountType(getAccountTypeFromListPrefs())
					);
			
			// save all users
			SharedPreferences prefs = this.getSharedPreferences(Defs.PREFS_USER_PREFS, 0);
			UsersManager.getInstance().save(prefs);
		}
	}
	
	private boolean isSettingsValid() {
		return !TextUtils.isEmpty(_accountNamePref.getText()) &&
			!TextUtils.isEmpty(_accountNumberPref.getText()) &&
			!TextUtils.isEmpty(_accountOperatorPref.getValue()) &&
			!TextUtils.isEmpty(_accountPasswordPref.getText())
			;
	}
	
	private AccountType getAccountTypeFromListPrefs() {
		String[] operators = getResStringArray(R.array.array_available_operators);
		if (_accountOperatorPref.getValue().equals(operators[0])) {
			return AccountType.Globul;
		}
		else if (_accountOperatorPref.getValue().equals(operators[1])) {
			return AccountType.M_Tel;
		}
		
		return null;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			
			if (isSettingsValid()) {
				saveSettings();
				setResult(RESULT_OK);
			}
			else {
				//TODO: warning
				setResult(RESULT_CANCELED);
				Log.i(Defs.LOG_TAG, "Nothing was saved!");
			}
			
			finish();
			return true;
		}
		
		return super.onKeyDown(keyCode, event);
	}

	private String getResString(int id) {
		return this.getResources().getString(id);
	}	
	
	private String[] getResStringArray(int id) {
		return this.getResources().getStringArray(id);
	}	
	
}
