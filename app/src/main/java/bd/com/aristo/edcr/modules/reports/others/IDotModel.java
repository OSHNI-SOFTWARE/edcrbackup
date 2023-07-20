package bd.com.aristo.edcr.modules.reports.others;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.mikepenz.fastadapter.IDraggable;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.mikepenz.materialdrawer.holder.StringHolder;

import java.util.List;

import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.utils.SystemUtils;
import bd.com.aristo.edcr.utils.ui.texts.ATextView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by monir.sobuj on 6/7/2017.
 */

public class IDotModel extends AbstractItem<IDotModel, IDotModel.ViewHolder> implements IDraggable<IDotModel, IItem> {

    public StringHolder date, weekDay;
    public boolean isAddition;
    public boolean isAbsent;


    private boolean mIsDraggable = true;


    public IDotModel withName(String NameRes, String weekDay, boolean isAddition, boolean isAbsent) {
        this.date = new StringHolder(NameRes);
        this.weekDay = new StringHolder(weekDay);
        this.isAddition = isAddition;

        this.isAbsent = isAbsent;
        return this;
    }

    public String getDate(){
        return date.toString();
    }

    @Override
    public boolean isDraggable() {
        return mIsDraggable;
    }

    @Override
    public IDotModel withIsDraggable(boolean draggable) {
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
        return R.layout.item_dot;
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
        if(TextUtils.isEmpty(date.toString())){
            viewHolder.name.setVisibility(View.GONE);
        } else {

            if(isAbsent){
                viewHolder.llDate.setBackgroundColor(SystemUtils.getColorFromID(ctx, R.color.red));
                viewHolder.name.setTextColor(SystemUtils.getColorFromID(ctx, R.color.white));
                viewHolder.weekDay.setTextColor(SystemUtils.getColorFromID(ctx, R.color.white));
            } else {
                viewHolder.llDate.setBackgroundColor(SystemUtils.getColorFromID(ctx, R.color.blue));
                viewHolder.name.setTextColor(SystemUtils.getColorFromID(ctx, R.color.white));
                viewHolder.weekDay.setTextColor(SystemUtils.getColorFromID(ctx, R.color.white));
            }

            if(isAddition){
                viewHolder.llDate.setBackgroundColor(SystemUtils.getColorFromID(ctx, R.color.light_gray));
                viewHolder.name.setTextColor(SystemUtils.getColorFromID(ctx, R.color.color4));
                viewHolder.weekDay.setTextColor(SystemUtils.getColorFromID(ctx, R.color.color4));
            }

            viewHolder.name.setVisibility(View.VISIBLE);
            StringHolder.applyTo(date, viewHolder.name);
            StringHolder.applyTo(weekDay, viewHolder.weekDay);
        }
    }

    @Override
    public void unbindView(ViewHolder holder) {
        super.unbindView(holder);
        holder.name.setText(null);
        holder.weekDay.setText(null);
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
        @BindView(R.id.weekDay)
        ATextView weekDay;
        @BindView(R.id.llDate)
        LinearLayout llDate;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }
}
