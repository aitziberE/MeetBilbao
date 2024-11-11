package com.example.meetbilbao;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.VideoView;

public class GuggenheimActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guggenheim);

        VideoView videoView = findViewById(R.id.videoView);
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.guggenheim_video;
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);

        videoView.setOnPreparedListener(mediaPlayer -> {
            mediaPlayer.setVolume(0f, 0f);
            mediaPlayer.start();
        });

        ImageView mapImage = findViewById(R.id.guggenheimMap);
        mapImage.setColorFilter(Color.parseColor("#505050"), PorterDuff.Mode.SRC_ATOP);
        mapImage.setOnClickListener(view -> {
            Uri gmmIntentUri = Uri.parse("geo:0,0?q=Museo+Guggenheim+Bilbao,+Bilbao,+Spain");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        });
    }


}
