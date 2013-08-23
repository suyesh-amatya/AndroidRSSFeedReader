package rss.feedreader;

import java.util.List;
import java.util.Vector;

public class RSSSearchFeed {
	private List<RSSSearchItem> _searchitemlist;
	private int _searchitemcount = 0;
	
	RSSSearchFeed(){
		_searchitemlist = new Vector<RSSSearchItem>(0); 
	}
	
	int addSearchItem(RSSSearchItem item){
		_searchitemlist.add(item);
		_searchitemcount++;
		return _searchitemcount;
	}
	
	RSSSearchItem getSearchItem(int location){
		return _searchitemlist.get(location);
	}
	
	List<RSSSearchItem> getAllSearchItems(){
		return _searchitemlist;
	}
	
	int getSearchItemCount(){
		return _searchitemcount;
	}

}
