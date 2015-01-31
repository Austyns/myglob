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

import net.vexelon.myglob.R;
import net.vexelon.myglob.configuration.Defs;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class AboutFragment extends BaseFragment implements OnClickListener {
	// unique ID	
	public static final int TAB_ID = 2;

	public static AboutFragment newInstance() {
		AboutFragment fragment = new AboutFragment();

        Bundle args = new Bundle();
        fragment.setArguments(args);
        
        return fragment;		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
    	if (Defs.LOG_ENABLED)
    		Log.v(Defs.LOG_TAG, "onCreate()");
    	
		super.onCreate(savedInstanceState);
//		setHasOptionsMenu(false);
//		setMenuVisibility(false);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	if (Defs.LOG_ENABLED)
    		Log.v(Defs.LOG_TAG, "onCreateView()");
    	
		View v = inflater.inflate(R.layout.about, container, false);
		
		ImageView icLogo = (ImageView) v.findViewById(R.id.about_logo);
		icLogo.setImageResource(R.drawable.about_icon);

		PackageInfo pinfo = null;
		try {
			pinfo = getActivity().getPackageManager().getPackageInfo(
					getActivity().getPackageName(), PackageManager.GET_GIDS);
		} catch(Exception e) {
			if (Defs.LOG_ENABLED)
				Log.e(Defs.LOG_TAG, "Failed to get version info!", e);
		}

		StringBuilder sb = new StringBuilder(500);
		sb.append(getResString(R.string.app_name));
		sb.append("\n");
		if ( pinfo != null ) {
			sb.append(getResString(R.string.about_version));
			sb.append(" ");
			sb.append(pinfo.versionName);
			sb.append("\n");
		}
		sb.append("\n")
		.append(getResString(R.string.about_tagline))
		.append("\n")
		.append("\n")
		.append(getResString(R.string.about_author))
		.append("\n")
		.append("http://code.google.com/p/myglob")
		.append("\n")
		.append("\n")
		.append(getResString(R.string.about_oss))
		.append("\n")
		.append("\n")
		.append(getResString(R.string.about_thanks))
		.append("\n").append("Kristian Iliev (Testing)")
		.append("\n").append("Atanas Atanasow (Testing)")
		.append("\n").append("Янчо Гинчев (Bug reports)")
		.append("\n").append("Svetlana Velkova (Bug reports)")
		.append("\n").append("P.Zabukovsek (Suggestions)")
		.append("\n").append("STL ENCH (Bug reports)")
		
		.append("\n");

		this.setText(v, R.id.about_apptitle, sb.toString());
		Linkify.addLinks((TextView) v.findViewById(R.id.about_apptitle), Linkify.ALL);

		return v;		
	}
	
	public void onActivityCreated(Bundle savedInstanceState) {
	   	if (Defs.LOG_ENABLED)
    		Log.v(Defs.LOG_TAG, "onActivityCreated()");
	   	
		super.onActivityCreated(savedInstanceState);
		
		//TODO showLoading
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}
