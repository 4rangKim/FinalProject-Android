package finalproject.app.fcm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MyCarListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_car_list);
    }
    public void goMain(View v){
        Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
        finish();
    }
}