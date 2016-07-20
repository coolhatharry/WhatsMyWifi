package coreylee.com.whatsmywifi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * TODO
 */
public class AddWifiActivity extends AppCompatActivity {

    private EditText mWifiName;
    private EditText mWifiPassword;
    private AppCompatButton mCreateButton;
    private Intent mQRIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wifi);

        setupUI();

    }

    /**
     * Obtains the views of the UI from the activity add wifi layout
     */
    private void setupUI() {
        mWifiName = (EditText) findViewById(R.id.input_wifi_name);
        mWifiPassword = (EditText) findViewById(R.id.input_wifi_password);
        mCreateButton = (AppCompatButton) findViewById(R.id.button_create_wifi);

        setCreateButtonListener();
    }

    /**
     * TODO
     */
    private void setCreateButtonListener() {

        mCreateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                createWifi();
            }
        });
    }

    /**
     * TODO
     */
    private void createWifi() {

        storeWifiData();

        //TODO
//        generateQRCode();

        //TODO
        this.finish();//Replace
    }

    /**
     * TODO
     */
    private void storeWifiData() {
        Wifi wifi = new Wifi();

        wifi.setWifiName(mWifiName.getText().toString());
        wifi.setWifiPassword(mWifiPassword.getText().toString());

        WifiDBHelper wifiDBHelper = new WifiDBHelper(this);

        wifiDBHelper.insertWifi(wifi);

    }
}
