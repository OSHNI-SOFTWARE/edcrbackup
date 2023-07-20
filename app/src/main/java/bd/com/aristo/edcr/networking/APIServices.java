package bd.com.aristo.edcr.networking;

import bd.com.aristo.edcr.models.db.DoctorsModel;
import bd.com.aristo.edcr.models.db.TokenModel;
import bd.com.aristo.edcr.models.response.LoginResponse;
import bd.com.aristo.edcr.models.db.NotificationModel;
import bd.com.aristo.edcr.models.db.ProductModel;
import bd.com.aristo.edcr.models.response.PostResponse;
import bd.com.aristo.edcr.models.response.ResponseDetail;
import bd.com.aristo.edcr.models.response.ResponseMaster;
import bd.com.aristo.edcr.models.response.ResponseTADA;
import bd.com.aristo.edcr.models.response.ResponseTypeDCRDetail;
import bd.com.aristo.edcr.models.response.VersionResponse;
import bd.com.aristo.edcr.modules.bill.model.Bill;
import bd.com.aristo.edcr.modules.dcr.accompany.AccompanyServerModel;
import bd.com.aristo.edcr.modules.dcr.dcr.model.DCRListModel;
import bd.com.aristo.edcr.modules.dcr.dcr.model.DCRModelForServer;
import bd.com.aristo.edcr.modules.dcr.dcr.model.DCRModelServer;
import bd.com.aristo.edcr.modules.dcr.dcr.model.DCRUncoveredListModel;
import bd.com.aristo.edcr.modules.dcr.dcr.model.IPlanExeModel;
import bd.com.aristo.edcr.modules.dvr.model.GetDVR;
import bd.com.aristo.edcr.modules.gwds.GWDServerModel;
import bd.com.aristo.edcr.modules.pwds.PWDServerModel;
import bd.com.aristo.edcr.modules.reports.model.AbsentReport;
import bd.com.aristo.edcr.modules.reports.model.DCRAccompany;
import bd.com.aristo.edcr.modules.reports.model.DoctorCoverage;
import bd.com.aristo.edcr.modules.reports.model.DoctorWiseItemModel;
import bd.com.aristo.edcr.modules.reports.model.IDCRMPOModel;
import bd.com.aristo.edcr.modules.reports.model.IDOTExecution;
import bd.com.aristo.edcr.modules.reports.model.PhysicalStockGet;
import bd.com.aristo.edcr.modules.reports.model.Uncover;
import bd.com.aristo.edcr.modules.reports.ss.model.ItemExecuteModel;
import bd.com.aristo.edcr.modules.reports.ss.model.ItemStatementModel;
import bd.com.aristo.edcr.modules.reports.ss.model.NewDoctorDCR;
import bd.com.aristo.edcr.modules.reports.ss.model.SampleStatementModel;
import bd.com.aristo.edcr.modules.tp.model.TPModel;
import bd.com.aristo.edcr.modules.wp.model.WPForGet;
import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by monir.sobuj on 5/25/17.
 */

public interface APIServices {

    String TAG = APIServices.class.getSimpleName();

    @GET("EligiblePolicy")
    Observable<Boolean> isEligible(@Query("OperationMode") String mode,
                                   @Query("OperationDate") String date);
    @GET("EligibleMarket")
    Observable<Boolean> checkEmpMarket(@Query("EmpCode") String empCode,
                                   @Query("UserID") String mpLoc,
                                   @Query("MarketCode") String marketCode,
                                   @Query("Token") String fcmToken);

    @GET("Json/GetDateTimeMilliseconds")
    Observable<String> getServerMillis();
    @GET("Json/GetAppVersion")
    Observable<VersionResponse> getAppVersion(@Query("AppType") String appType);

    @GET("Json/GetToken")
    Observable<ResponseDetail<TokenModel>> getToken(@Query("UserID") String UserID,
                                                    @Query("Designation") String designation);

    /*@GET("login")
    Observable<LoginResponse> login(@Query("employee_number") String UserID,
                                    @Query("Password") String password,
                                    @Query("Token") String deviceToken);*/

    @FormUrlEncoded
    @POST("login")
    Observable<LoginResponse> login(@Field("employee_number") String phone,
                                    @Field("password") String password);

