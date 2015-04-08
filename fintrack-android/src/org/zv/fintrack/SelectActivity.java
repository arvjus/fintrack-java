package org.zv.fintrack;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.CheckBox;

import org.apache.http.HttpStatus;

import org.zv.fintrack.model.Category;
import org.zv.fintrack.model.Income;
import org.zv.fintrack.model.Expense;
import org.zv.fintrack.model.Summary;
import org.zv.fintrack.sax.XmlParser;
import org.zv.fintrack.service.AsyncTaskWithProgress;

public class SelectActivity extends Activity {
	private static final int DATE_FROM_DIALOG_ID = 0;
	private static final int DATE_TO_DIALOG_ID = 1;
	private Calendar date_from;
	private Calendar date_to;
	private Button date_from_button;
	private Button date_to_button;
	private CheckBox incomes_checkbox;
	private Spinner categories_spinner;
	private ArrayAdapter<Category> categories_spinner_adapter;
	private Button plain_button;
	private Button summary_simple_button;
	private Button summary_bymonth_button;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select);
        
        // set date
        date_from = Calendar.getInstance();
        date_from.set(Calendar.DAY_OF_MONTH, 1);
        date_to = Calendar.getInstance();
        
        // setup ui controls
        date_from_button = (Button)findViewById(R.id.select_date_from_button);
        date_from_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	showDialog(DATE_FROM_DIALOG_ID);
            }
        });
        
        date_to_button = (Button)findViewById(R.id.select_date_to_button);
        date_to_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	showDialog(DATE_TO_DIALOG_ID);
            }
        });
        
        incomes_checkbox = (CheckBox) findViewById(R.id.select_incomes_checkbox); 
        
        Category all = new Category();
        all.categoryId = "xx";
        all.name = "All Categories";
        all.nameShort = "All Categories";
        List<Category> categories = new ArrayList<Category>();
        categories.add(all);
        categories.addAll(FintrackApplication.getInstance().getCategories());
        categories_spinner_adapter = new ArrayAdapter<Category>(this, android.R.layout.simple_spinner_item, categories);  
        categories_spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categories_spinner = (Spinner) findViewById(R.id.select_categories_spinner);
        categories_spinner.setAdapter(categories_spinner_adapter);
        categories_spinner.setSelection(0);

        plain_button = (Button)findViewById(R.id.select_plain_button);
        plain_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	retrievePlain();
            }
        });

        summary_simple_button = (Button)findViewById(R.id.select_summary_simple_button);
        summary_simple_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
        		retrieveSummary("/rest/incomes/summary/simple", "/rest/expenses/summary/bycategory");
            }
        });

        summary_bymonth_button = (Button)findViewById(R.id.select_summary_bymonth_button);
        summary_bymonth_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
        		retrieveSummary("/rest/incomes/summary/bymonth", "/rest/expenses/summary/bymonth");
            }
        });

        setButtonText();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
	    switch (id) {
	    case DATE_FROM_DIALOG_ID:
	        return new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					date_from.set(Calendar.YEAR, year);
					date_from.set(Calendar.MONTH, monthOfYear);
					date_from.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			        setButtonText();
				}
	        },
	        date_from.get(Calendar.YEAR), date_from.get(Calendar.MONTH), date_from.get(Calendar.DAY_OF_MONTH));
	    case DATE_TO_DIALOG_ID:
	        return new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					date_to.set(Calendar.YEAR, year);
					date_to.set(Calendar.MONTH, monthOfYear);
					date_to.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			        setButtonText();
				}
	        },
	        date_to.get(Calendar.YEAR), date_to.get(Calendar.MONTH), date_to.get(Calendar.DAY_OF_MONTH));
	    }
		return super.onCreateDialog(id);
	}

	private void setButtonText() {
		date_from_button.setText(String.format("%4d-%02d-%02d", date_from.get(Calendar.YEAR), date_from.get(Calendar.MONTH) + 1, date_from.get(Calendar.DAY_OF_MONTH)));
		date_to_button.setText(String.format("%4d-%02d-%02d", date_to.get(Calendar.YEAR), date_to.get(Calendar.MONTH) + 1, date_to.get(Calendar.DAY_OF_MONTH)));
	}

	private void retrievePlain() {
		AsyncTaskWithProgress task = new AsyncTaskWithProgress(this, "Fetching Plain Records") {
    		private ArrayList<Map<String,String>> result = new ArrayList<Map<String,String>>();
    		private int incomeCount;

    		@Override
    		protected Boolean doInBackground(String... params) {
				Log.d(getClass().getSimpleName(), "doInBackground()..");
				int status;
				StringBuilder sb = new StringBuilder();
				XmlParser parser = new XmlParser();
				Map<String, String> postparams = new HashMap<String, String>();
    			try {
    				if (params[2] != null) {
    					publishProgress("Fetch Incomes..");
    					postparams.put("dateFrom", params[0]);
    					postparams.put("dateTo", params[1]);
    					status = FintrackApplication.getInstance().doPost("/rest/incomes/list", postparams, sb);
    					if (status != HttpStatus.SC_OK) {
    						publishProgress("Status: " + status);
    						return false;
    					}
    					ArrayList<Income> incomes = parser.parseIncomesResponse(sb.toString());
    					for (Income income : incomes) {
    						Map<String,String> map = new HashMap<String,String>();
    						map.put("line1", "Income");
    						map.put("line2", String.format("%1s [%2s]", income.createDate, income.userId));
    						map.put("line3", income.amount + (income.descr.length() > 0 ? " - " + income.descr : ""));
    						result.add(map);
    					}
    					incomeCount = incomes.size();
    				}

					publishProgress("Fetch Expenses..");
					postparams.clear();
					postparams.put("dateFrom", params[0]);
					postparams.put("dateTo", params[1]);
					if (params[3] != null) {
						postparams.put("categoryIds", params[3]);
					}
					status = FintrackApplication.getInstance().doPost("/rest/expenses/list", postparams, sb);
					if (status != HttpStatus.SC_OK) {
						publishProgress("Status: " + status);
						return false;
					}
					ArrayList<Expense> expenses = parser.parseExpensesResponse(sb.toString());
					for (Expense expense : expenses) {
						Map<String,String> map = new HashMap<String,String>();
						map.put("line1", String.format("Expense (%1s)", expense.category));
						map.put("line2", String.format("%1s [%2s]", expense.createDate, expense.userId));
						map.put("line3", expense.amount + (expense.descr.length() > 0 ? " - " + expense.descr : ""));
						result.add(map);
					}
					
					return true;
				} catch (Exception e) {
					publishProgress("Error: " + e.toString());
					Log.e(getClass().getSimpleName(), e.toString());
					return false;
				}
    		}

			@Override
			protected void onPostExecute(Boolean success) {
				Log.d(getClass().getSimpleName(), "onPostExecute()..");
				super.onPostExecute(success);

				Intent intent = new Intent(SelectActivity.this, ResultActivity.class);
				FintrackApplication.getInstance().setResult(result);
				intent.putExtra("incomeCount", incomeCount);
				intent.putExtra("lineCount", 3);
				startActivity(intent);
			}
    	};
    	execute(task);
	}

	private void retrieveSummary(final String uri_inc, final String uri_exp) {
		AsyncTaskWithProgress task = new AsyncTaskWithProgress(this, "Fetching Summary Records") {
    		private ArrayList<Map<String,String>> result = new ArrayList<Map<String,String>>();
    		private int incomeCount;

    		@Override
    		protected Boolean doInBackground(String... params) {
				Log.d(getClass().getSimpleName(), "doInBackground()..");
				int status;
				StringBuilder sb = new StringBuilder();
				XmlParser parser = new XmlParser();
				Map<String, String> postparams = new HashMap<String, String>();
				double total_amount = 0.0;
				int total_count = 0;
    			try {
    				if (params[2] != null) {
    					publishProgress("Fetch Incomes..");
    					postparams.put("dateFrom", params[0]);
    					postparams.put("dateTo", params[1]);
    					status = FintrackApplication.getInstance().doPost(uri_inc, postparams, sb);
    					if (status != HttpStatus.SC_OK) {
    						publishProgress("Status: " + status);
    						return false;
    					}

    					total_amount = 0.0;
    					total_count = 0;
    					ArrayList<Summary> summaries = parser.parseSummaryResponse(sb.toString());
    					for (Summary summary : summaries) {
    						Map<String, String> map = new HashMap<String, String>();
    						map.put("line1", "Income " + (summary.group != null ? "(" + summary.group + ")" : ""));
    						map.put("line2", String.format("Amount: %1s, Count: %2s", summary.amount, summary.count));
    						result.add(map);
    						total_amount += summary.amount;
    						total_count += summary.count;
    					}
    					incomeCount = summaries.size();
    					if (incomeCount > 1) {
    						Map<String, String> map = new HashMap<String, String>();
    						map.put("line1", "Income <Total>");
    						map.put("line2", String.format("Amount: %1s, Count: %2s", total_amount, total_count));
    						result.add(map);
    						incomeCount ++;
    					}
    				}

					publishProgress("Fetch Expenses..");
					postparams.clear();
					postparams.put("dateFrom", params[0]);
					postparams.put("dateTo", params[1]);
					if (params[3] != null) {
						postparams.put("categoryIds", params[3]);
					}
					status = FintrackApplication.getInstance().doPost(uri_exp, postparams, sb);
					if (status != HttpStatus.SC_OK) {
						publishProgress("Status: " + status);
						return false;
					}

					total_amount = 0.0;
					total_count = 0;
					ArrayList<Summary> summaries = parser.parseSummaryResponse(sb.toString());
					for (Summary summary : summaries) {
						Map<String, String> map = new HashMap<String, String>();
						map.put("line1", String.format("Expense (%1s)", summary.group));
						map.put("line2", String.format("Amount: %1s, Count: %2s", summary.amount, summary.count));
						result.add(map);
						total_amount += summary.amount;
						total_count += summary.count;
					}
					if (summaries.size() > 1) {
						Map<String, String> map = new HashMap<String, String>();
						map.put("line1", "Expense <Total>");
						map.put("line2", String.format("Amount: %1s, Count: %2s", total_amount, total_count));
						result.add(map);
					}
					return true;
				} catch (Exception e) {
					publishProgress("Error: " + e.toString());
					Log.e(getClass().getSimpleName(), e.toString());
					return false;
				}
    		}

			@Override
			protected void onPostExecute(Boolean success) {
				Log.d(getClass().getSimpleName(), "onPostExecute()..");
				super.onPostExecute(success);

				Intent intent = new Intent(SelectActivity.this, ResultActivity.class);
				FintrackApplication.getInstance().setResult(result);
				intent.putExtra("incomeCount", incomeCount);
				intent.putExtra("lineCount", 2);
				startActivity(intent);
			}
    	};
    	execute(task);
	}

	private void execute (AsyncTaskWithProgress task) {
		String categoryId = null;
		Category category = categories_spinner_adapter.getItem(categories_spinner.getSelectedItemPosition());
		if (!"xx".equals(category.categoryId)) {
			categoryId = category.categoryId;
		}
    	task.execute(
			date_from_button.getText().toString(),
			date_to_button.getText().toString(),
			incomes_checkbox.isChecked() ? "checked" : null,
			categoryId);
	}
}

