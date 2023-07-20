package bd.com.aristo.edcr.modules.dss;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.utils.ui.texts.ATextView;

/**
 * Created by altaf.sil on 1/23/18.
 */

public class DSSViewHolder extends RecyclerView.ViewHolder {

    ATextView name, tvCount;
    ImageView ivCommon, ivTick;
    ATextView txtItemFor;
    public DSSViewHolder(View itemView) {
        super(itemView);
        name = (ATextView) itemView.findViewById(R.id.name);
        txtItemFor = (ATextView) itemView.findViewById(R.id.txtItemFor);
        tvCount = (ATextView) itemView.findViewById(R.id.tvCount);
        ivCommon = (ImageView) itemView.findViewById(R.id.ivCheck);
        ivTick = (ImageView) itemView.findViewById(R.id.ivTick);
    }
}
