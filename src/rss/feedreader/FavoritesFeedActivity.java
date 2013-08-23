package rss.feedreader;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TabHost;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

public class FavoritesFeedActivity extends ListActivity{
	public static final String PROVIDER_NAME = "rss.feedreader.RssFavoritesCP";
	public static final String uriString = "content://"+ PROVIDER_NAME + "/rss_favorites";
	public static final Uri CONTENT_URI = Uri.parse(uriString);
	public static final Uri CONTENT_URI1 = Uri.parse(uriString+"/type/");
	//public static final Uri CONTENT_URI2 = Uri.parse(uriString+"/type/#/search");
	public static final Uri CONTENT_URI3 = Uri.parse(uriString+"/search");

	
	private TabHost mTabHost;
	Cursor c;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mTabHost = HelloTabWidget.context.getTabHost();
        mTabHost.setOnTabChangedListener(new TabChangeListener());
        registerForContextMenu(getListView());
        
    }
    
    
    
    private class TabChangeListener implements OnTabChangeListener{

		public void onTabChanged(String tabId) {
			// TODO Auto-generated method stub
			ContentResolver contentResolver = getContentResolver();
			if(RSSReader.rss_id == 0){
	        	c = contentResolver.query(CONTENT_URI, null, null, null, null);
	        }else{
	        	c = contentResolver.query(CONTENT_URI1.buildUpon().
	    				appendPath(String.valueOf(RSSReader.rss_id)).build(), null, null, null, null);
	        }
			
			String[] from = new String[] { RssFavoritesDbAdapter.FEED_TITLE, RssFavoritesDbAdapter.FEED_DATE};
	    	int[] to = new int[] {R.id.title, R.id.pubdate};    	
	    	SimpleCursorAdapter sca = new MySimpleCursorAdapter(FavoritesFeedActivity.this, R.layout.feed_items,
	    			c, from, to);  
	    	setListAdapter(sca);
	    	//sca.notifyDataSetChanged();
			
		}
    	
    }
    
    public void onListItemClick(ListView parent, View v, int position,long id) {
    	Cursor c = getContentResolver().query(CONTENT_URI.buildUpon().
				appendPath(String.valueOf(id)).build(), null, null, null, null);
    	
    	Intent itemintent = new Intent(this,ShowDescription.class);

    	Bundle b = new Bundle();
    	b.putString("title", c.getString(c.getColumnIndex(RssFavoritesDbAdapter.FEED_TITLE)));
    	b.putString("description", c.getString(c.getColumnIndex(RssFavoritesDbAdapter.FEED_DESCRIPTION)));
    	b.putString("link", c.getString(c.getColumnIndex(RssFavoritesDbAdapter.FEED_LINK)));
    	b.putString("pubdate", c.getString(c.getColumnIndex(RssFavoritesDbAdapter.FEED_DATE)));
    	b.putString("imgurl", c.getString(c.getColumnIndex(RssFavoritesDbAdapter.FEED_IMAGE_LINK1)));

    	itemintent.putExtra("android.intent.extra.INTENT", b);

    	startActivity(itemintent);

	}
    
    
    @Override   
    public void onBackPressed() {
    	if(RSSReader.rss_id == 0){
    		finish();
    	}
    	else{
    		//RSSReader.rss_id = 0;
        	startActivity(new Intent(this, HelloTabWidget.class));
            finish();
    	}
    } 
    
    
	/* 
	 * Context Menu Setup and Handling
	 */
	
	private static final int DELETE = Menu.FIRST;
	private static final int DELETE_ALL = Menu.FIRST+1;

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle("Context Menu"); 
        menu.add(Menu.NONE, DELETE, Menu.NONE, "Delete");
        menu.add(Menu.NONE, DELETE_ALL, Menu.NONE, "Delete All");
	}

	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		switch(item.getItemId()) {		
    	case DELETE:
    		//After delete, re(querying) was needed. The SimpleCursorAdapter
    	    //couldn't update the list and the deleted data was also displaying  
	        //until the view was refreshed. This problem was visible only in the
    		//"categorized Favorites" tab and not in the "All Favorite Feeds" tab. 
    		getContentResolver().delete(
	        		CONTENT_URI.buildUpon().appendPath(String.valueOf(info.id)).build(), null, null);
	        if(RSSReader.rss_id == 0){
	        	c = getContentResolver().query(CONTENT_URI, null, null, null, null);
	        }else{
	        	c = getContentResolver().query(CONTENT_URI1.buildUpon().
	    				appendPath(String.valueOf(RSSReader.rss_id)).build(), null, null, null, null);
	        }
			
			String[] from = new String[] { RssFavoritesDbAdapter.FEED_TITLE, RssFavoritesDbAdapter.FEED_DATE};
	    	int[] to = new int[] {R.id.title, R.id.pubdate};    	
	    	SimpleCursorAdapter sca = new MySimpleCursorAdapter(FavoritesFeedActivity.this, R.layout.feed_items,
	    			c, from, to);  
	    	setListAdapter(sca); 
	        return true;
	        
    	case DELETE_ALL:
    		//Didn't need to (re)query after delete in this part
    		if(RSSReader.rss_id == 0){
	        	getContentResolver().delete(CONTENT_URI, null, null);
	        }else{
	        	getContentResolver().delete(CONTENT_URI1.buildUpon().
	    				appendPath(String.valueOf(RSSReader.rss_id)).build(), null, null);
	        }
	        return true;
		}
		
		
		
		return super.onContextItemSelected(item);
	}

    
	/* 
	 * Option Menu Setup and Handling
	 */
	
	 private static final int SEARCH = Menu.FIRST;
	 private static final int REFRESH = Menu.FIRST+1;
		
	 @Override
	 public boolean onCreateOptionsMenu(Menu menu) {
		 menu.add(Menu.NONE, SEARCH, Menu.NONE, "Search Feeds")
		 .setIcon(R.drawable.search)
		 .setAlphabeticShortcut('s');

		 menu.add(Menu.NONE, REFRESH, Menu.NONE, "Refresh Feeds")
		 .setIcon(R.drawable.refresh)
		 .setAlphabeticShortcut('r');
		 
		 return(super.onCreateOptionsMenu(menu));
	 }


	 @Override
	 public boolean onOptionsItemSelected(MenuItem item) {

		 switch (item.getItemId()) {
			 case SEARCH:
				Search();
				return true;
			 case REFRESH:
				ContentResolver contentResolver = getContentResolver();
				if(RSSReader.rss_id == 0){
					c = contentResolver.query(CONTENT_URI, null, null, null, null);
				}else{
					c = contentResolver.query(CONTENT_URI1.buildUpon().
							appendPath(String.valueOf(RSSReader.rss_id)).build(), null, null, null, null);
				}
				
				String[] from = new String[] { RssFavoritesDbAdapter.FEED_TITLE, RssFavoritesDbAdapter.FEED_DATE};
				int[] to = new int[] {R.id.title, R.id.pubdate};    	
				SimpleCursorAdapter sca = new MySimpleCursorAdapter(FavoritesFeedActivity.this, R.layout.feed_items,
						c, from, to);  
				setListAdapter(sca);
				return true;
		 }

		 return(super.onOptionsItemSelected(item));
	 }
	 
	 
	 private void Search() {
		 	LayoutInflater inflater=LayoutInflater.from(this);
			final View addView=inflater.inflate(R.layout.search_layout, null);
			
			new AlertDialog.Builder(this)
				.setTitle("Search By")
				.setView(addView)
				.setPositiveButton("OK", 
						new DialogInterface.OnClickListener() {
					    	public void onClick(DialogInterface dialog,int whichButton) {
					    		// Read alert input 
					    		EditText editSearch =(EditText)addView.findViewById(R.id.editSearchKeyword);
					    		String searchKeyword = editSearch.getText().toString().trim();
					    		searchFeeds(searchKeyword);
					    		
					    	}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int whichButton) {
								// ignore, just dismiss
							}
						})
				.show();

	 }
	 
	
	 private void searchFeeds(String searchKeyword){
		 if(mTabHost.getCurrentTab() == 1){
			 if(RSSReader.rss_id == 0){
		        	c = getContentResolver().query(CONTENT_URI3, null, searchKeyword, null, null);
		        }else{
		        	c = getContentResolver().query(CONTENT_URI1.buildUpon().
		    				appendPath(String.valueOf(RSSReader.rss_id))
		    				.appendPath("search").build(), null, searchKeyword, null, null);
		        }
				
				String[] from = new String[] { RssFavoritesDbAdapter.FEED_TITLE, RssFavoritesDbAdapter.FEED_DATE};
		    	int[] to = new int[] {R.id.title, R.id.pubdate};    	
		    	SimpleCursorAdapter sca = new MySimpleCursorAdapter(FavoritesFeedActivity.this, R.layout.feed_items,
		    			c, from, to);  
		    	setListAdapter(sca);
		 }
		 
	 }
    
	 private class MySimpleCursorAdapter  extends SimpleCursorAdapter{

		 public MySimpleCursorAdapter(Context context, int layout, Cursor c,
				 String[] from, int[] to) {
			 // TODO Auto-generated constructor stub
			 super(context, layout, c, from, to);
		 }

		 //int counter=0;
		 @Override   // Called when updating the ListView
		 public View getView(int position, View convertView, ViewGroup parent) {
			 /* Reuse super handling ==> A TextView from R.layout.list_item */
			 View v =  super.getView(position,convertView,parent);
			 String title; 

			 TextView tTitle = (TextView) v.findViewById(R.id.title);
			 title = tTitle.getText().toString();
			 if (title.length() > 42){
				 title = title.substring(0, 42) + "...";
			 }

			 tTitle.setText(title);

			 return v;
		 }
	 }
    
}
