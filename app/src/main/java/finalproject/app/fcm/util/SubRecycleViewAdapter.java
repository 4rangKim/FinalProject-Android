package finalproject.app.fcm.util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import finalproject.app.fcm.R;
import finalproject.app.fcm.vo.CarVo;
import finalproject.app.fcm.vo.P_AreaVO;

public class SubRecycleViewAdapter extends RecyclerView.Adapter<SubRecycleViewAdapter.ViewHolder> {
    ArrayList<P_AreaVO> items = new ArrayList<P_AreaVO>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.recycleview_item_sub, viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        P_AreaVO item = items.get(position);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(P_AreaVO item) {
        items.add(item);
    }
    public void setItems(ArrayList<P_AreaVO> items) {
        this.items = items;
    }
    public P_AreaVO getItem(int position) {
        return items.get(position);
    }
    public void setItem(int position, P_AreaVO item) {
        items.set(position, item);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView areaNum;
        ImageView activate, disabled;
        int state;

        public ViewHolder(View itemView) {
            super(itemView);
            areaNum = itemView.findViewById(R.id.area_id);
            activate = itemView.findViewById(R.id.areaActivate);
            disabled = itemView.findViewById(R.id.areaDisabled);
        }
        public void setItem(P_AreaVO item) {
            areaNum.setText(item.getArea_id());
            state = item.getState();
            if(state==1){
                disabled.setVisibility(View.VISIBLE);
                activate.setVisibility(View.INVISIBLE);
            }else {
                disabled.setVisibility(View.INVISIBLE);
                activate.setVisibility(View.VISIBLE);
            }
        }
    }
}