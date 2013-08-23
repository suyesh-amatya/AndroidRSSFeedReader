package rss.feedreader;


import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;



/**
 * 
 * A Database Adapter class
 * for database/tables creation and setup 
 * @author suyesh
 *
 */

public class DbAdapter extends SQLiteOpenHelper{
	/* *******************************************************************
     * 
     * Database configuration: Make only appropriate changes of column names.  
     * 
     * *******************************************************************/
	
    /* General DB Properties: Change values only */
    private static final String DATABASE_NAME = "rssFeedReaderDB";
    private static final int DATABASE_VERSION = 1; // Increase ==> new empty DB created
    private static final String TAG = "DbAdapter";  // Used in error messages

    
   


    /* Internal DB Classes: Do not change */
    //private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    
    /* Calling Activity: Do not change */
    //private final Context mCtx;
    
    /* Tables creation sql statement */
    private static final String DATABASE_RSS_TABLE_CREATE =
            "create table "+RssURLDbAdapter.DATABASE_RSS_TABLE+" ("+RssURLDbAdapter.KEY_RSS_ROWID+" integer primary key autoincrement, "
            + RssURLDbAdapter.RSS_TITLE+" text not null, "+ RssURLDbAdapter.RSS_URL+" text not null , "+RssURLDbAdapter.CATEGORY+" text);";

    private static final String DATABASE_FEED_TABLE_CREATE =
        "create table "+RssFavoritesDbAdapter.DATABASE_FEED_TABLE+" ("+RssFavoritesDbAdapter.KEY_FEED_ROWID+" integer primary key autoincrement, "
        + RssFavoritesDbAdapter.RSS_ID+" integer not null, "+ RssFavoritesDbAdapter.FEED_TITLE+" text not null , "+RssFavoritesDbAdapter.FEED_DATE+" text  not null, " 
        + RssFavoritesDbAdapter.FEED_DESCRIPTION+" text, "+ RssFavoritesDbAdapter.FEED_LINK+" text, "+RssFavoritesDbAdapter.FEED_IMAGE_LINK1+" text);";
        //+RssFavoritesDbAdapter.FEED_IMAGE_LINK2+" text);";
    
    /**Default populate the table DATABASE_RSS_TABLE**/
    private static final String DEFAULT_POPULATE_DATABASE_RSS_TABLE =
    	"insert into  "+RssURLDbAdapter.DATABASE_RSS_TABLE+" ("+RssURLDbAdapter.KEY_RSS_ROWID+
    	", "+RssURLDbAdapter.RSS_TITLE+", "+ RssURLDbAdapter.RSS_URL+", "+RssURLDbAdapter.CATEGORY+") values (1, '"
    	+"bbc sports', 'http://newsrss.bbc.co.uk/rss/sportonline_world_edition/front_page/rss.xml', 'sports');";

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     * 
     * @param ctx the Context within which to work
     */
    public DbAdapter(Context ctx) {
    	super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
        //this.mCtx = ctx;
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

    
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_RSS_TABLE_CREATE);
        db.execSQL(DATABASE_FEED_TABLE_CREATE);
        db.execSQL(DEFAULT_POPULATE_DATABASE_RSS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS "+RssURLDbAdapter.DATABASE_RSS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+RssFavoritesDbAdapter.DATABASE_FEED_TABLE);
        onCreate(db);
    }

}
