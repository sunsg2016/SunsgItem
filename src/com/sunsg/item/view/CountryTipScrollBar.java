package com.sunsg.item.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.breadtrip.R;
import com.sunsg.item.util.Logger;
import com.sunsg.item.util.Tools;

public class CountryTipScrollBar extends FrameLayout {
	private Context mContext;

	private View mBar;
	private ImageView mScrollBar;
	private TextView mScrollTip;
	private float lastY;// mScrollBar 最后的位置

	private int minTop;// 据顶部的最小距离
	private int maxBottom;// 据底部的最大距离

	public CountryTipScrollBar(Context context) {
		this(context, null);
	}

	public CountryTipScrollBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public CountryTipScrollBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	private void init(Context context) {
		inflate(context, R.layout.country_scroll_bar, this);
		mContext = context;
		mBar = findViewById(R.id.v_scroll_bar);
		mScrollBar = (ImageView) findViewById(R.id.iv_scroll_bar);
		mScrollTip = (TextView) findViewById(R.id.tv_scroll_tip);
		onTouchScrollBar();
	}

	/**
	 * 触摸 scrollbar
	 */
	private void onTouchScrollBar() {
		mScrollBar.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Logger.i("test", "ontouch");
				float y = event.getY();
				int action = event.getAction();
				switch (action) {
				case MotionEvent.ACTION_DOWN:
					lastY = event.getY();
					mScrollTip.setVisibility(View.VISIBLE);
					break;
				case MotionEvent.ACTION_CANCEL:
				case MotionEvent.ACTION_UP:
					mScrollTip.setVisibility(View.INVISIBLE);
//					return false;
					break;
				case MotionEvent.ACTION_MOVE:
					int dy = (int) (y - lastY);
					// mScrollBar.scrollBy(0, dy);
					layoutScrollBar(dy);
					lastY = y;
					break;

				default:
					break;
				}
				return true;
			}
		});
	}

	private void layoutScrollBar(int dy) {
		Logger.i("test", "bar.gettop = " + mBar.getTop());
		Logger.i("test", "bar.getbottom = " + mBar.getBottom());
		Logger.i("test", "mScrollBar.gettop" + mScrollBar.getTop());
		int left = mScrollBar.getLeft();
		int right = mScrollBar.getRight();
		int top = mScrollBar.getTop() + dy;
		int bottom = mScrollBar.getBottom() + dy;

		if (minTop == 0)
			minTop = mBar.getTop();
		if (maxBottom == 0)
			maxBottom = mBar.getBottom();
		if (top < minTop) {
			return;
		}
		if (bottom > maxBottom) {
			return;
		}
		mScrollBar.layout(left, top, right, bottom);
		
		mScrollTip.layout(mScrollTip.getLeft(), mScrollTip.getTop()+dy, mScrollTip.getRight(), mScrollTip.getBottom() +dy);
	}

}
