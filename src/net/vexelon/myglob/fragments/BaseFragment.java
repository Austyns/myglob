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

import java.util.ArrayList;
import java.util.List;

import net.vexelon.myglob.configuration.Defs;
import net.vexelon.myglob.users.User;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

public abstract class BaseFragment extends SherlockFragment implements IFragmentEvents {
	
	public List<IFragmentEvents> listeners = new ArrayList<IFragmentEvents>();
	
	public void addListener(IFragmentEvents listener) {
		Log.d(Defs.LOG_TAG, "adding listener...");
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}
	
	protected void setText(View v, int id, String text) {
		TextView tx = (TextView) v.findViewById(id);
		if ( tx != null )
			tx.setText(text);
	}
	
	protected void setText(View v, int id, int textId) {
		TextView tx = (TextView) v.findViewById(id);
		if ( tx != null )
			tx.setText(textId);
	}	

	protected  String getResString(int id) {
		return this.getResources().getString(id);
	}
	
	@Override
	public void onFEvent_InvoiceUpdated(User forUser) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void onFEvent_UserChanged() {
		// TODO Auto-generated method stub
	}
}
