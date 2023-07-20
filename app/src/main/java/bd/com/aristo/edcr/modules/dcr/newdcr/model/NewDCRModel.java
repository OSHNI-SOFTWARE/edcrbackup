package bd.com.aristo.edcr.modules.dcr.newdcr.model;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mikepenz.fastadapter.IItem;

import java.util.Collections;
import java.util.List;

import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.utils.StringUtils;
import bd.com.aristo.edcr.utils.constants.StringConstants;
import bd.com.aristo.edcr.utils.ui.texts.ATextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by monir.sobuj on 6/13/17.
 */

public class NewDCRModel extends RealmObject implements IItem<NewDCRModel, NewDCRModel.ViewHolder> {

    @PrimaryKey
    private long id;
    private String date;
    private String shift;
    private int option; //0-new doctor, 1-existing doctor, 2-intern, 3-others
    private String doctorName;
    private String doctorID;
    private String remarks;
    private boolean isSynced;
    private String noOfIntern;
    private String contact;  //or, address
    private String ward;  //or, degree

    private String accompanyId;  //or, degree


    private RealmList<NewDCRProductModel> newDCRProductModels;

    @Ignore
    private boolean clicked = false;
    @Ignore
    private Object tag;// defines if this item is isSelectable
    @Ignore
    private boolean isSelectable = true;
    @Ignore
    private boolean isEnabled = true;


    @Ignore
    private boolean isSelected = false; // defines if the item is selected

    public boolean isClicked() {
        return clicked;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }

    @Ignore
    public static String COL_ID = "id",
            COL_DOCTOR_ID = "doctorID",
            COL_DOCTOR_NAME = "doctorName",
            COL_DATE = "date",
            COL_SHIFT = "shift",
            COL_OPTION = "option";

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public int getOption() {
        return option;
    }

