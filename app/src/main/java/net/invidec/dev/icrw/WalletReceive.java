package net.invidec.dev.icrw;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class WalletReceive extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_receive);

       TextView buttonVal1 = findViewById(R.id.textViewAddress);
        buttonVal1.setText(WalletDashboard.address);

    }
}
