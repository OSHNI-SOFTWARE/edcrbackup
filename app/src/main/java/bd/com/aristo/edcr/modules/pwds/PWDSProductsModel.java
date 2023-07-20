package bd.com.aristo.edcr.modules.pwds;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
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
 * Created by monir.sobuj on 02/11/17.
 */

public class PWDSProductsModel implements IItem<PWDSProductsModel, PWDSProductsModel.ViewHolder>{

    private String code;

    private String name;
    private String generic;
    private String packSize;
    private int quantity;
    private int status;
    private int planned;
    private boolean isApproved;
    private boolean isUploaded;

//    @Ignore
//    protected long uniqueID = -1;


    //variables needed for adapter
    protected boolean isSelected = false; // defines if the item is selected

    protected Object tag;// defines if this item is isSelectable

    protected boolean isSelectable = true;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code   = code;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackSize() {
        return packSize;
    }

    public void setPackSize(String packSize) {
        this.packSize = packSize;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getGeneric() {
        return generic;
    }

    public void setGeneric(String generic) {
        this.generic = generic;
    }
    public int getPlanned() {
        return planned;
    }

    public void setPlanned(int planned) {
        this.planned = planned;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

    public boolean isUploaded() {
        return isUploaded;
    }

    public void setUploaded(boolean uploaded) {
        isUploaded = uploaded;
    }

    @Override
    public String toString() {
        return "PWDSProductsModel{" +
                "unique id='" + StringUtils.hashString64Bit(code) + '\'' +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", generic='" + generic + '\'' +
                ", packSize='" + packSize + '\'' +
                ", allotted=" + quantity +
                ", planned=" + planned +
                ", status=" + status +
                ", isApproved=" + isApproved +
                '}';
    }

    @Override
    public Object getTag() {
        return tag;
    }

    @Override
    public PWDSProductsModel withTag(Object tag) {
        this.tag = tag;
        return this;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public PWDSProductsModel withEnabled(boolean enabled) {
        return null;
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public PWDSProductsModel withSetSelected(boolean selected) {
        return null;
    }

    @Override
    public boolean isSelectable() {
        return isSelectable;
    }

    @Override
    public PWDSProductsModel withSelectable(boolean selectable) {
        this.isSelectable = selectable;
        return this;
    }

    @Override
    public int getType() {
        return R.id.gwdsList;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_pwds_product;
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

        String rcvQty  = "Total qty: "+quantity;
        String planDoc = "Plan doc: "+planned;

        holder.name.setText(name);
        holder.generic.setText("");//(" ["+generic+"]");
        holder.allotted.setText(rcvQty);
        holder.planned.setText(planDoc);
        holder.itemView.setSelected(isSelected());

        Context ctx = holder.itemView.getContext();
        if(planned > 0){
            if(isApproved){
                holder.itemRowLayout.setBackgroundColor(SystemUtils.getColorFromID(ctx, R.color.blue));
                holder.name.setTextColor(SystemUtils.getColorFromID(ctx, R.color.white));
                holder.allotted.setTextColor(SystemUtils.getColorFromID(ctx, R.color.white));
                holder.planned.setTextColor(SystemUtils.getColorFromID(ctx, R.color.white));
            } else if(isUploaded){
                holder.itemRowLayout.setBackgroundColor(SystemUtils.getColorFromID(ctx, R.color.color4));
                holder.name.setTextColor(SystemUtils.getColorFromID(ctx, R.color.white));
                holder.allotted.setTextColor(SystemUtils.getColorFromID(ctx, R.color.white));
                holder.planned.setTextColor(SystemUtils.getColorFromID(ctx, R.color.white));
            } else {
                holder.itemRowLayout.setBackgroundColor(SystemUtils.getColorFromID(ctx, R.color.gray));
                holder.name.setTextColor(SystemUtils.getColorFromID(ctx, R.color.white));
                holder.allotted.setTextColor(SystemUtils.getColorFromID(ctx, R.color.white));
                holder.planned.setTextColor(SystemUtils.getColorFromID(ctx, R.color.white));
            }

        } else {
            holder.itemRowLayout.setBackgroundColor(SystemUtils.getColorFromID(ctx, R.color.white));
        }
    }

    @Override
    public void unbindView(ViewHolder holder) {
        holder.name.setText(null);
        holder.generic.setText(null);
    }

    @Override
    public boolean equals(int id) {
        return false;
    }

    @Override
    public PWDSProductsModel withIdentifier(long identifier) {
//        this.uniqueID = identifier;
        return null;
    }

    @Override
    public long getIdentifier() {
        return StringUtils.hashString64Bit(code);
    }


    static class ViewHolder extends RecyclerView.ViewHolder{

        ATextView name, generic, allotted, planned;
        LinearLayout itemRowLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            name = (ATextView) itemView.findViewById(R.id.name);
            generic = (ATextView) itemView.findViewById(R.id.generic);
            allotted = (ATextView) itemView.findViewById(R.id.allotted);
            planned = (ATextView) itemView.findViewById(R.id.planed);

            itemRowLayout = (LinearLayout) itemView.findViewById(R.id.itemRowLayout);
        }
    }
}