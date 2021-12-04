package finalproject.app.fcm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;

public class ChargeActivity extends AppCompatActivity {
    String id, pointVal;
    EditText myPoint, chargePoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge);
        Intent intent = getIntent();
        id = intent.getStringExtra("MemberId");
        pointVal = intent.getStringExtra("MemberPoint");
        myPoint = findViewById(R.id.myPoint);
        chargePoint = findViewById(R.id.newPoint);
        myPoint.setText(pointVal);
        Log.d("charge","ID:"+id);
        Log.d("charge","pointVal:"+pointVal);
    }
    public void goMain(View v){
        String point = myPoint.getText().toString();
        Intent intent3 = new Intent(getApplicationContext(), MainActivity.class);
        intent3.putExtra("MemberPoint2", point);
        finish();
    }
    public void charge(View v){
        String point = chargePoint.getText().toString();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String pointVal2 = chargeHttp(id,point);
                    Intent intent2 = new Intent(getApplicationContext(), MainActivity.class);
                    intent2.putExtra("MemberPoint2", pointVal2);
                    Log.d("charge","MemberPoint2:"+pointVal2);
                    setResult(RESULT_OK, intent2);
                    finish();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public String chargeHttp(String id, String point) throws IOException {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://192.168.0.123:80/Finalweb/pointcharge.mc");
        ArrayList<NameValuePair> chargeInfo = new ArrayList<NameValuePair>();

        try {
            chargeInfo.add(new BasicNameValuePair("id", URLDecoder.decode(id, "UTF-8")));
            chargeInfo.add(new BasicNameValuePair("point", URLDecoder.decode(point, "UTF-8")));
            post.setEntity(new UrlEncodedFormEntity(chargeInfo, "UTF-8"));
            Log.d("charge",chargeInfo.toString()+"");
        } catch (UnsupportedEncodingException ex) {
            Log.e("charge", ex.toString());
        }
        HttpResponse response = client.execute(post);
        Log.d("charge", "response StatusCode:" + response.getStatusLine().getStatusCode()); // response StatusCode: 200
        HttpEntity resEntity = response.getEntity();
        String result = EntityUtils.toString(resEntity);
        Log.d("charge",result+"");
        return result;
    }
}