package com.example.latihanhari3.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.latihanhari3.FakultasActivity;
import com.example.latihanhari3.JurusanActivity;
import com.example.latihanhari3.R;
import com.example.latihanhari3.TambahFakultasActivity;
import com.example.latihanhari3.TambahJurusanActivity;
import com.example.latihanhari3.helper.Constans;
import com.example.latihanhari3.helper.SqlAdapter;
import com.example.latihanhari3.models.Fakultas;
import com.example.latihanhari3.models.Jurusan;

import java.util.List;

public class JurusanAdapter extends RecyclerView.Adapter<JurusanAdapter.ViewHolder> {
    private Context context;
    private List<Jurusan> jurusanList;

    public JurusanAdapter(Context context, List<Jurusan> jurusanList) {
        this.context = context;
        this.jurusanList = jurusanList;
    }

    @NonNull
    @Override
    public JurusanAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_jurusan, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JurusanAdapter.ViewHolder holder, int position) {
        final Jurusan jurusan = jurusanList.get(position);
        holder.textViewJurusan.setText(jurusan.getNama_jurusan());
        holder.textViewFakultas.setText(jurusan.getNama_fakultas());

        holder.relativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Jurusan " + jurusan.getNama_jurusan());
                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.select_dialog_item);
                arrayAdapter.add("Edit");
                arrayAdapter.add("Hapus");

                builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
                                Intent intent = new Intent(context, TambahJurusanActivity.class);
                                intent.putExtra(Constans.IS_EDIT, true);
                                intent.putExtra(Constans.ID_JURUSAN, jurusan.getId_jurusan());
                                intent.putExtra(Constans.NAMA, jurusan.getNama_jurusan());
                                intent.putExtra(Constans.ID, jurusan.getId_fakultas());

                                context.startActivity(intent);
                                break;
                            case 1:
                                SqlAdapter dbAdapter = new SqlAdapter(context);
                                dbAdapter.open();

                                boolean hasil = dbAdapter.deleteJurusan(jurusan.getId_jurusan());
                                dbAdapter.close();

                                if(hasil) {
                                    Toast.makeText(context, "Data berhasil dihapus.", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(context, "Data gagal dihapus.", Toast.LENGTH_LONG).show();
                                }
                                notifyDataSetChanged();
                                ((JurusanActivity)context).getData();
                                dialogInterface.dismiss();
                                break;
                        }
                    }
                });

                builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                builder.show();


                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return jurusanList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout relativeLayout;
        TextView textViewJurusan, textViewFakultas;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            relativeLayout = itemView.findViewById(R.id.relativeLayout);
            textViewJurusan = itemView.findViewById(R.id.textViewJurusan);
            textViewFakultas = itemView.findViewById(R.id.textViewFakultas);
        }
    }
}
