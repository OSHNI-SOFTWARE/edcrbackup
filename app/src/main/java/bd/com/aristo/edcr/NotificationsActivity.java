package bd.com.aristo.edcr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import javax.inject.Inject;

import bd.com.aristo.edcr.fcm.adapter.NotificationAdapter;
import bd.com.aristo.edcr.fcm.listener.NotificationListener;
import bd.com.aristo.edcr.models.db.NotificationModel;
import bd.com.aristo.edcr.models.db.UserModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class NotificationsActivity extends AppCompatActivity  implements NotificationListener{

    private static final String TAG = NotificationsActivity.class.getSimpleName();

    private NotificationsActivity activity = this;

    @Inject
    Realm r;

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.notFoundTV)
    TextView notFoundTV;

    NotificationAdapter notificationAdapter;


    public static void start(Activity context){
        Intent intent = new Intent(context,NotificationsActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        App.getComponent().inject(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initialize();

        fetchData();
    }

    public void initialize(){
        ButterKnife.bind(this);

        UserModel userModel = r.where(UserModel.class).findFirst();
        if (userModel==null){
            LoginActivity.start(activity);
            finish();
        }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity);
        mRecyclerView.setLayoutManager(layoutManager);
        notificationAdapter = new NotificationAdapter(activity,this);
        mRecyclerView.setAdapter(notificationAdapter);

    }


    public void fetchData(){
        RealmQuery<NotificationModel> query = r.where(NotificationModel.class)
                //.equalTo("isRead",false)
                .sort("nID");
        final RealmResults<NotificationModel> notificationsList = query.findAll();
        notificationAdapter.setNotificationsList(notificationsList);

        if (notificationsList.size()>0){
            notFoundTV.setVisibility(View.GONE);
        }else {
            notFoundTV.setVisibility(View.VISIBLE);
        }

    }


    @Override
    public void onClickTag(final NotificationModel notificationModel) {
        r.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                notificationModel.setRead(true);
                r.insertOrUpdate(notificationModel);
                notificationAdapter.notifyDataSetChanged();
            }
        });

    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();

        }
        return true;
    }
}
