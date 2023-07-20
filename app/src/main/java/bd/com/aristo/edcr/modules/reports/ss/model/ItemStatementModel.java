package bd.com.aristo.edcr.modules.reports.ss.model;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.gson.annotations.SerializedName;
import com.mikepenz.fastadapter.IItem;

import java.util.Collections;
import java.util.List;

import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.utils.StringUtils;
import bd.com.aristo.edcr.utils.SystemUtils;
import bd.com.aristo.edcr.utils.constants.StringConstants;
import bd.com.aristo.edcr.utils.ui.texts.ATextView;
import bd.com.aristo.edcr.utils.ui.texts.BTextView;
import io.realm.annotations.Ignore;

/**
 * Created by monir.sobuj on 01/11/17.
 */

public class ItemStatementModel implements IItem<ItemStatementModel, ItemStatementModel.ViewHolder> {

    @SerializedName("BalanceQty")
    private String balanceQty;

    @SerializedName("ExecuteQty")
    private String executeQty;

    @SerializedName("GenericName")
    private String genericName;

    @SerializedName("GivenQty")
    private String receivedQty;

    @SerializedName("ProductCode")
    private String productCode;

    @SerializedName("ProductName")
    private String productName;

    @SerializedName("RestQty")
    private String carryQty;

    @SerializedName("PackSize")
    private String packSize;

    @SerializedName("ItemFor")
    private String itemFor; //R, I
    @SerializedName("ItemType")
    private String itemType; //Sl,Sm,Gt

    @Ignore
    protected boolean isSelected = false; // defines if the item is selected

    @Ignore
    protected Object tag;// defines if this item is isSelectable

    @Ignore
    protected boolean isSelectable = true;

    public ItemStatementModel() {
    }

    public String getBalanceQty() {
        return balanceQty;
    }

    public void setBalanceQty(String balanceQty) {
        this.balanceQty = balanceQty;
    }

    public String getExecuteQty() {
        return executeQty;
    }

    public void setExecuteQty(String executeQty) {
        this.executeQty = executeQty;
    }

    public String getGenericName() {
        return genericName;
    }

    public void setGenericName(String genericName) {
        this.genericName = genericName;
    }

    public String getReceivedQty() {
        return receivedQty;
    }

