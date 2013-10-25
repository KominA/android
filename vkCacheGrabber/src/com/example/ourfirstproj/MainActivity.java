package com.example.ourfirstproj;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends Activity {

	TextView textViewInfo;
	Button buttonCopy;
	ScrollView scrollViewInfo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		textViewInfo = (TextView) findViewById(R.id.textViewInfo);
		scrollViewInfo=(ScrollView)findViewById(R.id.scrollViewInfo);
		buttonCopy=(Button)findViewById(R.id.copy_music);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void downloadFiles(View v) {	
		textViewInfo.setText("");
		buttonCopy.setEnabled(false);
		
		Handler h = new Handler() {
		      public void handleMessage(android.os.Message msg) {
		    	  
		    	  if (msg.what == 15)
		    		  buttonCopy.setEnabled(true);
		    	  else
		    	  {
		    		  textViewInfo.append(msg.obj.toString());
		    		  scrollViewInfo.scrollTo(0, 1000000);
		    	  }
		      };
		    };
		
		OurWorker w = new OurWorker(h);
		w.start();
	}

}
