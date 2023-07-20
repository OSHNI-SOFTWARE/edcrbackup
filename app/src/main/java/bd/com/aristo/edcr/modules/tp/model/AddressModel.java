package bd.com.aristo.edcr.modules.tp.model;

import io.realm.RealmObject;

/**
 * Created by altaf.sil on 1/16/18.
 */

public class AddressModel extends RealmObject {
    public String address;

    public AddressModel(){

    }

    public AddressModel(String adr){
        this.address = adr;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
