package com.example.latihanhari3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.latihanhari3.helper.Constans;
import com.example.latihanhari3.helper.SqlAdapter;
import com.example.latihanhari3.models.Fakultas;
import com.example.latihanhari3.models.Jurusan;
import com.example.latihanhari3.models.Mahasiswa;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TambahMahasiswaActivity extends AppCompatActivity {

    Spinner spinnerJurusan, spinnerJenisKelamin;
    EditText editTextNama, editTextNim, editTextTglLahir;
    Button buttonSimpan;

    private static final SimpleDateFormat formater = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
    DatePickerDialog datePickerDialog;
    Calendar calendar;
    private int mYear, mMonth, mDay;

    private boolean isEdit = false;
    private int id;
    private int idJenisKelamin = 1;
    private int idMahasiswa;

    private List<Jurusan> list = new ArrayList<>();
    private List<String> namaJurusan = new ArrayList<>();

    private String[] listJenisKelamin = {"Laki-laki", "Perempuan"};

    private int jurusanSelected = 0;
    private int jenisKelaminSelected = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_mahasiswa);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Tambah Mahasiswa");


        spinnerJurusan = findViewById(R.id.spinnerJurusan);
        spinnerJenisKelamin = findViewById(R.id.spinnerJenisKelamin);
        editTextNama = findViewById(R.id.editTextNama);
        editTextNim = findViewById(R.id.editTextNim);
        editTextTglLahir = findViewById(R.id.editTextTglLahir);
        buttonSimpan = findViewById(R.id.buttonSimpan);

        setTanggalLahir();

        buttonSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                simpanData();
            }
        });

        isEdit = getIntent().getBooleanExtra(Constans.IS_EDIT, false);

        if (isEdit) {
            setTitle("Edit Mahasiswa");
            idMahasiswa = getIntent().getIntExtra(Constans.ID_MAHASISWA, 0);
            id = getIntent().getIntExtra(Constans.ID_JURUSAN, 0);
            String nama = getIntent().getStringExtra(Constans.NAMA);
            String nim = getIntent().getStringExtra(Constans.NIM);
            String tglLahir = getIntent().getStringExtra(Constans.TGL_LAHIR);

            editTextNama.setText(nama);
            editTextNim.setText(nim);
            editTextTglLahir.setText(tglLahir);
            buttonSimpan.setText("Update");
        }

        getDataJurusan();

        spinnerJurusan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Jurusan jurusan = list.get(i);
                id = jurusan.getId_jurusan();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setTanggalLahir() {
        editTextTglLahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mCurrentDate = Calendar.getInstance();
                mYear = mCurrentDate.get(Calendar.YEAR);
                mMonth = mCurrentDate.get(Calendar.MONTH);
                mDay = mCurrentDate.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(TambahMahasiswaActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        calendar = Calendar.getInstance();
                        calendar.set(i, i1, i2);
                        editTextTglLahir.setText(formater.format(calendar.getTime()));
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

    }

    private void getDataJurusan() {
        SqlAdapter dbAdapter = new SqlAdapter(this);
        dbAdapter.open();

        Cursor cursor;

        cursor = dbAdapter.getDataJurusanAll();
        cursor.moveToNext();
        if(cursor.getCount() > 0) {
            int i = 0;
            while (cursor.isAfterLast() == false) {
                Jurusan jurusan = new Jurusan();
                jurusan.setId_jurusan(cursor.getInt(cursor.getColumnIndexOrThrow("id_jurusan")));
                jurusan.setNama_jurusan(cursor.getString(cursor.getColumnIndexOrThrow("nama_jurusan")));

                list.add(jurusan);
                namaJurusan.add(jurusan.getNama_jurusan());

                Log.e("TAG", jurusan.getId_jurusan() + "");

                if(isEdit) {
                    if(id == jurusan.getId_jurusan()) {
                        jurusanSelected = i;
                    }
                }
                cursor.moveToNext();
                i++;
            }
        } else {
            Toast.makeText(this, "Data jurusan tidak ada.", Toast.LENGTH_LONG).show();
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, namaJurusan);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerJurusan.setAdapter(arrayAdapter);
        spinnerJurusan.setSelection(jurusanSelected, true);

    }

    private void simpanData() {
        editTextNama.setError(null);
        editTextNim.setError(null);
        editTextTglLahir.setError(null);
        boolean cancel =  false;
        View focusView = null;

        if (editTextNama.getText().toString().isEmpty()) {
            editTextNama.setError("Nama harus diisi!");
            cancel = true;
            focusView = editTextNama;

        } else if (editTextNim.getText().toString().isEmpty()) {
            editTextNim.setError("NIM harus diisi!");
            cancel = true;
            focusView = editTextNim;
        }

        if(cancel) {
            focusView.requestFocus();
        } else {
            SqlAdapter dbAdapter = new SqlAdapter(getBaseContext());
            Mahasiswa mahasiswa = new Mahasiswa();
            mahasiswa.setNama(editTextNama.getText().toString());
            mahasiswa.setNim(editTextNim.getText().toString());
            mahasiswa.setTgl_lahir(editTextTglLahir.toString());
            mahasiswa.setId_jurusan(id);

            dbAdapter.open();

            if (isEdit) {
                mahasiswa.setId_mahasiswa(idMahasiswa);
                boolean hasil = dbAdapter.updateDataMahasiswa(mahasiswa);
                if (hasil) {
                    Toast.makeText(this, "Data berhasil diupdate.", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(TambahMahasiswaActivity.this, MahasiswaActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Data gagal diupdate.", Toast.LENGTH_LONG).show();
                }
            } else {
                boolean hasil = dbAdapter.insertMahasiswa(mahasiswa);
                if (hasil) {
                    Toast.makeText(getBaseContext(), "Data berhasil disimpan.", Toast.LENGTH_LONG).show();
                    dbAdapter.close();

                    Intent intent = new Intent(TambahMahasiswaActivity.this, MahasiswaActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getBaseContext(), "Data gagal disimpan.", Toast.LENGTH_LONG).show();
                    dbAdapter.close();
                }
            }
        }
    }
}