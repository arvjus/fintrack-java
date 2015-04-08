package org.zv.fintrack.service;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.Toast;

public abstract class AsyncTaskWithProgress extends AsyncTask<String, String, Boolean> {
	protected Context context;
	protected String title;
	protected boolean showToast;
	protected ProgressDialog progressDialog;
	
	public AsyncTaskWithProgress(Context context, String title) {
		this.context = context;
		this.title = title;

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        this.showToast = prefs.getBoolean("show_toast", false);
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (context != null) {
			progressDialog = ProgressDialog.show(context, title, null, true, false);
            progressDialog.setCanceledOnTouchOutside(true);
            progressDialog.setOnCancelListener(
            	new DialogInterface.OnCancelListener() {
        			@Override
        			public void onCancel(DialogInterface dialog) {
        				AsyncTaskWithProgress.this.cancel(true);
        				if (showToast) {
            				Toast.makeText(AsyncTaskWithProgress.this.context, title + " canceled.", Toast.LENGTH_SHORT).show();
        				}
        			}
        		}
            );
		}
	}

	protected void onProgressUpdate(String... progress) {
		if (progressDialog != null) {
			progressDialog.setMessage(progress[0]);
		}
	}
	
	protected void onPostExecute(Boolean success) {
		if (context != null && progressDialog != null) {
			if (success) {
				progressDialog.dismiss();
			}
			if (showToast) {
				Toast.makeText(context, title + (success ? " - success." : " - failure."), Toast.LENGTH_SHORT).show();
			}
		}
	}
}
