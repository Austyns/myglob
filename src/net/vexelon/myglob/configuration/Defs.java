/*
 * MyGlob Android Application
 * 
 * Copyright (C) 2010 Petar Petrov
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
package net.vexelon.myglob.configuration;

import java.text.SimpleDateFormat;

import net.vexelon.myglob.R;

public class Defs {
	
	public final static String LOG_TAG = "net.vexelon.myglob";
	public final static boolean LOG_ENABLED = true;
	
	// Theming
	public final static int THEME = R.style.Theme_Sherlock;
	
	public final static int CLR_BUTTON_UPDATE = 0xFFA6D060;
	public final static String CLR_TEXT_HIGHLIGHT = "#FF7F00"; //0xFFFF7F00;
	
	public final static SimpleDateFormat globalDateFormat = new SimpleDateFormat("dd-MM-yy HH:mm");

	public final static int MENU_OPTIONS_BASE = 100;
	public final static int MENU_ADD_ACCOUNT = 200;
	public final static int MENU_MANAGE_ACCOUNTS = 300;
	public final static int MENU_UPDATE_INVOICE = 400;
	
	// save/load keys
	public final static String PREFS_ALL_PREFS = "MyGlobAppPreferences";
	public final static String PREFS_KEY = "_KEY_";
	public final static String PREFS_LAST_SELECTED_ACCOUNT = "_LAST_SELECTED_ACCOUNT_";
	public final static String PREFS_LAST_SELECTED_OPERATION = "_LAST_SELECTED_OPERATION_";
	public final static String PREFS_LAST_CHECKED_INFO = "_LAST_CHECKED_INFO_";
	
	public final static String PREFS_USER_PREFS = "MyGlobAppPreferences.UserPrefs";
	public final static String PREFS_USER_COUNT = "_USER_COUNT";
	public final static String PREFS_USER_NAME = "_USER_NAME";
	public final static String PREFS_USER_PHONENUMBER = "_USER_PHONENUMBER";
	public final static String PREFS_USER_PASSWORD = "_PASSWORD";
	public final static String PREFS_USER_CHECKSTODAY = "_CHECKSTODAY";
	public final static String PREFS_USER_CHECKSTOTAL = "_CHECKSTOTAL";
	public final static String PREFS_USER_TRAFFICTODAY = "_TRAFFICTODAY";
	public final static String PREFS_USER_TRAFFICTOTAL = "_TRAFFICTOTAL";
	public final static String PREFS_USER_LASTCHECKDATETIME = "_LASTCHECKDATETIME";
	public final static String PREFS_USER_LASTCHECKDATA = "_LASTCHECKDATA";
	
	public final static int INTENT_ACCOUNT_ADD_RQ = 200;
	public final static int INTENT_ACCOUNT_EDIT_RQ = 210;
	public final static int INTENT_RESULT_ACCOUT_DELETED = 100;
	public final static String INTENT_ACCOUNT_ADD = "_ACCOUNT_ADD";
	public final static String INTENT_ACCOUNT_EDIT = "_ACCOUNT_EDIT";
	public final static String INTENT_ACCOUNT_PHONENUMBER = "_ACCOUNT_PHONENUMBER";
	
	// Invoice
	public final static String ISTORAGE_XML_FORMAT = "ivc_%s_%s";
	public final static String INV_KEY_STATUS = "_invk_status";
	public final static String INV_KEY_NO = "_invk_num";
	public final static String INV_KEY_DATEISSUED = "_invk_dateissued";
	public final static String INV_KEY_DATEREF = "_invk_dateref";
	public final static String INV_KEY_AMOUNT_TOTAL = "_invk_totalamount";
	public final static String INV_KEY_AMOUNT_PREVBALANCE = "_invk_prevbalance";
	public final static String INV_KEY_AMOUNT_TOPAY = "_invk_amountopay";
	public final static String INV_KEY_DATE_DUE = "_invk_duedate";
	
	// OBSOLETE
	public final static int INTENT_SIGNIN_RQ = 100;
	public final static String LEGACY_INTENT_EXTRA_KEY = "_SK";
	public final static String LEGACY_INTENT_EXTRA_USERNAME = "_USERNAME";
	public final static String LEGACY_INTENT_EXTRA_PASSWORD = "_PASSWORD";
	public final static String LEGACY_INTENT_EXTRA_SAVECREDENTIALS = "_SAVECREDENTIALS";
//	public final static String INTENT_EXTRA_LASTRESULT = "_LASTRESULT";
	
	public final static String DUMMY_PASSWORD = "$_DUMMY_PASSWORD_^";
	
}
