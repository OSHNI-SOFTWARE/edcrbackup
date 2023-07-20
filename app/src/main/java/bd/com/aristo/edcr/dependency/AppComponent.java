package bd.com.aristo.edcr.dependency;

import bd.com.aristo.edcr.App;
import bd.com.aristo.edcr.ColorInfoTDPGDialog;
import bd.com.aristo.edcr.ProductSynJob;
import bd.com.aristo.edcr.fcm.notification.FCMSendNotification;
import bd.com.aristo.edcr.modules.bill.fragment.DatePickerFragment;
import bd.com.aristo.edcr.modules.dcr.dcr.activity.DCRCalendarActivity;
import bd.com.aristo.edcr.modules.dcr.dcr.activity.DCRSummaryListActivity;
import bd.com.aristo.edcr.modules.dcr.dcr.fragment.AddAccompanyDialog;
import bd.com.aristo.edcr.modules.dcr.dcr.fragment.DCRColorInfoDialog;
import bd.com.aristo.edcr.modules.dcr.dcr.fragment.InternRestUploadDialog;
import bd.com.aristo.edcr.modules.dvr.fragment.ColorInfoDialog;
import bd.com.aristo.edcr.modules.dvr.fragment.DVRCalendarDialog;
import bd.com.aristo.edcr.modules.dvr.fragment.DVRSelectionActivity;
import bd.com.aristo.edcr.modules.dvr.fragment.DVRSelectionFromSummaryActivity;
import bd.com.aristo.edcr.modules.dvr.fragment.DVRSummaryActivity;
import bd.com.aristo.edcr.modules.gwds.GWDSListFragment;
import bd.com.aristo.edcr.modules.location.DoctorLocationActivity;
import bd.com.aristo.edcr.modules.location.EditDoctorLocationDialog;
import bd.com.aristo.edcr.HomeActivity;
import bd.com.aristo.edcr.modules.dss.DSSCalendarFragment;
import bd.com.aristo.edcr.modules.dvr.fragment.DVRActivity;
import bd.com.aristo.edcr.modules.gwds.GWDSGiftFragment;
import bd.com.aristo.edcr.modules.location.EveningLocationFragment;
import bd.com.aristo.edcr.modules.location.LocationWiseDoctorActivity;
import bd.com.aristo.edcr.modules.location.MorningLocationFragment;
import bd.com.aristo.edcr.modules.location.NoLocationFragment;
import bd.com.aristo.edcr.modules.pwds.PWDSListFragment;
import bd.com.aristo.edcr.modules.pwds.PWDSProductsFragment;
import bd.com.aristo.edcr.modules.reports.ReportActivity;
import bd.com.aristo.edcr.modules.reports.ReportMenuActivity;
import bd.com.aristo.edcr.models.ui.ReportMainMenuModel;
import bd.com.aristo.edcr.modules.bill.fragment.BillStatementActivity;
import bd.com.aristo.edcr.ChangePasswordActivity;
import bd.com.aristo.edcr.modules.dcr.dcr.activity.DCRActivity;
import bd.com.aristo.edcr.HostActivity;
import bd.com.aristo.edcr.LoginActivity;
import bd.com.aristo.edcr.MainMenuActivity;
import bd.com.aristo.edcr.NotificationsActivity;
import bd.com.aristo.edcr.SettingsActivity;
import bd.com.aristo.edcr.models.ui.MainMenuModel;
import bd.com.aristo.edcr.modules.bill.fragment.BillListFragment;
import bd.com.aristo.edcr.modules.bill.fragment.DetailsFragment;
import bd.com.aristo.edcr.modules.dcr.accompany.AccompanyActivity;
import bd.com.aristo.edcr.modules.dcr.dcr.fragment.AddSampleItemActivity;
import bd.com.aristo.edcr.modules.dcr.dcr.fragment.DCRDetailFragment;
import bd.com.aristo.edcr.modules.dcr.dcr.fragment.DCRNewFragment;
import bd.com.aristo.edcr.modules.dcr.dcr.fragment.DCRUnCoverFragment;
import bd.com.aristo.edcr.modules.dcr.dcr.fragment.DCRUploadDialog;
import bd.com.aristo.edcr.modules.dcr.dcr.fragment.DCRViewPager;
import bd.com.aristo.edcr.modules.dcr.dcr.fragment.DoctorListFragment;
import bd.com.aristo.edcr.modules.dcr.dcr.fragment.DoctorListUnplanFragment;
import bd.com.aristo.edcr.modules.dcr.dcr.fragment.DoctorListViewPager;
import bd.com.aristo.edcr.modules.dcr.dcr.fragment.InternDCRViewPager;
import bd.com.aristo.edcr.modules.dcr.dcr.fragment.InternGiftFragment;
import bd.com.aristo.edcr.modules.dcr.dcr.fragment.InternSampleFragment;
import bd.com.aristo.edcr.modules.dcr.newdcr.fragment.ExistingDoctorFragment;
import bd.com.aristo.edcr.modules.dcr.dcr.fragment.GiftFragment;
import bd.com.aristo.edcr.modules.dcr.newdcr.fragment.NewDcrInternFragment;
import bd.com.aristo.edcr.modules.dcr.newdcr.fragment.NewDoctorFragment;
import bd.com.aristo.edcr.modules.dcr.newdcr.fragment.NewGiftFragment;
import bd.com.aristo.edcr.modules.dcr.newdcr.fragment.NewPromotedFragment;
import bd.com.aristo.edcr.modules.dcr.newdcr.fragment.NewSampleFragment;
import bd.com.aristo.edcr.modules.dcr.newdcr.fragment.OthersFragment;
import bd.com.aristo.edcr.modules.dvr.fragment.DVREveningFragment;
import bd.com.aristo.edcr.modules.dvr.fragment.DVRMorningFragment;
import bd.com.aristo.edcr.modules.reports.others.ColorInfoDCRDialog;
import bd.com.aristo.edcr.modules.reports.others.DCRAccompanyFragment;
import bd.com.aristo.edcr.modules.reports.others.DCRDoctorListFragment;
import bd.com.aristo.edcr.modules.reports.others.DCRMPOFragment;
import bd.com.aristo.edcr.modules.reports.others.DVRMarketListFragment;
import bd.com.aristo.edcr.modules.reports.others.DVRSummaryFragment;
import bd.com.aristo.edcr.modules.reports.others.DoctorCoverageFragment;
import bd.com.aristo.edcr.modules.reports.others.DoctorWiseDOTFragment;
import bd.com.aristo.edcr.modules.reports.others.DoctorWiseItemFragment;
import bd.com.aristo.edcr.modules.reports.others.NoDCRDoctorListFragment;
import bd.com.aristo.edcr.modules.reports.others.PWDSReportFragment;
import bd.com.aristo.edcr.modules.reports.others.PhysicalStockFragment;
import bd.com.aristo.edcr.modules.reports.others.UncoverDotFragment;
import bd.com.aristo.edcr.modules.reports.others.WorkPlanSummaryFragment;
import bd.com.aristo.edcr.modules.reports.ss.GiftItemFragment;
import bd.com.aristo.edcr.modules.reports.ss.InternItemFragment;
import bd.com.aristo.edcr.modules.reports.ss.SampleProductFragment;
import bd.com.aristo.edcr.modules.reports.ss.SampleStatementFragment;
import bd.com.aristo.edcr.modules.reports.ss.SelectiveProductFragment;
import bd.com.aristo.edcr.modules.tp.activity.TPListActivity;
import bd.com.aristo.edcr.modules.tp.fragment.TPCalendarFragment;
import bd.com.aristo.edcr.modules.tp.model.CalenderModel;
import bd.com.aristo.edcr.modules.wp.fragment.ColorInfoWPDialog;
import bd.com.aristo.edcr.modules.wp.fragment.DoctorsFragment;
import bd.com.aristo.edcr.modules.wp.fragment.InternUploadDialog;
import bd.com.aristo.edcr.modules.wp.fragment.WPCalendarFragment;
import bd.com.aristo.edcr.modules.wp.fragment.WPViewPager;
import bd.com.aristo.edcr.modules.dcr.dcr.fragment.PromotedFragment;
import bd.com.aristo.edcr.modules.dcr.dcr.fragment.SampleFragment;
import bd.com.aristo.edcr.modules.dcr.newdcr.existdoctors.ExistDoctorsListActivity;
import bd.com.aristo.edcr.modules.dcr.newdcr.fragment.NewDCRListFragment;
import bd.com.aristo.edcr.modules.dss.DSSFragment;
import bd.com.aristo.edcr.modules.tp.fragment.TPEveningFragment;
import bd.com.aristo.edcr.modules.tp.fragment.TPListFragment;
import bd.com.aristo.edcr.modules.tp.fragment.TPMorningFragment;
import bd.com.aristo.edcr.modules.tp.activity.TourPlanActivity;
import bd.com.aristo.edcr.modules.tp.activity.WorkPlaceActivity;
import bd.com.aristo.edcr.networking.RequestServices;
import bd.com.aristo.edcr.utils.RealmUtils;
import dagger.Component;

