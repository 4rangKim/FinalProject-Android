package finalProject.app.fcm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

public class LogoActivity extends AppCompatActivity {
    Handler handler;
    Runnable delayThread = () -> {
        Intent intent = new Intent(LogoActivity.this, SignInActivity.class);
        startActivity(intent);
        finish();
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);
        handler = new Handler(Looper.myLooper());
        handler.postDelayed(delayThread,2000);
    }
}