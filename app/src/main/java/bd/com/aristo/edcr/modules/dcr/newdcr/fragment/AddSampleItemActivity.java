package bd.com.aristo.edcr.modules.dcr.newdcr.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.IItemAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import bd.com.aristo.edcr.App;
import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.models.db.ProductModel;
import bd.com.aristo.edcr.models.db.UserModel;
import bd.com.aristo.edcr.modules.dcr.dcr.DCRUtils;
import bd.com.aristo.edcr.modules.dcr.dcr.model.SampleProductsModel;
import bd.com.aristo.edcr.models.db.DateModel;
import bd.com.aristo.edcr.modules.wp.WPUtils;
import bd.com.aristo.edcr.networking.APIServices;
import bd.com.aristo.edcr.utils.ToastUtils;
import bd.com.aristo.edcr.utils.ui.texts.AnEditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class AddSampleItemActivity extends AppCompatActivity {

    private final String TAG = AddSampleItemActivity.class.getSimpleName();
    private AddSampleItemActivity activity = this;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.etSearch)
    AnEditText etSearch;

    @Inject
    APIServices apiServices;
    @Inject
    Realm r;

    List<ProductModel> productModelList;
    List<SampleProductsModel> sampleProductsList;

    public UserModel userModel;
    public DateModel dateModel;
    public void getUserInfo(){
        userModel = r.where(UserModel.class).findFirst();
    }

    public static void start(Activity context){
        Intent intent = new Intent(context, AddSampleItemActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sample_item);
        dateModel = DCRUtils.getToday();
        initialize();


    }

    public void initialize(){
        ButterKnife.bind(this);
        etSearch.setVisibility(View.GONE);
        setSupportActionBar(toolbar);
        App.getComponent().inject(this);

        getUserInfo();

        try{
            getSupportActionBar().setHomeButtonEnabled(true);
        }catch (Exception e){
            e.printStackTrace();
        }
        productModelList = WPUtils.getMonthWiseProducts(r, dateModel.getYear(), dateModel.getMonth());
        if(productModelList.size() > 0) {
            sampleProductsList = WPUtils.getSampleProductModels(r, productModelList, dateModel);
            refreshList();
        }

    }

    private void refreshList(){


        final FastItemAdapter<SampleProductsModel> fastAdapter = new FastItemAdapter<>();
        fastAdapter.add(sampleProductsList);
        fastAdapter.withSelectable(true);
        fastAdapter.withOnClickListener(new FastAdapter.OnClickListener<SampleProductsModel>() {
            @Override
            public boolean onClick(View v, IAdapter<SampleProductsModel> adapter, SampleProductsModel item, int position) {
                //ToastUtils.shortToast(item.getName());
                if(item.getBalance() > 0) {
                    EventBus.getDefault().post(item);

                    finish();
                } else {
                    ToastUtils.shortToast("No Balance!!");
                }

                return false;
            }
        });

        //fill the recycler view
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(fastAdapter);

        fastAdapter.getItemAdapter().withFilterPredicate(new IItemAdapter.Predicate<SampleProductsModel>() {
            @Override
            public boolean filter(SampleProductsModel item, CharSequence constraint) {
                return !item.getName().toLowerCase().contains(constraint.toString().toLowerCase());
                //return !item.getName().startsWith(String.valueOf(constraint));
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fastAdapter.getItemAdapter().filter(s);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //fastAdapter.getItemAdapter().filter(s);
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();

        }
        return true;
    }


}
