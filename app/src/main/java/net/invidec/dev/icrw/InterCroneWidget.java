package net.invidec.dev.icrw;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.util.Log;
import android.widget.RemoteViews;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of App Widget functionality.
 */
public class InterCroneWidget extends AppWidgetProvider {

    /*
    public void UpdateEURBTC () {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Configuration.apiUrl_EURBTC,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray list = new JSONArray(response.toString());
                            JSONObject item = list.getJSONObject(0);
                            btceur = Double.parseDouble(item.getString("open"));
                            Log.d("UpdateEURBTC", String.valueOf(btceur));
                            UpdateValues();

                        } catch (Exception e) { Log.d("UpdateEURBTC Exception ", e.toString()); }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error response", error.toString());
            }}) {
            @Override
            protected Map<String, String> getParams()
            { Map<String, String>  params = new HashMap<String, String>();
                //params.put("id", "0");
                return params; }};
        queue.add(stringRequest);
    }
    public void UpdateBTCICR () {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Configuration.apiUrl_BTCICR,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray list = new JSONArray(response.toString());
                            JSONObject item = list.getJSONObject(0);
                            btcicr = Double.parseDouble(item.getString("open"));
                            Log.d("UpdateBTCICR", String.valueOf(btcicr));
                            UpdateValues();

                        } catch (Exception e) { Log.d("UpdateBTCICR Exception ", e.toString()); }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error response", error.toString());
            }}) {
            @Override
            protected Map<String, String> getParams()
            { Map<String, String>  params = new HashMap<String, String>();
                //params.put("id", "0");
                return params; }};
        queue.add(stringRequest);
    }

    public static void GetPriceCharts (Context context) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Configuration.getApp_auth(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("GetBalance Response ", response);
                        //textViewBalance.setText(response.toString());
                        try {
                            JSONObject jsondata = new JSONObject(response);
                            JSONObject result = jsondata.getJSONObject("result");
                            //String userid  = user.getString("userid");

                            balance = result.getString("balance");
                            address = result.getString("address");

                            //balanceIcr = Double.parseDouble(balance);
                            //balanceEur = balanceIcr * icreur;
                            //balanceBtc = balanceIcr * btcicr;

                            //textViewBalance.setText(balanceIcr+" ICR");
                            //textViewBalance2.setText(balanceEur+" EUR");

                            UpdateValues();

                            textViewIcrAddress.setText(address);

                        } catch (Exception e) {
                            Log.d("GetBalance Exception ", e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("GetBalance Error", error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("id", "0");
                params.put("method", "getBalance");
                params.put("account", account);
                params.put("key", accountkey);
                return params;
            }};
        queue.add(stringRequest);

        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Configuration.getApp_pricecharts(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response ", response.toString());
                        try {
                            //JSONObject responseJson = new JSONObject(response.toString());
                            JSONObject result = new JSONObject(response.toString());
                            //JSONObject result = responseJson.getJSONObject("result");
                            Float btceur = Float.parseFloat(result.getString("btceur"));
                            Float btcicr = Float.parseFloat(result.getString("icrbtc"));
                            Float icreur = Float.parseFloat(result.getString("icreur"));
                            //Log.d("Price Charts", btceur+" "+btcicr+" "+icreur);
                            widgetText="1 BTC = "+btceur+" EUR\n1 ICR = "+icreur+" EUR";


                        } catch (Exception e) {
                            Log.d("Exception ", e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error response", error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                //params.put("method", "getPriceCharts");
                return params;
            }};
        queue.add(stringRequest);

    }
    */

    public static CharSequence widgetText = "InterCrone";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        //Log.d("widget update", "now");

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.inter_crone_widget);
        views.setTextViewText(R.id.appwidget_text, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        //GetPriceCharts(context);
        widgetText = "work in progress";
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        //GetPriceCharts(context);
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