/**
 * Created by Tariqul.Islam on 5/17/17.
 */

@Component(modules = AppModule.class)

public interface AppComponent {

    void inject(LoginActivity loginActivity);
    void inject(HomeActivity homeActivity);
    void inject(MainMenuModel mainMenuModel);
    void inject(ReportMainMenuModel reportMainMenuModel);
    void inject(MainMenuActivity mainMenuActivity);
    void inject(ReportMenuActivity reportMenuActivity);
    void inject(NotificationsActivity notificationsActivity);
    void inject(DoctorLocationActivity doctorLocationActivity);
    void inject(EditDoctorLocationDialog editDoctorLocationDialog);
    void inject(LocationWiseDoctorActivity locationWiseDoctorActivity);
    void inject(EveningLocationFragment eveningLocationFragment);
    void inject(MorningLocationFragment morningLocationFragment);
    void inject(NoLocationFragment noLocationFragment);

    void inject(TPCalendarFragment tpCalendarFragment);
    void inject(TPListActivity tpListActivity);
    void inject(CalenderModel tpCalendarModel);
    void inject(TPListFragment tpListFragment);
    void inject(WorkPlaceActivity tpWorkPlaceActivity);
    void inject(TourPlanActivity tourPlanActivity);
    void inject(TPMorningFragment tpMorningFragment);
    void inject(TPEveningFragment tpEveningFragment);

