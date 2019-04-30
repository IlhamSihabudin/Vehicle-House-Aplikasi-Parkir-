package com.example.ilham.vehiclehouse.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ilham.vehiclehouse.Model.Parkir;
import com.example.ilham.vehiclehouse.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private Context context;
    private List<Parkir> parkir;

    public RecyclerViewAdapter(Context context, List<Parkir> parkir) {
        this.context = context;
        this.parkir = parkir;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_parkir, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Parkir parkirs = parkir.get(position);
        Date jamMasuk, jamKeluar;
        holder.txtNamaPetugas.setText(parkirs.getNama_petugas());
        holder.txtTotalBayar.setText(parkirs.getBayar());
        holder.txtJamMasuk.setText(parkirs.getJam_masuk() + " WIB");
        holder.txtJamKeluar.setText(parkirs.getJam_keluar() + " WIB");
    }

    @Override
    public int getItemCount() {
        return parkir.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.txtNamaPetugas) TextView txtNamaPetugas;
        @BindView(R.id.txtJamMasuk) TextView txtJamMasuk;
        @BindView(R.id.txtJamKeluar) TextView txtJamKeluar;
        @BindView(R.id.txtTotalBayar) TextView txtTotalBayar;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
