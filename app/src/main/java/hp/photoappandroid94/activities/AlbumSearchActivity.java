package hp.photoappandroid94.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import hp.photoappandroid94.R;
import hp.photoappandroid94.adapter.PhotoAdapter;
import hp.photoappandroid94.model.Album;
import hp.photoappandroid94.model.Photo;
import hp.photoappandroid94.util.Save;

public class AlbumSearchActivity extends AppCompatActivity {

    private static ArrayList<Bitmap> bitmapArrayList = new ArrayList<>();
    private static List<Photo> tempPhoto = new ArrayList<>();
    private static List<Photo> tempPhoto2 = new ArrayList<>();
    private Context context = this;
    private FloatingActionButton fab, fab2;
    private Animation fabOpen, fabClose, rotateForward, rotateBackward;
    private LinearLayout ll;
    private TextView tv;
    private boolean isOpen = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_search);


        EditText et = (EditText) findViewById(R.id.sEditText);
        GridView gv = (GridView) findViewById(R.id.Sgridview);
        ll = findViewById(R.id.addResult);
        tv = findViewById(R.id.tv);
        fab = findViewById(R.id.fab);
        fab2 = findViewById(R.id.fab2);

        fabOpen = AnimationUtils.loadAnimation(context, R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(context, R.anim.fab_close);

        rotateForward = AnimationUtils.loadAnimation(context, R.anim.rotate_forward);
        rotateBackward = AnimationUtils.loadAnimation(context, R.anim.rotate_backward);

        Toolbar toolbar = (Toolbar)findViewById(R.id.Album_search_toolbar);
        setSupportActionBar(toolbar);

        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!et.getText().toString().isEmpty()){
                    tempPhoto.clear();
                    tempPhoto2.clear();
                    String ss = et.getText().toString();
                    List<String> result = Arrays.asList(ss.split(","));
                    for(String s : result) {
                        for (Album a : AlbumActivity.albums) {
                            for (Photo p : a.getPhotos()) {
                                if (p.getPeopleTag().contains(s) || p.getLocationTag().contains(s)) {
                                    if(tempPhoto.contains(p)){

                                    }else {
                                        tempPhoto.add(p);
                                    }
                                }
                            }
                        }
                    }
                    tempPhoto2.addAll(tempPhoto);
                    imageBitmap(tempPhoto, gv, context);
                }else{
                    tempPhoto.clear();
                    tempPhoto2.clear();
                    gv.setAdapter(null);
                }
            }



            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        fab.setOnClickListener(view -> animateFab());
        fab2.setOnClickListener(view -> {
            animateFab();
            if(!tempPhoto2.isEmpty()){
                final int min = 20;
                final int max = 200;
                final int random = new Random().nextInt((max - min) + 1) + min;
                Album album = new Album("Album from search result " + Integer.toString(random));
                album.getPhotos().addAll(tempPhoto2);
                AlbumActivity.albums.add(album);
                Save.save(AlbumActivity.albums, context);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                tempPhoto.clear();
                tempPhoto2.clear();
                Intent it = new Intent(context, AlbumActivity.class);
                it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(it);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void animateFab(){
        if(isOpen){
            ll.setVisibility(View.GONE);
            fab.startAnimation(rotateForward);
            fab2.startAnimation(fabClose);
            fab2.setClickable(false);
            tv.startAnimation(fabClose);
            isOpen = false;
        }else{
            ll.setVisibility(View.VISIBLE);
            fab.startAnimation(rotateBackward);
            fab2.startAnimation(fabOpen);
            fab2.setClickable(true);
            tv.startAnimation(fabOpen);
            isOpen = true;
        }
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
        }
        tempPhoto.clear();
        g.setAdapter(new PhotoAdapter(t, bitmapArrayList));
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
