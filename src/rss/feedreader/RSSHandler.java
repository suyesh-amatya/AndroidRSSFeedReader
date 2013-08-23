package rss.feedreader;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.*;

import android.util.Log;


public class RSSHandler extends DefaultHandler 
{
    
    RSSFeed _feed;
    RSSItem _item;
    String _lastElementName = "";
    boolean title = true;
    boolean lastBuildDate = true;
    final int RSS_TITLE = 1;
    final int RSS_LINK = 2;
    final int RSS_DESCRIPTION = 3;
    final int RSS_CATEGORY = 4;
    final int RSS_PUBDATE = 5;
    
    int depth = 0, i=0;
    String currentValue = "";
    /*
     * Constructor 
     */
    RSSHandler()
    {
    }
    
    /*
     * getFeed - this returns our feed when all of the parsing is complete
     */
    RSSFeed getFeed()
    {
        return _feed;
    }
    
    
    public void startDocument() throws SAXException
    {
        // initialize our RSSFeed object - this will hold our parsed contents
        _feed = new RSSFeed();
        // initialize the RSSItem object - you will use this as a crutch to grab 
		// the info from the channel
        // because the channel and items have very similar entries..
        _item = new RSSItem();

    }
    public void endDocument() throws SAXException
    {
    }
    public void startElement(String namespaceURI, String localName,String qName, 
                                             Attributes atts) throws SAXException
    {
        depth++;
        currentValue = "";
        if (localName.equals("item"))
        {
            // create a new item
            _item = new RSSItem();
            return;
        }
        
        
        if (localName.equals("thumbnail"))
        {
        	if(i%2==0){
        		_item.set_imgurl(atts.getValue("url"));
        		//System.out.println(atts.getValue("url"));
        	}
        	else{
        		_item.set_imgurl(atts.getValue("url"));
        	}
        	i++;
            return;
        }
        
    }
    
    public void endElement(String namespaceURI, String localName, String qName) 
                                                               throws SAXException
    {
        depth--;
        
        if (localName.equals("title"))
        {
        	if(title){
        		_feed.setHead_title(currentValue);
        		title = false;
        	}
        	else{
        		_item.setTitle(currentValue);
        	}
            
            return;
        }
        if (localName.equals("description"))
        {
            _item.setDescription(currentValue);
            return;
        }
        if (localName.equals("link"))
        {
            _item.setLink(currentValue);
            return;
        }
        if (localName.equals("category"))
        {
            _item.setCategory(currentValue);
            return;
        }
        if (localName.equals("pubDate") || localName.equals("lastBuildDate"))
        {
        	if(lastBuildDate){
        		_feed.setLastBuildDate(currentValue);
        		lastBuildDate = false;
        	}
        	else{
        		_item.setPubDate(currentValue);
        	}
            
            return;
        }
       
        if (localName.equals("item"))
        {
            // add our item to the list!
            _feed.addItem(_item);
            return;
        }
    }
     
    public void characters(char ch[], int start, int length)
    {
        currentValue += new String(ch,start,length);
        Log.i("RSSReader","characters[" + currentValue + "]");
        return;        
    }
}