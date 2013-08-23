package rss.feedreader;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;



/**
 * 
 * A Database Adapter class
 * for favorite of rss feeds 
 * @author suyesh
 *
 */
public class RssFavoritesDbAdapter {
	/* table name */
    static final String DATABASE_FEED_TABLE = "tbl_rssFavorites";

    /* Column Names for DATABASE_FEED_TABLE: Add and rename when new database needed */
    public static final String KEY_FEED_ROWID = "_id";   // Do not change this one!
    public static final String RSS_ID = "rss_id";
    public static final String FEED_TITLE = "feed_title";
    public static final String FEED_DATE = "feed_date";
    public static final String FEED_DESCRIPTION = "feed_description";
    public static final String FEED_LINK = "feed_link";
    public static final String FEED_IMAGE_LINK1= "feed_image_link1";
    //public static final String FEED_IMAGE_LINK2 = "feed_image_link2";
 
    /* Internal DB Classes: Do not change */
    private DbAdapter dbHelper;
    private SQLiteDatabase db;
    
    /* Calling Activity: Do not change */
    private final Context mCtx;
    
    


    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     * 
     * @param ctx the Context within which to work
     */
    public RssFavoritesDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open the rssFeedReader database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     * 
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public RssFavoritesDbAdapter open() throws SQLException {
        dbHelper = new DbAdapter(mCtx);
        db = dbHelper.getWritableDatabase();
        return this;
    }
    
    public void close() {
        dbHelper.close();
    }

    /* ************************************************
     * 
     * My Adapter User Interface: Change only parts related
     * to columns () 
     * 
     * ***********************************************/
    
    /**
     * Create a new DB entry using the rss_url and category provided. If the entry is
     * successfully created return the new rowId for that entry, otherwise return
     * a -1 to indicate failure.
     * 
     * @param rss_title
     * @param rss_url
     * @param category
     * @return rowId or -1 if failed
     */
    public long insertEntry(ContentValues values) {
        ContentValues initialValues = new ContentValues();
        
        initialValues.put(RSS_ID, values.getAsString(RSS_ID));
        initialValues.put(FEED_TITLE, values.getAsString(FEED_TITLE));
        initialValues.put(FEED_DATE, values.getAsString(FEED_DATE));
        initialValues.put(FEED_DESCRIPTION, values.getAsString(FEED_DESCRIPTION));
        initialValues.put(FEED_LINK, values.getAsString(FEED_LINK));
        initialValues.put(FEED_IMAGE_LINK1, values.getAsString(FEED_IMAGE_LINK1));

        return db.insert(DATABASE_FEED_TABLE, null, initialValues);
    }

    //select tbl_rssfavorites._id,tbl_rssfavorites.rss_id, tbl_rssfavorites.feed_title, tbl_rssxmlurls._id,tbl_rssxmlurls.rss_title from tbl_rssfavorites, tbl_rssxmlurls
    //where tbl_rssfavorites.rss_id= tbl_rssxmlurls._id and  tbl_rssfavorites.rss_id = 3
    
    /**
     * Return a Cursor over the list of all entries in the table
     * 
     * @return Cursor over all entries
     */
    public Cursor fetchAllEntries() {
    	String[] projection = new String[] {KEY_FEED_ROWID, RSS_ID, FEED_TITLE, FEED_DATE, FEED_DESCRIPTION, FEED_LINK, FEED_IMAGE_LINK1};
    	String restrict = null;   // Get all entries
        return db.query(DATABASE_FEED_TABLE, projection, restrict, null, null, null, null);
    }
    
    
    /**
     * Return a Cursor over the list of all entries in the table
     * with the matched searchKeyword
     * @param searchKeyword to be searched
     * @return Cursor over the list of matched entries
     */
    public Cursor fetchSearchFromAllFavorites(String searchKeyword) {
    	String[] projection = new String[] {KEY_FEED_ROWID, RSS_ID, FEED_TITLE, FEED_DATE, FEED_DESCRIPTION, FEED_LINK, FEED_IMAGE_LINK1};
    	String restrict = FEED_TITLE +" LIKE '%"+searchKeyword+"%'  OR " 
        + FEED_TITLE +" LIKE '%"+    searchKeyword.toLowerCase()+"%'    OR  " 
		+ FEED_TITLE +" LIKE '%"+    searchKeyword.toUpperCase()+"%'    OR  "
		+ FEED_DESCRIPTION +" LIKE '%"+searchKeyword+"%'     OR      "
		+ FEED_DESCRIPTION +" LIKE '%"+searchKeyword.toLowerCase()+"%'   OR   "
		+ FEED_DESCRIPTION +" LIKE '%"+searchKeyword.toUpperCase()+"%'";
        return db.query(DATABASE_FEED_TABLE, projection, restrict, null, null, null, null);
    }
    
    
    
