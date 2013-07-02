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

import java.util.List;
import java.util.Map;

import net.vexelon.myglob.R;
import net.vexelon.myglob.actions.ActionExecuteException;
import net.vexelon.myglob.actions.ActionResult;
import net.vexelon.myglob.actions.InvoiceUpdateAction;
import net.vexelon.myglob.configuration.Defs;
import net.vexelon.myglob.configuration.GlobalSettings;
import net.vexelon.myglob.users.User;
import net.vexelon.myglob.users.UsersManager;
import net.vexelon.myglob.utils.Utils;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class InvoiceFragment extends BaseFragment {
	// unique ID	
	public static final int TAB_ID = 1;
	
	public static InvoiceFragment newInstance() {
		InvoiceFragment fragment = new InvoiceFragment();

        Bundle args = new Bundle();
        fragment.setArguments(args);
        
        return fragment;		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
    	if (Defs.LOG_ENABLED)
    		Log.v(Defs.LOG_TAG, "onCreate()");
    	
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.invoice, container, false);
		
		// TODO
		
		return v;
	}
	
    private void updateInvoiceView(List<Map<String, String>> results) {
    	
    	View v = getView();
    	
    	setText(v, R.id.tv_invoice_num, results.get(0).get("InvNum"));
    	setText(v, R.id.tv_invoice_date, results.get(0).get("InvNum"));
    	setText(v, R.id.tv_invoice_services, results.get(0).get("InvNum"));
    	setText(v, R.id.tv_invoice_discount, results.get(0).get("Disc"));
    	setText(v, R.id.tv_invoice_totvat, results.get(0).get("Tot-VAT"));
    }
	
	public void update() {
		if (Defs.LOG_ENABLED) {
			Log.d(Defs.LOG_TAG, "Updating invoice ...");
		}
		
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
						User user = UsersManager.getInstance().getUserByPhoneNumber(
								GlobalSettings.getInstance().getLastSelectedPhoneNumber());
						
						final ActionResult actionResult = new InvoiceUpdateAction(activity, user)
								.execute();
						
						final List<Map<String, String>> results = 
								(List<Map<String, String>>)actionResult.getListResult();
//						
//						// remember last account and operation
//						GlobalSettings.getInstance().setLastSelectedPhoneNumber(phoneNumber);
//						GlobalSettings.getInstance().setLastSelectedOperation(finalOperation);	
						
						// update text field
						activity.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								updateInvoiceView(results);
//								tvContent.setText(Html.fromHtml(actionResult.getString()));
//								updateProfileView(phoneNumber);
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
}
