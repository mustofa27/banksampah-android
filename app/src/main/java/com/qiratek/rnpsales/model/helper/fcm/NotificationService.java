package com.qiratek.rnpsales.model.helper.fcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.qiratek.rnpsales.R;
import com.qiratek.rnpsales.view.activity.MainActivity;

import java.util.Map;


public class NotificationService extends FirebaseMessagingService {
    static private String TAG_CHANNEL_ID="M_CH_ID";
    static private String TAG_CHANNEL_NAME="M_CH_NAME";

    @Override
    public void onNewToken(String s) {
        try {
            TempIDFCM tempIDFCM = new TempIDFCM(this);
            tempIDFCM.SaveToken(s);
        }
        catch (Exception e){

        }
    }

    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData() != null) {
            Map body = remoteMessage.getData();
            String tipe = body.get("type").toString();
            String title = remoteMessage.getNotification().getTitle();
            String content = remoteMessage.getNotification().getBody();
            sendNotification(tipe, title, content);
        }
    }


    private void sendNotification(String tipe, String tittleBody, String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("type", tipe);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, TAG_CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(tittleBody)
                        .setContentText(messageBody)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(messageBody))
                        .setContentIntent(pendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    TAG_CHANNEL_ID, TAG_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);
        }
        Notification notification = notificationBuilder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(0, notification);
    }
}
