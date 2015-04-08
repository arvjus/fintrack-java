package org.zv.fintrack;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import org.apache.http.HttpStatus;

import org.zv.fintrack.service.AsyncTaskWithProgress;
import org.zv.fintrack.model.Category;
import org.zv.fintrack.sax.XmlParser;

public class FintrackActivity extends Activity {
	private Button recent_button;
	private Button income_button;
	private Button expense_button;
	private Button select_button;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		Log.d(getClass().getSimpleName(), "onCreate()..");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // setup ui controls
        income_button = (Button)findViewById(R.id.main_income_button);
        income_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
    	        startActivity(new Intent(FintrackActivity.this, IncomeActivity.class));
            }
        });

        expense_button = (Button)findViewById(R.id.main_expense_button);
        expense_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
    	        startActivity(new Intent(FintrackActivity.this, ExpenseActivity.class));
            }
        });

        recent_button = (Button)findViewById(R.id.main_recent_button);
        recent_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
    	        startActivity(new Intent(FintrackActivity.this, RecentActivity.class));
            }
        });

        select_button = (Button)findViewById(R.id.main_select_button);
        select_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
    	        startActivity(new Intent(FintrackActivity.this, SelectActivity.class));
            }
        });
        
		retrieveData();
	}

    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
    	getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.main_menu_prefs_item:
			// set default prefs
	        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
	        String url = prefs.getString("service_location", null);
	        if (url == null || url.trim().length() == 0) {
		       	Editor editor = prefs.edit();
		    	editor.putString("service_location", "http://192.168.1.10:8080/fintrack");
		    	editor.commit();
	        }
	    	
	        startActivity(new Intent(this, PreferencesActivity.class));
			return true;
		case R.id.main_menu_connect_item:
			retrieveData();
			return true;
		case R.id.main_menu_close_item:
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void enableButtons(boolean enabled) {
		for (Button button : new Button [] { recent_button, income_button, expense_button, select_button }) {
			button.setEnabled(enabled);
		}
	}
	
	private void retrieveData() {
		AsyncTaskWithProgress task = new AsyncTaskWithProgress(this, "Initial connect") {
			@Override
			protected void onPreExecute() {
				Log.d(getClass().getSimpleName(), "onPreExecute()..");
				super.onPreExecute();
				enableButtons(false);
			}

			@Override
    		protected Boolean doInBackground(String... params) {
				Log.d(getClass().getSimpleName(), "doInBackground()..");
				int status;
				StringBuilder sb = new StringBuilder();
    			try {
					publishProgress("Authenticate..");
					status = FintrackApplication.getInstance().authenticate(); 
					if (status != HttpStatus.SC_OK) {
						publishProgress("Status: " + status);
						return false;
					}    				

					publishProgress("Fetch Categories..");
					status = FintrackApplication.getInstance().doGet("/rest/categories", sb);
					if (status != HttpStatus.SC_OK) {
						publishProgress("Status: " + status);
						return false;
					}
					XmlParser parser = new XmlParser();
					List<Category> categories = parser.parseCatetoriesResponse(sb.toString());
					FintrackApplication.getInstance().setCategories(categories);
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
				enableButtons(success);
			}
    	};
    	task.execute();
	}
}
