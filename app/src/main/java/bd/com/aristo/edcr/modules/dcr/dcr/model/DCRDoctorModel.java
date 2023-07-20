package bd.com.aristo.edcr.modules.dcr.dcr.model;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.modules.dvr.model.DayShift;
import bd.com.aristo.edcr.utils.StringUtils;
import bd.com.aristo.edcr.utils.SystemUtils;
import bd.com.aristo.edcr.utils.ui.texts.ATextView;

/**
 * Created by monir.sobuj on 6/8/17.
 */

public class DCRDoctorModel implements IItem<DCRDoctorModel, DCRDoctorModel.ViewHolder> {

    private String id;
    private String name;
    private String degree;
    private String contact;
    private String selected;
    private String sample;
    private String gift;
    private boolean isMorning;
    private int status;

    private List<DayShift> dotList;
    private List<DayShift> dotExeList;
    private List<DayShift> dotAdditionalList;

    private boolean clicked = false;


    private Object tag;// defines if this item is isSelectable
    private boolean isSelectable = true;
    private boolean isEnabled = true;

    private boolean isSelected = false; // defines if the item is selected

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public String getSample() {
        return sample;
    }

    public void setSample(String sample) {
        this.sample = sample;
    }

    public String getGift() {
        return gift;
    }

    public void setGift(String gift) {
        this.gift = gift;
    }

    public boolean isMorning() {
        return isMorning;
    }

    public void setMorning(boolean morning) {
        isMorning = morning;
    }

    public List<DayShift> getDotList() {
        return dotList;
    }

    public void setDotList(List<DayShift> dotList) {
        this.dotList = dotList;
    }

    public List<DayShift> getDotExeList() {
        return dotExeList;
    }

    public void setDotExeList(List<DayShift> dotExeList) {
        this.dotExeList = dotExeList;
    }

    public List<DayShift> getDotAdditionalList() {
        return dotAdditionalList;
    }

    public void setDotAdditionalList(List<DayShift> dotAdditionalList) {
        this.dotAdditionalList = dotAdditionalList;
    }

    @Override
    public Object getTag() {
        return tag;
    }


    @Override
    public DCRDoctorModel withTag(Object tag) {
        this.tag = tag;
        return this;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public DCRDoctorModel withEnabled(boolean enabled) {
        this.isEnabled = enabled;
        return this;
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public DCRDoctorModel withSetSelected(boolean selected) {
        isSelected = selected;
        return this;
    }


    @Override
    public boolean isSelectable() {
        return isSelectable;
    }

    @Override
    public DCRDoctorModel withSelectable(boolean selectable) {
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
        return R.id.rvDoctors;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_today_doctor;
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
//        SystemUtils.log("on bind view");
        holder.txtName.setText(name+" ["+id+"]");
        holder.txtDegree.setText(degree);
        holder.txtSelected.setText(selected);
        holder.txtSample.setText(sample);
        holder.txtGift.setText(gift);
        if(status == 1){
            holder.llDocInfo.setBackgroundColor(SystemUtils.getColorFromID(ctx, R.color.light_gray));
        }

        final FastItemAdapter<IDotModel> fastAdapter = new FastItemAdapter<>();
        fastAdapter.add(getDot());
        fastAdapter.setHasStableIds(false);
        holder.rvDot.setLayoutManager(new LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false));
        holder.rvDot.setAdapter(fastAdapter);

        //SystemUtils.log("DVRDoctorsModel -> bindView: " + toString());
    }

    @Override
    public void unbindView(ViewHolder holder) {
        holder.txtName.setText(null);
        holder.txtDegree.setText(null);
        holder.txtSelected.setText(null);
        holder.txtSample.setText(null);
        holder.txtGift.setText(null);
    }

    @Override
    public boolean equals(int code) {
        return false;
    }

    @Override
    public DCRDoctorModel withIdentifier(long identifier) {
        return null;
    }

    @Override
    public long getIdentifier() {
        return 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        ATextView txtName;
        ATextView txtDegree;
        ATextView txtSelected;
        ATextView txtSample;
        ATextView txtGift;
        LinearLayout llDocInfo;
        RecyclerView rvDot;

        public ViewHolder(View itemView) {
            super(itemView);

            txtName                                           = (ATextView) itemView.findViewById(R.id.name);
            txtDegree                                         = (ATextView) itemView.findViewById(R.id.degree);
            txtSelected                                       = (ATextView) itemView.findViewById(R.id.selected);
            txtSample                                         = (ATextView) itemView.findViewById(R.id.sample);
            txtGift                                           = (ATextView) itemView.findViewById(R.id.gift);
            llDocInfo                                         = (LinearLayout) itemView.findViewById(R.id.llDocInfo);
            rvDot                                     = (RecyclerView) itemView.findViewById(R.id.rvDot);
        }
    }

    public List<IDotModel> getDot(){
        List<IDotModel> dotModelList = new ArrayList<>();
        for(DayShift dot:dotList){
            IDotModel iDotModel = new IDotModel();
            iDotModel.withName(String.valueOf(dot.getDay()), dot.getWeekDay(),false, false, false);
            for(DayShift dotExe:dotExeList){
                if(dotExe.getDay() == dot.getDay()){
                    iDotModel.setExe(true);
                }
            }
            dotModelList.add(iDotModel);
        }

        for(DayShift dot:dotAdditionalList){
            IDotModel iDotModel = new IDotModel();
            iDotModel.withName(String.valueOf(dot.getDay()), dot.getWeekDay(),true, false, false);
            dotModelList.add(iDotModel);
        }

        return dotModelList;
    }
}
