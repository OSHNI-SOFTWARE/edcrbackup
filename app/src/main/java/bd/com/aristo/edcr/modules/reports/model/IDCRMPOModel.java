package bd.com.aristo.edcr.modules.reports.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.annotations.SerializedName;
import com.mikepenz.fastadapter.IItem;

import java.util.Collections;
import java.util.List;

import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.utils.StringUtils;


/**
 * Created by monir.sobuj on 06/11/17.
 */

public class IDCRMPOModel implements IItem<IDCRMPOModel, IDCRMPOModel.ViewHolder> {



    @SerializedName("Date")
    private String date;
    @SerializedName("EveningAbsent")
    private int eAbsent;
    @SerializedName("EveningDCR")
    private int eDCR;
    @SerializedName("EveningDOT")
    private int eDOT;
    @SerializedName("EveningNewDCR")
    private int eNewDCR;
    @SerializedName("EveningWP")
    private int eWP;
    @SerializedName("GiftInternQty")
    private int giftIntern;
    @SerializedName("GiftRegularQty")
    private int giftRegular;
    @SerializedName("MorningAbsent")
    private int mAbsent;
    @SerializedName("MorningDCR")
    private int mDCR;
    @SerializedName("MorningDOT")
    private int mDOT;
    @SerializedName("MorningNewDCR")
    private int mNewDCR;
    @SerializedName("MorningWP")
    private int mWP;
    @SerializedName("SampleInternQty")
    private int sampleIntern;
    @SerializedName("SampleRegularQty")
    private int sampleRegular;
    @SerializedName("SelectedRegularQty")
    private int selectedCount;
    @SerializedName("TotalDCR")
    private int totalDCR;



    //variables needed for adapter
    protected boolean isSelected = false; // defines if the item is selected

    protected Object tag;// defines if this item is isSelectable

    protected boolean isSelectable = true;

    public String getDate() {
        return this.date;
    }

    public void setDate(final String date) {
        this.date = date;
    }

    public int geteAbsent() {
        return this.eAbsent;
    }

    public void seteAbsent(final int eAbsent) {
        this.eAbsent = eAbsent;
    }

    public int geteDCR() {
        return this.eDCR;
    }

    public void seteDCR(final int eDCR) {
        this.eDCR = eDCR;
    }

    public int geteDOT() {
        return this.eDOT;
    }

    public void seteDOT(final int eDOT) {
        this.eDOT = eDOT;
    }

    public int geteNewDCR() {
        return this.eNewDCR;
    }

    public void seteNewDCR(final int eNewDCR) {
        this.eNewDCR = eNewDCR;
    }

    public int geteWP() {
        return this.eWP;
    }

    public void seteWP(final int eWP) {
        this.eWP = eWP;
    }

    public int getGiftIntern() {
        return this.giftIntern;
    }

    public void setGiftIntern(final int giftIntern) {
        this.giftIntern = giftIntern;
    }

    public int getGiftRegular() {
        return this.giftRegular;
    }

    public void setGiftRegular(final int giftRegular) {
        this.giftRegular = giftRegular;
    }

    public int getmAbsent() {
        return this.mAbsent;
    }

    public void setmAbsent(final int mAbsent) {
        this.mAbsent = mAbsent;
    }

    public int getmDCR() {
        return this.mDCR;
    }

    public void setmDCR(final int mDCR) {
        this.mDCR = mDCR;
    }

    public int getmDOT() {
        return this.mDOT;
    }

    public void setmDOT(final int mDOT) {
        this.mDOT = mDOT;
    }

    public int getmNewDCR() {
        return this.mNewDCR;
    }

    public void setmNewDCR(final int mNewDCR) {
        this.mNewDCR = mNewDCR;
    }

    public int getmWP() {
        return this.mWP;
    }

    public void setmWP(final int mWP) {
        this.mWP = mWP;
    }

    public int getSampleIntern() {
        return this.sampleIntern;
    }

    public void setSampleIntern(final int sampleIntern) {
        this.sampleIntern = sampleIntern;
    }

    public int getSampleRegular() {
        return this.sampleRegular;
    }

    public void setSampleRegular(final int sampleRegular) {
        this.sampleRegular = sampleRegular;
    }

    public int getSelectedCount() {
        return this.selectedCount;
    }

    public void setSelectedCount(final int selectedCount) {
        this.selectedCount = selectedCount;
    }

    public int getTotalDCR() {
        return this.totalDCR;
    }

    public void setTotalDCR(final int totalDCR) {
        this.totalDCR = totalDCR;
    }

    @Override
    public String toString() {
        return "IDCRMPOModel{" +
                "date='" + date + '\'' +
                ", eAbsent=" + eAbsent +
                ", eDCR=" + eDCR +
                ", eDOT=" + eDOT +
                ", eNewDCR=" + eNewDCR +
                ", eWP=" + eWP +
                ", giftIntern=" + giftIntern +
                ", giftRegular=" + giftRegular +
                ", mAbsent=" + mAbsent +
                ", mDCR=" + mDCR +
                ", mDOT=" + mDOT +
                ", mNewDCR=" + mNewDCR +
                ", mWP=" + mWP +
                ", sampleIntern=" + sampleIntern +
                ", sampleRegular=" + sampleRegular +
                ", selectedCount=" + selectedCount +
                ", totalDCR=" + totalDCR +
                '}';
    }