    void inject(HostActivity hostActivity);
    void inject(ReportActivity reportActivity);


    void inject(SettingsActivity settingsActivity);
    void inject(ChangePasswordActivity changePasswordActivity);

    void inject(AddSampleItemActivity addSampleItemActivity);
    void inject(PWDSProductsFragment PWDSProductsFragment);
    void inject(PWDSListFragment pwdsListFragment);
    void inject(bd.com.aristo.edcr.modules.pwds.DoctorsFragment doctorsFragment);
    void inject(GWDSGiftFragment GWDSGiftFragment);
    void inject(GWDSListFragment gwdsListFragment);
    void inject(bd.com.aristo.edcr.modules.gwds.DoctorsFragment doctorsFragment);
    void inject(bd.com.aristo.edcr.modules.dvr.fragment.DVRActivity dvrActivity);
    void inject(DVRMarketListFragment dvrMarketListFragment);
    void inject(DVRMorningFragment DVRMorningFragment);
    void inject(DVREveningFragment dvrEveningFragment);
    void inject(DVRCalendarDialog dvrCalendarDialog);
    void inject(DVRSelectionActivity dvrSelectionActivity);
    void inject(DVRSelectionFromSummaryActivity dvrSelectionFromSummaryActivity);
    void inject(DVRSummaryActivity dvrSummaryActivity);
    void inject(DSSCalendarFragment dssCalendarFragment);
    void inject(DSSFragment dssProductFragment);
    void inject(WPCalendarFragment wpCalendarFragment);
    void inject(DoctorsFragment wpDoctorFragment);
    void inject(InternUploadDialog internUploadDialog);
    void inject(WPViewPager wpViewPager);
    void inject(bd.com.aristo.edcr.modules.wp.fragment.PromotedFragment wpPromotedFragment);
    void inject(bd.com.aristo.edcr.modules.wp.fragment.SampleFragment wpSampleFragment);
    void inject(bd.com.aristo.edcr.modules.wp.fragment.GiftFragment wpGiftFragment);
    void inject(bd.com.aristo.edcr.modules.wp.fragment.WPInternViewPager wpInternViewPager);
    void inject(bd.com.aristo.edcr.modules.wp.fragment.InternGiftFragment internGiftFragment);
    void inject(bd.com.aristo.edcr.modules.wp.fragment.InternSampleFragment internSampleFragment);

