package edepa.minilibs;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Locale;

public class BitmapSave extends AsyncTask<Bitmap, Void, Uri> {

    public static final int IMAGE_QUALITY = 100;

    private SaveListener saveListener;

    public interface SaveListener {
        void onSaveComplete(Uri uri);
    }

    public BitmapSave(SaveListener saveListener) {
        this.saveListener = saveListener;
    }

    public File getDefaultFile(){
        File sdCard = Environment.getExternalStorageDirectory();
        return new File(sdCard.getAbsolutePath() + "/edepa");
    }

    public String getDefaultFilename(){
        return String.format(Locale.getDefault(),
                "%d.jpg", System.currentTimeMillis());
    }

    @Override
    protected void onPostExecute(Uri uri) {
        super.onPostExecute(uri);
        saveListener.onSaveComplete(uri);
    }

    public Uri saveBitmap(Bitmap bitmap) throws Exception {
        Uri result = null;
        File file = getDefaultFile();
        if (file.exists() || file.mkdirs()) {
            String fileName = getDefaultFilename();
            File outFile = new File(file, fileName);
            FileOutputStream outStream = new FileOutputStream(outFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, IMAGE_QUALITY, outStream);
            outStream.flush();
            outStream.close();
            result = Uri.fromFile(outFile);
        }   return result;
    }

    @Override
    protected Uri doInBackground(Bitmap... bitmaps) {
        try { return saveBitmap(bitmaps[0]); }
        catch (Exception e){
            Log.i(getClass().getSimpleName(), "Save failed: " + e.getMessage());
            return null;
        }
    }

}
