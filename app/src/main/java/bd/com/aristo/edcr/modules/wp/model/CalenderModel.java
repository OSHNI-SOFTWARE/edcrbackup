package bd.com.aristo.edcr.modules.wp.model;

import android.content.Context;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

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

public class CalenderModel extends AbstractItem<CalenderModel, CalenderModel.ViewHolder> implements IDraggable<CalenderModel, IItem> {

    public String header;
    public StringHolder date;
    public StringHolder description;
    private int progress;
    public boolean isCurrent = false;
    public boolean isSynced = false;
    public boolean isSaved = false;
    public boolean isDvrSaved = false;
    private boolean mIsDraggable = true;

    public boolean isDvrSaved() {
        return isDvrSaved;
    }

    public void setDvrSaved(boolean dvrSaved) {
        isDvrSaved = dvrSaved;
    }

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


    public CalenderModel withHeader(String header) {
        this.header = header;
        return this;
    }

    public CalenderModel withName(String Name) {
        this.date = new StringHolder(Name);
        return this;
    }

    public CalenderModel withName(@StringRes int NameRes) {
        this.date = new StringHolder(NameRes);
        return this;
    }

    public CalenderModel withDescription(String description) {
        this.description = new StringHolder(description);
        return this;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public CalenderModel withDescription(@StringRes int descriptionRes) {
        this.description = new StringHolder(descriptionRes);
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
        return R.layout.item_calendar_wp;
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
        viewHolder.llDate.setMinimumHeight(SharedPrefsUtils.getIntegerPreference(ctx, StringConstants.PREF_CALENDAR_ITEM_H, 150));
        //get the context

        if(viewHolder.getAdapterPosition() % 2 == 0){
            viewHolder.llDate.setBackgroundColor(SystemUtils.getColorFromID(ctx, R.color.color6));
            viewHolder.name.setTextColor(SystemUtils.getColorFromID(ctx, R.color.color2));
        } else {
            viewHolder.llDate.setBackgroundColor(SystemUtils.getColorFromID(ctx, R.color.white));
            viewHolder.name.setTextColor(SystemUtils.getColorFromID(ctx, R.color.color2));
        }



       if(isCurrent){
           viewHolder.name.setTextColor(SystemUtils.getColorFromID(ctx, R.color.color1));
        }
        if(isDvrSaved()){
            if(isSynced()){
                viewHolder.progressBar.setProgress(100);
            } else if(isSaved()){
                viewHolder.progressBar.setProgress(progress);
            } else {
                viewHolder.progressBar.setProgress(0);
            }
        } else {
            viewHolder.progressBar.setVisibility(View.GONE);
        }

        //set the background for the item
        //UIUtils.setBackground(viewHolder.view, FastAdapterUIUtils.getSelectableBackground(ctx, Color.RED, true));

        //set the text for the date
        StringHolder.applyTo(date, viewHolder.name);
        //set the text for the description or hide
        //StringHolder.applyToOrHide(description, viewHolder.description);
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
        ATextView name;
        @BindView(R.id.llDate)
        RelativeLayout llDate;
        @BindView(R.id.progressBar)
        ProgressBar progressBar;
        /*@BindView(R.id.btxtToday)
        BTextView btxtToday;*/

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }
}
