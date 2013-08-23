package rss.feedreader;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



/**
 * 
 * A Database Adapter class
 * for urls of rss xml 
 * @author suyesh
 *
 */
public class RssURLDbAdapter {
 	/* table name */
    static final String DATABASE_RSS_TABLE = "tbl_rssXmlURLs";

    /* Column Names for DATABASE_RSS_TABLE: Add and rename when new database needed */
    public static final String KEY_RSS_ROWID = "_id";   // Do not change this one!
    public static final String RSS_TITLE = "rss_title";
    public static final String RSS_URL = "rss_url";
    public static final String CATEGORY = "category";

    
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
    public RssURLDbAdapter(Context ctx) {
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
    public RssURLDbAdapter open() throws SQLException {
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
    public long insertEntry(String rss_title, String rss_url, String category) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(RSS_TITLE, rss_title);
        initialValues.put(RSS_URL, rss_url);
        initialValues.put(CATEGORY, category);

        return db.insert(DATABASE_RSS_TABLE, null, initialValues);
    }

    /**
     * Delete the entry with the given rowId
     * 
     * @param rowId id of entry to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteEntry(long rowId) {
    	String restrict = KEY_RSS_ROWID + "=" + rowId;
        return db.delete(DATABASE_RSS_TABLE, restrict, null) > 0;
    }

    /**
     * Return a Cursor over the list of all entries in the database
     * 
     * @return Cursor over all entries
     */
    public Cursor fetchAllEntries() {
    	String[] projection = new String[] {KEY_RSS_ROWID, RSS_TITLE, RSS_URL, CATEGORY};
    	String restrict = null;   // Get all entries
        return db.query(DATABASE_RSS_TABLE, projection, restrict, null, null, null, null);
    }
    
    /**
     * Return the number of entries in the database
     * 
     * @return Number of all entries
     */
    public int countEntries() {
        return fetchAllEntries().getCount();
    }


    /**
     * Return a Cursor positioned at the entry that matches the given rowId
     * 
     * @param rowId id of entry to retrieve
     * @return Cursor positioned to matching entry, if found
     * @throws SQLException if entry could not be found/retrieved
     */
    public Cursor fetchEntry(long rowId) throws SQLException {
    	String[] projection = new String[] {KEY_RSS_ROWID, RSS_TITLE, RSS_URL, CATEGORY};
    	String restrict = KEY_RSS_ROWID + "=" + rowId;
        Cursor mCursor = db.query(true, DATABASE_RSS_TABLE, projection, restrict, 
        		                        null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    /**
     * Update the entry using the details provided. The entry to be updated is
     * specified using the rowId, and it is altered to use the country and year
     * values passed in
     * 
     * @param rowId id of note to update
     * @param new rss_title value
     * @param new rss_url value
     * @param new category value
     * @return true if the entry was successfully updated, false otherwise
     */
    public boolean updateEntry(long rowId, String rss_title, String rss_url, String category) {
        ContentValues args = new ContentValues();
        args.put(RSS_TITLE, rss_title);
        args.put(RSS_URL, rss_url);
        args.put(CATEGORY, category);

        String restrict = KEY_RSS_ROWID + "=" + rowId;
        return db.update(DATABASE_RSS_TABLE, args, restrict , null) > 0;
    }
    
    
    /**
     * Return a Cursor over the sorted list of all entries in the database
     * @param sortOrder
     * @return Cursor over all the sorted entries
     *  
     */
    
    public Cursor fetchAllEntriesWithsortOrder(String sortOrder) {
    	String[] projection = new String[] {KEY_RSS_ROWID, RSS_TITLE, RSS_URL, CATEGORY};
    	String restrict = null;   // Get all entries
        return db.query(DATABASE_RSS_TABLE, projection, restrict, null, null, null, sortOrder);
    }
    
}
