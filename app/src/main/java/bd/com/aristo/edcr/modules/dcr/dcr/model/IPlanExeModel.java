package bd.com.aristo.edcr.modules.dcr.dcr.model;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.annotations.SerializedName;
import com.mikepenz.fastadapter.IItem;

import java.util.Collections;
import java.util.List;

import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.utils.StringUtils;
import bd.com.aristo.edcr.utils.constants.StringConstants;
import bd.com.aristo.edcr.utils.ui.texts.ATextView;

/**
 * Created by monir.sobuj on 06/11/17.
 */

public class IPlanExeModel implements IItem<IPlanExeModel, IPlanExeModel.ViewHolder> {

    @SerializedName("DoctorName")
    private String doctorName;
    @SerializedName("PlanSelected")
    private String planStar;
    @SerializedName("PlanSample")
    private String planSample;
    @SerializedName("PlanGift")
    private String planGift;
    @SerializedName("DCRSelected")
    private String dcrStar;
    @SerializedName("DCRSample")
    private String dcrSample;
    @SerializedName("DCRGift")
    private String dcrGift;
    @SerializedName("ShiftName")
    private String shift;
    @SerializedName("Accompany")
    private String accompany;
    @SerializedName("Remark")
    private String remarks;
    @SerializedName("Date")
    private String date;



    //variables needed for adapter
    protected boolean isSelected = false; // defines if the item is selected

    protected Object tag;// defines if this item is isSelectable

    protected boolean isSelectable = true;


    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getPlanStar() {
        return planStar;
    }

    public void setPlanStar(String planStar) {
        this.planStar = planStar;
    }

    public String getPlanSample() {
        return planSample;
    }

    public void setPlanSample(String planSample) {
        this.planSample = planSample;
    }

    public String getPlanGift() {
        return planGift;
    }

    public void setPlanGift(String planGift) {
        this.planGift = planGift;
    }

    public String getDcrStar() {
        return dcrStar;
    }

    public void setDcrStar(String dcrStar) {
        this.dcrStar = dcrStar;
    }

    public String getDcrSample() {
        return dcrSample;
    }

    public void setDcrSample(String dcrSample) {
        this.dcrSample = dcrSample;
    }

    public String getDcrGift() {
        return dcrGift;
    }

    public void setDcrGift(String dcrGift) {
        this.dcrGift = dcrGift;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public String getAccompany() {
        return accompany;
    }

    public void setAccompany(String accompany) {
        this.accompany = accompany;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "IPlanExeModel{" +
                "doctorName='" + doctorName + '\'' +
                ", planStar='" + planStar + '\'' +
                ", planSample='" + planSample + '\'' +
                ", planGift='" + planGift + '\'' +
                ", dcrStar='" + dcrStar + '\'' +
                ", dcrSample='" + dcrSample + '\'' +
                ", dcrGift='" + dcrGift + '\'' +
                ", shift='" + shift + '\'' +
                ", accompany='" + accompany + '\'' +
                ", remarks='" + remarks + '\'' +
                '}';
    }

    @Override
    public Object getTag() {
        return tag;
    }

    @Override
    public IPlanExeModel withTag(Object tag) {
        this.tag = tag;
        return this;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public IPlanExeModel withEnabled(boolean enabled) {
        return null;
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public IPlanExeModel withSetSelected(boolean selected) {
        return null;
    }

    @Override
    public boolean isSelectable() {
        return isSelectable;
    }

    @Override
    public IPlanExeModel withSelectable(boolean selectable) {
        this.isSelectable = selectable;
        return this;
    }

    @Override
    public int getType() {
        return R.id.gwdsList;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_plan_exe;
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
        holder.txtAccompany.setText(accompany);
        holder.txtDoctorName.setText(doctorName);
        holder.txtDate.setText(date);
        holder.txtPlanSelected.setText(planStar);
        holder.txtPlanSample.setText(planSample);
        holder.txtPlanGift.setText(planGift);
        holder.txtExecutionSelected.setText(dcrStar);
        holder.txtExecutionSample.setText(dcrSample);
        holder.txtExecutionGift.setText(dcrGift);
        holder.txtShift.setText(shift.equalsIgnoreCase(StringConstants.MORNING)? StringConstants.CAPITAL_MORNING:StringConstants.CAPITAL_EVENING);
        holder.txtRemarks.setText(remarks);
    }

    @Override
    public void unbindView(ViewHolder holder) {
        holder.txtAccompany.setText(null);
        holder.txtDoctorName.setText(null);
        holder.txtDate.setText(null);
        holder.txtPlanSelected.setText(null);
        holder.txtPlanSample.setText(null);
        holder.txtPlanGift.setText(null);
        holder.txtExecutionSelected.setText(null);
        holder.txtExecutionSample.setText(null);
        holder.txtExecutionGift.setText(null);
        holder.txtShift.setText(null);
        holder.txtRemarks.setText(null);
    }


    @Override
    public boolean equals(int id) {
        return false;
    }

    @Override
    public IPlanExeModel withIdentifier(long identifier) {
//        this.uniqueID = identifier;
        return null;
    }

    @Override
    public long getIdentifier() {
        return StringUtils.hashString64Bit(doctorName);
    }



    public static class ViewHolder extends RecyclerView.ViewHolder{

        ATextView txtDoctorName, txtDate, txtPlanSelected, txtPlanSample,
                txtPlanGift, txtExecutionSelected, txtExecutionSample, txtExecutionGift, txtShift, txtAccompany, txtRemarks;
        public ViewHolder(View itemView) {
            super(itemView);
            txtDoctorName = (ATextView) itemView.findViewById(R.id.txtDoctorName);
            txtDate = (ATextView) itemView.findViewById(R.id.txtDate);
            txtPlanSelected = (ATextView) itemView.findViewById(R.id.txtPlanSelected);
            txtPlanSample = (ATextView) itemView.findViewById(R.id.txtPlanSample);
            txtPlanGift = (ATextView) itemView.findViewById(R.id.txtPlanGift);
            txtExecutionSelected = (ATextView) itemView.findViewById(R.id.txtExecutionSelected);
            txtExecutionSample = (ATextView) itemView.findViewById(R.id.txtExecutionSample);
            txtExecutionGift = (ATextView) itemView.findViewById(R.id.txtExecutionGift);
            txtShift = (ATextView) itemView.findViewById(R.id.txtShift);
            txtAccompany = (ATextView) itemView.findViewById(R.id.txtAccompany);
            txtRemarks = (ATextView) itemView.findViewById(R.id.txtRemarks);


        }
    }
}