package com.danegor.wheelyproject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.danegor.wheelyproject.Content.Item;

public class ItemDetailFragment extends Fragment {
	public static final String ARG_ITEM_ID = "item_id";

	private Item mItem;

	public ItemDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(ARG_ITEM_ID)) {
			mItem = Content.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
			getActivity().getActionBar().setTitle(mItem.title);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_item_detail,
				container, false);

		if (mItem != null) {
			((TextView) rootView.findViewById(R.id.item_detail_title)).setText("Title: " + mItem.title);
			((TextView) rootView.findViewById(R.id.item_detail_text)).setText(mItem.text);
		}

		return rootView;
	}
}
