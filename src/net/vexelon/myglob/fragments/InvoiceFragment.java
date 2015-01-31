/*
 * MyGlob Android Application
 * 
 * Copyright (C) 2013 Petar Petrov
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
package net.vexelon.myglob.fragments;

import org.json.JSONObject;

import net.vexelon.myglob.R;
import net.vexelon.myglob.actions.ActionExecuteException;
import net.vexelon.myglob.actions.ActionResult;
import net.vexelon.myglob.actions.InvoiceSummaryLoadCachedAction;
import net.vexelon.myglob.actions.InvoiceSummaryUpdateAction;
import net.vexelon.myglob.configuration.Defs;
import net.vexelon.myglob.configuration.GlobalSettings;
import net.vexelon.myglob.users.User;
import net.vexelon.myglob.users.UsersManager;
import net.vexelon.myglob.utils.Utils;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class InvoiceFragment extends BaseFragment {
	// unique ID	
	public static final int TAB_ID = 1;
	
	private boolean isUpdated = false;
	
	public static InvoiceFragment newInstance() {
		InvoiceFragment fragment = new InvoiceFragment();

        Bundle args = new Bundle();
        fragment.setArguments(args);
        
        return fragment;		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
    	if (Defs.LOG_ENABLED)
    		Log.v(Defs.LOG_TAG, "InvoiceFragment.onCreate()");
    	
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	if (Defs.LOG_ENABLED)
    		Log.v(Defs.LOG_TAG, "InvoiceFragment.onCreateView()");
	    	
		View v = inflater.inflate(R.layout.invoice, container, false);
		return v;
	}
	
	@Override
	public void onStart() {
		initInvoiceView();
		super.onStart();
	}
	
    @Override
    public void onSaveInstanceState(Bundle outState) {
    	if (Defs.LOG_ENABLED)
    		Log.v(Defs.LOG_TAG, "InvoiceFragment.onSaveInstanceState");
    	
    	super.onSaveInstanceState(outState);
    	setUserVisibleHint(true);
    }	
	
	private void initInvoiceView() {
		View v = getView();
		
		TableLayout table_invoice = (TableLayout) v.findViewById(R.id.table_invoice);
		table_invoice.setVisibility(View.GONE);
		
		TextView tv = (TextView) v.findViewById(R.id.tv_invoice_status_nodata);
		tv.setVisibility(View.VISIBLE);
		
		try {
			User user = UsersManager.getInstance().getUserByPhoneNumber(
					GlobalSettings.getInstance().getLastSelectedPhoneNumber());
			if (user != null) {
				ActionResult actionResult = new InvoiceSummaryLoadCachedAction(this.getActivity(), user).execute();
				updateInvoiceView(user, actionResult.getJsonResult());
			}
		} catch (ActionExecuteException e) {
			if (Defs.LOG_ENABLED) {
				Log.d(Defs.LOG_TAG, "Failed loading invoice cache!", e);
			}
			// Simply skip view updates until user updates manually ...
		}		
	}
	
    private void updateInvoiceView(User user, JSONObject json) {
    	if (Defs.LOG_ENABLED) 
    		Log.v(Defs.LOG_TAG, "Updating invoice summary for: " + user.getPhoneNumber());
    	
    	View v = getView();
    	String invNo = "";
    	
    	if (json != null) {
    		invNo = Utils.emptyIfNull(json, Defs.INV_KEY_NO);
    	}
    	
    	if (invNo.length() > 0) {
    		String status = Utils.emptyIfNull(json, Defs.INV_KEY_STATUS);
    		if (status.equals("1")) {
    			status = getResString(R.string.text_invoice_status_paid);
    		} else if (status.equals("0")) {
    			status = getResString(R.string.text_invoice_status_notpaid);
    		}
	    	setText(v, R.id.tv_invoice_num, Utils.emptyIfNull(json, Defs.INV_KEY_NO));
	    	setText(v, R.id.tv_invoice_date, Utils.emptyIfNull(json, Defs.INV_KEY_DATEISSUED));
	    	setText(v, R.id.tv_invoice_dateref, Utils.emptyIfNull(json, Defs.INV_KEY_DATEREF));
	    	setText(v, R.id.tv_invoice_status, status);
	    	setText(v, R.id.tv_invoice_totvat, Utils.emptyIfNull(json, Defs.INV_KEY_AMOUNT_TOTAL));
	    	setText(v, R.id.tv_invoice_prev_amountdue, Utils.emptyIfNull(json, Defs.INV_KEY_AMOUNT_PREVBALANCE));
	    	setText(v, R.id.tv_invoice_total_dueamount, Utils.emptyIfNull(json, Defs.INV_KEY_AMOUNT_TOPAY));
	    	setText(v, R.id.tv_invoice_duedate, Utils.emptyIfNull(json, Defs.INV_KEY_DATE_DUE));
	    	// make table visible 
    		TextView tv = (TextView) v.findViewById(R.id.tv_invoice_status_nodata);
    		tv.setVisibility(View.GONE);
        	TableLayout table_invoice = (TableLayout) v.findViewById(R.id.table_invoice);
    		table_invoice.setVisibility(View.VISIBLE);   	    	
    	} else {
    		setText(v, R.id.tv_invoice_status_nodata, R.string.text_invoice_invalid);
    	}
    	
    	setUpdated(true);
    }
    
	public void update() {
		if (Defs.LOG_ENABLED)
			Log.d(Defs.LOG_TAG, "Updating invoice ...");
		
		final FragmentActivity activity = getActivity();
		
		if (UsersManager.getInstance().size() > 0) {

			// show progress
			final ProgressDialog myProgress = ProgressDialog.show(activity, 
					getResString(R.string.dlg_progress_title), 
					getResString(R.string.dlg_progress_message), 
					true);

			new Thread() {
				public void run() {
					try {
						final User user = UsersManager.getInstance().getUserByPhoneNumber(
								GlobalSettings.getInstance().getLastSelectedPhoneNumber());
						
						final ActionResult actionResult = new InvoiceSummaryUpdateAction(activity, user).execute();
						
						// update text field
						activity.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								updateInvoiceView(user, actionResult.getJsonResult());
								// notify listeners that invoice was updated
								for(IFragmentEvents listener : listeners) {
									listener.onFEvent_InvoiceUpdated(user);									
								}
							}
						});
						
						// close progress bar dialog
						activity.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								myProgress.dismiss();
							}
						});							
						
					} catch (ActionExecuteException e) {
						Log.e(Defs.LOG_TAG, "Error updating invoice!", e);
						
						// close progress bar dialog
						activity.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								myProgress.dismiss();
							}
						});							
						
						// Show error dialog
						if (e.isErrorResIdAvailable()) {
							Utils.showAlertDialog(activity, e.getErrorResId(), e.getErrorTitleResId());
						} else {
							Utils.showAlertDialog(activity, e.getMessage(), getResString(e.getErrorTitleResId()));
						}						
					}
				};
			}.start();
		} else  {
			// show add user screen
			Toast.makeText(this.getActivity().getApplicationContext(), 
					R.string.text_account_add_new, Toast.LENGTH_SHORT).show();

		}		
	}
	
	public boolean isUpdated() {
		return isUpdated;
	}

	public void setUpdated(boolean isUpdated) {
		this.isUpdated = isUpdated;
	}	
	
	@Override
	public void onFEvent_UserChanged() {
		initInvoiceView();
	}
}
