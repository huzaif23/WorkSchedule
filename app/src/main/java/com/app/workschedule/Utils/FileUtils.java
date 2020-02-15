package com.app.workschedule.Utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FileUtils {

    static FileUtils instance;
    private File file;
    Context context;
    String mainPath;

    private FileUtils(Context context) {
        this.context = context;
        createMainDirectory();
    }

    public static synchronized FileUtils getInstance(Context context) {
        if (instance == null)
            instance = new FileUtils(context);
        return instance;
    }

    private void createMainDirectory() {
        file = new File(context.getExternalFilesDir(null).getAbsolutePath(), "Work Schedule");
        mainPath = file.getPath();
        if (!file.exists())
            file.mkdir();
    }

    public String createPictureDirectory() {
        if (mainPath != null && !mainPath.isEmpty()) {
            file = new File(mainPath + File.separator + "pictures");
            if (!file.exists())
                file.mkdir();
        }
        return file.getPath();
    }

    public String createAudioDirectory() {
        if (mainPath != null && !mainPath.isEmpty()) {
            file = new File(mainPath + File.separator + "audio");
            if (!file.exists())
                file.mkdir();
        }
        return file.getPath();
    }

    public void save(Bitmap bitmap, File file) throws IOException {
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getFileNameFromUri(Uri uri,Context context) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public String convertImageToBase64(String path) {
        if (new File(path).exists()) {
            Bitmap bm = BitmapFactory.decodeFile(path);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream); //bm is the bitmap object
            byte[] b = byteArrayOutputStream.toByteArray();
            Log.d("byteArray", "run: " + b);
            String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
            return encodedImage;
        } else {
            return "";
        }

    }

}
