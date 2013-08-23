package rss.feedreader;


import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;


public class AddURL extends Activity{
	static final String[] RSS_TITLES = new String[] {"bbc news",
		"bbc sports", "bbc science", "bbc europe", "bbc business"
	};
	
	static final String[] RSS_URLS = new String[] {"http://feeds.bbci.co.uk/news/rss.xml",
		"http://newsrss.bbc.co.uk/rss/sportonline_world_edition/front_page/rss.xml",
		"http://feeds.bbci.co.uk/news/science_and_environment/rss.xml",
		"http://feeds.bbci.co.uk/news/world/europe/rss.xml",
		"http://feeds.bbci.co.uk/news/business/rss.xml"
	};
	
	static final String[] CATEGORIES = new String[] {"news",
		"sports", "science", "europe", "business"
	};
	
	private AutoCompleteTextView acTitle, acUrl, acCategory;
	private Button btnEnter, btnCancel;
	
	public static final String PROVIDER_NAME = "rss.feedreader.RssUrlCP";
	public static final String uriString = "content://"+ PROVIDER_NAME + "/rss_urls";
	public static final Uri CONTENT_URI = Uri.parse(uriString);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.url_add_form);
		
		acTitle = (AutoCompleteTextView) findViewById(R.id.acTitle);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>
		(this, android.R.layout.simple_dropdown_item_1line, RSS_TITLES);
		acTitle.setAdapter(adapter);
		
		acUrl = (AutoCompleteTextView) findViewById(R.id.acUrl);
		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>
		(this, android.R.layout.simple_dropdown_item_1line, RSS_URLS);
		acUrl.setAdapter(adapter1);
		
		acCategory = (AutoCompleteTextView) findViewById(R.id.acCategory);
		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>
		(this, android.R.layout.simple_dropdown_item_1line, CATEGORIES);
		acCategory.setAdapter(adapter2);
		
		btnEnter = (Button) findViewById(R.id.btnEnter);
		btnCancel = (Button) findViewById(R.id.btnCancel);
		
		if(getIntent().getExtras() == null){
			btnEnter.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String title = acTitle.getText().toString();
					String url = acUrl.getText().toString();
					String category = acCategory.getText().toString();
					
					if(title.trim().length()>0 && url.trim().length()>0){
						if(URLUtil.isValidUrl(url)){
							ContentValues insertValues = new ContentValues();
				    		
			    			insertValues.put(RssURLDbAdapter.RSS_TITLE, title);
			    			insertValues.put(RssURLDbAdapter.RSS_URL, url);
			    			insertValues.put(RssURLDbAdapter.CATEGORY, category);
				    		getContentResolver().insert(
				    	        		CONTENT_URI.buildUpon().build(), insertValues);
				    		finish();
						}
						else{
							Toast.makeText(AddURL.this,
									"You need to enter Valid URL.", Toast.LENGTH_LONG).show();
						}
						
					}
					else{
		    			Toast.makeText(AddURL.this,
								"You need to enter Title AND URL.", Toast.LENGTH_LONG).show();
		    		}
					
				}
			});
			
			
			btnCancel.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					finish();
				}
			});
		
		}
		
		else{
			final long id = getIntent().getExtras().getLong("id");
			//System.out.println(id);
			
			Cursor c = getContentResolver().query(CONTENT_URI.buildUpon().
					appendPath(String.valueOf(id)).build(), null, null, null, null);
			
			acTitle.setText(c.getString(c.getColumnIndex("rss_title")));
			acUrl.setText(c.getString(c.getColumnIndex("rss_url")));
			acCategory.setText(c.getString(c.getColumnIndex("category")));
			
			
			btnEnter.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String title = acTitle.getText().toString();
					String url = acUrl.getText().toString();
					String category = acCategory.getText().toString();
					
					if(title.trim().length()>0 && url.trim().length()>0){
						if(URLUtil.isValidUrl(url)){
							ContentValues updateValues = new ContentValues();
				    		
							updateValues.put(RssURLDbAdapter.RSS_TITLE, title);
							updateValues.put(RssURLDbAdapter.RSS_URL, url);
							updateValues.put(RssURLDbAdapter.CATEGORY, category);
				    		getContentResolver().update(
				    				CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build(), updateValues
				    					, null, null);
				    		finish();
						}
						else{
							Toast.makeText(AddURL.this,
									"You need to enter Valid URL.", Toast.LENGTH_LONG).show();
						}
						
					}
					else{
		    			Toast.makeText(AddURL.this,
								"You need to enter Title AND URL.", Toast.LENGTH_LONG).show();
		    		}
					
				}
			});
			
			
			btnCancel.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					finish();
				}
			});
			
		}
		
		
	}

}
