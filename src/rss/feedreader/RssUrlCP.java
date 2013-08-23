package rss.feedreader;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.widget.Toast;

public class RssUrlCP extends ContentProvider {
	
	private RssURLDbAdapter dbAdapter;
	public static final String PROVIDER_NAME = "rss.feedreader.RssUrlCP";
	public static final String uriString = "content://"+ PROVIDER_NAME + "/rss_urls";
	public static final Uri CONTENT_URI = Uri.parse(uriString);
	private static final int RSS_URLS = 1;
	private static final int RSS_URL_ID = 2;
	//declare URI matcher
	private static final UriMatcher uriMatcher;
	static{
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		// content://rss.feedreader.RssUrlCP/rss_urls
		uriMatcher.addURI(PROVIDER_NAME, "rss_urls", RSS_URLS);
		// content://rss.feedreader.RssUrlCP/rss_urls/5
		uriMatcher.addURI(PROVIDER_NAME, "rss_urls/#", RSS_URL_ID);
	}

	
	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		// initialize your data storage adapter
		dbAdapter = new RssURLDbAdapter(getContext());
		dbAdapter.open();
		return (dbAdapter == null) ? false : true;
	}


	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		// return MIME type of your content
		int match = uriMatcher.match(uri);
        switch (match){
            case RSS_URLS:
                return "vnd.android.cursor.dir/rss_urls";
            case RSS_URL_ID:
                return "vnd.android.cursor.item/rss_urls";
            default:
                return null;
        }
    }


	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		if (uriMatcher.match(uri) != RSS_URLS) {
			throw new IllegalArgumentException("Unknown URI " + uri); 
		}
		
		// insert new entry
		long rowId = dbAdapter.insertEntry(values.getAsString("rss_title"),
				values.getAsString("rss_url"),
				values.getAsString("category"));
		// create URI with ID of new entry
		if (rowId > 0) {
			Uri rowUri = ContentUris.withAppendedId(CONTENT_URI, rowId);
			getContext().getContentResolver().notifyChange(rowUri, null);
			return rowUri;
		}
		//TODO: throw exception here
		throw new SQLException("Failed to insert row into " + uri);
	}

	
	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		int count = 0;
		boolean flag = false;
		switch (uriMatcher.match(uri)){
			/*case COUNTRIES:
				//update entries based on selection argument
				count = dbAdapter.update(values, selection, selectionArgs);
				break;*/
			case RSS_URL_ID:
				// update single entry based on ID and selection argument
				String strId = uri.getPathSegments().get(1);
				long id = Long.parseLong(strId);
				//long id = Long.getLong(uri.getPathSegments().get(1)); this one generates nullPointer Exception
					flag = dbAdapter.updateEntry(id, values.getAsString("rss_title"),
							values.getAsString("rss_url"),
							values.getAsString("category"));	
				break;
			default: 
				throw new IllegalArgumentException("Unknown URI " + uri);
		}
		
		getContext().getContentResolver().notifyChange(uri, null);
		if(flag) count = 1;
		return count;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		int count = 0;
		boolean flag = false;
		switch (uriMatcher.match(uri)){
			case RSS_URL_ID:
				// get the id as string
				String strId = uri.getPathSegments().get(1);
				long id = Long.parseLong(strId);
				
				//When trying to convert string to long this doesn't work....strange??
				//long id = Long.getLong(uri.getPathSegments().get(1)); 
				flag = dbAdapter.deleteEntry(id);
				break;
			default: 
				throw new IllegalArgumentException("Unknown URI " + uri);
		}
		
		getContext().getContentResolver().notifyChange(uri, null);
		if(flag) count = 1;
		return count;
	}
	
	
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		Cursor c;
		switch (uriMatcher.match(uri)){
			case RSS_URLS:
				if(sortOrder != null){
					c = dbAdapter.fetchAllEntriesWithsortOrder(sortOrder);
				}
				else{
					//fetch all entries
					c = dbAdapter.fetchAllEntries();
				}
				break;
			case RSS_URL_ID:
				// get the id as string
				String strId = uri.getPathSegments().get(1);
				long id = Long.parseLong(strId);
				
				// fetch single entry based on ID and selection argument
				
				//this approach didn't work as it threw some nullPointer Exception
				//long id = Long.getLong(uri.getPathSegments().get(1));
				c = dbAdapter.fetchEntry(id);
				break;
			default: 
				throw new IllegalArgumentException("Unknown URI " + uri);
		}
	
		c.setNotificationUri(getContext().getContentResolver(), uri);
		
		return c;
	}

}
