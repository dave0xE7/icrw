package net.invidec.dev.icrw;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

public class WalletDashboard extends AppCompatActivity {

    public static String account, accountkey, accountname;

    public TextView textViewCharts, textViewBalance, textViewBalance2, textViewIcrAddress, textViewHeading;

    public static Double btceur, btcicr, icreur;
    public static String balance, address;
    public static Double balanceIcr, balanceEur, balanceBtc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_dashboard);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbarMain);
        setSupportActionBar(myToolbar);

        //handleSharedPreferences();

        textViewCharts = findViewById(R.id.textViewCharts);
        textViewBalance= findViewById(R.id.textViewBalance);
        textViewBalance2= findViewById(R.id.textViewBalance2);
        textViewIcrAddress= findViewById(R.id.textViewIcrAddress);
        textViewHeading= findViewById(R.id.textViewHeading);

        icreur = 0d; btceur = 0d; btcicr = 0d;
        balanceBtc = 0d; balanceEur = 0d; balanceIcr = 0d;

        UpdateEURBTC();
        UpdateBTCICR();

        GetBalance();

    }

    public void UpdateValues () {
        icreur = (btceur * btcicr);
        textViewCharts.setText("1 BTC = "+btceur.toString()+" EUR\n1 ICR = "+String.format("%.6f", btcicr)+" BTC\n1 ICR = "+String.format("%.6f", icreur)+" EUR");
        balanceIcr = Double.parseDouble(balance);
        balanceEur = balanceIcr*icreur;
        balanceBtc = balanceIcr*btcicr;
        //textViewBalance.setText(balanceIcr+" ICR");
        textViewBalance.setText(String.format("%.2f", balanceIcr)+" ICR");
        //textViewBalance2.setText(balanceEur+" EUR / "+balanceBtc+" BTC");
        textViewBalance2.setText(String.format("%.2f", balanceEur)+" EUR / "+String.format("%.6f", balanceBtc)+" BTC");
    }

    public void GetBalance () {
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
    }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Toolbar tb = (Toolbar) findViewById(R.id.toolbarMain);
        tb.inflateMenu(R.menu.toolbar);
        tb.setOnMenuItemClickListener(
                new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        return onOptionsItemSelected(item);
                    }
                });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_sync:
                Toast.makeText(this, "Sync Balance", Toast.LENGTH_SHORT).show();
                GetBalance();
                break;
            // action with ID action_settings was selected
            case R.id.action_settings:
                //Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT).show();
                gotoAbout();
                break;
            case R.id.action_export:
                Toast.makeText(this, "Exporting", Toast.LENGTH_SHORT).show();
                ExportAccount();
                break;
            default:
                break;
        }

        return true;
    }


    public void GetPriceCharts () {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Configuration.getApp_pricecharts(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response ", response);
                        try {
                            //JSONObject responseJson = new JSONObject(response.toString());
                            JSONObject result = new JSONObject(response);
                            //JSONObject result = responseJson.getJSONObject("result");
                            btceur = Double.parseDouble(result.getString("btceur"));
                            btcicr = Double.parseDouble(result.getString("icrbtc"));
                            icreur = Double.parseDouble(result.getString("icreur"));
                            Log.d("Price Charts", btceur+" "+btcicr+" "+icreur);
                            textViewCharts.setText("1 BTC = "+btceur+" EUR\n1 ICR = "+btcicr+" BTC\n1 ICR = "+icreur+" EUR");

                        } catch (Exception e) {
                            Log.d("GetPrice Exception", e.toString());
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

    protected void show_alert(String msg) {
        AlertDialog.Builder dialog=new AlertDialog.Builder(WalletDashboard.this);
        dialog.setMessage(msg);
        dialog.setPositiveButton("Ok",new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }

        });
        dialog.show();
    }

    public void CopyAddress (View view) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("ICRAddress", address);
        clipboard.setPrimaryClip(clip);
        //show_alert("Copied to Clipboard!");
    }

    public void gotoSend (View view) {
        Intent intent = new Intent(this, WalletSend.class);
        startActivity(intent);
    }
    public void gotoReceive (View view) {
        Intent intent = new Intent(this, WalletReceive.class);
        startActivity(intent);
    }
    public void gotoTransactions (View view) {
        Intent intent = new Intent(this, WalletTransactions.class);
        startActivity(intent);
    }
    public void gotoAbout () {
        Intent intent = new Intent(this, AboutScrollActivity.class);
        startActivity(intent);
    }
    // TODO: IMPORT AND EXPORT
    public void ExportAccount () {
        String filename = "account_backup";
        String fileContents = account+":"+accountkey;
        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(fileContents.getBytes());
            outputStream.close();
            Log.d("Export File Path: ",getFilesDir().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
