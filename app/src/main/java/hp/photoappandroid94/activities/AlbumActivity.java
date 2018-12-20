package hp.photoappandroid94.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import hp.photoappandroid94.R;
import hp.photoappandroid94.adapter.MyAlbumAdapter;
import hp.photoappandroid94.model.Album;
import hp.photoappandroid94.util.Save;


public class AlbumActivity extends AppCompatActivity {

    private ListView listView;
    private EditText et;
    private Button add, cancel;
    private Context context = this;
    public static List<Album> albums = new ArrayList<>();
    private MyAlbumAdapter mAdapter;
    private int tempPos;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        listView = findViewById(R.id.albumListView);
        add = findViewById(R.id.createButton);
        cancel = findViewById(R.id.cancelButton);
        et = findViewById(R.id.albumEditText);


        albums = Save.loadData(context);

        Toolbar toolbar = findViewById(R.id.Album_toolbar);
        setSupportActionBar(toolbar);

        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.toString().trim().length()==0){
                    add.setEnabled(false);
                    add.setAlpha(0.25f);
                } else {
                    add.setEnabled(true);
                    add.setAlpha(1.0f);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!et.getText().toString().isEmpty()){
                    Album a = new Album(et.getText().toString());
                    int k = 0;
                    for(Album aa : albums){
                        if(aa.getAlbumName().equalsIgnoreCase(a.getAlbumName())){
                            k = 5;
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage("Album already exist!")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            //do things
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                            et.getText().clear();
                            break;
                        }
                    }
                    if(k == 0) {
                        albums.add(a);
                        mAdapter.notifyDataSetChanged();
                        et.getText().clear();
                        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        Save.save(albums, context);
                    }
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et.getText().clear();
                et.clearFocus();
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });

        mAdapter = new MyAlbumAdapter(context, albums);

        registerForContextMenu(listView);
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent mIntent = new Intent(context, AlbumDetailActivity.class);
                mIntent.putExtra("pos", i);
                mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(mIntent);
                finish();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                tempPos = position;
                return false;
            }
        });

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Choose your option");
        getMenuInflater().inflate(R.menu.albumlist_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.rename:
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                final EditText input = new EditText(context);
                builder.setTitle("Rename Album");
                builder.setMessage("Enter new album name");
                builder.setPositiveButton("Rename",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                if(!input.getText().toString().isEmpty()) {
                                    Album a = new Album(input.getText().toString());
                                    int k = 0;
                                    for(Album aa : albums){
                                        if(aa.getAlbumName().equalsIgnoreCase(a.getAlbumName())){
                                            k = 5;
                                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                            builder.setMessage("Album already exist!")
                                                    .setCancelable(false)
                                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                        }
                                                    });
                                            AlertDialog alert = builder.create();
                                            alert.show();
                                            input.getText().clear();
                                            break;
                                        }
                                    }
                                    if(k == 0) {
                                        albums.get(tempPos).setName(input.getText().toString());
                                        Save.save(albums, context);
                                    }
                                }
                            }
                        });
                builder.setNegativeButton("Cancel",null);
                builder.setView(input);
                final AlertDialog dialog = builder.create();
                dialog.show();
                ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE)
                        .setEnabled(false);
                input.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (TextUtils.isEmpty(s)) {
                            ((AlertDialog) dialog).getButton(
                                    AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                        } else {
                            ((AlertDialog) dialog).getButton(
                                    AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                        }
                    }
                });
                return true;
            case R.id.delete:
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                albums.remove(tempPos);
                                mAdapter.notifyDataSetChanged();
                                Save.save(albums, context);
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("Are you sure you want to delete this album?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
                return true;
            default:
                 return super.onContextItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_search:
                Intent mIntent = new Intent(context, AlbumSearchActivity.class);
                context.startActivity(mIntent);
                return true;
            case R.id.info:
                String alert1 = "Enter new album bu clicking the check button";
                String alert2 = "To delete album: long click on the album";
                String alert3 = "To rename album: long click on the album";
                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.setTitle("How to use this page");
                alertDialog.setMessage(alert1 +"\n\n"+ alert2 +"\n\n"+ alert3);
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
