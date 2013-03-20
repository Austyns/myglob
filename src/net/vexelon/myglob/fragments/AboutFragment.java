package net.vexelon.myglob.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;

public class AboutFragment extends SherlockFragment implements OnClickListener {

	public static AboutFragment newInstance() {
		AboutFragment fragment = new AboutFragment();

        Bundle args = new Bundle();
        fragment.setArguments(args);
        
        return fragment;		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		//TODO showLoading
	}
	
	@Override
	public void onResume() {
		super.onResume();
		// app resumed
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	
}
