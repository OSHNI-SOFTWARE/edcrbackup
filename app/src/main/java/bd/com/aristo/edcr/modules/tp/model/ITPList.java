package bd.com.aristo.edcr.modules.tp.model;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mikepenz.fastadapter.IItem;

import java.util.Collections;
import java.util.List;

import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.utils.StringUtils;
import bd.com.aristo.edcr.utils.constants.StringConstants;
import bd.com.aristo.edcr.utils.ui.texts.ATextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.annotations.Ignore;

/**
 * Created by monir.sobuj on 6/8/17.
 */

public class ITPList implements IItem<ITPList, ITPList.ViewHolder> {


    private String date;

    private String morningShiftType;

    private String morningContact;
    private String morningRT;
    private String morningPlacelist;
    private String morningNDA;

    private String eveningContact;
    private String eveningRT;
    private String eveningPlacelist;
    private String eveningNDA;

    private String eveningShiftType;

    private boolean isTPChanged = false;

    private String cell;

    private int day;
    private int month;
    private int year;

    private boolean clicked = false;

    private Object tag;// defines if this item is isSelectable

    private boolean isSelectable = true;

    private boolean isEnabled = true;


    @Ignore
    private boolean isSelected = false; // defines if the item is selected

    public boolean isClicked() {
        return clicked;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }


    public static final String COL_CODE = "code", COL_SHIFT = "shift", COL_NAME = "name", COL_ID = "id";


    public String getMorningContact() {
        return morningContact;
    }

    public void setMorningContact(String morningContact) {
        this.morningContact = morningContact;
    }

    public String getEveningContact() {
        return eveningContact;
    }

    public void setEveningContact(String eveningContact) {
        this.eveningContact = eveningContact;
    }

    public String getMorningRT() {
        return morningRT;
    }

    public void setMorningRT(String morningRT) {
        this.morningRT = morningRT;
    }

    public String getEveningRT() {
        return eveningRT;
    }

