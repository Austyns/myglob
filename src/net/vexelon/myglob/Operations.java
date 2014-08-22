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
package net.vexelon.myglob;

import net.vexelon.myglob.configuration.Defs;
import android.content.Context;

public enum Operations {
	CHECK_ALL(0, R.string.operation_check_all),
	CHECK_CURRENT_BALANCE(10, R.string.operation_check_balance),
	CHECK_AVAIL_MINUTES(12, R.string.operation_check_avail_minutes),
	CHECK_AVAIL_DATA(14, R.string.operation_check_avail_data),
	CHECK_AVAIL_TRAVELNSURF(15, R.string.operation_check_travelnsurf),
	CHECK_SMS_PACKAGE(16, R.string.operation_check_sms_pack),
	CHECK_CREDIT_LIMIT(18, R.string.operation_check_credit_limit)
	;

	private int resId;
	private int id;

	Operations(int id, int resourceId) {
		this.id = id;
		this.resId = resourceId;
	}

	public String getText(Context context) {
		return context.getString(this.resId);
	}
	
	public int getId() {
		return this.id;
	}
	
	public int getResourceId() {
		return this.resId;
	}
	
	public int getMenuId() {
		return Defs.MENU_OPTIONS_BASE + 1 + this.id;
	}
}
