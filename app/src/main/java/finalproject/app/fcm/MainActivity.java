package finalproject.app.fcm;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import finalproject.app.fcm.util.RecycleViewAdapter;
import finalproject.app.fcm.vo.ParkingVO;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    /* Navigation Drawer */
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    TextView nav_HeaderId, nav_HeaderPoint;
    ActivityResultLauncher<Intent> goCharge_Launcher;
    ActivityResultLauncher<Intent> goMyCar_Launcher;
    String id,pwd, pointVal;
    Handler handler, handler2;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        /* Navigation Drawer */
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.layout_drawer);
        navigationView = findViewById(R.id.nav);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        drawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.app_name,R.string.app_name);
        drawerToggle.syncState();
        drawerLayout.addDrawerListener(drawerToggle);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Intent intent = getIntent();
        nav_HeaderId = navigationView.getHeaderView(0).findViewById(R.id.signInMemberId);
        nav_HeaderPoint = navigationView.getHeaderView(0).findViewById(R.id.pointVal);
        id = intent.getStringExtra("MemberId");
        pwd = intent.getStringExtra("MemberPwd");
        pointVal = intent.getStringExtra("MemberPoint");
        Log.d("main",pointVal+"");
        nav_HeaderId.setText(id);
        nav_HeaderPoint.setText(pointVal);
        /* RecycleView */
        RecyclerView recyclerView = findViewById(R.id.parkingLotList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        RecycleViewAdapter adapter = new RecycleViewAdapter();
        adapter.addItem(new ParkingVO("주차장", 1,1));
        recyclerView.setAdapter(adapter);
        /* Intent */
        handler = new Handler(Looper.myLooper());
        handler2 = new Handler(Looper.myLooper());
        goCharge_Launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent intent2 = result.getData();
                            String pointVal2 = intent2.getStringExtra("MemberPoint2");
                            intent.putExtra("MemberPoint",pointVal2);

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    nav_HeaderPoint.setText(pointVal2);
                                    Log.d("main","changePoint"+nav_HeaderPoint.getText().toString());
                                }
                            });
                        }
                    }
                });
        goMyCar_Launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            handler2.post(new Runnable() {
                                @Override
                                public void run() {

                                }
                            });
                        }
                    }
                });
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.logoutMenu:
                Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                intent.putExtra("signInfo", "signOut");
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }
    public void goCharge(View v){
        Intent intent = new Intent(MainActivity.this, ChargeActivity.class);
        String point = nav_HeaderPoint.getText().toString();
        intent.putExtra("MemberId",id);
        intent.putExtra("MemberPoint", point);
        goCharge_Launcher.launch(intent);
    }
    public void myCarMenu(View v){
        Intent intent = new Intent(MainActivity.this, MyCarListActivity.class);
        intent.putExtra("MemberId",id);
        intent.putExtra("MemberPwd",pwd);
        Log.d("main","myCarMenu intent:"+id+", "+pwd);
        goMyCar_Launcher.launch(intent);
    }
}