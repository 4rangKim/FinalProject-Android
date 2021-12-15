package finalProject.app.fcm;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

import finalProject.app.fcm.util.PreferenceManager;
import finalProject.app.fcm.vo.MemberVO;

public class SignInActivity extends AppCompatActivity {
    ActivityResultLauncher<Intent> signIn_Launcher;
    EditText id, pwd;
    String point;
    int resultCode;
    Context mContext;
    CheckBox saveIdAndPwd;
    MemberVO User;
    JSONObject Info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        getHashKey();
        mContext = this;
        resultCode = 0;
        User = null;
        Info = null;
        saveIdAndPwd = findViewById(R.id.rememberIdAndPwd);
        id = findViewById(R.id.signInId);
        pwd = findViewById(R.id.signInPwd);

        signIn_Launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        saveIdAndPwd.setChecked(false);
                        PreferenceManager.clear(mContext);
                    }
                });

        /* save Id And Pwd */
        saveIdAndPwd.setOnClickListener(view -> {
            if(saveIdAndPwd.isChecked()){
                PreferenceManager.setString(mContext,"id",id.getText().toString());
                PreferenceManager.setString(mContext,"pwd",pwd.getText().toString());
                PreferenceManager.setBoolean(mContext,"saveIdAndPwd",saveIdAndPwd.isChecked());
                Log.d("signIn","saveIdAndPwd: "+PreferenceManager.getBoolean(mContext,"saveIdAndPwd")+", (id="+PreferenceManager.getString(mContext,"id")+", pwd="+PreferenceManager.getString(mContext,"pwd")+")");
            }else {
                PreferenceManager.setBoolean(mContext,"saveIdAndPwd",saveIdAndPwd.isChecked());
                PreferenceManager.clear(mContext);
                Log.d("signIn","saveIdAndPwd: "+PreferenceManager.getBoolean(mContext,"saveIdAndPwd"));
            }
        });
        boolean saveIdAndPwdCheck = PreferenceManager.getBoolean(mContext,"saveIdAndPwd");
        if(saveIdAndPwdCheck){
            id.setText(PreferenceManager.getString(mContext,"id"));
            pwd.setText(PreferenceManager.getString(mContext,"pwd"));
            saveIdAndPwd.setChecked(true);
        }
    }
    public void goSignUp(View v) {
        Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
        startActivity(intent);
    }
    public void signIn(View v) {
        List<String> signInInfo = new ArrayList<>();
        id = findViewById(R.id.signInId);
        pwd = findViewById(R.id.signInPwd);
        String idVal = id.getText().toString();
        String pwdVal = pwd.getText().toString();
        if(!idVal.equals("") & !pwdVal.equals("")){
            signInInfo.add(idVal);
            signInInfo.add(pwdVal);
            new Thread(() -> {
                try {
                    resultCode = signINHttp(signInInfo);
                    if (resultCode==0) {
                        id.setText("");
                        pwd.setText("");
                    } else {
                        resultCode = 1;
                        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                        intent.putExtra("MemberId",User.getMem_id());
                        intent.putExtra("MemberPwd",User.getMem_pwd());
                        intent.putExtra("MemberName",User.getMem_name());
                        intent.putExtra("MemberTel",User.getMem_tel()+"");
                        intent.putExtra("MemberPoint",User.getMem_money()+"");
                        intent.putExtra("MemberCar1",User.getMem_car1());
                        intent.putExtra("MemberCar2",User.getMem_car2());
                        Log.d("signIn","User: "+User.toString());
                        signIn_Launcher.launch(intent);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
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
        startActivity(intent);
    }

    public void goFindPwd(View v) {
        Intent intent = new Intent(SignInActivity.this, FindPwdActivity.class);
        startActivity(intent);
    }

    public int signINHttp(List<String> signInInfo) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost("http://192.168.0.123:80/Finalweb/userlogin.mc");
        ArrayList<NameValuePair> MemberInfo = new ArrayList<>();
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
        String ResultInfo = EntityUtils.toString(resEntity);
        if(!ResultInfo.equals("null")){
            Log.d("signIn", point + "");
            try {
                JSONArray UserInfo = new JSONArray(ResultInfo);
                Info = UserInfo.getJSONObject(0);
                String mem_id = Info.getString("mem_id");
                String mem_pwd = Info.getString("mem_pwd");
                String mem_name = Info.getString("mem_name");
                String mem_tel = Info.getString("mem_tel");
                String mem_money = Info.getString("mem_money");
                String mem_car1 = Info.getString("mem_car1");
                String mem_car2 = Info.getString("mem_car2");
                Log.d("signIn",mem_id+", "+mem_pwd+", "+mem_name+", "+mem_tel+", "+mem_money+", "+mem_car1+", "+mem_car2);
                User = new MemberVO(mem_id, mem_pwd, mem_name, Integer.parseInt(mem_tel), Integer.parseInt(mem_money), mem_car1, mem_car2);
                Log.d("signIn",User.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return 1;
        }
        return 0;
    }
    private void getHashKey(){
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo == null){
            return;
        }
            Log.e("KeyHash", "KeyHash:null");

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            } catch (NoSuchAlgorithmException e) {
                Log.e("KeyHash", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
    }
}