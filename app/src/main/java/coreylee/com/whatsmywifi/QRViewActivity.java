package coreylee.com.whatsmywifi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

public class QRViewActivity extends AppCompatActivity {

    private String mWifiName;
    private ImageView mQRImageView;
    private EditText mEditWifiName;
    private EditText mEditWifiPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_view);

        Bundle extras = getIntent().getExtras();

        Log.d("INQRACTIVITY", "IN");

        if (extras != null) {
            mWifiName  = extras.getString("WIFI_NAME");
        }

        Log.d("QRACTIVITY", mWifiName);

        setupUI();

        obtainData();
    }

    /**
     * TODO
     */
    private void setupUI() {

        mQRImageView = (ImageView) findViewById(R.id.wifi_view_qr);
        mEditWifiName = (EditText) findViewById(R.id.wifi_view_name);
        mEditWifiPassword = (EditText) findViewById(R.id.wifi_view_password);
    }

    /**
     * TODO
     */
    private void obtainData() {
        WifiDBHelper wifiDBHelper = new WifiDBHelper(this);

//        Wifi wifi = wifiDBHelper.getWifi();

        wifiDBHelper.close();
    }
}
