package net.vexelon.myglob;

import java.util.List;

import net.vexelon.myglob.MainActivity.Operations;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class OperationsArrayAdapter extends ArrayAdapter<Operations> {
	
	private List<Operations> _items;
	
	public OperationsArrayAdapter(Context context, int textViewResId, List<Operations> items) {
		super(context, textViewResId, items);
		this._items = items;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return super.getView(position, convertView, parent);
	}
	
	@Override
	public Operations getItem(int position) {
		// TODO Auto-generated method stub
		return super.getItem(position);
	}
}
