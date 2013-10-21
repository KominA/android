package com.example.ourfirstproj;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends Activity {

	TextView text;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		text = (TextView) findViewById(R.id.textView1);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void downloadFiles(View v) {
		text.setText("");	
		findViewById(R.id.button1).setEnabled(false);
		
		Handler h = new Handler() {
		      public void handleMessage(android.os.Message msg) {
		    	  
		    	  if (msg.what == 15) 
		    		  findViewById(R.id.button1).setEnabled(true);
		    	  else
		    	  {
		    		  text.append(msg.obj.toString());
		    		  ScrollView vvvv = (ScrollView)findViewById(R.id.scrollView1);
		    		  vvvv.scrollTo(0, 1000000);
		    	  }
		      };
		    };
		
		OurWorker w = new OurWorker(h);
		w.start();
	}

}
