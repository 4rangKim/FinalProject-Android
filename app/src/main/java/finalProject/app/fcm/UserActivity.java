package finalProject.app.fcm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import finalProject.app.fcm.util.UserCarRecycleViewAdapter;
import finalProject.app.fcm.vo.CarVo;

public class UserActivity extends AppCompatActivity {
    String id, name, tel, pointVal, carNum1, carNum2;
    UserCarRecycleViewAdapter adapter;
    TextView userName, userId, userPoint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        userName = findViewById(R.id.userName);
        userId = findViewById(R.id.userId);
        userPoint = findViewById(R.id.userPoint);
        /* Intent */
        Intent intent = getIntent();
        id = intent.getStringExtra("MemberId");
        name = intent.getStringExtra("MemberName");
        tel = intent.getStringExtra("MemberTel");
        pointVal = intent.getStringExtra("MemberPoint");
        carNum1 = intent.getStringExtra("MemberCar1");
        if(!intent.getStringExtra("MemberCar2").equals("null")){
            carNum2 = intent.getStringExtra("MemberCar2");
            Log.d("user","carNum is not null");
        }else {
            carNum2 = "";
            Log.d("user","carNum is null");
        }
        userName.setText(name);
        userId.setText(id);
        userPoint.setText(pointVal);
        /* RecyclerView */
        RecyclerView recyclerView = findViewById(R.id.userCarList);
        LinearLayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new UserCarRecycleViewAdapter();
        recyclerView.setAdapter(adapter);
        if(carNum2.equals("")){
            adapter.addItem(new CarVo(carNum1));
        }else {
            adapter.addItem(new CarVo(carNum1));
            adapter.addItem(new CarVo(carNum2));
        }
    }
    public void goMain(View v){
        finish();
    }
}