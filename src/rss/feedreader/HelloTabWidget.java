package rss.feedreader;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

public class HelloTabWidget extends TabActivity{
	public static HelloTabWidget context;//we can also use Context
	
	public void onCreate(Bundle savedInstanceState) { 
		context = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		Resources res = getResources(); // Resource object to get Drawables    
		TabHost tabHost = getTabHost();  // The activity TabHost    
		TabHost.TabSpec spec;  // Resusable TabSpec for each tab    
		Intent intent;  // Reusable Intent for each tab
		
		// Create an Intent to launch an Activity for the tab (to be reused)
		intent = new Intent().setClass(this, FeedsGroup.class);		
		// Initialize a TabSpec for each tab and add it to the TabHost
		spec = tabHost.newTabSpec("feeds_group").setIndicator("Feed URLs",
									res.getDrawable(R.drawable.ic_tab_artists_grey))
					  .setContent(intent);
		tabHost.addTab(spec);
		
		
		
		// Do the same for the other tabs
		intent = new Intent().setClass(this, FavoritesFeedActivity.class);
		spec = tabHost.newTabSpec("favorites_activity").setIndicator("All Favorite Feeds",
									res.getDrawable(R.drawable.ic_tab_artists_grey))
					  .setContent(intent);
		tabHost.addTab(spec);
		
		if(RSSReader.rss_id == 0){
			tabHost.setCurrentTab(0);
		}
		else{
			RSSReader.rss_id = 0;
			tabHost.setCurrentTab(1);
		}
	}

	
	
}
