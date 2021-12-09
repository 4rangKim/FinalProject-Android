package finalProject.app.fcm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class SignUpActivity extends AppCompatActivity {
    Handler signUp_handler;
    EditText id, pwd, name, tel, carNum, idVal;
    TextView idDoubleCheck;
    Handler handler;
    boolean doubleCheck = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        signUp_handler = new Handler(Looper.myLooper());
        handler = new Handler(Looper.myLooper());
    }

    public void goSignIn(View v) {
        finish();
    }

    public void DoubleCheck(View v) {
        idVal = findViewById(R.id.memberId);
        idDoubleCheck = findViewById(R.id.idDoubleCheck);
        String id = idVal.getText().toString();
        new Thread(() -> {
            try {
                int result = DoubleCheck(id);
                if (result == 1) {
                    handler.post(() -> {
                        idDoubleCheck.setText("사용가능한 아이디 입니다.");
                        idDoubleCheck.setTextColor(getResources().getColor(R.color.yellow));
                        doubleCheck = true;
                    });
                } else {
                    handler.post(() -> {
                        idVal.setText("");
                        idVal.setHintTextColor(getResources().getColor(R.color.red));
                        idVal.setHint("사용할 수 없는 아이디 입니다.");
                        doubleCheck = false;
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void signUp(View v) {
        id = findViewById(R.id.memberId);
        pwd = findViewById(R.id.memberPwd);
        name = findViewById(R.id.memberName);
        tel = findViewById(R.id.memberPhone);
        carNum = findViewById(R.id.carNum);
        String idVal = id.getText().toString();
        String pwdVal = pwd.getText().toString();
        String nameVal = name.getText().toString();
        String telVal = tel.getText().toString();
        String carNumVal = carNum.getText().toString();
        if (!doubleCheck) {
            toast("아이디 중복확인을 해주세요.");
        }
        if (!idVal.equals("") & !pwdVal.equals("") & !nameVal.equals("") & !telVal.equals("") & !carNumVal.equals("")) {
            List<String> memberInfo = new ArrayList<>();
            memberInfo.add(idVal);
            memberInfo.add(pwdVal);
            memberInfo.add(nameVal);
            memberInfo.add(telVal);
            memberInfo.add(carNumVal);
            new Thread(() -> {
                try {
                    signUPHttp(memberInfo);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
            finish();
        } else {
            toast("가입 정보를 확인해 주세요.");
        }
    }

    public void toast(String msg) {
        Toast.makeText(this, msg + "", Toast.LENGTH_SHORT).show();
    }

    public void signUPHttp(List<String> memberInfo) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost("http://192.168.0.123:80/Finalweb/register.mc");
        ArrayList<NameValuePair> MemberInfo = new ArrayList<>();
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
        Log.e("signUp", "response StatusCode:" + response.getStatusLine().getStatusCode()); // response StatusCode: 200
    }

    public int DoubleCheck(String id) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost("http://192.168.0.123:80/Finalweb/doublecheck.mc");
        ArrayList<NameValuePair> MemberInfo = new ArrayList<>();
        try {
            MemberInfo.add(new BasicNameValuePair("id", URLDecoder.decode(id, "UTF-8")));
            Log.d("signUp", id + "");
            post.setEntity(new UrlEncodedFormEntity(MemberInfo, "UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            Log.e("signUp", ex.toString());
        }
        HttpResponse response = client.execute(post);
        Log.d("signUp", "response StatusCode:" + response.getStatusLine().getStatusCode()); // response StatusCode: 200
        HttpEntity resEntity = response.getEntity();
        int result = Integer.parseInt(EntityUtils.toString(resEntity));
        Log.d("signUp", result + "");
        return result;
    }

}