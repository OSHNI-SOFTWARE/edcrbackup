package bd.com.aristo.edcr.utils;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by square on 3/8/16.
 */


public class FileUtils {

    public static String defaultPath = Environment.getExternalStorageDirectory()+"/SIL/EDCR/";
    public static String defaultPathExportDB = Environment.getExternalStorageDirectory()+"/SIL/";

    static String tag = "FileUtils";


    public static void createDefaultFolder(Activity context){
        File folder = new File(FileUtils.defaultPath);
        if(!folder.exists()){
            if(!folder.mkdirs()){
                PopupUtils.showToast(context, "Cannot access storage");
            }
        }
    }

    public static void deleteAllFilesIn(@Nullable File fileOrDirectory){
        try {
            if (fileOrDirectory == null) {
                fileOrDirectory = new File(defaultPath);
            }
            if (fileOrDirectory.isDirectory() && fileOrDirectory.length() > 0)
                if(fileOrDirectory.listFiles() != null) {
                    for (File child : fileOrDirectory.listFiles()) {
                        if (child.length() > 0)
                            deleteAllFilesIn(child);
                    }
                }
            if (fileOrDirectory.exists()) {
                Log.i(tag, "Deleting: " + fileOrDirectory.getName());
                boolean isDeleted = fileOrDirectory.delete();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static List<String> getAllFileNames(){
        File folder = new File(defaultPath);
        File[] files = folder.listFiles();
        List<String> names = new ArrayList<>();
        if(files != null && files.length > 0) {
            for (File f : files) {
                names.add(f.getName());
            }
        }
        return names;
    }

    public static boolean deleteFile(String filename, @Nullable String filePath) {
        if(filePath == null) filePath = defaultPath;
        File file = new File(filePath + filename);
        boolean didFileDeleted = false;
        if(file.exists()) didFileDeleted = file.delete();
        return didFileDeleted;
    }

    /**
     * Creates a file with specified values
     * @param contents data to be written into file
     * @param fileName date of the file
     * @param filePath path of the file null to use default
     * @return true for success false otherwise
     */
    public static boolean writeToFile(String contents, String fileName, String filePath){
        if(contents.trim().isEmpty()) return false;

        filePath = (filePath == null ? defaultPath : filePath);
//		File myExternalFile = new File(context.getExternalFilesDir(filePath), fileName);
        File dir = new File(filePath);
        dir.mkdirs();
        File myExternalFile = new File(dir, fileName);
        FileOutputStream fos;
        OutputStreamWriter osw;
        try {
            fos = new FileOutputStream(myExternalFile);
            osw = new OutputStreamWriter(fos);
            osw.append(contents);
            osw.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void exportDatabase(Activity context, String dbFileName, String exportPathWithFileName){
        //Open your local db as the input stream
        try {
            String dataFolder = context.getFilesDir().getPath();
            String dbFolder   = dataFolder.substring(0, dataFolder.lastIndexOf("/"));
            String inFileName = dbFolder + "/databases/" + dbFileName; //"data/"+context.getPackageName()+"/databases/" + dbFileName;
            File dbFile = new File(inFileName);
            FileInputStream fis = new FileInputStream(dbFile);

            String outFileName = exportPathWithFileName;
            //Open the empty db as the output stream
            OutputStream output = new FileOutputStream(outFileName);
            //transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            //Close the streams
            output.flush();
            output.close();
            fis.close();
            PopupUtils.showToast(context, "Exported DB at: " + exportPathWithFileName);
        }catch (Exception e){
            PopupUtils.showToast(context, "Export failed");
            e.printStackTrace();
        }
    }

    /*public static String[] saveInPermanentFile(Context context, JSONObject chapterJSON){
        //filename and checksum is needed
        String fileName = FileUtils.getNewFileName(context);
        String contents = chapterJSON.toString();
//        logLargeString("cont: " + contents);
        contents        = new CryptographicUtils().encrypt(contents);
        FileUtils.writeToFile(contents, fileName, null);
        String checksum = CryptographicUtils.getMd5(FileUtils.defaultPath + fileName);
        return new String[] {fileName, checksum};
    }*/

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

}