    public void setOption(int option) {
        this.option = option;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public boolean isSynced() {
        return isSynced;
    }

    public void setSynced(boolean synced) {
        isSynced = synced;
    }

    public String getNoOfIntern() {
        return noOfIntern;
    }

    public void setNoOfIntern(String noOfIntern) {
        this.noOfIntern = noOfIntern;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }


    public RealmList<NewDCRProductModel> getNewDCRProductModels() {
        return newDCRProductModels;
    }

    public void setNewDCRProductModels(RealmList<NewDCRProductModel> newDCRProductModels) {
        this.newDCRProductModels = newDCRProductModels;
    }

    @Override
    public String toString() {
        return "NewDCRModel{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", shift='" + shift + '\'' +
                ", option='" + option + '\'' +
                ", doctorName='" + doctorName + '\'' +
                ", doctorID='" + doctorID + '\'' +
                ", remarks='" + remarks + '\'' +
                ", isSynced=" + isSynced +
                ", noOfIntern=" + noOfIntern +
                ", contact='" + contact + '\'' +
                ", ward='" + ward + '\'' +
                ", accompanyId='" + accompanyId + '\'' +
                ", newDCRProductModels=" + newDCRProductModels +
                '}';
    }

    @Override
    public Object getTag() {
        return tag;
    }

    @Override
    public NewDCRModel withTag(Object tag) {
        this.tag = tag;
        return this;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public NewDCRModel withEnabled(boolean enabled) {
        this.isEnabled = enabled;
        return this;
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public NewDCRModel withSetSelected(boolean selected) {
        isSelected = selected;
        return this;
    }


    @Override
    public boolean isSelectable() {
        return isSelectable;
    }

    @Override
    public NewDCRModel withSelectable(boolean selectable) {
        this.isSelectable = selectable;
        return this;
    }

    @Override
    public int getType() {
        return R.id.rvNewDcr;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_newdcr_row;
    }

    @Override
    public View generateView(Context ctx) {
        NewDCRModel.ViewHolder viewHolder                           = getViewHolder(LayoutInflater.from(ctx).inflate(getLayoutRes(), null, false));
        bindView(viewHolder, Collections.EMPTY_LIST);
        return viewHolder.itemView;
    }

    @Override
    public View generateView(Context ctx, ViewGroup parent) {
        NewDCRModel.ViewHolder viewHolder                           = getViewHolder(LayoutInflater.from(ctx).inflate(getLayoutRes(), parent, false));
        bindView(viewHolder, Collections.EMPTY_LIST);
        return viewHolder.itemView;
    }

    private NewDCRModel.ViewHolder getViewHolder(View view) {
        return new NewDCRModel.ViewHolder(view);
    }

    @Override
    public NewDCRModel.ViewHolder getViewHolder(ViewGroup parent) {
        return getViewHolder(LayoutInflater.from(parent.getContext()).inflate(getLayoutRes(), parent, false));
    }

    @Override
    public void bindView(NewDCRModel.ViewHolder holder, List<Object> payloads) {

        Context ctx = holder.itemView.getContext();
        holder.tvRemark.setText(remarks);
        holder.address.setText(contact);
        String doctorNameID = "";
        if(!TextUtils.isEmpty(accompanyId)) {
            holder.txtAccompanyInfo.setText("Accompany with: "+accompanyId);
        }
        switch (option){
            case 0:
                holder.tvOptionTitle.setText(R.string.new_doctor);
                doctorNameID = doctorName+" (New)";
                break;
            case 1:
                holder.tvOptionTitle.setText(R.string.existing_doctor);
                doctorNameID = doctorName+" ("+ doctorID +")";
                break;
            case 2:
                holder.tvOptionTitle.setText(R.string.intern_doctor);
                doctorNameID = doctorName+" (Intern)";
                break;
            case 3:
                holder.tvOptionTitle.setText(R.string.new_dcr_other);
                doctorNameID = doctorName+" (Others)";
                break;
            default:
                break;
        }
        holder.tvName.setText(doctorNameID);
        holder.tvShiftName.setText(shift.equalsIgnoreCase(StringConstants.MORNING)?"MORNING":"EVENING");
        holder.tvDate.setText(date);
        if(!TextUtils.isEmpty(shift) && shift.equalsIgnoreCase(StringConstants.MORNING)){
            holder.ivShiftName.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_mini_morning_inverted));
        } else if(!TextUtils.isEmpty(shift) && shift.equalsIgnoreCase(StringConstants.EVENING)){
            holder.ivShiftName.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_mini_evening_inverted));
        }
        if(isSynced){
            //holder.ivSync.setVisibility(View.GONE);
            holder.ivSync.setImageResource(R.drawable.ic_mini_tick);
            holder.ivSync.setBackgroundColor(ctx.getResources().getColor(R.color.color2));
        } else {
            holder.ivSync.setVisibility(View.VISIBLE);
        }
        //holder.ivSync.setImageDrawable(null);

    }

    @Override
    public boolean equals(int code) {
        return false;
    }

    @Override
    public NewDCRModel withIdentifier(long identifier) {
        return this;
    }

    @Override
    public long getIdentifier() {
        return StringUtils.hashString64Bit(String.valueOf(id));
    }

    @Override
    public void unbindView(NewDCRModel.ViewHolder holder) {
        holder.tvRemark.setText(null);
        holder.txtAccompanyInfo.setText(null);
        holder.address.setText(null);
        holder.tvName.setText(null);
        holder.tvOptionTitle.setText(null);
        holder.tvShiftName.setText(null);
        holder.tvDate.setText(null);
        holder.ivShiftName.setImageDrawable(null);
        holder.ivSync.setImageDrawable(null);
    }

    public String getAccompanyId() {
        return accompanyId;
    }

    public void setAccompanyId(String accompanyId) {
        this.accompanyId = accompanyId;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.ivSync)
        public ImageView ivSync;
        @BindView(R.id.ivShiftName)
        ImageView ivShiftName;
        @BindView(R.id.tvDate)
        ATextView tvDate;

        @BindView(R.id.tvShiftName)
        ATextView tvShiftName;

        @BindView(R.id.tvOptionTitle)
        ATextView tvOptionTitle;

        @BindView(R.id.tvName)
        ATextView tvName;

        @BindView(R.id.address)
        ATextView address;

        @BindView(R.id.tvRemark)
        ATextView tvRemark;
        @BindView(R.id.txtAccompanyInfo)
        ATextView txtAccompanyInfo;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
