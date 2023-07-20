package bd.com.aristo.edcr.modules.reports.ss.model;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.StringRes;
import androidx.recyclerview.widget.RecyclerView;

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

public class GridModel extends AbstractItem<GridModel, GridModel.ViewHolder> {

    public StringHolder value;

    public GridModel withName(String Name) {
        this.value = new StringHolder(Name);
        return this;
    }

    public GridModel withName(@StringRes int NameRes) {
        this.value = new StringHolder(NameRes);
        return this;
    }


    /**
     * defines the type defining this item. must be unique. preferably an id
     *
     * @return the type
     */
    @Override
    public int getType() {
        return R.id.ssSummary;
    }

    /**
     * defines the layout which will be used for this item in the list
     *
     * @return the layout for this item
     */
    @Override
    public int getLayoutRes() {
        return R.layout.item_statement_summary_grid;
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
        viewHolder.gridItem.setMinimumHeight(SharedPrefsUtils.getIntegerPreference(ctx, StringConstants.PREF_SUMMARY_GRID_ITEM_H, 75));
        StringHolder.applyTo(value, viewHolder.txtValue);
        if(viewHolder.getAdapterPosition() < 7 || viewHolder.getAdapterPosition() % 7 == 0){
            viewHolder.txtValue.setTextColor(SystemUtils.getColorFromID(ctx, R.color.md_green_700));
        } else {
            viewHolder.txtValue.setTextColor(SystemUtils.getColorFromID(ctx, R.color.md_black_1000));
        }
    }

    @Override
    public void unbindView(ViewHolder holder) {
        super.unbindView(holder);
        holder.txtValue.setText(null);
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
        @BindView(R.id.txtValue)
        ATextView txtValue;
        @BindView(R.id.gridItem)
        RelativeLayout gridItem;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }
}
