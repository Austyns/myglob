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

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import net.vexelon.mobileops.GLBInvoiceXMLParser;
import net.vexelon.myglob.R;
import net.vexelon.myglob.actions.ActionExecuteException;
import net.vexelon.myglob.actions.ActionResult;
import net.vexelon.myglob.actions.InvoiceLoadCachedAction;
import net.vexelon.myglob.actions.InvoiceSummaryUpdateAction;
import net.vexelon.myglob.actions.InvoiceUpdateAction;
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
	
	@SuppressWarnings("unchecked")
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
				ActionResult actionResult = new InvoiceLoadCachedAction(this.getActivity(), user)
						.execute();
				
				updateInvoiceView(user, null);
			}
		} catch (ActionExecuteException e) {
			if (Defs.LOG_ENABLED) {
				Log.d(Defs.LOG_TAG, "Failed loading invoice cache!", e);
			}
			// Simply skip view updates until user updates manually ...
		}		
	}
	
	private BigDecimal valOrZero(String value) {
		BigDecimal result = new BigDecimal("0.00");
		try {
			result = new BigDecimal(value);
		} catch (NumberFormatException e) {
			Log.w(Defs.LOG_TAG, "Failed to get decimal value from " + value + "!", e);
		}
		return result;
	}
	
//    private void updateInvoiceView(User user, List<Map<String, String>> results) {
//    	if (Defs.LOG_ENABLED) 
//    		Log.v(Defs.LOG_TAG, "Updating invoice for: " + user.getPhoneNumber());
//    	
//    	View v = getView();
//    	boolean found = false;
//    	
//    	for (Map<String, String> map : results) {
//			if (map.containsKey(GLBInvoiceXMLParser.TAG_MSISDN)) {
//				String value = map.get(GLBInvoiceXMLParser.TAG_MSISDN);
////				String userPhone = user.getPhoneNumber();
////				if (value.endsWith(userPhone.substring(userPhone.length() - 6, userPhone.length()))) {
//				if (value.trim().length() == 0) {
//					// invoice info
//			    	setText(v, R.id.tv_invoice_num, map.get(GLBInvoiceXMLParser.TAG_INVNUM));
//			    	
//		    		Calendar calendar = Calendar.getInstance();
//		    		calendar.setTimeInMillis(Long.parseLong(map.get(GLBInvoiceXMLParser.TAG_DATE)));			    	
//			    	setText(v, R.id.tv_invoice_date, Defs.globalDateFormat.format(calendar.getTime()));
//			    	// costs
//			    	BigDecimal servicesCharge = new BigDecimal("0.00");
//			    	BigDecimal discounts = new BigDecimal("0.00");
//			    	try {
//			    		// solve discounts amount
//			    		BigDecimal discount = valOrZero(map.get(GLBInvoiceXMLParser.TAG_DISCOUNT));
//			    		BigDecimal discountPackage = valOrZero(map.get(GLBInvoiceXMLParser.TAG_DISCOUNT_PACKAGE));
//			    		BigDecimal discountLoyality = valOrZero(map.get(GLBInvoiceXMLParser.TAG_DISCOUNT_LOYALITY));
//			    		BigDecimal discountUBB = valOrZero(map.get(GLBInvoiceXMLParser.TAG_DISCOUNT_GLOBUL_UBB));
//			    		discounts = discounts
//			    				.add(discount)
//			    				.add(discountPackage)
//			    				.add(discountLoyality)
//			    				.add(discountUBB);
//			    		
//			    		// solve services costs
//			    		BigDecimal fixedCharge = valOrZero(map.get(GLBInvoiceXMLParser.TAG_FIXED_CHARGE));
////			    		BigDecimal discounts = new BigDecimal(map.get(GLBInvoiceXMLParser.TAG_DISCOUNT));
//			    		BigDecimal totalNoVAT = valOrZero(map.get(GLBInvoiceXMLParser.TAG_TOTAL_NO_VAT));
//			    		servicesCharge = totalNoVAT
//			    				.subtract(discounts)
//			    				.subtract(fixedCharge);
//			    		
//			    	} catch (Exception e) {
//			    		Log.e(Defs.LOG_TAG, "Failed to get decimal prices info!", e);
//			    		/*
//			    		 * XXX
//			    		 * It would be better to throw exception at this point!
//			    		 */
//			    		discounts = new BigDecimal(map.get(GLBInvoiceXMLParser.TAG_DISCOUNT));
//			    	}
//			    	setText(v, R.id.tv_invoice_services, servicesCharge.toPlainString());
//			    	setText(v, R.id.tv_invoice_fixed_charge, map.get(GLBInvoiceXMLParser.TAG_FIXED_CHARGE));
//			    	setText(v, R.id.tv_invoice_discount, discounts.toPlainString());
//			    	// totals
//			    	setText(v, R.id.tv_invoice_tot_no_vat, map.get(GLBInvoiceXMLParser.TAG_TOTAL_NO_VAT));
//			    	setText(v, R.id.tv_invoice_vat, map.get(GLBInvoiceXMLParser.TAG_VAT));
//			    	setText(v, R.id.tv_invoice_totvat, map.get(GLBInvoiceXMLParser.TAG_TOTALVAT));
//			    	// amount dues
//			    	setText(v, R.id.tv_invoice_prev_amountdue, map.get(GLBInvoiceXMLParser.TAG_PREV_AMOUNTDUE));
//			    	setText(v, R.id.tv_invoice_paied_amountdue, map.get(GLBInvoiceXMLParser.TAG_PAID_AMOUNTDUE));
//			    	setText(v, R.id.tv_invoice_total_dueamount, map.get(GLBInvoiceXMLParser.TAG_TOTAL_DUEAMOUNT));		
//			    
//			    	found = true;
//			    	break;
//				}
//			}
//		}
//    	
//    	if (!found) {
//    		// empty MSISDN was not found!
//    		setText(v, R.id.tv_invoice_status_nodata, R.string.text_invoice_invalid);
//    	} else {
//    		TextView tv = (TextView) v.findViewById(R.id.tv_invoice_status_nodata);
//    		tv.setVisibility(View.GONE);
//    		
//        	TableLayout table_invoice = (TableLayout) v.findViewById(R.id.table_invoice);
//    		table_invoice.setVisibility(View.VISIBLE);    		
//    	}
//
//    	setUpdated(true);
//    }
	
    private void updateInvoiceView(User user, Map<String, String> results) {
    	if (Defs.LOG_ENABLED) 
    		Log.v(Defs.LOG_TAG, "Updating invoice summary for: " + user.getPhoneNumber());
    	
    	View v = getView();
    	boolean found = false;
    	
    	if (results != null) {
    		String status = Utils.emptyIfNull(results.get(Defs.INV_KEY_STATUS));
    		if (status.equals("1")) {
    			status = getResString(R.string.text_invoice_status_paid);
    		} else if (status.equals("0")) {
    			status = getResString(R.string.text_invoice_status_notpaid);
    		}
    		
	    	setText(v, R.id.tv_invoice_num, Utils.emptyIfNull(results.get(Defs.INV_KEY_NO)));
	    	setText(v, R.id.tv_invoice_date, Utils.emptyIfNull(results.get(Defs.INV_KEY_DATEISSUED)));
	    	setText(v, R.id.tv_invoice_dateref, Utils.emptyIfNull(results.get(Defs.INV_KEY_DATEREF)));
	    	setText(v, R.id.tv_invoice_status, status);
	    	setText(v, R.id.tv_invoice_totvat, Utils.emptyIfNull(results.get(Defs.INV_KEY_AMOUNT_TOTAL)));
	    	setText(v, R.id.tv_invoice_prev_amountdue, Utils.emptyIfNull(results.get(Defs.INV_KEY_AMOUNT_PREVBALANCE)));
	    	setText(v, R.id.tv_invoice_total_dueamount, Utils.emptyIfNull(results.get(Defs.INV_KEY_AMOUNT_TOPAY)));
	    	setText(v, R.id.tv_invoice_duedate, Utils.emptyIfNull(results.get(Defs.INV_KEY_DATE_DUE)));
	    	found = true;
    	}
    	
    	if (!found) {
    		// empty MSISDN was not found!
    		setText(v, R.id.tv_invoice_status_nodata, R.string.text_invoice_invalid);
    	} else {
    		TextView tv = (TextView) v.findViewById(R.id.tv_invoice_status_nodata);
    		tv.setVisibility(View.GONE);
    		
        	TableLayout table_invoice = (TableLayout) v.findViewById(R.id.table_invoice);
    		table_invoice.setVisibility(View.VISIBLE);    		
    	}
    	
    	setUpdated(true);
    }
    
    @SuppressWarnings("unchecked")
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
						
//						final ActionResult actionResult = new InvoiceUpdateAction(activity, user).execute();
						final ActionResult actionResult = new InvoiceSummaryUpdateAction(activity, user).execute();
						
						// update text field
						activity.runOnUiThread(new Runnable() {

							@Override
							public void run() {
//								updateInvoiceView(user, (List<Map<String, String>>)actionResult.getListResult());
								updateInvoiceView(user, (Map<String, String>)actionResult.getMapResult());
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
