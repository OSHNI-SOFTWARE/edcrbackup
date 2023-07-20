package bd.com.aristo.edcr.modules.dvr.model;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.utils.ui.texts.ATextView;

/**
 * Created by monir.sobuj on 01/11/17.
 */

public class IDoctorsModel implements IItem<IDoctorsModel, IDoctorsModel.ViewHolder> {

    private String id;

    private String name, degree, special, mLoc, eLoc;
    private boolean isSaved, isSynced;
    private boolean clicked = false;
    private List<DayShift> dayShiftList;
    long identifier;

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

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }


    public boolean isSaved() {
        return isSaved;
    }

    public void setSaved(boolean saved) {
        isSaved = saved;
    }

    public boolean isSynced() {
        return isSynced;
    }

    public void setSynced(boolean synced) {
        isSynced = synced;
    }

    public boolean isClicked() {
        return clicked;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }

    public List<DayShift> getDayShiftList() {
        return dayShiftList;
    }

    public void setDayShiftList(List<DayShift> dayShiftList) {
        this.dayShiftList = dayShiftList;
    }

    public String getmLoc() {
        return mLoc;
    }

    public void setmLoc(String mLoc) {
        this.mLoc = mLoc;
    }

    public String geteLoc() {
        return eLoc;
    }

    public void seteLoc(String eLoc) {
        this.eLoc = eLoc;
    }


    @Override
    public String toString() {
        return "IDoctorsModel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", degree='" + degree + '\'' +
                ", special='" + special + '\'' +
                ", isSaved=" + isSaved +
                ", isSynced=" + isSynced +
                '}';
    }

    @Override
    public Object getTag() {
        return tag;
    }

    @Override
    public IDoctorsModel withTag(Object tag) {
        this.tag = tag;
        return this;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public IDoctorsModel withEnabled(boolean enabled) {
        this.isEnabled = enabled;
        return this;
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public IDoctorsModel withSetSelected(boolean selected) {
        this.isSelected = selected;
        return this;
    }

    @Override
    public boolean isSelectable() {
        return isSelectable;
    }

    @Override
    public IDoctorsModel withSelectable(boolean selectable) {
        this.isSelectable = selectable;
        return this;
    }



    @Override
    public int getType() {
        return R.id.gwdsList;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_doctor;
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
        String degreeSpecial = degree+"("+special+")";
        holder.degreeSpecial.setText(degreeSpecial);
        holder.txtEveningLoc.setText("E: "+eLoc);
        holder.txtMorningLoc.setText("M: "+mLoc);
        holder.dotCount.setText("Dot count: "+dayShiftList.size());

       final FastItemAdapter<IDotModel> fastAdapterMorning = new FastItemAdapter<>();
        fastAdapterMorning.add(getMorningDot());
        fastAdapterMorning.setHasStableIds(false);
        holder.rvMorningDot.setLayoutManager(new LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false));
        holder.rvMorningDot.setAdapter(fastAdapterMorning);

        final FastItemAdapter<IDotModel> fastAdapterEvening = new FastItemAdapter<>();
        fastAdapterEvening.add(getEveningDot());
        fastAdapterEvening.setHasStableIds(false);
        holder.rvEveningDot.setLayoutManager(new LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false));
        holder.rvEveningDot.setAdapter(fastAdapterEvening);
    }

    @Override
    public void unbindView(ViewHolder holder) {
        holder.name.setText(null);
        holder.degreeSpecial.setText(null);
        holder.txtEveningLoc.setText(null);
        holder.txtMorningLoc.setText(null);

    }

    @Override
    public boolean equals(int id) {
        return false;
    }

    @Override
    public IDoctorsModel withIdentifier(long identifier) {
        this.identifier = identifier;
        return this;
    }

    @Override
    public long getIdentifier() {
        return identifier;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        ATextView name, degreeSpecial, txtMorningLoc, txtEveningLoc, dotCount;
        RecyclerView rvMorningDot,rvEveningDot;

        public ViewHolder(View itemView) {
            super(itemView);

            name                                        = (ATextView) itemView.findViewById(R.id.name);
            dotCount                                        = (ATextView) itemView.findViewById(R.id.dotCount);
            txtMorningLoc                                        = (ATextView) itemView.findViewById(R.id.txtMorningLoc);
            txtEveningLoc                                        = (ATextView) itemView.findViewById(R.id.txtEveningLoc);
            degreeSpecial                                      = (ATextView) itemView.findViewById(R.id.degreeSpecial);
            rvMorningDot                                     = (RecyclerView) itemView.findViewById(R.id.rvMDot);
            rvEveningDot                                     = (RecyclerView) itemView.findViewById(R.id.rvEDot);

        }
    }

    public List<IDotModel> getMorningDot(){
        List<IDotModel> dotModelList = new ArrayList<>();
        for(DayShift dayShift:dayShiftList){
            if(dayShift.isMorning()){
                IDotModel iDotModel = new IDotModel();
                iDotModel.withName(String.valueOf(dayShift.getDay()), dayShift.getWeekDay(), dayShift.isApproved());
                dotModelList.add(iDotModel);
            }
        }
        return dotModelList;
    }

    public List<IDotModel> getEveningDot(){
        List<IDotModel> dotModelList = new ArrayList<>();
        for(DayShift dayShift:dayShiftList){
            if(!dayShift.isMorning()){
                IDotModel iDotModel = new IDotModel();
                iDotModel.withName(String.valueOf(dayShift.getDay()), dayShift.getWeekDay(), dayShift.isApproved());
                dotModelList.add(iDotModel);
            }
        }
        return dotModelList;
    }

}
