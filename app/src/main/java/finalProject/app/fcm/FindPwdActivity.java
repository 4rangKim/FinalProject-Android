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

public class FindPwdActivity extends AppCompatActivity {
    EditText id,name,tel;
    String resultMsg;
    Handler handler;
    TextView result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pwd);
        result = findViewById(R.id.findPwdResult);
        handler = new Handler(Looper.myLooper());
    }
    public void goSignIn(View v){
        finish();
    }
    public void findPwd(View v){
        List<String> findPwdInfo = new ArrayList<>();
        id = findViewById(R.id.findPwdId);
        name = findViewById(R.id.findPwdName);
        tel = findViewById(R.id.findPwdTel);
        findPwdInfo.add(id.getText().toString());
        findPwdInfo.add(name.getText().toString());
        findPwdInfo.add(tel.getText().toString());
        new Thread(() -> {
            try {
                resultMsg = findIdHttp(findPwdInfo);
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
    public String findIdHttp(List<String> findPwdInfo) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost("http://192.168.0.123:80/Finalweb/pwdcheck.mc");
        ArrayList<NameValuePair> PwdInfo = new ArrayList<>();
        try {
            PwdInfo.add(new BasicNameValuePair("id", URLDecoder.decode(findPwdInfo.get(0), "UTF-8")));
            PwdInfo.add(new BasicNameValuePair("name", URLDecoder.decode(findPwdInfo.get(1), "UTF-8")));
            PwdInfo.add(new BasicNameValuePair("tel", URLDecoder.decode(findPwdInfo.get(2), "UTF-8")));
            post.setEntity(new UrlEncodedFormEntity(PwdInfo, "UTF-8"));
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