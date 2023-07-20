package bd.com.aristo.edcr.modules.dvr.model;

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

public class IDVRDoctors implements IItem<IDVRDoctors, IDVRDoctors.ViewHolder> {

    private String id;
    private String name;
    private List<DayShift> dayShiftList;

    private boolean isSelected = false; // defines if the item is selected

    private Object tag;// defines if this item is isSelectable

    private boolean isSelectable = true;

    private boolean isEnabled = true;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }





    public List<DayShift> getDayShiftList() {
        return dayShiftList;
    }

    public void setDayShiftList(List<DayShift> dayShiftList) {
        this.dayShiftList = dayShiftList;
    }


    @Override
    public String toString() {
        return "IDVRDoctors{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", dayShiftList=" + dayShiftList +
                ", isSelected=" + isSelected +
                ", tag=" + tag +
                ", isSelectable=" + isSelectable +
                ", isEnabled=" + isEnabled +
                '}';
    }

    @Override
    public Object getTag() {
        return tag;
    }

    @Override
    public IDVRDoctors withTag(Object tag) {
        this.tag = tag;
        return this;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public IDVRDoctors withEnabled(boolean enabled) {
        this.isEnabled = enabled;
        return this;
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public IDVRDoctors withSetSelected(boolean selected) {
        this.isSelected = selected;
        return this;
    }

    @Override
    public boolean isSelectable() {
        return isSelectable;
    }

    @Override
    public IDVRDoctors withSelectable(boolean selectable) {
        this.isSelectable = selectable;
        return this;
    }



    @Override
    public int getType() {
        return R.id.gwdsList;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_dvr_summary_doc;
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
        holder.name.setText(name + " [" + id + "]");
    }

    @Override
    public void unbindView(ViewHolder holder) {
        holder.name.setText(null);

    }

    @Override
    public boolean equals(int id) {
        return false;
    }

    @Override
    public IDVRDoctors withIdentifier(long identifier) {
        return null;
    }

    @Override
    public long getIdentifier() {
        return StringUtils.hashString64Bit(id);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        ATextView name;

        public ViewHolder(View itemView) {
            super(itemView);

            name                                        = (ATextView) itemView.findViewById(R.id.name);
        }
    }



}
