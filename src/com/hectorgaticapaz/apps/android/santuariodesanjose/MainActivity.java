package com.hectorgaticapaz.apps.android.santuariodesanjose;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        
        new AsyncDialog().execute();
    }
    
    public void FinishAsyncDialog() {
		finish();
		startActivity(new Intent(getApplicationContext(), LaMeraMama.class));
    }
    
    private class AsyncDialog extends AsyncTask<Void, Void, Void> {

    	@Override
    	protected Void doInBackground(Void... parms) {
    		try {
//    			Thread.sleep(4000);
    			Thread.sleep(40);
    		} catch (InterruptedException e) {
    			e.printStackTrace();
    		}
    		return null;
    	}
    	
    	@Override
    	protected void onPostExecute(Void result) {
    		FinishAsyncDialog();
    	}

    }

}
