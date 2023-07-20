package bd.com.aristo.edcr.fcm;

/**
 * Created by altaf.sil on 1/17/18.
 */

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Random;

import bd.com.aristo.edcr.NotificationsActivity;
import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.utils.MyLog;
import bd.com.aristo.edcr.utils.SharedPrefsUtils;

/**
 * Created by 184002 on 2/23/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";
    private static final String CHANNEL_ID = "EDCR2020";


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        MyLog.show(TAG, s);
        SharedPrefsUtils.setStringPreference(getApplicationContext(), SharedPrefsUtils.FCM_TOKEN, s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        createNotificationChannel();
        Map<String, String> data = remoteMessage.getData();
        String from = remoteMessage.getFrom();

        MyLog.show("RemoteMessage", data.toString());

        Intent intent = new Intent("EDCR_MESSAGING_EVENT");
        intent.setAction("EDCR_MESSAGING_EVENT");

        intent.putExtra("isNeedSync",false);
        intent.putExtra("tag", data.get("Tag"));
        intent.putExtra("title", data.get("Title"));
        intent.putExtra("detail", data.get("Detail"));
        intent.putExtra("count", data.get("Count"));
        intent.putExtra("datetime", data.get("Datetime"));
        getApplicationContext().sendBroadcast(intent);

        //Disable this if need to stop
        sendNotification(data);
    }

    //{Tag=Okay, Count=0, Title=Ok, Datetime=14-03-2018 16:03:SS, Detail=TP Approved}

    private void sendNotification(Map<String,String> data) {

        final String tag = data.get("Tag");
        final String title = data.get("Title");
        final String detail = data.get("Detail");
        final String count = data.get("Count");
        final String dateTime = data.get("Datetime");


        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(this, NotificationsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(detail)
                .setSound(defaultSoundUri)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        int randomInteger = new Random().nextInt(1000);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(randomInteger, builder.build());
    }



    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


}
