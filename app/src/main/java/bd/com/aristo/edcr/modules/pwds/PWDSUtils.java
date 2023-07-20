package bd.com.aristo.edcr.modules.pwds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import bd.com.aristo.edcr.models.db.DoctorsModel;
import bd.com.aristo.edcr.models.db.ProductModel;
import bd.com.aristo.edcr.utils.constants.StringConstants;
import io.realm.Realm;

/**
 * Created by monir.sobuj on 11/2/2017.
 */

public class PWDSUtils {


    public static List<ProductModel> getPWDSProducts(Realm realm, int year, int month) {

        return realm.where(ProductModel.class)
                .equalTo(ProductModel.COL_MONTH, month)
                .equalTo(ProductModel.COL_YEAR, year)
                .equalTo(ProductModel.COL_ITEM_FOR, StringConstants.ITEM_FOR_REGULAR)
                .equalTo(ProductModel.COL_ITEM_TYPE, StringConstants.SELECTED_ITEM)
                .findAll();

    }

    public static List<PWDSProductsModel> getPwdsProducts(Realm r, int month, int year){

        List<PWDSProductsModel> pwdsProductsModels = new ArrayList<>();
        int planned = 0;
        for(ProductModel pm : getPWDSProducts(r, year,month)){

            List<PWDSModel> pwdsModels = r.where(PWDSModel.class)
                    .equalTo(PWDSModel.COL_PRODUCT_ID, pm.getCode())
                    .equalTo(PWDSModel.COL_MONTH, month)
                    .equalTo(PWDSModel.COL_YEAR, year)
                    .findAll();
            PWDSProductsModel pwdsProductsModel = new PWDSProductsModel();
            pwdsProductsModel.setCode(pm.getCode());
            pwdsProductsModel.setName(pm.getName()+"("+pm.getPackSize()+")");
            pwdsProductsModel.setGeneric(pm.getGeneric());
            pwdsProductsModel.setQuantity(pm.getQuantity());
            pwdsProductsModel.setPlanned(pwdsModels != null ? pwdsModels.size() : 0);
            pwdsProductsModel.setApproved((pwdsModels != null && pwdsModels.size() > 0) && pwdsModels.get(0).isApproved());
            pwdsProductsModel.setUploaded((pwdsModels != null && pwdsModels.size() > 0) && pwdsModels.get(0).isUploaded());
            pwdsProductsModels.add(pwdsProductsModel);
        }
        return pwdsProductsModels;
    }

    /*public static int getPlannedCount(Realm r, ProductModel pm, int month, int year){
        List<PWDSModel> pwdsModels = r.where(PWDSModel.class)
                .equalTo(PWDSModel.COL_PRODUCT_ID, pm.getCode())
                .equalTo(PWDSModel.COL_MONTH, month)
                .equalTo(PWDSModel.COL_YEAR, year)
                .findAll();
        return pwdsModels.size();
    }

    public static boolean isApproved(Realm r, ProductModel pm, int month, int year){
        PWDSModel pwdsModels = r.where(PWDSModel.class)
                .equalTo(PWDSModel.COL_PRODUCT_ID, pm.getCode())
                .equalTo(PWDSModel.COL_MONTH, month)
                .equalTo(PWDSModel.COL_YEAR, year)
                .findFirst();
        if(pwdsModels != null){
            return pwdsModels.isApproved();
        }
        return false;
    }
*/




    public static List<PWDSDoctorsModel> getPwdsDoctors(List<DoctorsModel> doctorsModels, Realm r, String productCode, int month, int year){


        List<PWDSDoctorsModel> tempDoctorsModels = new ArrayList<>();
        List<PWDSDoctorsModel> pwdsDoctorsModels = new ArrayList<>();
        List<PWDSDoctorsModel> nonSelectedDoctorsModels = new ArrayList<>();
        int planned = 0;
        for(DoctorsModel dm : doctorsModels){
            if(!dm.getId().contains(StringConstants.ITEM_FOR_INTERN)) {
                PWDSDoctorsModel pwdsDoctorsModel = new PWDSDoctorsModel();
                pwdsDoctorsModel.setDegree(dm.getDegree());
                pwdsDoctorsModel.setAddress(dm.getAddress());
                pwdsDoctorsModel.setName(dm.getName());
                pwdsDoctorsModel.setSpecial(dm.getSpecial());
                pwdsDoctorsModel.setId(dm.getId());
                pwdsDoctorsModel.setPwds(getPwds(r, dm, productCode, month, year));
                tempDoctorsModels.add(pwdsDoctorsModel);
            }
        }
        Collections.sort(tempDoctorsModels, new Comparator<PWDSDoctorsModel>() {
            @Override
            public int compare(PWDSDoctorsModel pwdsDoctorsModel, PWDSDoctorsModel t1) {
                return pwdsDoctorsModel.getName().compareTo(t1.getName());
            }
        });
        nonSelectedDoctorsModels.addAll(tempDoctorsModels);
        for(PWDSDoctorsModel tempList:tempDoctorsModels){
            if(tempList.isPwds()){
                pwdsDoctorsModels.add(tempList);
                nonSelectedDoctorsModels.remove(tempList);
            }
        }
        pwdsDoctorsModels.addAll(nonSelectedDoctorsModels);


        return pwdsDoctorsModels;
    }

