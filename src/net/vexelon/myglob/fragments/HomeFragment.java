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
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class HomeFragment extends BaseFragment implements OnClickListener {
	// unique ID
	public static final int TAB_ID = 0;
	
	private Activity _activity;
	private AccountsArrayAdapter _adapterAccounts = null;
	
	public static HomeFragment newInstance() {
		HomeFragment fragment = new HomeFragment();

        Bundle args = new Bundle();
//        args.putString(Constants.User.USER_LOGIN, login);
//        args.putString(Constants.User.USER_NAME, name);
        fragment.setArguments(args);
        
        return fragment;		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this._activity = this.getActivity();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.main, container, false);
		
//      // init options spinner
//      Spinner spinnerOptions = (Spinner) findViewById(R.id.SpinnerOptions);
//      OperationsArrayAdapter adapter = new OperationsArrayAdapter(
//      		getSupportActionBar().getThemedContext(), R.layout.sherlock_spinner_item,
//      		new Operations[]{
//      		Operations.CHECK_CURRENT_BALANCE,
//				Operations.CHECK_AVAIL_MINUTES,
//				Operations.CHECK_AVAIL_DATA,
//				Operations.CHECK_SMS_PACKAGE,
//				Operations.CHECK_CREDIT_LIMIT,
//				Operations.CHECK_ALL,
//      });
//      adapter.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);
//      spinnerOptions.setAdapter(adapter);
//      
//      // pre-select Operation
//      int pos = adapter.getItemPosition(GlobalSettings.getInstance().getLastSelectedOperation());
//      if (pos != -1) {
//      	spinnerOptions.setSelection(pos);
//      }
//
//      // populate available accounts
//      updateAccountsSpinner();
//
//      // create update button
//      Button btnUpdate = (Button) findViewById(R.id.ButtonUpdate);
//      btnUpdate.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				updateSelectedStatus();
//			}
//		});
//      
//   // create menu button
//      Button btnMenu = (Button) findViewById(R.id.ButtonMenu);
//      btnMenu.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				openOptionsMenu();
//			}
//		});        
//
//      //btnUpdate.getBackground().setColorFilter(0x2212FF00, Mode.LIGHTEN);
//      btnUpdate.getBackground().setColorFilter(Defs.CLR_BUTTON_UPDATE, Mode.MULTIPLY);
		
		// load last saved operation info (if available)
		if (GlobalSettings.getInstance().getLastCheckedInfo() != GlobalSettings.NO_INFO) {
			TextView textContent = (TextView) v.findViewById(R.id.TextContent);
			textContent.setText(Html.fromHtml(GlobalSettings.getInstance().getLastCheckedInfo()));
		}
      
//
//      /**
//       * try to find legacy users and add them to UsersManager
//       */
//      final LegacySettings legacySettings = new LegacySettings();
//      legacySettings.init(prefsGeneral);
//      if (legacySettings.getPhoneNumber() != null) {
//
//			AlertDialog.Builder alertBuilder = new AlertDialog.Builder(_activity);
//			alertBuilder.setTitle(R.string.dlg_legacyuser_title)
//				.setMessage(String.format(getResString(R.string.dlg_legacyuser_msg), legacySettings.getPhoneNumber()))
//				.setIcon(R.drawable.alert)
//				.setPositiveButton(getResString(R.string.dlg_msg_yes), new DialogInterface.OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						// Create & Add user
//			        	User user = new User().setAccountName(legacySettings.getPhoneNumber())
//							.setPhoneNumber(legacySettings.getPhoneNumber())
//							.setAccountType(AccountType.Globul); //V1.1.0 has only Globul support
//
//			        	try {
//				        	if (UsersManager.getInstance().isUserExists(legacySettings.getPhoneNumber()))
//				        		throw new Exception(getResString(R.string.err_msg_user_already_exists));
//
//				        	UsersManager.getInstance().addUser(user);
//			        		UsersManager.getInstance().setUserPassword(user, legacySettings.getPassword());
//			        		UsersManager.getInstance().save(prefsUsers);
//			        		updateAccountsSpinner();
//			        	}
//			        	catch(Exception e) {
//			        		Utils.showAlertDialog(_activity, String.format(getResString(R.string.dlg_error_msg_legacy_user_failed), e.getMessage()), getResString(R.string.dlg_error_msg_title));
//			        	}
//			        	legacySettings.clear();
//						dialog.dismiss();
//					}
//				})
//				.setNegativeButton(getResString(R.string.dlg_msg_no), new DialogInterface.OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						legacySettings.clear();
//						dialog.dismiss();
//					}
//				}).show();
//      }				
		
		return v;
	}
	
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		//TODO showLoading
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
    	
        // pre-select
        if (GlobalSettings.getInstance().getLastSelectedAccount() != GlobalSettings.NO_ACCOUNT) {
        	updateSelection(GlobalSettings.getInstance().getLastSelectedAccount());
        }    	
    	
    	super.onStart();    	
    }	
	
	@Override
	public void onResume() {
		super.onResume();
		// app resumed
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
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

    	updateAccountsSpinner();
    }
    
