package com.jmbdh;



import java.util.HashMap;

import android.app.Activity;

import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

public class AnalyticsHelper {
	public Activity activity=null;
	public static final String GA_PROPERTY_ID = "UA-44819370-1";
	public static GoogleAnalytics mGa=null; 
	public static Tracker mTracker=null;
	public AnalyticsHelper(Activity activity){
		this.activity=activity;
		mGa = GoogleAnalytics.getInstance(activity);
	    mTracker = mGa.getTracker(GA_PROPERTY_ID);
		HashMap<String, String> hitParameters = new HashMap<String, String>();
		hitParameters.put(Fields.HIT_TYPE, "appview");
		hitParameters.put(Fields.SCREEN_NAME, activity.getClass().getName());
		hitParameters.put(Fields.APP_VERSION, "1.0");
		mTracker.send(hitParameters);
	}
//	private void initializeGa() {
//	    mGa = GoogleAnalytics.getInstance(activity);
//	    mTracker = mGa.getTracker(GA_PROPERTY_ID);
//	    HashMap<String, String> hitParameters = new HashMap<String, String>();
//		hitParameters.put(Fields.HIT_TYPE, "appview");
//		hitParameters.put(Fields.SCREEN_NAME, "Home Screen");
//
//		mTracker.send(hitParameters);
//	  }
}
