package bd.com.aristo.edcr.modules.bill.model;

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
import bd.com.aristo.edcr.utils.SystemUtils;
import bd.com.aristo.edcr.utils.constants.StringConstants;
import bd.com.aristo.edcr.utils.ui.texts.ATextView;
import io.realm.annotations.Ignore;

/**
 * Created by monir.sobuj on 6/8/17.
 */

public class IBillListItem implements IItem<IBillListItem, IBillListItem.ViewHolder> {


    private String day;

    private String nod;
    private String ta;
    private String da;
    private String total;
    private boolean hasRemarks;
    private String status;
    private boolean isReview;
    private long id;
    private boolean isFromServer;


    private Object tag;// defines if this item is isSelectable

    private boolean isSelectable = true;

    private boolean isEnabled = true;

    @Ignore
    private boolean isSelected = false; // defines if the item is selected

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getNod() {
        return nod;
    }

    public void setNod(String nod) {
        this.nod = nod;
    }

    public String getTa() {
        return ta;
    }

    public void setTa(String ta) {
        this.ta = ta;
    }

    public String getDa() {
        return da;
    }

    public void setDa(String da) {
        this.da = da;
    }


    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public boolean isHasRemarks() {
        return hasRemarks;
    }

    public void setHasRemarks(boolean hasRemarks) {
        this.hasRemarks = hasRemarks;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isReview() {
        return isReview;
    }

    public void setReview(boolean review) {
        isReview = review;
    }

    public boolean isFromServer() {
        return isFromServer;
    }

    public void setFromServer(boolean fromServer) {
        isFromServer = fromServer;
    }

    @Override
    public Object getTag() {
        return tag;
    }

    @Override
    public IBillListItem withTag(Object tag) {
        this.tag = tag;
        return this;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public IBillListItem withEnabled(boolean enabled) {
        this.isEnabled = enabled;
        return this;
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public IBillListItem withSetSelected(boolean selected) {
        isSelected = selected;
        return this;
    }


    @Override
    public boolean isSelectable() {
        return isSelectable;
    }

    @Override
    public IBillListItem withSelectable(boolean selectable) {
        this.isSelectable = selectable;
        return this;
    }

    @Override
    public int getType() {
        return R.id.rvBillList;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_bill_list;
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

        holder.txtDate.setText(day);
        holder.txtNDA.setText(nod);
        holder.txtTA.setText(ta);
        holder.txtDA.setText(da);
        holder.txtTotal.setText(total);

        if(status.equalsIgnoreCase(StringConstants.APPROVED_STATUS_APPROVED) && isFromServer){
            //holder.txtStatus.setBackgroundDrawable(SystemUtils.getDrawableFromID(ctx, R.drawable.bg_text_status_approved));
            holder.txtStatus.setText("Approved");
            holder.txtStatus.setTextColor(SystemUtils.getColorFromID(ctx, R.color.color2));
        } else if(status.equalsIgnoreCase(StringConstants.APPROVED_STATUS_PENDING) && isFromServer){
            //holder.txtStatus.setBackgroundDrawable(SystemUtils.getDrawableFromID(ctx, R.drawable.bg_text_status_upload));
            holder.txtStatus.setText("Uploaded");
            holder.txtStatus.setTextColor(SystemUtils.getColorFromID(ctx, R.color.color4));
        } else {
            //holder.txtStatus.setBackgroundDrawable(SystemUtils.getDrawableFromID(ctx, R.drawable.bg_text_status_saved));
            holder.txtStatus.setText("Saved");
            holder.txtStatus.setTextColor(SystemUtils.getColorFromID(ctx, R.color.black));
        }

        if(isReview) {
            //holder.txtStatus.setBackgroundDrawable(SystemUtils.getDrawableFromID(ctx, R.drawable.bg_text_status_review));
            holder.txtStatus.setText("Review");
            holder.txtStatus.setTextColor(SystemUtils.getColorFromID(ctx, R.color.red));
        }
        if(isHasRemarks()){
            holder.llRow.setBackgroundColor(SystemUtils.getColorFromID(ctx, R.color.cyan_light));

        } else
        {
            holder.llRow.setBackgroundColor(SystemUtils.getColorFromID(ctx, R.color.transparent));

        }
    }

    @Override
    public void unbindView(ViewHolder holder) {
        holder.txtDate.setText(null);
        holder.txtNDA.setText(null);
        holder.txtTA.setText(null);
        holder.txtDA.setText(null);
        holder.txtTotal.setText(null);

    }

    @Override
    public boolean equals(int code) {
        return false;
    }

    @Override
    public IBillListItem withIdentifier(long identifier) {
        return this;
    }

    @Override
    public long getIdentifier() {
        return id;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        ATextView txtDate;
        ATextView txtNDA;
        ATextView txtTA;
        ATextView txtDA;
        ATextView txtTotal;
        ATextView txtStatus;
        LinearLayout llRow;
        public ViewHolder(View itemView) {
            super(itemView);

            txtDate                                             = (ATextView) itemView.findViewById(R.id.txtDate);
            txtNDA                                              = (ATextView) itemView.findViewById(R.id.txtNDA);
            txtTA                                               = (ATextView) itemView.findViewById(R.id.txtTA);
            txtDA                                               = (ATextView) itemView.findViewById(R.id.txtDA);
            txtTotal                                            = (ATextView) itemView.findViewById(R.id.txtTotal);
            txtStatus                                        = (ATextView) itemView.findViewById(R.id.txtStatus);
            llRow                                        = (LinearLayout) itemView.findViewById(R.id.llRow);

        }
    }
}
