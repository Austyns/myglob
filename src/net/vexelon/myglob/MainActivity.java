/*
 * The MIT License
 *
 * Copyright (c) 2010 Petar Petrov
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
package net.vexelon.myglob;

import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;

import net.vexelon.myglob.configuration.AccountPreferencesActivity;
import net.vexelon.myglob.configuration.Defs;
import net.vexelon.myglob.configuration.GlobalSettings;
import net.vexelon.myglob.fragments.AboutFragment;
import net.vexelon.myglob.fragments.HomeFragment;
import net.vexelon.myglob.users.UsersManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;

public class MainActivity extends SherlockFragmentActivity {
	
	private ActionBar _actionBar;
	
	// Pager handling
	private ViewPager _pager;
	private PagerAdapter _adapter;
    private int _tabsCount;
    
    // Fragments
    private HomeFragment _homeFragment;
    private AboutFragment _aboutFragment;
    

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	this.setTheme(Defs.THEME);
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.view_pager);

//        this.getActionBar().setDisplayOptions(ActivityInfo.UIOPTION_SPLIT_ACTION_BAR_WHEN_NARROW);
//        this.getActionBar().setCustomView(R.layout.main);
//        this.getActionBar().set

        /**
         * load preferences
         */
        
        final SharedPreferences prefsUsers = this.getSharedPreferences(Defs.PREFS_USER_PREFS, 0);
        UsersManager.getInstance().reloadUsers(prefsUsers);

        SharedPreferences prefsGeneral = this.getSharedPreferences(Defs.PREFS_ALL_PREFS, 0);
        GlobalSettings.getInstance().init(prefsGeneral);

        /**
         * initialize UI
         */
//        this.setTitle(getResString(R.string.app_name) + " - " + getResString(R.string.about_tagline));
        
        this._actionBar = getSupportActionBar();
        this._actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        this._actionBar.setTitle(getResString(R.string.app_name) + " - " + getResString(R.string.about_tagline));
        this._actionBar.setDisplayHomeAsUpEnabled(true);            
        
        this._tabsCount = 2;
        
        this._adapter = new PageAdapter(getSupportFragmentManager());
        this._pager = (ViewPager) findViewById(R.id.pager);
        this._pager.setAdapter(this._adapter);
        
        this._pager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				_actionBar.setSelectedNavigationItem(arg0);
			}
		});
        
        // Create Fragments
        
        Tab tab = _actionBar.newTab().setText("home")
        		.setTabListener(new ActionBar.TabListener() {
					
					@Override
					public void onTabUnselected(Tab tab, FragmentTransaction ft) {}
					
					@Override
					public void onTabSelected(Tab tab, FragmentTransaction ft) {
						if (tab.getText().equals("home")) {
							_pager.setCurrentItem(0);
						}
					}
					
					@Override
					public void onTabReselected(Tab tab, FragmentTransaction ft) {}
				});
        
        _actionBar.addTab(tab, true);
        
        tab = _actionBar.newTab().setText("about")
        		.setTabListener(new ActionBar.TabListener() {
					
					@Override
					public void onTabUnselected(Tab tab, FragmentTransaction ft) {}
					
					@Override
					public void onTabSelected(Tab tab, FragmentTransaction ft) {
						if (tab.getText().equals("about")) {
							_pager.setCurrentItem(0);
						}						
					}
					
					@Override
					public void onTabReselected(Tab tab, FragmentTransaction ft) {}
				});
        
        _actionBar.addTab(tab);                
        
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
//            BitmapDrawable bg = (BitmapDrawable)getResources().getDrawable(R.drawable.bg_striped);
//            bg.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
//            getSupportActionBar().setBackgroundDrawable(bg);
//
//            BitmapDrawable bgSplit = (BitmapDrawable)getResources().getDrawable(R.drawable.bg_striped_split_img);
//            bgSplit.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
//            getSupportActionBar().setSplitBackgroundDrawable(bgSplit);
//        }        


    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//		menu.clear();
    	
		menu.add(Menu.NONE, Defs.MENU_REFRESH, 0, R.string.text_refresh)
		.setIcon(R.drawable.ic_refresh)
		.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		
		// Account Operations
        SubMenu submenuOperations = menu.addSubMenu(R.string.operations_title);
        
        Operations[] operationsArray = new Operations[] {
			Operations.CHECK_CURRENT_BALANCE,
			Operations.CHECK_AVAIL_MINUTES,
			Operations.CHECK_AVAIL_DATA,
			Operations.CHECK_SMS_PACKAGE,
			Operations.CHECK_CREDIT_LIMIT,
			Operations.CHECK_ALL
		};  
        
        for (Operations operation : operationsArray) {
        	submenuOperations.add(5, operation.getId(), 0, operation.getResourceId());	
		}

        submenuOperations.getItem()
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		
        // App Menu
        SubMenu submenuActions = menu.addSubMenu(R.string.text_menu);
        
        submenuActions.add(Menu.NONE, Defs.MENU_ADD_ACCOUNT, 0, R.string.menu_add_account)
		.setIcon(R.drawable.ic_menu_invite)
		.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        
        submenuActions.add(Menu.NONE, Defs.MENU_MANAGE_ACCOUNTS, 0, R.string.menu_manage_accounts)
		.setIcon(R.drawable.ic_menu_manage)
		.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        
//        submenuActions.add(Menu.NONE, Defs.MENU_ABOUT, 0, R.string.menu_about)
//		.setIcon(R.drawable.ic_menu_help)
//		.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);        
        
        submenuActions.getItem()
        .setIcon(R.drawable.ic_menu_moreoverflow_normal_holo_dark)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		return true; //mSherlock.dispatchCreateOptionsMenu(menu);
    }

//	@Override
//	public boolean onPrepareOptionsMenu(Menu menu) {
//		return initMenu(menu);
//	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {

		Intent intent = null;

		switch(item.getItemId()) {

		case Defs.MENU_ADD_ACCOUNT:
			intent = new Intent(this, AccountPreferencesActivity.class);
			intent.putExtra(Defs.INTENT_ACCOUNT_ADD, true);
			startActivityForResult(intent, Defs.INTENT_ACCOUNT_ADD_RQ);
			break;

		case Defs.MENU_MANAGE_ACCOUNTS:
//			showAccountsList();
			break;

		case Defs.MENU_ABOUT:
//			intent = new Intent(this, AboutActivity.class);
//			startActivity(intent);
			break;
		}

		return true;
	}
	
	protected String getResString(int id) {
		return this.getResources().getString(id);
	}		
	
	/******************************************************************************************************************
	 * 
	 * Responsible for managing fragment selections
	 *
	 */
	public class PageAdapter extends FragmentStatePagerAdapter {
		
		public PageAdapter(FragmentManager manager) {
			super(manager);
		}
		
		@Override
		public int getCount() {
			return _tabsCount;
		}
		
		@Override
		public Fragment getItem(int position) {
			if (Defs.LOG_ENABLED)
				Log.d(Defs.LOG_TAG, "getItem " + position);
			
			if (position == 1) {
				// About
				_aboutFragment = AboutFragment.newInstance();
				return _aboutFragment;
			} 
				
			// default (Home)
			_homeFragment = HomeFragment.newInstance();
			return _homeFragment;
		}
		
		@Override
		public void destroyItem(View container, int position, Object object) {
			if (Defs.LOG_ENABLED)
				Log.d(Defs.LOG_TAG, "destroyItem " + container + ", " + position + ", " + object);
		}
		
	}
}