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

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    ArrayList<ParkingVO> resultList;
    public RecyclerAdapter(ArrayList<ParkingVO> itemList){
        resultList = itemList;
    }
    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position) {
        holder.onBind(resultList.get(position));
    }

    public void setParkingLotList(ArrayList<ParkingVO> list){
        this.resultList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView Occupied;
        TextView Total;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.parkingLotName);
            Occupied = itemView.findViewById(R.id.parkingLotOccupiedState);
            Total = itemView.findViewById(R.id.parkingLotTotalState);
        }

        void onBind(ParkingVO result){
            name.setText(result.getP_id()+"");
            Occupied.setText(result.getO_num()+"");
            Total.setText(result.getT_num()+"");
        }
    }
}
