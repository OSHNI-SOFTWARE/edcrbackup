package bd.com.aristo.edcr.modules.reports.model;


import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mikepenz.fastadapter.items.AbstractItem;

import java.io.Serializable;
import java.util.List;

import bd.com.aristo.edcr.R;


/**
 * Created by hossain 05/05/20.
 */

public class AccompanyAdapter extends AbstractItem<AccompanyAdapter, AccompanyAdapter.ViewHolder> implements Serializable {


    private String mpoCode;
    private String mpoName;
    private String accompanyName;
    private String visitDate;
    private String morningCount;
    private String eveningCount;
    private String total;
    private long id;

    public String getMpoCode() {
        return mpoCode;
    }

    public void setMpoCode(String mpoCode) {
        this.mpoCode = mpoCode;
    }

    public String getMpoName() {
        return mpoName;
    }

    public void setMpoName(String mpoName) {
        this.mpoName = mpoName;
    }

    public String getAccompanyName() {
        return accompanyName;
    }

    public void setAccompanyName(String accompanyName) {
        this.accompanyName = accompanyName;
    }

    public String getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
    }

    public String getMorningCount() {
        return morningCount;
    }

    public void setMorningCount(String morningCount) {
        this.morningCount = morningCount;
    }

    public String getEveningCount() {
        return eveningCount;
    }

    public void setEveningCount(String eveningCount) {
        this.eveningCount = eveningCount;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    @Override
    public boolean equals(int id) {
        return false;
    }

    @Override
    public AccompanyAdapter withIdentifier(long identifier) {
        this.id=identifier;
        return this;
    }

    public long getId() {
        return id;
    }


    @Override
    public long getIdentifier() {
        return id;
    }

    @Override
    public int getType() {
        return R.id.rvDcrAccompany;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.accompany_item_row;
    }

    @Override
    public AccompanyAdapter.ViewHolder getViewHolder(View v) {
        return new AccompanyAdapter.ViewHolder(v);
    }

    @Override
    public void bindView(AccompanyAdapter.ViewHolder holder, List<Object> payloads) {
        super.bindView(holder, payloads);

        holder.mMpoName.setText(mpoName + " [" + mpoCode + "]");

        if (morningCount == null || morningCount.isEmpty())
            morningCount = "M: 0";
        else morningCount = "M: "+morningCount;
        holder.mMorningCount.setText(morningCount);

        if (eveningCount == null || eveningCount.isEmpty())
            eveningCount = "E: 0";
        else eveningCount = "E: "+ eveningCount;
        holder.mEveningCount.setText(eveningCount);


        if (visitDate == null || visitDate.isEmpty())
            visitDate = "Date:";
        else visitDate = "Date: "+visitDate;
        holder.mVisitDate.setText(visitDate);

        if (accompanyName == null || accompanyName.isEmpty())
            accompanyName = "";
        holder.mAccompany.setText( accompanyName);

        if (total == null || total.isEmpty())
            total = "0";
        holder.mTotal.setText("Total Visit:"+ total);
    }

    @Override
    public void unbindView(AccompanyAdapter.ViewHolder holder) {
        super.unbindView(holder);
        holder.mMpoName.setText(null);
        holder.mAccompany.setText(null);
        holder.mEveningCount.setText(null);
        holder.mMorningCount.setText(null);
        holder.mVisitDate.setText(null);
        holder.mTotal.setText(null);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mMpoName;
        private TextView mVisitDate;
        private TextView mAccompany;
        private TextView mMorningCount;
        private TextView mEveningCount;
        private TextView mTotal;
//
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mMpoName = itemView.findViewById(R.id.txtMpoName);
            mVisitDate = itemView.findViewById(R.id.txtVisitDate);
            mAccompany = itemView.findViewById(R.id.txtAccompany);
            mMorningCount = itemView.findViewById(R.id.txtMorning);
            mEveningCount = itemView.findViewById(R.id.txtEvening);
            mTotal = itemView.findViewById(R.id.txtTotal);
        }
    }
}
