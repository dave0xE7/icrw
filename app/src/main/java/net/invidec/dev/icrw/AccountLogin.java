package net.invidec.dev.icrw;

import android.app.DownloadManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AccountLogin extends AppCompatActivity {

    public TextView textViewError, textViewMessage;
    public String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_login);

        textViewError = findViewById(R.id.textViewError);
        textViewMessage = findViewById(R.id.textViewMessage);

    }



    public void handleLogin(View view) {
        EditText editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        email = editTextEmail.getText().toString();
        EditText editTextPass = (EditText) findViewById(R.id.editTextPass);
        password = editTextPass.getText().toString();

        textViewMessage.setText("Wird eingeloggt...");

        //requestLogin();
    }

    public void gotoDashboard() {
        Intent intent = new Intent(this, WalletDashboard.class);
        startActivity(intent);
    }

    /**
     public void requestLogin(final String email, final String password) {
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

        //final String auth_key = sharedPreferences.getString("auth_key", null);

        if (auth_key == null) {
            return;
        }

        StringRequest serverRequest = new StringRequest(Request.Method.POST, Configuration.getApp_auth(), new Response.Listener<String>() {
            @Override
            public void onResponse(String req) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("app_vname", app_vName);
                params.put("app_vcode", app_vCode);
                return params;
            }
        };

        requestQueue.add(serverRequest);
    }

    public void RequestJson () {
        String url = "http://my-json-feed";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (DownloadManager.Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        mTextView.setText("Response: " + response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                });

    // Access the RequestQueue through your singleton class.
    MySingleton.getInstance(this).

        addToRequestQueue(jsonObjectRequest);
    }**/

    /**public void requestLogin () {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://invidec.net/codiad/workspace/icrwallet/api.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("Response ", response.toString());
                    if (!response.toString().equals("false")) {
                        try {
                            JSONObject sessionData = new JSONObject(response.toString());
                            WalletDashboard.userid = sessionData.getString("userid");
                            WalletDashboard.token = sessionData.getString("token");

                        } catch (Exception e) {
                            Log.d("Exception ", e.toString());
                        }
                        textViewMessage.setText("Einloggen erfolgreich!");
                        textViewError.setText("");
                        gotoDashboard();
                    } else {
                        WalletDashboard.userid = response.toString();
                        textViewError.setText("Einloggen Fehlgeschlagen!");
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    textViewError.setText("Verbindung Fehlgeschlagen!");
                    Log.d("Error response", error.toString());
                }
            }) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                //add your parameters here as key-value pairs
                params.put("q", "login");
                params.put("email", email);
                params.put("password", password);

                return params;
            }};
        queue.add(stringRequest);
    }**/

}
