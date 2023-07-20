package bd.com.aristo.edcr.modules.dvr.model;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mikepenz.fastadapter.IDraggable;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.commons.utils.FastAdapterUIUtils;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialize.util.UIUtils;

import java.util.List;

import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.utils.SystemUtils;
import bd.com.aristo.edcr.utils.TempData;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by monir.sobuj on 6/7/2017.
 */

public class CalenderItem extends AbstractItem<CalenderItem, CalenderItem.ViewHolder> implements IDraggable<CalenderItem, IItem> {

    public StringHolder date;
    public StringHolder info;
    public boolean isCurrent = false;
    public boolean isSaved = false;

    public boolean isSaved() {
        return isSaved;
    }

    public void setSaved(boolean saved) {
        isSaved = saved;
    }

    private boolean mIsDraggable = true;

    public CalenderItem withName(String NameRes) {
        this.date = new StringHolder(NameRes);
        return this;
    }

    public CalenderItem withNameAndInfo(String NameRes, String info) {
        this.date = new StringHolder(NameRes);
        this.info = new StringHolder(info);
        return this;
    }


    public CalenderItem withIsCurrent(boolean isCurrent){
        this.isCurrent = isCurrent;
        return this;
    }

    @Override
    public boolean isDraggable() {
        return mIsDraggable;
    }

    @Override
    public CalenderItem withIsDraggable(boolean draggable) {
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
        return R.id.rvCalendar;
    }

    /**
     * defines the layout which will be used for this item in the list
     *
     * @return the layout for this item
     */
    @Override
    public int getLayoutRes() {
        return R.layout.item_calendar_dvr_sum;
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
        float toolbarHeight = ctx.getResources().getDimension(R.dimen.materialize_toolbar);
        float calenderHeight = TempData.CALENDER_WH.y - (3 * toolbarHeight);
        int calenderItemHeight = (int) Math.ceil(calenderHeight/5f) - 3;
        viewHolder.llDate.setMinimumHeight(calenderItemHeight/2);
        if(viewHolder.getAdapterPosition() % 2 == 0){
            viewHolder.llDate.setBackgroundColor(SystemUtils.getColorFromID(ctx, R.color.color6));

        } else {
            viewHolder.llDate.setBackgroundColor(SystemUtils.getColorFromID(ctx, R.color.white));
        }

       if(isCurrent){
           viewHolder.name.setTextColor(SystemUtils.getColorFromID(ctx, R.color.color1));
        }
        UIUtils.setBackground(viewHolder.view, FastAdapterUIUtils.getSelectableBackground(ctx, Color.RED, true));
        StringHolder.applyTo(date, viewHolder.name);
        StringHolder.applyTo(info, viewHolder.info);
    }

    @Override
    public void unbindView(ViewHolder holder) {
        super.unbindView(holder);
        holder.name.setText(null);
        // holder.description.setText(null);
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
        TextView name;
        @BindView(R.id.llDate)
        RelativeLayout llDate;
        @BindView(R.id.info)
        TextView info;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }
}
