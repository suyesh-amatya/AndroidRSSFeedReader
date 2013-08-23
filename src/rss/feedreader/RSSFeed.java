package rss.feedreader;


import java.util.List;
import java.util.Vector;


public class RSSFeed 
{
	private String _title = null;
	private String _pubdate = null;
	private int _itemcount = 0;
	private List<RSSItem> _itemlist;
	private String head_title;
	private String lastBuildDate;
	
	
	RSSFeed()
	{
		_itemlist = new Vector<RSSItem>(0); 
	}
	int addItem(RSSItem item)
	{
		_itemlist.add(item);
		_itemcount++;
		return _itemcount;
	}
	RSSItem getItem(int location)
	{
		return _itemlist.get(location);
	}
	List<RSSItem> getAllItems()
	{
		return _itemlist;
	}
	int getItemCount()
	{
		return _itemcount;
	}
	void setTitle(String title)
	{
		_title = title;
	}
	void setPubDate(String pubdate)
	{
		_pubdate = pubdate;
	}
	String getTitle()
	{
		return _title;
	}
	String getPubDate()
	{
		return _pubdate;
	}
	
	public void setHead_title(String head_title) {
		this.head_title = head_title;
	}
	public String getHead_title() {
		return head_title;
	}
	public void setLastBuildDate(String lastBuildDate) {
		this.lastBuildDate = lastBuildDate;
	}
	public String getLastBuildDate() {
		return lastBuildDate;
	}
	
}
