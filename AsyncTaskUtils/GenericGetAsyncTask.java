package com.littlefroginc.rollnearn.httphandler;

import java.util.HashMap;
import java.io.InputStream;
import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;
import android.content.Context;
import android.app.ProgressDialog;

public class GenericGetAsyncTask extends AsyncTask<HashMap<String,Object>,Void,String>
{
    private Context context;
    private Activity activity;
    private HttpHandler httpHandler;
    private AsyncTaskHandler handler;
    private ProgressDialog progressDialog;

    public GenericGetAsyncTask(Activity activity,final Context context,AsyncTaskHandler<String> handler)
    {
        this.activity        = activity;
        this.context         = context;
        this.handler         = handler;
        this.httpHandler     = new HttpHandler(context);

        activity.runOnUiThread(new Runnable()
        {
            public void run()
            {
                progressDialog = new ProgressDialog(context);
                progressDialog.setTitle("Please Wait");
            }
        });
    }

    @Override
    protected String doInBackground(final HashMap<String,Object>... params)
    {
        final InputStream[] inputStream = new InputStream[1];

        String url = (String)((HashMap<String,Object>)params[0]).get("URL");

        String response = httpHandler.GET( url );

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
                    progressDialog.show();
                }
            });
        }
    }

    @Override
    protected void onPostExecute(final String response)
    {
        super.onPostExecute(response);

        if(response != null)
            handler.handlePostTask(response);

        progressDialog.dismiss();
    }

    @Override
    protected void onProgressUpdate(Void... values)
    {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled(final String result)
    {
        super.onCancelled();

        progressDialog.dismiss();
    }

    @Override
    protected void onCancelled()
    {
        super.onCancelled();

        progressDialog.dismiss();
    }
}
