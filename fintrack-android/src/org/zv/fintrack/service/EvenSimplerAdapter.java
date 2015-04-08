package org.zv.fintrack.service;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

public class EvenSimplerAdapter extends SimpleAdapter {
    int incomeCount;
	public EvenSimplerAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to, int incomeCount) {
		super(context, data, resource, from, to);
		this.incomeCount = incomeCount;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
		view.setBackgroundColor((position < incomeCount) ? 0xFFDDEEDD : 0xFFDDDDEE);
		return view;
	}
}
