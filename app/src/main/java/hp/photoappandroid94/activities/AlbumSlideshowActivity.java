package hp.photoappandroid94.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import hp.photoappandroid94.R;
import hp.photoappandroid94.util.Save;

public class AlbumSlideshowActivity extends AppCompatActivity {

    private Context context = this;
    private int mainPos, imgPos;
    private ImageView imageView;
    private AlbumDetailActivity prevActivity;
    private TextView people, location, caption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_slideshow);
        Bundle mBundle = getIntent().getExtras();
        imgPos = getIntent().getIntExtra("pos", 0);
        mainPos = getIntent().getIntExtra("mainPos", 0);

        imageView = findViewById(R.id.slide);
        caption = findViewById(R.id.caption);
        people = findViewById(R.id.peopleTag);
        location = findViewById(R.id.locationTag);

        String path = AlbumActivity.albums.get(mainPos).getPhotos().get(imgPos).getimageFile();
        String filename = path.substring(path.lastIndexOf("/")+1);
        path.substring(path.lastIndexOf("/")+1);
        String file;
        if (filename.indexOf(".") > 0) {
            file = filename.substring(0, filename.lastIndexOf("."));
        } else {
            file =  filename;
        }
        caption.setText("Caption: " + file);
        people.setText("People: " + AlbumActivity.albums.get(mainPos).getPhotos().get(imgPos).getPeopleTag());
        location.setText("Location: " + AlbumActivity.albums.get(mainPos).getPhotos().get(imgPos).getLocationTag());

        Bitmap bitmap = prevActivity.bitmapArrayList.get(imgPos);
        imageView.setImageBitmap(bitmap);


        Toolbar toolbar = (Toolbar)findViewById(R.id.Album_slideshow_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.slideshow_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.nextImage:
                imgPos++;
                if(imgPos >= prevActivity.bitmapArrayList.size()){
                    imgPos = prevActivity.bitmapArrayList.size()-1;
                }else{
                    Bitmap bitmap = prevActivity.bitmapArrayList.get(imgPos);
                    imageView.setImageBitmap(bitmap);
                    String path = AlbumActivity.albums.get(mainPos).getPhotos().get(imgPos).getimageFile();
                    String filename = path.substring(path.lastIndexOf("/")+1);
                    path.substring(path.lastIndexOf("/")+1);
                    String file;
                    if (filename.indexOf(".") > 0) {
                        file = filename.substring(0, filename.lastIndexOf("."));
                    } else {
                        file =  filename;
                    }
                    caption.setText("Caption: " + file);
                    people.setText("People: " + AlbumActivity.albums.get(mainPos).getPhotos().get(imgPos).getPeopleTag());
                    location.setText("Location: " + AlbumActivity.albums.get(mainPos).getPhotos().get(imgPos).getLocationTag());

                }
                return true;
            case R.id.prevImage:
                imgPos--;
                if(imgPos < 0){
                    imgPos = 0;
                }else{
                    Bitmap bitmap2 = prevActivity.bitmapArrayList.get(imgPos);
                    String path = AlbumActivity.albums.get(mainPos).getPhotos().get(imgPos).getimageFile();
                    String filename = path.substring(path.lastIndexOf("/")+1);
                    path.substring(path.lastIndexOf("/")+1);
                    String file;
                    if (filename.indexOf(".") > 0) {
                        file = filename.substring(0, filename.lastIndexOf("."));
                    } else {
                        file =  filename;
                    }
                    caption.setText("Caption: " + file);
                    imageView.setImageBitmap(bitmap2);
                    people.setText("People: " + AlbumActivity.albums.get(mainPos).getPhotos().get(imgPos).getPeopleTag());
                    location.setText("Location: " + AlbumActivity.albums.get(mainPos).getPhotos().get(imgPos).getLocationTag());
                }
                return true;
            case R.id.addTag:
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
                View mView = getLayoutInflater().inflate(R.layout.dialog, null);
                final EditText etPeople = (EditText) mView.findViewById(R.id.etPeople);
                final EditText etLocation = (EditText) mView.findViewById(R.id.etLocation);
                Button mLogin = (Button) mView.findViewById(R.id.btnLogin);
                Button mCancel= (Button) mView.findViewById(R.id.btnCancel);
                mBuilder.setView(mView);
                if(AlbumActivity.albums.get(mainPos).getPhotos().get(imgPos).getPeopleTag().equals("")) {
                    etPeople.getText().clear();
                }else{
                    etPeople.setText(AlbumActivity.albums.get(mainPos).getPhotos().get(imgPos).getPeopleTag());
                }

                if(AlbumActivity.albums.get(mainPos).getPhotos().get(imgPos).getLocationTag().equals("")){
                    etLocation.getText().clear();
                }else{
                    etLocation.setText(AlbumActivity.albums.get(mainPos).getPhotos().get(imgPos).getLocationTag());
                }

                final AlertDialog dialog = mBuilder.create();
                dialog.show();
                mCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                mLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlbumActivity.albums.get(mainPos).getPhotos().get(imgPos).setPeople(etPeople.getText().toString().trim());
                        AlbumActivity.albums.get(mainPos).getPhotos().get(imgPos).setLocation(etLocation.getText().toString().trim());
                        Save.save(AlbumActivity.albums, context);
                        people.setText("People: " + AlbumActivity.albums.get(mainPos).getPhotos().get(imgPos).getPeopleTag());
                        location.setText("Location: " + AlbumActivity.albums.get(mainPos).getPhotos().get(imgPos).getLocationTag());
                        dialog.dismiss();
                    }
                });
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
