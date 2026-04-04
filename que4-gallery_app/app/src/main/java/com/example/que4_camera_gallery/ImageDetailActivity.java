package com.example.que4_camera_gallery;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.Date;

public class ImageDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);

        ImageView detailImageView = findViewById(R.id.detailImage);
        TextView detailInfo = findViewById(R.id.detailInfo);
        Button btnDelete = findViewById(R.id.btnDelete);

        String path = getIntent().getStringExtra("imagePath");
        if (path != null) {
            File file = new File(path);
            if (file.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                detailImageView.setImageBitmap(myBitmap);

                String infoText = "Name: " + file.getName() +
                        "\n\nPath: " + file.getAbsolutePath() +
                        "\n\nSize: " + (file.length() / 1024) + " KB" +
                        "\n\nDate: " + new Date(file.lastModified());
                detailInfo.setText(infoText);

                btnDelete.setOnClickListener(v -> {
                    new AlertDialog.Builder(this)
                            .setTitle("Delete Photo")
                            .setMessage("Are you sure you want to delete this photo?")
                            .setPositiveButton("Delete", (dialog, which) -> {
                                if (file.delete()) {
                                    Toast.makeText(this, "File Deleted", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(this, "Delete Failed", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("Cancel", null)
                            .show();
                });
            } else {
                Toast.makeText(this, "File not found", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, "No image path provided", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
