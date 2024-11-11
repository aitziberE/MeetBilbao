package com.example.meetbilbao;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

public class BECActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bec);

        ViewPager2 viewPager = findViewById(R.id.viewPager);

        List<Integer> images = new ArrayList<>();
        images.add(R.drawable.bec1);
        images.add(R.drawable.bec2);
        images.add(R.drawable.bec3);

        ImageAdapter adapter = new ImageAdapter(this, images);
        viewPager.setAdapter(adapter);

        viewPager.setCurrentItem(Integer.MAX_VALUE / 2, false);

        ImageView mapImage = findViewById(R.id.becmap);
        mapImage.setColorFilter(Color.parseColor("#505050"), PorterDuff.Mode.SRC_ATOP);
        mapImage.setOnClickListener(view -> {
            Uri gmmIntentUri = Uri.parse("geo:0,0?q=Bilbao+Exhibition+Centre,+Barakaldo,+Spain");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        });
    }


}
