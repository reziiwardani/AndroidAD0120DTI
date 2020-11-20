package com.example.latihanhari3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.latihanhari3.models.Fakultas;
import com.example.latihanhari3.models.Jurusan;
import com.example.latihanhari3.models.Mahasiswa;

public class MainActivity extends AppCompatActivity {

    Button buttonFakultas, buttonJurusan, buttonMahasiswa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        buttonFakultas = findViewById(R.id.buttonFakultas);
        buttonJurusan = findViewById(R.id.buttonJurusan);
        buttonMahasiswa = findViewById(R.id.buttonMahasiswa);

        buttonFakultas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FakultasActivity.class);
                startActivity(intent);
            }
        });

        buttonJurusan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, JurusanActivity.class);
                startActivity(intent);
            }
        });

        buttonMahasiswa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MahasiswaActivity.class);
                startActivity(intent);
            }
        });
    }
}