package bd.com.aristo.edcr.dependency;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import bd.com.aristo.edcr.App;
import bd.com.aristo.edcr.fcm.notification.FCMServices;
import bd.com.aristo.edcr.models.ui.MainMenuModel;
import bd.com.aristo.edcr.models.ui.ReportMainMenuModel;
import bd.com.aristo.edcr.models.db.UserModel;
import bd.com.aristo.edcr.modules.tp.model.AddressModel;
import bd.com.aristo.edcr.networking.APIClients;
import bd.com.aristo.edcr.networking.APIServices;
import bd.com.aristo.edcr.networking.RequestServices;
import bd.com.aristo.edcr.utils.DateTimeUtils;
import bd.com.aristo.edcr.utils.LoadingDialog;
import bd.com.aristo.edcr.utils.SharedPrefsUtils;
import bd.com.aristo.edcr.utils.constants.MainMenuConstants;
import bd.com.aristo.edcr.utils.constants.StringConstants;
import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;

import static bd.com.aristo.edcr.modules.dcr.dcr.DCRUtils.getToday;

/**
 * Created by monir.sobuj on 5/17/17.
 */

@Module
public class AppModule {

    private App app;
    private RequestServices requestServices;

    AppModule(App app, RequestServices requestServices){
        this.app = app;
        this.requestServices = requestServices;
    }

    @Provides
    App provideContext(){
        return app;
    }


    @Provides
    List<MainMenuModel> provideMainMenuModelList(){
        List<MainMenuModel> mainMenuModelList              = new ArrayList<>();
        //create model list

        for (int i = 0; i < MainMenuConstants.getInstance().getMenuTexts().length; i++){
            MainMenuModel m = new MainMenuModel();
            m.setMenuText(MainMenuConstants.getInstance().getMenuTexts()[i]);
            m.setMenuIcon(MainMenuConstants.getInstance().getMenuIcons()[i]);
            mainMenuModelList.add(m);
        }
        return mainMenuModelList;
    }


    @Provides
    List<ReportMainMenuModel> provideReportMenuList(){
        List<ReportMainMenuModel> reportMainMenuModels              = new ArrayList<>();
        //create model list

        for (int i = 0; i < MainMenuConstants.getInstance().getReportMenuTexts().length; i++){
            ReportMainMenuModel m = new ReportMainMenuModel();
            m.setMenuText(MainMenuConstants.getInstance().getReportMenuTexts()[i]);
            m.setMenuIcon(MainMenuConstants.getInstance().getReportMenuIcons()[i]);
            reportMainMenuModels.add(m);
        }
        return reportMainMenuModels;
    }


    @Provides
    APIServices provideAPIServices(){
        return APIClients.getInstance().create(APIServices.class);
    }

    @Provides
    FCMServices provideFCMServices(){
        return APIClients.getFCMInstance().create(FCMServices.class);
    }
    @Provides
    Realm provideRealm(){
        RealmConfiguration config = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        //return Realm.getDefaultInstance();
        return Realm.getInstance(config);
    }


    @Provides
    UserModel provideUserModel(){
        Realm r = provideRealm();
        return r.where(UserModel.class).findFirst();
    }


    @Provides
    long provideCurrentMillis(){
        return SharedPrefsUtils.getLongPreference(provideContext(), StringConstants.CURRENT_TIME, 0);
    }


    @Provides
    List<String> ndaProvide(){

        List<String> natureOfDA = new ArrayList<>();
        natureOfDA.add("HQ");
        natureOfDA.add("EH");
        natureOfDA.add("NH");
        natureOfDA.add("NA");

        return natureOfDA;
    }

    @Provides
    List<AddressModel> contactAddressProvide(){
        RealmQuery<AddressModel> query = provideRealm().where(AddressModel.class);
        RealmResults<AddressModel> realmAddressList = query.findAll();
        return realmAddressList;
    }

    @Provides
    LoadingDialog provideLoadingDialog(){
        LoadingDialog loadingDialog = LoadingDialog.newInstance(app, "Please wait...");
        return loadingDialog;
    }


    /**
     * Get a diff between two dates
     * @param date1 the oldest date
     * @param date2 the newest date
     * @param timeUnit the unit in which you want the diff
     * @return the diff value, in the provided unit
     */
    public long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillis = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillis,TimeUnit.MILLISECONDS);
    }

    public Date getDate(){
        Date date = null;
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        try {
            date = formatter.parse(DateTimeUtils.getDayMonthYear(getToday()));
            Log.e("Print result: ", String.valueOf(date));

        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        return date;
    }

    @Provides
    RequestServices provideRequestServices(){ return requestServices;}



}
