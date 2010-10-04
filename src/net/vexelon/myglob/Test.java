package net.vexelon.myglob;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.params.BasicHttpParams;

public class Test {
	
	public static void main(String[] args) {
		
		SchemeRegistry schemeReg = new SchemeRegistry();
		schemeReg.register(new Scheme("http", new PlainSocketFactory(), 80));
		
		SingleClientConnManager scm = new SingleClientConnManager(
				new BasicHttpParams(),
				schemeReg );
		
		DefaultHttpClient client = new DefaultHttpClient(scm, new BasicHttpParams());
		
		try {
			HttpResponse resp = client.execute(new HttpHost("my.globul.bg"), 
					new BasicHttpRequest("GET", "/")
			);
			
			System.out.println("RESP: " + resp.getStatusLine().getStatusCode() );
		}
		catch(Exception e) {
			e.printStackTrace();
		}

		
	}
}