    @GET("ChangePassword")
    Observable<LoginResponse> changePassword(@Query("UserID") String UserID,
                                             @Query("OldPassword") String oldPassword,
                                             @Query("NewPassword") String newPassword,
                                             @Query("Token") String deviceToken);
    @FormUrlEncoded
    @POST("PostPushNotification")
    Observable<ResponseDetail<String>> postNotification(
            @Field("MpGroup") String userGroup,
            @Field("OperationType") String operationType,
            @Field("Title") String title,
            @Field("Message") String msg,
            @Field("Year") String year,
            @Field("MonthNumber") String month,
            @Field("SetLocCode") String locCode);
    @GET("GetNotification")
    Observable<ResponseDetail<NotificationModel>> getNotification(@Query("UserID") String UserID);

    //Basic data
    @GET("GetDAnTA")
    Observable<ResponseTADA> getDANature(@Query("UserID") String UserID);

    @GET("GetInvItems")
    Observable<ResponseDetail<ProductModel>> getProducts(@Query("UserID") String UserID);

    @GET("GetInvItemsMonthly")
    Observable<ResponseDetail<ProductModel>> getProductsBeforeDCR(@Query("UserID") String UserID,
                                                                  @Query("MonthYear") String monthYear);

    @GET("GetDoctorList")
    Observable<ResponseDetail<DoctorsModel>> getDoctors(@Query("UserID") String UserID,
                                                        @Query("MarketCode") String marketCode);
    @FormUrlEncoded
    @POST("PostDoctorList")
    Observable<ResponseDetail<String>> postDoctor(@Field("JsonString") String JsonString);

    @GET("AccompanyList")
    Observable<ResponseDetail<AccompanyServerModel>> getMpoInfo(@Query("UserID") String UserID);



    // operational data

    @FormUrlEncoded
    @POST("PostTourPlanBody") // TourPlaning2?UserID=301&Year=2018&&MonthNumber=January&JsonString=sds
    Observable<ResponseDetail<String>> sendTPBody(@Field("UserID") String UserID,
                                                  @Field("Year") String Year,
                                                  @Field("MonthNumber") String MonthNumber,
                                                  @Field("JsonString") String JsonString);

    @FormUrlEncoded
    @POST("PostReviewTourPlanBody") // TourPlaning2?UserID=301&Year=2018&&MonthNumber=January&JsonString=sds
    Observable<ResponseDetail<String>> sendChangedTPBody(@Field("UserID") String UserID,
                                                         @Field("Year") String Year,
                                                         @Field("MonthNumber") String MonthNumber,
                                                         @Field("JsonString") String JsonString);

    @GET("GetTourPlan") // TourPlaning2?UserID=301&Year=2018&&MonthNumber=January&JsonString=sds GetTourPlaning?UserID=301&Year=2018&&MonthNumber=January
    Observable<ResponseDetail<TPModel>> getTP(@Query("UserID") String UserID,
                                             @Query("Year") String Year,
                                             @Query("MonthNumber") String MonthNumber);

    @GET("GetDVR") // TourPlaning2?UserID=301&Year=2018&&MonthNumber=January&JsonString=sds GetTourPlaning?UserID=301&Year=2018&&MonthNumber=January
    Observable<ResponseDetail<GetDVR>> getDVR(@Query("UserID") String UserID,
                                              @Query("Year") String Year,
                                              @Query("MonthNumber") String MonthNumber);

    @FormUrlEncoded
    @POST("PostDVRBody") // TourPlaning2?UserID=301&Year=2018&&MonthNumber=January&JsonString=sds GetTourPlaning?UserID=301&Year=2018&&MonthNumber=January
    Observable<ResponseDetail<String>> postDVRBody(@Field("UserID") String UserID,
                                                   @Field("Year") String Year,
                                                   @Field("MonthNumber") String MonthNumber,
                                                   @Field("JsonString") String jsonString);
    @FormUrlEncoded
    @POST("PostReviewDVRBody") // TourPlaning2?UserID=301&Year=2018&&MonthNumber=January&JsonString=sds GetTourPlaning?UserID=301&Year=2018&&MonthNumber=January
    Observable<ResponseDetail<String>> postReviewDVRBody(@Field("UserID") String UserID,
                                                         @Field("Year") String Year,
                                                         @Field("MonthNumber") String MonthNumber,
                                                         @Field("JsonString") String jsonString);
    @FormUrlEncoded
    @POST("postPWDS")
    Observable<ResponseMaster<PWDServerModel>> postPWDS(@Field("UserID") String UserID,
                                                        @Field("Year") String Year,
                                                        @Field("MonthNumber") String MonthNumber,
                                                        @Field("JsonString") String jsonString);

