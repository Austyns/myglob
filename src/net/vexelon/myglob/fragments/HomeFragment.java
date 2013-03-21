/*
 * The MIT License
 *
 * Copyright (c) 2013 Petar Petrov
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
package net.vexelon.myglob.fragments;

import net.vexelon.mobileops.InvalidCredentialsException;
import net.vexelon.mobileops.SecureCodeRequiredException;
import net.vexelon.myglob.AccountsArrayAdapter;
import net.vexelon.myglob.Operations;
import net.vexelon.myglob.R;
import net.vexelon.myglob.actions.AccountStatusAction;
import net.vexelon.myglob.actions.Action;
import net.vexelon.myglob.configuration.AccountPreferencesActivity;
import net.vexelon.myglob.configuration.Defs;
import net.vexelon.myglob.configuration.GlobalSettings;
import net.vexelon.myglob.users.User;
import net.vexelon.myglob.users.UsersManager;
import net.vexelon.myglob.utils.Utils;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class HomeFragment extends BaseFragment implements OnClickListener {
	// unique ID
	public static final int TAB_ID = 0;
	
	public static HomeFragment newInstance() {
		HomeFragment fragment = new HomeFragment();

        Bundle args = new Bundle();
        fragment.setArguments(args);
        
        return fragment;		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		this._activity = this.getActivity();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.main, container, false);
		
		TextView tvPhoneNumber = (TextView) v.findViewById(R.id.tv_profile_number);
		tvPhoneNumber.setOnClickListener(this);
//
//      //btnUpdate.getBackground().setColorFilter(0x2212FF00, Mode.LIGHTEN);
//      btnUpdate.getBackground().setColorFilter(Defs.CLR_BUTTON_UPDATE, Mode.MULTIPLY);
		
		return v;
	}
	
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	
    @Override
    public void onStart() {
//    	// trap Operations selection
//    	Spinner spinnerOptions = (Spinner) findViewById(R.id.SpinnerOptions);
//        spinnerOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//        	@Override
//        	public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//        		Operations operation = (Operations) parentView.getSelectedItem();
//        		GlobalSettings.getInstance().putLastSelectedOperation(operation);
//        	}
//        	
//        	@Override
//        	public void onNothingSelected(AdapterView<?> parentView) {
//        	}
//		});   
//        
//        // trap phone number selection
//        Spinner spinnerAccounts = (Spinner) findViewById(R.id.SpinnerUserAccounts);
//        spinnerAccounts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//        	@Override
//        	public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//        		String phoneNumber = (String) parentView.getSelectedItem();
//        		GlobalSettings.getInstance().putLastSelectedAccount(phoneNumber);
//        	}
//        	
//        	@Override
//        	public void onNothingSelected(AdapterView<?> parentView) {
//        	}
//		});
    	
		// load last saved operation info (if available)
		if (GlobalSettings.getInstance().getLastCheckedInfo() != GlobalSettings.NO_INFO) {
			TextView textContent = (TextView) getView().findViewById(R.id.tv_status_content);
			textContent.setText(Html.fromHtml(GlobalSettings.getInstance().getLastCheckedInfo()));
		}    	
    	
    	updateProfileView(GlobalSettings.getInstance().getLastSelectedAccount());
        // pre-select
    	
    	super.onStart();    	
    }	
	
	@Override
	public void onResume() {
		super.onResume();
		// app resumed
	}
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		
		switch(id) {
		case R.id.tv_profile_number:
			
			showAccountsList(new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					final String[] items = UsersManager.getInstance().getUsersPhoneNumbersList();
					updateProfileView(items[which]);
				}
			});
			
			break;
		}
		
	}
	
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	switch(requestCode) {
    	case Defs.INTENT_ACCOUNT_ADD_RQ:
    		if (resultCode == Activity.RESULT_OK) {
    			Toast.makeText(this.getActivity().getApplicationContext(), 
    					R.string.text_account_created, Toast.LENGTH_SHORT).show();
    		}
    		break;

    	case Defs.INTENT_ACCOUNT_EDIT_RQ:
    		if (resultCode == Activity.RESULT_OK) {
    			Toast.makeText(this.getActivity().getApplicationContext(), 
    					R.string.text_account_saved, Toast.LENGTH_SHORT).show();
    		}
    		else if (resultCode == Defs.INTENT_RESULT_ACCOUT_DELETED) {
    			Toast.makeText(this.getActivity().getApplicationContext(), 
    					R.string.text_account_removed, Toast.LENGTH_SHORT).show();
    		}
    		break;
    	}

        // pre-select
        if (GlobalSettings.getInstance().getLastSelectedAccount() != GlobalSettings.NO_ACCOUNT) {
        	updateProfileView(GlobalSettings.getInstance().getLastSelectedAccount());
        }     	
    }
    
    /**
     * Show preferences Activity where new account may be added
     */
    public void showAddAccount() {
    	Intent intent = new Intent(this.getActivity(), AccountPreferencesActivity.class);
		intent.putExtra(Defs.INTENT_ACCOUNT_ADD, true);
		startActivityForResult(intent, Defs.INTENT_ACCOUNT_ADD_RQ);    	
    }
    
    /**
     * Show list of accounts that can be edited
     */
    public void showEditAccount() {
    	
    	final FragmentActivity activity = this.getActivity();

    	showAccountsList(new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					final String[] items = UsersManager.getInstance().getUsersPhoneNumbersList();
					
					Intent intent = new Intent(activity.getApplicationContext(), 
							AccountPreferencesActivity.class);
					intent.putExtra(Defs.INTENT_ACCOUNT_EDIT, true);
					intent.putExtra(Defs.INTENT_ACCOUNT_PHONENUMBER, items[which]);
					startActivityForResult(intent, Defs.INTENT_ACCOUNT_EDIT_RQ);

					dialog.dismiss();
				}
			});
    }
    
    /**
     * Update all data with respect to selected account
     */
    private void updateProfileView(String phoneNumber) {
    	if (Defs.LOG_ENABLED)
    		Log.v(Defs.LOG_TAG, "Updating selection for: " + phoneNumber);
    	
    	View v = getView();
    	
    	User user = UsersManager.getInstance().getUserByPhoneNumber(phoneNumber);
    	if (user != null) {
    		setText(v, R.id.tv_profile_number, user.getPhoneNumber());
    		setText(v, R.id.tv_profile_name, user.getAccountName());
    		setText(v, R.id.tv_checks_today, String.valueOf(user.getChecksToday()));
    		setText(v, R.id.tv_checks_overal, String.valueOf(user.getChecksTotal()));
    		setText(v, R.id.tv_traffic_today, String.valueOf(user.getTrafficToday()));
    		setText(v, R.id.tv_traffic_overal, String.valueOf(user.getTrafficTotal()));
    		
    	} else {
			Toast.makeText(this.getActivity().getApplicationContext(), 
					R.string.text_account_not_found, Toast.LENGTH_SHORT).show();
    	}
    }
    

	/**
	 * Show a list of user accounts
	 */
	private void showAccountsList(DialogInterface.OnClickListener clickListener) {
    	if (Defs.LOG_ENABLED)
    		Log.v(Defs.LOG_TAG, "showAccountsList()");
    	
		final String[] items = UsersManager.getInstance().getUsersPhoneNumbersList();
		if (items != null) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
			builder.setTitle(R.string.dlg_account_select_title);
			builder.setCancelable(true);
			builder.setNegativeButton(R.string.dlg_msg_cancel, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
//			builder.setSingleChoiceItems(items, -1, clickListener);
			builder.setSingleChoiceItems(
					new AccountsArrayAdapter(this.getActivity(), android.R.layout.select_dialog_singlechoice, items), 
					-1, 
					clickListener);			

			AlertDialog alert = builder.create();
			alert.show();
		} else {
			Toast.makeText(this.getActivity().getApplicationContext(), 
					R.string.text_account_no_account, Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Fill accounts data in spinner
	 */
//	private void updateAccountsSpinner() {
//		View v = this.getView();
//		
//		Spinner spinnerAccounts = (Spinner) v.findViewById(R.id.SpinnerUserAccounts);
//		LinearLayout layout = (LinearLayout) v.findViewById(R.id.LayoutNoAccounts);
//		final String[] items = UsersManager.getInstance().getUsersPhoneNumbersList();
//
//		if (items != null) {
//			//_adapterAccounts = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
//			_adapterAccounts = new AccountsArrayAdapter(this.getActivity(), android.R.layout.simple_spinner_item, items);
//			_adapterAccounts.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//	        spinnerAccounts.setAdapter(_adapterAccounts);
//
//	        // visualize component
//	        spinnerAccounts.setVisibility(Spinner.VISIBLE);
//	        layout.setVisibility(LinearLayout.INVISIBLE);
//	        
//	        // pre-select
//	        if (GlobalSettings.getInstance().getLastSelectedAccount() != GlobalSettings.NO_ACCOUNT) {
//		        spinnerAccounts.setSelection(
//		        		_adapterAccounts.getItemPosition(GlobalSettings.getInstance().getLastSelectedAccount()));
//	        }
//		}
//		else {
//			// remove all items, if any
////			if (_adapterAccounts != null) {
////				_adapterAccounts.clear();
////			}
//
//			// no accounts, no selection
//			spinnerAccounts.setVisibility(Spinner.INVISIBLE);
//			layout.setVisibility(LinearLayout.VISIBLE);
//		}
//	}

	/**
	 * Get selected spinner option and update view
	 */
	public void updateStatus() {
		View v = this.getView();
		
		final FragmentActivity activity = getActivity();
		
		if (UsersManager.getInstance().size() > 0) {

			final Operations operation = Operations.CHECK_SMS_PACKAGE;
			final TextView tvContent = (TextView) v.findViewById(R.id.tv_status_content);
			final TextView tvPhoneNumber = (TextView) v.findViewById(R.id.tv_profile_number);
			final String phoneNumber = (String) tvPhoneNumber.getText();

			// show progress
			final ProgressDialog myProgress = ProgressDialog.show(activity, 
					getResString(R.string.dlg_progress_title), 
					getResString(R.string.dlg_progress_message), 
					true);

			new Thread() {
				public void run() {

					try {
						Action action = new AccountStatusAction(operation,
								UsersManager.getInstance().getUserByPhoneNumber(phoneNumber));
						
						// remember last account and operation
						GlobalSettings.getInstance().putLastSelectedAccount(phoneNumber);
						GlobalSettings.getInstance().putLastSelectedOperation(operation);
						
						final String data = action.execute().getString();
						
						// save last found 
						GlobalSettings.getInstance().putLastCheckedInfo(data);

						// update text field
						activity.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								tvContent.setText(Html.fromHtml(data));
							}
						});
						
					} catch (InvalidCredentialsException e) {
						// Show error dialog
						Utils.showAlertDialog(activity, R.string.dlg_error_msg_invalid_credentials, 
								R.string.dlg_error_msg_title);
					} catch (SecureCodeRequiredException e) {
						// Show error dialog
						Utils.showAlertDialog(activity, R.string.dlg_error_msg_securecode, R.string.dlg_error_msg_title);
					} catch (Exception e) {
						Log.e(Defs.LOG_TAG, "Error updating status!", e);
						// Show error dialog
						final String msg = e.getMessage();
						Utils.showAlertDialog(activity, msg, getResString(R.string.dlg_error_msg_title));
					}

					// close progress bar dialog
					activity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							myProgress.dismiss();
						}
					});
				};
			}.start();
		} else  {
			// show add user screen
			Toast.makeText(this.getActivity().getApplicationContext(), 
					R.string.text_account_add_new, Toast.LENGTH_SHORT).show();

		}
	}
	
}