    /**
     * Return a Cursor over the list of all entries in the table
     * with the given rowId and matched searchKeyword
     * @param rowId 
     * @param searchKeyword to be searched
     * @return Cursor over the list of matched entries
     */
    public Cursor fetchSearchFromTypeFavorites(long rowId, String searchKeyword) {
    	String[] projection = new String[] {KEY_FEED_ROWID, RSS_ID, FEED_TITLE, FEED_DATE, FEED_DESCRIPTION, FEED_LINK, FEED_IMAGE_LINK1};
    	String restrict = RSS_ID + "=" + rowId+"  AND ( " +FEED_TITLE +" LIKE '%"+searchKeyword+"%'  OR " 
        + FEED_TITLE +" LIKE '%"+    searchKeyword.toLowerCase()+"%'    OR  " 
		+ FEED_TITLE +" LIKE '%"+    searchKeyword.toUpperCase()+"%'    OR  "
		+ FEED_DESCRIPTION +" LIKE '%"+searchKeyword+"%'     OR      "
		+ FEED_DESCRIPTION +" LIKE '%"+searchKeyword.toLowerCase()+"%'   OR   "
		+ FEED_DESCRIPTION +" LIKE '%"+searchKeyword.toUpperCase()+"%' )";
    	
        return db.query(DATABASE_FEED_TABLE, projection, restrict, null, null, null, null);
    }
    
    
    /**
     * Return a Cursor positioned at the entry that matches the given rowId
     * 
     * @param rowId id of entry to retrieve
     * @return Cursor positioned to matching entry, if found
     * @throws SQLException if entry could not be found/retrieved
     */
    public Cursor fetchEntry(long rowId) throws SQLException {
    	String[] projection = new String[] {KEY_FEED_ROWID, RSS_ID, FEED_TITLE, FEED_DATE, FEED_DESCRIPTION, FEED_LINK, FEED_IMAGE_LINK1};
    	String restrict = KEY_FEED_ROWID + "=" + rowId;
        Cursor mCursor = db.query(true, DATABASE_FEED_TABLE, projection, restrict, 
        		                        null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    
    
    /**
     * Return a Cursor positioned at the entry that matches the given rowId
     * 
     * @param rowId id of entry to retrieve
     * @return Cursor positioned to matching entry, if found
     * @throws SQLException if entry could not be found/retrieved
     */
    public Cursor fetchFavoritesType(long rowId) throws SQLException {
    	String[] projection = new String[] {KEY_FEED_ROWID, RSS_ID, FEED_TITLE, FEED_DATE, FEED_DESCRIPTION, FEED_LINK, FEED_IMAGE_LINK1};
    	String restrict = RSS_ID + "=" + rowId;
        Cursor mCursor = db.query(true, DATABASE_FEED_TABLE, projection, restrict, 
        		                        null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    
    
    /**
     * Delete the entry with the given rowId
     * 
     * @param rowId id of entry to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteEntry(long rowId) {
    	String restrict = KEY_FEED_ROWID + "=" + rowId;
        return db.delete(DATABASE_FEED_TABLE, restrict, null) > 0;
    }
    
    
    
    /**
     * Delete all the entries
     * in the table
     * 
     * @return the number of rows affected, 0 otherwise
     */
    public int deleteAllEntries() {
        return db.delete(DATABASE_FEED_TABLE, "1", null);
    }
    
    
    /**
     * Delete the entries with the given rowId
     * 
     * @param rowId id of entries to delete
     * @return the number of rows affected, 0 otherwise
     */
    public int deleteFavoritesType(long rowId) {
    	String restrict = RSS_ID + "=" + rowId;
        return db.delete(DATABASE_FEED_TABLE, restrict, null);
    }
    
}
