package bd.com.aristo.edcr.modules.dvr.model;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.annotations.SerializedName;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.utils.StringUtils;
import bd.com.aristo.edcr.utils.ui.texts.ATextView;

/**
 * Created by monir.sobuj on 01/11/17.
 */

public class IMarketDoctorsModel implements IItem<IMarketDoctorsModel, IMarketDoctorsModel.ViewHolder> {

    @SerializedName("Doctor_Id")
    private String id;
    @SerializedName("Doctor_Name")
    private String name;
    @SerializedName("A")
    private String gA;
    @SerializedName("B")
    private String gB;
    @SerializedName("C")
    private String gC;
    @SerializedName("D")
    private String gD;
    @SerializedName("List")
    private List<MarketDotModel> marketDotModels;


    private boolean isSelected = false; // defines if the item is selected

    private Object tag;// defines if this item is isSelectable

    private boolean isSelectable = true;

    private boolean isEnabled = true;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getgA() {
        return gA;
    }

    public void setgA(String gA) {
        this.gA = gA;
    }

    public String getgB() {
        return gB;
    }

    public void setgB(String gB) {
        this.gB = gB;
    }

    public String getgC() {
        return gC;
    }

    public void setgC(String gC) {
        this.gC = gC;
    }

    public String getgD() {
        return gD;
    }

    public void setgD(String gD) {
        this.gD = gD;
    }

    public List<MarketDotModel> getMarketDotModels() {
        return marketDotModels;
    }

    public void setMarketDotModels(List<MarketDotModel> marketDotModels) {
        this.marketDotModels = marketDotModels;
    }

    @Override
    public Object getTag() {
        return tag;
    }

    @Override
    public IMarketDoctorsModel withTag(Object tag) {
        this.tag = tag;
        return this;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public IMarketDoctorsModel withEnabled(boolean enabled) {
        this.isEnabled = enabled;
        return this;
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public IMarketDoctorsModel withSetSelected(boolean selected) {
        this.isSelected = selected;
        return this;
    }

    @Override
    public boolean isSelectable() {
        return isSelectable;
    }

    @Override
    public IMarketDoctorsModel withSelectable(boolean selectable) {
        this.isSelectable = selectable;
        return this;
    }



    @Override
    public int getType() {
        return R.id.gwdsList;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_market_doctor;
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
//        SystemUtils.log("on bind view");
        Context ctx = holder.itemView.getContext();
        holder.name.setText(name + " [" + id + "]");
        holder.A.setText(getgA());
        holder.B.setText(getgB());
        holder.C.setText(getgC());
        holder.D.setText(getgD());

        final FastItemAdapter<IMarketDotModel> fastAdapter = new FastItemAdapter<>();
        fastAdapter.add(getDot());
        fastAdapter.setHasStableIds(true);
        holder.rvDot.setLayoutManager(new LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false));
        holder.rvDot.setAdapter(fastAdapter);
    }

    @Override
    public void unbindView(ViewHolder holder) {
        holder.name.setText(null);
        holder.A.setText(null);
        holder.B.setText(null);
        holder.C.setText(null);
        holder.D.setText(null);
    }

    @Override
    public boolean equals(int id) {
        return false;
    }

    @Override
    public IMarketDoctorsModel withIdentifier(long identifier) {
        return null;
    }

    @Override
    public long getIdentifier() {
        return StringUtils.hashString64Bit(id);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        ATextView name, A, B, C, D;
        RecyclerView rvDot;

        public ViewHolder(View itemView) {
            super(itemView);

            name                                        = (ATextView) itemView.findViewById(R.id.name);
            A                                           = (ATextView) itemView.findViewById(R.id.dotCountA);
            B                                           = (ATextView) itemView.findViewById(R.id.dotCountB);
            C                                           = (ATextView) itemView.findViewById(R.id.dotCountC);
            D                                           = (ATextView) itemView.findViewById(R.id.dotCountD);
            rvDot                                       = (RecyclerView) itemView.findViewById(R.id.rvDot);

        }
    }

    public List<IMarketDotModel> getDot(){
        List<IMarketDotModel> dotModelList = new ArrayList<>();
        for(MarketDotModel dot:marketDotModels){
            IMarketDotModel iDotModel = new IMarketDotModel();
            iDotModel.setMarketDotModel(dot);
            dotModelList.add(iDotModel);
        }
        return dotModelList;
    }

}
