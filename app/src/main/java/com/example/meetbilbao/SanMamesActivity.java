package com.example.meetbilbao;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.util.Locale;

public class SanMamesActivity extends AppCompatActivity {

    private TextView tfTitle, tfDescription;
    private MediaPlayer mediaPlayer;
    private SeekBar sbAudioProgress;
    private boolean isPlaying = false;
    private Button btnPlayAnthem, btnBookTour, btnLearnMore, btnCaptureMedia;
    private Spinner spinnerLanguage;
    private WebView map;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_san_mames);

        initViews();
        setupAudioPlayer();
        setupLanguageSpinner();
        setupMap();
        setupImg();

//        Button btnSanMames = findViewById(R.id.btnSanMames);
//
//        btnSanMames.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, SanMamesActivity.class);
//                startActivity(intent);
//            }
//        });
    }

    private void setupAudioPlayer() {
        mediaPlayer = MediaPlayer.create(this, R.raw.team_anthem);
        sbAudioProgress.setMax(mediaPlayer.getDuration());

        sbAudioProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        final Runnable updateSeekBar = new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null && isPlaying) {
                    sbAudioProgress.setProgress(mediaPlayer.getCurrentPosition());
                    sbAudioProgress.postDelayed(this, 1000); // Update every second
                }
            }
        };

        mediaPlayer.setOnCompletionListener(mp -> {
            isPlaying = false;
            btnPlayAnthem.setText(R.string.anthem_play);
            sbAudioProgress.setProgress(0);
        });

        btnPlayAnthem.setOnClickListener(v -> {
            if (isPlaying) {
                mediaPlayer.pause();
                btnPlayAnthem.setText(R.string.anthem_play);
            } else {
                mediaPlayer.start();
                btnPlayAnthem.setText(R.string.anthem_pause);
                sbAudioProgress.postDelayed(updateSeekBar, 1000);
            }
            isPlaying = !isPlaying;
        });
    }

    private void setupLanguageSpinner() {
        String[] languages = {"English", "Español"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, languages);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLanguage.setAdapter(adapter);

        spinnerLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedLanguage = parent.getItemAtPosition(position).toString();

                String languageCode = "en";
                if (selectedLanguage.equals("Español")) {
                    languageCode = "es";
                } else if (selectedLanguage.equals("English")) {
                    languageCode = "en";
                }

                changeLanguage(languageCode);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void changeLanguage(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        
        Intent refresh = new Intent(this, SanMamesActivity.class);
        startActivity(refresh);
        finish();
    }

    private void setupMap(){
        map.getSettings().setJavaScriptEnabled(true);

        //7372+M7 Bilbao
        String locationUrl = "https://maps.app.goo.gl/ssvGfNHxMBsH77HV9";

        map.loadUrl(locationUrl);

        map.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if (request.getUrl().toString().contains("maps")) {
                    return false;
                } else {
                    return super.shouldOverrideUrlLoading(view, request);
                }
            }
        });
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(locationUrl));
                startActivity(intent);
            }
        });
    }
    private void setupDB(){
        SQLiteDatabase db = null;
        db=openOrCreateDatabase("dbMeetBilbao", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS locations (name VARCHAR(20), img VARCHAR(100));");
        db.execSQL("INSERT INTO locations (name, img) VALUES ('San Mames', 'https://www.dazn.com/es-ES/news/f%C3%BAtbol/por-que-san-mames-llaman-la-catedral-razon-nombre-estadio-athletic-club/omyldyqeyby41so2unfxgvh6h');");
        db.close();
    }
    private void setupImg(){
        SQLiteDatabase db = openOrCreateDatabase("dbMeetBilbao", Context.MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("SELECT img FROM locations WHERE name = 'San Mames'", null);
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "Location image can't be found", Toast.LENGTH_SHORT).show();
        } else {
            cursor.moveToFirst();
            String imageUrl = cursor.getString(0);
            Picasso.get().load(imageUrl).into(img);
            cursor.close();
        }
        db.close();
    }

    private void initViews() {

        tfTitle = findViewById(R.id.tfTitle);
        tfTitle.setText(R.string.san_mames_title);
        tfDescription = findViewById(R.id.tfDescription);
        tfDescription.setText(R.string.san_mames_description);

        btnPlayAnthem = findViewById(R.id.btnPlayAnthem);
        sbAudioProgress = findViewById(R.id.sbAudioProgress);

        spinnerLanguage = findViewById(R.id.spinnerLanguage);

        img = findViewById(R.id.img);
        map = findViewById(R.id.webViewMap);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
