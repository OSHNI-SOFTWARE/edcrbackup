package bd.com.aristo.edcr.modules.dcr.newdcr;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import bd.com.aristo.edcr.models.db.ProductModel;
import bd.com.aristo.edcr.modules.dcr.dcr.DCRUtils;
import bd.com.aristo.edcr.modules.dcr.newdcr.fragment.DCRProductsModel;
import bd.com.aristo.edcr.modules.dcr.newdcr.model.NewDCRModel;
import bd.com.aristo.edcr.modules.dcr.newdcr.model.NewDCRProductModel;
import bd.com.aristo.edcr.models.db.DateModel;
import bd.com.aristo.edcr.utils.DateTimeUtils;
import bd.com.aristo.edcr.utils.constants.StringConstants;
import io.realm.Realm;
import io.realm.Sort;

import static bd.com.aristo.edcr.modules.wp.WPUtils.getPlannedCount;

/**
 * Created by monir.sobuj on 11/9/2017.
 */

public class NewDCRUtils {

    public static List<NewDCRModel> getNewDCRList(Realm r){
        List<NewDCRModel> newDCRModelList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        String monthYear = "-"+DateTimeUtils.getMonthNumber(month) + "-" + year;

        List<NewDCRModel> newDCRModels = r.where(NewDCRModel.class)
                .sort(new String[]{NewDCRModel.COL_DATE, NewDCRModel.COL_SHIFT}, new Sort[]{Sort.DESCENDING, Sort.DESCENDING})
                .findAll();
        for (NewDCRModel newDCRModel:newDCRModels){
            if(newDCRModel.getDate().contains(monthYear)){
                newDCRModelList.add(newDCRModel);
            }
        }
        return newDCRModelList;
    }

    public static long getId(Realm r){
        Number currentIdNum = r.where(NewDCRModel.class).max(NewDCRModel.COL_ID);
        int nextId;
        if(currentIdNum == null) {
            nextId = 1;
        } else {
            nextId = currentIdNum.intValue() + 1;
        }
        return nextId;
    }

    public static long getNewDCRProductModelId(Realm r){
        Number currentIdNum = r.where(NewDCRProductModel.class).max(NewDCRProductModel.COL_ID);
        int nextId;
        if(currentIdNum == null) {
            nextId = 1;
        } else {
            nextId = currentIdNum.intValue() + 1;
        }
        return nextId;
    }


    public static List<DCRProductsModel> getNewDcrSampleList(Realm r, boolean isIntern) {
        DateModel dateModel = DCRUtils.getToday();
        int month = dateModel.getMonth();
        int year = dateModel.getYear();
        List<DCRProductsModel> wpProductsModels = new ArrayList<>();
        List<ProductModel> sampleModelList;
        if(isIntern){
            sampleModelList = r.where(ProductModel.class)
                    .equalTo(ProductModel.COL_MONTH, month)
                    .equalTo(ProductModel.COL_YEAR, year)
                    .equalTo(ProductModel.COL_ITEM_TYPE, StringConstants.SAMPLE_ITEM)
                    .equalTo(ProductModel.COL_ITEM_FOR, StringConstants.ITEM_FOR_INTERN)
                    .findAll();
        } else {
            sampleModelList = r.where(ProductModel.class)
                    .equalTo(ProductModel.COL_MONTH, month)
                    .equalTo(ProductModel.COL_YEAR, year)
                    .equalTo(ProductModel.COL_ITEM_TYPE, StringConstants.SAMPLE_ITEM)
                    .equalTo(ProductModel.COL_ITEM_FOR, StringConstants.ITEM_FOR_REGULAR)
                    .findAll();
        }

        for (ProductModel sampleModel : sampleModelList) {

            DCRProductsModel wpProductsModel = new DCRProductsModel();
            wpProductsModel.setCode(sampleModel.getCode());
            wpProductsModel.setName(sampleModel.getName()+"("+sampleModel.getPackSize()+")");
            wpProductsModel.setQuantity(sampleModel.getQuantity());
            wpProductsModel.setPlanned(getPlannedCount(r, sampleModel.getCode(), month, year));
            wpProductsModel.setBalance(sampleModel.getBalance());
            wpProductsModel.setCount(0);
            wpProductsModel.setItemType(1);
            wpProductsModels.add(wpProductsModel);

        }
        Collections.sort(wpProductsModels, new Comparator<DCRProductsModel>() {
            @Override
            public int compare(DCRProductsModel dcrProductsModel, DCRProductsModel t1) {
                return t1.getName().compareTo(dcrProductsModel.getName());
            }
        });
        return wpProductsModels;
    }

    public static List<DCRProductsModel> getNewDcrGiftList(boolean isIntern, Realm r) {
        DateModel dateModel = DCRUtils.getToday();
        int month = dateModel.getMonth();
        int year = dateModel.getYear();
        List<DCRProductsModel> newDcrProductsModels = new ArrayList<>();


        List<ProductModel> giftModelList;
        if(isIntern){
            giftModelList = r.where(ProductModel.class)
                    .equalTo(ProductModel.COL_MONTH, month)
                    .equalTo(ProductModel.COL_YEAR, year)
                    .equalTo(ProductModel.COL_ITEM_TYPE, StringConstants.GIFT_ITEM)
                    .equalTo(ProductModel.COL_ITEM_FOR, StringConstants.ITEM_FOR_INTERN)
                    .findAll();
        } else {
            giftModelList = r.where(ProductModel.class)
                    .equalTo(ProductModel.COL_MONTH, month)
                    .equalTo(ProductModel.COL_YEAR, year)
                    .equalTo(ProductModel.COL_ITEM_TYPE, StringConstants.GIFT_ITEM)
                    .equalTo(ProductModel.COL_ITEM_FOR, StringConstants.ITEM_FOR_REGULAR)
                    .findAll();
        }


        for (ProductModel giftModel : giftModelList) {
            DCRProductsModel wpProductsModel = new DCRProductsModel();
            wpProductsModel.setCode(giftModel.getCode());
            wpProductsModel.setName(giftModel.getName() + "("+giftModel.getPackSize()+")");
            wpProductsModel.setQuantity(giftModel.getQuantity());
            wpProductsModel.setPlanned(getPlannedCount(r, giftModel.getCode(), month, year));
            wpProductsModel.setBalance(giftModel.getBalance());
            wpProductsModel.setCount(0);
            wpProductsModel.setItemType(2);
            newDcrProductsModels.add(wpProductsModel);
        }

        Collections.sort(newDcrProductsModels, new Comparator<DCRProductsModel>() {
            @Override
            public int compare(DCRProductsModel dcrProductsModel, DCRProductsModel t1) {
                return t1.getName().compareTo(dcrProductsModel.getName());
            }
        });
        return newDcrProductsModels;
    }
    public static int setSaveBalance(Realm r, NewDCRProductModel dcrProductModel){
        DateModel dateModel = DCRUtils.getToday();
        int balance = 0;
        int count = dcrProductModel.getCount();
        ProductModel pm = r.where(ProductModel.class).equalTo(ProductModel.COL_CODE, dcrProductModel.getProductID())
                .equalTo(ProductModel.COL_MONTH, dateModel.getMonth())
                .equalTo(ProductModel.COL_YEAR, dateModel.getYear()).findFirst();
        if(pm != null){
            balance = pm.getBalance();
        }
        return balance >= count ? count : balance;
    }



}