    @FormUrlEncoded
    @POST("PostReviewPWDS")
    Observable<ResponseMaster<PWDServerModel>> postChangePWDS(@Field("UserID") String UserID,
                                                              @Field("Year") String Year,
                                                              @Field("MonthNumber") String MonthNumber,
                                                              @Field("JsonString") String jsonString);

    @GET("getPWDS")
    Observable<ResponseMaster<PWDServerModel>> getPWDS(@Query("UserID") String UserID,
                                                       @Query("Year") String Year,
                                                       @Query("MonthNumber") String MonthNumber);

    @FormUrlEncoded
    @POST("PostGWDS")
    Observable<ResponseMaster<GWDServerModel>> postGWDS(@Field("UserID") String UserID,
                                                        @Field("Year") String Year,
                                                        @Field("MonthNumber") String MonthNumber,
                                                        @Field("JsonString") String jsonString);

    @FormUrlEncoded
    @POST("PostReviewGWDS")
    Observable<ResponseMaster<GWDServerModel>> postChangeGWDS(@Field("UserID") String UserID,
                                                              @Field("Year") String Year,
                                                              @Field("MonthNumber") String MonthNumber,
                                                              @Field("JsonString") String jsonString);

    @GET("GetGWDS")
    Observable<ResponseMaster<GWDServerModel>> getGWDS(@Query("UserID") String UserID,
                                                       @Query("Year") String Year,
                                                       @Query("MonthNumber") String MonthNumber);

    @GET("PostIntern")
    Observable<PostResponse> postIntern(@Query("UserID") String UserID,
                                        @Query("Date") String date,
                                        @Query("ShiftName") String ShiftName,
                                        @Query("InternID") String InternID,
                                        @Query("InstName") String InstName,
                                        @Query("Ward") String Ward,
                                        @Query("TeamLeader") String TeamLeader,
                                        @Query("ContactNo") String ContactNo,
                                        @Query("TeamVolume") String TeamVolume);

    @FormUrlEncoded
    @POST("PostWorkPlan")
    Observable<ResponseDetail<String>> postWorkPlan(@Field("UserID") String UserID,
                                                    @Field("Date") String Date,
                                                    @Field("JsonString") String JsonString);

    @FormUrlEncoded
    @POST("PostWorkPlanSingle")
    Observable<ResponseDetail<String>> postWorkPlanPlain(@Field("UserID") String UserID,
                                                    @Field("Date") String Date,
                                                    @Field("JsonString") String JsonString);

    /*@GET("Json/GetWorkPlan")
    Observable<ResponseMaster<WPDetailListModel>> getWorkPlan(@Query("UserID") String UserID,
                                                              @Query("Date") String Date);
    @GET("Json/GetMonthlyWorkPlan")
    Observable<ResponseMaster<WPDetailListModel>> getMonthlyWorkPlan(@Query("UserID") String UserID,
                                                                     @Query("MonthYear") String Date);*/
    @GET("GetWorkPlanSingle")
    Observable<ResponseDetail<WPForGet>> getWorkPlan(@Query("UserID") String UserID,
                                                     @Query("Date") String Date);
    @GET("GetMonthlyWorkPlanSingle")
    Observable<ResponseDetail<WPForGet>> getMonthlyWorkPlan(@Query("UserID") String UserID,
                                                            @Query("MonthYear") String Date);

    @FormUrlEncoded
    @POST("PostDCR")
    Observable<ResponseDetail<String>> postDCR(@Field("UserID") String UserID,
                                               @Field("ColleagueMPO") String ColleagueMPO,
                                               @Field("Date") String Date,
                                               @Field("ShiftName") String ShiftName,
                                               @Field("DoctorID") String DoctorID,
                                               @Field("Status") String Status,
                                               @Field("StatusCause") String StatusCause,
                                               @Field("Remarks") String Remarks,
                                               @Field("JsonString") String JsonString);
    @FormUrlEncoded
    //@POST("Json/PostDemoDCR")
    @POST("PostDcrMpo")
    Observable<ResponseDetail<String>> postDemoDCR(@Field("UserID") String UserID,
                                                   @Field("ColleagueMPO") String ColleagueMPO,
                                                   @Field("Date") String Date,
                                                   @Field("ShiftName") String ShiftName,
                                                   @Field("DoctorID") String DoctorID,
                                                   @Field("Status") String Status,
                                                   @Field("StatusCause") String StatusCause,
                                                   @Field("Remarks") String Remarks,
                                                   @Field("DcrType") String type,
                                                   @Field("DcrTypeCause") String subType,
                                                   @Field("JsonString") String JsonString);


