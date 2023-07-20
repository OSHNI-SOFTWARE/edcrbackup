package bd.com.aristo.edcr.modules.location;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mikepenz.fastadapter.IItem;

import java.util.Collections;
import java.util.List;

import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.utils.StringUtils;
import bd.com.aristo.edcr.utils.SystemUtils;
import bd.com.aristo.edcr.utils.ui.texts.ATextView;

/**
 * Created by monir.sobuj on 01/11/17.
 */

public class IDoctorLocation implements IItem<IDoctorLocation, IDoctorLocation.ViewHolder> {

    private String id;

    private String name;
    private String degree;
    private String special;
    @Nullable
    private String address;
    @Nullable
    private String mLoc;
    @Nullable
    private String eLoc;


    private boolean isSaved, isSynced;

    private boolean isSelected = false; // defines if the item is selected

    private Object tag;// defines if this item is isSelectable

    private boolean isSelectable = true;

    private boolean isEnabled = true;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }


    public boolean isSaved() {
        return isSaved;
    }

    public void setSaved(boolean saved) {
        isSaved = saved;
    }

    public boolean isSynced() {
        return isSynced;
    }

    public void setSynced(boolean synced) {
        isSynced = synced;
    }

    public String getmLoc() {
        return mLoc;
    }

    public void setmLoc(String mLoc) {
        this.mLoc = mLoc;
    }

    public String geteLoc() {
        return eLoc;
    }

    public void seteLoc(String eLoc) {
        this.eLoc = eLoc;
    }

    @Nullable
    public String getAddress() {
        return address;
    }

    public void setAddress(@Nullable String address) {
        this.address = address;
    }



    @Override
    public String toString() {
        return "IDoctorLocation{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", degree='" + degree + '\'' +
                ", special='" + special + '\'' +
                ", address='" + address + '\'' +
                ", mLoc='" + mLoc + '\'' +
                ", eLoc='" + eLoc + '\'' +
                ", isSaved=" + isSaved +
                ", isSynced=" + isSynced +
                '}';
    }

    @Override
    public Object getTag() {
        return tag;
    }

    @Override
    public IDoctorLocation withTag(Object tag) {
        this.tag = tag;
        return this;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public IDoctorLocation withEnabled(boolean enabled) {
        this.isEnabled = enabled;
        return this;
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public IDoctorLocation withSetSelected(boolean selected) {
        this.isSelected = selected;
        return this;
    }

    @Override
    public boolean isSelectable() {
        return isSelectable;
    }

    @Override
    public IDoctorLocation withSelectable(boolean selectable) {
        this.isSelectable = selectable;
        return this;
    }



    @Override
    public int getType() {
        return R.id.doctorList;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_doctor_location;
    }

    @Override
    public View generateView(Context ctx) {
        ViewHolder viewHolder                           = getViewHolder(LayoutInflater.from(ctx).inflate(getLayoutRes(), null, false));
        bindView(viewHolder, Collections.EMPTY_LIST);
        return viewHolder.itemView;
    }

    @Override
    public View generateView(Context ctx, ViewGroup parent) {
        ViewHolder viewHolder                           = getViewHolder(LayoutInflater.from(ctx).inflate(getLayoutRes(), parent, false));
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
        Context ctx = holder.itemView.getContext();
        //String nameId = " <span style=\"color:#FF9E02;\">"+name +"</span>"+" <span style=\"color:#01991f;\">"+ " [" + id + "]"+"</span> ";
        holder.id.setText(id);
        holder.name.setText(name);
        //String degreeSpecial = "<span style=\"color:#01991f;\">"+degree +"</span> "+"<span style=\"color:#6C00AA;\">"+", "+special+"</span> ";
        holder.degree.setText(degree);
        holder.special.setText(special);
        holder.address.setText(address);
        if(!TextUtils.isEmpty(mLoc)){
            holder.mLoc.setText(mLoc);
        } else {
            holder.mLoc.setText("");
        }
        if(!TextUtils.isEmpty(eLoc)){
            holder.eLoc.setText(eLoc);
        } else {
            holder.eLoc.setText("");
        }

        if(isSaved || isSynced){
            holder.itemView.setBackgroundColor(SystemUtils.getColorFromID(ctx, R.color.light_gray));
        } else {
            holder.itemView.setBackgroundColor(SystemUtils.getColorFromID(ctx, R.color.white));
        }
    }

    @Override
    public void unbindView(ViewHolder holder) {
        holder.name.setText(null);
        holder.degree.setText(null);
        holder.special.setText(null);
        holder.address.setText(null);
        holder.mLoc.setText(null);
        holder.eLoc.setText(null);
    }

    @Override
    public boolean equals(int id) {
        return false;
    }

    @Override
    public IDoctorLocation withIdentifier(long identifier) {
        return null;
    }

    @Override
    public long getIdentifier() {
        return StringUtils.hashString64Bit(id);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        ATextView id, name, degree, special, address, mLoc, eLoc;
        LinearLayout llEvening, llMorning;

        public ViewHolder(View itemView) {
            super(itemView);

            id                                          = (ATextView) itemView.findViewById(R.id.txtID);
            name                                        = (ATextView) itemView.findViewById(R.id.txtName);
            degree                                      = (ATextView) itemView.findViewById(R.id.txtDegree);
            special                                     = (ATextView) itemView.findViewById(R.id.txtSpecial);
            address                                     = (ATextView) itemView.findViewById(R.id.txtAddress);
            mLoc                                        = (ATextView) itemView.findViewById(R.id.txtMLoc);
            eLoc                                        = (ATextView) itemView.findViewById(R.id.txtELoc);
            llEvening                                   = (LinearLayout) itemView.findViewById(R.id.llEvening);
            llMorning                                   = (LinearLayout) itemView.findViewById(R.id.llMorning);

        }
    }

}
