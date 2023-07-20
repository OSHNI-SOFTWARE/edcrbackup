package bd.com.aristo.edcr.modules.dss;

import androidx.annotation.Nullable;

import bd.com.aristo.edcr.utils.StringUtils;

/**
 * Created by monir.sobuj on 06/11/17.
 */

public class DSSModel{

    private String code;

    private String name;
    @Nullable
    private String packSize;
    private int count;
    private boolean isChecked;
    private boolean isIntern;

    private int flag; // 1 for Selective, 2 for Sample, 3 for Gift


    public DSSModel(){

    }

    public DSSModel(String name,String code,String packSize,int count,boolean isChecked){
        this.name = name;
        this.code = code;
        this.packSize = packSize;
        this.count = count;
        this.isChecked = isChecked;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code   = code;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackSize() {
        return packSize;
    }

    public void setPackSize(String packSize) {
        this.packSize = packSize;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isIntern() {
        return isIntern;
    }

    public void setIntern(boolean intern) {
        isIntern = intern;
    }

    @Override
    public String toString() {
        return "PWDSProductsModel{" +
                "unique id='" + StringUtils.hashString64Bit(code) + '\'' +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", packSize='" + packSize + '\'' +
                ", count=" + count +
                '}';
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}