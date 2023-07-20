package bd.com.aristo.edcr.modules.dcr.newdcr.existdoctors;

/**
 * Created by altaf.sil on 1/24/18.
 */

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;

import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.models.Doctors;
import bd.com.aristo.edcr.utils.ui.texts.ATextView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ravi on 16/11/17.
 */

public class ExistDoctorsAdapter extends RecyclerView.Adapter<ExistDoctorsAdapter.MyViewHolder>
        implements Filterable {
    private Context mContext;
    private List<Doctors> doctorsList;
    private List<Doctors> doctorsListFiltered;
    private DoctorAdapterListener listener;

    public ExistDoctorsAdapter(Context context, List<Doctors> docList, DoctorAdapterListener listener) {
        this.mContext = context;
        this.listener = listener;
        this.doctorsList = docList;
        this.doctorsListFiltered = docList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.exist_doctor_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Doctors Doctors = doctorsListFiltered.get(position);
        holder.name.setText(Doctors.getName()+"["+Doctors.getDId()+"]");
        holder.degree.setText(Doctors.getDegree());
        holder.special.setText(Doctors.getSpecial());

    }

    @Override
    public int getItemCount() {
        if (doctorsListFiltered!=null){
            return doctorsListFiltered.size();
        }

        return 0;

    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    doctorsListFiltered = doctorsList;
                } else {
                    List<Doctors> filteredList = new ArrayList<>();
                    for (Doctors row : doctorsList) {
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getSpecial().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    doctorsListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = doctorsListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                if (filterResults.values!=null){
                    doctorsListFiltered = (List<Doctors>) filterResults.values;
                }
                notifyDataSetChanged();
            }
        };
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.name)
        ATextView name;

        @BindView(R.id.degree)
        ATextView degree;

        @BindView(R.id.special)
        ATextView special;



        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onDoctorSelected(doctorsListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }


}
