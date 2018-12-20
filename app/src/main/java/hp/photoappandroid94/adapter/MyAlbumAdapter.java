package hp.photoappandroid94.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import hp.photoappandroid94.R;
import hp.photoappandroid94.model.Album;
import hp.photoappandroid94.model.Photo;

public class MyAlbumAdapter extends ArrayAdapter<Album>{

    //the list values in the List of type album
    private List<Album> albumList;

    //activity context
    private Context context;

    static class ViewHolder {
        ImageView image;
        TextView name;
        TextView totalPhoto;
    }

    public MyAlbumAdapter(Context context, List<Album> albumList) {
        super(context, R.layout.list_item, albumList);
        this.context = context;
        this.albumList = albumList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        View result = convertView;

        if (result == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            result = inflater.inflate(R.layout.list_item, parent, false);
            viewHolder.name = result.findViewById(R.id.albumView_name);
            viewHolder.totalPhoto = result.findViewById(R.id.totalPhotos);
            viewHolder.image =  result.findViewById(R.id.imageView_thumbnail);

            result.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) result.getTag();
        }

        final Album album = albumList.get(position);

        if(album != null) {
            viewHolder.name.setText(album.getAlbumName());
            viewHolder.totalPhoto.setText("Total Photos: " + album.getPhotos().size());
        }
        if(!album.getPhotos().isEmpty()){
            viewHolder.image.setImageBitmap(imageBitmap(album.getPhotos().get(0), context));
        }else{
            viewHolder.image.setImageResource(R.drawable.ic_no_image);
        }

        return result;
    }

    private Bitmap imageBitmap(Photo photos, Context t) {
        Photo p = photos;
        Bitmap bm;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(p.getimageFile(), options);
        final int REQUIRED_SIZE = 1000;
        int scale = 1;
        while (options.outWidth / scale / 2 >= REQUIRED_SIZE && options.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
        }
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(p.getimageFile(), options);
        return bm;
    }
}