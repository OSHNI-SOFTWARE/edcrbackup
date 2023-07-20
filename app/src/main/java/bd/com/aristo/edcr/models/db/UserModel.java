package bd.com.aristo.edcr.models.db;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by altaf.sil on 1/6/18.
 */

public class UserModel extends RealmObject {

    @SerializedName("id")
    private long userId;
    @SerializedName("designation_id")
    private long designationId;
    @SerializedName("company_id")
    private long companyId;
    @SerializedName("depot_id")
    private long depotId;
    @SerializedName("teritory_ad_id")
    private long teritoryAdId;
    @SerializedName("teritory_gm_id")
    private long teritoryGmId;
    @SerializedName("teritory_dsm_id")
    private long teritoryDsmId;
    @SerializedName("teritory_rsm_id")
    private long teritoryRsmId;
    @SerializedName("teritory_am_id")
    private long teritoryAmId;
    @SerializedName("teritory_mio_id")
    private long teritoryMioId;
    @SerializedName("teritory_id")
    private long teritoryId;
    @SerializedName("type")
    private String type;
    @SerializedName("product_group")
    private String productGroup;
    @SerializedName("name")
    private String name;
    @SerializedName("contact_number")
    private String contactNumber;
    @SerializedName("emargency_contact_number")
    private String emargencyContactNumber;
    @SerializedName("employee_number")
    private String employeeNumber;
    @SerializedName("email")
    private String email;
    @SerializedName("status")
    private String status;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getDesignationId() {
        return designationId;
    }

    public void setDesignationId(long designationId) {
        this.designationId = designationId;
    }

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    public long getDepotId() {
        return depotId;
    }

    public void setDepotId(long depotId) {
        this.depotId = depotId;
    }

    public long getTeritoryAdId() {
        return teritoryAdId;
    }

    public void setTeritoryAdId(long teritoryAdId) {
        this.teritoryAdId = teritoryAdId;
    }

    public long getTeritoryGmId() {
        return teritoryGmId;
    }

    public void setTeritoryGmId(long teritoryGmId) {
        this.teritoryGmId = teritoryGmId;
    }

    public long getTeritoryDsmId() {
        return teritoryDsmId;
    }

    public void setTeritoryDsmId(long teritoryDsmId) {
        this.teritoryDsmId = teritoryDsmId;
    }

    public long getTeritoryRsmId() {
        return teritoryRsmId;
    }

    public void setTeritoryRsmId(long teritoryRsmId) {
        this.teritoryRsmId = teritoryRsmId;
    }

    public long getTeritoryAmId() {
        return teritoryAmId;
    }

    public void setTeritoryAmId(long teritoryAmId) {
        this.teritoryAmId = teritoryAmId;
    }

    public long getTeritoryMioId() {
        return teritoryMioId;
    }

    public void setTeritoryMioId(long teritoryMioId) {
        this.teritoryMioId = teritoryMioId;
    }

    public long getTeritoryId() {
        return teritoryId;
    }

    public void setTeritoryId(long teritoryId) {
        this.teritoryId = teritoryId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProductGroup() {
        return productGroup;
    }

    public void setProductGroup(String productGroup) {
        this.productGroup = productGroup;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmargencyContactNumber() {
        return emargencyContactNumber;
    }

    public void setEmargencyContactNumber(String emargencyContactNumber) {
        this.emargencyContactNumber = emargencyContactNumber;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "userId=" + userId +
                ", designationId=" + designationId +
                ", companyId=" + companyId +
                ", depotId=" + depotId +
                ", teritoryAdId=" + teritoryAdId +
                ", teritoryGmId=" + teritoryGmId +
                ", teritoryDsmId=" + teritoryDsmId +
                ", teritoryRsmId=" + teritoryRsmId +
                ", teritoryAmId=" + teritoryAmId +
                ", teritoryMioId=" + teritoryMioId +
                ", teritoryId=" + teritoryId +
                ", type='" + type + '\'' +
                ", productGroup='" + productGroup + '\'' +
                ", name='" + name + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", emargencyContactNumber='" + emargencyContactNumber + '\'' +
                ", employeeNumber='" + employeeNumber + '\'' +
                ", email='" + email + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
