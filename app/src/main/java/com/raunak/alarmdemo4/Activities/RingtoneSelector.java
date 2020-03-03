package com.raunak.alarmdemo4.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;import android.widget.Toast;
import com.raunak.alarmdemo4.Adapters.SongsAdapter;
import com.raunak.alarmdemo4.Interfaces.SongSelectorInterface;
import com.raunak.alarmdemo4.R;
import java.util.ArrayList;

public class RingtoneSelector extends AppCompatActivity implements SongSelectorInterface {
    private final int MY_PERMISSION_REQUEST = 1;
    ArrayList<String> songNameArrayList,pathArrayList;
    RecyclerView recyclerView;
    SongsAdapter songsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ringtonee_selector);

        getSupportActionBar().setTitle("Alarm tone");
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tone_menu,menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView)menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                songsAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public void doStuff(){
        recyclerView = findViewById(R.id.songsList);
        songNameArrayList = new ArrayList<>();
        pathArrayList = new ArrayList<>();
        getMusic();
        songsAdapter = new SongsAdapter(songNameArrayList,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(songsAdapter);
        recyclerView.setHasFixedSize(true);
        //DividerItemDecoration class is used for getting a vertical line between rows of RecyclerView
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
    }

    public void getMusic(){
        ContentResolver contentResolver = getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(uri,null,null,null,null);

        if (cursor != null && cursor.moveToFirst()){
            int songTittle = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int path = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);

            do {
                String currentTittle = cursor.getString(songTittle);
                String currentPath = cursor.getString(path);
                songNameArrayList.add(currentTittle);
                pathArrayList.add(currentPath);
            }while (cursor.moveToNext());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case MY_PERMISSION_REQUEST:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if (ContextCompat.checkSelfPermission(RingtoneSelector.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                    {
                        Toast.makeText(this, "No permission granted", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    return;
                }
            }
        }
    }

    @Override
    public void onItemClick(int position) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("SongName",pathArrayList.get(position));
        setResult(RESULT_OK,resultIntent);
        Toast.makeText(getApplicationContext(), ""+pathArrayList.get(position), Toast.LENGTH_SHORT).show();
        finish();
    }
}