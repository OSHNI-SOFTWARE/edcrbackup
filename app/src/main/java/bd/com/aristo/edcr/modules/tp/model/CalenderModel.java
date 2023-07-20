package bd.com.aristo.edcr.modules.tp.model;

import android.content.Context;
import android.graphics.Color;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.mikepenz.fastadapter.IDraggable;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.commons.utils.FastAdapterUIUtils;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialize.util.UIUtils;

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

public class CalenderModel extends AbstractItem<CalenderModel, CalenderModel.ViewHolder> implements IDraggable<CalenderModel, IItem> {

    public StringHolder date;
    public boolean isCurrent = false;
    public boolean isSynced = false;
    public boolean isSaved = false;
    public boolean isSent = false;
    public boolean isChanged = false;
    private StringHolder dayType;

    public boolean isSynced() {
        return isSynced;
    }

    public void setSynced(boolean synced) {
        isSynced = synced;
    }

    public boolean isSaved() {
        return isSaved;
    }

    public void setSaved(boolean saved) {
        isSaved = saved;
    }

    public boolean isSent() {
        return isSent;
    }

    public void setSent(boolean sent) {
        isSent = sent;
    }

    public boolean isChanged() {
        return isChanged;
    }

    public void setChanged(boolean changed) {
        isChanged = changed;
    }

    private boolean mIsDraggable = true;


    public CalenderModel withName(String Name, String dayType) {
        this.date = new StringHolder(Name);
        this.dayType = new StringHolder(dayType);
        return this;
    }

    public CalenderModel withName(String NameRes) {
        this.date = new StringHolder(NameRes);
        return this;
    }


    public CalenderModel withIsCurrent(boolean isCurrent){
        this.isCurrent = isCurrent;
        return this;
    }

    @Override
    public boolean isDraggable() {
        return mIsDraggable;
    }

    @Override
    public CalenderModel withIsDraggable(boolean draggable) {
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
        return R.id.rv;
    }

    /**
     * defines the layout which will be used for this item in the list
     *
     * @return the layout for this item
     */
    @Override
    public int getLayoutRes() {
        return R.layout.item_calendar;
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
        /*float toolbarHeight = ctx.getResources().getDimension(R.dimen.materialize_toolbar);
        float calenderHeight = TempData.CALENDER_WH.y - (3.5f * toolbarHeight);
        int calenderItemHeight = (int) Math.ceil(calenderHeight/5f) - 3;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(TempData.CALENDER_WH.x, calenderItemHeight);
        viewHolder.llDate.setLayoutParams(params);*/
        //get the context
        viewHolder.itemCalender.setMinimumHeight(SharedPrefsUtils.getIntegerPreference(ctx, StringConstants.PREF_CALENDAR_ITEM_H, 150));

        if(viewHolder.getAdapterPosition() % 2 == 0){
            viewHolder.itemCalender.setBackgroundColor(SystemUtils.getColorFromID(ctx, R.color.color6));
        } else {
            viewHolder.itemCalender.setBackgroundColor(SystemUtils.getColorFromID(ctx, R.color.white));
        }
        viewHolder.name.setTextColor(SystemUtils.getColorFromID(ctx, R.color.color2));


        if(isCurrent){
           viewHolder.name.setTextColor(SystemUtils.getColorFromID(ctx, R.color.color1));
        }

        if(isSynced()){
            viewHolder.tvCheck.setVisibility(View.VISIBLE);
            viewHolder.tvCheck.setBackgroundColor(SystemUtils.getColorFromID(ctx, R.color.colorPrimary));
        } else if(isSent()){
            viewHolder.tvCheck.setVisibility(View.VISIBLE);
            viewHolder.tvCheck.setBackgroundColor(SystemUtils.getColorFromID(ctx, R.color.color4));
        }else if(isSaved()){
            viewHolder.tvCheck.setVisibility(View.VISIBLE);
            viewHolder.tvCheck.setBackgroundColor(SystemUtils.getColorFromID(ctx, R.color.gray));
        } else if(isChanged()){
            viewHolder.tvCheck.setVisibility(View.VISIBLE);
            viewHolder.tvCheck.setBackgroundColor(SystemUtils.getColorFromID(ctx, R.color.md_red_800));
        } else {
            viewHolder.tvCheck.setVisibility(View.GONE);
        }

        UIUtils.setBackground(viewHolder.view, FastAdapterUIUtils.getSelectableBackground(ctx, Color.RED, true));

        StringHolder.applyTo(date, viewHolder.name);
    }

    @Override
    public void unbindView(ViewHolder holder) {
        super.unbindView(holder);
        holder.name.setText(null);
    }

    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    /**
     * our ViewHolder
     */
    protected static class ViewHolder extends RecyclerView.ViewHolder {
        protected View view;
        @BindView(R.id.date)
        ATextView name;
        @BindView(R.id.tvCheck)
        View tvCheck;
        @BindView(R.id.itemCalender)
        RelativeLayout itemCalender;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }
}
