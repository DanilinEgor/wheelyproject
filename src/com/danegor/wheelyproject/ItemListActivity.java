package com.danegor.wheelyproject;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

public class ItemListActivity extends FragmentActivity implements ItemListFragment.Callbacks {
    private final int SECONDS_TO_UPDATE = 30;
    
	private boolean mTwoPane;
	
	private MenuItem refreshItem;
	private final Content content = new Content(this);
	private final Handler handler = new Handler();
	private final Runnable runnable = new Runnable() {

		@Override
		public void run() {
			updateContent();
			handler.postDelayed(this, SECONDS_TO_UPDATE * 1000);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_list);

		if (findViewById(R.id.item_detail_container) != null) {
			mTwoPane = true;

			((ItemListFragment) getSupportFragmentManager().findFragmentById(
					R.id.item_list)).setActivateOnItemClick(true);
		}
		
		handler.postDelayed(runnable, SECONDS_TO_UPDATE * 1000);
	}

	@Override
	public void onItemSelected(String id) {
		if (mTwoPane) {
			Bundle arguments = new Bundle();
			arguments.putString(ItemDetailFragment.ARG_ITEM_ID, id);
			ItemDetailFragment fragment = new ItemDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.item_detail_container, fragment).commit();

		} else {
			Intent detailIntent = new Intent(this, ItemDetailActivity.class);
			detailIntent.putExtra(ItemDetailFragment.ARG_ITEM_ID, id);
			startActivity(detailIntent);
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeCallbacks(runnable);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_activity_actions, menu);
		refreshItem = menu.getItem(0);
		updateContent();
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_update:
			handler.removeCallbacks(runnable);
			updateContent();
			handler.postDelayed(runnable, SECONDS_TO_UPDATE * 1000);
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	public boolean isOnline() {
	    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    return (netInfo != null && netInfo.isConnectedOrConnecting());
	}
	
	public void updateContent() {
		if (isOnline()) {
			content.update();
			beginAnimationOfRefreshIcon();
		}
		else
			Toast.makeText(getApplicationContext(), "Not connected to Internet", Toast.LENGTH_SHORT).show();
	}
	
	public void beginAnimationOfRefreshIcon() {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    ImageView iv = (ImageView) inflater.inflate(R.layout.refresh_action_view, null);

	    Animation rotation = AnimationUtils.loadAnimation(this, R.anim.clockwise_refresh);
	    rotation.setRepeatCount(Animation.INFINITE);
	    iv.startAnimation(rotation);

	    refreshItem.setActionView(iv);
	}
	
	public void endAnimationOfRefreshIcon() {
		if (refreshItem != null && refreshItem.getActionView() != null) {
			refreshItem.getActionView().clearAnimation();
			refreshItem.setActionView(null);
		}
	}
	
}
