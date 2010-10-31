package net.vexelon.myglob;

import net.vexelon.myglob.MainActivity.Operations;
import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class OperationsArrayAdapter extends ArrayAdapter<Operations> {
	
//	private List<Operations> _items;
//	public OperationsArrayAdapter(Context context, int textViewResId, List<Operations> items) {
//		super(context, android.R.layout.simple_spinner_item, items);
//		this._items = items;
//	}
	
	private Operations[] _items;
	public OperationsArrayAdapter(Context context, int textViewResId, Operations[] items) {
		super(context, textViewResId, items);
		this._items = items;
	}	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if ( convertView == null ) {
			convertView = new TextView(getContext());
		}
		TextView tv = (TextView)convertView;
		tv.setText(_items[position].getName(getContext()));
		tv.setTextColor(Color.BLACK);
		tv.setPadding(1, 5, 1, 5);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
		return tv;
	}
	
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		if ( convertView == null ) {
			convertView = new TextView(getContext());
		}
		TextView tv = (TextView)convertView;
		tv.setText(_items[position].getName(getContext()));
		tv.setTextColor(Color.BLACK);
		tv.setPadding(1, 5, 1, 5);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
		return tv;		
	}
	
}
