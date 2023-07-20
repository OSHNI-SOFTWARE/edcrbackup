package bd.com.aristo.edcr.modules.reports.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.annotations.SerializedName;
import com.mikepenz.fastadapter.IItem;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.utils.StringUtils;


public class DoctorWiseItemModel implements IItem<DoctorWiseItemModel, DoctorWiseItemModel.ViewHolder>, Serializable {

    @SerializedName("PackSize")
    private String packSize;

    @SerializedName("ProductCode")
    private String productCode;

    @SerializedName("ProductName")
    private String productName;

    @SerializedName("Quantity")
    private String quantity;

    @SerializedName("SetDate")
    private String setDate; //SelectedItem,SampleItem,GiftItem

    protected boolean isSelected = false; // defines if the item is selected

    protected Object tag;// defines if this item is isSelectable

    protected boolean isSelectable = true;

    public DoctorWiseItemModel() {
    }

    public String getPackSize() {
        return packSize;
    }

    public void setPackSize(String packSize) {
        this.packSize = packSize;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getSetDate() {
        return setDate;
    }

    public void setSetDate(String setDate) {
        this.setDate = setDate;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    public void setSelectable(boolean selectable) {
        isSelectable = selectable;
    }

    @Override
    public Object getTag() {
        return tag;
    }

    @Override
    public DoctorWiseItemModel withTag(Object tag) {
        this.tag = tag;
        return this;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public DoctorWiseItemModel withEnabled(boolean enabled) {
        return null;
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public DoctorWiseItemModel withSetSelected(boolean selected) {
        return null;
    }

    @Override
    public boolean isSelectable() {
        return isSelectable;
    }

    @Override
    public DoctorWiseItemModel withSelectable(boolean selectable) {
        this.isSelectable = selectable;
        return this;
    }


    @Override
    public int getType() {
        return R.id.ssList;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_doctor_wise_item_row;
    }

    @Override
    public View generateView(Context ctx) {
        DoctorWiseItemModel.ViewHolder viewHolder = getViewHolder(LayoutInflater.from(ctx).inflate(getLayoutRes(), null, false));
        bindView(viewHolder, Collections.EMPTY_LIST);
        return viewHolder.itemView;
    }

    @Override
    public View generateView(Context ctx, ViewGroup parent) {
        DoctorWiseItemModel.ViewHolder viewHolder = getViewHolder(LayoutInflater.from(ctx).inflate(getLayoutRes(), parent, false));
        bindView(viewHolder, Collections.EMPTY_LIST);
        return viewHolder.itemView;
    }


    private DoctorWiseItemModel.ViewHolder getViewHolder(View view) {
        return new DoctorWiseItemModel.ViewHolder(view);
    }

    @Override
    public DoctorWiseItemModel.ViewHolder getViewHolder(ViewGroup parent) {
        return getViewHolder(LayoutInflater.from(parent.getContext()).inflate(getLayoutRes(), parent, false));
    }

    @Override
    public void bindView(DoctorWiseItemModel.ViewHolder holder, List<Object> payloads) {

        String name = productName + " ("+packSize+")";
        holder.txtName.setText(name);
        holder.txtCount.setText(quantity);
        holder.txtDate.setText(setDate);
    }

    @Override
    public void unbindView(DoctorWiseItemModel.ViewHolder holder) {
        holder.txtName.setText(null);
        holder.txtCount.setText(null);
        holder.txtDate.setText(null);

    }

    @Override
    public boolean equals(int id) {
        return false;
    }

    @Override
    public DoctorWiseItemModel withIdentifier(long identifier) {
        return null;
    }

    @Override
    public long getIdentifier() {
        return StringUtils.hashString64Bit(productCode);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtName, txtCount,txtDate;

        public ViewHolder(View itemView) {
            super(itemView);
            txtName =  itemView.findViewById(R.id.txtName);
            txtCount = itemView.findViewById(R.id.txtCount);
            txtDate =  itemView.findViewById(R.id.txtDate);
        }
    }
}
