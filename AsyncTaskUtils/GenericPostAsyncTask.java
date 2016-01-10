package com.littlefroginc.rollnearn.httphandler;

import java.util.HashMap;
import java.io.InputStream;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.view.ContextThemeWrapper;
import android.widget.Toast;
import android.content.Context;
import android.app.ProgressDialog;

import com.littlefroginc.rollnearn.R;

public class GenericPostAsyncTask extends AsyncTask<HashMap<String,Object>,Void, String>
{
    private Context context;
    private Activity activity;
    private HttpHandler httpHandler;
    private AsyncTaskHandler handler;
    private ProgressDialog progressDialog;

    public GenericPostAsyncTask(Activity activity,Context context,AsyncTaskHandler<String> handler)
    {
        this.activity        = activity;
        this.context         = context;
        this.handler         = handler;
        this.httpHandler     = new HttpHandler(context);
    }

    @Override
    protected String doInBackground(HashMap<String,Object>... params)
    {
        final InputStream[] inputStream = new InputStream[1];

        String url = (String)((HashMap<String,Object>)params[0]).get("URL") == null ? "" : (String)((HashMap<String,Object>)params[0]).get("URL");

        String parameters = (String)((HashMap<String,Object>)params[0]).get("PARAMETERS") == null ? "" : (String)((HashMap<String,Object>)params[0]).get("PARAMETERS");

        String response = httpHandler.POST( url, parameters);

        return response;
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();

        if(!httpHandler.checkConnection())
        {
            activity.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    Toast.makeText(context, "Error Connecting to Network. Please check your internet", Toast.LENGTH_LONG).show();
                }
            });
        }
        else
        {
            activity.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    if(progressDialog == null && !activity.isFinishing())
                    {
                        //progressDialog = new ProgressDialog(activity,AlertDialog.THEME_HOLO_LIGHT)ProgressDialog.show(activity,"Please Wait", "", true);
                        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB)
                        {
                            progressDialog = new ProgressDialog(new ContextThemeWrapper(activity, android.R.style.Theme_Holo_Light));
                            progressDialog.setTitle("Please Wait");
                            progressDialog.show();
                        }
                        else
                        {
                            progressDialog = new ProgressDialog(activity);
                            progressDialog.setTitle("Please Wait");
                            progressDialog.show();
                        }

                    }
                }
            });
        }
    }

    @Override
    protected void onPostExecute(String result)
    {
        super.onPostExecute(result);

        if(result != null)
            handler.handlePostTask(result);

        if (activity.isFinishing())
        {
            if( progressDialog != null && progressDialog.isShowing())
            {
                progressDialog.dismiss();
            }

            progressDialog = null;

            return;
        }

        if( progressDialog != null && progressDialog.isShowing())
        {
            progressDialog.dismiss();
        }

        progressDialog = null;
    }

    @Override
    protected void onProgressUpdate(Void... values)
    {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled(String result) {
        super.onCancelled();

        if( progressDialog != null && progressDialog.isShowing())
        {
            progressDialog.dismiss();
        }

        progressDialog = null;
    }

    @Override
    protected void onCancelled()
    {
        super.onCancelled();

        if( progressDialog != null && progressDialog.isShowing())
        {
            progressDialog.dismiss();
        }

        progressDialog = null;
    }

    public void dismissDialog()
    {
        if( progressDialog != null && progressDialog.isShowing())
        {
            progressDialog.dismiss();
        }

        progressDialog = null;
    }
}
