package com.example.meetbilbao;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
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
import androidx.viewpager2.widget.ViewPager2;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SanMamesActivity extends AppCompatActivity {

    private static final String DB_NAME = "dbMeetBilbao";
    private static final String TABLE_LOCATIONS = "locations";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_IMG = "img";

    private TextView tfTitle, tfDescription;
    private MediaPlayer mediaPlayer;
    private SeekBar sbAudioProgress;
    private boolean isPlaying = false;
    private Button btnPlayAnthem, btnMain;
    private Spinner spinnerLanguage;
    private WebView map;
    private ImageView img;
    private ViewPager2 carrusel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_san_mames);

        setupDB();
        initViews();
        setupHomeNavigation();
        setupAudioPlayer();
        setupLanguageSpinner();
        //setupMap();
        setupCarrusel();
        //setupMuseum();

    }

    private void setupDB(){
        SQLiteDatabase db = openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_LOCATIONS + " (" + COLUMN_NAME + " VARCHAR(20), " + COLUMN_IMG + " VARCHAR(100));");
        db.execSQL("INSERT INTO " + TABLE_LOCATIONS + " (" + COLUMN_NAME + ", " + COLUMN_IMG + ") VALUES ('San Mames', 'drawable/san_mames_outside_daylight');");
        db.close();
    }


    private void setupHomeNavigation() {
        btnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SanMamesActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
    private void setupAudioPlayer() {
        mediaPlayer = MediaPlayer.create(this, R.raw.team_anthem);
        sbAudioProgress.setMax(mediaPlayer.getDuration());

        sbAudioProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) mediaPlayer.seekTo(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        Runnable updateSeekBar = new Runnable() {
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

    private void resetAudioPlayer() {
        isPlaying = false;
        btnPlayAnthem.setText(R.string.anthem_play);
        sbAudioProgress.setProgress(0);
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
                changeLanguage(position == 1 ? "es" : "en");
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

//    private void setupMap(){
//        map.getSettings().setJavaScriptEnabled(true);
//
//        //7372+M7 Bilbao
//        String locationUrl = "https://maps.app.goo.gl/ssvGfNHxMBsH77HV9";
//
//        map.loadUrl(locationUrl);
//
//        map.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//                if (request.getUrl().toString().contains("maps")) {
//                    return false;
//                } else {
//                    return super.shouldOverrideUrlLoading(view, request);
//                }
//            }
//        });
//        map.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(locationUrl));
//                startActivity(intent);
//            }
//        });
//
////        ImageView mapImage = findViewById(R.id.becmap);
////        mapImage.setColorFilter(Color.parseColor("#505050"), PorterDuff.Mode.SRC_ATOP);
////        mapImage.setOnClickListener(view -> {
////            Uri gmmIntentUri = Uri.parse("geo:0,0?q=Bilbao+Exhibition+Centre,+Barakaldo,+Spain");
////            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
////            mapIntent.setPackage("com.google.android.apps.maps");
////            startActivity(mapIntent);
////        });
//    }

//        private void setupWebView(String url) {
//        map.getSettings().setJavaScriptEnabled(true);
//        map.loadUrl(url);
//        map.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//                return !request.getUrl().toString().contains("athletic");
//            }
//        });
//        map.setOnClickListener(v -> {
//            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//            startActivity(intent);
//        });
//    }
//
//    private void setupMuseum() {
//        setupWebView("https://sanmames.athletic-club.eus/museo/");
//    }

    private String setupImg() {
        SQLiteDatabase db = openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
        String imgUrl = null;
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_IMG + " FROM " + TABLE_LOCATIONS + " WHERE " + COLUMN_NAME + " = 'San Mames'", null);
        if (cursor.moveToFirst()) {
            imgUrl = cursor.getString(0);
        } else {
            Toast.makeText(this, "Location image can't be found", Toast.LENGTH_SHORT).show();
        }
        cursor.close();
        db.close();
        return imgUrl;
    }

    private void setupCarrusel() {
        List<Integer> imgUrlList = new ArrayList<>();

        String imageUrl = setupImg();

        if (imageUrl != null) {
            String resourceName = imageUrl.split("/")[1];

            int resId = getResources().getIdentifier(resourceName, "drawable", getPackageName());

            if (resId != 0) {
                imgUrlList.add(resId);
            } else {
                Toast.makeText(this, "Imagen no encontrada", Toast.LENGTH_SHORT).show();
            }
        }

        ImageAdapter adapter = new ImageAdapter(this, imgUrlList);
        carrusel.setAdapter(adapter);

        carrusel.setCurrentItem(Integer.MAX_VALUE / 2, false);
    }

    private void initViews() {

        btnMain = findViewById(R.id.btnMain);
        btnMain.setText(R.string.go_home);
        tfTitle = findViewById(R.id.tfTitle);
        tfTitle.setText(R.string.san_mames_title);
        tfDescription = findViewById(R.id.tfDescription);
        tfDescription.setText(R.string.san_mames_description);

        btnPlayAnthem = findViewById(R.id.btnPlayAnthem);
        sbAudioProgress = findViewById(R.id.sbAudioProgress);

        spinnerLanguage = findViewById(R.id.spinnerLanguage);

        map = findViewById(R.id.webViewMap);
        carrusel = findViewById(R.id.carrusel);
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
