package bd.com.aristo.edcr.modules.dcr.newdcr.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mikepenz.fastadapter.listeners.ClickEventHook;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import bd.com.aristo.edcr.App;
import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.models.db.UserModel;
import bd.com.aristo.edcr.modules.dcr.DCRSendModel;
import bd.com.aristo.edcr.modules.dcr.dcr.model.DCRModelForSend;
import bd.com.aristo.edcr.modules.dcr.newdcr.NewDCRListListener;
import bd.com.aristo.edcr.modules.dcr.newdcr.NewDCRUtils;
import bd.com.aristo.edcr.modules.dcr.newdcr.model.NewDCRModel;
import bd.com.aristo.edcr.modules.dcr.newdcr.model.NewDCRProductModel;
import bd.com.aristo.edcr.networking.APIServices;
import bd.com.aristo.edcr.networking.RequestServices;
import bd.com.aristo.edcr.utils.SharedPrefsUtils;
import bd.com.aristo.edcr.utils.ToastUtils;
import bd.com.aristo.edcr.utils.constants.StringConstants;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

/**
 * Created by monir.sobuj on 6/5/17.
 */

public class NewDCRListFragment extends Fragment implements NewDCRListListener {

    public static final String TAG = NewDCRListFragment.class.getSimpleName();

    @Inject
    APIServices apiServices;

    @Inject
    Realm r;
    @BindView(R.id.rvNewDcr)
    RecyclerView rvNewDcr;
    Context context;

    public UserModel userModel;
    @Inject
    RequestServices requestServices;

    public void getUserInfo() {
        userModel = r.where(UserModel.class).findFirst();
    }

    public NewDCRListFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //initiate dependencies
        App.getComponent().inject(this);
        View rootView = inflater.inflate(R.layout.fragment_newdcr_list, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getUserInfo();
        if(!SharedPrefsUtils.getBooleanPreference(getContext(), StringConstants.IS_PRODUCT_SYNCED, false)){
            requestServices.getProductAfterDCR(context, apiServices, userModel.getUserId(), r);
        }
        //SharedPrefsUtils.setBooleanPreference(getContext(), StringConstants.IS_PRODUCT_SYNCED, false);
        /*if(ConnectionUtils.isNetworkConnected(context)){
            requestServices.getProductAfterDCR(context, apiServices, userModel.getUserId(), r);
        }*/
        ((Activity) context).setTitle("New DCR List");
        refreshList();


    }

    public void refreshList() {
        List<NewDCRModel> dcrDoctors = NewDCRUtils.getNewDCRList(r);
        FastItemAdapter<NewDCRModel> fastAdapter = new FastItemAdapter<>();
        fastAdapter.add(dcrDoctors);
        fastAdapter.withSelectable(true);

        fastAdapter.withOnClickListener(new FastAdapter.OnClickListener<NewDCRModel>() {
            @Override
            public boolean onClick(View v, IAdapter<NewDCRModel> adapter, final NewDCRModel item, int position) {
                Log.e("DoctorListFragment", "" + item.getId());
                return false;
            }
        });


        //Click event on sync status
        fastAdapter.withItemEvent(new ClickEventHook<NewDCRModel>() {

            @Nullable
            @Override
            public View onBind(@NonNull RecyclerView.ViewHolder viewHolder) {
                if (viewHolder instanceof NewDCRModel.ViewHolder) {
                    return ((NewDCRModel.ViewHolder) viewHolder).ivSync;
                }
                return null;
            }

            @Override
            public void onClick(View v, int position, FastAdapter<NewDCRModel> fastAdapter1, final NewDCRModel item) {
                //react on the click event
                if (!item.isSynced()) {
                    displayNewDCRUploadAlert(item);
                }


            }
        });


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        rvNewDcr.setLayoutManager(layoutManager);
        rvNewDcr.setAdapter(fastAdapter);
    }

