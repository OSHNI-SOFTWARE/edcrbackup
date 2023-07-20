package bd.com.aristo.edcr.modules.reports.model;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.RelativeLayout;

import com.mikepenz.fastadapter.IDraggable;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.items.AbstractItem;

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

    public int date;
    public int status;


    private boolean mIsDraggable = true;


    public IDotModel withName(int date, int status) {
        this.date = date;
        this.status = status;
        return this;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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
        return R.layout.item_uncovered;
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
        viewHolder.date.setText(""+date);
        switch (status){
            case 0: //Only DOT
                viewHolder.date.setTextColor(SystemUtils.getColorFromID(ctx, R.color.md_blue_grey_600));
                viewHolder.status.setTextColor(SystemUtils.getColorFromID(ctx, R.color.md_blue_grey_600));
                viewHolder.status.setText("D");
                break;
            case 1: //DOT Execute
                viewHolder.date.setTextColor(SystemUtils.getColorFromID(ctx, R.color.md_green_700));
                viewHolder.status.setTextColor(SystemUtils.getColorFromID(ctx, R.color.md_green_700));
                viewHolder.status.setText("E");
                break;
            case 2: // Absent
                viewHolder.date.setTextColor(SystemUtils.getColorFromID(ctx, R.color.md_red_700));
                viewHolder.status.setTextColor(SystemUtils.getColorFromID(ctx, R.color.md_red_700));
                viewHolder.status.setText("A");
                break;
            case 3: // New
                viewHolder.date.setTextColor(SystemUtils.getColorFromID(ctx, R.color.md_orange_700));
                viewHolder.status.setTextColor(SystemUtils.getColorFromID(ctx, R.color.md_orange_700));
                viewHolder.status.setText("N");
                break;
            case 4:
                viewHolder.date.setTextColor(SystemUtils.getColorFromID(ctx, R.color.color4));
                viewHolder.status.setTextColor(SystemUtils.getColorFromID(ctx, R.color.color4));
                viewHolder.status.setText("D");
                break;
            default:
                break;
        }

    }

    @Override
    public void unbindView(ViewHolder holder) {
        super.unbindView(holder);
        holder.date.setText(null);
        holder.status.setText(null);
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
        ATextView date;
        @BindView(R.id.status)
        ATextView status;
        @BindView(R.id.rlDate)
        RelativeLayout rlDate;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }
}
