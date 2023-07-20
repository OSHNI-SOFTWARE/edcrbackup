package bd.com.aristo.edcr.utils;

import java.io.File;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Tariqul.Islam on 6/14/17.
 */

public class RealmUtils {

    public static void exportDatabase() {

        // init realm
         RealmConfiguration config = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        //return Realm.getDefaultInstance();
        Realm realm = Realm.getInstance(config);

        File exportRealmFile = null;
        // get or create an "export.realm" file
        exportRealmFile = new File(FileUtils.defaultPathExportDB, "export.realm");

        // if "export.realm" already exists, delete
        exportRealmFile.delete();

        // copy current realm to "export.realm"
        realm.writeCopyTo(exportRealmFile);

        realm.close();
    }





}
