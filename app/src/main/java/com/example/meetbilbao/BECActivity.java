package com.example.meetbilbao;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;
import java.util.ArrayList;
import java.util.List;

public class BECActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int AUDIO_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bec);

        // Referencias a los elementos de la UI
        ViewPager2 viewPager = findViewById(R.id.viewPager);
        ImageButton btnCamara = findViewById(R.id.btnCamara);

        // Lista de imágenes para el carrusel
        List<Integer> images = new ArrayList<>();
        images.add(R.drawable.bec1); // Asegúrate de que estos recursos existan
        images.add(R.drawable.bec2);
        images.add(R.drawable.bec3);

        // Configura el adaptador
        ImageAdapter adapter = new ImageAdapter(this, images);
        viewPager.setAdapter(adapter);

        // Configura el carrusel para desplazamiento infinito (opcional)
        viewPager.setCurrentItem(Integer.MAX_VALUE / 2, false);

        // Mapa interactivo para abrir Google Maps
        ImageView mapImage = findViewById(R.id.becmap);


        mapImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=Bilbao+Exhibition+Centre,+Barakaldo,+Spain");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        // Configura el botón de retorno a MainActivity
        Button btnReturn = findViewById(R.id.btnReturn);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un Intent para regresar a MainActivity
                Intent intent = new Intent(BECActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Configura el botón de la cámara
        btnCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });
        ImageButton btnAudio = findViewById(R.id.btnAudio);

        // Configura el botón para grabar audio
        btnAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAudioRecorder();
            }
        });
    }

    // Método para abrir la cámara
    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Verificar si la cámara está disponible en el dispositivo
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
        } else {
            Toast.makeText(this, "No hay una cámara disponible", Toast.LENGTH_SHORT).show();
        }
    }

    private void openAudioRecorder() {
        Intent audioIntent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);

        // Verificar si hay una aplicación para grabar audio
        if (audioIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(audioIntent, AUDIO_REQUEST_CODE);
        } else {
            Toast.makeText(this, "No se encontró una aplicación para grabar audio", Toast.LENGTH_SHORT).show();
        }
    }

    // Manejo del resultado de la cámara (si es necesario)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            // Aquí podrías manejar la foto si fuera necesario
        }
        if (requestCode == AUDIO_REQUEST_CODE && resultCode == RESULT_OK) {
            // Aquí puedes manejar el archivo de audio grabado si es necesario
            Uri audioUri = data.getData(); // Obtienes la URI del archivo de audio grabado
            Toast.makeText(this, "Audio grabado exitosamente: " + audioUri.toString(), Toast.LENGTH_SHORT).show();
        }
    }




}
