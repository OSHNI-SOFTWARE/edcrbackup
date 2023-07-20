package bd.com.aristo.edcr.modules.reports.others;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
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

public class IDoctorsModel implements IItem<IDoctorsModel, IDoctorsModel.ViewHolder> {

    private String id;

    private String name, degree, special, eLoc, mLoc;
    private boolean clicked = false;



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


    public boolean isClicked() {
        return clicked;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
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
        return R.layout.item_doctor_report;
    }

    @Override
    public View generateView(Context ctx) {
        IDoctorsModel.ViewHolder viewHolder = getViewHolder(LayoutInflater.from(ctx).inflate(getLayoutRes(), null, false));
        bindView(viewHolder, Collections.EMPTY_LIST);
        return viewHolder.itemView;
    }

    @Override
    public View generateView(Context ctx, ViewGroup parent) {
        IDoctorsModel.ViewHolder viewHolder = getViewHolder(LayoutInflater.from(ctx).inflate(getLayoutRes(), parent, false));
        bindView(viewHolder, Collections.EMPTY_LIST);
        return viewHolder.itemView;
    }

    private IDoctorsModel.ViewHolder getViewHolder(View view) {
        return new IDoctorsModel.ViewHolder(view);
    }

    @Override
    public IDoctorsModel.ViewHolder getViewHolder(ViewGroup parent) {
        return getViewHolder(LayoutInflater.from(parent.getContext()).inflate(getLayoutRes(), parent, false));
    }

    @Override
    public void bindView(IDoctorsModel.ViewHolder holder, List<Object> payloads) {
        String eveningLoc = "E: ", morningLoc = "M: ";
        holder.name.setText(name + " [" + id + "]");
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
    }

    @Override
    public void unbindView(IDoctorsModel.ViewHolder holder) {
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
        return null;
    }

    @Override
    public long getIdentifier() {
        return StringUtils.hashString64Bit(id);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ATextView name, degreeSpecial, txtMorningLoc, txtEveningLoc;

        public ViewHolder(View itemView) {
            super(itemView);

            name = (ATextView) itemView.findViewById(R.id.name);
            degreeSpecial = (ATextView) itemView.findViewById(R.id.degreeSpecial);
            txtMorningLoc = (ATextView) itemView.findViewById(R.id.txtMorningLoc);
            txtEveningLoc = (ATextView) itemView.findViewById(R.id.txtEveningLoc);

        }
    }
}