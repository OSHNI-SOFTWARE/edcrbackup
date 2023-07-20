package bd.com.aristo.edcr.modules.reports.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by monir.sobuj on 10/10/2018.
 */

public class DVRGroup {

    @SerializedName("DetailList")
    private List<DVRMarket> dvrMarketList;
    @SerializedName("MPGroup")
    private String group;

    public List<DVRMarket> getDvrMarketList() {
        return dvrMarketList;
    }

    public void setDvrMarketList(List<DVRMarket> dvrMarketList) {
        this.dvrMarketList = dvrMarketList;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