    @FormUrlEncoded
    //@POST("Json/PostRawDCR")
    @POST("PostRawDcrMpo")
    Observable<ResponseDetail<String>> postRawDCR(@Field("UserID") String UserID,
                                                  @Field("ColleagueMPO") String AccompanyID,//Accompany ID
                                                  @Field("Date") String Date,
                                                  @Field("ShiftName") String ShiftName,
                                                  @Field("DoctorID") String DoctorID, //doctor ID or doctor name
                                                  @Field("Remarks") String Remarks,
                                                  @Field("Ward") String ward, //address, ward ,
                                                  @Field("TeamLeader") String TeamLeader,
                                                  @Field("ContactNo") String ContactNo, //contact
                                                  @Field("TeamVolume") String TeamVolume,
                                                  @Field("DcrType") String type,
                                                  @Field("DcrTypeCause") String subType,
                                                  @Field("JsonString") String JsonString);

    /*@FormUrlEncoded
    @POST("Json/PostDemoRawDCR")
    Observable<ResponseDetail<String>> postDemoRawDCR(@Field("UserID") String UserID,
                                                      @Field("ColleagueMPO") String ColleagueMPO,
                                                      @Field("Date") String Date,
                                                      @Field("ShiftName") String ShiftName,
                                                      @Field("DoctorID") String DoctorID,
                                                      @Field("Remarks") String Remarks,
                                                      @Field("Ward") String Ward,
                                                      @Field("TeamLeader") String TeamLeader,
                                                      @Field("ContactNo") String ContactNo,
                                                      @Field("TeamVolume") String TeamVolume,
                                                      @Field("Latitude") String Latitude,
                                                      @Field("Longitude") String Longitude,
                                                      @Field("Address") String Address,
                                                      @Field("JsonString") String JsonString);*/

    /*@FormUrlEncoded
    @POST("Json/PostBillBody")
    Observable<ResponseDetail<String>> sendBill(@Field("UserID") String UserID,
                                                @Field("MPOCode") String MPOCode,
                                                @Field("Year") String year,
                                                @Field("MonthNumber") String month,
                                                @Field("DayNumber") String day,
                                                @Field("DA") String da,
                                                @Field("TA") String ta,
                                                @Field("TAOther") String taOther,
                                                @Field("Other") String other);*/
    @FormUrlEncoded
    @POST("PostBodyExpenseBillMpo") //http://localhost:1290/DCRService.svc/Json/PostBodyExpenseBillMpo
    Observable<ResponseDetail<String>> sendBill(@Field("UserID") String UserID,
                                                @Field("MPOCode") String MPOCode,
                                                @Field("EmpName") String name,
                                                @Field("Designation") String deg,
                                                @Field("Year") String year,
                                                @Field("MonthNumber") String month,
                                                @Field("DayNumber") String day,
                                                @Field("DA") String da,
                                                @Field("TA") String ta,
                                                @Field("TAOther") String taOther,
                                                @Field("Other") String other);
    //http://localhost:1290/DCRService.svc/Json/GetMpoTmRsmExpenseBillSummary?UserID=H61R2C&Designation=MPO&EmpCode=10907&Year=2020&MonthNumber=05
    @GET("GetMpoTmRsmExpenseBillSummary")
    Observable<ResponseDetail<Bill>> getMonthlyBill(@Query("UserID") String UserID,
                                                    @Query("Designation") String designation,
                                                    @Query("EmpCode") String MPOCode,
                                                    @Query("Year") String year,
                                                    @Query("MonthNumber") String month);
    /*@GET("Json/GetExpenseBill")
    Observable<ResponseTypeBill<Others, TA, OtherTA>> getDailyBill(@Query("UserID") String UserID,
                                                                   @Query("MPOCode") String MPOCode,
                                                                   @Query("Year") String year,
                                                                   @Query("MonthNumber") String month,
                                                                   @Query("DayNumber") String day);*/


