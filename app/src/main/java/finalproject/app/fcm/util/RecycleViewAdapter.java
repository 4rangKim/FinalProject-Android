package finalproject.app.fcm.util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import finalproject.app.fcm.R;
import finalproject.app.fcm.vo.ParkingVO;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {
    ArrayList<ParkingVO> items = new ArrayList<ParkingVO>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.recycleview_item, viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        ParkingVO item = items.get(position);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(ParkingVO item) {
        items.add(item);
    }

    public void setItems(ArrayList<ParkingVO> items) {
        this.items = items;
    }

    public ParkingVO getItem(int position) {
        return items.get(position);
    }

    public void setItem(int position, ParkingVO item) {
        items.set(position, item);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView Occupied;
        TextView Total;

        public ViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.parkingLotName);
            Occupied = itemView.findViewById(R.id.parkingLotOccupiedState);
            Total = itemView.findViewById(R.id.parkingLotTotalState);
        }

        public void setItem(ParkingVO item) {
            name.setText(item.getP_id()+"");
            Occupied.setText(item.getO_num()+"");
            Total.setText(item.getT_num()+"");
        }

    }

}