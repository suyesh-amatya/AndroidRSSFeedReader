package rss.feedreader;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.*;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener; 
import android.util.Log;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;

import org.xml.sax.XMLReader;



import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;



public class RSSReader extends Activity implements OnItemClickListener{
	public static final String PROVIDER_NAME = "rss.feedreader.RssFavoritesCP";
	public static final String uriString = "content://"+ PROVIDER_NAME + "/rss_favorites";
	public static final Uri CONTENT_URI = Uri.parse(uriString);
	
	public String RSSFEEDOFCHOICE = null;
	public static int rss_id;
	public final String tag = "RSSReader";
	private RSSFeed feed = null;
	private RSSSearchFeed searchFeed = null;
	private boolean searchFlag = false;
	
	private TabHost mTabHost;
	private TextView feedtitle;
	private TextView feedpubdate;
	private ListView itemlist;
	//private RSSListAdaptor<RSSItem> adapter;
	//ArrayAdapter<RSSItem> adapter;
	SimpleAdapter adapter;
	
	/** Called when the activity is first created. */
	
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.list_items);
        
        
        String tab_title = getIntent().getStringExtra("tab_title");
        String tab_title_fav = getIntent().getStringExtra("tab_title_fav");
        rss_id = safeLongToInt(getIntent().getLongExtra("id", 0));
        
        mTabHost=HelloTabWidget.context.getTabHost();
        //instead of using android.R.id.title we could have also defined our own 
        //TextView for tab title.
        ((TextView)mTabHost.getTabWidget().getChildAt(0)
        		.findViewById(android.R.id.title)).setText(tab_title);

        ((TextView)mTabHost.getTabWidget().getChildAt(1)
        		.findViewById(android.R.id.title)).setText(tab_title_fav);
        
        RSSFEEDOFCHOICE = getIntent().getStringExtra("url");
        feedtitle = (TextView) findViewById(R.id.feedtitle);
        feedpubdate = (TextView) findViewById(R.id.feedpubdate);
        itemlist = (ListView) findViewById(R.id.itemlist);
        
        // display UI
        new UpdateDisplay().execute();
        //UpdateDisplay();
        
        registerForContextMenu(itemlist);
    }

    
    private RSSFeed getFeed(String urlToRssFeed)
    {
    	try
    	{
    		// setup the url
    	   URL url = new URL(urlToRssFeed);

           // create the factory
           SAXParserFactory factory = SAXParserFactory.newInstance();
           // create a parser
           SAXParser parser = factory.newSAXParser();

           // create the reader (scanner)
           XMLReader xmlreader = parser.getXMLReader();
           // instantiate our handler
           RSSHandler theRssHandler = new RSSHandler();
           // assign our handler
           xmlreader.setContentHandler(theRssHandler);
           // get our data via the url class
           InputSource is = new InputSource(url.openStream());
           // perform the synchronous parse           
           xmlreader.parse(is);
           // get the results - should be a fully populated RSSFeed instance, or null on error
           return theRssHandler.getFeed();
    	}
    	catch (Exception ee)
    	{
    		// if we have a problem, simply return null
    		return null;
    	}
    }
 
    
    
    private class UpdateDisplay extends AsyncTask<Void, Void, Void>{
        private ProgressDialog progress = null;
        
        @Override
        protected Void doInBackground(Void... params) {
        	// go get our feed!
        	feed = getFeed(RSSFEEDOFCHOICE);
        	if(feed != null){
        		String[] from = new String[] {"title", "date"};
        		int[] to = new int[]{R.id.title, R.id.pubdate};
        		Iterator<RSSItem> i = feed.getAllItems().iterator();
        		List<HashMap<String, String>> fillMaps = new ArrayList<HashMap<String, String>>();
        		while (i.hasNext()){
        			HashMap<String, String> map = new HashMap<String, String>();
        			RSSItem item =  (RSSItem) i.next();
        			String title = item.getTitle();
        			String date = item.getPubDate();
        			if (title.length() > 42){
        				title = title.substring(0, 42) + "...";
        			}
        			map.put("title", title);
        			map.put("date", date);
        			fillMaps.add(map);
        		}
        		adapter = new SimpleAdapter(RSSReader.this, fillMaps, R.layout.feed_items, from, to);
        		//adapter = new ArrayAdapter<RSSItem>(RSSReader.this,android.R.layout.simple_list_item_1,feed.getAllItems());
        	}
        	return null;
        }

        @Override
        protected void onCancelled() {
        	super.onCancelled();
        }

        @Override
        protected void onPreExecute() {
        	//getParent() can also be used as an alternative for context
        	progress = ProgressDialog.show(
        			HelloTabWidget.context, null, "Loading RSS Feeds...");

        	super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void result) {
	        if (feed == null){
	        	feedtitle.setText("No RSS Feed Available. Check the Feed URL and Internet Connection!");
	        	progress.dismiss();
	        }
	        else{
	        	feedtitle.setText(feed.getHead_title());
		        feedpubdate.setText(feed.getLastBuildDate());
		        itemlist.setAdapter(adapter);

	        	

	        	itemlist.setOnItemClickListener(RSSReader.this);

	        	itemlist.setSelection(0);

	        	progress.dismiss();

	        	super.onPostExecute(result);
	        }
	
	        
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        	super.onProgressUpdate(values);
        }
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
			 new UpdateDisplay().execute();
			 return true;
		 }

		 return(super.onOptionsItemSelected(item));
	 }
    
   
	 private void Search() {
		 	LayoutInflater inflater=LayoutInflater.from(this);
			final View addView=inflater.inflate(R.layout.search_layout, null);
			////using this doesn't work, so we have to use getParent() or HelloTabWidget.context as our context here.
			new AlertDialog.Builder(getParent())
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
		 if(mTabHost.getCurrentTab() == 0 && RSSReader.rss_id != 0){
			 if(feed != null){
	        		String[] from = new String[] {"title", "date"};
	        		int[] to = new int[]{R.id.title, R.id.pubdate};
	        		Iterator<RSSItem> i = feed.getAllItems().iterator();
	        		List<HashMap<String, String>> fillMaps = new ArrayList<HashMap<String, String>>();
	        		searchFeed = new RSSSearchFeed();
	        		while (i.hasNext()){
	        			HashMap<String, String> map = new HashMap<String, String>();
	        			RSSItem item =  (RSSItem) i.next();
	        			String titleToDisplay = null;
	        			String title = item.getTitle();
	        			String description = item.getDescription();
	        			String date = item.getPubDate();
	        			String link = item.getLink();
	        			String imgLink = item.get_imgurl();
	        			if(title.toLowerCase().contains(searchKeyword.toLowerCase())
	        					|| description.toLowerCase().contains(searchKeyword.toLowerCase())){
	        				if (title.length() > 42){
		        				titleToDisplay = title.substring(0, 42) + "...";
		        			}
	        				else{
	        					titleToDisplay = title;
	        				}
	        					
		        			map.put("title", titleToDisplay);
		        			map.put("date", date);
		        			fillMaps.add(map);
		        			
		        			RSSSearchItem searchItem = new RSSSearchItem();
		        			searchItem.setTitle(title);
		        			searchItem.setDescription(description);
		        			searchItem.setPubDate(date);
		        			searchItem.setLink(link);
		        			searchItem.set_imgurl(imgLink);
		        			searchFeed.addSearchItem(searchItem);
		        			searchFlag = true;
	        			}
	        			
	        		}
	        		adapter = new SimpleAdapter(RSSReader.this, fillMaps, R.layout.feed_items, from, to);
	        		itemlist.setAdapter(adapter);
			 }
		 }
		 
	 }
	 
	 
	/* 
	 * Context Menu Setup and Handling
	 */
	
	private static final int FAVORITES = Menu.FIRST;
	

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle("Context Menu"); 
        menu.add(Menu.NONE, FAVORITES, Menu.NONE, "Add to Favorites");
	}

	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		switch(item.getItemId()) {		
    	case FAVORITES:
    		ContentValues insertValues = new ContentValues();    		
    		int loc = RSSReader.safeLongToInt(info.id);
    		if(searchFlag){
    			insertValues.put(RssFavoritesDbAdapter.RSS_ID, getIntent().getLongExtra("id", 0));
        		insertValues.put(RssFavoritesDbAdapter.FEED_TITLE, searchFeed.getSearchItem(loc).getTitle());
        		insertValues.put(RssFavoritesDbAdapter.FEED_DATE, searchFeed.getSearchItem(loc).getPubDate());
        		insertValues.put(RssFavoritesDbAdapter.FEED_DESCRIPTION, searchFeed.getSearchItem(loc).getDescription());
        		insertValues.put(RssFavoritesDbAdapter.FEED_LINK, searchFeed.getSearchItem(loc).getLink());
        		insertValues.put(RssFavoritesDbAdapter.FEED_IMAGE_LINK1, searchFeed.getSearchItem(loc).get_imgurl());
    		}
    		else{
    			insertValues.put(RssFavoritesDbAdapter.RSS_ID, getIntent().getLongExtra("id", 0));
        		insertValues.put(RssFavoritesDbAdapter.FEED_TITLE, feed.getItem(loc).getTitle());
        		insertValues.put(RssFavoritesDbAdapter.FEED_DATE, feed.getItem(loc).getPubDate());
        		insertValues.put(RssFavoritesDbAdapter.FEED_DESCRIPTION, feed.getItem(loc).getDescription());
        		insertValues.put(RssFavoritesDbAdapter.FEED_LINK, feed.getItem(loc).getLink());
        		insertValues.put(RssFavoritesDbAdapter.FEED_IMAGE_LINK1, feed.getItem(loc).get_imgurl());
    		}
    		
    		getContentResolver().insert(
	        		CONTENT_URI.buildUpon().build(), insertValues);
 	        
	        return true;
	        
		}
		
		return super.onContextItemSelected(item);
	}
    
    
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
    	Log.i(tag,"item clicked! [" + feed.getItem(position).getTitle() + "]");

    	Intent itemintent = new Intent(this,ShowDescription.class);

    	Bundle b = new Bundle();
    	if(searchFlag){
    		b.putString("title", searchFeed.getSearchItem(position).getTitle());
        	b.putString("description", searchFeed.getSearchItem(position).getDescription());
        	b.putString("link", searchFeed.getSearchItem(position).getLink());
        	b.putString("pubdate", searchFeed.getSearchItem(position).getPubDate());
        	b.putString("imgurl", searchFeed.getSearchItem(position).get_imgurl());
    	}
    	else{
    		b.putString("title", feed.getItem(position).getTitle());
        	b.putString("description", feed.getItem(position).getDescription());
        	b.putString("link", feed.getItem(position).getLink());
        	b.putString("pubdate", feed.getItem(position).getPubDate());
        	b.putString("imgurl", feed.getItem(position).get_imgurl());
    	}
    	

    	//Toast.makeText(this, feed.getItem(position).get_imgurl(), 2000).show();

    	itemintent.putExtra("android.intent.extra.INTENT", b);

    	startActivity(itemintent);
    }
    
    
    @Override   
    public void onBackPressed() {
    	FeedsGroup.group.back();   
        ((TextView)mTabHost.getTabWidget().getChildAt(0).
        		findViewById(android.R.id.title)).setText("Feed URLs");
        ((TextView)mTabHost.getTabWidget().getChildAt(1).
        		findViewById(android.R.id.title)).setText("All Favorite Feeds");
        rss_id = 0;
    } 

 
    public static int safeLongToInt(long l) { 
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) { 
            throw new IllegalArgumentException 
                (l + " cannot be cast to int without changing its value."); 
        } 
        return (int) l; 
    }
}