package bd.com.aristo.edcr.models.db;

import io.realm.RealmObject;

/**
 * Created by altaf.sil on 1/16/18.
 */

public class NewDoctorModel extends RealmObject {
    public String name;

    public NewDoctorModel(){

    }

    public NewDoctorModel(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
