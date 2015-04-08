package org.zv.fintrack;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.app.DatePickerDialog;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import org.apache.http.HttpStatus;

import org.zv.fintrack.service.AsyncTaskWithProgress;

public class IncomeActivity extends Activity {
	private static final int CREATE_DATE_DIALOG_ID = 0;
	private static final int CONFIRM_DIALOG_ID = 1;
	private static final int ERROR_DIALOG_ID = 2;
	private Calendar create_date;
	private Button create_date_button;
	private EditText amount_edittext;
	private EditText descr_edittext;
	private Button save_button;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.income);
        
        // setup ui controls
        create_date_button = (Button)findViewById(R.id.income_create_date_button);
        create_date_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	showDialog(CREATE_DATE_DIALOG_ID);
            }
        });
        
        amount_edittext = (EditText)findViewById(R.id.income_amount_edittext);
        descr_edittext = (EditText)findViewById(R.id.income_descr_edittext);
        
        save_button = (Button)findViewById(R.id.income_save_button);
		save_button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (amount_edittext.getText().toString().trim().length() > 0) {
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
			dialog.setTitle("Create new income..");
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
		AsyncTaskWithProgress task = new AsyncTaskWithProgress(this, "Creating New Income") {
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
					postparams.put("amount", params[2]);
					postparams.put("descr", params[3]);
					status = FintrackApplication.getInstance().doPost("/rest/incomes/new", postparams, sb);
					if (status == HttpStatus.SC_OK) {
						publishProgress("A new income record is created");
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
    				amount_edittext.setText("");
    				descr_edittext.setText("");
    			}
    			super.onPostExecute(success);
			}
    	};

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    	task.execute(
			create_date_button.getText().toString(),
			prefs.getString("username", "none"),
			amount_edittext.getText().toString(),
			descr_edittext.getText().toString());
	}
}
