package com.example.latihanhari3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.latihanhari3.helper.Constans;
import com.example.latihanhari3.helper.SqlAdapter;
import com.example.latihanhari3.models.Fakultas;
import com.example.latihanhari3.models.Jurusan;

import java.util.ArrayList;
import java.util.List;

public class TambahJurusanActivity extends AppCompatActivity {

    Spinner spinnerFakultas;
    EditText editTextJurusan;
    Button buttonSimpan;

    private boolean isEdit = false;
    private int id;
    private int idFakultas = 1;

    private List<Fakultas> list = new ArrayList<>();
    private List<String> namaFakultas = new ArrayList<>();

    private int fakultasSelected = 0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_jurusan);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Tambah Jurusan");

        spinnerFakultas = findViewById(R.id.spinnerFakultas);
        editTextJurusan = findViewById(R.id.editTextJurusan);
        buttonSimpan = findViewById(R.id.buttonSimpan);

        buttonSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                simpanData();
            }
        });

        isEdit = getIntent().getBooleanExtra(Constans.IS_EDIT, false);

        if (isEdit) {
            setTitle("Edit Jurusan");
            id = getIntent().getIntExtra(Constans.ID_JURUSAN, 0);
            idFakultas = getIntent().getIntExtra(Constans.ID, 0);
            String nama = getIntent().getStringExtra(Constans.NAMA);
            editTextJurusan.setText(nama);
            buttonSimpan.setText("Update");
        }

        getDataFakultas();

        spinnerFakultas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Fakultas fakultas = list.get(i);
                idFakultas = fakultas.getId_fakultas();

                Log.e("TAG", fakultas.getId_fakultas() + "");

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void simpanData() {
        editTextJurusan.setError(null);
        boolean cancel =  false;
        View focusView = null;

        if (editTextJurusan.getText().toString().isEmpty()) {
            editTextJurusan.setError("Nama jurusan harus diisi!");
            cancel = true;
            focusView = editTextJurusan;
        }

        if(cancel) {
            focusView.requestFocus();
        } else {
            SqlAdapter dbAdapter = new SqlAdapter(getBaseContext());
            Jurusan jurusan = new Jurusan();
            jurusan.setNama_jurusan(editTextJurusan.getText().toString());
            jurusan.setId_fakultas(idFakultas);

            dbAdapter.open();

            if(isEdit) {
                jurusan.setId_jurusan(id);
                boolean hasil = dbAdapter.updateDataJurusan(jurusan);
                if(hasil) {
                    Toast.makeText(this, "Data berhasil diupdate.", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(TambahJurusanActivity.this, JurusanActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Data gagal diupdate.", Toast.LENGTH_LONG).show();
                }
            } else {
                boolean hasil = dbAdapter.insertJurusan(jurusan);
                if (hasil) {
                    Toast.makeText(getBaseContext(), "Data berhasil disimpan.", Toast.LENGTH_LONG).show();
                    dbAdapter.close();

                    Intent intent = new Intent(TambahJurusanActivity.this, JurusanActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getBaseContext(), "Data gagal disimpan.", Toast.LENGTH_LONG).show();
                    dbAdapter.close();
                }
            }
        }
    }

    private void getDataFakultas() {
        SqlAdapter dbAdapter = new SqlAdapter(this);
        dbAdapter.open();

        Cursor cursor;

        cursor = dbAdapter.getDataFakultasAll();
        cursor.moveToNext();
        if(cursor.getCount() > 0) {
            int i = 0;
            while (cursor.isAfterLast() == false) {
                Fakultas fakultas = new Fakultas();
                fakultas.setId_fakultas(cursor.getInt(cursor.getColumnIndexOrThrow("id_fakultas")));
                fakultas.setNama_fakultas(cursor.getString(cursor.getColumnIndexOrThrow("nama_fakultas")));

                list.add(fakultas);
                namaFakultas.add(fakultas.getNama_fakultas());

                Log.e("TAG", fakultas.getId_fakultas() + "");

                if(isEdit) {
                    if(idFakultas == fakultas.getId_fakultas()) {
                        fakultasSelected = i;
                    }
                }
                cursor.moveToNext();
                i++;
            }
        } else {
            Toast.makeText(this, "Data fakultas tidak ada.", Toast.LENGTH_LONG).show();
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, namaFakultas);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFakultas.setAdapter(arrayAdapter);
        spinnerFakultas.setSelection(fakultasSelected, true);
    }
}