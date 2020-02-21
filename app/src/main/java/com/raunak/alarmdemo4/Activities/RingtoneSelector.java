package com.raunak.alarmdemo4.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.raunak.alarmdemo4.Adapters.SongsAdapter;
import com.raunak.alarmdemo4.R;

import java.util.ArrayList;

public class RingtoneSelector extends AppCompatActivity {
    private final int MY_PERMISSION_REQUEST = 1;
    ArrayList<String> arrayList;
    RecyclerView recyclerView;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ringtonee_selector);

        if(ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(RingtoneSelector.this,
            Manifest.permission.READ_EXTERNAL_STORAGE)){
                ActivityCompat.requestPermissions(RingtoneSelector.this,
                        new String[]{   Manifest.permission.READ_EXTERNAL_STORAGE},MY_PERMISSION_REQUEST);
            }else{
                ActivityCompat.requestPermissions(RingtoneSelector.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},MY_PERMISSION_REQUEST);
            }
        }else{
            doStuff();
        }
    }

    public void doStuff(){
        recyclerView = findViewById(R.id.songsList);
        arrayList = new ArrayList<>();
        getMusic();
        SongsAdapter songsAdapter = new SongsAdapter(arrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(songsAdapter);
        recyclerView.setHasFixedSize(true);
    }

    public void getMusic(){
        ContentResolver contentResolver = getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(uri,null,null,null,null);

        if (cursor != null && cursor.moveToFirst()){
            int songTittle = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);

            do {
                String currentTittle = cursor.getString(songTittle);
                String currentArtist = cursor.getString(songArtist);
                arrayList.add(currentTittle +"\n"+currentArtist);
            }while (cursor.moveToNext());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case MY_PERMISSION_REQUEST:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if (ContextCompat.checkSelfPermission(RingtoneSelector.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this, "No permission granted", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    return;
                }
            }
        }
    }
}
