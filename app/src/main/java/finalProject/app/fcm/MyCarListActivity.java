package finalProject.app.fcm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import finalProject.app.fcm.util.MyCarRecycleViewAdapter;
import finalProject.app.fcm.vo.CarVo;

public class MyCarListActivity extends AppCompatActivity {
    Dialog addCarDialog, addCarFailDialog;
    String id, pwd, carNumVal, carNum1, carNum2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_car_list);
        /* Dialog */
        addCarDialog = new Dialog(MyCarListActivity.this);
        addCarDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        addCarDialog.setContentView(R.layout.dialog_add_car);
        addCarFailDialog = new Dialog(MyCarListActivity.this);
        addCarFailDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        addCarFailDialog.setContentView(R.layout.dialog_add_car_fail);

        /* Intent */
        Intent intent = getIntent();
        id = intent.getStringExtra("MemberId");
        pwd = intent.getStringExtra("MemberPwd");
        carNum1 = intent.getStringExtra("MemberCar1");
        carNum2 = "";
        if(intent.getStringExtra("MemberCar2")!=null){
            carNum2 = intent.getStringExtra("MemberCar2");
        }
        Log.d("addCar", id+", "+pwd+", "+carNum1+", "+carNum2);

        /* RecycleView */
        RecyclerView recyclerView = findViewById(R.id.myCarList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        MyCarRecycleViewAdapter adapter = new MyCarRecycleViewAdapter();
        if(carNum2==null){
            adapter.addItem(new CarVo(carNum1));
        }else {
            adapter.addItem(new CarVo(carNum1));
            Log.d("addCar","add carNum1: "+carNum1);
            adapter.addItem(new CarVo(carNum2));
            Log.d("addCar","add carNum2: "+carNum2);
        }
        recyclerView.setAdapter(adapter);
    }
    public void goMain(View v){
        finish();
    }
    public void goAddCarPage(View v){
        if(!carNum2.equals("")){
            addCarFailDialog.show();
            Button userOk;
            userOk = addCarFailDialog.findViewById(R.id.addCarFailOk);
            userOk.setOnClickListener(view -> addCarFailDialog.dismiss());
        }else {
            addCarDialog.show();
            ImageButton close;
            Button addCar;
            EditText newCarNum;
            List<String> addCarNumInfo = new ArrayList<>();
            newCarNum = addCarDialog.findViewById(R.id.newCarNum);
            close = addCarDialog.findViewById(R.id.addCarMenuCloseBtn);
            addCar = addCarDialog.findViewById(R.id.addCarBtn);

            close.setOnClickListener(view -> addCarDialog.dismiss());
            addCar.setOnClickListener(view -> {
                carNumVal = newCarNum.getText().toString();
                new Thread(() -> {
                    if(carNumVal!=null){
                        addCarNumInfo.add(id);
                        addCarNumInfo.add(pwd);
                        addCarNumInfo.add(carNumVal);
                        Log.d("addCar", addCarNumInfo.toString()+"");
                        try {
                            addCarHttp(addCarNumInfo);
                            addCarDialog.dismiss();
                            Log.d("addCar", "addCarHttp 성공");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            });
        }
    }
    public void addCarHttp(List<String> addCarNumInfo) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost("http://192.168.0.123:80/Finalweb/carinsert.mc");
        ArrayList<NameValuePair> addCarInfo = new ArrayList<>();
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
        Log.d("addCar", "addCarHttp 완료");
    }
}