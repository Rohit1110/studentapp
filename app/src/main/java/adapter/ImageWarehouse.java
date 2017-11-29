package adapter;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Environment;

import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Callback;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import utils.Utility;

import static java.security.AccessController.getContext;


/**
 * Created by Rohit on 11/29/2017.
 */

public class ImageWarehouse implements Callback {

    private String mDirectory;
    private String mFileName;
    private ImageView mContainer;
    Context context;


    private static final String TAG = "ImageWarehouse";

    public ImageWarehouse(String fileName, ImageView container, String directory, Context context) {
        this.mFileName = fileName;
        this.mContainer = container;
        this.mDirectory = directory;
        this.getStorageDir();
        this.context = context;
    }


    @Override
    public void onSuccess() {
       /* if (this.isExternalStorageWritable()) {*/
        final Bitmap bitmap = ((BitmapDrawable) this.mContainer.getDrawable()).getBitmap();
        new AsyncTask<Void, Void, File>() {
            @Override
            protected File doInBackground(Void... params) {
                // File file = null;
                /// File mypath=null;
//                    try {
//                        ContextWrapper cw = new ContextWrapper(context);
//                        //file = new File(ImageWarehouse.this.getStorageDir().getPath().concat("/").concat(ImageWarehouse.this.mFileName.concat(Environment.DIRECTORY_PICTURES)));
//                        File directory = cw.getDir("imageDir",Context.MODE_WORLD_READABLE);
//                        // Create imageDir
//                        mypath=new File(directory,"profile.jpg");
//                        mypath.createNewFile();
//                        FileOutputStream ostream = new FileOutputStream(mypath);
//                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
//                        ostream.close();
//                    } catch (Exception e) {
//                        Log.e(TAG, "External Storage is not available 123"+e);
//                    }
                //Utility.saveFile();
                File pictureFile = getOutputMediaFile();
                if (pictureFile == null) {
                    Log.d(TAG, "Error creating media file, check storage permissions: ");// e.getMessage());
                }
                try {
                    FileOutputStream fos = new FileOutputStream(pictureFile);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
                    fos.close();
                } catch (FileNotFoundException e) {
                    Log.d(TAG, "File not found: " + e.getMessage());
                } catch (IOException e) {
                    Log.d(TAG, "Error accessing file: " + e.getMessage());
                }
                return pictureFile;
            }
        }.execute();
        /*} else {
            Log.e(TAG, "External Storage is not available 1111");
        }*/
    }

    @Override
    public void onError() {

    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public File getStorageDir() {
        File file = new File(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_PICTURES.concat(this.mDirectory));
        if (!file.mkdirs()) {
        }
        return file;
    }

    private File getOutputMediaFile() {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + context.getPackageName()
                + "/Files");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName = "MI_" + timeStamp + ".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }
}

