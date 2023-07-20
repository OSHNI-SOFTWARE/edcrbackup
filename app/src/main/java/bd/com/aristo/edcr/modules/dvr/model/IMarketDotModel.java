package bd.com.aristo.edcr.modules.dvr.model;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

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

public class IMarketDotModel extends AbstractItem<IMarketDotModel, IMarketDotModel.ViewHolder> implements IDraggable<IMarketDotModel, IItem> {


    MarketDotModel marketDotModel;

    private boolean mIsDraggable = true;

    public MarketDotModel getMarketDotModel() {
        return marketDotModel;
    }

    public void setMarketDotModel(MarketDotModel marketDotModel) {
        this.marketDotModel = marketDotModel;
    }

    @Override
    public boolean isDraggable() {
        return mIsDraggable;
    }

    @Override
    public IMarketDotModel withIsDraggable(boolean draggable) {
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
        return R.layout.item_market_dot;
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
        if(marketDotModel.getA().equalsIgnoreCase(".")){
            viewHolder.dotA.setTextColor(SystemUtils.getColorFromID(ctx, R.color.red));
        } else {
            viewHolder.dotA.setTextColor(SystemUtils.getColorFromID(ctx, R.color.color2));
        }
        if(marketDotModel.getB().equalsIgnoreCase(".")){
            viewHolder.dotB.setTextColor(SystemUtils.getColorFromID(ctx, R.color.red));
        } else {
            viewHolder.dotB.setTextColor(SystemUtils.getColorFromID(ctx, R.color.color2));
        }
        if(marketDotModel.getC().equalsIgnoreCase(".")){
            viewHolder.dotC.setTextColor(SystemUtils.getColorFromID(ctx, R.color.red));
        } else {
            viewHolder.dotC.setTextColor(SystemUtils.getColorFromID(ctx, R.color.color2));
        }
        if(marketDotModel.getD().equalsIgnoreCase(".")){
            viewHolder.dotD.setTextColor(SystemUtils.getColorFromID(ctx, R.color.red));
        } else {
            viewHolder.dotD.setTextColor(SystemUtils.getColorFromID(ctx, R.color.color2));
        }
        if(marketDotModel.getDay().equalsIgnoreCase("00")){
            viewHolder.day.setText("G");
        } else {
            viewHolder.day.setText(marketDotModel.getDay());
        }

        viewHolder.dotA.setText(marketDotModel.getA());
        //viewHolder.dotA.setBackgroundColor(SystemUtils.getColorFromID(ctx, R.color.light_gray));
        viewHolder.dotB.setText(marketDotModel.getB());
        viewHolder.dotC.setText(marketDotModel.getC());
        //viewHolder.dotC.setBackgroundColor(SystemUtils.getColorFromID(ctx, R.color.light_gray));
        viewHolder.dotD.setText(marketDotModel.getD());

    }

    @Override
    public void unbindView(ViewHolder holder) {
        super.unbindView(holder);
        holder.dotA.setText(null);
        holder.dotB.setText(null);
        holder.dotC.setText(null);
        holder.dotD.setText(null);
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
        @BindView(R.id.day)
        ATextView day;
        @BindView(R.id.dotA)
        ATextView dotA;
        @BindView(R.id.dotB)
        ATextView dotB;
        @BindView(R.id.dotC)
        ATextView dotC;
        @BindView(R.id.dotD)
        ATextView dotD;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }
}
