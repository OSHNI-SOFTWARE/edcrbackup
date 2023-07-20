package bd.com.aristo.edcr.modules.reports.others;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.utils.StringUtils;
import bd.com.aristo.edcr.utils.ui.texts.ATextView;

/**
 * Created by monir.sobuj on 6/8/17.
 */

public class IDoctorItem implements IItem<IDoctorItem, IDoctorItem.ViewHolder> {

    private String id;

    private String name, degree, special, eLoc, mLoc;
    private boolean clicked = false;

    private List<DayDot> dotList;


    private Object tag;// defines if this item is isSelectable
    private boolean isSelectable = true;
    private boolean isEnabled = true;

    private boolean isSelected = false; // defines if the item is selected


    public boolean isClicked() {
        return clicked;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }
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

    public String geteLoc() {
        return eLoc;
    }

    public void seteLoc(String eLoc) {
        this.eLoc = eLoc;
    }

    public String getmLoc() {
        return mLoc;
    }

    public void setmLoc(String mLoc) {
        this.mLoc = mLoc;
    }

    public List<DayDot> getDotList() {
        return dotList;
    }

    public void setDotList(List<DayDot> dotList) {
        this.dotList = dotList;
    }

    @Override
    public Object getTag() {
        return tag;
    }


    @Override
    public IDoctorItem withTag(Object tag) {
        this.tag = tag;
        return this;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public IDoctorItem withEnabled(boolean enabled) {
        this.isEnabled = enabled;
        return this;
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public IDoctorItem withSetSelected(boolean selected) {
        isSelected = selected;
        return this;
    }


    @Override
    public boolean isSelectable() {
        return isSelectable;
    }

    @Override
    public IDoctorItem withSelectable(boolean selectable) {
        this.isSelectable = selectable;
        return this;
    }

    @Override
    public String toString() {
        return "DVRDoctorsModel{" +
                "unique id='" + StringUtils.hashString64Bit(id) + '\'' +
                "id='" + id + '\'' +
                ", date='" + name + '\'' +
                '}';
    }


    @Override
    public int getType() {
        return R.id.rvDot;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_doctor_report;
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
        String eveningLoc = "E: ", morningLoc = "M: ";
        String nameId = name + " <span style=\"color:#FF9E02;\">" + " [" + id + "]" + "</span>";
        holder.name.setText(Html.fromHtml(nameId));
        String degreeSpecial = degree + " <span style=\"color:#01991f;\">" + special + "</span> ";
        holder.degreeSpecial.setText(Html.fromHtml(degreeSpecial));
        if (!TextUtils.isEmpty(eLoc)) {
            eveningLoc += eLoc;
        }
        if(!TextUtils.isEmpty(mLoc)){
            morningLoc += mLoc;
        }
        holder.txtEveningLoc.setText(eveningLoc);
        holder.txtMorningLoc.setText(morningLoc);

        if(dotList != null && dotList.size()>0) {
            final FastItemAdapter<IDotModel> fastAdapter = new FastItemAdapter<>();
            fastAdapter.add(getDot());
            fastAdapter.setHasStableIds(false);
            holder.rvDot.setLayoutManager(new LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false));
            holder.rvDot.setAdapter(fastAdapter);
        }
    }

    @Override
    public void unbindView(ViewHolder holder) {
        holder.name.setText(null);
        holder.degreeSpecial.setText(null);
        holder.txtEveningLoc.setText(null);
        holder.txtMorningLoc.setText(null);
    }

    @Override
    public boolean equals(int code) {
        return false;
    }

    @Override
    public IDoctorItem withIdentifier(long identifier) {
        return null;
    }

    @Override
    public long getIdentifier() {
        return 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        ATextView name, degreeSpecial, txtMorningLoc, txtEveningLoc;
        RecyclerView rvDot;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (ATextView) itemView.findViewById(R.id.name);
            degreeSpecial = (ATextView) itemView.findViewById(R.id.degreeSpecial);
            txtMorningLoc = (ATextView) itemView.findViewById(R.id.txtMorningLoc);
            txtEveningLoc = (ATextView) itemView.findViewById(R.id.txtEveningLoc);
            rvDot         = (RecyclerView) itemView.findViewById(R.id.rvDot);
        }
    }

    public List<IDotModel> getDot(){
        List<IDotModel> dotModelList = new ArrayList<>();
        for(DayDot dotExe:dotList){
            IDotModel iDotModel = new IDotModel();
            iDotModel.withName(String.valueOf(dotExe.getDay()), dotExe.getWeekDay(), dotExe.isNew(), dotExe.isAbsent());
            dotModelList.add(iDotModel);
        }

        return dotModelList;
    }
}
