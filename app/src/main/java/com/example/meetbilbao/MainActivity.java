package com.example.meetbilbao;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageButton ibBilbao = findViewById(R.id.IBBilbao);
        ibBilbao.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onClickBilbao(v);
            }
        });

        ImageButton ibBec = findViewById(R.id.IBBec);
        ibBec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickBec(v);
            }
        });

        ImageButton ibGuggenheim = findViewById(R.id.IBGuggenheim);
        ibGuggenheim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickGuggenheim(v);
            }
        });

        ImageButton ibSanMames = findViewById(R.id.IBSanMames);
        ibSanMames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSanMames(v);
            }
        });

    }

    private void onClickBilbao(View v) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.bilbao.eus"));

        try {
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "Error al abrir el sitio web", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickBec(View v) {
        try {
            Intent intent = new Intent(MainActivity.this, BECActivity.class);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Error al abrir la actividad BEC", Toast.LENGTH_SHORT).show();
        }
    }

    private void onClickGuggenheim(View v) {
        try {
            Intent intent = new Intent(MainActivity.this, GuggenheimActivity.class);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Error al abrir la actividad Guggenheim", Toast.LENGTH_SHORT).show();
        }
    }

    private void onClickSanMames(View v) {
        try {
            Intent intent = new Intent(MainActivity.this, SanMamesActivity.class);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Error al abrir la actividad San Mamés", Toast.LENGTH_SHORT).show();
        }
    }
}
