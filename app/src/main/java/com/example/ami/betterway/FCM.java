package com.example.ami.betterway;

import android.app.Notification;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import static android.support.constraint.Constraints.TAG;


public class FCM extends FirebaseMessagingService {
    DatabaseReference myRef;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        super.onMessageReceived(remoteMessage);
        String title = null;
        String body = null;

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.i(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {

            title = remoteMessage.getData().get("title");
            body = remoteMessage.getData().get("body");

        }

        // https://developer.android.com/training/notify-user/expanded#java
        if(title.equals("Group separation!")){
            Notification notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), "some_channel_id")
                    .setContentTitle(title)
                    .setContentText(body)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setSmallIcon(R.drawable.logo)
                    .setBadgeIconType(R.mipmap.ic_lost)
                    //.setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.prova_icon))
                    //.setStyle(new NotificationCompat.BigPictureStyle()
                    //.bigPicture(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.prova_icon)).bigLargeIcon(null))
                    .build();
            NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
            manager.notify(123, notificationBuilder);
        } else {
            Notification notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), "some_channel_id")
                    .setContentTitle(title)
                    .setContentText(body)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setSmallIcon(R.drawable.logo)
                    .setBadgeIconType(R.mipmap.prova_icon)
                    .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.prova_icon))
                    //.setStyle(new NotificationCompat.BigPictureStyle()
                    //.bigPicture(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.prova_icon)).bigLargeIcon(null))
                    .build();
            NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
            manager.notify(123, notificationBuilder);
        }


    }

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        Log.i(TAG, "Refreshed token: " + token);

        myRef = FirebaseDatabase.getInstance().getReference();
        myRef.child("token").child(token).setValue(token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.

    }

}