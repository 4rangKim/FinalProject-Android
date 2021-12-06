package finalproject.app.fcm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

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

public class MyCarListActivity extends AppCompatActivity {
    Dialog addCarDialog;
    String id, pwd, carNumVal;
    int resultCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* Dialog */
        setContentView(R.layout.activity_my_car_list);
        addCarDialog = new Dialog(MyCarListActivity.this);
        addCarDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        addCarDialog.setContentView(R.layout.dialog_add_car);
        /* Intent */
        Intent intent = getIntent();
        id = intent.getStringExtra("MemberId");
        pwd = intent.getStringExtra("MemberPwd");
    }
    public void goMain(View v){
        finish();
    }
    public void goAddCarPage(View v){
        addCarDialog.show();
        ImageButton close;
        Button addCar;
        EditText newCarNum;
        List<String> addCarNumInfo = new ArrayList<String>();
        newCarNum = findViewById(R.id.newCarNum);
        close = findViewById(R.id.addCarMenuCloseBtn);
        addCar = findViewById(R.id.addCarBtn);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCarDialog.dismiss();
            }
        });
        addCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                carNumVal = newCarNum.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(carNumVal!=null){
                            addCarNumInfo.add(id);
                            addCarNumInfo.add(pwd);
                            addCarNumInfo.add(carNumVal);
                            try {
                                resultCode = addCarHttp(addCarNumInfo);
                                if(resultCode == 1){
                                    addCarDialog.dismiss();
                                }else {
                                    Log.d("main","myCarMenu intent:"+id+", "+pwd);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }
        });
    }
    public int addCarHttp(List<String> addCarNumInfo) throws IOException {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://192.168.0.123:80/Finalweb/.mc");
        ArrayList<NameValuePair> addCarInfo = new ArrayList<NameValuePair>();
        try {
            addCarInfo.add(new BasicNameValuePair("id", URLDecoder.decode(addCarNumInfo.get(0), "UTF-8")));
            addCarInfo.add(new BasicNameValuePair("pwd", URLDecoder.decode(addCarNumInfo.get(1), "UTF-8")));
            addCarInfo.add(new BasicNameValuePair("carNum", URLDecoder.decode(addCarNumInfo.get(2), "UTF-8")));
            post.setEntity(new UrlEncodedFormEntity(addCarInfo, "UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            Log.e("addCar", ex.toString());
        }
        HttpResponse response = client.execute(post);
        Log.d("addCar", "response StatusCode:" + response.getStatusLine().getStatusCode()); // response StatusCode: 200
        HttpEntity resEntity = response.getEntity();
        int resultCode = Integer.parseInt(EntityUtils.toString(resEntity));
        Log.d("addCar",resultCode+"");
        return resultCode;
    }
}