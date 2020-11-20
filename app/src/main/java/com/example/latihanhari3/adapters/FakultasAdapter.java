package com.example.latihanhari3.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.IInterface;
import android.text.Layout;
import android.text.TextDirectionHeuristic;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.latihanhari3.FakultasActivity;
import com.example.latihanhari3.R;
import com.example.latihanhari3.TambahFakultasActivity;
import com.example.latihanhari3.helper.Constans;
import com.example.latihanhari3.helper.SqlAdapter;
import com.example.latihanhari3.models.Fakultas;

import java.util.List;

public class FakultasAdapter extends RecyclerView.Adapter<FakultasAdapter.ViewHolder> {

    private Context context;
    private List<Fakultas> fakultasList;

    public FakultasAdapter(Context context, List<Fakultas> fakultasList) {
        this.context = context;
        this.fakultasList = fakultasList;
    }

    @NonNull
    @Override
    public FakultasAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fakultas, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FakultasAdapter.ViewHolder holder, int position) {
        final Fakultas fakultas = fakultasList.get(position);
        holder.textViewFakultas.setText(fakultas.getNama_fakultas());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Fakultas " + fakultas.getNama_fakultas());

                builder.setNegativeButton("Hapus", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SqlAdapter dbAdapter = new SqlAdapter(context);
                        dbAdapter.open();

                        boolean hasil = dbAdapter.deleteFakultasById(fakultas.getId_fakultas());
                        dbAdapter.close();

                        if(hasil) {
                            Toast.makeText(context, "Data berhasil dihapus.", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(context, "Data gagal dihapus.", Toast.LENGTH_LONG).show();
                        }
                        notifyDataSetChanged();
                        ((FakultasActivity)context).getData();
                        dialogInterface.dismiss();
                    }
                });

                builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(context, TambahFakultasActivity.class);
                        intent.putExtra(Constans.IS_EDIT, true);
                        intent.putExtra(Constans.ID, fakultas.getId_fakultas());
                        intent.putExtra(Constans.NAMA, fakultas.getNama_fakultas());
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
        return fakultasList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewFakultas;
        RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewFakultas = itemView.findViewById(R.id.textViewFakultas);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
        }
    }
}
