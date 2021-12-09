package finalProject.app.fcm.util;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import finalProject.app.fcm.R;
import finalProject.app.fcm.vo.ParkingVO;

public class MainRecycleViewAdapter extends RecyclerView.Adapter<MainRecycleViewAdapter.ViewHolder> implements OnParkingLotItemClickListener{
    ArrayList<ParkingVO> items = new ArrayList<>();
    OnParkingLotItemClickListener listener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.recycleview_item_main, viewGroup, false);

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

    public void setOnItemClickListener(OnParkingLotItemClickListener listener){ this.listener = listener; }
    @Override
    public void onItemClick(ViewHolder holder, View view, int position) {
        if(listener != null){ listener.onItemClick(holder,view,position); }
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, empty;

        public ViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.parkingLotName);
            empty = itemView.findViewById(R.id.parkingLotEmptyState);
            String nameVal = name.getText().toString();
            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition() ;
                if (pos != RecyclerView.NO_POSITION) {
                    if(listener != null){
                        listener.onItemClick(ViewHolder.this, v, pos);
                    }
                }
            });
        }

        public void setItem(ParkingVO item) {
            String parkingLotName = item.getP_id();
            int emptyStateVal = item.getCount();
            name.setText(parkingLotName);
            empty.setText(String.valueOf(emptyStateVal));
            if(emptyStateVal==0){
                empty.setTextColor(Color.parseColor("#fe7057"));
            }else if (emptyStateVal<10){
                empty.setTextColor(Color.parseColor("#eb9d02"));
            }else {
                empty.setTextColor(Color.parseColor("#ffffff"));
            }
        }
    }
}