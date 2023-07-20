package bd.com.aristo.edcr.modules.wp.model;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mikepenz.fastadapter.IItem;

import java.util.Collections;
import java.util.List;

import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.utils.StringUtils;
import bd.com.aristo.edcr.utils.SystemUtils;
import bd.com.aristo.edcr.utils.constants.StringConstants;
import bd.com.aristo.edcr.utils.ui.texts.ATextView;
import io.realm.annotations.Ignore;

/**
 * Created by monir.sobuj on 01/11/17.
 */

public class WPDoctorsModel implements IItem<WPDoctorsModel, WPDoctorsModel.ViewHolder> {

    private String id;

    private String name;
    private String ins;
    private String insName;

    private String special;
    private boolean isSaved;
    private boolean isSynced;
    private boolean isMorning;

    //variables needed for adapter
    @Ignore
    private boolean isSelected = false; // defines if the item is selected
    @Ignore
    private Object tag;// defines if this item is isSelectable
    @Ignore
    private boolean isSelectable = true;
    @Ignore
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

    public String getIns() {
        return ins;
    }

    public void setIns(String ins) {
        this.ins = ins;
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

    public boolean isMorning() {
        return isMorning;
    }

    public void setMorning(boolean morning) {
        isMorning = morning;
    }


    public String getInsName() {
        return insName;
    }

    public void setInsName(String insName) {
        this.insName = insName;
    }

    @Override
    public String toString() {
        return "WPDoctorModel{" +
                "unique id='" + StringUtils.hashString64Bit(id) + '\'' +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", ins='" + ins + '\'' +
                ", ins Name='" + insName + '\'' +
                ", speciality='" + special + '\'' +
                '}';
    }

    @Override
    public Object getTag() {
        return tag;
    }

    @Override
    public WPDoctorsModel withTag(Object tag) {
        this.tag = tag;
        return this;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public WPDoctorsModel withEnabled(boolean enabled) {
        this.isEnabled = enabled;
        return this;
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public WPDoctorsModel withSetSelected(boolean selected) {
        this.isSelected = selected;
        return this;
    }

    @Override
    public boolean isSelectable() {
        return isSelectable;
    }

    @Override
    public WPDoctorsModel withSelectable(boolean selectable) {
        this.isSelectable = selectable;
        return this;
    }



    @Override
    public int getType() {
        return R.id.gwdsList;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_wp_doctor;
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

        holder.name.setText(name+" ["+id+"]");
        holder.shift.setText(isMorning? StringConstants.CAPITAL_MORNING : StringConstants.CAPITAL_EVENING);
        holder.ins.setText(insName);
        holder.special.setText(special);
        if(isSaved){
            tickOn(holder, holder.itemView.getContext());
        } else
            checkOn(holder);
    }

    @Override
    public void unbindView(ViewHolder holder) {
        holder.name.setText(null);
        holder.shift.setText(null);
        holder.ins.setText(null);
        holder.special.setText(null);
        holder.ivCheck.setImageDrawable(null);
        holder.ivTick.setImageDrawable(null);
    }

    @Override
    public boolean equals(int id) {
        return false;
    }

    @Override
    public WPDoctorsModel withIdentifier(long identifier) {
        return null;
    }

    @Override
    public long getIdentifier() {
        return StringUtils.hashString64Bit(id);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        ATextView name, ins, special, shift;
        ImageView ivCheck, ivTick;

        public ViewHolder(View itemView) {
            super(itemView);

            name                                        = (ATextView) itemView.findViewById(R.id.name);
            shift                                       = (ATextView) itemView.findViewById(R.id.shift);
            ins                                         = (ATextView) itemView.findViewById(R.id.ins);
            special                                     = (ATextView) itemView.findViewById(R.id.special);
            ivCheck                                     = (ImageView) itemView.findViewById(R.id.ivCheck);
            ivTick                                   = (ImageView) itemView.findViewById(R.id.ivTick);
        }
    }

    private void checkOn(WPDoctorsModel.ViewHolder holder){
        holder.ivCheck.setVisibility(View.VISIBLE);
        holder.ivTick.setVisibility(View.GONE);
    }

    private void tickOn(WPDoctorsModel.ViewHolder holder, Context ctx){
        holder.ivCheck.setVisibility(View.GONE);
        holder.ivTick.setVisibility(View.VISIBLE);
        if(isSynced){
            holder.ivTick.setBackgroundColor(SystemUtils.getColorFromID(ctx, R.color.color2));
        }
    }
}
