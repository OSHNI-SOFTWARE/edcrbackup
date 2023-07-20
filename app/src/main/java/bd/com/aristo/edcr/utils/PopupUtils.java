package bd.com.aristo.edcr.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Toast;

import bd.com.aristo.edcr.R;

/**
 * Created by Tariqul.Islam on 6/14/17.
 */

public class PopupUtils {

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    public static final int REQUEST_CAMERA = 101;
    public static final int SELECT_FILE = 102;
    public static Bitmap bitmap;
    public static Uri uri;



    public static CheckBox getCheckbox(Activity context, String text, int uniqueID, boolean isChecked, boolean isEnabled, int bgColor, @Nullable Object objectToTag){
        CheckBox checkBox = (CheckBox) context.getLayoutInflater().inflate(R.layout.checkbox_mcq_option, null);
        checkBox.setText(text);
        checkBox.setId(uniqueID);
        checkBox.setChecked(isChecked);
        checkBox.setEnabled(isEnabled);
        checkBox.setBackgroundColor(bgColor);
        if(objectToTag != null) checkBox.setTag(objectToTag);
        return checkBox;
    }

    public static void showToast(Context context, String s) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }

    public static void showPopupMarketChange(final Activity context) {
        try {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View layout = inflater.inflate(R.layout.pop_up_upload_prescription_option, (ViewGroup) context.findViewById(R.id.popup));

            final PopupWindow popupWindow = new PopupWindow(layout, (WindowManager.LayoutParams.WRAP_CONTENT), (WindowManager.LayoutParams.WRAP_CONTENT), true);
            popupWindow.setAnimationStyle(R.style.popupAnimation);
            popupWindow.setOutsideTouchable(false);
            /*layout.post(new Runnable() {
                @Override
                public void run() {
                    popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
                }
            });*/
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
                }
            });
            layout.setOnTouchListener(new View.OnTouchListener() {
                private int popX = 0;
                private int popY = 0;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int sides;
                    int topBot;
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            popX = (int) event.getRawX();
                            popY = (int) event.getRawY();
                            break;

                        case MotionEvent.ACTION_MOVE:
                            sides = (int) (event.getRawX() - popX);
                            topBot = (int) (event.getRawY() - popY);
                            popupWindow.update(sides, topBot, -1, -1);
                            break;
                    }
                    return true;
                }
            });

            final ImageButton optionGallery, optionCamera;

            optionGallery           = (ImageButton) layout.findViewById(R.id.option_gallery);
            optionCamera           = (ImageButton) layout.findViewById(R.id.option_camera);

            //tvInfo.setText("Really want to delete those " + chlva.getSelectedItems().size() + " selected chapters? This cannot be undone!");
            optionCamera.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v){
                    popupWindow.dismiss();
                    cameraIntent(context);


                }
            });

            optionGallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    popupWindow.dismiss();
                    galleryIntent(context);

                }
            });

        } catch (Exception e){
            e.printStackTrace();
        }

    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(final Context context)
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>=android.os.Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public static void cameraIntent(Activity activity)
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        activity.startActivityForResult(intent, REQUEST_CAMERA);
    }
    public static void galleryIntent(Activity activity)
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        activity.startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    public static void setBitmap(Bitmap bm){
        bitmap = bm;
    }

    public static void setUri(Uri uri1){
        Log.e("PopupUtils", "setUri(): "+uri1.toString());
        uri = uri1;
    }

    public static Uri getUri(){
        Log.e("PopupUtils", "setUri(): "+uri.toString());
        return uri;
    }




}
