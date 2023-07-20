package bd.com.aristo.edcr.modules.dcr.accompany;

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
import bd.com.aristo.edcr.utils.ui.texts.ATextView;

/**
 * Created by monir.sobuj on 6/8/17.
 */

public class AccompanyModel implements IItem<AccompanyModel, AccompanyModel.ViewHolder> {


    private String id;
    private String name;
    private String designation;

    private Object tag;
    private boolean isSelectable = true;
    private boolean isEnabled = true;
    private boolean isSelected = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }



    @Override
    public String toString() {
        return "IDoctorsModel{" +
                "unique id='" + StringUtils.hashString64Bit(id) + '\'' +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public Object getTag() {
        return tag;
    }

    @Override
    public AccompanyModel withTag(Object tag) {
        this.tag = tag;
        return this;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public AccompanyModel withEnabled(boolean enabled) {
        this.isEnabled = enabled;
        return this;
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public AccompanyModel withSetSelected(boolean selected) {
        isSelected = selected;
        return this;
    }


    @Override
    public boolean isSelectable() {
        return isSelectable;
    }

    @Override
    public AccompanyModel withSelectable(boolean selectable) {
        this.isSelectable = selectable;
        return this;
    }

    @Override
    public int getType() {
        return R.id.rvAccompany;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_accompany;
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
        holder.txtName.setText(name);
        //SystemUtils.log("IDoctorsModel -> bindView: " + toString());
    }

    @Override
    public void unbindView(ViewHolder holder) {
        holder.txtName.setText(null);
    }

    @Override
    public boolean equals(int code) {
        return false;
    }

    @Override
    public AccompanyModel withIdentifier(long identifier) {
        return null;
    }

    @Override
    public long getIdentifier() {
        return 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        ATextView txtName;
        ImageView ivMinus;

        public ViewHolder(View itemView) {
            super(itemView);
            txtName                                           = (ATextView) itemView.findViewById(R.id.name);
            ivMinus                                           = (ImageView) itemView.findViewById(R.id.ivMinus);
        }
    }
}
