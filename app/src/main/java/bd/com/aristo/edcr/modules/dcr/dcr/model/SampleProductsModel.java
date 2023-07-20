package bd.com.aristo.edcr.modules.dcr.dcr.model;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mikepenz.fastadapter.IItem;

import java.util.Collections;
import java.util.List;

import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.utils.StringUtils;
import bd.com.aristo.edcr.utils.SystemUtils;
import bd.com.aristo.edcr.utils.ui.texts.ATextView;

/**
 * Created by monir.sobuj on 06/11/17.
 */

public class SampleProductsModel implements IItem<SampleProductsModel, SampleProductsModel.ViewHolder>{

    private String code;

    private String name;
    private String generic;
    private String packSize;
    private int quantity;
    private int status;
    private int balance;
    private int planned;
    private int count;
    private int pType;
    private boolean isDoctorNotSelected = false;

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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getpType() {
        return pType;
    }

    public void setpType(int pType) {
        this.pType = pType;
    }

    public boolean isDoctorNotSelected() {
        return this.isDoctorNotSelected;
    }

    public void setDoctorNotSelected(final boolean doctorNotSelected) {
        this.isDoctorNotSelected = doctorNotSelected;
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
                ", count=" + count +
                ", isDoctorNotSelected=" + isDoctorNotSelected +
                '}';
    }

    @Override
    public Object getTag() {
        return tag;
    }

    @Override
    public SampleProductsModel withTag(Object tag) {
        this.tag = tag;
        return this;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public SampleProductsModel withEnabled(boolean enabled) {
        return null;
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public SampleProductsModel withSetSelected(boolean selected) {
        return null;
    }

    @Override
    public boolean isSelectable() {
        return isSelectable;
    }

    @Override
    public SampleProductsModel withSelectable(boolean selectable) {
        this.isSelectable = selectable;
        return this;
    }

    @Override
    public int getType() {
        return R.id.mRecyclerView;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_sample_row;
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
        Context ctx = holder.itemView.getContext();
        holder.name.setText(name);
        holder.generic.setText(generic);
        holder.allotted.setText("Total qty: "+quantity);
        holder.planned.setText("Plan qty: "+planned);
        holder.balQtyTV.setText("Bal qty: "+balance);
        if(isDoctorNotSelected){
            holder.name.setTextColor(SystemUtils.getColorFromID(ctx, R.color.red));
            holder.generic.setTextColor(SystemUtils.getColorFromID(ctx, R.color.red));
            holder.allotted.setTextColor(SystemUtils.getColorFromID(ctx, R.color.red));
            holder.planned.setTextColor(SystemUtils.getColorFromID(ctx, R.color.red));
            holder.balQtyTV.setTextColor(SystemUtils.getColorFromID(ctx, R.color.red));
        }

    }

    @Override
    public void unbindView(ViewHolder holder) {
        holder.name.setText(null);
        holder.generic.setText(null);
        holder.allotted.setText(null);
        holder.planned.setText(null);
        holder.balQtyTV.setText(null);
    }


    @Override
    public boolean equals(int id) {
        return false;
    }

    @Override
    public SampleProductsModel withIdentifier(long identifier) {
//        this.uniqueID = identifier;
        return null;
    }

    @Override
    public long getIdentifier() {
        return StringUtils.hashString64Bit(code);
    }


    static class ViewHolder extends RecyclerView.ViewHolder{
        ATextView name, generic, allotted, planned,balQtyTV;
        public ViewHolder(View itemView) {
            super(itemView);
            name = (ATextView) itemView.findViewById(R.id.name);
            generic = (ATextView) itemView.findViewById(R.id.generic);
            allotted = (ATextView) itemView.findViewById(R.id.allotted);
            planned = (ATextView) itemView.findViewById(R.id.planed);
            balQtyTV = (ATextView) itemView.findViewById(R.id.balQtyTV);

        }
    }
}