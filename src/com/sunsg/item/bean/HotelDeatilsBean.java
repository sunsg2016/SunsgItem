package com.sunsg.item.bean;

import java.util.List;

public class HotelDeatilsBean extends QueryResult {
	private String apicode;
	// private Overview overview;
	private List<Details> data;

	public class Overview {
		private int max_length;
		private int zoom;
		private String max_lo;
		private String max_la;
		private String min_lo;
		private String min_la;
		private String keyword;
		private List<String> servings;
		private List<String> brands;
		private List<String> styles;
	}

	public class Details {
		private String name;
		private String address;
		private String isgroup;
		private int id;
		private String encrypt_id;
		private String service;
		private int grade;
		private String telephone;
		private String ext;
		private String score;
		private String daodaoscore;
		private String commentscore;
		private int daodaoamount;
		private String price;
		private String imagepath;
		private String around;
		private int commentamount;
		private String la;
		private String lo;
		private String la_g;
		private String lo_g;
		private String distance;
		private String hotelbrand;
		private String html5url;

	}
}
