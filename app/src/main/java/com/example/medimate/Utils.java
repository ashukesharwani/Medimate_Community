package com.example.medimate;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ocpsoft.prettytime.PrettyTime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public final class Utils {

    public static final String LOG_TAG = com.example.medimate.Utils.class.getSimpleName();

    public Utils() {

    }

    public static List<Model> fetchNews (Context context, String requestUrl) {

        URL url =  createUrl(requestUrl);
        String jsonResponse = "";
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Model> news = extractFromJson(context, jsonResponse);
        return news;


    }

    private static List<Model> extractFromJson(Context context, String bookJson) {
        if (TextUtils.isEmpty(bookJson)) {
            return null;
        }

        List<Model> news = new ArrayList<>();

        try {
            JSONObject baseJsonResponse = new JSONObject(bookJson);
            JSONArray arrays = baseJsonResponse.getJSONArray("articles");

            for (int i = 0; i < arrays.length(); i++) {
                JSONObject getvalues = arrays.getJSONObject(i);
                JSONObject getsource = getvalues.getJSONObject("source");

                String title = getvalues.getString("title");
                String source = getsource.getString("name");
                String description = getvalues.getString("description");
                String url = getvalues.getString("url");
                String urlToImage = getvalues.getString("urlToImage");
                String time = getvalues.getString("publishedAt");

                Model models = new Model(title, time, description, source, url, urlToImage);
                news.add(models);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return news;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);


            } else {
                Log.e(LOG_TAG, "ERROR WITH RESPONSE CODE");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection!=null) {
                urlConnection.disconnect();
            }

            if (inputStream!=null) {
                inputStream.close();
            }
        }

        return jsonResponse;

    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream!=null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();

            while (line!=null) {
                output.append(line);
                line = reader.readLine();
            }
        }

        return output.toString();
    }


    private static URL createUrl(String stringURL) {
        URL url = null;

        try {
            url = new URL(stringURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        return url;
    }


    public static String DateToTimeFormat(String oldstringDate){
        PrettyTime p = new PrettyTime(new Locale(getCountry()));
        String isTime = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'",
                    Locale.ENGLISH);
            Date date = sdf.parse(oldstringDate);
            isTime = p.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return isTime;
    }


    public static String DateFormat(String oldstringDate){
        String newDate;
        SimpleDateFormat dateFormat = new SimpleDateFormat("E, d MMM yyyy", new Locale(getCountry()));
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(oldstringDate);
            newDate = dateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            newDate = oldstringDate;
        }

        return newDate;
    }

    public static String getCountry(){
        Locale locale = Locale.getDefault();
        String country = String.valueOf(locale.getCountry());
        return country.toLowerCase();
    }
}