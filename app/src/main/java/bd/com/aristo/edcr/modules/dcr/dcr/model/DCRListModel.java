package bd.com.aristo.edcr.modules.dcr.dcr.model;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;

import java.util.Collections;
import java.util.List;

import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.utils.constants.StringConstants;
import bd.com.aristo.edcr.utils.ui.texts.ATextView;

/**
 * Created by monir.sobuj on 6/8/17.
 */

public class DCRListModel implements IItem<DCRListModel, DCRListModel.ViewHolder> {


    @SerializedName("DoctorName")
    @Expose
    private String docName;
    @SerializedName("DoctorID")
    @Expose
    private String docId;
    @SerializedName("Status")
    @Expose
    private String absentStatus;
    @SerializedName("StatusCause")
    @Expose
    private String absentCause;
    @SerializedName("SubDetailList")
    @Expose
    private List<DCRDetailModel> dcrDetailModelList;

    private boolean clicked = false;

    private Object tag;
    private boolean isSelectable = true;
    private boolean isEnabled = true;
    //variables no need to store
    //variables needed for adapter
    private boolean isSelected = false; // defines if the item is selected

    public boolean isClicked() {
        return clicked;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }
    @Override
    public Object getTag() {
        return tag;
    }


    @Override
    public DCRListModel withTag(Object tag) {
        this.tag = tag;
        return this;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public DCRListModel withEnabled(boolean enabled) {
        this.isEnabled = enabled;
        return this;
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public DCRListModel withSetSelected(boolean selected) {
        isSelected = selected;
        return this;
    }


    @Override
    public boolean isSelectable() {
        return isSelectable;
    }

    @Override
    public DCRListModel withSelectable(boolean selectable) {
        this.isSelectable = selectable;
        return this;
    }

    @Override
    public int getType() {
        return R.id.rvDoctors;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_dcr_detail_row;
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

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getAbsentStatus() {
        return absentStatus;
    }

    public void setAbsentStatus(String absentStatus) {
        this.absentStatus = absentStatus;
    }

    public String getAbsentCause() {
        return absentCause;
    }

    public void setAbsentCause(String absentCause) {
        this.absentCause = absentCause;
    }

    public List<DCRDetailModel> getDcrDetailModelList() {
        return dcrDetailModelList;
    }

    public void setDcrDetailModelList(List<DCRDetailModel> dcrDetailModelList) {
        this.dcrDetailModelList = dcrDetailModelList;
    }

    @Override
    public void bindView(ViewHolder holder, List<Object> payloads) {
        Context ctx = holder.itemView.getContext();

        holder.txtName.setText(docName+" ["+docId+"]");
        if(TextUtils.isEmpty(absentStatus)){
            holder.txtAbsentCause.setVisibility(View.GONE);
            holder.rvProductDetail.setVisibility(View.VISIBLE);
            final FastItemAdapter<DCRDetailModel> fastAdapter = new FastItemAdapter<>();
            fastAdapter.add(getDcrDetailModelList());
            fastAdapter.setHasStableIds(false);
            holder.rvProductDetail.setLayoutManager(new LinearLayoutManager(ctx));
            holder.rvProductDetail.setAdapter(fastAdapter);
        } else {
            if(absentStatus.equalsIgnoreCase(StringConstants.STATUS_PRESENT)){
                holder.txtAbsentCause.setVisibility(View.GONE);
                holder.rvProductDetail.setVisibility(View.VISIBLE);
                final FastItemAdapter<DCRDetailModel> fastAdapter = new FastItemAdapter<>();
                fastAdapter.add(getDcrDetailModelList());
                fastAdapter.setHasStableIds(false);
                holder.rvProductDetail.setLayoutManager(new LinearLayoutManager(ctx));
                holder.rvProductDetail.setAdapter(fastAdapter);
            } else {
                holder.rvProductDetail.setVisibility(View.GONE);
                holder.txtAbsentCause.setVisibility(View.VISIBLE);
                holder.txtAbsentCause.setText(absentCause);
            }
        }
    }

    @Override
    public void unbindView(ViewHolder holder) {
        holder.txtName.setText(null);
        holder.txtAbsentCause.setText(null);
        holder.rvProductDetail.setAdapter(null);
    }

    @Override
    public boolean equals(int code) {
        return false;
    }

    @Override
    public DCRListModel withIdentifier(long identifier) {
        return null;
    }

    @Override
    public long getIdentifier() {
        return 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        ATextView txtName;
        ATextView txtAbsentCause;
        RecyclerView rvProductDetail;

        public ViewHolder(View itemView) {
            super(itemView);

            txtName                                                = (ATextView) itemView.findViewById(R.id.name);
            txtAbsentCause                                         = (ATextView) itemView.findViewById(R.id.absentCause);
            rvProductDetail                                        = (RecyclerView) itemView.findViewById(R.id.rvProductDetail);
        }
    }
}
