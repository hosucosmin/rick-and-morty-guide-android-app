package cosmin.hosu.rickandmortyguideapp.character.convertor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ImageLoader {

    private static final String TAG = "ImageLoader";

    public interface ImageLoadListener {
        void onImageLoaded(byte[] imageData);

        void onImageLoadFailed();
    }

    public static void loadImageFromUrl(String imageUrl, ImageLoadListener listener) {
        new ImageLoadTask(listener).execute(imageUrl);
    }

    private static class ImageLoadTask extends AsyncTask<String, Void, byte[]> {

        private ImageLoadListener listener;

        public ImageLoadTask(ImageLoadListener listener) {
            this.listener = listener;
        }

        @Override
        protected byte[] doInBackground(String... params) {
            String imageUrl = params[0];
            byte[] imageData = null;

            try {
                URL url = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();

                InputStream input = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(input);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                imageData = stream.toByteArray();
            } catch (Exception e) {
                Log.e(TAG, "Error loading image from URL: " + e.getMessage());
            }

            return imageData;
        }

        @Override
        protected void onPostExecute(byte[] imageData) {
            if (imageData != null) {
                listener.onImageLoaded(imageData);
            } else {
                listener.onImageLoadFailed();
            }
        }
    }
}
