package net.invidec.dev.icrw;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import static net.invidec.dev.icrw.WalletDashboard.account;
import static net.invidec.dev.icrw.WalletDashboard.accountkey;

public class WalletTransactions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_transactions);

        textViewListTransactions = findViewById(R.id.textViewTransactionList);

        ListTransactions();

    }

    TextView textViewListTransactions;

    public String FormatTime (String unixtime) {
        //Unix seconds
        //convert seconds to milliseconds
        Date date = new Date(Long.parseLong(unixtime)*1000L);
        // format of the date
        //SimpleDateFormat jdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        SimpleDateFormat jdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        jdf.setTimeZone(TimeZone.getTimeZone("GMT+1"));
        String java_date = jdf.format(date);
        return java_date;
    }

    public void ListTransactions () {
        RequestQueue requestQueue = Volley.newRequestQueue(WalletTransactions.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Configuration.getApp_auth(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response ", response.toString());
                        //textViewListTransactions.setText(response.toString());
                        //show_alert("sent");
                        try {
                            JSONObject jsondata = new JSONObject(response.toString());
                            JSONArray result = jsondata.getJSONArray("result");


                            String list = "";

                            for (int i = 0; i < result.length(); i++) {
                                JSONObject item = result.getJSONObject(i);
                                //Log.d("list length", String.valueOf(result.length())+itemAmount);

                                String itemCategory = item.getString("category");
                                String itemTime = item.getString("time");
                                String itemAddress = "";

                                if (itemCategory.contains("move")) {
                                    //itemAddress = item.getString("account");
                                    itemAddress = "neighbor";
                                } else {
                                    itemAddress = item.getString("address");
                                }

                                String itemAmount= item.getString("amount");
                                String confirmations="10";
                                list = list +"["+FormatTime(itemTime)+"] "+itemAmount+" ICR\n"+itemAddress+"\n"+confirmations+" Confirmations \n\n";
                            }

                            textViewListTransactions.setText(list);
                            //String txid = result.getString("txid");

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
                params.put("id", "0");
                params.put("method", "listTransactions");
                params.put("account", account);
                params.put("key", accountkey);
                return params;
            }};
        requestQueue.add(stringRequest);
    }
}
