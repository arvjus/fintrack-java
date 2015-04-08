package org.zv.fintrack;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Button;

import org.apache.http.HttpStatus;
import org.zv.fintrack.model.Income;
import org.zv.fintrack.model.Expense;
import org.zv.fintrack.sax.XmlParser;
import org.zv.fintrack.service.AsyncTaskWithProgress;

public class RecentActivity extends Activity {
	private Spinner income_spinner;
	private Spinner expense_spinner;
	private Button search_button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recent);

        // setup ui controls
        ArrayAdapter<CharSequence> income_spinner_adapter = ArrayAdapter.createFromResource(this, R.array.recent_spinner_entries, android.R.layout.simple_spinner_item);
        income_spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        income_spinner = (Spinner)findViewById(R.id.recent_income_spinner);
        income_spinner.setAdapter(income_spinner_adapter);
        income_spinner.setSelection(1);

        ArrayAdapter<CharSequence> expense_spinner_adapter = ArrayAdapter.createFromResource(this, R.array.recent_spinner_entries, android.R.layout.simple_spinner_item);
        expense_spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        expense_spinner = (Spinner)findViewById(R.id.recent_expense_spinner);
        expense_spinner.setAdapter(expense_spinner_adapter);
        expense_spinner.setSelection(2);

        search_button = (Button)findViewById(R.id.recent_search_button);
        search_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
        		retrieveData();
            }
        });
	}
	
	void retrieveData() {
		AsyncTaskWithProgress task = new AsyncTaskWithProgress(this, "Fetching Resent Records") {
    		private ArrayList<Map<String,String>> result = new ArrayList<Map<String,String>>();
    		private int incomeCount;

    		@Override
    		protected Boolean doInBackground(String... params) {
				Log.d(getClass().getSimpleName(), "doInBackground()..");
				int status;
				StringBuilder sb = new StringBuilder();
    			try {
					publishProgress("Fetch Incomes..");
					status = FintrackApplication.getInstance().doGet("/rest/incomes/latest-" + params[0], sb);
					if (status != HttpStatus.SC_OK) {
						publishProgress("Status: " + status);
						return false;
					}
					XmlParser parser = new XmlParser();
					ArrayList<Income> incomes = parser.parseIncomesResponse(sb.toString());
					for (Income income : incomes) {
						Map<String,String> map = new HashMap<String,String>();
						map.put("line1", "Income");
						map.put("line2", String.format("%1s [%2s]", income.createDate, income.userId));
						map.put("line3", income.amount + (income.descr.length() > 0 ? " - " + income.descr : ""));
						result.add(map);
					}
					incomeCount = incomes.size();

					publishProgress("Fetch Expenses..");
					status = FintrackApplication.getInstance().doGet("/rest/expenses/latest-"+ params[1], sb);
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

				Intent intent = new Intent(RecentActivity.this, ResultActivity.class);
				FintrackApplication.getInstance().setResult(result);
				intent.putExtra("incomeCount", incomeCount);
				intent.putExtra("lineCount", 3);
				startActivity(intent);
			}
    	};
    	
    	// retrieve spinner values and execute query
    	Object income_selection = income_spinner.getSelectedItem();
    	if (income_selection == null) {
    		income_selection = "5";
    	}
    	Object expense_selection = expense_spinner.getSelectedItem();
    	if (expense_selection == null) {
    		expense_selection = "10";
    	}
    	task.execute(income_selection.toString(), expense_selection.toString());
	}
}
