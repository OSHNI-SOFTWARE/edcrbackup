package bd.com.aristo.edcr.modules.tp.model;

import java.util.List;

/**
 * Created by monir.sobuj on 6/8/17.
 */

public class TPEveningModel {

    private long id;

    private String contactAddress;

    private String rTime;

    private String nda;
    private String shiftType;
    List<ITPPlacesModel> placeList;
    private String serverId;
    private boolean isApproved;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContactAddress() {
        return contactAddress;
    }

    public void setContactAddress(String contactAddress) {
        this.contactAddress = contactAddress;
    }

    public String getrTime() {
        return rTime;
    }

    public void setrTime(String rTime) {
        this.rTime = rTime;
    }

    public String getNda() {
        return nda;
    }

    public void setNda(String nda) {
        this.nda = nda;
    }

    public String getShiftType() {
        return shiftType;
    }

    public void setShiftType(String shiftType) {
        this.shiftType = shiftType;
    }
    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public List<ITPPlacesModel> getPlaceList() {
        return placeList;
    }

    public void setPlaceList(List<ITPPlacesModel> placeList) {
        this.placeList = placeList;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

    @Override
    public String toString() {
        return "TPEveningModel{" +
                "id=" + id +
                ", contactAddress='" + contactAddress + '\'' +
                ", rTime='" + rTime + '\'' +
                ", nda='" + nda + '\'' +
                ", shiftType='" + shiftType + '\'' +
                ", placeList=" + placeList +
                ", serverId='" + serverId + '\'' +
                '}';
    }
}
