package bd.com.aristo.edcr.fcm.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.fcm.listener.NotificationListener;
import bd.com.aristo.edcr.models.db.NotificationModel;
import bd.com.aristo.edcr.utils.SystemUtils;
import bd.com.aristo.edcr.utils.ui.texts.ATextView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by altaf.sil on 1/18/18.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private List<NotificationModel> notificationModelList;
    private Context mContext;

    private NotificationListener mNotificationListener;

    public NotificationAdapter(Context context, NotificationListener notificationListener) {
        this.mContext = context;
        this.mNotificationListener = notificationListener;
        this.notificationModelList = new ArrayList<>();
    }

    @Override
    public NotificationViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_notification_row,viewGroup,false);

        NotificationViewHolder viewHolder = new NotificationViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NotificationViewHolder notificationViewHolder, int i) {
        final NotificationModel notificationModel = notificationModelList.get(i);
        notificationViewHolder.titleTV.setText(notificationModel.getTitle());
        notificationViewHolder.detailTV.setText(notificationModel.getDetail());
        notificationViewHolder.dateTimeTV.setText(notificationModel.getDateTime());
        if(notificationModel.isRead()){
            notificationViewHolder.llRow.setBackgroundColor(SystemUtils.getColorFromID(mContext, R.color.transparent));
        } else {
            notificationViewHolder.llRow.setBackgroundColor(SystemUtils.getColorFromID(mContext, R.color.light_gray));
        }
        notificationViewHolder.llRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNotificationListener.onClickTag(notificationModel);
            }
        });

    }

    public void setNotificationsList(List<NotificationModel> notificationsList){
        notificationModelList.clear();
        notificationModelList.addAll(notificationsList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (null != notificationModelList ? notificationModelList.size() : 0);
    }


    class NotificationViewHolder extends RecyclerView.ViewHolder {
       @BindView(R.id.txtTitle)
       ATextView titleTV;

        @BindView(R.id.txtMsg)
        ATextView detailTV;

        @BindView(R.id.ivSync)
        ImageView syncIV;

        @BindView(R.id.txtDateTime)
        ATextView dateTimeTV;
        @BindView(R.id.llRow)
        LinearLayout llRow;


        public NotificationViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);

        }
    }
}
