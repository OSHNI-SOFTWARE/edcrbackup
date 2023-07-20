package bd.com.aristo.edcr.modules.dcr.dcr.model;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mikepenz.fastadapter.IItem;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.utils.ui.texts.ATextView;
import io.realm.annotations.Ignore;

/**
 * Created by monir.sobuj on 6/8/17.
 */

public class FrontPlaceModel implements IItem<FrontPlaceModel, FrontPlaceModel.ViewHolder> {

    private String name;

    private String shift;

    private boolean clicked = false;
    private boolean isCompleted = false;

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    private Context context;


    private Object tag;// defines if this item is isSelectable

    private boolean isSelectable = true;

    private boolean isEnabled = true;


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


    public static final String COL_CODE = "code", COL_SHIFT = "shift", COL_NAME = "name", COL_ID = "id";

    @Override
    public String toString() {
        return "FrontPlaceModel{" +
                ", name='" + name + '\'' +
                ", shift='" + shift + '\'' +
                '}';
    }

    @Override
    public Object getTag() {
        return tag;
    }

    @Override
    public FrontPlaceModel withTag(Object tag) {
        this.tag = tag;
        return this;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public FrontPlaceModel withEnabled(boolean enabled) {
        this.isEnabled = enabled;
        return this;
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public FrontPlaceModel withSetSelected(boolean selected) {
        isSelected = selected;
        return this;
    }


    @Override
    public boolean isSelectable() {
        return isSelectable;
    }

    @Override
    public FrontPlaceModel withSelectable(boolean selectable) {
        this.isSelectable = selectable;
        return this;
    }

    @Override
    public int getType() {
        return R.id.rvLocation;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_dcr_front_places;
    }

    @Override
    public View generateView(Context ctx) {
        context = ctx;
        ViewHolder viewHolder                           = getViewHolder(LayoutInflater.from(ctx).inflate(getLayoutRes(), null, false));
        bindView(viewHolder, Collections.EMPTY_LIST);
        return viewHolder.itemView;
    }

    @Override
    public View generateView(Context ctx, ViewGroup parent) {
        context = ctx;
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

        //holder.shift.setText(shift);
        holder.name.setText(name);
        /*if(!TextUtils.isEmpty(shift) && shift.equalsIgnoreCase(StringConstants.CAPITAL_MORNING)){
            holder.name.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.color2_transparent));
        } else {
            holder.name.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.color4_transparent));
        }
        *//*if(isCompleted){
            holder.itemView.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.color6));
        } else {
            holder.itemView.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.transparent));
        }*/

    }

    @Override
    public void unbindView(ViewHolder holder) {
        holder.name.setText(null);
    }

    @Override
    public boolean equals(int code) {
        return false;
    }

    @Override
    public FrontPlaceModel withIdentifier(long identifier) {
        return null;
    }

    @Override
    public long getIdentifier() {
        return new Random(5).nextInt();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        ATextView name;
        View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView                               = itemView;
            name                                        = (ATextView) itemView.findViewById(R.id.name);
        }
    }
}
