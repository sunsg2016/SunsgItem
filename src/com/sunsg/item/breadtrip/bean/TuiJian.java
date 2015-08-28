package com.sunsg.item.breadtrip.bean;

import java.util.List;

public class TuiJian {
	//search_data list
	public List<SearchDataChild> search_data;
	static class SearchDataChild{
		public String type;
		public String title;
		public List<SearchDataChildChild> elements;
	    public static class SearchDataChildChild{
//	        public static class Elements{
	        	public int rating;
	        	public String name;
	        	public String url;
	        	public String name_orig;
	        	public boolean has_experience;
	        	public int comments_count;
	        	public int rating_users;
	        	public int type;
	        	public String id;
	        	public boolean has_route_maps;
	        	public String icon;
	        	public Location location;
	        	public static class Location{
	        		public double lat;
	        		public double lng;
	        	}
	        }
//	    }
	}
	
	
	
	
//	//elements list
//	public List<Elements> elements;
//	
//	public static class Elements{
//		public String url;
//		public int type;
//		public String title;
//		public List<Data> data;
//		public static class Data{
//			
//			public static class DataChlid{ 
//				
//			}
//			
//		}
//	}
}
