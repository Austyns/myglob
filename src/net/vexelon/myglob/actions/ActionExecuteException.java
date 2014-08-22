/*
 * MyGlob Android Application
 * 
 * Copyright (C) 2012 Petar Petrov
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
package net.vexelon.myglob.actions;

import net.vexelon.myglob.R;

@SuppressWarnings("serial")
public class ActionExecuteException extends Exception {
	
	protected int errorResId = -1;
	protected int errorTitleResId = -1;
	
	public ActionExecuteException(Throwable t) {
		super(t);
		this.errorTitleResId = R.string.dlg_error_msg_title;
	}
	
	public ActionExecuteException(int errorResId, int errorTitleResId) {
		super("");
		this.errorResId = errorResId;
		this.errorTitleResId = errorTitleResId;
	}
	
	public ActionExecuteException(int errorResId, int errorTitleResId, Throwable t) {
		super(t);
		this.errorResId = errorResId;
		this.errorTitleResId = errorTitleResId;		
	}
	
	public ActionExecuteException(int errorResId) {
		super("");
		this.errorResId = errorResId;
		this.errorTitleResId = R.string.dlg_error_msg_title;
	}
	
	public ActionExecuteException(int errorResId, Throwable t) {
		super(t);
		this.errorResId = errorResId;
		this.errorTitleResId = R.string.dlg_error_msg_title;		
	}	
	
	public int getErrorResId() {
		return this.errorResId;
	}
	
	public int getErrorTitleResId() {
		return this.errorTitleResId;
	}	
	
	public boolean isErrorResIdAvailable() {
		return this.errorResId != -1;
	}
}