//    public boolean Activity.onCreateOptionsMenu(android.view.Menu menu) {
//    	return mSherlock.dispatchCreateOptionsMenu(menu);
//    }
    
    /**
     * Update all data with respect to selected account
     */
    private void updateSelection(String phoneNumber) {
    	if (Defs.LOG_ENABLED)
    		Log.i(Defs.LOG_TAG, "Updating selection for: " + phoneNumber);
    	
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
    		// TODO
    	}
    }
    

	/**
	 * Show a list of user accounts
	 */
	private void showAccountsList() {

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
			builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent(_activity.getApplicationContext(), 
							AccountPreferencesActivity.class);
					intent.putExtra(Defs.INTENT_ACCOUNT_EDIT, true);
					intent.putExtra(Defs.INTENT_ACCOUNT_PHONENUMBER, items[which]);
					startActivityForResult(intent, Defs.INTENT_ACCOUNT_EDIT_RQ);

					dialog.dismiss();
				}
			});

			AlertDialog alert = builder.create();
			alert.show();
		}
		else {
			Toast.makeText(this.getActivity().getApplicationContext(), 
					R.string.text_account_no_account, Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Prefill accounts data in spinner
	 */
	private void updateAccountsSpinner() {
		View v = this.getView();
		
		Spinner spinnerAccounts = (Spinner) v.findViewById(R.id.SpinnerUserAccounts);
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.LayoutNoAccounts);
		final String[] items = UsersManager.getInstance().getUsersPhoneNumbersList();

		if (items != null) {
			//_adapterAccounts = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
			_adapterAccounts = new AccountsArrayAdapter(this.getActivity(), android.R.layout.simple_spinner_item, items);
			_adapterAccounts.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        spinnerAccounts.setAdapter(_adapterAccounts);

	        // visualize component
	        spinnerAccounts.setVisibility(Spinner.VISIBLE);
	        layout.setVisibility(LinearLayout.INVISIBLE);
	        
	        // pre-select
	        if (GlobalSettings.getInstance().getLastSelectedAccount() != GlobalSettings.NO_ACCOUNT) {
		        spinnerAccounts.setSelection(
		        		_adapterAccounts.getItemPosition(GlobalSettings.getInstance().getLastSelectedAccount()));
	        }
		}
		else {
			// remove all items, if any
//			if (_adapterAccounts != null) {
//				_adapterAccounts.clear();
//			}

			// no accounts, no selection
			spinnerAccounts.setVisibility(Spinner.INVISIBLE);
			layout.setVisibility(LinearLayout.VISIBLE);
		}
	}

	/**
	 * Get selected spinner option and update view
	 */
	private void updateSelectedStatus() {
		View v = this.getView();
		
		Spinner spinnerAccounts = (Spinner) v.findViewById(R.id.SpinnerUserAccounts);

		if (UsersManager.getInstance().size() > 0 && spinnerAccounts.getSelectedItemPosition() != Spinner.INVALID_POSITION) {

			Spinner spinnerOptions = (Spinner) v.findViewById(R.id.SpinnerOptions);
			final Operations operation = (Operations) spinnerOptions.getSelectedItem();
			final TextView tx = (TextView) _activity.findViewById(R.id.TextContent);
			final String phoneNumber = (String) spinnerAccounts.getItemAtPosition(spinnerAccounts.getSelectedItemPosition());

			// show progress
			final ProgressDialog myProgress = ProgressDialog.show(this.getActivity().getApplicationContext(), 
					getResString(R.string.dlg_progress_title), getResString(R.string.dlg_progress_message), true);

			// do work
			new Thread() {
				public void run() {

					try {
						Action action = new AccountStatusAction(operation,
								UsersManager.getInstance().getUserByPhoneNumber(phoneNumber));
						
						// remember last account and operation
						GlobalSettings.getInstance().putLastSelectedAccount(phoneNumber);
						GlobalSettings.getInstance().putLastSelectedOperation(operation);
						
						final String data = action.execute().getString();
						
						// save what was last found
						GlobalSettings.getInstance().putLastCheckedInfo(data);

						// update text field
						_activity.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								tx.setText(Html.fromHtml(data));
								//WebView wv = (WebView) _activity.findViewById(R.id.TextContent);
								//wv.loadData(data, "text/html", "utf-8");
							}
						});
						
					}
					catch (InvalidCredentialsException e) {
						// Show error dialog
						Utils.showAlertDialog(_activity, R.string.dlg_error_msg_invalid_credentials, 
								R.string.dlg_error_msg_title);
					}
					catch (SecureCodeRequiredException e) {
						// Show error dialog
						Utils.showAlertDialog(_activity, R.string.dlg_error_msg_securecode, R.string.dlg_error_msg_title);
					}
					catch (Exception e) {
						Log.e(Defs.LOG_TAG, "Error updating status!", e);
						// Show error dialog
						final String msg = e.getMessage();
						Utils.showAlertDialog(_activity, msg, getResString(R.string.dlg_error_msg_title));
					}

					// close progress bar dialog
					_activity.runOnUiThread(new Runnable() {
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

		} // end if
	}
	
}
