package finalProject.app.fcm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import finalProject.app.fcm.vo.PayListVO;

public class PayResultActivity extends AppCompatActivity {
    EditText inTime, outTime, cost;
    TimerTask timerTask;
    String id, inTimeVal, outTimeVal, costVal;
    PayListVO paymentResult;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_result);
        handler = new Handler(Looper.myLooper());
        inTime = findViewById(R.id.inTime);
        outTime = findViewById(R.id.outTime);
        cost = findViewById(R.id.cost);
        Intent intent = getIntent();
        id = intent.getStringExtra("MemberId");
        /* TimerTask */
        Timer timer = new Timer(true);
        timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    PaymentHttp(id);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                handler.post(() -> {
                    inTime.setText(inTimeVal);
                    cost.setText(costVal);
                    Date time = new Date();
                    SimpleDateFormat format = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault());
                    outTimeVal = format.format(time);
                    outTime.setText(outTimeVal);
                });
            }
            @Override
            public boolean cancel() {
                Log.d("payNow", "ParkingInfo TimerTask is cancel");
                return super.cancel();
            }
        };
        timer.schedule(timerTask, 0,5000);
    }
    public void goMain(View v){
        finish();
        timerTask.cancel();
    }
    public void PaymentHttp(String id) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost("http://192.168.0.123:80/Finalweb/nowPayment.mc");
        ArrayList<NameValuePair> MemberInfo = new ArrayList<>();
        MemberInfo.add(new BasicNameValuePair("id", URLDecoder.decode(id, "UTF-8")));
        post.setEntity(new UrlEncodedFormEntity(MemberInfo, "UTF-8"));
        HttpResponse response = client.execute(post);
        Log.d("payNow", "response StatusCode:" + response.getStatusLine().getStatusCode()); // response StatusCode: 200
        HttpEntity resEntity = response.getEntity();
        String ResultInfo = EntityUtils.toString(resEntity);
        Log.d("payNow", ResultInfo);
        if(!ResultInfo.equals("null")){
            Log.d("payNow", ResultInfo + "");
            try {
                JSONArray Info = new JSONArray(ResultInfo);
                JSONObject payListInfo = Info.getJSONObject(0);
                String mem_id = payListInfo.getString("mem_id");
                String car_num = payListInfo.getString("carNum");
                String in_time = payListInfo.getString("in_time");
                String out_time = payListInfo.getString("out_time");
                String payment = payListInfo.getString("payment");
                Log.d("payNow", mem_id + ", " + car_num + ", " + in_time + ", " + out_time + ", " + payment);
                paymentResult = new PayListVO(mem_id,car_num,in_time,out_time,Integer.parseInt(payment));
                costVal = String.valueOf(paymentResult.getPayment());
                inTimeVal = paymentResult.getIn_time();
                Log.d("payNow","paymentResult: "+paymentResult.toString());
                Log.d("payNow",paymentResult.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}