    public void setReceivedQty(String receivedQty) {
        this.receivedQty = receivedQty;
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

    public String getCarryQty() {
        return carryQty;
    }

    public void setCarryQty(String carryQty) {
        this.carryQty = carryQty;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getItemFor() {
        return itemFor;
    }

    public void setItemFor(String itemFor) {
        this.itemFor = itemFor;
    }

    public String getPackSize() {
        return packSize;
    }

    public void setPackSize(String packSize) {
        this.packSize = packSize;
    }

    @Override
    public String toString() {
        return "ItemStatementModel{" +
                "balanceQty='" + balanceQty + '\'' +
                ", executeQty='" + executeQty + '\'' +
                ", genericName='" + genericName + '\'' +
                ", receivedQty='" + receivedQty + '\'' +
                ", productCode='" + productCode + '\'' +
                ", productName='" + productName + '\'' +
                ", carryQty='" + carryQty + '\'' +
                ", packSize='" + packSize + '\'' +
                ", itemFor='" + itemFor + '\'' +
                ", itemType='" + itemType + '\'' +
                '}';
    }

    @Override
    public Object getTag() {
        return tag;
    }

    @Override
    public ItemStatementModel withTag(Object tag) {
        this.tag = tag;
        return this;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public ItemStatementModel withEnabled(boolean enabled) {
        return null;
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public ItemStatementModel withSetSelected(boolean selected) {
        return null;
    }

    @Override
    public boolean isSelectable() {
        return isSelectable;
    }

    @Override
    public ItemStatementModel withSelectable(boolean selectable) {
        this.isSelectable = selectable;
        return this;
    }


    @Override
    public int getType() {
        return R.id.mRecyclerView;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_ssample_product;
    }

    @Override
    public View generateView(Context ctx) {
        ItemStatementModel.ViewHolder viewHolder = getViewHolder(LayoutInflater.from(ctx).inflate(getLayoutRes(), null, false));
        bindView(viewHolder, Collections.EMPTY_LIST);
        return viewHolder.itemView;
    }

    @Override
    public View generateView(Context ctx, ViewGroup parent) {
        ItemStatementModel.ViewHolder viewHolder = getViewHolder(LayoutInflater.from(ctx).inflate(getLayoutRes(), parent, false));
        bindView(viewHolder, Collections.EMPTY_LIST);
        return viewHolder.itemView;
    }


    private ItemStatementModel.ViewHolder getViewHolder(View view) {
        return new ItemStatementModel.ViewHolder(view);
    }

    @Override
    public ItemStatementModel.ViewHolder getViewHolder(ViewGroup parent) {
        return getViewHolder(LayoutInflater.from(parent.getContext()).inflate(getLayoutRes(), parent, false));
    }

    @Override
    public void bindView(ItemStatementModel.ViewHolder holder, List<Object> payloads) {
        Context ctx = holder.itemView.getContext();
        holder.rctQtyTV.setText("Rcv Qty: "+receivedQty);
        holder.carryQtyTV.setText("Carry Qty: "+carryQty);
        holder.exeQtyTV.setText("Exe. Qty: "+executeQty);
        holder.batQtyTV.setText("Bal Qty: "+balanceQty);

        if (itemType!=null && itemType.equalsIgnoreCase(StringConstants.SELECTED_ITEM)){
            //holder.itemIV.setImageResource(R.drawable.ic_mini_star);
            holder.generic.setText(genericName);
            holder.name.setText(""+productName+" ("+packSize+")");
            if (!TextUtils.isEmpty(genericName)){
                holder.generic.setVisibility(View.VISIBLE);
            }else{
                holder.generic.setVisibility(View.GONE);
            }
        }else if(itemType!=null && itemType.equalsIgnoreCase(StringConstants.SAMPLE_ITEM)){
            //holder.itemIV.setImageResource(R.drawable.ic_mini_product);
            holder.generic.setText(genericName);
            holder.name.setText(""+productName+" ("+packSize+")");
            if (!TextUtils.isEmpty(genericName)){
                holder.generic.setVisibility(View.VISIBLE);
            }else{
                holder.generic.setVisibility(View.GONE);
            }


        }else{
            holder.llMain.setBackgroundColor(SystemUtils.getColorFromID(ctx, R.color.light_gray));
            holder.generic.setText("");
            holder.generic.setVisibility(View.GONE);
            holder.name.setText(""+productName+" ("+packSize+")");
        }
    }

    @Override
    public void unbindView(ItemStatementModel.ViewHolder holder) {
        holder.txtItemFor.setText(null);
        holder.name.setText(null);
        holder.generic.setText(null);
        holder.rctQtyTV.setText(null);
        holder.carryQtyTV.setText(null);
        holder.exeQtyTV.setText(null);
        holder.batQtyTV.setText(null);
    }

    @Override
    public boolean equals(int id) {
        return false;
    }

    @Override
    public ItemStatementModel withIdentifier(long identifier) {
        return this;
    }

    @Override
    public long getIdentifier() {
        return StringUtils.hashString64Bit(productCode);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        ATextView name, generic, rctQtyTV, carryQtyTV,exeQtyTV,batQtyTV;
        BTextView txtItemFor;

        LinearLayout llMain;
        //ImageView itemIV;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (ATextView) itemView.findViewById(R.id.name);
            txtItemFor = (BTextView) itemView.findViewById(R.id.txtItemFor);
            generic = (ATextView) itemView.findViewById(R.id.generic);
            rctQtyTV = (ATextView) itemView.findViewById(R.id.rctQtyTV);
            carryQtyTV = (ATextView) itemView.findViewById(R.id.carryQtyTV);
            exeQtyTV = (ATextView) itemView.findViewById(R.id.exeQtyTV);
            batQtyTV = (ATextView) itemView.findViewById(R.id.batQtyTV);
            llMain = itemView.findViewById(R.id.llMain);
            //itemIV = (ImageView) itemView.findViewById(R.id.itemIV);
        }
    }
}
