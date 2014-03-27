package com.db;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jmbdh.PuntuacionFragment.AppListFragment;
import com.newproyectjmb.R;

/**
 * A custom ArrayAdapter used by the {@link AppListFragment} to display the
 * device's installed applications.
 */
public class PointListAdapter extends ArrayAdapter<Point> {
	private LayoutInflater mInflater;

	public PointListAdapter(Context ctx) {
		super(ctx, android.R.layout.simple_list_item_2);
		mInflater = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;

		if (convertView == null) {
			view = mInflater.inflate(R.layout.list_item_icon_text, parent,
					false);
		} else {
			view = convertView;
		}

		Point item = getItem(position);
		((TextView) view.findViewById(R.id.bdposition)).setText(position+"");
		((TextView) view.findViewById(R.id.bduser)).setText(item.getName()+"");
		((TextView) view.findViewById(R.id.bdround)).setText("Round");
		((TextView) view.findViewById(R.id.bdpoints)).setText("pointsss");
		return view;
	}

	public void setData(List<Point> data) {
		clear();
		if (data != null) {
			for (int i = 0; i < data.size(); i++) {
				add(data.get(i));
			}
		}
	}
}
