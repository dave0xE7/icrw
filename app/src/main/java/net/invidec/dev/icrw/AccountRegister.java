package net.invidec.dev.icrw;

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

import java.util.HashMap;
import java.util.Map;

public class AccountRegister extends AppCompatActivity {

    public TextView textViewError, textViewMessage;
    public String name1, name2, email, pass1, pass2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_register);

        textViewError = findViewById(R.id.textViewError);
        textViewMessage = findViewById(R.id.textViewMessage);

    }

    public void handleRegistration (View view) {
        EditText editTextName1 = (EditText) findViewById(R.id.editTextName1);
        name1 = editTextName1.getText().toString();
        EditText editTextName2 = (EditText) findViewById(R.id.editTextName2);
        name2 = editTextName2.getText().toString();
        EditText editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        email = editTextEmail.getText().toString();
        EditText editTextPass1 = (EditText) findViewById(R.id.editTextPass1);
        pass1 = editTextPass1.getText().toString();
        EditText editTextPass2 = (EditText) findViewById(R.id.editTextPass2);
        pass2 = editTextPass2.getText().toString();

        requestRegistration();
    }

    public void gotoLogin () {
        Intent intent = new Intent(this, AccountLogin.class);
        startActivity(intent);
    }

    public void requestRegistration () {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://invidec.net/codiad/workspace/icrwallet/api.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response ", response.toString());
                        if (!response.toString().equals("false")) {
                            textViewMessage.setText("Registrierung erfolgreich!");
                            textViewError.setText("");
                            gotoLogin();
                        } else {
                            textViewError.setText("Registrierung Fehlgeschlagen!");
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
                params.put("q", "register");
                params.put("name1", name1);
                params.put("name2", name2);
                params.put("email", email);
                params.put("pass1", pass1);
                params.put("pass2", pass2);
                return params;
            }};
        queue.add(stringRequest);
    }
}
