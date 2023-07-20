package bd.com.aristo.edcr.modules.reports.ss;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import bd.com.aristo.edcr.modules.reports.ss.model.ItemStatementModel;
import bd.com.aristo.edcr.modules.reports.ss.model.SSResponse;

/**
 * Created by monir.sobuj on 2/20/2018.
 */

public class SampleUtils {
    /*public static void updateItemStatementList(final Realm r, final List<ItemStatementModel> itemStatementModelList,final String itemType){
        r.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                r.where(ItemStatementModel.class).equalTo("itemType", itemType).findAll().deleteAllFromRealm();
                realm.insertOrUpdate(itemStatementModelList);
            }
        });
    }*/

    public static List<ItemStatementModel> getItemStatementList(String itemType, String itemFor) {
        SSResponse ssResponse = SSResponse.getInstance();
        List<ItemStatementModel> itemStatementModels = new ArrayList<>();
        if(ssResponse.getItemStatementModelList() != null && ssResponse.getItemStatementModelList(). size() > 0){
            for(ItemStatementModel itemStatementModel:ssResponse.getItemStatementModelList()){
                if(itemStatementModel.getItemType().equalsIgnoreCase(itemType) && itemStatementModel.getItemFor().equalsIgnoreCase(itemFor)){
                    itemStatementModels.add(itemStatementModel);
                }
            }
        }
        /*List<ItemStatementModel> itemStatementModels = realm.where(ItemStatementModel.class)
                .equalTo("itemType",itemType)
                .equalTo("itemFor", itemFor)
                .findAll().sort("productName");*/
        Collections.sort(itemStatementModels, (promoItem, p1) -> promoItem.getProductName().compareTo(p1.getProductName()));
        return itemStatementModels;
    }

    public static List<ItemStatementModel> getItemStatementListForIntern(String itemFor) {
        /*List<ItemStatementModel> itemStatementModels = realm.where(ItemStatementModel.class)
                .equalTo("itemFor", itemFor)
                .findAll().sort("productName");*/
        SSResponse ssResponse = SSResponse.getInstance();
        List<ItemStatementModel> itemStatementModels = new ArrayList<>();
        if(ssResponse.getItemStatementModelList() != null && ssResponse.getItemStatementModelList(). size() > 0){
            for(ItemStatementModel itemStatementModel:ssResponse.getItemStatementModelList()){
                if(itemStatementModel.getItemFor().equalsIgnoreCase(itemFor)){
                    itemStatementModels.add(itemStatementModel);
                }
            }
        }

        Collections.sort(itemStatementModels, (promoItem, p1) -> promoItem.getProductName().compareTo(p1.getProductName()));

        return itemStatementModels;
    }
}
