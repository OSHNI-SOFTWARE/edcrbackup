package bd.com.aristo.edcr.modules.reports.ss.model;

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
import bd.com.aristo.edcr.utils.ui.texts.ATextView;

/**
 * Created by monir.sobuj on 01/11/17.
 */

public class ItemExecuteModel implements IItem<ItemExecuteModel, ItemExecuteModel.ViewHolder> {

    @SerializedName("DoctorID")
    private String doctorID;

    @SerializedName("DoctorName")
    private String doctorName;

    @SerializedName("GenericName")
    private String genericName;

    @SerializedName("ItemType")
    private String itemType;

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

    public ItemExecuteModel() {
    }

    public String getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getGenericName() {
        return genericName;
    }

    public void setGenericName(String genericName) {
        this.genericName = genericName;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
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
    public ItemExecuteModel withTag(Object tag) {
        this.tag = tag;
        return this;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public ItemExecuteModel withEnabled(boolean enabled) {
        return null;
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public ItemExecuteModel withSetSelected(boolean selected) {
        return null;
    }

    @Override
    public boolean isSelectable() {
        return isSelectable;
    }

    @Override
    public ItemExecuteModel withSelectable(boolean selectable) {
        this.isSelectable = selectable;
        return this;
    }


    @Override
    public int getType() {
        return R.id.ssList;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_execute_row;
    }

    @Override
    public View generateView(Context ctx) {
        ItemExecuteModel.ViewHolder viewHolder = getViewHolder(LayoutInflater.from(ctx).inflate(getLayoutRes(), null, false));
        bindView(viewHolder, Collections.EMPTY_LIST);
        return viewHolder.itemView;
    }

    @Override
    public View generateView(Context ctx, ViewGroup parent) {
        ItemExecuteModel.ViewHolder viewHolder = getViewHolder(LayoutInflater.from(ctx).inflate(getLayoutRes(), parent, false));
        bindView(viewHolder, Collections.EMPTY_LIST);
        return viewHolder.itemView;
    }


    private ItemExecuteModel.ViewHolder getViewHolder(View view) {
        return new ItemExecuteModel.ViewHolder(view);
    }

    @Override
    public ItemExecuteModel.ViewHolder getViewHolder(ViewGroup parent) {
        return getViewHolder(LayoutInflater.from(parent.getContext()).inflate(getLayoutRes(), parent, false));
    }

    @Override
    public void bindView(ItemExecuteModel.ViewHolder holder, List<Object> payloads) {
        holder.doctorIdTV.setText(doctorID);
        holder.name.setText(doctorName);
        holder.rcvQtyTV.setText(quantity);
        holder.executeDateTV.setText(setDate);
    }

    @Override
    public void unbindView(ItemExecuteModel.ViewHolder holder) {
        holder.name.setText(null);
        holder.rcvQtyTV.setText(null);
        holder.executeDateTV.setText(null);
        holder.doctorIdTV.setText(null);

    }

    @Override
    public boolean equals(int id) {
        return false;
    }

    @Override
    public ItemExecuteModel withIdentifier(long identifier) {
        return null;
    }

    @Override
    public long getIdentifier() {
        return StringUtils.hashString64Bit(productCode);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        ATextView name, rcvQtyTV,executeDateTV,doctorIdTV;

        public ViewHolder(View itemView) {
            super(itemView);
            doctorIdTV = (ATextView) itemView.findViewById(R.id.doctorIdTV);
            name = (ATextView) itemView.findViewById(R.id.name);
            rcvQtyTV = (ATextView) itemView.findViewById(R.id.rcvQtyTV);
            executeDateTV = (ATextView) itemView.findViewById(R.id.executeDateTV);
        }
    }
}
