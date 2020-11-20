package com.example.latihanhari3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.latihanhari3.helper.Constans;
import com.example.latihanhari3.helper.SqlAdapter;
import com.example.latihanhari3.models.Fakultas;

public class TambahFakultasActivity extends AppCompatActivity {

    EditText editTextFakultas;
    Button buttonSimpan;

    // Untuk Edit
    private boolean isEdit = false;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_fakultas);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Tambah Fakultas");

        editTextFakultas = findViewById(R.id.editTextFakultas);
        buttonSimpan = findViewById(R.id.buttonSimpan);

        buttonSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                simpanData();
            }
        });

        isEdit = getIntent().getBooleanExtra(Constans.IS_EDIT, false);

        if(isEdit) {
            setTitle("Edit Fakultas");
            id = getIntent().getIntExtra(Constans.ID, 0);
            String nama = getIntent().getStringExtra(Constans.NAMA);
            editTextFakultas.setText(nama);
            buttonSimpan.setText("Update");
        }
    }

    private void simpanData() {
        editTextFakultas.setError(null);
        boolean cancel =  false;
        View focusView = null;

        if (editTextFakultas.getText().toString().isEmpty()) {
            editTextFakultas.setError("Nama fakultas harus diisi!");
            cancel = true;
            focusView = editTextFakultas;
        }

        if(cancel) {
            focusView.requestFocus();
        } else {
            SqlAdapter dbAdapter = new SqlAdapter(getBaseContext());
            Fakultas fakultas = new Fakultas();
            fakultas.setNama_fakultas(editTextFakultas.getText().toString());

            dbAdapter.open();

            if(isEdit) {
                fakultas.setId_fakultas(id);
                boolean hasil = dbAdapter.updateFakultas(fakultas);
                if(hasil) {
                    Toast.makeText(this, "Data berhasil diupdate.", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(this, FakultasActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Data gagal diupdate.", Toast.LENGTH_LONG).show();
                }
            } else {
                boolean hasil = dbAdapter.insertFakultas(fakultas);
                if (hasil) {
                    Toast.makeText(getBaseContext(), "Data berhasil disimpan.", Toast.LENGTH_LONG).show();
                    dbAdapter.close();

                    Intent intent = new Intent(TambahFakultasActivity.this, FakultasActivity.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(getBaseContext(), "Data gagal disimpan.", Toast.LENGTH_LONG).show();
                    dbAdapter.close();
                }
            }
        }
    }
}