package finalProject.app.fcm.util;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import finalProject.app.fcm.R;
import finalProject.app.fcm.vo.PayListVO;

public class MyPayListRecycleViewAdapter extends RecyclerView.Adapter<MyPayListRecycleViewAdapter.ViewHolder> implements Filterable {
    ArrayList<PayListVO> items = new ArrayList<>();
    ArrayList<PayListVO> allItems = new ArrayList<>();
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
        viewHolder.setItem(items.get(position));
    }
    @Override
    public int getItemCount() {
        return items.size();
    }
    public void addItem(PayListVO item) {
        items.add(item);
        allItems.add(item);
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
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String[] date = charSequence.toString().split(",");
                String[] startDate = date[0].split("-");
                String[] endDate = date[1].split("-");
                Log.d("adapter","startDate:"+startDate[0]+","+startDate[1]+","+startDate[2]+" endDate:"+endDate[0]+","+endDate[1]+","+endDate[2]);
                Calendar calStart = new GregorianCalendar();
                calStart.set(Calendar.YEAR,Integer.parseInt(startDate[0]));
                calStart.set(Calendar.MONTH,Integer.parseInt(startDate[1]));
                calStart.set(Calendar.DATE,Integer.parseInt(startDate[2]));
                long searchStartDate = calStart.getTimeInMillis();
                Calendar calEnd = new GregorianCalendar();
                calEnd.set(Calendar.YEAR,Integer.parseInt(endDate[0]));
                calEnd.set(Calendar.MONTH,Integer.parseInt(endDate[1]));
                calEnd.set(Calendar.DATE,Integer.parseInt(endDate[2]));
                long searchEndDate = calEnd.getTimeInMillis();
                String str = charSequence.toString();
                if(str.isEmpty()){
                    allItems = items;
                }else {
                    ArrayList<PayListVO> filteringList = new ArrayList<>();
                    for(int i =0; i<=(allItems.size()-1); i++){
                        String[] outDateFull = (allItems.get(i).getOut_time()).split(" ");
                        String[] outDate = outDateFull[0].split("-");
                        Calendar calOut = new GregorianCalendar();
                        calOut.set(Calendar.YEAR,Integer.parseInt(outDate[0]));
                        calOut.set(Calendar.MONTH,Integer.parseInt(outDate[1]));
                        calOut.set(Calendar.DATE,Integer.parseInt(outDate[2]));
                        long realOutDate = calOut.getTimeInMillis();
                        if(searchStartDate<realOutDate){
                            if(searchEndDate>realOutDate){
                                String mem_id = allItems.get(i).getMem_id();
                                String car_num = allItems.get(i).getCar_num();
                                String in_time = allItems.get(i).getIn_time();
                                String out_time = allItems.get(i).getOut_time();
                                int payment = allItems.get(i).getPayment();
                                PayListVO searchItem = new PayListVO(mem_id,car_num,in_time,out_time,payment);
                                filteringList.add(searchItem);
                            }
                        }
                    }
                    items = filteringList;
                }
                FilterResults results = new FilterResults();
                results.values = items;
                return results;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                items = (ArrayList<PayListVO>) filterResults.values;
                notifyDataSetChanged();
            }
        };
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