package net.vexelon.myglob;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class LoginActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		requestWindowFeature(Window.FEATURE_LEFT_ICON);
		 
	}

}
