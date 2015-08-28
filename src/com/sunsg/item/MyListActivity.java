package com.sunsg.item;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.breadtrip.R;
import com.sunsg.item.adapter.MyAdapter;
import com.sunsg.item.view.MyListView;
import com.sunsg.item.view.MyListView.OnDeleteListener;

public class MyListActivity extends Activity {

	private MyListView myListView;

	private MyAdapter adapter;

	private List<String> contentList = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_my_listview);
		initList();
		myListView = (MyListView) findViewById(R.id.my_list_view);
		myListView.setOnDeleteListener(new OnDeleteListener() {
			@Override
			public void onDelete(int index) {
				contentList.remove(index);
				adapter.notifyDataSetChanged();
			}
		});
		adapter = new MyAdapter(this, 0, contentList);
		myListView.setAdapter(adapter);
	}

	private void initList() {
		contentList.add("Content Item 1");
		contentList.add("Content Item 2");
		contentList.add("Content Item 3");
		contentList.add("Content Item 4");
		contentList.add("Content Item 5");
		contentList.add("Content Item 6");
		contentList.add("Content Item 7");
		contentList.add("Content Item 8");
		contentList.add("Content Item 9");
		contentList.add("Content Item 10");
		contentList.add("Content Item 11");
		contentList.add("Content Item 12");
		contentList.add("Content Item 13");
		contentList.add("Content Item 14");
		contentList.add("Content Item 15");
		contentList.add("Content Item 16");
		contentList.add("Content Item 17");
		contentList.add("Content Item 18");
		contentList.add("Content Item 19");
		contentList.add("Content Item 20");
	}

}
