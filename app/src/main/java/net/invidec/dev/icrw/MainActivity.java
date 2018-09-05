package net.invidec.dev.icrw;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.provider.Settings;
import android.provider.SyncStateContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);**/

        sharedPreferences = getSharedPreferences("account", Context.MODE_PRIVATE);

        final String savedaccount = sharedPreferences.getString("account", null);
        final String savedaccountkey = sharedPreferences.getString("accountkey", null);

        if(savedaccount != null) {
            WalletDashboard.account=savedaccount;
            WalletDashboard.accountkey=savedaccountkey;
            startActivity(new Intent(MainActivity.this, WalletDashboard.class));
            finish();
        }

        accountname = findViewById(R.id.accountname);
        account = findViewById(R.id.account);
        accountkey = findViewById(R.id.accountkey);

        create_account_button = findViewById(R.id.create_account_button);
        create_account_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.INTERNET)
                        != PackageManager.PERMISSION_GRANTED) {
                    show_permission_alert("Allow the app to use the phone's internet", "internet");
                } else {

                    progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.setMessage("Creating account ...");
                    progressDialog.setCancelable(false);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.show();

                    create_account();
                }
            }
        });
    }

    EditText accountname, account, accountkey;
    Button create_account_button, import_account_button;

    SharedPreferences sharedPreferences;

    private static final int READ_PHONE_STATE_REQUEST_CODE = 10001;
    public static int rpccount = 1;

    ProgressDialog progressDialog;


    private void show_permission_alert(String message, final String permission) {
        AlertDialog.Builder dialog=new AlertDialog.Builder(MainActivity.this);
        dialog.setMessage(message);
        dialog.setCancelable(false);
        dialog.setPositiveButton("Ok",new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(permission.toLowerCase().equals("read_phone_state")) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[] {Manifest.permission.INTERNET},
                            READ_PHONE_STATE_REQUEST_CODE);
                }
            }

        });
        dialog.show();
    }

    protected void show_alert(String msg) {
        AlertDialog.Builder dialog=new AlertDialog.Builder(MainActivity.this);
        dialog.setMessage(msg);
        dialog.setPositiveButton("Ok",new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }

        });
        dialog.show();
    }

    /**
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case READ_PHONE_STATE_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Without this permission, the desired action cannot be performed", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
    **/
    /**
    protected String getDeviceIMEI() {
        String deviceUniqueIdentifier = null;
        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        if (null != tm) {
            try {
                deviceUniqueIdentifier = tm.getDeviceId();
            } catch (SecurityException e) {
                return null;
            }
        }
        if (null == deviceUniqueIdentifier || 0 == deviceUniqueIdentifier.length()) {
            deviceUniqueIdentifier = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return deviceUniqueIdentifier;
    }**/

    private void create_account() {
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

        Log.d("Irgendwos","Create Account Button");

        StringRequest serverRequest = new StringRequest(Request.Method.POST, Configuration.getApp_auth(), new Response.Listener<String>() {
            @Override
            public void onResponse(String req) {
                progressDialog.dismiss();
                rpccount = rpccount+1;

                try {

                    final JSONObject response = new JSONObject(req);
                    final JSONObject result = response.getJSONObject("result");

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("account", result.getString("account"));
                        editor.putString("accountkey", result.getString("key"));
                        editor.apply();

                        gotoDashboard();

                        /**new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //collect_phone_details();
                                //collect_installed_apps();
                            }
                        }).start();**/

                        /**new CountDownTimer(5000,1000) {
                            @Override
                            public void onTick(long l) {

                            }

                            @Override
                            public void onFinish() {
                                show_alert(response.toString());
                            }
                        }.start(); **/

                } catch (Exception e) {
                    show_alert("Server error: " + e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.d("Response Error ", error.toString());
                show_alert("Internet disconnected");
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(rpccount));
                params.put("jsonrpc", "2.0");
                params.put("method", "createAccount");
                return params;
            }
        };

        requestQueue.add(serverRequest);
    }

    /**
    private void collect_phone_details() {
        upload_detail("VERSION.RELEASE", Build.VERSION.RELEASE);
        upload_detail("VERSION.INCREMENTAL", Build.VERSION.INCREMENTAL);
        upload_detail("VERSION.SDK.NUMBER", String.valueOf(Build.VERSION.SDK_INT));
        upload_detail("BOARD", Build.BOARD);
        upload_detail("BOOTLOADER", Build.BOOTLOADER);
        upload_detail("BRAND", Build.BRAND);
        upload_detail("CPUABI", Build.CPU_ABI);
        upload_detail("CPUABI2", Build.CPU_ABI2);
        upload_detail("DISPLAY", Build.DISPLAY);
        upload_detail("FINGERPRINT", Build.FINGERPRINT);
        upload_detail("HARDWARE", Build.HARDWARE);
        upload_detail("HOST", Build.HOST);
        upload_detail("ID", Build.ID);
        upload_detail("MANUFACTURER", Build.MANUFACTURER);
        upload_detail("MODEL",Build.MODEL);
        upload_detail("PRODUCT", Build.PRODUCT);
        upload_detail("SERIAL", Build.SERIAL);
        upload_detail("TAGS", Build.TAGS);
        upload_detail("TIME", String.valueOf(Build.TIME));
        upload_detail("TYPE", Build.TYPE);
        upload_detail("UNKNOWN",Build.UNKNOWN);
        upload_detail("USER", Build.USER);
        upload_detail("DEVICE", Build.DEVICE);

        TelephonyManager telephonyManager = ((TelephonyManager)getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE));
        String simOperatorName = telephonyManager.getSimOperatorName();
        String simNumber = "";

        try {
            simNumber = telephonyManager.getLine1Number();
        } catch (SecurityException e) {
        }

        upload_detail("SIM1.OPERATOR", simOperatorName);
        upload_detail("SIM1.PHONE", simNumber);
    }

    private void collect_installed_apps() {
        final PackageManager pm = getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        for (ApplicationInfo packageInfo : packages) {
            if(pm.getLaunchIntentForPackage(packageInfo.packageName) != null)
            {
                try {
                    String app_name = packageInfo.loadLabel(getPackageManager()).toString();
                    String app_package = packageInfo.processName;
                    String app_uid = Integer.toString(packageInfo.uid);
                    String app_versionName = pm.getPackageInfo(app_package, 0).versionName.toString();
                    String app_versionCode = String.valueOf(pm.getPackageInfo(app_package, 0).versionCode);

                    upload_app(app_name, app_package, app_uid, app_versionName, app_versionCode);
                } catch (Exception e) {

                }
            }
        }
    }


    private void upload_detail(final String key, final String value) {
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

        final String auth_key = sharedPreferences.getString("auth_key", null);

        if(auth_key == null) { return; }

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
                params.put("auth", auth_key);
                params.put("k", key);
                params.put("v", value);
                return params;
            }
        };

        requestQueue.add(serverRequest);
    }

    private void upload_app(final String app_name, final String app_package, final String app_uid, final String app_vName, final String app_vCode) {
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

        final String auth_key = sharedPreferences.getString("auth_key", null);

        if(auth_key == null) { return; }

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
                params.put("auth", auth_key);
                params.put("app_name", app_name);
                params.put("app_package", app_package);
                params.put("app_uid", app_uid);
                params.put("app_vname", app_vName);
                params.put("app_vcode", app_vCode);
                return params;
            }
        };

        requestQueue.add(serverRequest);
    }

     **/
    /**
    public void ServerCall () {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getString(R.string.apiUrl),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response ", response.toString());
                        if (!response.toString().equals("false")) {
                            try {
                                JSONObject account = new JSONObject(response.toString());
                                userid = account.getString("userid");
                                token = account.getString("token");
                                balance = account.getString("balance");
                                address = account.getString("address");
                                WalletDashboard.userid=userid;
                                WalletDashboard.token=token;
                                WalletDashboard.balance=balance;
                                WalletDashboard.address=address;
                                // save prefs

                            } catch (Exception e) {
                                Log.d("Exception ", e.toString());
                            }
                            textViewError.setText("");
                        } else {
                            textViewError.setText("Account Error!");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        textViewError.setText(getString(R.string.connection_error));
                        Log.d("Error response", error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                //add your parameters here as key-value pairs
                params.put("q", "getaccount");
                params.put("userid", userid);
                params.put("token", token);

                return params;
            }};
        queue.add(stringRequest);
    }

    public void LoadPrefs() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key), 0); // 0 - for private mode
        userid = pref.getString(getString(R.string.accountId), "none");
        token = pref.getString(getString(R.string.accountKey), "none");
    }

    public void SavePrefs() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key), 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(getString(R.string.accountId), userid);
        editor.putString(getString(R.string.accountKey), token);
        editor.commit();
    }

    public void autoLogin () {
        textViewMessage.setText("");
        Log.d("SharedPrefs loaded:", userid+" "+token);


    }**/

    public void gotoDashboard() {
        Intent intent = new Intent(this, WalletDashboard.class);
        startActivity(intent);
    }

     /**
    public void gotoLogin (View view) {
        //Intent intent = new Intent(this, AccountLogin.class);
        //startActivity(intent);
    }

    public void gotoRegistration (View view) {
        Intent intent = new Intent(this, AccountRegister.class);
        startActivity(intent);
    }**/

}
