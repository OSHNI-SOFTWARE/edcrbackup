package bd.com.aristo.edcr.fcm.notification;

/**
 * Created by monir.sobuj on 14/07/2019.
 */

public class FCMBodyData {

    private String Tag;
    private String Title;
    private String Detail;
    private String Count;
    private String Datetime;

    public String getTag() {
        return Tag;
    }

    public void setTag(String tag) {
        Tag = tag;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDetail() {
        return Detail;
    }

    public void setDetail(String detail) {
        Detail = detail;
    }

    public String getCount() {
        return Count;
    }

    public void setCount(String count) {
        Count = count;
    }

    public String getDatetime() {
        return Datetime;
    }

    public void setDatetime(String datetime) {
        Datetime = datetime;
    }

    @Override
    public String toString() {
        return "FCMBodyData{" +
                "Tag='" + Tag + '\'' +
                ", Title='" + Title + '\'' +
                ", Detail='" + Detail + '\'' +
                ", Count='" + Count + '\'' +
                ", Datetime='" + Datetime + '\'' +
                '}';
    }
}
