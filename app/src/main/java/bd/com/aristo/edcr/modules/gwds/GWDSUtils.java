package bd.com.aristo.edcr.modules.gwds;

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

public class GWDSUtils {

    public static List<ProductModel> getGWDSProducts(Realm realm, int year, int month) {
        return realm.where(ProductModel.class)
                .equalTo(ProductModel.COL_YEAR, year)
                .equalTo(ProductModel.COL_MONTH, month)
                .equalTo(ProductModel.COL_ITEM_TYPE, StringConstants.GIFT_ITEM)
                .equalTo(ProductModel.COL_ITEM_FOR, StringConstants.ITEM_FOR_REGULAR)
                .findAll();
    }

    public static List<GWDSGiftModel> getGWDSGifts(Realm r, int month, int year){

        List<GWDSGiftModel> gwdsProductModels = new ArrayList<>();
        int planned = 0;
        for(ProductModel gm : getGWDSProducts(r, year, month)){
            List<GWDSModel> gwdsModels = r.where(GWDSModel.class)
                    .equalTo(GWDSModel.COL_GIFT_ID, gm.getCode())
                    .equalTo(GWDSModel.COL_MONTH, month)
                    .equalTo(GWDSModel.COL_YEAR, year)
                    .findAll();
            GWDSGiftModel gwdsProductModel = new GWDSGiftModel();
            String name = gm.getName() +"("+ gm.getPackSize() +")";
            gwdsProductModel.setCode(gm.getCode());
            gwdsProductModel.setName(name);
            gwdsProductModel.setQuantity(gm.getQuantity());
            gwdsProductModel.setPlanned(gwdsModels != null ? gwdsModels.size() : 0);
            gwdsProductModel.setApproved((gwdsModels != null && gwdsModels.size() > 0) && gwdsModels.get(0).isApproved());
            gwdsProductModel.setUploaded((gwdsModels != null && gwdsModels.size() > 0) && gwdsModels.get(0).isUploaded());
            gwdsProductModels.add(gwdsProductModel);
        }
        return gwdsProductModels;
    }

    /*public static int getPlannedCount(Realm r, ProductModel gm, int month, int year){
        List<GWDSModel> gwdsModels = r.where(GWDSModel.class)
                .equalTo(GWDSModel.COL_GIFT_ID, gm.getCode())
                .equalTo(GWDSModel.COL_MONTH, month)
                .equalTo(GWDSModel.COL_YEAR, year)
                .findAll();
        return gwdsModels.size();
    }

    public static boolean isApproved(Realm r, ProductModel gm, int month, int year){
        GWDSModel gwdsModels = r.where(GWDSModel.class)
                .equalTo(GWDSModel.COL_GIFT_ID, gm.getCode())
                .equalTo(GWDSModel.COL_MONTH, month)
                .equalTo(GWDSModel.COL_YEAR, year)
                .findFirst();
        if(gwdsModels != null){
            return gwdsModels.isApproved();
        }
        return false;
    }

    public static boolean isUploaded(Realm r, ProductModel gm, int month, int year){
        GWDSModel gwdsModels = r.where(GWDSModel.class)
                .equalTo(GWDSModel.COL_GIFT_ID, gm.getCode())
                .equalTo(GWDSModel.COL_MONTH, month)
                .equalTo(GWDSModel.COL_YEAR, year)
                .findFirst();
        if(gwdsModels != null){
            return gwdsModels.isUploaded();
        }
        return false;
    }*/

    public static List<GWDSDoctorsModel> getGWDSDoctors(List<DoctorsModel> doctorsModels, Realm r,
                                                        String giftCode, int month, int year){

        List<GWDSDoctorsModel> tempDoctorsModels = new ArrayList<>();
        List<GWDSDoctorsModel> nonSelectedDoctorsModels = new ArrayList<>();
        List<GWDSDoctorsModel> gwdsDoctorsModels = new ArrayList<>();
        int planned = 0;
        for(DoctorsModel dm : doctorsModels){
            if(!dm.getId().contains(StringConstants.ITEM_FOR_INTERN)) {
                GWDSDoctorsModel gwdsDoctorsModel = new GWDSDoctorsModel();
                gwdsDoctorsModel.setDegree(dm.getDegree());
                gwdsDoctorsModel.setAddress(dm.getAddress());
                gwdsDoctorsModel.setName(dm.getName());
                gwdsDoctorsModel.setSpecial(dm.getSpecial());
                gwdsDoctorsModel.setId(dm.getId());
                gwdsDoctorsModel.setGwds(getGWDS(r, dm, giftCode, month, year));
                tempDoctorsModels.add(gwdsDoctorsModel);
            }
        }

        Collections.sort(tempDoctorsModels, new Comparator<GWDSDoctorsModel>() {
            @Override
            public int compare(GWDSDoctorsModel gwdsDoctorsModel, GWDSDoctorsModel t1) {
                return gwdsDoctorsModel.getName().compareTo(t1.getName());
            }
        });
        nonSelectedDoctorsModels.addAll(tempDoctorsModels);
        for(GWDSDoctorsModel tempList:tempDoctorsModels){
            if(tempList.isGwds()){
                gwdsDoctorsModels.add(tempList);
                nonSelectedDoctorsModels.remove(tempList);
            }
        }
        gwdsDoctorsModels.addAll(nonSelectedDoctorsModels);
        return gwdsDoctorsModels;
    }

