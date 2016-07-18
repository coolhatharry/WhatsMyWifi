package coreylee.com.whatsmywifi;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;


/**
 * Utility class that wraps access to the runtime permissions API for Android M and
 * provides basic helper methods for permissions.
 * <p/>
 * Created by coreylee on 16-07-18.
 */
public class PermissionUtil {

    /**
     * Id to identify a camera permission request
     */
    public static final int REQUEST_CAMERA = 0;

    /**
     * Id to identify the Write to External Storage permission request
     */
    public static final int REQUEST_STORAGE = 1;

    private Activity mContainingActivity;

    private View mLayout;

    public PermissionUtil(Activity activity, View view) {
        mContainingActivity = activity;
        mLayout = view;
    }

    /**
     * Request to write to external storage
     */
    public void requestStoragePermissions() {

        if (isStoragePermissionGranted()) {
            return;
        }

        if (ActivityCompat.shouldShowRequestPermissionRationale(mContainingActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            Snackbar.make(mLayout, R.string.permission_storage_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(mContainingActivity,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    REQUEST_STORAGE);
                        }
                    })
                    .show();
        } else {

            // External Storage permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(mContainingActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_STORAGE);
        }
    }

    /**
     * Check if the permission was granted by the user to write to external storage
     *
     * @return true or false if the storage permission was granted
     */
    public boolean isStoragePermissionGranted() {

        if (ContextCompat.checkSelfPermission(mContainingActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Request the Camera permissions
     */
    public void requestCameraPermissions() {

        if (isCameraPermissionGranted()) {
            return;
        }

        if (ActivityCompat.shouldShowRequestPermissionRationale(mContainingActivity,
                Manifest.permission.CAMERA)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            Snackbar.make(mLayout, R.string.permission_camera_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(mContainingActivity,
                                    new String[]{Manifest.permission.CAMERA},
                                    REQUEST_CAMERA);
                        }
                    })
                    .show();
        } else {

            // Camera permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(mContainingActivity, new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA);
        }

    }

    /**
     * Check if the permission was granted by the user to use the camera
     *
     * @return true or false if the storage permission was granted
     */
    public boolean isCameraPermissionGranted() {

        if (ContextCompat.checkSelfPermission(mContainingActivity, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Check that all given permissions have been granted by verifying that each entry
     */
    public static boolean verifyPermissions(int[] grantResults) {

        // At least one result must be checked.
        if (grantResults.length < 1) {
            return false;
        }

        // Verify that each required permission has been granted, otherwise return false.
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }

        return true;
    }

}
