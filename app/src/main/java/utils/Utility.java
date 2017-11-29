package utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Rohit on 11/27/2017.
 */

public class Utility {

    public void createAlert(Context context,String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton("ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    public static void saveFile(Context context, Bitmap b, String picName) throws IOException {
        FileOutputStream fos;

            fos = context.openFileOutput(picName, Context.MODE_PRIVATE);
            b.compress(Bitmap.CompressFormat.PNG, 100, fos);

            fos.close();

    }

    public static Bitmap loadBitmap(Context context, String picName){
        Bitmap b = null;
        FileInputStream fis=null;
        try {
            fis = context.openFileInput(picName);
            b = BitmapFactory.decodeStream(fis);
        }
        catch (FileNotFoundException e) {
            Log.d("tag", "file not found");
            e.printStackTrace();
        }
        catch (IOException e) {
            Log.d("tag", "io exception");
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return b;
    }

}
