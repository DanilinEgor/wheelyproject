package com.danegor.wheelyproject;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SimpleAdapter;

public class ItemListActivity extends FragmentActivity implements ItemListFragment.Callbacks {
    private final int SECONDS_TO_UPDATE = 10;
    
	private boolean mTwoPane;
	
	private final Content c = new Content();
	private final Handler handler = new Handler();
	private final Runnable r = new Runnable() {

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
		
		updateContent();
		handler.postDelayed(r, SECONDS_TO_UPDATE * 1000);
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
		handler.removeCallbacks(r);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_activity_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_update:
			updateContent();
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
			c.update();
			((SimpleAdapter) ((ItemListFragment) getSupportFragmentManager()
					.findFragmentById(R.id.item_list)).getListAdapter()).notifyDataSetChanged();
		}
	}
	
}
