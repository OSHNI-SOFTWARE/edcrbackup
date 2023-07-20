package bd.com.aristo.edcr.utils.ui.collections;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;

import java.util.List;

import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.utils.ui.texts.ATextView;

/**
 * Created by monir.sobuj on 6/28/2017.
 */

public class ASpinner extends androidx.appcompat.widget.AppCompatSpinner {

    AttributeSet attributeSet;

    public ASpinner(Context context){
        super(context);
    }
    public ASpinner(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        this.attributeSet = attributeSet;
    }
    public ASpinner(Context context, AttributeSet attributeSet, int defStyleAttr){
        super(context, attributeSet, defStyleAttr);
        this.attributeSet = attributeSet;
    }

    public void initCustomSpinner(List<?> dataSet) {
        CustomSpinnerAdapter customSpinnerAdapter=new CustomSpinnerAdapter(getContext(),dataSet);
        setAdapter(customSpinnerAdapter);

    }


    public class CustomSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

        private final Context activity;
        private List<?> asr;
        LayoutInflater inflater;

        public CustomSpinnerAdapter(Context context, List<?> asr) {

            this.asr=asr;
            activity = context;
            inflater = LayoutInflater.from(context);
        }



        public int getCount()
        {
            return asr.size();
        }

        public Object getItem(int i)
        {
            return asr.get(i);
        }

        public long getItemId(int i)
        {
            return (long)i;
        }



        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            convertView = inflater.inflate(R.layout.text_view_aspinner, null);
            ATextView txt = (ATextView) convertView.findViewById(R.id.text_view_spinner);
            txt.setText(asr.get(position).toString());
            return  convertView;
        }

        public View getView(int i, View view, ViewGroup viewgroup) {
            view = inflater.inflate(R.layout.text_view_aspinner, null);
            ATextView txt = (ATextView) view.findViewById(R.id.text_view_spinner);
            txt.setText(asr.get(i).toString());
            txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_down, 0);
            return  view;
        }

    }

}
