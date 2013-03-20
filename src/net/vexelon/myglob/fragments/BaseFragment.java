package net.vexelon.myglob.fragments;

import android.view.View;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

public class BaseFragment extends SherlockFragment {

	protected void setText(View v, int id, String text) {
		TextView tx = (TextView) v.findViewById(id);
		if ( tx != null )
			tx.setText(text);
	}

	protected  String getResString(int id) {
		return this.getResources().getString(id);
	}		
}