    public static boolean getGWDS(Realm r, DoctorsModel dm, String giftCode, int month, int year){
        List<GWDSModel> gwdsModels = r.where(GWDSModel.class)
                .equalTo(GWDSModel.COL_GIFT_ID, giftCode)
                .equalTo(GWDSModel.COL_DOCTOR_ID, dm.getId())
                .equalTo(GWDSModel.COL_MONTH, month)
                .equalTo(GWDSModel.COL_YEAR, year)
                .findAll();
        return gwdsModels.size()>0;
    }


    public static List<GWDServerModel> getProductWithDoctorsList(List<ProductModel> ProductModelList, Realm r,
                                                                 int month, int year){

        List<GWDServerModel> gWDServerModels = new ArrayList<>();

        for(ProductModel ProductModel : ProductModelList){

            GWDServerModel gWDServerModel = new GWDServerModel();

            gWDServerModel.setProductCode(ProductModel.getCode());

            List<GWDSModel> list = r.where(GWDSModel.class)
                    .equalTo(GWDSModel.COL_MONTH, month)
                    .equalTo(GWDSModel.COL_YEAR, year)
                    .equalTo(GWDSModel.COL_GIFT_ID, ProductModel.getCode())
                    .equalTo(GWDSModel.COL_IS_APPROVED, false)
                    .findAll();

            List<GWDServerDoctorModel> gwdServerDoctorModelsList=new ArrayList<>();
            for (GWDSModel gWDSModel:list){
                GWDServerDoctorModel gWDServerDoctorModel=new GWDServerDoctorModel();
                gWDServerDoctorModel.setDoctorID(gWDSModel.getDoctorID());
                gWDServerDoctorModel.setApprovalStatus(gWDSModel.isApproved()?StringConstants.APPROVED_STATUS_APPROVED: StringConstants.APPROVED_STATUS_PENDING);
                gwdServerDoctorModelsList.add(gWDServerDoctorModel);
            }
            gWDServerModel.setList(gwdServerDoctorModelsList);
            if(list.size() > 0){
                gWDServerModels.add(gWDServerModel);
            }
        }


        return gWDServerModels;
    }

    public static void updateGWDSAfterUpload(final List<ProductModel> productModelList, Realm r,
                                                                 final int month, final int year){

        r.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for(ProductModel productModel : productModelList){

                    List<GWDSModel> list = realm.where(GWDSModel.class)
                            .equalTo(GWDSModel.COL_MONTH, month)
                            .equalTo(GWDSModel.COL_YEAR, year)
                            .equalTo(GWDSModel.COL_GIFT_ID, productModel.getCode())
                            .equalTo(GWDSModel.COL_IS_APPROVED, false)
                            .findAll();

                    for (GWDSModel gWDSModel:list){
                        gWDSModel.setUploaded(true);
                        realm.insertOrUpdate(gWDSModel);
                    }
                }
            }
        });

    }

    public static void updateGWDS(List<GWDServerModel> gwdServerModelList, Realm realm, int month, int year){
        if(!realm.isInTransaction())
            realm.beginTransaction();

        realm.where(GWDSModel.class)
                .equalTo(GWDSModel.COL_MONTH, month)
                .equalTo(GWDSModel.COL_YEAR, year)
                .findAll().deleteAllFromRealm();

        for (GWDServerModel gwdServerModel:gwdServerModelList){

            for (GWDServerDoctorModel gwdsDoctorsModel:gwdServerModel.getList()){
                GWDSModel gwdsModel = new GWDSModel();
                gwdsModel.setId(getPrevId(realm));
                gwdsModel.setDoctorID(gwdsDoctorsModel.getDoctorID());
                gwdsModel.setMonth(month);
                gwdsModel.setYear(year);
                gwdsModel.setUploaded(true);
                gwdsModel.setGiftID(gwdServerModel.getProductCode());
                if(gwdsDoctorsModel.getApprovalStatus().equalsIgnoreCase(StringConstants.APPROVED_STATUS_APPROVED)){
                    gwdsModel.setApproved(true);
                } else {
                    gwdsModel.setApproved(false);
                }

                realm.insertOrUpdate(gwdsModel);

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
        Number currentIdNum = realm.where(GWDSModel.class).max(GWDSModel.COL_ID);
        int nextId;
        if(currentIdNum == null) {
            nextId = 1;
        } else {
            nextId = currentIdNum.intValue() + 1;
        }
        return  nextId;
    }

    public static boolean isApproved(Realm realm, int month, int year){
        return realm.where(GWDSModel.class)
                .equalTo(GWDSModel.COL_IS_APPROVED, true)
                .equalTo(GWDSModel.COL_MONTH, month)
                .equalTo(GWDSModel.COL_YEAR, year)
                .findAll().size() > 0;
    }
}
