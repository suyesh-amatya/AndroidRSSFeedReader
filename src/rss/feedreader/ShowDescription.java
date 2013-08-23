package rss.feedreader;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.*;

public class ShowDescription extends Activity 
{
    public void onCreate(Bundle icicle) 
    {
        super.onCreate(icicle);
        setContentView(R.layout.showdescription);
        
        String theStory = null;
        String imgurl = null;
        
        Intent startingIntent = getIntent();
        
        if (startingIntent != null)
        {
        	Bundle b = startingIntent.getBundleExtra("android.intent.extra.INTENT");
        	if (b == null)
        	{
        		theStory = "bad bundle?";
        	}
        	else
    		{
        		theStory = b.getString("title") + "\n\n" + b.getString("pubdate") + 
        		"\n\n" + b.getString("description").replace('\n',' ') + 
        		"\n\nMore information:\n" + b.getString("link");
        		
        		imgurl = b.getString("imgurl");
        		TextView title= (TextView) findViewById(R.id.txtTitle);
        		TextView date= (TextView) findViewById(R.id.txtDate);
        		TextView desc= (TextView) findViewById(R.id.txtDescription);
        		TextView link= (TextView) findViewById(R.id.link);
        		
        		title.setText(b.getString("title"));
        		date.setText(b.getString("pubdate"));
        		desc.setText(b.getString("description"));
        		link.setText("More information:\n"+b.getString("link"));
        		
    		}
        }
        else
        {
        	theStory = "Information Not Found.";
        	TextView db= (TextView) findViewById(R.id.txtTitle);
            db.setText(theStory);
        
        }
        
        
        try { 
        	  ImageView i = (ImageView)findViewById(R.id.imageView1); 
        	  Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(imgurl).getContent()); 
        	  i.setImageBitmap(bitmap); 
        } catch (MalformedURLException e) { 
        	  e.printStackTrace(); 
        } catch (IOException e) { 
        	  e.printStackTrace(); 
        } 

/*      ImageView i = (ImageView)findViewById(R.id.imageView1); 
        try {
        URL newurl = new URL(imgurl);
        Bitmap mIcon_val = BitmapFactory.decodeStream(newurl.openConnection() .getInputStream());
        i.setImageBitmap(mIcon_val);
        } catch (MalformedURLException e) { 
      	  e.printStackTrace(); 
      	} catch (IOException e) { 
      	  e.printStackTrace(); 
      	} 
*/         
        
        
        Button backbutton = (Button) findViewById(R.id.back);
        
        backbutton.setOnClickListener(new Button.OnClickListener() 
        {
            public void onClick(View v) 
            {
            	finish();
            }
        });        
    }
}
