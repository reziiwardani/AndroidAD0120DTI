package com.example.latihanhari3.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.latihanhari3.FakultasActivity;
import com.example.latihanhari3.MahasiswaActivity;
import com.example.latihanhari3.R;
import com.example.latihanhari3.TambahFakultasActivity;
import com.example.latihanhari3.TambahMahasiswaActivity;
import com.example.latihanhari3.helper.Constans;
import com.example.latihanhari3.helper.SqlAdapter;
import com.example.latihanhari3.models.Jurusan;
import com.example.latihanhari3.models.Mahasiswa;

import java.util.List;

public class MahasiswaAdapter extends RecyclerView.Adapter<MahasiswaAdapter.ViewHolder> {

    private Context context;
    private List<Mahasiswa> mahasiswaList;

    public MahasiswaAdapter(Context context, List<Mahasiswa> mahasiswaList) {
        this.context = context;
        this.mahasiswaList = mahasiswaList;
    }

    @NonNull
    @Override
    public MahasiswaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mahasiswa, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MahasiswaAdapter.ViewHolder holder, int position) {
        final Mahasiswa mahasiswa = mahasiswaList.get(position);
        holder.textViewNama.setText(mahasiswa.getNama());
        holder.textViewNim.setText(mahasiswa.getNim());
        holder.textViewJurusan.setText(mahasiswa.getNama_jurusan());
        holder.textViewTglLahir.setText(mahasiswa.getTgl_lahir());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(mahasiswa.getNama());

                builder.setNegativeButton("Hapus", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SqlAdapter dbAdapter = new SqlAdapter(context);
                        dbAdapter.open();

                        boolean hasil = dbAdapter.deleteMahasiswaById(mahasiswa.getId_mahasiswa());
                        dbAdapter.close();

                        if(hasil) {
                            Toast.makeText(context, "Data berhasil dihapus.", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(context, "Data gagal dihapus.", Toast.LENGTH_LONG).show();
                        }
                        notifyDataSetChanged();
                        ((MahasiswaActivity)context).getData();
                        dialogInterface.dismiss();
                    }
                });

                builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(context, TambahMahasiswaActivity.class);
                        intent.putExtra(Constans.IS_EDIT, true);

                        intent.putExtra(Constans.ID_JURUSAN, mahasiswa.getId_jurusan());
                        intent.putExtra(Constans.ID_MAHASISWA, mahasiswa.getId_mahasiswa());
                        intent.putExtra(Constans.NAMA, mahasiswa.getNama());
                        intent.putExtra(Constans.NIM, mahasiswa.getNim());
                        intent.putExtra(Constans.TGL_LAHIR, mahasiswa.getTgl_lahir());
                        context.startActivity(intent);
                    }
                });
                builder.setNeutralButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mahasiswaList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout relativeLayout;
        TextView textViewNama, textViewNim, textViewJurusan, textViewTglLahir;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            relativeLayout = itemView.findViewById(R.id.relativeLayout);
            textViewNama = itemView.findViewById(R.id.textViewNama);
            textViewNim = itemView.findViewById(R.id.textViewNim);
            textViewJurusan = itemView.findViewById(R.id.textViewJurusan);
            textViewTglLahir = itemView.findViewById(R.id.textViewTglLahir);
        }
    }
}
