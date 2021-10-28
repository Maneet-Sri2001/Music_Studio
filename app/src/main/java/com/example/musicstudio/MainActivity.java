package com.example.musicstudio;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private String itemsAll[];
    private ListView songsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        externalStorage();
        songsList = findViewById(R.id.songsList);
    }

    public void externalStorage() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        displaySong();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                });
    }

    public ArrayList<File> readAudio(File file) {
        ArrayList<File> arrayList = new ArrayList<>();

        File[] allFile = file.listFiles();
        for (File singleFile : allFile) {
            if (!singleFile.isHidden() && singleFile.isDirectory())
                arrayList.addAll(readAudio(singleFile));
            else {
                if (singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".acc") || singleFile.getName().endsWith(".wma"))
                    arrayList.add(singleFile);
            }
        }
        return arrayList;
    }

    private void displaySong() {
        final ArrayList<File> song = readAudio(Environment.getExternalStorageDirectory());
        itemsAll = new String[song.size()];

        for (int count = 0; count<song.size(); count++) {
            itemsAll[count] = song.get(count).getName();
            Log.e("Song " + count, " : " + itemsAll);
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, itemsAll);
        songsList.setAdapter(arrayAdapter);
    }
}