package com.example.latihanhari3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.latihanhari3.adapters.JurusanAdapter;
import com.example.latihanhari3.adapters.MahasiswaAdapter;
import com.example.latihanhari3.helper.SqlAdapter;
import com.example.latihanhari3.models.Jurusan;
import com.example.latihanhari3.models.Mahasiswa;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MahasiswaActivity extends AppCompatActivity {

    ProgressBar progressBar;
    FloatingActionButton floatingActionButtonTambah;
    TextView textView;
    RecyclerView recyclerView;

    private SqlAdapter dbAdapter;
    List<Mahasiswa> list = new ArrayList<>();
    private MahasiswaAdapter adapter;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mahasiswa);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Mahasiswa");

        floatingActionButtonTambah = findViewById(R.id.floatingActionButtonTambah);
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.textView);

        floatingActionButtonTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MahasiswaActivity.this, TambahMahasiswaActivity.class);
                startActivity(intent);
            }
        });

        getData();
    }

    public void getData() {
        list.clear();
        dbAdapter = new SqlAdapter(getBaseContext());
        dbAdapter.open();

        cursor = dbAdapter.getDataMahasiswaAll();
        cursor.moveToFirst();

        showLoading(true);
        Log.e("TAG", "" + cursor.getCount());

        if (cursor.getCount() > 0) {
            showLoading(false);

            while (cursor.isAfterLast() == false) {
                Mahasiswa mahasiswa = new Mahasiswa();
                mahasiswa.setId_mahasiswa(cursor.getInt(cursor.getColumnIndexOrThrow("id_mahasiswa")));
                mahasiswa.setNama(cursor.getString(cursor.getColumnIndexOrThrow("nama")));
                mahasiswa.setNim(cursor.getString(cursor.getColumnIndexOrThrow("nim")));
                mahasiswa.setTgl_lahir(cursor.getString(cursor.getColumnIndexOrThrow("tgl_lahir")));
                mahasiswa.setId_jurusan(cursor.getInt(cursor.getColumnIndexOrThrow("id_jurusan")));
                mahasiswa.setNama_jurusan(cursor.getString(cursor.getColumnIndexOrThrow("nama_jurusan")));
//                mahasiswa.setJenis_kelamin(cursor.getInt(cursor.getColumnIndexOrThrow("jenis_kelamin")));

                list.add(mahasiswa);
                cursor.moveToNext();
            }

            adapter = new MahasiswaAdapter(this, list);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);

        } else {
            showLoading(false);
        }
        dbAdapter.close();
    }

    private void showLoading(boolean b) {
        if(b) {
            floatingActionButtonTambah.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
            floatingActionButtonTambah.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
}