package bd.com.aristo.edcr.modules.reports.others;

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
import bd.com.aristo.edcr.utils.ui.texts.ATextView;

/**
 * Created by monir.sobuj on 01/11/17.
 */

public class DoctorCoverageModel implements IItem<DoctorCoverageModel, DoctorCoverageModel.ViewHolder> {


    private String category, value;


    private boolean isSelected = false; // defines if the item is selected

    private Object tag;// defines if this item is isSelectable

    private boolean isSelectable = true;

    private boolean isEnabled = true;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public Object getTag() {
        return tag;
    }

    @Override
    public DoctorCoverageModel withTag(Object tag) {
        this.tag = tag;
        return this;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public DoctorCoverageModel withEnabled(boolean enabled) {
        this.isEnabled = enabled;
        return this;
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public DoctorCoverageModel withSetSelected(boolean selected) {
        this.isSelected = selected;
        return this;
    }

    @Override
    public boolean isSelectable() {
        return isSelectable;
    }

    @Override
    public DoctorCoverageModel withSelectable(boolean selectable) {
        this.isSelectable = selectable;
        return this;
    }



    @Override
    public int getType() {
        return R.id.gwdsList;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_report_doctor_coverage;
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

        holder.category.setText(category);
        holder.value.setText(value);

    }

    @Override
    public void unbindView(ViewHolder holder) {
        holder.category.setText(null);
        holder.value.setText(null);
    }

    @Override
    public boolean equals(int id) {
        return false;
    }

    @Override
    public DoctorCoverageModel withIdentifier(long identifier) {
        return null;
    }

    @Override
    public long getIdentifier() {
        return StringUtils.hashString64Bit(category);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        ATextView category, value;

        public ViewHolder(View itemView) {
            super(itemView);

            category                                        = (ATextView) itemView.findViewById(R.id.txtCategory);
            value                                        = (ATextView) itemView.findViewById(R.id.txtValue);
        }
    }



}
