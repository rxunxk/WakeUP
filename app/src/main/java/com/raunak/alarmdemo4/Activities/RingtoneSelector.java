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
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.raunak.alarmdemo4.Adapters.SongsAdapter;
import com.raunak.alarmdemo4.HelperClasses.DelayGenerator;
import com.raunak.alarmdemo4.Interfaces.SongSelectorInterface;
import com.raunak.alarmdemo4.R;
import java.util.ArrayList;

public class RingtoneSelector extends AppCompatActivity implements SongSelectorInterface {
    private int STORAGE_PERMISSION_CODE = 1;
    ArrayList<String> songNameArrayList,pathArrayList;
    RecyclerView recyclerView;
    SongsAdapter songsAdapter;
    private ShimmerFrameLayout mShimmerViewContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ringtonee_selector);

        mShimmerViewContainer = findViewById(R.id.shimmer_songs_container);

        getSupportActionBar().setTitle("Alarm tone");
        if(ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(getApplicationContext(), "Loading songs", Toast.LENGTH_SHORT).show();
            doStuff();
        }else{
            requestStoragePermission();
        }
    }

    public void requestStoragePermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
            new AlertDialog.Builder(this)
                    .setTitle("Permission Needed")
                    .setMessage("in order to display songs in your device we need STORAGE permissions. ")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(RingtoneSelector.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create().show();
        }else{
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
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
        songNameArrayList.add("Default song 1");
        songNameArrayList.add("Default song 2");
        pathArrayList.add("song1");
        pathArrayList.add("song2");

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
        DelayGenerator.delay(1, new DelayGenerator.DelayCallback() {
            @Override
            public void afterDelay() {
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE){
            if (permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "PERMISSION GRANTED", Toast.LENGTH_SHORT).show();
                doStuff();
            }else{
                Toast.makeText(this,"PERMISSION DENIED",Toast.LENGTH_SHORT);
            }
        }
    }

    @Override
    public void onItemClick(int position) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("SongName",pathArrayList.get(position));
        setResult(RESULT_OK,resultIntent);
        Toast.makeText(getApplicationContext(), ""+songNameArrayList.get(position), Toast.LENGTH_SHORT).show();
        finish();
    }
    @Override
    public void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmerAnimation();
    }

    @Override
    protected void onPause() {
        mShimmerViewContainer.stopShimmerAnimation();
        super.onPause();
    }
}