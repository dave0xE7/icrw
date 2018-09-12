package net.invidec.dev.icrw;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static net.invidec.dev.icrw.WalletDashboard.account;
import static net.invidec.dev.icrw.WalletDashboard.accountkey;

public class WalletSend extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_send);

        textViewBal = findViewById(R.id.textViewBal);
        textViewBal.setText(WalletDashboard.balance+" ICR");

        sendAddress = findViewById(R.id.editTextSendAddress);
        sendAmount = findViewById(R.id.editTextSendAmount);
        sendLabel = findViewById(R.id.editTextSendLabel);

        checkBoxVerify = findViewById(R.id.checkBoxVerification);
        checkBoxVerify.setVisibility(View.INVISIBLE);


    }

    public void onClickSend (View view) {
        Float inputAmount = Float.parseFloat(sendAmount.getText().toString());
        Float currentBalance = Float.parseFloat(WalletDashboard.balance);
        if (inputAmount > currentBalance) {
            inputAmount = currentBalance;
            sendAmount.setText(inputAmount.toString());
            show_alert("Your balance is too low!");
        }
        if (sendAddress.getText().toString() == "") {
            show_alert("Invalid Address");
        }
        if (checkBoxVerify.isChecked()) {
            Send();
        } else {
            checkBoxVerify.setVisibility(View.VISIBLE);
        }
    }

    protected void show_alert(String msg) {
        AlertDialog.Builder dialog=new AlertDialog.Builder(WalletSend.this);
        dialog.setMessage(msg);
        dialog.setPositiveButton("Ok",new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }

        });
        dialog.show();
    }

    public void Send () {
        RequestQueue requestQueue = Volley.newRequestQueue(WalletSend.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Configuration.getApp_auth(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("send Response ", response.toString());
                        try {
                            JSONObject jsondata = new JSONObject(response.toString());
                            JSONObject result = jsondata.getJSONObject("result");
                            Boolean okStatus  = result.getBoolean("ok");

                            //String txid = result.getString("txid");
                            if (okStatus) {

                                Toast.makeText(getApplicationContext(), "Transaction Successful!", Toast.LENGTH_SHORT).show();
                                //show_alert("Transaction Sucessful!");
                                sendAddress.setText("");
                                sendAmount.setText("");
                                sendLabel.setText("");
                            } else {
                                Toast.makeText(getApplicationContext(), "Transaction Failed!", Toast.LENGTH_SHORT).show();

                            }


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
                params.put("method", "makeTransaction");
                params.put("account", account);
                params.put("key", accountkey);
                params.put("address", sendAddress.getText().toString());
                params.put("amount", sendAmount.getText().toString());
                return params;
            }};
        requestQueue.add(stringRequest);
    }

    public void gotoDashboard () {
        Intent intent = new Intent(this, WalletDashboard.class);
        startActivity(intent);
    }


    TextView textViewBal;
    Button buttonSend;
    EditText sendAddress, sendAmount, sendLabel;
    CheckBox checkBoxVerify;
}
