package bd.com.aristo.edcr.modules.wp.model;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mikepenz.fastadapter.IItem;

import java.util.Collections;
import java.util.List;

import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.utils.StringUtils;
import bd.com.aristo.edcr.utils.ui.texts.ATextView;
import bd.com.aristo.edcr.utils.ui.texts.BTextView;

/**
 * Created by monir.sobuj on 06/11/17.
 */

public class WPProductsModel implements IItem<WPProductsModel, WPProductsModel.ViewHolder>{

    private String code;

    private String name;
    private String generic;
    private String packSize;
    private int quantity;
    private int status;
    private int planned;
    private int balance;
    private int count;
    private boolean isGiven;

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

    public boolean isGiven() {
        return isGiven;
    }

    public void setGiven(boolean given) {
        isGiven = given;
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
                '}';
    }

    @Override
    public Object getTag() {
        return tag;
    }

    @Override
    public WPProductsModel withTag(Object tag) {
        this.tag = tag;
        return this;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public WPProductsModel withEnabled(boolean enabled) {
        return null;
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public WPProductsModel withSetSelected(boolean selected) {
        return null;
    }

    @Override
    public boolean isSelectable() {
        return isSelectable;
    }

    @Override
    public WPProductsModel withSelectable(boolean selectable) {
        this.isSelectable = selectable;
        return this;
    }

    @Override
    public int getType() {
        return R.id.gwdsList;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_wp_product;
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
        holder.name.setText(name);
        holder.tvBalance.setText("Balance: "+balance);
        holder.allotted.setText("Total qty: "+quantity);
        holder.planned.setText("Plan qty: "+planned);
        holder.tvCount.setText(""+count);
        holder.itemView.setSelected(isSelected());
        if(isGiven){
            holder.txtIsGiven.setVisibility(View.VISIBLE);
        } else {
            holder.txtIsGiven.setVisibility(View.GONE);
        }
    }

    @Override
    public void unbindView(ViewHolder holder) {
        holder.name.setText(null);
        holder.txtIsGiven.setText(null);
        holder.allotted.setText(null);
        holder.planned.setText(null);
        holder.tvCount.setText(null);
        holder.tvBalance.setText(null);
    }

    @Override
    public boolean equals(int id) {
        return false;
    }

    @Override
    public WPProductsModel withIdentifier(long identifier) {
//        this.uniqueID = identifier;
        return null;
    }

    @Override
    public long getIdentifier() {
        return StringUtils.hashString64Bit(code);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        ATextView name, generic, allotted, planned, tvCount, tvBalance;
        BTextView txtIsGiven;
        LinearLayout llClick;
        public ImageView ivPlus, ivMinus;
        public ViewHolder(View itemView) {
            super(itemView);
            name = (ATextView) itemView.findViewById(R.id.name);
            tvBalance = (ATextView) itemView.findViewById(R.id.balanced);
            //generic = (ATextView) itemView.findViewById(R.id.generic);
            allotted = (ATextView) itemView.findViewById(R.id.allotted);
            planned = (ATextView) itemView.findViewById(R.id.planed);
            txtIsGiven = (BTextView) itemView.findViewById(R.id.txtIsGiven);
            tvCount = (ATextView) itemView.findViewById(R.id.tvCount);
            ivMinus = (ImageView) itemView.findViewById(R.id.ivMinus);
            ivPlus = (ImageView) itemView.findViewById(R.id.ivPlus);
            llClick = (LinearLayout) itemView.findViewById(R.id.llClick);

        }
    }
}