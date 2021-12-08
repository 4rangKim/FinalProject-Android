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
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;
import finalproject.app.fcm.util.MainRecycleViewAdapter;
import finalproject.app.fcm.vo.CarVo;
import finalproject.app.fcm.vo.MemberVO;
import finalproject.app.fcm.vo.ParkingVO;
import finalproject.app.fcm.vo.PayListVO;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    TextView nav_HeaderId, nav_HeaderPoint;
    ActivityResultLauncher<Intent> goCharge_Launcher;
    ActivityResultLauncher<Intent> goMyCar_Launcher;
    ActivityResultLauncher<Intent> goPayList_Launcher;
    ActivityResultLauncher<Intent> goPayResult_Launcher;
    String id,pwd, name, carNum1, carNum2;
    int tel,pointVal;
    Handler handler, handler2, handler3;
    ParkingVO parkingLot;
    MainRecycleViewAdapter adapter;
    TimerTask timerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        nav_HeaderId = navigationView.getHeaderView(0).findViewById(R.id.signInMemberId);
        nav_HeaderPoint = navigationView.getHeaderView(0).findViewById(R.id.pointVal);
        /* RecycleView */
        RecyclerView recyclerView = findViewById(R.id.parkingLotList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MainRecycleViewAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new MainRecycleViewAdapter(){
            @Override
            public void onItemClick(MainRecycleViewAdapter.ViewHolder holder, View view, int position) {
                super.onItemClick(holder, view, position);
                ParkingVO item = adapter.getItem(position);
                String p_id = item.getP_id();
                Intent intent = new Intent(MainActivity.this,SubActivity.class);
                intent.putExtra("p_id", p_id);
                startActivity(intent);
                timerTask.cancel();
            }
        });
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
        nav_HeaderId.setText(name+" 님");
        nav_HeaderPoint.setText(String.valueOf(pointVal));
        handler = new Handler(Looper.myLooper());
        handler2 = new Handler(Looper.myLooper());
        handler3 = new Handler(Looper.myLooper());
        goCharge_Launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent intent2 = result.getData();
                            String pointVal2 = intent2.getStringExtra("MemberPoint");
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
        /* TimerTask */
        Timer timer = new Timer(true);
        timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    ParkingInfoHttp();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
            @Override
            public boolean cancel() {
                Log.d("main", "ParkingInfo TimerTask is cancel");
                return super.cancel();
            }
        };
        timer.schedule(timerTask, 0,5000);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.logout:
                Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                setResult(RESULT_OK, intent);
                finish();
                timerTask.cancel();
                break;
            case R.id.payList:
                Intent intent2 = new Intent(MainActivity.this, PayListActivity.class);
                intent2.putExtra("MemberId",id);
                startActivity(intent2);
                Log.d("main","goPayList");
                timerTask.cancel();
                break;
            case R.id.payResult:
                Intent intent3 = new Intent(MainActivity.this, PayResultActivity.class);
                intent3.putExtra("MemberId",id);
                startActivity(intent3);
                timerTask.cancel();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }
    public void goMyPage(View v){
        Intent intent = new Intent(MainActivity.this, UserActivity.class);
        intent.putExtra("MemberId",id);
        intent.putExtra("MemberPwd",pwd);
        intent.putExtra("MemberName",name);
        intent.putExtra("MemberTel",tel);
        intent.putExtra("MemberPoint",pointVal);
        intent.putExtra("MemberCar1",carNum1);
        intent.putExtra("MemberCar2",carNum2);
        startActivity(intent);
        timerTask.cancel();
    }
    public void goCharge(View v){
        Intent intent = new Intent(MainActivity.this, ChargeActivity.class);
        intent.putExtra("MemberId",id);
        intent.putExtra("MemberPoint", String.valueOf(pointVal));
        goCharge_Launcher.launch(intent);
        drawerLayout.closeDrawer(GravityCompat.START);
        timerTask.cancel();
    }
    public void myCarMenu(View v){
        Intent intent = new Intent(MainActivity.this, MyCarListActivity.class);
        intent.putExtra("MemberId",id);
        intent.putExtra("MemberPwd",pwd);
        intent.putExtra("MemberCar1",carNum1);
        intent.putExtra("MemberCar2",carNum2);
        Log.d("main","myCarMenu intent:"+id+", "+pwd+", "+carNum1+", "+carNum2);
        goMyCar_Launcher.launch(intent);
        drawerLayout.closeDrawer(GravityCompat.START);
        timerTask.cancel();
    }
    public void ParkingInfoHttp() throws IOException {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://192.168.0.123:80/Finalweb/parealist.mc");
        ArrayList<NameValuePair> MemberInfo = new ArrayList<>();
        MemberInfo.add(new BasicNameValuePair("id", URLDecoder.decode(id, "UTF-8")));
        post.setEntity(new UrlEncodedFormEntity(MemberInfo, "UTF-8"));
        HttpResponse response = client.execute(post);
        Log.d("main", "response StatusCode:" + response.getStatusLine().getStatusCode()); // response StatusCode: 200
        HttpEntity resEntity = response.getEntity();
        String ResultInfo = EntityUtils.toString(resEntity);
        Log.d("main", ResultInfo);
        if(!ResultInfo.equals("null")){
            try {
                JSONArray Info = new JSONArray(ResultInfo);
                for(int i=0;i<Info.length();i++) {
                    JSONObject parkingListInfo = Info.getJSONObject(i);
                    String p_id = parkingListInfo.getString("p_id");
                    String count = parkingListInfo.getString("count");
                    Log.d("main", p_id + ", " + count);
                    parkingLot = new ParkingVO(p_id, Integer.parseInt(count));
                    adapter.addItem(parkingLot);
                    Log.d("main", parkingLot.toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}