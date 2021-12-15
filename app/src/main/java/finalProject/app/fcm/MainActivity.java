package finalProject.app.fcm;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import finalProject.app.fcm.util.FragmentParkingLotList;
import finalProject.app.fcm.util.FragmentParkingLotMap;
import finalProject.app.fcm.util.FragmentViewpagerAdapter;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    TextView nav_HeaderId, nav_HeaderPoint;
    ActivityResultLauncher<Intent> goCharge_Launcher;
    String id,pwd, name, carNum1, carNum2;
    int tel,pointVal;
    Handler handler;
    /* FCM */
    NotificationManagerCompat notificationManager;
    String channelId = "channel";
    String channelName = "Channel_name";
    int importance = NotificationManager.IMPORTANCE_LOW;
    /* ViewPager */
    ViewPager2 viewPager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("why", "main 실행");
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
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        nav_HeaderId = navigationView.getHeaderView(0).findViewById(R.id.signInMemberId);
        nav_HeaderPoint = navigationView.getHeaderView(0).findViewById(R.id.pointVal);
        /* ViewPager */
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(FragmentParkingLotList.newInstance(0));
        fragments.add(FragmentParkingLotMap.newInstance(1));
        viewPager2 = findViewById(R.id.mainViewpager);
        FragmentViewpagerAdapter mainViewpager = new FragmentViewpagerAdapter(this, fragments);
        viewPager2.setAdapter(mainViewpager);
        viewPager2.setUserInputEnabled(false);
        /* Intent */
        Intent intent = getIntent();
        id = intent.getStringExtra("MemberId");
        pwd = intent.getStringExtra("MemberPwd");
        name = intent.getStringExtra("MemberName");
        tel = Integer.parseInt(intent.getStringExtra("MemberTel"));
        carNum1 = intent.getStringExtra("MemberCar1");
        carNum2 = intent.getStringExtra("MemberCar2");
        pointVal = Integer.parseInt(intent.getStringExtra("MemberPoint"));
        Log.d("main","Intent : id="+id+", pwd="+pwd+", name="+name+", tel="+tel+", carNum1="+carNum1+", carNum2="+carNum2);
        nav_HeaderId.setText(name);
        nav_HeaderPoint.setText(String.valueOf(pointVal));
        handler = new Handler(Looper.myLooper());
        goCharge_Launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent intent2 = result.getData();
                    if (intent2!=null){
                        pointVal = Integer.parseInt(intent2.getStringExtra("MemberPoint"));
                        intent.putExtra("MemberPoint",String.valueOf(pointVal));
                        handler.post(() -> {
                            nav_HeaderPoint.setText(String.valueOf(pointVal));
                            Log.d("main","changePoint"+nav_HeaderPoint.getText().toString());
                        });
                    }

                }
            }
        );

    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.logout){
            Intent intent = new Intent(MainActivity.this, SignInActivity.class);
            setResult(RESULT_OK, intent);
            finish();
        }else if(item.getItemId()==R.id.payList){
            Intent intent2 = new Intent(MainActivity.this, PayListActivity.class);
            intent2.putExtra("MemberId",id);
            startActivity(intent2);
            Log.d("main","goPayList");
        }else if(item.getItemId()==R.id.payResult){
            Intent intent3 = new Intent(MainActivity.this, PayResultActivity.class);
            intent3.putExtra("MemberId",id);
            startActivity(intent3);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }
    public void mainChange(View v){
        TextView text = findViewById(R.id.mainChangeText);
        if(viewPager2.getCurrentItem()==0){
            viewPager2.setCurrentItem(1);
            handler.post(() -> text.setText("목록 보기"));
        }else if(viewPager2.getCurrentItem()==1){
            viewPager2.setCurrentItem(0);
            handler.post(() -> text.setText("지도 보기"));
        }
    }
    public void goMyPage(View v){
        Intent intent = new Intent(MainActivity.this, UserActivity.class);
        intent.putExtra("MemberId",id);
        intent.putExtra("MemberPwd",pwd);
        intent.putExtra("MemberName",name);
        intent.putExtra("MemberTel",String.valueOf(tel));
        intent.putExtra("MemberPoint",String.valueOf(pointVal));
        intent.putExtra("MemberCar1",carNum1);
        intent.putExtra("MemberCar2",carNum2);
        Log.d("main","goMyPage : id="+id+", pwd="+pwd+", name="+name+", tel="+tel+", carNum1="+carNum1+", carNum2="+carNum2);
        startActivity(intent);
    }
    public void goCharge(View v){
        Intent intent = new Intent(MainActivity.this, ChargeActivity.class);
        intent.putExtra("MemberId",id);
        intent.putExtra("MemberPoint", String.valueOf(pointVal));
        goCharge_Launcher.launch(intent);
        drawerLayout.closeDrawer(GravityCompat.START);
    }
    public void myCarMenu(View v){
        Intent intent = new Intent(MainActivity.this, MyCarListActivity.class);
        intent.putExtra("MemberId",id);
        intent.putExtra("MemberPwd",pwd);
        intent.putExtra("MemberCar1",carNum1);
        intent.putExtra("MemberCar2",carNum2);
        Log.d("main","myCarMenu intent:"+id+", "+pwd+", "+carNum1+", "+carNum2);
        startActivity(intent);
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d("main", "onNewIntent 호출됨");
        if (intent != null) {
            processIntent(intent);
        }

        super.onNewIntent(intent);
    }
    private void processIntent(Intent intent) {
        String from = intent.getStringExtra("from");
        if (from == null) {
            Log.d("main", "from is null.");
            return;
        }

        String title = intent.getStringExtra("title");
        String body = intent.getStringExtra("body");
        Log.d("main", "Intent= title: "+title+", body: "+body);
        String channelId = "channel";

        notificationManager = NotificationManagerCompat.from(MainActivity.this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }
        Intent intent2 = new Intent(MainActivity.this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 101, intent2, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(MainActivity.this, "channel")
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setVibrate(new long[]{1000, 1000});
        notificationManager.notify(0, mBuilder.build());
    }
}