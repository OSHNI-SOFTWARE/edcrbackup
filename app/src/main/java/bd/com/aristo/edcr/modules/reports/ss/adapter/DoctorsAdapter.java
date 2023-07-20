package bd.com.aristo.edcr.modules.reports.ss.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.models.Doctors;
import bd.com.aristo.edcr.utils.ui.texts.ATextView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by altaf.sil on 1/25/18.
 */

public class DoctorsAdapter extends RecyclerView.Adapter<DoctorsAdapter.MyViewHolder> {

    private static List<Doctors> mDataList = new ArrayList<>();

    private int mRowIndex = -1;


    public DoctorsAdapter() {
    }

    public void setData(List<Doctors> data) {
        mDataList.clear();

        mDataList.addAll(data);

        notifyDataSetChanged();
    }


    public void setRowIndex(int index) {
        mRowIndex = index;
    }


    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        final View view = LayoutInflater.from(context).inflate(R.layout.product_item_todoctor, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.name.setText(mDataList.get(position).getName());
        holder.countTV.setText(String.valueOf(mDataList.get(position).getCountItem()));
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.name)
        ATextView name;
        @BindView(R.id.countTV)
        ATextView countTV;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }

}
