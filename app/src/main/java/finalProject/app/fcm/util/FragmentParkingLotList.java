package finalProject.app.fcm.util;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;
import finalProject.app.fcm.R;
import finalProject.app.fcm.SubActivity;
import finalProject.app.fcm.vo.ParkingVO;

public class FragmentParkingLotList extends Fragment {
    Timer timer;
    TimerTask timerTask;
    ParkingVO parkingLot;
    MainRecycleViewAdapter adapter;
    Handler handler;
    String msg;

    public static FragmentParkingLotList newInstance(int number) {
        FragmentParkingLotList parkingLotList = new FragmentParkingLotList();
        Bundle bundle = new Bundle();
        bundle.putInt("number", number);
        parkingLotList.setArguments(bundle);
        return parkingLotList;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            int num = getArguments().getInt("number");
        }
    }

    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {
        if(inflater!=null){
            ViewGroup rootView = (ViewGroup)LayoutInflater.from(inflater.getContext()).inflate(R.layout.fragment_main_parking_lot_list, container, false);
            handler = new Handler(Looper.myLooper());
            msg = "parkingLotList";
            /* RecycleView */
            RecyclerView recyclerView = rootView.findViewById(R.id.parkingLotList);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
            adapter = new MainRecycleViewAdapter();
            recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(new MainRecycleViewAdapter(){
                @Override
                public void onItemClick(MainRecycleViewAdapter.ViewHolder holder, View view, int position) {
                    super.onItemClick(holder, view, position);
                    ParkingVO item = adapter.getItem(position);
                    String p_id = item.getP_id();
                    Intent intent = new Intent(getActivity(), SubActivity.class);
                    intent.putExtra("p_id", p_id);
                    startActivity(intent);
                    timerTask.cancel();
                }
            });
            /* TimerTask */
            timer = new Timer(true);
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    try {
                        ParkingInfoHttp(msg);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    handler.post(() -> adapter.notifyDataSetChanged());
                }
                @Override
                public boolean cancel() {
                    Log.d("Fragment", "Main: ParkingInfo TimerTask is cancel");
                    return super.cancel();
                }
            };
            timer.schedule(timerTask, 0,5000);
            return rootView;
        }
        return null;
    }

    @Override
    public void onPause() {
        super.onPause();
        timerTask.cancel();
    }

    public void ParkingInfoHttp(String msg) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost("http://192.168.0.123:80/Finalweb/parealist.mc");
        ArrayList<NameValuePair> MemberInfo = new ArrayList<>();
        MemberInfo.add(new BasicNameValuePair("id", URLDecoder.decode(msg, "UTF-8")));
        post.setEntity(new UrlEncodedFormEntity(MemberInfo, "UTF-8"));
        HttpResponse response = client.execute(post);
        Log.d("main", "response StatusCode:" + response.getStatusLine().getStatusCode()); // response StatusCode: 200
        HttpEntity resEntity = response.getEntity();
        String ResultInfo = EntityUtils.toString(resEntity);
        Log.d("main", ResultInfo);
        if(!ResultInfo.equals("null")){
            adapter.remove();
            try {
                JSONArray Info = new JSONArray(ResultInfo);
                for(int i=0;i<Info.length();i++) {
                    JSONObject parkingListInfo = Info.getJSONObject(i);
                    String p_id = parkingListInfo.getString("p_id");
                    String count = parkingListInfo.getString("count");
                    Log.d("main", p_id + ", " + count);
                    parkingLot = new ParkingVO(p_id, Integer.parseInt(count));
                    adapter.addItem(parkingLot);
                    Log.d("main", parkingLot.toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
