package coreylee.com.whatsmywifi;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The main screen for the app
 */
public class HomeScreenActivity extends AppCompatActivity {
    private Intent mAddWifiIntent;
    private WifiDBHelper mWifiDBHelper;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private WifiAdapter mWifiAdapter;
    private PermissionUtil mPermissionUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAddWifiIntent = new Intent(new Intent(this, AddWifiActivity.class));

        mPermissionUtil = new PermissionUtil(this, this.findViewById(R.id.home_screen_view));

        setupAddWifiButton();

        setupRecyclerView();

        prepareWifiData();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will automatically handle clicks on
        // the Home/Up button, so long as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            this.prepareWifiData();

            return true;
        } else if (id == R.id.action_scan_qr) {

            mPermissionUtil.requestCameraPermissions();

            if (mPermissionUtil.isCameraPermissionGranted()) {
                scanQRCode();
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == PermissionUtil.REQUEST_CAMERA) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                scanQRCode();

            } else {
                Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_LONG).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result == null) {
            super.onActivityResult(requestCode, resultCode, data);

            return;
        }

        if (result.getContents() == null) {
            Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();

            Log.d("SCANNED", result.toString());

            connectToNetwork(result.getContents());
        }

    }

    private void connectToNetwork(String contents) {

        Wifi wifiNetwork = extractNetworkInformation(contents);

        // Verify the QR code is correct
        if (wifiNetwork == null) {
            Log.e("QRSCANERROR", "QR Code not recognised");
            return;
        }

        String ssid = wifiNetwork.getWifiName();
        String password = wifiNetwork.getWifiPassword();

        WifiConfiguration wifiConfig = new WifiConfiguration();
        wifiConfig.SSID = String.format("\"%s\"", ssid);
        wifiConfig.preSharedKey = String.format("\"%s\"", password);

        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);

        // Verify that wifi is enabled
        if (wifiManager.getWifiState() == WifiManager.WIFI_STATE_DISABLED) {
            wifiManager.setWifiEnabled(true);
        }

        // Save the network information
        int networkId = wifiManager.addNetwork(wifiConfig);
        wifiManager.disconnect();
        wifiManager.enableNetwork(networkId, true);
        wifiManager.reconnect();
    }

    private Wifi extractNetworkInformation(String contents) {
        Wifi wifi = new Wifi();
        Pattern pattern = Pattern.compile("^WIFI:S:(.*);P:(.*);;$");
        Matcher matcher = pattern.matcher(contents);

        if (matcher.find()) {
            MatchResult matchResult = matcher.toMatchResult();

            wifi.setWifiName(matchResult.group(1));
            wifi.setWifiPassword(matchResult.group(2));

            Log.d("NAME", wifi.getWifiName());
            Log.d("PASS", wifi.getWifiPassword());

        } else {
            wifi = null;
        }

        return wifi;
    }

    /**
     * This will handle the scanning of the QR code using the devices camera
     */
    private void scanQRCode() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setOrientationLocked(false);
        integrator.initiateScan();
    }

    /**
     * Adds a floating add button which will trigger the add wifi activity if selected
     */
    private void setupAddWifiButton() {
        FloatingActionButton addWifiButton = (FloatingActionButton) findViewById(R.id.add_wifi);
        addWifiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(mAddWifiIntent);
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
    }

    /**
     * Displays a list of added wifi settings to the user
     */
    private void setupRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.home_recycler_view);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());

        mWifiDBHelper = new WifiDBHelper(this);
        mWifiAdapter = new WifiAdapter(this, mWifiDBHelper.getAllWifis());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        mRecyclerView.setAdapter(mWifiAdapter);
    }

    /**
     * Obtains the most recent wifi data from the database and adds it to the adapter
     */
    private void prepareWifiData() {

        mWifiAdapter.swapCursor(mWifiDBHelper.getAllWifis());

    }
}
