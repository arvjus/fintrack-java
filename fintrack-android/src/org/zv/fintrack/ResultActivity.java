package org.zv.fintrack;

import java.util.ArrayList;
import java.util.Map;

import org.zv.fintrack.service.EvenSimplerAdapter;

import android.app.ListActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;

public class ResultActivity extends ListActivity {
	@Override
	@SuppressWarnings("unchecked")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ArrayList<Map<String,String>> result = FintrackApplication.getInstance().getResult();
		FintrackApplication.getInstance().setResult(null);

		int incomeCount = getIntent().getIntExtra("incomeCount", 0); 
		int lineCount = getIntent().getIntExtra("lineCount", 3);
		if (lineCount == 3) {
			setListAdapter(new EvenSimplerAdapter(this, result, R.layout.list_item3, new String[] { "line1", "line2", "line3" }, 
				new int[] { R.id.list_item_line1_textview, R.id.list_item_line2_textview, R.id.list_item_line3_textview }, incomeCount));
		} else {
			/*
			ArrayList<Map<String,Spanned>> result2 = new ArrayList<Map<String,Spanned>>();
			Map<String,Spanned> map = new java.util.HashMap<String,Spanned>();
			map.put("line1", Html.fromHtml("income"));
			map.put("line2", Html.fromHtml("count <b>123</b>"));
			result2.add(map);
			*/
			setListAdapter(new EvenSimplerAdapter(this, result, R.layout.list_item2, new String[] { "line1", "line2" }, 
				new int[] { R.id.list_item_line1_textview, R.id.list_item_line2_textview }, incomeCount));
		} 
	}
}