    void inject(SampleStatementFragment sampleStatementFragment);
    void inject(SampleProductFragment sampleProductFragment);
    void inject(SelectiveProductFragment selectiveProductFragment);
    void inject(GiftItemFragment giftItemFragment);
    void inject(InternItemFragment internItemFragment);
    void inject(ColorInfoTDPGDialog colorInfoTDPGDialog);

    void inject(BillStatementActivity billActivity);
    void inject(DetailsFragment detailsFragment);
    void inject(BillListFragment billListFragment);

    void inject(ExistDoctorsListActivity existDoctorsListActivity);


    void inject(DCRActivity DCRActivity);
    void inject(DCRSummaryListActivity dcrSummaryListActivity);
    void inject(AddAccompanyDialog addAccompanyDialog);
    void inject(DoctorListFragment doctorListFragment);
    void inject(DoctorListUnplanFragment doctorListUnplanFragment);
    void inject(AccompanyActivity accompanyActivity);
    void inject(NewDcrInternFragment newDcrInternFragment);
    void inject(NewDoctorFragment newDoctorFragment);
    void inject(OthersFragment othersFragment);
    void inject(ExistingDoctorFragment existingDoctorFragment);
    void inject(DCRViewPager dcrViewPager);
    void inject(DoctorListViewPager doctorListViewPager);
    void inject(PromotedFragment promotedFragment);
    void inject(SampleFragment sampleFragment);
    void inject(GiftFragment giftFragment);
    void inject(NewSampleFragment newSampleFragment);
    void inject(NewGiftFragment newGiftFragment);
    void inject(NewPromotedFragment newPromotedFragment);
    void inject(NewDCRListFragment newDCRListFragment);
    void inject(DCRUploadDialog dcrUploadDialog);
    void inject(InternGiftFragment internGiftFragment);
    void inject(InternDCRViewPager internDCRViewPager);
    void inject(InternSampleFragment internSampleFragment);
    void inject(DCRUnCoverFragment dcrUnCoverFragment);
    void inject(DCRDetailFragment dcrDetailFragment);
    void inject(DCRNewFragment dcrNewFragment);
    void inject(DCRCalendarActivity dcrCalendarActivity);
    void inject(bd.com.aristo.edcr.modules.dcr.newdcr.fragment.AddSampleItemActivity addSampleItemActivity);
    void inject(DCRColorInfoDialog dcrColorInfoDialog);

    void inject(DCRDoctorListFragment DCRDoctorListFragment);
    void inject(NoDCRDoctorListFragment noDCRDoctorListFragment);
    void inject(UncoverDotFragment uncoverDotFragment);
    void inject(DVRSummaryFragment dvrSummaryFragment);
    void inject(PWDSReportFragment pwdsReportFragment);
    void inject(WorkPlanSummaryFragment workPlanSummaryFragment);
    void inject(DoctorCoverageFragment doctorCoverageFragment);
    void inject(DVRActivity.BackgroundTask backgroundTask);
    void inject(ColorInfoDialog colorInfoDialog);
    void inject(ColorInfoWPDialog colorInfoWPDialog);
    void inject(ColorInfoDCRDialog colorInfoDCRDialog);
    void inject(DCRAccompanyFragment dcrAccompanyFragment);
    void inject(PhysicalStockFragment physicalStockFragment);
    void inject(DCRMPOFragment dcrmpoFragment);
    void inject(DoctorWiseItemFragment doctorWiseItemFragment);
    void inject(DatePickerFragment datePickerFragment);
    void inject(InternRestUploadDialog datePickerFragment);
    void inject(DoctorWiseDOTFragment doctorWiseDOTFragment);


    void inject(ProductSynJob productSynJob);
    void inject(FCMSendNotification fcmSendNotification);


    void inject(RealmUtils realmUtils);

    final class Initializer {
        private Initializer(){}

        public static AppComponent init(App app, RequestServices requestServices){
            return DaggerAppComponent.builder().appModule(new AppModule(app, requestServices)).build();
        }
    }

}
