package hp.photoappandroid94.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import hp.photoappandroid94.R;
import hp.photoappandroid94.adapter.PhotoAdapter;
import hp.photoappandroid94.model.Album;
import hp.photoappandroid94.model.Photo;
import hp.photoappandroid94.util.Save;

public class AlbumDetailActivity extends AppCompatActivity {

    private GridView gridView;
    private Context context = this;
    private Album album;
    private int position;
    private int mainPos;
    private Album al;
    private static final int SELECT_FILE = 2019;
    public static ArrayList<Bitmap> bitmapArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_detail);

        mainPos = getIntent().getIntExtra("pos", 0);

        al = AlbumActivity.albums.get(mainPos);

        gridView = findViewById(R.id.gridview);
        registerForContextMenu(gridView);

        if(!AlbumActivity.albums.get(mainPos).getPhotos().isEmpty()) {
            imageBitmap(AlbumActivity.albums.get(mainPos).getPhotos(), gridView, context);
        }

        gridView.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent mIntent = new Intent(context, AlbumSlideshowActivity.class);
            mIntent.putExtra("pos", i);
            mIntent.putExtra("mainPos", mainPos);
            context.startActivity(mIntent);
        });

        //delete fix
        gridView.setOnItemLongClickListener((adapterView, view, i, l) -> {
            position = i;
            return false;
        });

        Toolbar toolbar = findViewById(R.id.Album_Detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == SELECT_FILE){
                Uri imageURI = data.getData();
                String selectedImagePath = getRealPathFromURI(imageURI);
                Photo p = new Photo(selectedImagePath);
                AlbumActivity.albums.get(mainPos).getPhotos().add(p);
                imageBitmap(AlbumActivity.albums.get(mainPos).getPhotos(), gridView, context);
                Drawable d = Drawable.createFromPath(p.getimageFile());
                Save.save(AlbumActivity.albums, context);
            }
        }
    }

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;


    private  boolean checkAndRequestPermissions() {
        int read = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int write = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (read != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (write != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] ps = {MediaStore.MediaColumns.DATA};
        CursorLoader cursorLoader = new CursorLoader(this, contentUri, ps, null, null,
                null);
        Cursor cursor = cursorLoader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void imageBitmap(List<Photo> photos, GridView g, Context t) {
        bitmapArrayList.clear();
        for(Photo p : photos) {
            Bitmap bm;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(p.getimageFile(), options);
            final int REQUIRED_SIZE = 1000;
            int scale = 1;
            while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                    && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;
            options.inSampleSize = scale;
            options.inJustDecodeBounds = false;
            bm = BitmapFactory.decodeFile(p.getimageFile(), options);
            bitmapArrayList.add(bm);
            g.setAdapter(new PhotoAdapter(t, bitmapArrayList));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.image_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                bitmapArrayList.clear();
                Intent it = new Intent(context, AlbumActivity.class);
                it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(it);
                this.finish();
                return true;
            case R.id.addImage:
                if(checkAndRequestPermissions()) {
                    Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    gallery.setType("image/*");
                    startActivityForResult(Intent.createChooser(gallery, "select gallery"), SELECT_FILE);
                }
                return true;
//            case R.id.addFromCamera:
//                return true;
            case R.id.info2:
                String alert1 = "Add new image by clicking the 3 dots on top, and choose where you want to add image from";
                String alert2 = "To delete image: long click on the image";
                String alert3 = "To copy image: long click on the image";
                String alert4 = "To move image: long click on the image";
                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.setTitle("How to use this page");
                alertDialog.setMessage(alert1 +"\n\n"+ alert2 +"\n\n"+ alert3 +"\n\n"+ alert4);
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                        (dialog, which) -> dialog.dismiss());
                alertDialog.show();
                return true;
                default:
                    return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Choose your option");
        getMenuInflater().inflate(R.menu.photolist_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.copy:
                listDialog("copy");
                return true;
            case R.id.move:
                listDialog("move");
                return true;
            case R.id.deletePhoto:
                DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            if(!AlbumActivity.albums.get(mainPos).getPhotos().isEmpty()) {
                                AlbumActivity.albums.get(mainPos).getPhotos().remove(position);
                                imageBitmap(AlbumActivity.albums.get(mainPos).getPhotos(), gridView, context);
                                Save.save(AlbumActivity.albums, context);
                                if(AlbumActivity.albums.get(mainPos).getPhotos().isEmpty()){
                                    gridView.setAdapter(null);
                                }
                            }else{
                                gridView.setAdapter(null);
                            }
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            break;
                    }
                };
                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("Are you sure you want to delete this image?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void listDialog(String s){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose an album to " + s + " to: ");

        final ArrayAdapter<String> aa = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_single_choice);
        for(Album a : AlbumActivity.albums){
            if(!a.getAlbumName().equals(al.getAlbumName())) {
                aa.add(a.getAlbumName());
            }
        }
        int checkedItem = 0;
        final int[] picked = {0};
        builder.setSingleChoiceItems( aa, checkedItem, (dialog, which) -> picked[0] = which);

        builder.setPositiveButton("OK", (dialog, which) -> {
            if(s.equals("copy")){
                copy(aa.getItem(picked[0]));
            }else if(s.equals("move")){
                move(aa.getItem(picked[0]));
            }
        });
        builder.setNegativeButton("Cancel", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void move(String s){
        for(Album a : AlbumActivity.albums){
            if(a.getAlbumName().equals(s)){
                if(!AlbumActivity.albums.get(mainPos).getPhotos().isEmpty()) {
                    a.getPhotos().add(AlbumActivity.albums.get(mainPos).getPhotos().get(position));
                    AlbumActivity.albums.get(mainPos).getPhotos().remove(position);
                    imageBitmap(AlbumActivity.albums.get(mainPos).getPhotos(), gridView, context);
                    Save.save(AlbumActivity.albums, context);
                    Toast.makeText(context, "moved photo to album: " + a.getAlbumName(), Toast.LENGTH_SHORT ).show();
                }
                break;
            }
        }
    }
    private void copy(String s){
        for(Album a : AlbumActivity.albums){
            if(a.getAlbumName().equals(s)){
                if(!AlbumActivity.albums.get(mainPos).getPhotos().isEmpty()) {
                    Photo temp = AlbumActivity.albums.get(mainPos).getPhotos().get(position);
                    Photo p = new Photo(temp.getimageFile());
                    p.setLocation(temp.getLocationTag());
                    p.setPeople(temp.getPeopleTag());
                    a.getPhotos().add(p);
                    Save.save(AlbumActivity.albums, context);
                    Toast.makeText(context, "copied photo to album: " + a.getAlbumName(), Toast.LENGTH_SHORT ).show();
                }
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent it = new Intent(context, AlbumActivity.class);
        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(it);
        this.finish();
    }
}
