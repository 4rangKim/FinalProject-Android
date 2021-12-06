package finalproject.app.fcm;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
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
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;

import finalproject.app.fcm.util.PreferenceManager;

public class SignInActivity extends AppCompatActivity {
    ActivityResultLauncher<Intent> goSignUp_Launcher;
    ActivityResultLauncher<Intent> signIn_Launcher;
    ActivityResultLauncher<Intent> goFindId_Launcher;
    ActivityResultLauncher<Intent> goFindPwd_Launcher;
    EditText id, pwd;
    String point;
    int pointVal;
    Context mContext;
    CheckBox autoSignIn, saveIdAndPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mContext = this;
        autoSignIn = findViewById(R.id.autoLogin);
        saveIdAndPwd = findViewById(R.id.rememberIdandPwd);
        id = findViewById(R.id.signInId);
        pwd = findViewById(R.id.signInPwd);

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
                            autoSignIn.setChecked(false);
                            saveIdAndPwd.setChecked(false);
                            PreferenceManager.clear(mContext);
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
        autoSignIn.setOnClickListener(view -> {
            if(autoSignIn.isChecked()){
                saveIdAndPwd.setChecked(true);
                PreferenceManager.setBoolean(mContext,"autoSignIn",saveIdAndPwd.isChecked());
            }else {
                saveIdAndPwd.setChecked(false);
                PreferenceManager.setBoolean(mContext,"autoSignIn",saveIdAndPwd.isChecked());
                PreferenceManager.clear(mContext);
            }
        });
        saveIdAndPwd.setOnClickListener(view -> {
            if(saveIdAndPwd.isChecked()){
                PreferenceManager.setString(mContext,"id",id.getText().toString());
                PreferenceManager.setString(mContext,"pwd",pwd.getText().toString());
                PreferenceManager.setBoolean(mContext,"saveIdAndPwd",saveIdAndPwd.isChecked());
            }else {
                PreferenceManager.setBoolean(mContext,"saveIdAndPwd",saveIdAndPwd.isChecked());
                PreferenceManager.clear(mContext);
            }
        });
        Boolean saveIdAndPwdCheck = PreferenceManager.getBoolean(mContext,"saveIdAndPwd");
        if(saveIdAndPwdCheck){
            id.setText(PreferenceManager.getString(mContext,"id"));
            pwd.setText(PreferenceManager.getString(mContext,"pwd"));
            saveIdAndPwd.setChecked(true);
        }
        Boolean autoSignInCheck = PreferenceManager.getBoolean(mContext,"autoSignIn");
        if(autoSignInCheck){
            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
            String id = PreferenceManager.getString(mContext,"id");
            String point = 0+"";
            intent.putExtra("MemberId",id);
            intent.putExtra("MemberPoint",point);
            Log.d("signIn","Id:"+id+"Point:"+point);
            autoSignIn.setChecked(true);
            saveIdAndPwd.setChecked(true);
            signIn_Launcher.launch(intent);
        }
    }

    public void goSignUp(View v) {
        Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
        goSignUp_Launcher.launch(intent);
    }

    public void signIn(View v) {
        List<String> signInInfo = new ArrayList<String>();
        id = findViewById(R.id.signInId);
        pwd = findViewById(R.id.signInPwd);
        String idval = id.getText().toString();
        String pwdval = pwd.getText().toString();
        if(idval!=null&pwdval!=null){
            signInInfo.add(idval);
            signInInfo.add(pwdval);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        pointVal = signINHttp(signInInfo);
                        point = String.valueOf(pointVal);
                        if (pointVal == 0) {
                            id.setText("");
                            pwd.setText("");
                        } else {
                            pointVal = 1;
                            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                            intent.putExtra("MemberId",id.getText().toString());
                            intent.putExtra("MemberPwd",pwd.getText().toString());
                            intent.putExtra("MemberPoint",point);
                            signIn_Launcher.launch(intent);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }else {
            toast("아이디와 비밀번호를 확인해 주세요.");
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
        int point = Integer.parseInt(EntityUtils.toString(resEntity));
        Log.d("signIn",point+"");
        return point;
    }
}