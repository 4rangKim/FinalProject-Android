package finalProject.app.fcm.util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import finalProject.app.fcm.R;
import finalProject.app.fcm.vo.PayListVO;

public class MyPayListRecycleViewAdapter extends RecyclerView.Adapter<MyPayListRecycleViewAdapter.ViewHolder> {
    ArrayList<PayListVO> items = new ArrayList<>();

    public MyPayListRecycleViewAdapter(){
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.recycleview_item_mypay_list, viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        PayListVO item = items.get(position);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(PayListVO item) {
        items.add(item);
    }
    public void setItems(ArrayList<PayListVO> items) {
        this.items = items;
    }
    public PayListVO getItem(int position) {
        return items.get(position);
    }
    public void setItem(int position, PayListVO item) {
        items.set(position, item);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView carNum, inTime, outTime, cost;

        public ViewHolder(View itemView) {
            super(itemView);
            carNum = itemView.findViewById(R.id.payListCarNum);
            inTime = itemView.findViewById(R.id.payListInTime);
            outTime = itemView.findViewById(R.id.payListOutTime);
            cost = itemView.findViewById(R.id.payListCost);
        }
        public void setItem(PayListVO item) {

            carNum.setText(item.getCar_num());
            inTime.setText(item.getIn_time());
            outTime.setText(item.getOut_time());
            cost.setText(String.valueOf(item.getPayment()));
        }
    }
}