package rss.feedreader;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;

public class RssFavoritesCP extends ContentProvider {
	
	private RssFavoritesDbAdapter dbAdapter;
	public static final String PROVIDER_NAME = "rss.feedreader.RssFavoritesCP";
	public static final String uriString = "content://"+ PROVIDER_NAME + "/rss_favorites";
	public static final Uri CONTENT_URI = Uri.parse(uriString);
	public static final Uri CONTENT_URI1 = Uri.parse(uriString+"/type");
	//public static final Uri CONTENT_URI2 = Uri.parse(uriString+"/type/#/search");
	public static final Uri CONTENT_URI3 = Uri.parse(uriString+"/search");
	private static final int RSS_FAVORITES = 1;
	private static final int RSS_FAVORITE_ID = 2;
	private static final int RSS_FAVORITE_TYPE = 3;
	private static final int RSS_FAVORITE_TYPE_SEARCH = 4;
	private static final int RSS_FAVORITE_SEARCH = 5;
	//declare URI matcher
	private static final UriMatcher uriMatcher;
	static{
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		// content://rss.feedreader.RssFavoritesCP/rss_favorites
		uriMatcher.addURI(PROVIDER_NAME, "rss_favorites", RSS_FAVORITES);
		// content://rss.feedreader.RssFavoritesCP/rss_favorites/5
		uriMatcher.addURI(PROVIDER_NAME, "rss_favorites/#", RSS_FAVORITE_ID);
		// content://rss.feedreader.RssFavoritesCP/rss_favorites/type/5		
		uriMatcher.addURI(PROVIDER_NAME, "rss_favorites/type/#", RSS_FAVORITE_TYPE);
		// content://rss.feedreader.RssFavoritesCP/rss_favorites/type/#/search	
		uriMatcher.addURI(PROVIDER_NAME, "rss_favorites/type/#/search/", RSS_FAVORITE_TYPE_SEARCH);
		// content://rss.feedreader.RssFavoritesCP/rss_favorites/search		
		uriMatcher.addURI(PROVIDER_NAME, "rss_favorites/search", RSS_FAVORITE_SEARCH);
	}

	
	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		// initialize your data storage adapter
		dbAdapter = new RssFavoritesDbAdapter(getContext());
		dbAdapter.open();
		return (dbAdapter == null) ? false : true;
	}


	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		int count = 0;
		boolean flag = false;
		switch (uriMatcher.match(uri)){
			case RSS_FAVORITE_ID:
				// get the id as string
				String strId = uri.getPathSegments().get(1);
				long id = Long.parseLong(strId);
				
				//When trying to convert string to long this doesn't work....strange??
				//long id = Long.getLong(uri.getPathSegments().get(1)); 
				
				flag = dbAdapter.deleteEntry(id);
				if(flag) count = 1;
				break;
			
			case RSS_FAVORITES:
				//fetch all entries
				count = dbAdapter.deleteAllEntries();
				break;
				
			case RSS_FAVORITE_TYPE:
				String strId1 = uri.getPathSegments().get(2);
				long rss_id = Long.parseLong(strId1);
				count = dbAdapter.deleteFavoritesType(rss_id);
				break;
				
			default: 
				throw new IllegalArgumentException("Unknown URI " + uri);
		}
		
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}


	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		if (uriMatcher.match(uri) != RSS_FAVORITES) {
			throw new IllegalArgumentException("Unknown URI " + uri); 
		}
		
		// insert new entry
		long rowId = dbAdapter.insertEntry(values);
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
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		Cursor c;
		switch (uriMatcher.match(uri)){
			case RSS_FAVORITES:
				//fetch all entries
				c = dbAdapter.fetchAllEntries();
				break;
			case RSS_FAVORITE_ID:
				// get the id as string
				String strId = uri.getPathSegments().get(1);
				long id = Long.parseLong(strId);
				// fetch single entry based on ID and selection argument
				
				//this approach didn't work as it threw some nullPointer Exception
				//long id = Long.getLong(uri.getPathSegments().get(1));
				c = dbAdapter.fetchEntry(id);
				break;
			case RSS_FAVORITE_TYPE:
				String strId1 = uri.getPathSegments().get(2);
				long rss_id = Long.parseLong(strId1);
				c = dbAdapter.fetchFavoritesType(rss_id);
				break;
			case RSS_FAVORITE_SEARCH:
				c = dbAdapter.fetchSearchFromAllFavorites(selection);
				break;
			case RSS_FAVORITE_TYPE_SEARCH:
				String strId2 = uri.getPathSegments().get(2);
				long rss_id1 = Long.parseLong(strId2);
				c = dbAdapter.fetchSearchFromTypeFavorites(rss_id1, selection);
				break;
			default: 
				throw new IllegalArgumentException("Unknown URI " + uri);
		}
	
		c.setNotificationUri(getContext().getContentResolver(), uri);
		
		return c;
	}


	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}




}
