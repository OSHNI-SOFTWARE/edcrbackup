package bd.com.aristo.edcr.modules.dvr.model;

import java.util.List;

/**
 * Created by monir.sobuj on 01/11/17.
 */

public class DVRForSendMaster {

    private String DayNumber;
    private List<DVRForSend> DetailList;

    public String getDayNumber() {
        return DayNumber;
    }

    public void setDayNumber(String dayNumber) {
        DayNumber = dayNumber;
    }

    public List<DVRForSend> getDetailList() {
        return DetailList;
    }

    public void setDetailList(List<DVRForSend> detailList) {
        DetailList = detailList;
    }
}
