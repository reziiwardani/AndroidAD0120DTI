package com.example.latihanhari3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.latihanhari3.adapters.FakultasAdapter;
import com.example.latihanhari3.helper.SqlAdapter;
import com.example.latihanhari3.models.Fakultas;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class FakultasActivity extends AppCompatActivity {

    ProgressBar progressBar;
    FloatingActionButton floatingActionButtonTambah;
    TextView textView;
    RecyclerView recyclerView;

    private SqlAdapter dbAdapter;
    List<Fakultas> list = new ArrayList<>();
    private FakultasAdapter adapter;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fakultas);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Fakultas");

        floatingActionButtonTambah = findViewById(R.id.floatingActionButtonTambah);
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.textView);


        floatingActionButtonTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FakultasActivity.this, TambahFakultasActivity.class);
                startActivity(intent);
            }
        });

        getData();

    }

    public void getData() {

        list.clear();
        dbAdapter = new SqlAdapter(getBaseContext());
        dbAdapter.open();

        cursor = dbAdapter.getDataFakultasAll();
        cursor.moveToFirst();

        floatingActionButtonTambah.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        if(cursor.getCount() > 0) {
            floatingActionButtonTambah.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);

            while (cursor.isAfterLast() == false) {
                Fakultas fakultas = new Fakultas();
                fakultas.setId_fakultas(cursor.getInt(cursor.getColumnIndexOrThrow("id_fakultas")));
                fakultas.setNama_fakultas(cursor.getString(cursor.getColumnIndexOrThrow("nama_fakultas")));

                list.add(fakultas);
                cursor.moveToNext();
            }

            adapter = new FakultasAdapter(this, list);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);

        } else {
            progressBar.setVisibility(View.GONE);
            floatingActionButtonTambah.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
        }
        dbAdapter.close();

    }
}