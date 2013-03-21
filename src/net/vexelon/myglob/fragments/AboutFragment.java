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
	public static final int TAB_ID = 1;

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
		sb.append(getResString(R.string.about_tagline));
		sb.append("\n");
		if ( pinfo != null ) {
			sb.append(getResString(R.string.about_version));
			sb.append(" ");
			sb.append(pinfo.versionName);
			sb.append("\n");
		}
		sb.append(getResString(R.string.about_author))
		.append("\n")
		.append("http://code.google.com/p/myglob")
		.append("\n")
		.append("\n")
		.append(getResString(R.string.about_icons_info))
		.append("\n")
		.append("http://www.famfamfam.com")
		.append("\n")
		.append("\n")
		.append(getResString(R.string.about_thanks))
		.append("\n").append("Atanas Atanasow (Testing)")
		.append("\n").append("Янчо Гинчев (Bug reports)")
		.append("\n").append("Svetlana Velkova (Bug reports)")
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
