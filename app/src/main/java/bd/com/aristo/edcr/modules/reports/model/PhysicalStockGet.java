package bd.com.aristo.edcr.modules.reports.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.annotations.SerializedName;
import com.mikepenz.fastadapter.IItem;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.utils.StringUtils;
import bd.com.aristo.edcr.utils.SystemUtils;
import io.realm.annotations.Ignore;

/**
 * Created by monir.sobuj on 6/8/17.
 */

public class PhysicalStockGet implements IItem<PhysicalStockGet, PhysicalStockGet.ViewHolder>, Serializable {

    @SerializedName("ProductCode")
    private String productCode;

    @SerializedName("ProductName")
    private String productName;

    @SerializedName("MPGroup")
    private String mpGroup;

    @SerializedName("PackSize")
    private String packSize;

    @SerializedName("ItemFor")
    private String itemFor;

    @SerializedName("ItemType")
    private String itemType;

    @SerializedName("LogicalQty")
    private String balance;

    @SerializedName("PhysicalQty")
    private String actualBalance;

    @SerializedName("Remarks")
    private String remarks;

    @SerializedName("SetDate")
    private String date;

    private boolean clicked = false;


    private Object tag;//defines if this item is isSelectable

    private boolean isSelectable = true;

    private boolean isEnabled = true;
    @Ignore
    private boolean isSelected = false; // defines if the item is selected

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

    public String getMpGroup() {
        return mpGroup;
    }

    public void setMpGroup(String mpGroup) {
        this.mpGroup = mpGroup;
    }

    public String getPackSize() {
        return packSize;
    }

    public void setPackSize(String packSize) {
        this.packSize = packSize;
    }

    public String getItemFor() {
        return itemFor;
    }

    public void setItemFor(String itemFor) {
        this.itemFor = itemFor;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getActualBalance() {
        return actualBalance;
    }

    public void setActualBalance(String actualBalance) {
        this.actualBalance = actualBalance;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public boolean isClicked() {
        return clicked;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public Object getTag() {
        return tag;
    }

    @Override
    public PhysicalStockGet withTag(Object tag) {
        this.tag = tag;
        return this;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public PhysicalStockGet withEnabled(boolean enabled) {
        this.isEnabled = enabled;
        return this;
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public PhysicalStockGet withSetSelected(boolean selected) {
        isSelected = selected;
        return this;
    }

    @Override
    public boolean isSelectable() {
        return isSelectable;
    }

    @Override
    public PhysicalStockGet withSelectable(boolean selectable) {
        this.isSelectable = selectable;
        return this;
    }

    @Override
    public int getType() {
        return R.id.rvAccompany;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_psc_uploaded;
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
        String strType = "", strFor = "";
        if(itemType.equalsIgnoreCase("Sm")){
            strType = "Sample";
        } else if(itemType.equalsIgnoreCase("Sl")){
            strType = "Selected";
        } else {
            strType = "Gift";
        }
        if(itemFor.equalsIgnoreCase("R")){
            strFor = "Regular";
        } else {
            strFor = "Intern";
        }
        String product = productName +"["+packSize+"]" +" - " + strType +" for "+ strFor;
        String bal = "EDCR Bal: " + balance;
        String stock = "Stock Bal: " + actualBalance;
        holder.txtProductName.setText(product);
        holder.txtBalance.setText(bal);
        holder.txtActualBalance.setText(stock);
        if(balance.equalsIgnoreCase(actualBalance)){
            holder.txtActualBalance.setTextColor(SystemUtils.getColorFromID(ctx, R.color.blue));
        }
    }

    @Override
    public void unbindView(ViewHolder holder) {
        holder.txtActualBalance.setText(null);
        holder.txtBalance.setText(null);
        holder.txtProductName.setText(null);
    }

    @Override
    public boolean equals(int code) {
        return false;
    }

    @Override
    public PhysicalStockGet withIdentifier(long identifier) {
        return null;
    }

    @Override
    public long getIdentifier() {
        return StringUtils.hashString64Bit(productCode);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtProductName, txtBalance, txtActualBalance;
        public ImageView ivMinus, ivPlus;

        public ViewHolder(View itemView) {
            super(itemView);

            txtProductName                                  = (TextView) itemView.findViewById(R.id.txtProductName);
            txtBalance                                    = (TextView) itemView.findViewById(R.id.txtBalance);
            txtActualBalance                                    = (TextView) itemView.findViewById(R.id.txtActualBalance);
            ivMinus                                     = (ImageView) itemView.findViewById(R.id.ivMinus);
            ivPlus                                     = (ImageView) itemView.findViewById(R.id.ivPlus);
        }
    }
}
