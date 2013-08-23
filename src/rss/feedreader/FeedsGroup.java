package rss.feedreader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SimpleAdapter;

public class FeedsGroup extends ActivityGroup {

        // Keep this in a static variable to make it accessible for all the nested activities, lets them manipulate the view
	public static FeedsGroup group;

        // Need to keep track of the history if you want the back-button to work properly, don't use this if your activities requires a lot of memory.
	private ArrayList<View> history;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);
	      this.history = new ArrayList<View>();
	      group = this;

              // Start the root activity withing the group and get its view
	      View view = getLocalActivityManager().startActivity("feeds_activity", new
	    	      							Intent(this,RSSFeedReaderActivity.class)
	    	      							.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
	    	                                .getDecorView();

              // Replace the view of this ActivityGroup
	      replaceView(view);

	   }

	public void replaceView(View v) {
                // Adds the old one to history
		history.add(v);
                // Changes this Groups View to the new View.
		setContentView(v);
	}

	public void back() {
		if(history.size() > 0) {
			history.remove(history.size()-1);
			if(history.size() > 0){
				setContentView((View)history.get(history.size()-1));
			}
			else{
				finish();
			}
		}else {
			finish();
		}
	}

   @Override
    public void onBackPressed() {
	   FeedsGroup.group.back();
       return;
    }

    /*private static final int ADD_FEED_URL = Menu.FIRST;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, ADD_FEED_URL, Menu.NONE, "Add New RSS Feed URL")
				.setIcon(R.drawable.add)
				.setAlphabeticShortcut('a');
		
		return(super.onCreateOptionsMenu(menu));
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
			case ADD_FEED_URL:
	    		Intent intent = new Intent(FeedsGroup.this,AddURL.class);
	    		FeedsGroup.this.startActivity(intent);
				return true;
		}

		return(super.onOptionsItemSelected(item));
	}*/
}
