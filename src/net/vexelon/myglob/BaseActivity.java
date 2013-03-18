package net.vexelon.myglob;

import net.vexelon.myglob.configuration.Defs;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.ActionBar.Tab;

public class BaseActivity extends SherlockActivity implements ActionBar.TabListener {
	
	protected Activity _activity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.setTheme(Defs.THEME);
		super.onCreate(savedInstanceState);
		
		// defaults
		_activity = this;
		
		// create tabs
        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        String[] tabsArrays = this.getResources().getStringArray(R.array.tabs_array);
        for (int i = 0; i < tabsArrays.length; i++) {
            ActionBar.Tab tab = getSupportActionBar().newTab();
            tab.setText(tabsArrays[i]);
            tab.setTabListener(this);
            getSupportActionBar().addTab(tab);
        }		
	}
	
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
		
	}
	
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}
}
