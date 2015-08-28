package com.sunsg.item;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.breadtrip.R;

public class SelectCityActivity extends Activity{
	private ListView list;
	private MyAdapter mMyAdapter;
	
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_select_city);
			list = (ListView) findViewById(R.id.listview);
			mMyAdapter = new MyAdapter(this);
			mMyAdapter.setItems(setData());
			mMyAdapter.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Item item = (Item) mMyAdapter.getItem((Integer) v.getTag());
					if(item.isTitle){
						boolean isopen = item.isOpen ? false: true;
						for (int i = 0; i < mMyAdapter.getItems().size(); i++) {
							if(item.pinyin.equals(mMyAdapter.getItems().get(i).pinyin))
							mMyAdapter.getItems().get(i).isOpen =  isopen;
						}
						
						mMyAdapter.notifyDataSetChanged();
					}else{
//						不是标题
					}
					
				}
			});
			list.setAdapter(mMyAdapter);
		}
		
		
		public List<Item> setData(){
			List<Item> items = new ArrayList<Item>();
			items.add(new Item("#","A"));
			items.add(new Item("安徽","A"));
			items.add(new Item("安达","A"));
			items.add(new Item("安通","A"));
			items.add(new Item("阿坝","A"));
			
			items.add(new Item("#","B"));
			items.add(new Item("北京","B"));
			items.add(new Item("不大啦","B"));
			items.add(new Item("不松岭","B"));
			items.add(new Item("北苑","B"));
			
			
			items.add(new Item("#","C"));
			items.add(new Item("村有","C"));
			items.add(new Item("从摇头晃脑","C"));
			items.add(new Item("层级","C"));
			items.add(new Item("除了","C"));
			
			items.add(new Item("#","D"));
			items.add(new Item("顿开","D"));
			items.add(new Item("东京机会","D"));
			items.add(new Item("豆浆","D"));
			items.add(new Item("对于好","D"));
			
			return items;
		}
		
		public class Item{
			public String content;
			public String pinyin;
			public boolean isTitle;
			public boolean isOpen;
			
			public Item(){
				
			}
			public Item(String content,String pinyin){
				this.content = content;
				this.pinyin = pinyin;
				if("#".equals(content)){
					isTitle = true;
				}else{
					isTitle = false;
				}
				isOpen = true;
			}
		}
		
		
		
		public class MyAdapter extends BaseAdapter{
			private List<Item> mItem;
			private Context context;
			private View.OnClickListener onClicklistener;
			public MyAdapter(Context context){
				this.context = context;
				mItem = new ArrayList<Item>();
			}
			
			public void setItems(List<Item> items){
				this.mItem = items;
				notifyDataSetChanged();
			}
			
			public List<Item> getItems(){
				return mItem;
			}
			
			public void setOnClickListener(View.OnClickListener onClickListener){
				this.onClicklistener = onClickListener;
			}
			@Override
			public int getCount() {
				return mItem.size();
			}

			@Override
			public Object getItem(int position) {
				return mItem.get(position);
			}

			@Override
			public long getItemId(int position) {
				return position;
			}
			
			public  int dp2px(Context context, float dp) {
				final float scale = context.getResources().getDisplayMetrics().density;
				return (int) (dp * scale + 0.5f);
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				final Views views;
				if(convertView == null){
					views = new Views();
					convertView = View.inflate(context, R.layout.activity_select_city_item, null);
					views.content = (TextView) convertView.findViewById(R.id.content);
					convertView.setTag(views);
				}else{
					views = (Views) convertView.getTag();
				}
				
				Item item = mItem.get(position);
				RelativeLayout.LayoutParams params = null;
				if(item.isOpen){
					params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, dp2px(context, 60));
					convertView.findViewById(R.id.all).setLayoutParams(params);
				}else{
					params = new RelativeLayout.LayoutParams(0, 0);
					if(!item.isTitle){
						convertView.findViewById(R.id.all).setLayoutParams(params);
					}
				}
				views.content.setText(item.content);
				
				if(item.isTitle){ 
					views.content.setText(item.pinyin);
				}
			    convertView.findViewById(R.id.all).setTag(position);
				if(onClicklistener != null) convertView.findViewById(R.id.all).setOnClickListener(onClicklistener);
				
				return convertView;
			}
			
			
			class Views{
				public TextView content;
				public ImageView Im;
			}
			
		}
		
		
}
