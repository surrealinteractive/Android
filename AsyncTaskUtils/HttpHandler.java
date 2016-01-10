package com.littlefroginc.rollnearn.httphandler;

import java.net.URL;
import java.util.Map;
import android.util.Log;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.BufferedReader;
import android.content.Context;
import android.net.NetworkInfo;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.io.BufferedOutputStream;
import android.net.ConnectivityManager;

public class HttpHandler
{
    private Context context;
    private static final char PARAMETER_DELIMITER   = '&';
    private static final char PARAMETER_EQUALS_CHAR = '=';

    public HttpHandler(Context context)
    {
        this.context = context;
    }

    public boolean checkConnection()
    {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected())
        {
            return true;
        }

        return false;
    }

    public String POST(String urlString,String parameterString)
    {
        String response = null;

        try
        {
            URL url = new URL(urlString);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoInput(true);
            conn.setFixedLengthStreamingMode(parameterString.getBytes().length);

            OutputStream os = new BufferedOutputStream(conn.getOutputStream());
            os.write(parameterString.getBytes());
            os.close();

            int responseCode = conn.getResponseCode();

            Log.d("RESPONSE CODE:", responseCode + "");

            InputStream inputStream = conn.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();

            String line = "";

            while ((line = reader.readLine()) != null)
            {
                stringBuilder.append(line + "\n");
            }

            Log.d("RESPONSE :", stringBuilder.toString());

            inputStream.close();
            reader.close();

            response = stringBuilder.toString();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return response;
    }

    public String POST(String urlString,Map<String,String> parameters)
    {
        String response = null;

        String parameterString = createQueryStringForParameter(parameters);

        try
        {
            URL url = new URL(urlString);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoInput(true);
            conn.setFixedLengthStreamingMode(parameterString.getBytes().length);

            PrintWriter writer = new PrintWriter(conn.getOutputStream());
            writer.print(parameterString);
            writer.close();

            int responseCode = conn.getResponseCode();

            Log.d("RESPONSE CODE:", responseCode + "");

            InputStream inputStream = conn.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();

            String line = "";

            while ((line = reader.readLine()) != null)
            {
                stringBuilder.append(line + "\n");
            }

            Log.d("RESPONSE :", stringBuilder.toString());

            inputStream.close();
            reader.close();

            response = stringBuilder.toString();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return response;
    }

    public String GET(String urlString)
    {
        String response = null;

        try
        {
            URL url = new URL(urlString);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            conn.connect();

            int responseCode = conn.getResponseCode();

            Log.d("RESPONSE CODE:", responseCode + "");

            InputStream inputStream = conn.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();

            String line = "";

            while ((line = reader.readLine()) != null)
            {
                stringBuilder.append(line + "\n");
            }

            Log.d("RESPONSE :", stringBuilder.toString());

            inputStream.close();
            reader.close();

            response = stringBuilder.toString();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return response;
    }

    public static String createQueryStringForParameter(Map<String,String> parameter)
    {
        StringBuilder parameterQueryString = new StringBuilder();

        try
        {
            if (parameter != null)
            {
                boolean firstParameter = true;

                for (String parameterName : parameter.keySet())
                {
                    if (!firstParameter)
                        parameterQueryString.append(PARAMETER_DELIMITER);

                    parameterQueryString.append(parameterName).append(PARAMETER_EQUALS_CHAR).
                            append(URLEncoder.encode(parameter.get(parameterName),"UTF-8"));
                }
            }
        }
        catch(Exception ex)
        {
            Log.e("createQueryString", ex.getMessage());
        }

        return parameterQueryString.toString();
    }
}
