package net.vexelon.myglob;

import net.vexelon.glbclient.GLBClient;
import net.vexelon.glbclient.GLBHttpClientImpl;
import net.vexelon.myglob.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
      
        test_();
    }
    
	public void test_() {
		GLBClient client = new GLBHttpClientImpl("", "");
		
		try {
			client.login();
			
			String content = client.getCurrentBalance();
			Log.d(Defs.LOG_TAG, content);

			content = client.getAvailableMinutes();
			Log.d(Defs.LOG_TAG, content);
			
			client.logout();
		}
		catch(Exception e) {
			Log.e(Defs.LOG_TAG, e.toString());
			//System.err.println(e.toString());
		}
		finally {
			client.close();
		}  		
	}    
}