    public void newDcr(int option) {
        Fragment fragment;
        switch (option) {
            case 0:
                fragment = NewDoctorFragment.newInstance();
                ((AppCompatActivity) context).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, fragment)
                        .addToBackStack("dcr_new_doctor")
                        .commit();
                break;
            case 1:
                fragment = ExistingDoctorFragment.newInstance();
                ((AppCompatActivity) context).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, fragment)
                        .addToBackStack("dcr_existing_doctor")
                        .commit();
                break;
            case 2:
                fragment = NewDcrInternFragment.newInstance();
                ((AppCompatActivity) context).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, fragment)
                        .addToBackStack("dcr_intern_doctor")
                        .commit();
                break;
            case 3:
                fragment = OthersFragment.newInstance();
                ((AppCompatActivity) context).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, fragment)
                        .addToBackStack("dcr_others")
                        .commit();
                break;
            default:
                break;
        }

    }


    @OnClick(R.id.btnExisting)
    public void existingDoc() {
        newDcr(1);
    }

    @OnClick(R.id.btnNewDoctor)
    public void newDoctor() {
        newDcr(0);
    }

    @OnClick(R.id.btnIntern)
    public void internDoc() {
        newDcr(2);
    }

    @OnClick(R.id.btnOthers)
    public void others() {
        newDcr(3);
    }


    public void displayNewDCRUploadAlert(final NewDCRModel newDCRModel) {
        String accompanyIDS = newDCRModel.getAccompanyId();
        String msg = "";
        if (TextUtils.isEmpty(accompanyIDS)) {
            msg = "You are trying to upload New DCR " + " <span style=\"color:#F06423;\">" + "without Accompany!</span>" + " Would you like to continue?"; //#F06423
        } else {
            msg = "You are trying to upload New DCR" + " <span style=\"color:#01991f;\">" + "Accompany with: " + accompanyIDS + "!" + "</span>" + " Would you like to continue?"; //#01991f
        }
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setMessage(Html.fromHtml(msg));
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
                uploadNewDcrDoctor(newDCRModel);
            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        alert.show();
    }

    public void uploadNewDcrDoctor(final NewDCRModel newDCRModel) {
        final long newDCRID = newDCRModel.getId();
        String strKeyPerson = "";
        String strTeamVolume = "0";
        if (newDCRModel.getOption() == 2) {
            //Intern doctor
            strKeyPerson = newDCRModel.getDoctorName();
            strTeamVolume = newDCRModel.getNoOfIntern();
        }
        List<DCRModelForSend> dcrModelForSendList = getNewDcrForSend(r, newDCRID);
        DCRSendModel dcrSendModel = new DCRSendModel();
        dcrSendModel.setUserId(userModel.getUserId());
        dcrSendModel.setAccompanyIds(TextUtils.isEmpty(newDCRModel.getAccompanyId()) ? "" : newDCRModel.getAccompanyId());
        dcrSendModel.setCreateDate(newDCRModel.getDate());
        dcrSendModel.setShift(newDCRModel.getShift());
        dcrSendModel.setDoctorId(newDCRModel.getDoctorID());
        dcrSendModel.setRemarks(newDCRModel.getRemarks());
        dcrSendModel.setWard(newDCRModel.getWard());
        dcrSendModel.setContactNo(newDCRModel.getContact());
        dcrSendModel.setTeamLeader(strKeyPerson);
        dcrSendModel.setTeamVolume(strTeamVolume);
        dcrSendModel.setDcrSubType(newDCRModel.getOption());
        dcrSendModel.setSampleList(dcrModelForSendList);
        requestServices.uploadNewDcrFromList(context, dcrSendModel, apiServices, r, this, newDCRModel);
    }

    public List<DCRModelForSend> getNewDcrForSend(Realm r,long newDcrID){

        List<DCRModelForSend> dcrModelForSendList = new ArrayList<>();
        NewDCRModel dcrModels = r.where(NewDCRModel.class)
                .equalTo(NewDCRModel.COL_ID,newDcrID)
                .findFirst();


        if(dcrModels != null){
            String[] date = dcrModels.getDate().split("-");
            String monthYear =  "";
            if(date.length == 3){
                monthYear = date[1]+"-"+date[2];
            }
            List<NewDCRProductModel> dcrProductModels = r.where(NewDCRProductModel.class)
                    .equalTo(NewDCRProductModel.COL_NEW_DCR_ID, dcrModels.getId())
                    .findAll();

            for(NewDCRProductModel newDCRProductModel:dcrProductModels){
                DCRModelForSend newDCRModelForSend = new DCRModelForSend();
                newDCRModelForSend.setProductCode(newDCRProductModel.getProductID());
                newDCRModelForSend.setQuantity(String.valueOf(NewDCRUtils.setSaveBalance(r, newDCRProductModel)));
                dcrModelForSendList.add(newDCRModelForSend);
            }
        }
        return dcrModelForSendList;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }


    @Override
    public void onSuccess(String remarks, final NewDCRModel newDCRModel) {
        ToastUtils.shortToast(StringConstants.UPLOAD_SUCCESS_MSG);
        requestServices.getProductAfterDCR(context, apiServices, userModel.getUserId(), r);
        r.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm r) {
                newDCRModel.setSynced(true);
                r.insertOrUpdate(newDCRModel);
                refreshList();


            }
        });
    }

    @Override
    public void onFailed(String remarks) {

    }
}
