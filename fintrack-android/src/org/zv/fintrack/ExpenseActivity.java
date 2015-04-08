package org.zv.fintrack;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import org.apache.http.HttpStatus;

import org.zv.fintrack.model.Category;
import org.zv.fintrack.service.AsyncTaskWithProgress;

public class ExpenseActivity extends Activity {
	private static final int CREATE_DATE_DIALOG_ID = 0;
	private static final int CONFIRM_DIALOG_ID = 1;
	private static final int ERROR_DIALOG_ID = 2;
	private Calendar create_date;
	private Button create_date_button;
	private Spinner categories_spinner;
	private ArrayAdapter<Category> categories_spinner_adapter;
	private EditText amount_edittext;
	private EditText descr_edittext;
	private Button save_button;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expense);
        
        // setup ui controls
        create_date_button = (Button)findViewById(R.id.expense_create_date_button);
        create_date_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	showDialog(CREATE_DATE_DIALOG_ID);
            }
        });
        
        Category none = new Category();
        none.categoryId = "xx";
        none.name = "<None>";
        none.nameShort = "<None>";
        List<Category> categories = new ArrayList<Category>();
        categories.add(none);
        categories.addAll(FintrackApplication.getInstance().getCategories());
        categories_spinner_adapter = new ArrayAdapter<Category>(this, android.R.layout.simple_spinner_item, categories);  
        categories_spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categories_spinner = (Spinner) findViewById(R.id.expense_categories_spinner);
        categories_spinner.setAdapter(categories_spinner_adapter);
		categories_spinner.setSelection(0);    				

        amount_edittext = (EditText)findViewById(R.id.expense_amount_edittext);
        descr_edittext = (EditText)findViewById(R.id.expense_descr_edittext);
        
        save_button = (Button)findViewById(R.id.expense_save_button);
        save_button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (categories_spinner.getSelectedItemPosition() != AdapterView.INVALID_POSITION &&
					categories_spinner.getSelectedItemPosition() != 0 &&
					amount_edittext.getText().toString().trim().length() > 0) {
	            	showDialog(CONFIRM_DIALOG_ID);
				} else {
	            	showDialog(ERROR_DIALOG_ID);
				}
			}
        });

        create_date = Calendar.getInstance();
		create_date_button.setText(String.format("%4d-%02d-%02d", create_date.get(Calendar.YEAR), create_date.get(Calendar.MONTH) + 1, create_date.get(Calendar.DAY_OF_MONTH)));
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		AlertDialog dialog;
	    switch (id) {
	    case CREATE_DATE_DIALOG_ID:
	        return new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					create_date.set(Calendar.YEAR, year);
					create_date.set(Calendar.MONTH, monthOfYear);
					create_date.set(Calendar.DAY_OF_MONTH, dayOfMonth);
					create_date_button.setText(String.format("%4d-%02d-%02d", create_date.get(Calendar.YEAR), create_date.get(Calendar.MONTH) + 1, create_date.get(Calendar.DAY_OF_MONTH)));
				}
	        },
	        create_date.get(Calendar.YEAR), create_date.get(Calendar.MONTH), create_date.get(Calendar.DAY_OF_MONTH));
		case CONFIRM_DIALOG_ID:
			dialog = new AlertDialog.Builder(this).create();
			dialog.setTitle("Create new expense..");
			dialog.setButton("OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						createNew();
					}
				});
			dialog.setButton(Dialog.BUTTON_NEGATIVE, "Cancel", (Message)null);
			return dialog;
		case ERROR_DIALOG_ID:
			dialog = new AlertDialog.Builder(this).create();
			dialog.setTitle("You must select values..");
			dialog.setCancelable(true);
			dialog.setCanceledOnTouchOutside(true);
			dialog.setButton(Dialog.BUTTON_NEUTRAL, "Close", (Message)null);
			return dialog;
	    }
		return super.onCreateDialog(id);
	}

	private void createNew() {
		AsyncTaskWithProgress task = new AsyncTaskWithProgress(this, "Creating New Expense") {
    		@Override
    		protected Boolean doInBackground(String... params) {
				Log.d(getClass().getSimpleName(), "doInBackground()..");
				int status;
				StringBuilder sb = new StringBuilder();
				Map<String, String> postparams = new HashMap<String, String>();
    			try {
					postparams.clear();
					postparams.put("createDate", params[0]);
					postparams.put("userId", params[1]);
					postparams.put("categoryId", params[2]);
					postparams.put("amount", params[3]);
					postparams.put("descr", params[4]);
					status = FintrackApplication.getInstance().doPost("/rest/expenses/new", postparams, sb);
					if (status == HttpStatus.SC_OK) {
						publishProgress("A new expense record is created");
						return true;
					} else {
						publishProgress("Status: " + status);
						return false;
					}
				} catch (Exception e) {
					publishProgress("Error: " + e.toString());
					Log.e(getClass().getSimpleName(), e.toString());
					return false;
				}
    		}

			@Override
			protected void onPostExecute(Boolean success) {
    			if (success) {
    				categories_spinner.setSelection(0);    				
    				amount_edittext.setText("");
    				descr_edittext.setText("");
    			}
    			super.onPostExecute(success);
			}
    	};

    	Category category = categories_spinner_adapter.getItem(categories_spinner.getSelectedItemPosition());
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    	task.execute(
			create_date_button.getText().toString(),
			prefs.getString("username", "none"),
			category.categoryId,
			amount_edittext.getText().toString(),
			descr_edittext.getText().toString());
	}
}