    @Override
    public Object getTag() {
        return tag;
    }

    @Override
    public IDCRMPOModel withTag(Object tag) {
        this.tag = tag;
        return this;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public IDCRMPOModel withEnabled(boolean enabled) {
        return null;
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public IDCRMPOModel withSetSelected(boolean selected) {
        return null;
    }

    @Override
    public boolean isSelectable() {
        return isSelectable;
    }

    @Override
    public IDCRMPOModel withSelectable(boolean selectable) {
        this.isSelectable = selectable;
        return this;
    }

    @Override
    public int getType() {
        return R.id.dcrList;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_dcr_mpo;
    }

    @Override
    public View generateView(Context ctx) {
        ViewHolder viewHolder = getViewHolder(LayoutInflater.from(ctx).inflate(getLayoutRes(), null, false));
        bindView(viewHolder, Collections.EMPTY_LIST);
        return viewHolder.itemView;
    }

    @Override
    public View generateView(Context ctx, ViewGroup parent) {
        ViewHolder viewHolder = getViewHolder(LayoutInflater.from(ctx).inflate(getLayoutRes(), parent, false));
        bindView(viewHolder, Collections.EMPTY_LIST);
        return viewHolder.itemView;
    }


    private ViewHolder getViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public ViewHolder getViewHolder(ViewGroup parent) {
        return getViewHolder(LayoutInflater.from(parent.getContext()).inflate(getLayoutRes(), parent, false));
    }

    @Override
    public void bindView(ViewHolder holder, List<Object> payloads) {
        holder.txtDate.setText(date);
        int giftCount = giftIntern + giftRegular;
        int sampleCount = sampleIntern + sampleRegular;
        holder.txtGift.setText("Gift: "+giftCount);
        holder.txtSample.setText("Sample: "+sampleCount);
        holder.txtSelected.setText("Selected: "+selectedCount);
        mDCR = mDCR - mAbsent;
        eDCR = eDCR - eAbsent;
        int totalDCR = eDCR + mDCR + eNewDCR + mNewDCR;
        holder.txtTotalDCR.setText("Total DCR: "+totalDCR);
        holder.txtEDCR.setText(""+eDCR);
        holder.txtEAbsent.setText(""+eAbsent);
        holder.txtENew.setText(""+eNewDCR);
        holder.txtEPlan.setText(""+eWP);
        holder.txtEDot.setText(""+eDOT);

        holder.txtMDCR.setText(""+mDCR);
        holder.txtMAbsent.setText(""+mAbsent);
        holder.txtMNew.setText(""+mNewDCR);
        holder.txtMPlan.setText(""+mWP);
        holder.txtMDot.setText(""+mDOT);

    }

    @Override
    public void unbindView(ViewHolder holder) {
        holder.txtDate.setText(null);
        holder.txtSelected.setText(null);
        holder.txtGift.setText(null);
        holder.txtSample.setText(null);
        holder.txtSelected.setText(null);
        holder.txtTotalDCR.setText(null);
        holder.txtEDCR.setText(null);
        holder.txtEAbsent.setText(null);
        holder.txtENew.setText(null);
        holder.txtEPlan.setText(null);
        holder.txtEDot.setText(null);
        holder.txtMDCR.setText(null);
        holder.txtMAbsent.setText(null);
        holder.txtMNew.setText(null);
        holder.txtMPlan.setText(null);
        holder.txtMDot.setText(null);

    }


    @Override
    public boolean equals(int id) {
        return false;
    }

    @Override
    public IDCRMPOModel withIdentifier(long identifier) {
//        this.uniqueID = identifier;
        return null;
    }

    @Override
    public long getIdentifier() {
        return StringUtils.hashString64Bit(date);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtDate, txtSelected, txtSample,
                txtGift, txtTotalDCR, txtEDCR, txtEAbsent, txtENew, txtEPlan, txtEDot, txtMDCR, txtMAbsent, txtMNew, txtMPlan, txtMDot;
        public ViewHolder(View itemView) {
            super(itemView);
            txtSelected = itemView.findViewById(R.id.txtSelected);
            txtSample = itemView.findViewById(R.id.txtSample);
            txtGift = itemView.findViewById(R.id.txtGift);
            txtTotalDCR = itemView.findViewById(R.id.txtTotalDCR);
            txtDate = itemView.findViewById(R.id.txtDate);

            txtEDCR = itemView.findViewById(R.id.txtEDCR);
            txtEAbsent = itemView.findViewById(R.id.txtEAbsent);
            txtENew = itemView.findViewById(R.id.txtENew);
            txtEPlan = itemView.findViewById(R.id.txtEPlan);
            txtEDot = itemView.findViewById(R.id.txtEDot);
            txtMAbsent = itemView.findViewById(R.id.txtMAbsent);
            txtMDCR = itemView.findViewById(R.id.txtMDCR);
            txtMNew = itemView.findViewById(R.id.txtMNew);
            txtMPlan = itemView.findViewById(R.id.txtMPlan);
            txtMDot = itemView.findViewById(R.id.txtMDot);
        }
    }
}