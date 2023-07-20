package bd.com.aristo.edcr.modules.reports.ss.model;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by altaf.sil on 3/18/18.
 */

public class SSResponse {

    private static SSResponse ssResponse = null;
    private SSResponse(){

    }

    public static SSResponse getInstance(){
        if(ssResponse == null){
            ssResponse = new SSResponse();
        }

        return ssResponse;
    }

    public List<ItemStatementModel> itemStatementModelList;

    public void addModels(List<ItemStatementModel> itemStatementModels){
        if (this.itemStatementModelList == null || this.itemStatementModelList.size() <= 0) {
            this.itemStatementModelList = new ArrayList<>();
        }
        this.itemStatementModelList.addAll(itemStatementModels);
    }

    public List<ItemStatementModel> getItemStatementModelList() {
        return itemStatementModelList;
    }
}
