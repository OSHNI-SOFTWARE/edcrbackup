package bd.com.aristo.edcr.modules.dss;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import bd.com.aristo.edcr.R;

/**
 * Created by altaf.sil on 1/23/18.
 */

public class SectionViewHolder extends RecyclerView.ViewHolder {

    TextView name;
    public SectionViewHolder(View itemView) {
        super(itemView);
        name = (TextView) itemView.findViewById(R.id.section);
    }
}
