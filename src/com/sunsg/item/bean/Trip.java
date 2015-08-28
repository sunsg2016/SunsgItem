package com.sunsg.item.bean;

import java.util.List;

public class Trip {
	public int target_waypoint_id;
	public List<String>covered_countries;
	public int waypoints;
	public boolean wifi_sync;
	public String last_day;
	public PointInfo poi_infos_count;
	
	public String first_day;
	public String id;
	public String city;
	public int privacy;
	public int day_count;
	public String first_timezone;
	public int comment_count;
	public int shared;
	public List<String> tips;
	public String province;
	public double mileage;
	public String description;
	public int view_count;
	public List<String>city_slug_urls;
	public double last_modified;
	public User user;
	public long date_complete;
	public String device;
	public String date_added;
	public List<String>cities;
	public String trackpoints_thumbnail_image;
	public String name;
	public String country;
	public String recommendations;
	public String cover_image;
	public String recommended;
	public StartPoint start_point;
	public List<Days>days;
	
	public class StartPoint{
		public double latitude;
		public double longitude;
	}
	public class PointInfo{
		public int flight;
		public int mall;
		public int hotel;
		public int sights;
		public int restaurant;
	}
	
	public class User{
		public String location_name;
		public String name;
		public String resident_city_id;
		public String mobile;
		public int gender;
		public String avatar_m;
		public String cover;
		public String custom_url;
		public String id;
		public String birthday;
		public String country_num;
		public String avatar_s;
		public String avatar_l;
		public boolean email_verified;
		public String country_code;
		public String email;
		public String user_desc;
	}
	
	public class Days{
		public String date;
		public int day;
		public List<WayPoints> waypoints;
	}
	
	public class WayPoints{
		public String photo;
		public String photo_1600;
		
//		public String poi;
//		
		public String trip_id;
		public String id;
//		
		public String city;
		public int privacy;
		public int comments;
		public String timezone;
		public boolean recommended;
//		
		public String text;
		public String photo_weblive;
		public int shared;
		public String province;
		public String photo_s;
		public boolean track;
		public String hotel;
		public long date_added;
		
		public int day;
		public boolean cover;
		public String photo_w640;
		public String photo_webtrip;
		public String local_time;
		public String recommendations;
		public String model;
		public PhotoInfo photo_info;
		public Location location;
	}
	
	public class PhotoInfo{
		public int w;
		public int h;
	}
	
	public class Location{
		public double latitude;
		public double lat;
		public double lng;
		public double longitude;
	}
	
	public class Place{
		public Province province;
		public Country country;
	}
	
	public class Province{
		public int type;
		public int id;
	}
	
	public class Country{
		public int type;
		public String id;
	}
}
