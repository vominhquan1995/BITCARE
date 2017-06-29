package asia.health.bitcare.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import asia.health.bitcare.R;

public abstract class BasePermissionActivity extends BaseToolbarActivity {

    private final int PERMISSIONS_REQUEST_CODE = 7376;
    private final int PERMISSIONS_REQUEST_BY_SETTING_CODE = 7388;
    private OnPermissionsLisnener onPermissionsLisnener;
    private String[] permissions;
    private String explanationMessage;
    private AlertDialog mAlertDialog;

    public void requestRuntimePermissions(String[] permissions, String explanationMessage, OnPermissionsLisnener onPermissionsLisnener) {

        this.permissions = permissions;
        this.explanationMessage = explanationMessage;
        this.onPermissionsLisnener = onPermissionsLisnener;

        boolean allPermissionsGranted = true;
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context,
                    permission) != PackageManager.PERMISSION_GRANTED) {

                allPermissionsGranted = false;
                break;
            }
        }

        if (!allPermissionsGranted) {

//            // Should we show an explanation?
//            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
//                    android.Manifest.permission.ACCESS_FINE_LOCATION)
//                    || ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
//                    android.Manifest.permission.ACCESS_COARSE_LOCATION)) {
//
//                // Show an explanation to the user *asynchronously* -- don't block
//                // this thread waiting for the user's response! After the user
//                // sees the explanation, try again to request the permission.
//
//            } else {
//
//                // No explanation needed, we can request the permission.
//
//                ActivityCompat.requestPermissions(MainActivity.this,
//                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
//                                android.Manifest.permission.ACCESS_COARSE_LOCATION},
//                        PERMISSIONS_REQUEST_ACCESS_LOCATION);
//
//                // MY_PERMISSIONS_REQUEST_LOCATION is an
//                // app-defined int constant. The callback method gets the
//                // result of the request.
//            }
            ActivityCompat.requestPermissions((Activity) context, permissions,
                    PERMISSIONS_REQUEST_CODE);
        } else {
            if (this.onPermissionsLisnener != null)
                this.onPermissionsLisnener.onPermissionGranted();
        }
    }

    @SuppressWarnings("ResourceType")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {

        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            // for each permission check if the user grantet/denied them
            // you may want to group the rationale in a single dialog,
            // this is just an example
            boolean allPermissionsGranted = true;
            boolean showRationale = false;
            for (int i = 0, len = permissions.length; i < len; i++) {
                String permission = permissions[i];
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        showRationale = shouldShowRequestPermissionRationale(permission);
                    }
                    allPermissionsGranted = false;
                }
            }
            if (!allPermissionsGranted) {
                if (!showRationale) {
                    // user denied flagging NEVER ASK AGAIN
                    // you can either enable some fall back,
                    // disable features of your app
                    // or open another dialog explaining
                    // again the permission and directing to
                    // the app setting
                    mAlertDialog = new AlertDialog.Builder(context)
                            .setTitle(getString(R.string.permission_denied))
                            .setMessage(explanationMessage)
                            .setCancelable(false)
                            .setNegativeButton(R.string.upcase_im_sure, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    onPermissionsLisnener.onPermissionDenied();
                                }
                            })
                            .setPositiveButton(R.string.upcase_goto_settings, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    // go to settings permission
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                                    intent.setData(uri);

                                    startActivityForResult(intent, PERMISSIONS_REQUEST_BY_SETTING_CODE);
                                }
                            })
                            .show();

                } else {
                    // user denied WITHOUT never ask again
                    // this is a good place to explain the user
                    // why you need the permission and ask if he want
                    // to accept it (the rationale)
                    mAlertDialog = new AlertDialog.Builder(context)
                            .setTitle(getString(R.string.permission_denied))
                            .setMessage(explanationMessage)
                            .setNegativeButton(R.string.upcase_im_sure, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    onPermissionsLisnener.onPermissionDenied();
                                }
                            })
                            .setPositiveButton(R.string.upcase_retry, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    ActivityCompat.requestPermissions((Activity) context, BasePermissionActivity.this.permissions,
                                            PERMISSIONS_REQUEST_CODE);
                                }
                            })
                            .setCancelable(false)
                            .show();
                }
            } else {

                if (onPermissionsLisnener != null)
                    onPermissionsLisnener.onPermissionGranted();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PERMISSIONS_REQUEST_BY_SETTING_CODE) {

            boolean allPermissionsGranted = true;
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(context,
                        permission) != PackageManager.PERMISSION_GRANTED) {

                    allPermissionsGranted = false;
                    break;
                }
            }

            if (!allPermissionsGranted) {

                ActivityCompat.requestPermissions((Activity) context, permissions,
                        PERMISSIONS_REQUEST_CODE);
            } else {
                mAlertDialog.dismiss();
                if (onPermissionsLisnener != null)
                    onPermissionsLisnener.onPermissionGranted();
            }
        }
    }

    public interface OnPermissionsLisnener {

        void onPermissionGranted();

        void onPermissionDenied();
    }
}
