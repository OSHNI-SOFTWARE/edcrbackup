package bd.com.aristo.edcr.modules.tp.model;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mikepenz.fastadapter.IItem;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.utils.StringUtils;
import bd.com.aristo.edcr.utils.ui.texts.ATextView;
import io.realm.annotations.Ignore;

/**
 * Created by monir.sobuj on 6/8/17.
 */

public class ITPPlacesModel implements IItem<ITPPlacesModel, ITPPlacesModel.ViewHolder>, Serializable {

    private long id;

    private String code;

    private String name;

    private String shift;

    private boolean clicked = false;


    private Object tag;// defines if this item is isSelectable

    private boolean isSelectable = true;

    private boolean isEnabled = true;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Ignore
    private boolean isSelected = false; // defines if the item is selected

    public boolean isClicked() {
        return clicked;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }
    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public static final String COL_CODE = "code", COL_SHIFT = "shift", COL_NAME = "name", COL_ID = "id";

    @Override
    public String toString() {
        return "DVRDoctorsModel{" +
                "unique id='" + StringUtils.hashString64Bit(code) + '\'' +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", shift='" + shift + '\'' +
                '}';
    }

    @Override
    public Object getTag() {
        return tag;
    }

    @Override
    public ITPPlacesModel withTag(Object tag) {
        this.tag = tag;
        return this;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public ITPPlacesModel withEnabled(boolean enabled) {
        this.isEnabled = enabled;
        return this;
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public ITPPlacesModel withSetSelected(boolean selected) {
        isSelected = selected;
        return this;
    }


    @Override
    public boolean isSelectable() {
        return isSelectable;
    }

    @Override
    public ITPPlacesModel withSelectable(boolean selectable) {
        this.isSelectable = selectable;
        return this;
    }

    @Override
    public int getType() {
        return R.id.rvWorkPlace;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_work_place;
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

        holder.ivTick.setVisibility(View.GONE);
        holder.name.setText(name);



        //SystemUtils.log("DVRDoctorsModel -> bindView: " + toString());
    }

    @Override
    public void unbindView(ViewHolder holder) {
        holder.name.setText(null);
        holder.ivTick.setImageDrawable(null);
    }

    @Override
    public boolean equals(int code) {
        return false;
    }

    @Override
    public ITPPlacesModel withIdentifier(long identifier) {
        return null;
    }

    @Override
    public long getIdentifier() {
        return StringUtils.hashString64Bit(code);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        ATextView name;
        ImageView ivTick;

        public ViewHolder(View itemView) {
            super(itemView);

            name                                        = (ATextView) itemView.findViewById(R.id.name);
            ivTick                                        = (ImageView) itemView.findViewById(R.id.ivTick);
        }
    }
}
