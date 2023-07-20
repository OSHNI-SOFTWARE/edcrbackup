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

public class DCRModelForServer implements IItem<DCRModelForServer, DCRModelForServer.ViewHolder> {


    @SerializedName("DCRCount")
    @Expose
    private int dcrCount;
    @SerializedName("SelectedItemCount")
    @Expose
    private int selectedProductCount;
    @SerializedName("Date")
    @Expose
    private String date;
    @SerializedName("SampleItemCount")
    @Expose
    private int sampleCount;
    @SerializedName("GiftItemCount")
    @Expose
    private int giftCount;
    @SerializedName("NewDCRCount")
    @Expose
    private int newDcrCount;
    @SerializedName("UnCoverdDCR")
    @Expose
    private int missedDcrCount;

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

    public int getDcrCount() {
        return dcrCount;
    }

    public void setDcrCount(int dcrCount) {
        this.dcrCount = dcrCount;
    }

    public int getSelectedProductCount() {
        return selectedProductCount;
    }

    public void setSelectedProductCount(int selectedProductCount) {
        this.selectedProductCount = selectedProductCount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getSampleCount() {
        return sampleCount;
    }

    public void setSampleCount(int sampleCount) {
        this.sampleCount = sampleCount;
    }

    public int getGiftCount() {
        return giftCount;
    }

    public void setGiftCount(int giftCount) {
        this.giftCount = giftCount;
    }

    public int getNewDcrCount() {
        return newDcrCount;
    }

    public void setNewDcrCount(int newDcrCount) {
        this.newDcrCount = newDcrCount;
    }

    public int getMissedDcrCount() {
        return missedDcrCount;
    }

    public void setMissedDcrCount(int missedDcrCount) {
        this.missedDcrCount = missedDcrCount;
    }

    @Override
    public Object getTag() {
        return tag;
    }


    @Override
    public DCRModelForServer withTag(Object tag) {
        this.tag = tag;
        return this;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public DCRModelForServer withEnabled(boolean enabled) {
        this.isEnabled = enabled;
        return this;
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public DCRModelForServer withSetSelected(boolean selected) {
        isSelected = selected;
        return this;
    }


    @Override
    public boolean isSelectable() {
        return isSelectable;
    }

    @Override
    public DCRModelForServer withSelectable(boolean selectable) {
        this.isSelectable = selectable;
        return this;
    }



    @Override
    public String toString() {
        return "DCRModelForServer{" +
                "dcrCount=" + dcrCount +
                ", selectedProductCount=" + selectedProductCount +
                ", date='" + date + '\'' +
                ", sampleCount=" + sampleCount +
                ", giftCount=" + giftCount +
                ", newDcrCount=" + newDcrCount +
                ", missedDcrCount=" + missedDcrCount +
                '}';
    }

    @Override
    public int getType() {
        return R.id.rvDoctors;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_dcr_server_row;
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
        holder.txtDate.setText(date);
        holder.txtDcrCount.setText(String.valueOf(dcrCount));
        holder.txtNewDcrCount.setText(String.valueOf(newDcrCount));
        holder.txtSelected.setText(String.valueOf(selectedProductCount));
        holder.txtSample.setText(String.valueOf(sampleCount));
        holder.txtGift.setText(String.valueOf(giftCount));
        holder.txtMissedDcrCount.setText(String.valueOf(missedDcrCount));
        setTextColor(ctx, holder.txtDcrCount, dcrCount, false);
        setTextColor(ctx, holder.txtNewDcrCount, newDcrCount, false);
        setTextColor(ctx, holder.txtMissedDcrCount, missedDcrCount, true);
        setTextColor(ctx, holder.txtSelected, selectedProductCount, false);
        setTextColor(ctx, holder.txtSample, sampleCount, false);
        setTextColor(ctx, holder.txtGift, giftCount, false);
    }

    @Override
    public void unbindView(ViewHolder holder) {
        holder.txtDcrCount.setText(null);
        holder.txtNewDcrCount.setText(null);
        holder.txtMissedDcrCount.setText(null);
        holder.txtSelected.setText(null);
        holder.txtSample.setText(null);
        holder.txtGift.setText(null);
        holder.txtDate.setText(null);
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
    public DCRModelForServer withIdentifier(long identifier) {
        return null;
    }

    @Override
    public long getIdentifier() {
        return 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        ATextView txtDcrCount;
        ATextView txtNewDcrCount;
        ATextView txtMissedDcrCount;
        ATextView txtSelected;
        ATextView txtSample;
        ATextView txtGift;
        ATextView txtDate;

        public ViewHolder(View itemView) {
            super(itemView);

            txtDcrCount                                       = (ATextView) itemView.findViewById(R.id.dcrCount);
            txtNewDcrCount                                    = (ATextView) itemView.findViewById(R.id.newDcrCount);
            txtMissedDcrCount                                 = (ATextView) itemView.findViewById(R.id.missedDcrCount);
            txtSelected                                       = (ATextView) itemView.findViewById(R.id.selected);
            txtSample                                         = (ATextView) itemView.findViewById(R.id.sample);
            txtGift                                           = (ATextView) itemView.findViewById(R.id.gift);
            txtDate                                        = (ATextView) itemView.findViewById(R.id.date);
        }
    }
}
