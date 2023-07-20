package bd.com.aristo.edcr.modules.reports.others;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mikepenz.fastadapter.IItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.modules.reports.model.IDotModel;
import bd.com.aristo.edcr.modules.reports.model.UncoverDot;
import bd.com.aristo.edcr.utils.StringUtils;
import bd.com.aristo.edcr.utils.ui.texts.ATextView;

/**
 * Created by monir.sobuj on 01/11/17.
 */

public class IUncoverModel implements IItem<IUncoverModel, IUncoverModel.ViewHolder> {

    private String id;

    private String name, degree, special, shift, dotCount, address;
    private boolean isSaved, isSynced;
    private boolean clicked = false;
    private String dvrId;
    private int pos;
    private List<UncoverDot> uncoverList;

    public List<UncoverDot> getUncoverList() {
        return uncoverList;
    }

    public void setUncoverList(List<UncoverDot> uncoverList) {
        this.uncoverList = uncoverList;
    }

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
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

    public String getDvrId() {
        return dvrId;
    }

    public void setDvrId(String dvrId) {
        this.dvrId = dvrId;
    }

    public String getDotCount() {
        return dotCount;
    }

    public void setDotCount(String dotCount) {
        this.dotCount = dotCount;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    @Override
    public String toString() {
        return "IDoctorsModel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", degree='" + degree + '\'' +
                ", special='" + special + '\'' +
                ", shift='" + shift + '\'' +
                ", dotCount='" + dotCount + '\'' +
                ", isSaved=" + isSaved +
                ", isSynced=" + isSynced +
                '}';
    }

    @Override
    public Object getTag() {
        return tag;
    }

    @Override
    public IUncoverModel withTag(Object tag) {
        this.tag = tag;
        return this;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public IUncoverModel withEnabled(boolean enabled) {
        this.isEnabled = enabled;
        return this;
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public IUncoverModel withSetSelected(boolean selected) {
        this.isSelected = selected;
        return this;
    }

    @Override
    public boolean isSelectable() {
        return isSelectable;
    }

    @Override
    public IUncoverModel withSelectable(boolean selectable) {
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
//        SystemUtils.log("on bind view");
        Context ctx = holder.itemView.getContext();
        holder.name.setText(name + " [" + id + "]");
        String degreeSpecial = degree+" <span style=\"color:#01991f;\">"+special+"</span> ";
        holder.degreeSpecial.setText(Html.fromHtml(degreeSpecial));
        if (!TextUtils.isEmpty(address)){
            holder.address.setVisibility(View.VISIBLE);
            holder.address.setText(address);
        }else{
            holder.address.setVisibility(View.GONE);
        }
    }

    @Override
    public void unbindView(ViewHolder holder) {
        holder.name.setText(null);
        holder.degreeSpecial.setText(null);
        holder.address.setText(null);
    }

    @Override
    public boolean equals(int id) {
        return false;
    }

    @Override
    public IUncoverModel withIdentifier(long identifier) {
        return null;
    }

    @Override
    public long getIdentifier() {
        return StringUtils.hashString64Bit(id);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        ATextView name, degreeSpecial, address;

        public ViewHolder(View itemView) {
            super(itemView);

            name                                        = (ATextView) itemView.findViewById(R.id.name);
            degreeSpecial                                      = (ATextView) itemView.findViewById(R.id.degreeSpecial);
            address                                     = (ATextView) itemView.findViewById(R.id.address);

        }
    }

    public List<IDotModel> getDot(){
        List<IDotModel> dotModelList = new ArrayList<>();
        for(UncoverDot dot:uncoverList){
            IDotModel iDotModel = new IDotModel();
            iDotModel.withName(dot.getDay(), dot.getStatus());
            dotModelList.add(iDotModel);
        }
        return dotModelList;
    }

}