    public static boolean getPwds(Realm r, DoctorsModel dm, String productCode, int month, int year){
        List<PWDSModel> pwdsModels = r.where(PWDSModel.class)
                .equalTo(PWDSModel.COL_PRODUCT_ID, productCode)
                .equalTo(PWDSModel.COL_DOCTOR_ID, dm.getId())
                .equalTo(PWDSModel.COL_MONTH, month)
                .equalTo(PWDSModel.COL_YEAR, year)
                .findAll();
        return pwdsModels.size()>0;
    }


    public static List<PWDServerModel> getProductWithDoctorsList(List<ProductModel> productModelList, Realm r, int month, int year){

        List<PWDServerModel> pWDServerModels = new ArrayList<>();

        for(ProductModel dm : productModelList){

            PWDServerModel pWDServerModel = new PWDServerModel();

            pWDServerModel.setProductCode(dm.getCode());

            List<PWDSModel> list = r.where(PWDSModel.class)
                    .equalTo(PWDSModel.COL_MONTH, month)
                    .equalTo(PWDSModel.COL_YEAR, year)
                    .equalTo(PWDSModel.COL_PRODUCT_ID,dm.getCode())
                    .equalTo(PWDSModel.COL_IS_APPROVED, false)
                    .findAll();

            List<PWDServerDoctorModel> pwdServerDoctorModelsList=new ArrayList<>();
             for (PWDSModel pwdsModel:list){
                 PWDServerDoctorModel pwdServerDoctorModel=new PWDServerDoctorModel();
                 pwdServerDoctorModel.setDoctorID(pwdsModel.getDoctorID());
                 pwdServerDoctorModel.setApprovalStatus(pwdsModel.isApproved()?StringConstants.APPROVED_STATUS_APPROVED:StringConstants.APPROVED_STATUS_PENDING);
                 pwdServerDoctorModelsList.add(pwdServerDoctorModel);
             }
             pWDServerModel.setList(pwdServerDoctorModelsList);
            if(list.size() > 0){
                pWDServerModels.add(pWDServerModel);
            }
        }


        return pWDServerModels;
    }

    public static void updatePWDSAfterUpload(final List<ProductModel> productModelList, Realm r,
                                             final int month, final int year){

        r.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for(ProductModel productModel : productModelList){

                    List<PWDSModel> list = realm.where(PWDSModel.class)
                            .equalTo(PWDSModel.COL_MONTH, month)
                            .equalTo(PWDSModel.COL_YEAR, year)
                            .equalTo(PWDSModel.COL_PRODUCT_ID, productModel.getCode())
                            .equalTo(PWDSModel.COL_IS_APPROVED, false)
                            .findAll();

                    for (PWDSModel pWDSModel:list){
                        pWDSModel.setUploaded(true);
                        realm.insertOrUpdate(pWDSModel);
                    }
                }
            }
        });

    }

    public static void updatePWDS(List<PWDServerModel> pwdServerModelList,Realm realm, int month, int year){
        if(!realm.isInTransaction())
            realm.beginTransaction();
        realm.where(PWDSModel.class)
                .equalTo(PWDSModel.COL_MONTH, month)
                .equalTo(PWDSModel.COL_YEAR, year)
                .findAll().deleteAllFromRealm();
        for (PWDServerModel pwdServerModel:pwdServerModelList){

            for (PWDServerDoctorModel pwdsDoctorsModel:pwdServerModel.getList()){
                PWDSModel pwdsModel = new PWDSModel();
                pwdsModel.setId(getPrevId(realm));
                pwdsModel.setDoctorID(pwdsDoctorsModel.getDoctorID());
                pwdsModel.setMonth(month);
                pwdsModel.setYear(year);
                pwdsModel.setProductID(pwdServerModel.getProductCode());
                pwdsModel.setUploaded(true);
                if(pwdsDoctorsModel.getApprovalStatus().equalsIgnoreCase(StringConstants.APPROVED_STATUS_APPROVED)){
                    pwdsModel.setApproved(true);
                } else {
                    pwdsModel.setApproved(false);
                }

                realm.insertOrUpdate(pwdsModel);

            }
        }
        try{
            realm.commitTransaction();
        }catch(Exception e){
            if(realm.isInTransaction()){
                realm.cancelTransaction();
            }
        }


    }

    public static long getPrevId(Realm realm){
        Number currentIdNum = realm.where(PWDSModel.class).max(PWDSModel.COL_ID);
        int nextId;
        if(currentIdNum == null) {
            nextId = 1;
        } else {
            nextId = currentIdNum.intValue() + 1;
        }
        return  nextId;
    }

    public static boolean isApproved(Realm realm, int month, int year){
        return realm.where(PWDSModel.class)
                .equalTo(PWDSModel.COL_IS_APPROVED, true)
                .equalTo(PWDSModel.COL_MONTH, month)
                .equalTo(PWDSModel.COL_YEAR, year)
                .findAll().size() > 0;
    }


}
