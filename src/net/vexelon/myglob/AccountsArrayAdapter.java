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

import net.vexelon.myglob.users.UsersManager;
import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AccountsArrayAdapter extends ArrayAdapter<String> {
	
	private String _items[];
	public AccountsArrayAdapter(Context context, int textViewResId, String items[]) {
		super(context, textViewResId, items);
		this._items = items;
	}	
	
//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//		if ( convertView == null ) {
//			convertView = new TextView(getContext());
//		}
//		TextView tv = (TextView)convertView;
//		tv.setText(_items[position].getName(getContext()));
//		tv.setTextColor(Color.BLACK);
//		tv.setPadding(1, 5, 1, 5);
//		tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
//		return tv;
//	}
	
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		if ( convertView == null ) {
			convertView = new TextView(getContext());
		}
		TextView tv = (TextView)convertView;
		tv.setText(String.format("%s - %s", 
				_items[position], UsersManager.getInstance().getUserByPhoneNumber(_items[position]).getAccountName()
				)
				);
		tv.setTextColor(Color.BLACK);
		tv.setPadding(1, 5, 1, 5);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
		return tv;		
	}
	
	public int getItemPosition(String phoneNumber) {
		for (int i = 0; i < _items.length; i++) {
			if (_items[i].equals(phoneNumber))
				return i;
		}
		return -1;
	}
	
}
