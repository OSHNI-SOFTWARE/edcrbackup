package bd.com.aristo.edcr.modules.gwds;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
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
import bd.com.aristo.edcr.utils.ui.texts.ATextView;
import io.realm.annotations.Ignore;

/**
 * Created by monir.sobuj on 01/11/17.
 */

public class GWDSDoctorsModel implements IItem<GWDSDoctorsModel, GWDSDoctorsModel.ViewHolder> {

    private String id;

    private String name, degree, special, address;
    private boolean isSaved, isSynced;
    private boolean isGwds;


    //variables needed for adapter
    @Ignore
    private boolean isSelected = false; // defines if the item is selected
    @Ignore
    private Object tag;// defines if this item is isSelectable
    @Ignore
    private boolean isSelectable = true;
    @Ignore
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
    public boolean isGwds() {
        return isGwds;
    }

    public void setGwds(boolean gwds) {
        isGwds = gwds;
    }




    @Override
    public String toString() {
        return "IDoctorsModel{" +
                "unique id='" + StringUtils.hashString64Bit(id) + '\'' +
                "id='" + id + '\'' +
                ", date='" + name + '\'' +
                ", degree='" + degree + '\'' +
                ", special='" + special + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GWDSDoctorsModel that = (GWDSDoctorsModel) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public Object getTag() {
        return tag;
    }

    @Override
    public GWDSDoctorsModel withTag(Object tag) {
        this.tag = tag;
        return this;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public GWDSDoctorsModel withEnabled(boolean enabled) {
        this.isEnabled = enabled;
        return this;
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public GWDSDoctorsModel withSetSelected(boolean selected) {
        this.isSelected = selected;
        return this;
    }

    @Override
    public boolean isSelectable() {
        return isSelectable;
    }

    @Override
    public GWDSDoctorsModel withSelectable(boolean selectable) {
        this.isSelectable = selectable;
        return this;
    }



    @Override
    public int getType() {
        return R.id.gwdsList;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_pwds_doctor;
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
//        SystemUtils.log("on bind view");
        holder.name.setText(name+" ["+id+"]");
        String degreeSpecial = degree+" <span style=\"color:#01991f;\">"+special+"</span> ";
        holder.degreeSpecial.setText(Html.fromHtml(degreeSpecial));
        if (!TextUtils.isEmpty(address)){
            holder.address.setVisibility(View.VISIBLE);
            holder.address.setText(address);
        }else{
            holder.address.setVisibility(View.GONE);
        }
       if(isGwds){
           tickOn(holder);
       } else
           checkOn(holder);
    }

    @Override
    public void unbindView(ViewHolder holder) {
        holder.name.setText(null);
        holder.degreeSpecial.setText(null);
        holder.address.setText(null);
        holder.ivCheck.setImageDrawable(null);
        holder.ivTick.setImageDrawable(null);
    }

    @Override
    public boolean equals(int id) {
        return false;
    }

    @Override
    public GWDSDoctorsModel withIdentifier(long identifier) {
        return null;
    }

    @Override
    public long getIdentifier() {
        return StringUtils.hashString64Bit(id);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        ATextView name, degreeSpecial, address;
        ImageView ivCheck, ivTick;

        public ViewHolder(View itemView) {
            super(itemView);

            name                                        = (ATextView) itemView.findViewById(R.id.name);
            degreeSpecial                               = (ATextView) itemView.findViewById(R.id.degreeSpecial);
            address                                     = (ATextView) itemView.findViewById(R.id.address);
            ivCheck                                     = (ImageView) itemView.findViewById(R.id.ivCheck);
            ivTick                                      = (ImageView) itemView.findViewById(R.id.ivTick);
        }
    }

    private void checkOn(ViewHolder holder){
        holder.ivCheck.setVisibility(View.VISIBLE);
        holder.ivTick.setVisibility(View.GONE);
    }

    private void tickOn(ViewHolder holder){
        holder.ivCheck.setVisibility(View.GONE);
        holder.ivTick.setVisibility(View.VISIBLE);
    }
}
