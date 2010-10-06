package net.vexelon.myglob;

import java.io.UnsupportedEncodingException;

import net.vexelon.glbclient.GLBClient;
import net.vexelon.glbclient.GLBHttpClientImpl;
import net.vexelon.myglob.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.text.AndroidCharacter;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private Activity _context = null;;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        _context = this;
        
        // initialize items
        
        Spinner spinnerOptions = (Spinner) findViewById(R.id.SpinnerOptions);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, 
        		new String[] {"Провери сметка", "Провери минути"});
        spinnerOptions.setAdapter(adapter);
        
        Button btnUpdate = (Button) findViewById(R.id.ButtonUpdate);
        btnUpdate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				//Intent intent = new Intent(_context, LoginActivity.class);
				//startActivity(intent);
				String data = test_();
				
				TextView tx = (TextView) _context.findViewById(R.id.TextContent);
				data = data.replaceAll("(<.[^>]*>)", "");
				data = data.replaceAll("(</.[^>]*>)", "");	
				try {
				String newStr = new String(data.getBytes(), "Windows-1251");
				tx.setText(newStr);
				}
				catch(UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				//tx.setText(Html.fromHtml(data));

				//WebView wv = (WebView) _context.findViewById(R.id.TextContent);
				//wv.loadData(data, "text/html", "utf-8");
				
			}
		});

        //Button btn = (Button)findViewById(R.id.ButtonLogin);
        //btn.getBackground().setColorFilter(0x4F00FF00, Mode.MULTIPLY);
    }
    
	public String test_() {
		GLBClient client = new GLBHttpClientImpl("", "");
		
		try {
			client.login();
			
			//String content = client.getCurrentBalance();
			//Log.d(Defs.LOG_TAG, content);

			String content = client.getAvailableMinutes();
			//Log.d(Defs.LOG_TAG, content);
			
			client.logout();
			
			return content;
		}
		catch(Exception e) {
			Log.e(Defs.LOG_TAG, e.toString());
			//System.err.println(e.toString());
		}
		finally {
			client.close();
		}  		
		
		return "";
	}    
}