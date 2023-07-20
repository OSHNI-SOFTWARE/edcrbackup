package bd.com.aristo.edcr.modules.reports.others;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;

import java.util.Collections;
import java.util.List;

import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.modules.dvr.fragment.DVRSelectionActivity;
import bd.com.aristo.edcr.modules.dvr.model.IDVRDoctors;
import bd.com.aristo.edcr.utils.StringUtils;
import bd.com.aristo.edcr.utils.ui.texts.ATextView;

/**
 * Created by monir.sobuj on 01/11/17.
 */

public class IDVRSummaryModel implements IItem<IDVRSummaryModel, IDVRSummaryModel.ViewHolder> {

    private String id;
    private int month;
    private int year;
    private boolean isCurrentMonth;
    private String morningCount, eveningCount, morningLoc, eveningLoc;
    private List<IDVRDoctors> mDVRDoctors;
    private List<IDVRDoctors> eDVRDoctors;


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

    public String getMorningCount() {
        return morningCount;
    }

    public void setMorningCount(String morningCount) {
        this.morningCount = morningCount;
    }

    public String getEveningCount() {
        return eveningCount;
    }

    public void setEveningCount(String eveningCount) {
        this.eveningCount = eveningCount;
    }

    public String getMorningLoc() {
        return morningLoc;
    }

    public void setMorningLoc(String morningLoc) {
        this.morningLoc = morningLoc;
    }

    public String getEveningLoc() {
        return eveningLoc;
    }

    public void setEveningLoc(String eveningLoc) {
        this.eveningLoc = eveningLoc;
    }

    public List<IDVRDoctors> getmDVRDoctors() {
        return mDVRDoctors;
    }

    public void setmDVRDoctors(List<IDVRDoctors> mDVRDoctors) {
        this.mDVRDoctors = mDVRDoctors;
    }

    public List<IDVRDoctors> geteDVRDoctors() {
        return eDVRDoctors;
    }

    public void seteDVRDoctors(List<IDVRDoctors> eDVRDoctors) {
        this.eDVRDoctors = eDVRDoctors;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public boolean isCurrentMonth() {
        return isCurrentMonth;
    }

    public void setCurrentMonth(boolean currentMonth) {
        isCurrentMonth = currentMonth;
    }

    @Override
    public Object getTag() {
        return tag;
    }

    @Override
    public IDVRSummaryModel withTag(Object tag) {
        this.tag = tag;
        return this;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public IDVRSummaryModel withEnabled(boolean enabled) {
        this.isEnabled = enabled;
        return this;
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public IDVRSummaryModel withSetSelected(boolean selected) {
        this.isSelected = selected;
        return this;
    }

    @Override
    public boolean isSelectable() {
        return isSelectable;
    }

    @Override
    public IDVRSummaryModel withSelectable(boolean selectable) {
        this.isSelectable = selectable;
        return this;
    }



    @Override
    public int getType() {
        return R.id.gwdsList;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_dvr_summary;
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
        final Context ctx = holder.itemView.getContext();
        holder.eveningCount.setText(eveningCount);
        holder.morningCount.setText(morningCount);
        holder.morningLoc.setText(morningLoc);
        holder.eveningLoc.setText(eveningLoc);
        holder.date.setText(id);
        final FastItemAdapter<IDVRDoctors> fastAdapterMorning = new FastItemAdapter<>();
        fastAdapterMorning.add(getmDVRDoctors());
        fastAdapterMorning.setHasStableIds(false);
        holder.rvMDoctors.setLayoutManager(new LinearLayoutManager(ctx));
        holder.rvMDoctors.setAdapter(fastAdapterMorning);

        final FastItemAdapter<IDVRDoctors> fastAdapterEvening = new FastItemAdapter<>();
        fastAdapterEvening.add(geteDVRDoctors());
        fastAdapterEvening.setHasStableIds(false);
        holder.rvEDoctors.setLayoutManager(new LinearLayoutManager(ctx));
        holder.rvEDoctors.setAdapter(fastAdapterEvening);

        //click to go to DVR Selection
        fastAdapterEvening.withOnClickListener(new FastAdapter.OnClickListener<IDVRDoctors>() {
            @Override
            public boolean onClick(View v, IAdapter<IDVRDoctors> adapter, IDVRDoctors item, int position) {
                DVRSelectionActivity.startFromList(ctx, month, year, item.getId(), isCurrentMonth, -20);
                return false;
            }
        });

        fastAdapterMorning.withOnClickListener(new FastAdapter.OnClickListener<IDVRDoctors>() {
            @Override
            public boolean onClick(View v, IAdapter<IDVRDoctors> adapter, IDVRDoctors item, int position) {
                DVRSelectionActivity.startFromList(ctx, month, year, item.getId(), isCurrentMonth, -20);
                return false;
            }
        });

    }

    @Override
    public void unbindView(ViewHolder holder) {
        holder.eveningCount.setText(null);
        holder.morningCount.setText(null);
        holder.morningLoc.setText(null);
        holder.eveningLoc.setText(null);
        holder.date.setText(null);
    }

    @Override
    public boolean equals(int id) {
        return false;
    }

    @Override
    public IDVRSummaryModel withIdentifier(long identifier) {
        return null;
    }

    @Override
    public long getIdentifier() {
        return StringUtils.hashString64Bit(id);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        ATextView eveningCount, morningCount, date, morningLoc, eveningLoc;
        RecyclerView rvMDoctors, rvEDoctors;


        public ViewHolder(View itemView) {
            super(itemView);

            eveningCount                                        = (ATextView) itemView.findViewById(R.id.txtEvening);
            morningCount                                        = (ATextView) itemView.findViewById(R.id.txtMorning);
            eveningLoc                                          = (ATextView) itemView.findViewById(R.id.txtEveningLoc);
            morningLoc                                          = (ATextView) itemView.findViewById(R.id.txtMorningLoc);
            date                                                = (ATextView) itemView.findViewById(R.id.txtDate);
            rvMDoctors                                          = (RecyclerView) itemView.findViewById(R.id.rvMDoctors);
            rvEDoctors                                          = (RecyclerView) itemView.findViewById(R.id.rvEDoctors);
        }
    }



}