    public void setEveningRT(String eveningRT) {
        this.eveningRT = eveningRT;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMorningPlacelist() {
        return morningPlacelist;
    }

    public void setMorningPlacelist(String morningPlacelist) {
        this.morningPlacelist = morningPlacelist;
    }

    public String getMorningNDA() {
        return morningNDA;
    }

    public void setMorningNDA(String morningNDA) {
        this.morningNDA = morningNDA;
    }

    public String getEveningPlacelist() {
        return eveningPlacelist;
    }

    public void setEveningPlacelist(String eveningPlacelist) {
        this.eveningPlacelist = eveningPlacelist;
    }

    public String getEveningNDA() {
        return eveningNDA;
    }

    public void setEveningNDA(String eveningNDA) {
        this.eveningNDA = eveningNDA;
    }

    public String getMorningShiftType() {
        return morningShiftType;
    }

    public void setMorningShiftType(String morningShiftType) {
        this.morningShiftType = morningShiftType;
    }

    public String getEveningShiftType() {
        return eveningShiftType;
    }

    public void setEveningShiftType(String eveningShiftType) {
        this.eveningShiftType = eveningShiftType;
    }


    public boolean isTPChanged() {
        return isTPChanged;
    }

    public void setTPChanged(boolean TPChanged) {
        isTPChanged = TPChanged;
    }

    @Override
    public Object getTag() {
        return tag;
    }

    @Override
    public ITPList withTag(Object tag) {
        this.tag = tag;
        return this;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public ITPList withEnabled(boolean enabled) {
        this.isEnabled = enabled;
        return this;
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public ITPList withSetSelected(boolean selected) {
        isSelected = selected;
        return this;
    }


    @Override
    public boolean isSelectable() {
        return isSelectable;
    }

    @Override
    public ITPList withSelectable(boolean selectable) {
        this.isSelectable = selectable;
        return this;
    }

    @Override
    public int getType() {
        return R.id.tpList;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_tp;
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
        holder.dateTV.setText(date);

        if(isTPChanged()){
            holder.txtTPChanged.setVisibility(View.VISIBLE);
        } else {
            holder.txtTPChanged.setVisibility(View.GONE);
        }
        holder.morningShiftTypeTV.setText(morningShiftType);
        holder.morningContact.setText(morningContact);

        if (morningShiftType!=null && morningShiftType.equalsIgnoreCase(StringConstants.LEAVE)){
            holder.morningRT.setVisibility(View.GONE);
            holder.morningPlaceTV.setVisibility(View.GONE);
            holder.morningNDA.setVisibility(View.GONE);
        }else if(morningShiftType!=null &&  morningShiftType.equalsIgnoreCase(StringConstants.MEETING)){
            holder.morningRT.setText(morningRT);
            holder.morningPlaceTV.setText(morningPlacelist);
            holder.morningNDA.setVisibility(View.GONE);
        }else{
            holder.morningRT.setText(morningRT);
            holder.morningPlaceTV.setText(morningPlacelist);
            holder.morningNDA.setText(morningNDA);
        }

        holder.eveningShiftTypeTV.setText(eveningShiftType);
        holder.eveningContact.setText(eveningContact);

        if (eveningShiftType!=null && eveningShiftType.equalsIgnoreCase(StringConstants.LEAVE)){
            holder.eveningRT.setVisibility(View.GONE);
            holder.eveningPlaceListTV.setVisibility(View.GONE);
            holder.eveningNDA.setVisibility(View.GONE);
        }else if(eveningShiftType!=null && eveningShiftType.equalsIgnoreCase(StringConstants.MEETING)){
            holder.eveningRT.setText(eveningRT);
            holder.eveningPlaceListTV.setText(eveningPlacelist);
            holder.eveningNDA.setVisibility(View.GONE);
        }else{
            holder.eveningRT.setText(eveningRT);
            holder.eveningPlaceListTV.setText(eveningPlacelist);
            holder.eveningNDA.setText(eveningNDA);
        }



    }

    @Override
    public void unbindView(ViewHolder holder) {
        holder.morningShiftTypeTV.setText(null);
        holder.dateTV.setText(null);
        holder.morningContact.setText(null);
        holder.morningRT.setText(null);
        holder.morningPlaceTV.setText(null);
        holder.morningNDA.setText(null);

        holder.eveningShiftTypeTV.setText(null);
        holder.eveningContact.setText(null);
        holder.eveningRT.setText(null);
        holder.eveningPlaceListTV.setText(null);
        holder.eveningNDA.setText(null);
    }

    @Override
    public boolean equals(int code) {
        return false;
    }

    @Override
    public ITPList withIdentifier(long identifier) {
        return null;
    }

    @Override
    public long getIdentifier() {
        return StringUtils.hashString64Bit(date);
    }

    public String getCell() {
        return cell;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }


    static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.dateTV)
        ATextView dateTV;

        @BindView(R.id.morningShiftTypeTV)
        ATextView morningShiftTypeTV;

        @BindView(R.id.morningContact)
        ATextView morningContact;

        @BindView(R.id.morningRT)
        ATextView morningRT;

        @BindView(R.id.morningPlaceTV)
        ATextView morningPlaceTV;

        @BindView(R.id.morningNDA)
        ATextView morningNDA;

        @BindView(R.id.eveningShiftTypeTV)
        ATextView eveningShiftTypeTV;

        @BindView(R.id.eveningContact)
        ATextView eveningContact;

        @BindView(R.id.eveningRT)
        ATextView eveningRT;

        @BindView(R.id.eveningPlaceListTV)
        ATextView eveningPlaceListTV;

        @BindView(R.id.eveningNDA)
        ATextView eveningNDA;

        @BindView(R.id.txtTPChanged)
        ATextView txtTPChanged;

        //txtTPChanged


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
