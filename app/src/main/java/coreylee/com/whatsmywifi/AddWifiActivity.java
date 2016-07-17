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

public class AddWifiActivity extends AppCompatActivity {

    private EditText mWifiName;
    private EditText mWifiPassword;
    private Spinner mWifiType;
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
        mWifiType = (Spinner) findViewById(R.id.spinner_wifi_type);
        mCreateButton = (AppCompatButton) findViewById(R.id.button_create_wifi);

        setupSpinnerAdapter();
        setCreateButtonListener();
    }

    /**
     * Adds an array adapter to the spinner widget
     */
    private void setupSpinnerAdapter() {
        ArrayAdapter<CharSequence> adapterWifiType = ArrayAdapter.createFromResource(this,
                R.array.wifi_type, android.R.layout.simple_spinner_item);

        adapterWifiType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Add the wifi type adapter to the spinner
        mWifiType.setAdapter(adapterWifiType);
    }

    /**
     *
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
     *
     */
    private void createWifi() {

        storeWifiData();

        //TODO
//        generateQRCode();

        //TODO
        this.finish();//Replace
    }

    /**
     *
     */
    private void storeWifiData() {
        Wifi wifi = new Wifi();

        wifi.setWifiName(mWifiName.getText().toString());
        wifi.setWifiPassword(mWifiPassword.getText().toString());
        wifi.setWifiType(WifiType.valueOf(mWifiType.getSelectedItem().toString()));

        WifiDBHelper wifiDBHelper = new WifiDBHelper(this);

        Log.d("DB ROWS", "" + wifiDBHelper.getNumberOfRows());

        wifiDBHelper.insertWifi(wifi);

    }
}
