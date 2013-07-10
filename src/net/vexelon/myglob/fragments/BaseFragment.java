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
