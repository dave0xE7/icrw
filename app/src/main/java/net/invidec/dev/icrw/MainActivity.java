package net.invidec.dev.icrw;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.SyncStateContract;
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

import java.util.HashMap;
import java.util.Map;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "net.invidec.dev.MESSAGE";

    public TextView textViewError, textViewMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        textViewError = findViewById(R.id.textViewError);
        textViewMessage = findViewById(R.id.textViewMessage);

        autoLogin();

    }

    public void autoLogin () {
        SharedPreferences sharedPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);

        final String userid = sharedPref.getString(getString(R.string.saved_userid_key), "");
        final String token = sharedPref.getString(getString(R.string.saved_token_key), "");

        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://invidec.net/codiad/workspace/icrwallet/api.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response ", response.toString());
                        if (!response.toString().equals("false")) {
                            textViewMessage.setText("Automatisches Einloggen!");

                            WalletDashboard.userid = userid;
                            WalletDashboard.token = token;

                            textViewError.setText("");
                            gotoDashboard();
                        } else {
                            textViewError.setText("Automatisches Einloggen Fehlgeschlagen!");
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
                params.put("q", "checklogin");
                params.put("userid", userid);
                params.put("token", token);

                return params;
            }};
        queue.add(stringRequest);
    }

    /**
    public static void writeString(Context context, final String KEY, String property) {
        SharedPreferences.Editor editor = context.getSharedPreferences("icrw", context.MODE_PRIVATE).edit();
        editor.putString(KEY, property);
        editor.commit();
    }

    public static String readString(Context context, final String KEY) {
        return context.getSharedPreferences("icrw", context.MODE_PRIVATE).getString(KEY, null);
    }
     **/

    public void gotoDashboard() {
        Intent intent = new Intent(this, WalletDashboard.class);
        startActivity(intent);
    }

    /** Called when the user taps the Send button */
    /**public void sendMessage(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, SecondActivity.class);
        EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);


    }**/

    public void gotoLogin (View view) {
        Intent intent = new Intent(this, AccountLogin.class);
        startActivity(intent);
    }

    public void gotoRegistration (View view) {
        Intent intent = new Intent(this, AccountRegister.class);
        startActivity(intent);
    }

}
