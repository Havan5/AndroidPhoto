package hp.photoappandroid94.util;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import hp.photoappandroid94.model.Album;

public class Save {

    private static String fileName = "album.ser";

    public static void save(List<Album> albums, Context context){
        FileOutputStream fos;
        try {
            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(albums);
            os.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Album> loadData(Context context){
        List<Album> albums = new ArrayList<>();
        FileInputStream fis;
        ObjectInput ois;
        try {
            fis = context.openFileInput(fileName);
            ois = new ObjectInputStream(fis);
            albums.addAll((List<Album>) ois.readObject());
            ois.close();
            fis.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return albums;
    }
}
