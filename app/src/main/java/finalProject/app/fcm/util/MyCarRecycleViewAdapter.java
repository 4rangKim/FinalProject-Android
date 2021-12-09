package finalProject.app.fcm.util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import finalProject.app.fcm.R;
import finalProject.app.fcm.vo.CarVo;

public class MyCarRecycleViewAdapter extends RecyclerView.Adapter<MyCarRecycleViewAdapter.ViewHolder> {
    ArrayList<CarVo> items = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.recycleview_item_mycar, viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        CarVo item = items.get(position);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(CarVo item) {
        items.add(item);
    }
    public void setItems(ArrayList<CarVo> items) {
        this.items = items;
    }
    public CarVo getItem(int position) {
        return items.get(position);
    }
    public void setItem(int position, CarVo item) {
        items.set(position, item);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView carNum;

        public ViewHolder(View itemView) {
            super(itemView);
            carNum = itemView.findViewById(R.id.myCarNum);
        }
        public void setItem(CarVo item) {
            carNum.setText(item.getCarNum());
        }
    }
}