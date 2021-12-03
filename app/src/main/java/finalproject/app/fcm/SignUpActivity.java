package finalproject.app.fcm;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;
import finalproject.app.fcm.vo.MemberVO;

public class SignUpActivity extends AppCompatActivity {
    ActivityResultLauncher<Intent> signUp_Launcher;
    Handler signUp_handler;
    EditText id, pwd, name, tel, carNum, idVal;
    TextView idDoubleCheck;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        signUp_handler = new Handler(Looper.myLooper());
        handler = new Handler(Looper.myLooper());
        signUp_Launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode()== Activity.RESULT_OK){

                        }
                    }
                });
    }
    public void goSignIn(View v){
        finish();
    }
    public void DoubleCheck(View v){
        idVal = findViewById(R.id.memberId);
        idDoubleCheck = findViewById(R.id.idDoubleCheck);
        String id = idVal.getText().toString();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int result = DoubleCheck(id);
                    if(result==1){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                idDoubleCheck.setText("사용가능한 아이디 입니다.");
                                idDoubleCheck.setTextColor(getResources().getColor(R.color.yellow));
                            }
                        });
                    }else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                idVal.setText("");
                                idVal.setHintTextColor(getResources().getColor(R.color.red));
                                idVal.setHint("사용할 수 없는 아이디 입니다.");
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public void signUp(View v){
        id = findViewById(R.id.memberId);
        pwd = findViewById(R.id.memberPwd);
        name = findViewById(R.id.memberName);
        tel = findViewById(R.id.memberPhone);
        carNum = findViewById(R.id.carNumber);
        List<String> memberInfo = new ArrayList<String>();
        memberInfo.add(id.getText().toString());
        memberInfo.add(pwd.getText().toString());
        memberInfo.add(name.getText().toString());
        memberInfo.add(tel.getText().toString());
        memberInfo.add(carNum.getText().toString());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    signUPHttp(memberInfo);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        signUp_Launcher.launch(intent);
    }
    public void signUPHttp(List<String> memberInfo) throws IOException {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://192.168.0.123:80/Finalweb/register.mc");
        ArrayList<NameValuePair> MemberInfo = new ArrayList<NameValuePair>();
        try {
            MemberInfo.add(new BasicNameValuePair("id", URLDecoder.decode(memberInfo.get(0), "UTF-8")));
            MemberInfo.add(new BasicNameValuePair("pwd", URLDecoder.decode(memberInfo.get(1), "UTF-8")));
            MemberInfo.add(new BasicNameValuePair("name", URLDecoder.decode(memberInfo.get(2), "UTF-8")));
            MemberInfo.add(new BasicNameValuePair("tel", URLDecoder.decode(memberInfo.get(3), "UTF-8")));
            MemberInfo.add(new BasicNameValuePair("carNum", URLDecoder.decode(memberInfo.get(4), "UTF-8")));
            post.setEntity(new UrlEncodedFormEntity(MemberInfo, "UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            Log.e("signUp", ex.toString());
        }
        HttpResponse response = client.execute(post);
        Log.e("signUp", "response StatusCode:"+response.getStatusLine().getStatusCode()); // response StatusCode: 200
    }
    public int DoubleCheck(String id) throws IOException {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://192.168.0.123:80/Finalweb/doublecheck.mc");
        ArrayList<NameValuePair> MemberInfo = new ArrayList<NameValuePair>();
        try {
            MemberInfo.add(new BasicNameValuePair("id", URLDecoder.decode(id, "UTF-8")));
            Log.d("signUp",id+"");
            post.setEntity(new UrlEncodedFormEntity(MemberInfo, "UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            Log.e("signUp", ex.toString());
        }
        HttpResponse response = client.execute(post);
        Log.d("signUp", "response StatusCode:"+response.getStatusLine().getStatusCode()); // response StatusCode: 200
        HttpEntity resEntity = response.getEntity();
        int result = Integer.parseInt(EntityUtils.toString(resEntity));
        Log.d("signUp",result+"");
        return result;
    }
}