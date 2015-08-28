package com.sunsg.item.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.breadtrip.R;
import com.sunsg.item.util.Logger;

public class TabFragment extends Fragment {
	public static final String TITLE = "title";
	private String mTitle = "Defaut Value";
	private TextView mTextView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mTitle = getArguments().getString(TITLE);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_tab, container, false);
		mTextView = (TextView) view.findViewById(R.id.id_info);
		mTextView.setText(mTitle);
		mTextView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Logger.e("test", mTitle);
			}
		});
		return view;

	}

	public static TabFragment newInstance(String title) {
		TabFragment tabFragment = new TabFragment();
		Bundle bundle = new Bundle();
		bundle.putString(TITLE, title);
		tabFragment.setArguments(bundle);
		return tabFragment;
	}

}
