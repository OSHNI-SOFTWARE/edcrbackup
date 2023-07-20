package bd.com.aristo.edcr.modules.reports.ss.model;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by altaf.sil on 3/18/18.
 */

public class SampleStatementResponse {

    private static SampleStatementResponse ssResponse = null;
    private SampleStatementResponse(){

    }

    public static SampleStatementResponse getInstance(){
        if(ssResponse == null){
            ssResponse = new SampleStatementResponse();
        }

        return ssResponse;
    }
    public List<SampleStatementModel> sampleStatementModelList;

    public void addModels(List<SampleStatementModel> sampleStatementModelList){
        if (this.sampleStatementModelList == null || this.sampleStatementModelList.size() <= 0) {
            this.sampleStatementModelList = new ArrayList<>();
        }
        this.sampleStatementModelList.addAll(sampleStatementModelList);
    }

    public List<SampleStatementModel> getSampleStatementModelList(String itemType, String itemFor) {
        List<SampleStatementModel> returnList = new ArrayList<>();
        for(SampleStatementModel sample:sampleStatementModelList){
            if(sample.getItemType().equalsIgnoreCase(itemType) && sample.getItemFor().equalsIgnoreCase(itemFor)){
                returnList.add(sample);
            }
        }
        return returnList;
    }
    public int[][] getSummary(){//0-SmR, 1-SlR, 2-SmI, 3-GtR, 4-GtI, //0-total, 1-1st, 2-2nd, 3-3rd, 4-4th, 5-bal
        int[][] summary = new int[5][6];
        for(SampleStatementModel sample:sampleStatementModelList){
            String pCode = sample.getProductCode();
            if(!pCode.contains("PPM")) {
                String type = pCode.substring(0, 3).toLowerCase();
                switch (type) {
                    case "smr":
                        summary[0][0] += sample.getTotalQty();
                        summary[0][1] += sample.getExecuteFirst();
                        summary[0][2] += sample.getExecuteSecond();
                        summary[0][3] += sample.getExecuteThird();
                        summary[0][4] += sample.getExecuteFourth();
                        summary[0][5] += sample.getClosingStock();
                        break;
                    case "slr":
                        summary[1][0] += sample.getTotalQty();
                        summary[1][1] += sample.getExecuteFirst();
                        summary[1][2] += sample.getExecuteSecond();
                        summary[1][3] += sample.getExecuteThird();
                        summary[1][4] += sample.getExecuteFourth();
                        summary[1][5] += sample.getClosingStock();
                        break;
                    case "smi":
                        summary[2][0] += sample.getTotalQty();
                        summary[2][1] += sample.getExecuteFirst();
                        summary[2][2] += sample.getExecuteSecond();
                        summary[2][3] += sample.getExecuteThird();
                        summary[2][4] += sample.getExecuteFourth();
                        summary[2][5] += sample.getClosingStock();
                        break;
                    case "gtr":
                        summary[3][0] += sample.getTotalQty();
                        summary[3][1] += sample.getExecuteFirst();
                        summary[3][2] += sample.getExecuteSecond();
                        summary[3][3] += sample.getExecuteThird();
                        summary[3][4] += sample.getExecuteFourth();
                        summary[3][5] += sample.getClosingStock();
                        break;
                    case "gti":
                        summary[4][0] += sample.getTotalQty();
                        summary[4][1] += sample.getExecuteFirst();
                        summary[4][2] += sample.getExecuteSecond();
                        summary[4][3] += sample.getExecuteThird();
                        summary[4][4] += sample.getExecuteFourth();
                        summary[4][5] += sample.getClosingStock();
                        break;
                    default:
                        break;
                }
            }
        }
        return summary;
    }
}
