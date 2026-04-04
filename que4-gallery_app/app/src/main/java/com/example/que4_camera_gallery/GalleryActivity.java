package com.example.que4_camera_gallery;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class GalleryActivity extends AppCompatActivity {

    private GridView gridView;
    private ArrayList<File> imageFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        // Corrected ID to match activity_gallery.xml
        gridView = findViewById(R.id.galleryGridView);
        loadImages();

        gridView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(GalleryActivity.this, ImageDetailActivity.class);
            intent.putExtra("imagePath", imageFiles.get(position).getAbsolutePath());
            startActivity(intent);
        });
    }

    private void loadImages() {
        imageFiles = new ArrayList<>();
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (storageDir != null && storageDir.exists()) {
            File[] files = storageDir.listFiles();
            if (files != null) {
                // Sort files by last modified to show newest first
                Arrays.sort(files, (f1, f2) -> Long.compare(f2.lastModified(), f1.lastModified()));
                for (File file : files) {
                    if (file.getName().endsWith(".jpg") || file.getName().endsWith(".png")) {
                        imageFiles.add(file);
                    }
                }
            }
        }
        ImageAdapter imageAdapter = new ImageAdapter(this, imageFiles);
        gridView.setAdapter(imageAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadImages(); // Refresh the grid if an image was deleted
    }
}
