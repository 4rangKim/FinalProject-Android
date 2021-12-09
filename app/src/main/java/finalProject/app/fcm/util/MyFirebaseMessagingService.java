package finalProject.app.fcm.util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import finalProject.app.fcm.MainActivity;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FMS";

    public MyFirebaseMessagingService() {

    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d("token", "onNewToken 호출됨 : " + token);
    }
    @Override
    public void onMessageReceived(@NonNull  RemoteMessage remoteMessage) {
        Log.d(TAG, "onMessageReceived 호출됨.");

        String from = remoteMessage.getFrom();
        String title;
        String body;
        if(remoteMessage.getNotification()!=null) {
            title = remoteMessage.getNotification().getTitle();
            body = remoteMessage.getNotification().getBody();
            Log.d(TAG, "from : " + from + ", msg : title: "+title+", body: "+body);
            sendToActivity(getApplicationContext(), from, title, body);
        }else {
            Log.d(TAG, "from : " + from + ", msg is null");
        }
    }

    private void sendToActivity(Context context, String from, String title, String body) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("from", from);
        intent.putExtra("title", title);
        intent.putExtra("body", body);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

}
