package bd.com.aristo.edcr.modules.reports.model;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mikepenz.fastadapter.IItem;

import java.util.Collections;
import java.util.List;

import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.utils.StringUtils;
import bd.com.aristo.edcr.utils.SystemUtils;
import io.realm.annotations.Ignore;

/**
 * Created by monir.sobuj on 6/8/17.
 */

public class PSCDateModel implements IItem<PSCDateModel, PSCDateModel.ViewHolder> {

    private String date;
    private String remarks;
    private List<PhysicalStockGet> physicalStockGetList;


    private boolean clicked = false;


    private Object tag;//defines if this item is isSelectable

    private boolean isSelectable = true;

    private boolean isEnabled = true;
    @Ignore
    private boolean isSelected = false; // defines if the item is selected


    public List<PhysicalStockGet> getPhysicalStockGetList() {
        return physicalStockGetList;
    }

    public void setPhysicalStockGetList(List<PhysicalStockGet> physicalStockGetList) {
        this.physicalStockGetList = physicalStockGetList;
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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public Object getTag() {
        return tag;
    }

    @Override
    public PSCDateModel withTag(Object tag) {
        this.tag = tag;
        return this;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public PSCDateModel withEnabled(boolean enabled) {
        this.isEnabled = enabled;
        return this;
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public PSCDateModel withSetSelected(boolean selected) {
        isSelected = selected;
        return this;
    }

    @Override
    public boolean isSelectable() {
        return isSelectable;
    }

    @Override
    public PSCDateModel withSelectable(boolean selectable) {
        this.isSelectable = selectable;
        return this;
    }

    @Override
    public int getType() {
        return R.id.pscDateRecycler;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_psc_date;
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
        if(isSelected){
            holder.llDate.setBackgroundColor(SystemUtils.getColorFromID(ctx, R.color.color4));
        }
        holder.txtDate.setText(date);
    }

    @Override
    public void unbindView(ViewHolder holder) {
        holder.txtDate.setText(null);
    }

    @Override
    public boolean equals(int code) {
        return false;
    }

    @Override
    public PSCDateModel withIdentifier(long identifier) {
        return null;
    }

    @Override
    public long getIdentifier() {
        return StringUtils.hashString64Bit(date);
    }





    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtDate;
        public LinearLayout llDate;

        public ViewHolder(View itemView) {
            super(itemView);

            txtDate                                  = (TextView) itemView.findViewById(R.id.txtDate);
            llDate                                  = (LinearLayout) itemView.findViewById(R.id.llDate);
        }
    }
}
