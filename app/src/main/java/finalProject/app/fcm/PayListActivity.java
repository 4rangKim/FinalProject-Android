package finalProject.app.fcm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;
import finalProject.app.fcm.util.MyPayListRecycleViewAdapter;
import finalProject.app.fcm.vo.PayListVO;

public class PayListActivity extends AppCompatActivity {
    String id;
    PayListVO payList;
    Context mContext;
    MyPayListRecycleViewAdapter adapter;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_list);
        handler = new Handler(Looper.myLooper());
        mContext = this;
        /* Intent */
        Intent intent = getIntent();
        id = intent.getStringExtra("MemberId");
        /* RecycleView */
        RecyclerView recyclerView = findViewById(R.id.payList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MyPayListRecycleViewAdapter();
        recyclerView.setAdapter(adapter);
        new Thread(() -> {
            try {
                payListHttp(id);
            } catch (IOException e) {
                e.printStackTrace();
            }
            handler.post(() -> adapter.notifyDataSetChanged());
        }).start();

    }
    public void goMain(View v){ finish(); }
    public void payListHttp(String id) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost("http://192.168.0.123:80/Finalweb/paylist.mc");
        ArrayList<NameValuePair> MemberInfo = new ArrayList<>();
        MemberInfo.add(new BasicNameValuePair("id", URLDecoder.decode(id, "UTF-8")));
        post.setEntity(new UrlEncodedFormEntity(MemberInfo, "UTF-8"));
        HttpResponse response = client.execute(post);
        Log.d("payList", "response StatusCode:" + response.getStatusLine().getStatusCode()); // response StatusCode: 200
        String ResultInfo = EntityUtils.toString(response.getEntity(), "UTF-8");
        if(!ResultInfo.equals("null")){
            Log.d("payList", ResultInfo + "");
            try {
                JSONArray Info = new JSONArray(ResultInfo);
                for(int i=0;i<Info.length();i++) {
                    JSONObject payListInfo = Info.getJSONObject(i);
                    String mem_id = payListInfo.getString("mem_id");
                    String car_num = payListInfo.getString("carNum");
                    String in_time = payListInfo.getString("in_time");
                    String out_time = payListInfo.getString("out_time");
                    String payment = payListInfo.getString("payment");
                    Log.d("payList", mem_id + ", " + car_num + ", " + in_time + ", " + out_time + ", " + payment);
                    payList = new PayListVO(mem_id,car_num,in_time,out_time,Integer.parseInt(payment));
                    adapter.addItem(payList);
                    Log.d("payList","payList 에 item 추가 : "+payList.toString());
                }
                Log.d("payList",payList.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {
            Toast.makeText(this,"결제 내역을 불러오는데 오류가 발생했습니다.",Toast.LENGTH_LONG).show();
        }
    }
}