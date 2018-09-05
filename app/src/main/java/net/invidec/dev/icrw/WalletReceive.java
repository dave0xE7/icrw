package net.invidec.dev.icrw;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

public class WalletReceive extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_receive);

        textViewAddress = findViewById(R.id.textViewAddress);
        textViewAddress.setText(WalletDashboard.address);

        qrcodeimage = (WebView) findViewById(R.id.qrcodeimage);
        qrcodeimage.loadUrl("https://chart.googleapis.com/chart?chs=300x300&cht=qr&chl="+WalletDashboard.address+"&choe=UTF-8");
    }

    public void CopyAddress (View view) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("ICRAddress", WalletDashboard.address);
        clipboard.setPrimaryClip(clip);
        //show_alert("Copied to Clipboard!");
    }

    WebView qrcodeimage;
    TextView textViewAddress;
}
