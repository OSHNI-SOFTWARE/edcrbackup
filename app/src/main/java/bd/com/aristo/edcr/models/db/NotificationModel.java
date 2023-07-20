package bd.com.aristo.edcr.models.db;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by altaf.sil on 1/17/18.
 */

public class NotificationModel extends RealmObject {
    @SerializedName("OperationType")
    private String tag;
    @SerializedName("Title")
    private String title;
    @SerializedName("Message")
    private String detail;
    @SerializedName("SetDateTime")
    private String dateTime;
    @PrimaryKey
    @SerializedName("NotificationID")
    private String nID;

    private int count;
    private boolean isRead;

    public NotificationModel(){

    }

    public NotificationModel(String mTag,String mTitle,String mDetail,int mCount,String mDateTime,boolean mRead){
        this.tag = mTag;
        this.title = mTitle;
        this.detail = mDetail;
        this.count = mCount;
        this.dateTime = mDateTime;
        this.isRead = mRead;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getnID() {
        return nID;
    }

    public void setnID(String nID) {
        this.nID = nID;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    @Override
    public String toString() {
        return "NotificationModel{" +
                "tag='" + tag + '\'' +
                ", title='" + title + '\'' +
                ", detail='" + detail + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", nID='" + nID + '\'' +
                ", count=" + count +
                ", isRead=" + isRead +
                '}';
    }
}
