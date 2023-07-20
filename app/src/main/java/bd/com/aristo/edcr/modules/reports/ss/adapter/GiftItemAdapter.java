package bd.com.aristo.edcr.modules.reports.ss.adapter;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.modules.reports.ss.model.SSProductModel;
import bd.com.aristo.edcr.utils.ui.texts.ATextView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by altaf.sil on 1/25/18.
 */

public class GiftItemAdapter extends RecyclerView.Adapter<GiftItemAdapter.MyViewHolder> {

    private final Context mContext;
    private static List<SSProductModel> mData;

    public GiftItemAdapter(Context context) {
        mContext = context;
        mData = new ArrayList<>();
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.item_selective_product, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.name.setText(mData.get(position).getProductName());
        holder.rcvQtyTV.setText("Rcv. "+mData.get(position).getRcvQty());
        holder.planQtyTV.setText("Plan "+mData.get(position).getPlanQty());
        holder.executeQtyTV.setText("Exe. "+mData.get(position).getBalQty());
        holder.balQtyTV.setText("Bal. "+mData.get(position).getBalQty());

        holder.doctorsAdapter.setData(mData.get(position).getDoctorsList());
        holder.doctorsAdapter.setRowIndex(position);
        holder.doctorsAdapter.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void updateGiftItem(List<SSProductModel> pList){
        mData.addAll(pList);
        notifyDataSetChanged();

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.name)
        ATextView name;

        @BindView(R.id.rcvQtyTV)
        ATextView rcvQtyTV;

        @BindView(R.id.planQtyTV)
        ATextView planQtyTV;

        @BindView(R.id.executeQtyTV)
        ATextView executeQtyTV;

        @BindView(R.id.balQtyTV)
        ATextView balQtyTV;

        @BindView(R.id.verticalRecyclerView)
        RecyclerView verticalRecyclerView;

        private DoctorsAdapter doctorsAdapter;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
            Context context = itemView.getContext();
            verticalRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            doctorsAdapter = new DoctorsAdapter();
            verticalRecyclerView.setAdapter(doctorsAdapter);
        }
    }

}
