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

import com.example.latihanhari3.adapters.FakultasAdapter;
import com.example.latihanhari3.adapters.JurusanAdapter;
import com.example.latihanhari3.helper.SqlAdapter;
import com.example.latihanhari3.models.Fakultas;
import com.example.latihanhari3.models.Jurusan;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class JurusanActivity extends AppCompatActivity {

    ProgressBar progressBar;
    FloatingActionButton floatingActionButtonTambah;
    TextView textView;
    RecyclerView recyclerView;

    private SqlAdapter dbAdapter;
    List<Jurusan> list = new ArrayList<>();
    private JurusanAdapter adapter;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jurusan);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Jurusan");

        floatingActionButtonTambah = findViewById(R.id.floatingActionButtonTambah);
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.textView);

        floatingActionButtonTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(JurusanActivity.this, TambahJurusanActivity.class);
                startActivity(intent);
            }
        });

        getData();
    }

    public void getData() {

        list.clear();
        dbAdapter = new SqlAdapter(getBaseContext());
        dbAdapter.open();

        cursor = dbAdapter.getDataJurusanAll();
        cursor.moveToFirst();

        showLoading(true);
        Log.e("TAG", "" + cursor.getCount());

        if (cursor.getCount() > 0) {
            showLoading(false);

            while (cursor.isAfterLast() == false) {
                Jurusan jurusan = new Jurusan();
                jurusan.setId_jurusan(cursor.getInt(cursor.getColumnIndexOrThrow("id_jurusan")));
                jurusan.setNama_jurusan(cursor.getString(cursor.getColumnIndexOrThrow("nama_jurusan")));
                jurusan.setId_fakultas(cursor.getInt(cursor.getColumnIndexOrThrow("id_fakultas")));
                jurusan.setNama_fakultas(cursor.getString(cursor.getColumnIndexOrThrow("nama_fakultas")));

                list.add(jurusan);
                cursor.moveToNext();
            }

            adapter = new JurusanAdapter(this, list);
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
            textView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

    }
}