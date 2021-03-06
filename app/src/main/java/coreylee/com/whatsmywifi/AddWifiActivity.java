package coreylee.com.whatsmywifi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Allows the user to add their wifi settings (SSID and Password) and save it to the SQL Database.
 * This will also call the next activity to generate a QR Code with all the specs.
 * Created by coreylee on 16-07-13.
 */
public class AddWifiActivity extends AppCompatActivity {

    private PermissionUtil mPermissionUtil;
    private EditText mWifiName;
    private EditText mWifiPassword;
    private AppCompatButton mCreateButton;
    private Wifi mWifi = new Wifi();
    private Intent mQRViewIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wifi);

        mPermissionUtil = new PermissionUtil(this, this.findViewById(R.id.add_wifi_view));

        setupUI();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == PermissionUtil.REQUEST_CAMERA) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                storeAndGenerateWifi();

            } else {
                Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_LONG).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    /**
     * Obtains the views of the UI from the activity add wifi layout
     */
    private void setupUI() {

        mWifiName = (EditText) findViewById(R.id.input_wifi_name);
        mWifiPassword = (EditText) findViewById(R.id.input_wifi_password);
        mCreateButton = (AppCompatButton) findViewById(R.id.button_save_and_generate);

        setCreateButtonListener();
    }

    /**
     * Call the createWifi method when the create button is called
     */
    private void setCreateButtonListener() {

        mCreateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                storeAndGenerateWifi();
            }
        });
    }

    /**
     * Store the wifi data into the database and generates the qr code based on the wifi specs
     */
    private void storeAndGenerateWifi() {

        // Ask for permission to write to external memory
        mPermissionUtil.requestStoragePermissions();

        // Display a loading spinner to the user
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Generating QR Code...");
        progressDialog.show();

        Thread mThread = new Thread() {
            @Override
            public void run() {
                storeWifiData();

                generateQRCode();

                progressDialog.dismiss();

                startNextActivity();

            }
        };

        mThread.start();


    }

    /**
     * Obtain the intent  and start the QR view activity
     */
    private void startNextActivity() {
        mQRViewIntent = new Intent(new Intent(this, QRViewActivity.class));
        mQRViewIntent.putExtra("WIFI_NAME", mWifi.getWifiName());
        mQRViewIntent.putExtra("WIFI_PASSWORD", mWifi.getWifiPassword());

        this.startActivity(mQRViewIntent);

    }

    /**
     * Stores the Wifi specs, SSID and Password, to the database
     */
    private void storeWifiData() {

        mWifi.setWifiName(mWifiName.getText().toString());
        mWifi.setWifiPassword(mWifiPassword.getText().toString());

        WifiDBHelper wifiDBHelper = new WifiDBHelper(this);

        wifiDBHelper.insertWifi(mWifi);
        wifiDBHelper.close();

    }

    /**
     * Generates the qr code based on the specs of the wifi data
     */
    private void generateQRCode() {
        String contents = "[[[WIFI_NAME]]]=" + mWifi.getWifiName() +
                "[[[WIFI_PASSWORD]]]=" + mWifi.getWifiPassword();

        Log.d("CONTENT", contents);
        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(contents, BarcodeFormat.QR_CODE, 660, 660, null);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();

            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }

            saveBitmapToDevice(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }

    }

    /**
     * Converts the bitmap to an image file and stores it in the device
     *
     * @param bitmap The QR bitmap to compress into an image
     */
    private void saveBitmapToDevice(Bitmap bitmap) {
        String path = Environment.getExternalStorageDirectory().toString();
        File qrFile = new File(path, mWifi.getWifiName() + ".jpg"); // the File to save to

        Log.d("PATH", qrFile.toString());
        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(qrFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();

                    // Can now be viewed in the photo gallery
                    MediaStore.Images.Media.insertImage(getContentResolver(),
                            qrFile.getAbsolutePath(),
                            mWifi.getWifiName(),
                            mWifi.getWifiName() + " " + mWifi.getWifiPassword());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


}
