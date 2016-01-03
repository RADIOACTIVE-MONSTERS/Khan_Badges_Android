package com.example.tylerlacroix.khan;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by tylerlacroix on 15-12-28.
 */
public class ServerData {

    public interface ServerDataDelegate {
        void finishedLoading();
    }

    static private ArrayList<Badge> badges = new ArrayList<Badge>();
    static public HashMap<Integer, String> categories = new HashMap<Integer, String>();
    static private ServerDataDelegate delegate;
    static private ServerDataDelegate categoryDelegate;

    public static void loadData(ServerDataDelegate _delegate) {
        delegate = _delegate;
        (new AsyncTaskParseJsonBadges()).execute();
    }

    public static void loadCategories(ServerDataDelegate _delegate) {
        categoryDelegate = _delegate;
        (new AsyncTaskParseJsonCategories()).execute();
    }

    public static ArrayList<Badge> getBadges(int category) {
        if (category == -1) {
            return new ArrayList<Badge>(badges);
        }
        ArrayList<Badge> ret = new ArrayList<Badge>();
        for (int i = 0; i < badges.size();i++) {
            Badge badge = badges.get(i);
            if (badge.category == category)
                ret.add(badge);
        }
        return ret;
    }

    public static class AsyncTaskParseJsonBadges extends AsyncTask<String, String, String> {

        HttpURLConnection urlConnection;

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... arg0) {
            StringBuilder result = new StringBuilder();

            try {
                URL url = new URL("http://www.khanacademy.org/api/v1/badges");
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

            }catch( Exception e) {
                e.printStackTrace();
            }
            finally {
                urlConnection.disconnect();
            }

            try {
                badges.clear();

                JSONArray jsonArray = new JSONArray(result.toString());

                for(int i=0;i<jsonArray.length();i++){
                    JSONObject badgeJSON = jsonArray.getJSONObject(i);
                    JSONObject icons = badgeJSON.getJSONObject("icons");
                    Badge badge = new Badge();
                    badge.icon = icons.getString("email");
                    badge.descript = badgeJSON.getString("description");
                    badge.description_extended = badgeJSON.getString("safe_extended_description");
                    badge.category = badgeJSON.getInt("badge_category");
                    badge.image = icons.getString("large");
                    badge.points = badgeJSON.getInt("points");
                    badges.add(badge);
                }


                Collections.sort(badges);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return result.toString();
        }

        @Override
        protected void onPostExecute(String strFromDoInBg) {
            delegate.finishedLoading();
        }
    }

    public static class AsyncTaskParseJsonCategories extends AsyncTask<String, String, String> {

        HttpURLConnection urlConnection;

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... arg0) {
            StringBuilder result = new StringBuilder();

            try {
                URL url = new URL("http://www.khanacademy.org/api/v1/badges/categories");
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

            }catch( Exception e) {
                e.printStackTrace();
            }
            finally {
                urlConnection.disconnect();
            }

            try {
                categories.clear();

                JSONArray jsonArray = new JSONArray(result.toString());

                for(int i=0;i<jsonArray.length();i++) {
                    JSONObject badgeJSON = jsonArray.getJSONObject(i);
                    categories.put(badgeJSON.getInt("category"), badgeJSON.getString("type_label"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return result.toString();
        }

        @Override
        protected void onPostExecute(String strFromDoInBg) {
            categoryDelegate.finishedLoading();
        }
    }

}
