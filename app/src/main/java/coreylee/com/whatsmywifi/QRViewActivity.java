package coreylee.com.whatsmywifi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ShareActionProvider;
import android.widget.Toast;

import java.io.File;

/**
 * Displays the selected wifi's information (QR Code, SSID and Password)
 * Allows the user to delete the selected wifi.
 * <p/>
 * Created by coreylee on 16-07-22.
 */
public class QRViewActivity extends AppCompatActivity {

    private String mWifiName;
    private String mWifiPassword;
    private ImageView mQRImageView;
    private EditText mEditWifiName;
    private EditText mEditWifiPassword;
    private File mQRImageFile;
    private ShareActionProvider mShareActionProvider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_view);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            mWifiName = extras.getString("WIFI_NAME");
            mWifiPassword = extras.getString("WIFI_PASSWORD");
        }

        setupUI();

        obtainImage();
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

            // Remove image from device
            if (!removeImageFromDevice()) {
                Toast.makeText(this, "Error " + mWifiName, Toast.LENGTH_LONG).show();
            }

            Toast.makeText(this, "Deleted " + mWifiName, Toast.LENGTH_LONG).show();

            this.finish();

            return true;

        } else if (id == R.id.action_share) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(mQRImageFile));
            shareIntent.setType("image/jpeg");
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share_wifi_qr)));
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Find the image in the path and remove it from the device
     *
     * @return a boolean if the file was removed without incident
     */
    private boolean removeImageFromDevice() {
        return mQRImageFile.delete();
    }

    /**
     * Setup the UI of the QR activity view
     */
    private void setupUI() {

        mQRImageView = (ImageView) findViewById(R.id.wifi_view_qr);

        mEditWifiName = (EditText) findViewById(R.id.wifi_view_name);
        mEditWifiName.setFocusable(false);
        mEditWifiName.setKeyListener(null);
        mEditWifiName.setText(mWifiName);

        mEditWifiPassword = (EditText) findViewById(R.id.wifi_view_password);
        mEditWifiPassword.setFocusable(false);
        mEditWifiPassword.setKeyListener(null);
        mEditWifiPassword.setText(mWifiPassword);
    }

    /**
     * Obtain the image from the file direction and display it to the user
     */
    private void obtainImage() {
        String path = Environment.getExternalStorageDirectory().toString();
        mQRImageFile = new File(path, mWifiName + ".jpg"); // the File to save to

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(mQRImageFile.getAbsolutePath(), bmOptions);

        mQRImageView.setImageBitmap(bitmap);

    }

}
