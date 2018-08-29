package net.invidec.dev.icrw;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.SharedPreferences;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class WalletDashboard extends AppCompatActivity {

    public static String userid, token, name1, name2, email;

    public TextView textViewCharts, textViewBalance;
    public Button buttonBalance;

    public float btceur, btcicr, icreur;
    public static String balance, address;

    private void handleSharedPreferences () {
        SharedPreferences sharedPref = WalletDashboard.this.getPreferences(Context.MODE_PRIVATE);

        //userid = sharedPref.getString(getString(R.string.saved_userid_key), "");
        //token= sharedPref.getString(getString(R.string.saved_token_key), "");

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.saved_userid_key), userid);
        editor.putString(getString(R.string.saved_token_key), token);
        editor.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_dashboard);

        handleSharedPreferences();

        textViewCharts = findViewById(R.id.textViewCharts);
        textViewBalance= findViewById(R.id.textViewBalance);
        buttonBalance = findViewById(R.id.buttonBalance);

        RetrieveCharts();
        textViewBalance.setText(userid);


        RetrieveAllData();

    }

    public void onClickBalance (View view) {


    }

    public void RetrieveAllData () {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://invidec.net/codiad/workspace/icrwallet/api.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response ", response.toString());
                        textViewBalance.setText(response.toString());
                        try {
                            JSONObject alldata = new JSONObject(response.toString());
                            JSONObject user = alldata.getJSONObject("user");
                            JSONObject wallet = alldata.getJSONObject("wallet");
                            //String userid  = user.getString("userid");
                            name1 = user.getString("name1");
                            name2 = user.getString("name2");
                            email = user.getString("email");

                            balance = wallet.getString("balance");
                            address = wallet.getString("address");

                            //textViewBalance.setText("Welcome "+name1+" "+name2);
                            textViewBalance.setText("Welcome "+name1+" "+name2+" "+address);

                            buttonBalance.setText(""+balance+" ICR");

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
                params.put("q", "alldata");
                params.put("userid", userid);
                params.put("token", token);
                return params;
            }};
        queue.add(stringRequest);
    }

    public void RetrieveCharts () {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://invidec.net/codiad/workspace/icrwallet/api.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response ", response.toString());
                        try {
                            JSONObject jsoncharts = new JSONObject(response.toString());
                            btceur = Float.parseFloat(jsoncharts.getString("btceurval"));
                            btcicr = Float.parseFloat(jsoncharts.getString("icrbtcval"));
                            icreur = Float.parseFloat(jsoncharts.getString("icreurval"));
                            Button buttonVal1 = findViewById(R.id.buttonVal1);
                            buttonVal1.setText("BTC>EUR\n"+btceur);
                            Button buttonVal2 = findViewById(R.id.buttonVal2);
                            buttonVal2.setText("ICR>BTC\n"+btcicr);
                            Button buttonVal3 = findViewById(R.id.buttonVal3);
                            buttonVal3.setText("ICR>EUR\n"+icreur);
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
                params.put("q", "charts");
                return params;
            }};
        queue.add(stringRequest);
    }

    public void gotoSend (View view) {
        Intent intent = new Intent(this, WalletSend.class);
        startActivity(intent);
    }
    public void gotoReceive (View view) {
        Intent intent = new Intent(this, WalletReceive.class);
        startActivity(intent);
    }
}
