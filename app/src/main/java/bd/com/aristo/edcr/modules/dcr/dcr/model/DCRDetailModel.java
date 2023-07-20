package bd.com.aristo.edcr.modules.dcr.dcr.model;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mikepenz.fastadapter.IItem;

import java.util.Collections;
import java.util.List;

import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.utils.SystemUtils;
import bd.com.aristo.edcr.utils.ui.texts.ATextView;

/**
 * Created by monir.sobuj on 6/8/17.
 */

public class DCRDetailModel implements IItem<DCRDetailModel, DCRDetailModel.ViewHolder> {


    @SerializedName("ItemType")
    @Expose
    private String type;
    @SerializedName("ProductCode")
    @Expose
    private String code;
    @SerializedName("ProductName")
    @Expose
    private String name;
    @SerializedName("Quantity")
    @Expose
    private int count;

    private boolean clicked = false;



    private Object tag;// defines if this item is isSelectable

    private boolean isSelectable = true;

    private boolean isEnabled = true;
    //variables no need to store
    //variables needed for adapter

    private boolean isSelected = false; // defines if the item is selected

    public boolean isClicked() {
        return clicked;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }


    @Override
    public Object getTag() {
        return tag;
    }


    @Override
    public DCRDetailModel withTag(Object tag) {
        this.tag = tag;
        return this;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public DCRDetailModel withEnabled(boolean enabled) {
        this.isEnabled = enabled;
        return this;
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public DCRDetailModel withSetSelected(boolean selected) {
        isSelected = selected;
        return this;
    }


    @Override
    public boolean isSelectable() {
        return isSelectable;
    }

    @Override
    public DCRDetailModel withSelectable(boolean selectable) {
        this.isSelectable = selectable;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "DCRDetailModel{" +
                "type='" + type + '\'' +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", count=" + count +
                '}';
    }

    @Override
    public int getType() {
        return R.id.rvDoctors;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_dcr_sub_detail_row;
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
        holder.txtProductName.setText(name);
        holder.txtCount.setText(String.valueOf(count));
    }

    @Override
    public void unbindView(ViewHolder holder) {
        holder.txtCount.setText(null);
        holder.txtProductName.setText(null);
    }

    public void setTextColor(Context ctx, ATextView textView, int count, boolean isReverse){
        if(!isReverse) {
            if (count == 0) {
                textView.setTextColor(SystemUtils.getColorFromID(ctx, R.color.red));
            } else {
                textView.setTextColor(SystemUtils.getColorFromID(ctx, R.color.color2));
            }
        } else {
            if (count == 0) {
                textView.setTextColor(SystemUtils.getColorFromID(ctx, R.color.color2));
            } else {
                textView.setTextColor(SystemUtils.getColorFromID(ctx, R.color.red));
            }
        }
    }

    @Override
    public boolean equals(int code) {
        return false;
    }

    @Override
    public DCRDetailModel withIdentifier(long identifier) {
        return null;
    }

    @Override
    public long getIdentifier() {
        return 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        ATextView txtProductName;
        ATextView txtCount;

        public ViewHolder(View itemView) {
            super(itemView);

            txtProductName                                       = (ATextView) itemView.findViewById(R.id.productName);
            txtCount                                             = (ATextView) itemView.findViewById(R.id.count);
        }
    }
}
