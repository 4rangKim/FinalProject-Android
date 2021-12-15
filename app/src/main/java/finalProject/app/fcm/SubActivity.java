package finalProject.app.fcm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;
import finalProject.app.fcm.util.SubRecycleViewAdapter;
import finalProject.app.fcm.vo.P_AreaVO;

public class SubActivity extends AppCompatActivity {
    String p_id;
    TimerTask timerTask;
    Handler handler;
    P_AreaVO p_areaList;
    SubRecycleViewAdapter adapter;
    TextView parkingLotName, detailInfoName;
    Dialog detailInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);
        Log.d("why", "subActivity 실행");
        handler = new Handler(Looper.myLooper());
        parkingLotName = findViewById(R.id.areaStateParkingLotName);
        /* Intent */
        Intent intent = getIntent();
        p_id = intent.getStringExtra("p_id");
        parkingLotName.setText(p_id);
        /* RecycleView */
        RecyclerView recyclerView = findViewById(R.id.areaStateList);
        LinearLayoutManager layoutManager = new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SubRecycleViewAdapter();
        recyclerView.setAdapter(adapter);
        /* TimerTask */
        Timer timer = new Timer(true);
        timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    PAreaHttp(p_id);
                    Log.d("sub", "PAreaHttp 완료 ("+p_id+")");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                handler.post(() -> {
                    adapter.notifyDataSetChanged();
                    Log.d("sub", "timerTask UI Change.");
                });
            }
        };
        timer.schedule(timerTask, 0,10000);
        /* Dialog */
        detailInfo = new Dialog(SubActivity.this);
        detailInfo.requestWindowFeature(Window.FEATURE_NO_TITLE);
        detailInfo.setContentView(R.layout.dialog_sub_detail_info);
        detailInfoName = detailInfo.findViewById(R.id.detailInfoName);

    }
    public void detailInfo(View v){
        detailInfoName.setText(p_id);
        detailInfo.show();
        ImageButton close = detailInfo.findViewById(R.id.detailPageClose);
        close.setOnClickListener(view -> detailInfo.dismiss());
    }
    public void goMain(View v){
        timerTask.cancel();
        finish();
        Log.d("why", "sub finish");
    }
    public void PAreaHttp(String p_id) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost("http://192.168.0.123:80/Finalweb/pareaState.mc");
        ArrayList<NameValuePair> MemberInfo = new ArrayList<>();
        MemberInfo.add(new BasicNameValuePair("p_id", URLDecoder.decode(p_id, "UTF-8")));
        post.setEntity(new UrlEncodedFormEntity(MemberInfo, "UTF-8"));
        HttpResponse response = client.execute(post);
        Log.d("sub", "response StatusCode:" + response.getStatusLine().getStatusCode()); // response StatusCode: 200
        HttpEntity resEntity = response.getEntity();
        String ResultInfo = EntityUtils.toString(resEntity);
        Log.d("sub", ResultInfo);
        if(!ResultInfo.equals("null")){
            try {
                adapter.removeItem();
                JSONArray Info = new JSONArray(ResultInfo);
                for(int i=0;i<Info.length();i++) {
                    JSONObject pAreaListInfo = Info.getJSONObject(i);
                    String area_id = pAreaListInfo.getString("area_id");
                    String state = pAreaListInfo.getString("state");
                    Log.d("sub", p_id + ": area_id=" + area_id+", state="+state);
                    p_areaList = new P_AreaVO(area_id,p_id,Integer.parseInt(state));
                    adapter.addItem(p_areaList);
                    Log.d("sub", p_areaList.toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}