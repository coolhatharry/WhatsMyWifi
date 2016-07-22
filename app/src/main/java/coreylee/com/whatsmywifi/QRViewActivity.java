package coreylee.com.whatsmywifi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

public class QRViewActivity extends AppCompatActivity {

    private String mWifiName;
    private String mWifiPassword;
    private ImageView mQRImageView;
    private EditText mEditWifiName;
    private EditText mEditWifiPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_view);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            mWifiName = extras.getString("WIFI_NAME");
            mWifiPassword = extras.getString("WIFI_PASSWORD");
        }

        Log.d("NAME", mWifiName);
        Log.d("PASSWORD", mWifiPassword);

        setupUI();

        obtainData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_qr_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will automatically handle clicks on
        // the Home/Up button, so long as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_delete) {
            WifiDBHelper wifiDBHelper = new WifiDBHelper(this);
            wifiDBHelper.deleteWifi(mWifiName);

            return true;
        }

        return super.onOptionsItemSelected(item);
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
