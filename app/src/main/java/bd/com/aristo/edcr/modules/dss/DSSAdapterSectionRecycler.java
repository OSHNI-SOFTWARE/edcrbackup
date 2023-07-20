package bd.com.aristo.edcr.modules.dss;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.intrusoft.sectionedrecyclerview.SectionRecyclerViewAdapter;

import java.util.List;

import bd.com.aristo.edcr.R;

/**
 * Created by altaf.sil on 1/23/18.
 */

public class DSSAdapterSectionRecycler extends SectionRecyclerViewAdapter<SectionHeader, DSSModel, SectionViewHolder, DSSViewHolder> {

    Context context;

    public DSSAdapterSectionRecycler(Context context, List<SectionHeader> sectionItemList) {
        super(context, sectionItemList);
        this.context = context;
    }

    @Override
    public SectionViewHolder onCreateSectionViewHolder(ViewGroup sectionViewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.section_header_row, sectionViewGroup, false);
        return new SectionViewHolder(view);
    }

    @Override
    public DSSViewHolder onCreateChildViewHolder(ViewGroup childViewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_dss, childViewGroup, false);
        return new DSSViewHolder(view);
    }

    @Override
    public void onBindSectionViewHolder(SectionViewHolder sectionViewHolder, int sectionPosition, SectionHeader section) {
        sectionViewHolder.name.setText(section.sectionText);
    }

    @Override
    public void onBindChildViewHolder(DSSViewHolder childViewHolder, int sectionPosition, int childPosition, final DSSModel dssModel) {
        if(dssModel.getPackSize() != null){
            childViewHolder.name.setText(dssModel.getName()); //+"("+dssModel.getPackSize()+")"
        } else {
            childViewHolder.name.setText(dssModel.getName());
        }

        childViewHolder.tvCount.setText(""+dssModel.getCount());
        childViewHolder.itemView.setSelected(dssModel.isChecked());

        if(dssModel.isChecked()){
            tickOn(childViewHolder);
        } else {
            tickOff(childViewHolder);
        }

        if(dssModel.isIntern()){
            childViewHolder.txtItemFor.setText("INTERN");
        } else {
            childViewHolder.txtItemFor.setText("REGULAR");
        }

        childViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dssModel.isChecked()){
                    dssModel.setChecked(false);
                }else{
                    dssModel.setChecked(true);
                }
                notifyDataSetChanged();
            }
        });

    }

    private void tickOff(DSSViewHolder holder){
        holder.ivCommon.setVisibility(View.VISIBLE);
        holder.ivTick.setVisibility(View.GONE);
    }

    private void tickOn(DSSViewHolder holder){
        holder.ivCommon.setVisibility(View.GONE);
        holder.ivTick.setVisibility(View.VISIBLE);
    }
}