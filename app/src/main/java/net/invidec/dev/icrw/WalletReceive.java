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
import android.content.Intent;
import android.widget.Toast;

public class WalletReceive extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_receive);

        tvAccountAddress = findViewById(R.id.tvAccountAddress);
        tvAccountAddress .setText(WalletDashboard.address);

        qrcodeimage = (WebView) findViewById(R.id.qrcodeimage);
        qrcodeimage.loadUrl("https://chart.googleapis.com/chart?chs=300x300&cht=qr&chl="+WalletDashboard.address+"&choe=UTF-8");
    }

    public void CopyAddress (View view) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("ICRAddress", WalletDashboard.address);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "Copied to Clipboard!", Toast.LENGTH_SHORT).show();
        //show_alert("Copied to Clipboard!");
    }

    public void ShareText (String text) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");
        //startActivity(sendIntent);
        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.sendTo)));
    }

    public void buttonShareToApp (View view) {
        ShareText(WalletDashboard.address);
    }

    WebView qrcodeimage;
    TextView tvAccountAddress;
}
