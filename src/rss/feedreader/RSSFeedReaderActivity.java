package rss.feedreader;

import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;


public class RSSFeedReaderActivity extends ListActivity {
	public static final String PROVIDER_NAME = "rss.feedreader.RssUrlCP";
	public static final String uriString = "content://"+ PROVIDER_NAME + "/rss_urls";
	public static final Uri CONTENT_URI = Uri.parse(uriString);
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
        
        ContentResolver contentResolver = getContentResolver();

    	Cursor c = contentResolver.query(CONTENT_URI, null, null, null, null); 
    	String[] from = new String[] { "_id", "rss_title", "rss_url" };
    	int[] to = new int[] { R.id.id, R.id.title, R.id.url};    	
    	SimpleCursorAdapter sca = new MySimpleCursorAdapter(this, R.layout.url_list,
    			c, from, to);  
    	setListAdapter(sca);
    	
    	registerForContextMenu(getListView());
    }
    
    
    public void onListItemClick(ListView parent, View v, int position,long id) {
    	//super.onListItemClick(parent, v, position, id);
    	Cursor c = getContentResolver().query(CONTENT_URI.buildUpon().
				appendPath(String.valueOf(id)).build(), null, null, null, null);
    	
    	String rss_url = c.getString(c.getColumnIndex("rss_url"));
    	String rss_title = c.getString(c.getColumnIndex("rss_title"));
    	
    	Intent intent = new Intent(this,RSSReader.class);
		intent.putExtra("id", id);
    	intent.putExtra("url", rss_url);
		intent.putExtra("tab_title", rss_title);
		intent.putExtra("tab_title_fav", rss_title+" Favorites");
		//startActivity(intent);
		
		
		// Create the view using FeedsGroup's LocalActivityManager
        View view = FeedsGroup.group.getLocalActivityManager()
        .startActivity("rss_reader", intent
        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        .getDecorView();

        // Again, replace the view
        FeedsGroup.group.replaceView(view);

	}
    
    class MySimpleCursorAdapter  extends SimpleCursorAdapter{
    	
		public MySimpleCursorAdapter(Context context, int layout, Cursor c,
				String[] from, int[] to) {
			super(context, layout, c, from, to);
			// TODO Auto-generated constructor stub
		}

		//int counter=0;
		@Override   // Called when updating the ListView
		public View getView(int position, View convertView, ViewGroup parent) {
			/* Reuse super handling ==> A TextView from R.layout.list_item */
			View v =  super.getView(position,convertView,parent);
			TextView tId = (TextView) v.findViewById(R.id.id);
			String counter = Integer.toString(position+1);
			tId.setText(counter);
			
			return v;
		}
	}
    

	/* 
	 * Option Menu Setup and Handling
	 */
    private static final int ADD_FEED_URL = Menu.FIRST;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, ADD_FEED_URL, Menu.NONE, "Add New RSS Feed URL")
				.setIcon(R.drawable.add)
				.setAlphabeticShortcut('a');
		
		return(super.onCreateOptionsMenu(menu));
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
			case ADD_FEED_URL:
				/* Start new Activity */
	    		Intent intent = new Intent(RSSFeedReaderActivity.this,AddURL.class);
	    		RSSFeedReaderActivity.this.startActivity(intent);
				return true;
		}

		return(super.onOptionsItemSelected(item));
	}

	
	
	/* 
	 * Context Menu Setup and Handling
	 */
	
	private static final int DELETE = Menu.FIRST;
	private static final int UPDATE = Menu.FIRST+1;

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle("Context Menu"); 
        menu.add(Menu.NONE, DELETE, Menu.NONE, "Delete");
        menu.add(Menu.NONE, UPDATE, Menu.NONE, "Update");
	}

	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		
		switch(item.getItemId()) {
    	case DELETE:
    		//System.out.println(info.id);
    		if(info.id == 1){
    			Toast.makeText(RSSFeedReaderActivity.this,
						"This is the Default FEED URL. You cannot delete it!", Toast.LENGTH_LONG).show();
    		}
    		else{
    	        getContentResolver().delete(
    	        		CONTENT_URI.buildUpon().appendPath(String.valueOf(info.id)).build(), null, null);
    			
    		}
 	        
	        return true;
	        
    	case UPDATE:
    		
    		if(info.id == 1){
    			Toast.makeText(RSSFeedReaderActivity.this,
						"This is the Default FEED URL. You cannot update it!", Toast.LENGTH_LONG).show();
    		}
    		else{
        		Intent intent = new Intent(RSSFeedReaderActivity.this,AddURL.class);
        		intent.putExtra("id", info.id);
        		startActivity(intent);
    		}
	        return true;
		}
		
		
		
		return super.onContextItemSelected(item);
	}
}