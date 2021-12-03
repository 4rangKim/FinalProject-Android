package finalproject.app.fcm;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

public class SignInActivity extends AppCompatActivity {
    ActivityResultLauncher<Intent> goSignUp_Launcher;
    ActivityResultLauncher<Intent> signIn_Launcher;
    ActivityResultLauncher<Intent> goFindId_Launcher;
    ActivityResultLauncher<Intent> goFindPwd_Launcher;
    EditText id, pwd;
    int resultCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        goSignUp_Launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {

                        }
                    }
                });
        signIn_Launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {

                        }
                    }
                });
        goFindId_Launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {

                        }
                    }
                });
        goFindPwd_Launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {

                        }
                    }
                });
    }

    public void goSignUp(View v) {
        Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
        goSignUp_Launcher.launch(intent);
    }

    public void signIn(View v) {
        List<String> signInInfo = new ArrayList<String>();
        id = findViewById(R.id.signInId);
        pwd = findViewById(R.id.signInPwd);
        signInInfo.add(id.getText().toString());
        signInInfo.add(pwd.getText().toString());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    resultCode = signINHttp(signInInfo);
                    if (resultCode == 1) {
                        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                        signIn_Launcher.launch(intent);
                    } else if (resultCode == 0) {
                        id.setText("");
                        pwd.setText("");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(resultCode==0){
            toast("로그인 실패");
        }
    }
    public void toast(String msg){
        Toast.makeText(this,msg+"",Toast.LENGTH_SHORT).show();
    }
    public void goFindId(View v) {
        Intent intent = new Intent(SignInActivity.this, FindIdActivity.class);
        goFindId_Launcher.launch(intent);
    }

    public void goFindPwd(View v) {
        Intent intent = new Intent(SignInActivity.this, FindPwdActivity.class);
        goFindPwd_Launcher.launch(intent);
    }

    public int signINHttp(List<String> signInInfo) throws IOException {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://192.168.0.123:80/Finalweb/userlogin.mc");
        ArrayList<NameValuePair> MemberInfo = new ArrayList<NameValuePair>();
        try {
            MemberInfo.add(new BasicNameValuePair("id", URLDecoder.decode(signInInfo.get(0), "UTF-8")));
            MemberInfo.add(new BasicNameValuePair("pwd", URLDecoder.decode(signInInfo.get(1), "UTF-8")));
            post.setEntity(new UrlEncodedFormEntity(MemberInfo, "UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            Log.e("signIn", ex.toString());
        }
        HttpResponse response = client.execute(post);
        Log.d("signIn", "response StatusCode:" + response.getStatusLine().getStatusCode()); // response StatusCode: 200
        HttpEntity resEntity = response.getEntity();
        int result = Integer.parseInt(EntityUtils.toString(resEntity));
        Log.d("signIn",result+"");
        return result;
    }
}