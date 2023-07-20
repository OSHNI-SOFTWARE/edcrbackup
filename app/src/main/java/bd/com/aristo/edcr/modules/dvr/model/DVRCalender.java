package bd.com.aristo.edcr.modules.dvr.model;

import android.content.Context;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.LinearLayout;

import com.mikepenz.fastadapter.IDraggable;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.mikepenz.materialdrawer.holder.StringHolder;

import java.util.List;

import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.utils.SharedPrefsUtils;
import bd.com.aristo.edcr.utils.SystemUtils;
import bd.com.aristo.edcr.utils.constants.StringConstants;
import bd.com.aristo.edcr.utils.ui.texts.ATextView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by monir.sobuj on 6/7/2017.
 */

public class DVRCalender extends AbstractItem<DVRCalender, DVRCalender.ViewHolder> implements IDraggable<DVRCalender, IItem> {

    public StringHolder date;
    private boolean isCurrent = false;
    public boolean isApproved = false;
    public boolean isSaved = false;
    private boolean isInit = false;
    public boolean isMorning = false;
    private boolean isEvening = false;
    private boolean isMEditable = false;
    private boolean isEEditable = false;
    private boolean isClickable = true;



    private boolean mIsDraggable = true;

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean synced) {
        isApproved = synced;
    }

    public boolean isSaved() {
        return isSaved;
    }

    public void setSaved(boolean saved) {
        isSaved = saved;
    }


    public boolean isInit() {
        return isInit;
    }

    public void setInit(boolean init) {
        isInit = init;
    }


    public DVRCalender withName(String Name) {
        this.date = new StringHolder(Name);
        return this;
    }

    public DVRCalender withName(@StringRes int NameRes) {
        this.date = new StringHolder(NameRes);
        return this;
    }

    public DVRCalender withIsCurrent(boolean isCurrent) {
        this.isCurrent = isCurrent;
        return this;
    }

    public boolean isMorning() {
        return isMorning;
    }

    public void setMorning(boolean morning) {
        isMorning = morning;
    }

    public boolean isEvening() {
        return isEvening;
    }

    public void setEvening(boolean evening) {
        isEvening = evening;
    }

    public boolean isMEditable() {
        return isMEditable;
    }

    public void setMEditable(boolean MEditable) {
        isMEditable = MEditable;
    }

    public boolean isEEditable() {
        return isEEditable;
    }

    public void setEEditable(boolean EEditable) {
        isEEditable = EEditable;
    }

    public boolean isClickable() {
        return isClickable;
    }

    public void setClickable(boolean clickable) {
        isClickable = clickable;
    }

    @Override
    public boolean isDraggable() {
        return mIsDraggable;
    }

    @Override
    public DVRCalender withIsDraggable(boolean draggable) {
        this.mIsDraggable = draggable;
        return this;
    }

    /**
     * defines the type defining this item. must be unique. preferably an id
     *
     * @return the type
     */
    @Override
    public int getType() {
        return R.id.rvCalender;
    }

    /**
     * defines the layout which will be used for this item in the list
     *
     * @return the layout for this item
     */
    @Override
    public int getLayoutRes() {
        return R.layout.item_dialog_calendar_dvr;
    }

    /**
     * binds the data of this item onto the viewHolder
     *
     * @param viewHolder the viewHolder of this item
     */
    @Override
    public void bindView(ViewHolder viewHolder, List<Object> payloads) {
        super.bindView(viewHolder, payloads);
        Context ctx = viewHolder.itemView.getContext();
        viewHolder.itemCalender.setMinimumHeight(SharedPrefsUtils.getIntegerPreference(ctx, StringConstants.PREF_CALENDAR_ITEM_H, 150));
        if(viewHolder.getAdapterPosition() % 2 == 0){
            viewHolder.itemCalender.setBackgroundColor(SystemUtils.getColorFromID(ctx, R.color.color6));
            viewHolder.date.setTextColor(SystemUtils.getColorFromID(ctx, R.color.color2));
        } else {
            viewHolder.itemCalender.setBackgroundColor(SystemUtils.getColorFromID(ctx, R.color.white));
            viewHolder.date.setTextColor(SystemUtils.getColorFromID(ctx, R.color.color2));
        }
        if (isCurrent) {
            viewHolder.date.setTextColor(SystemUtils.getColorFromID(ctx, R.color.color1));
        }
        StringHolder.applyTo(date, viewHolder.date);

        if(isEvening){
            if(isClickable){
                viewHolder.txtEvening.setBackgroundColor(SystemUtils.getColorFromID(ctx, R.color.color4));
                viewHolder.txtEvening.setTextColor(SystemUtils.getColorFromID(ctx, R.color.white));
            } else {
                viewHolder.txtEvening.setBackgroundColor(SystemUtils.getColorFromID(ctx, R.color.colorPrimaryDark));
                viewHolder.txtEvening.setTextColor(SystemUtils.getColorFromID(ctx, R.color.white));
            }

        } else {
            if(!isClickable){
                viewHolder.txtEvening.setBackgroundColor(SystemUtils.getColorFromID(ctx, R.color.light_gray));
            }
        }
        if(isMorning) {
            if(isClickable){
                viewHolder.txtMorning.setBackgroundColor(SystemUtils.getColorFromID(ctx, R.color.color4));
                viewHolder.txtMorning.setTextColor(SystemUtils.getColorFromID(ctx, R.color.white));
            } else {
                viewHolder.txtMorning.setBackgroundColor(SystemUtils.getColorFromID(ctx, R.color.colorPrimaryDark));
                viewHolder.txtMorning.setTextColor(SystemUtils.getColorFromID(ctx, R.color.white));
            }

        } else {
            if(!isClickable){
                viewHolder.txtMorning.setBackgroundColor(SystemUtils.getColorFromID(ctx, R.color.light_gray));
            }
        }


        viewHolder.itemCalender.setVisibility(View.VISIBLE);
        if(!isEEditable && !isMEditable){
            viewHolder.itemCalender.setVisibility(View.GONE);
        } else if(!isMEditable){
            viewHolder.itemCalender.setVisibility(View.VISIBLE);
            viewHolder.txtMorning.setVisibility(View.INVISIBLE);
            viewHolder.txtEvening.setVisibility(View.VISIBLE);
        } else if(!isEEditable){
            viewHolder.itemCalender.setVisibility(View.VISIBLE);
            viewHolder.txtEvening.setVisibility(View.INVISIBLE);
            viewHolder.txtMorning.setVisibility(View.VISIBLE);
        } else {
            viewHolder.itemCalender.setVisibility(View.VISIBLE);
            viewHolder.txtEvening.setVisibility(View.VISIBLE);
            viewHolder.txtMorning.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void unbindView(ViewHolder holder) {
        super.unbindView(holder);
        holder.date.setText(null);
    }

    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    /**
     * our ViewHolder
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected View view;
        @BindView(R.id.date)
        ATextView date;
        @BindView(R.id.txtMorning)
        public ATextView txtMorning;
        @BindView(R.id.txtEvening)
        public ATextView txtEvening;
        @BindView(R.id.itemCalender)
        LinearLayout itemCalender;


        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }
}
