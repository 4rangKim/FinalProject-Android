package finalproject.app.fcm;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import finalproject.app.fcm.util.RecycleViewAdapter;
import finalproject.app.fcm.util.RecyclerAdapter;
import finalproject.app.fcm.vo.ParkingVO;

public class MainActivity extends AppCompatActivity {
    /* Navigation Drawer */
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    /* RecycleView */
    RecyclerView parkingLotList;
    RecyclerAdapter recyclerAdapter;
    ArrayList<ParkingVO> resultList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /* Navigation Drawer */
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.layout_drawer);
        navigationView = findViewById(R.id.nav);
        navigationView.setItemIconTintList(null);
        drawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.app_name,R.string.app_name);
        drawerToggle.syncState();
        drawerLayout.addDrawerListener(drawerToggle);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        /* RecycleView */
        RecyclerView recyclerView = findViewById(R.id.parkingLotList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        RecycleViewAdapter adapter = new RecycleViewAdapter();
        for(int i=1;i<=10;i++){
            adapter.addItem(new ParkingVO("주차장", i,i));
        }


        recyclerView.setAdapter(adapter);
    }
}