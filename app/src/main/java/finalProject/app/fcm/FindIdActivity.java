package finalProject.app.fcm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;

public class FindIdActivity extends AppCompatActivity {
    EditText name,tel;
    String resultMsg;
    Handler handler;
    TextView result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_id);
        result = findViewById(R.id.findIdResult);
        handler = new Handler(Looper.myLooper());
    }
    public void goSignIn(View v){
        finish();
    }
    public void findId(View v){
        List<String> findIdInfo = new ArrayList<>();
        name = findViewById(R.id.findIdName);
        tel = findViewById(R.id.findIdTel);
        findIdInfo.add(name.getText().toString());
        findIdInfo.add(tel.getText().toString());
        new Thread(() -> {
            try {
                resultMsg = findIdHttp(findIdInfo);
                if(resultMsg.equals("fail")){
                    handler.post(() -> result.setText("일치하는 회원 정보를 찾지 못했습니다."));
                }else {
                    handler.post(() -> result.setText(resultMsg));
                }
                SystemClock.sleep(1000);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public String findIdHttp(List<String> findIdInfo) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost("http://192.168.0.123:80/Finalweb/idcheck.mc");
        ArrayList<NameValuePair> IdInfo = new ArrayList<>();
        try {
            IdInfo.add(new BasicNameValuePair("name", URLDecoder.decode(findIdInfo.get(0), "UTF-8")));
            IdInfo.add(new BasicNameValuePair("tel", URLDecoder.decode(findIdInfo.get(1), "UTF-8")));
            post.setEntity(new UrlEncodedFormEntity(IdInfo, "UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            Log.e("findId", ex.toString());
        }
        HttpResponse response = client.execute(post);
        Log.d("findId", "response StatusCode:" + response.getStatusLine().getStatusCode()); // response StatusCode: 200
        HttpEntity resEntity = response.getEntity();
        String result = EntityUtils.toString(resEntity);
        Log.d("findId",result+"");
        return result;
    }
}