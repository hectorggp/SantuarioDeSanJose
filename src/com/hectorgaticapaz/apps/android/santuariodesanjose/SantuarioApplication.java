package com.hectorgaticapaz.apps.android.santuariodesanjose;

import com.parse.Parse;

import android.app.Application;

public class SantuarioApplication extends Application {
	
	@Override
	public void onCreate() {
		super.onCreate();
		Parse.initialize(getApplicationContext(), "UZEiwQ4SBWuNsARu2QghfJUzYvwjUB0gfc7Svjjf", "5QTEkubn7Io9GS9Dek2pOCHulL0nODLJfq2OmirL");
	}

}
