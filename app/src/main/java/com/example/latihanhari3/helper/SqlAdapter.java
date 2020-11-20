package com.example.latihanhari3.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.latihanhari3.models.Fakultas;
import com.example.latihanhari3.models.Jurusan;
import com.example.latihanhari3.models.Mahasiswa;

import java.util.Currency;

public class SqlAdapter {
    private static final String DB_NAME = "latihan.db";
    private static final int VERSION = 1;
    private final Context context;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private static final String FAKULTAS = "fakultas";
    private static final String JURUSAN = "jurusan";
    private static final String MAHASISWA = "mahasiswa";

    private static final String TABEL_FAKULTAS = "CREATE TABLE fakultas" +
            "(id_fakultas INTEGER PRIMARY KEY AUTOINCREMENT, nama_fakultas TEXT NOT NULL)";

    private static final String TABEL_JURUSAN = "CREATE TABLE jurusan" +
            "(id_jurusan INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "id_fakultas INTEGER, nama_jurusan TEXT NOT NULL)";

    private static final String TABEL_MAHASISWA = "CREATE TABLE mahasiswa" +
            "(id_mahasiswa INTEGER PRIMARY KEY AUTOINCREMENT," +
            "id_jurusan INTEGER, " +
            "nim TEXT NOT NULL, " +
            "nama TEXT NOT NULL, " +
            "tgl_lahir TEXT)";
            // "tgl_lahir TEXT, " +
            // "jenis_kelamin INTEGER DEFAULT 1)";

    public SqlAdapter(Context context) {
        this.context = context;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, DB_NAME, null, VERSION);

        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(TABEL_FAKULTAS);
            sqLiteDatabase.execSQL(TABEL_JURUSAN);
            sqLiteDatabase.execSQL(TABEL_MAHASISWA);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FAKULTAS);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + JURUSAN);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MAHASISWA);
            onCreate(sqLiteDatabase);
        }
    }

    public SqlAdapter open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    // CRUD Fakultas
    // Create
    public boolean insertFakultas(Fakultas f) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("nama_fakultas", f.getNama_fakultas());
        return db.insert("fakultas", null, contentValues) > 0;
    }

    // Delete All
    public boolean deleteFakultasAll() {
        return db.delete("fakultas", null, null) > 0;
    }

    // Delete by Id
    public boolean deleteFakultasById(int id) {
        return db.delete("fakultas", "id_fakultas = " + id, null) > 0;
    }

    // Read All
    public Cursor getDataFakultasAll() {
        String[] kolom = {"id_fakultas", "nama_fakultas"};
        return db.query("fakultas", kolom, null, null, null,null, "id_fakultas DESC");
    }

    // Read by Id
    public Cursor getFakultasById(int id) {
        String[] kolom = {"id_fakultas", "nama_fakultas"};
        Cursor cursor = db.query("fakultas", kolom, "id_fakultas = " + id, null, null, null, null);
        if(cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    // Update
    public boolean updateFakultas(Fakultas f) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("nama_fakultas", f.getNama_fakultas());

        return db.update("fakultas", contentValues, "id_fakultas = " + f.getId_fakultas(), null) > 0;
    }

    // CRUD Jurusan
    // Create
    public boolean insertJurusan(Jurusan j) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("id_fakultas", j.getId_fakultas());
        contentValues.put("nama_jurusan", j.getNama_jurusan());
        return db.insert("jurusan", null, contentValues) > 0;
    }

    // Delete All
    public boolean deleteJurusanAll() {
        return db.delete("jurusan", null, null) > 0;
    }

    // Delete by Id
    public boolean deleteJurusan(int id) {
        return db.delete("jurusan", "id_jurusan = " + id, null) > 0;
    }

    // Read All
    public Cursor getDataJurusanAll() {
       String query = "SELECT * FROM fakultas f, jurusan j " +
               "WHERE f.id_fakultas = j.id_fakultas";
       return db.rawQuery(query, null);
    }

    // Read by Id
    public Cursor getJurusanById(int id) {
        String query = "SELECT * FROM fakultas f, jurusan j " +
                "WHERE f.id_fakultas = j.id_fakultas " +
                "AND j.id_fakultas = " + id;

        Cursor cursor =  db.rawQuery(query, null);
        if(cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    // Update
    public boolean updateDataJurusan(Jurusan j) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("id_fakultas", j.getId_fakultas());
        contentValues.put("nama_jurusan", j.getNama_jurusan());
        return db.update("jurusan", contentValues, "id_jurusan = " + j.getId_jurusan(), null) > 0;
    }

    // CRUD Mahasiswa
    // Insert
    public boolean insertMahasiswa(Mahasiswa m) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("id_jurusan", m.getId_jurusan());
        contentValues.put("nim", m.getNim());
        contentValues.put("nama", m.getNama());
        contentValues.put("tgl_lahir", m.getTgl_lahir());
//        contentValues.put("jenis_kelamin", m.getJenis_kelamin());

        return db.insert("mahasiswa", null, contentValues) > 0;
    }

    // Delete All
    public boolean deleteMahasiswaAll() {
        return db.delete("mahasiswa", null, null) > 0;
    }

    // Delete by Id
    public boolean deleteMahasiswaById(int id) {
        return db.delete("mahasiswa", "id_mahasiswa = " + id, null) > 0;
    }

    // Read All
    public Cursor getDataMahasiswaAll() {
        String query = "SELECT * FROM mahasiswa m, jurusan j " +
                "WHERE m.id_jurusan = j.id_jurusan ";
        return db.rawQuery(query, null);
    }

    // Read by Id
    public Cursor getMahasiswaById(int id) {
        String query = "SELECT * FROM mahasiswa m, jurusan j " +
                "WHERE m.id_jurusan = j.id_jurusan " +
                "AND m.id_mahasiswa = " + id;

        Cursor cursor =  db.rawQuery(query, null);
        if(cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public boolean updateDataMahasiswa(Mahasiswa m) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("id_jurusan", m.getId_jurusan());
        contentValues.put("nim", m.getNim());
        contentValues.put("nama", m.getNama());
        contentValues.put("tgl_lahir", m.getTgl_lahir());
        // contentValues.put("jenis_kelamin", m.getJenis_kelamin());

        return db.update("mahasiswa", contentValues, "id_mahasiswa = " + m.getId_mahasiswa(), null) > 0;
    }
}