    //Report

    @GET("GetDCRItemExecution")
    Observable<ResponseDetail<DoctorWiseItemModel>> getDoctorWiseItem(@Query("UserID") String UserID,
                                                                      @Query("MonthYear") String MonthYear,
                                                                      @Query("DoctorID") String doctorID);
    @GET("GetDcrSummaryMPO")
    Observable<ResponseDetail<IDCRMPOModel>> getDCRMonthlyForMPO(@Query("UserID") String UserID,
                                                                 @Query("MonthYear") String monthYear);
    @GET("GetInvItemsPhysicalStockCheck")
    Observable<ResponseDetail<PhysicalStockGet>> getPhysicalStock(@Query("UserID") String UserID,  // MPO UserID
                                                                  @Query("Designation") String designation, // MPO
                                                                  @Query("MonthYear") String monthYear, //
                                                                  @Query("MPGroup") String mpGroup); // MPO UserID
    @GET("GetAccompany")
    Observable<ResponseDetail<DCRAccompany>> getDCRAccompany(@Query("UserID") String UserID,
                                                             @Query("Designation") String designation,
                                                             @Query("MonthYear") String MonthYear);
    @GET("GetDCRReport")
    Observable<ResponseDetail<DCRModelForServer>> getDCRList(@Query("UserID") String UserID,
                                                             @Query("MonthYear") String monthYear);
    @GET("GetDCRItemDetailReport")
    Observable<ResponseTypeDCRDetail<DCRListModel, DCRListModel, DCRUncoveredListModel>> getDCRDetails(@Query("UserID") String UserID,
                                                                                                       @Query("Date") String date);

    @GET("GetItemStatement")
    Observable<ResponseDetail<ItemStatementModel>> getItemStatement(@Query("UserID") String UserID,
                                                                    @Query("Year") String Year,
                                                                    @Query("MonthNumber") String MonthNumber,
                                                                    @Query("ItemType") String ItemType);
    @GET("GetSampleStatement")
    Observable<ResponseDetail<SampleStatementModel>> getSampleStatement(@Query("UserID") String UserID,
                                                                        @Query("Year") String Year,
                                                                        @Query("MonthNumber") String MonthNumber);

    @GET("GetItemExecute")
    Observable<ResponseDetail<ItemExecuteModel>> getItemExecute(@Query("UserID") String UserID,
                                                                @Query("Year") String Year,
                                                                @Query("MonthNumber") String MonthNumber,
                                                                @Query("ProductCode") String ProductCode);

    @GET("ReportPlanVsExecution")
    Observable<ResponseDetail<IPlanExeModel>> getPlanVsExe(@Query("UserID") String UserID,
                                                           @Query("MonthYear") String monthYear);
    @GET("GetMonthlyDCR")
    Observable<ResponseMaster<DCRModelServer>> getDCRFromServer(@Query("UserID") String UserID,
                                                                @Query("MonthYear") String monthYear);

    @GET("GetUnCoveredDoctor")
    Observable<ResponseMaster<Uncover>> getUncovered(@Query("UserID") String UserID,
                                                     @Query("MonthNumber") String MonthNumber,
                                                     @Query("Year") String Year);

    @GET("GetDoctorCoverageNew")
    Observable<ResponseDetail<DoctorCoverage>> getDoctorCoverage(@Query("UserID") String UserID,
                                                                 @Query("Year") String Year,
                                                                 @Query("MonthNumber") String MonthNumber);

    @GET("GetDoctorAbsentDcr")
    Observable<ResponseDetail<AbsentReport>> getDCRAbsentReport(@Query("UserID") String UserID,
                                                                @Query("Designation") String designation,
                                                                @Query("FromDate") String startDate,
                                                                @Query("ToDate") String endDate);

    @GET("GetDvrReport")
    Observable<ResponseDetail<IDOTExecution>> getDOTReport(@Query("UserID") String UserID,
                                                           @Query("Year") String year,
                                                           @Query("MonthNumber") String monthNumber);
    @GET("GetNewDcrNewDoctor")
    Observable<ResponseDetail<NewDoctorDCR>> getNewDoctorDCR(@Query("UserID") String UserID,
                                                          @Query("Year") String year,
                                                          @Query("MonthNumber") String monthNumber);


}
