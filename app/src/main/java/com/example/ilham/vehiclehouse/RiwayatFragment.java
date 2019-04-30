package com.example.ilham.vehiclehouse;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ilham.vehiclehouse.API.ParkirAPI;
import com.example.ilham.vehiclehouse.Adapter.RecyclerViewAdapter;
import com.example.ilham.vehiclehouse.Model.Parkir;
import com.example.ilham.vehiclehouse.Model.User;
import com.example.ilham.vehiclehouse.Model.Value;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RiwayatFragment extends Fragment {

    public static final String URL = "http://192.168.1.124/vehicle_house/";
    List<Parkir> parkir = new ArrayList<>();
    ProgressBar progressBar;
    RecyclerView recyclerView;
    TextView riwayatNotFound;
    RecyclerViewAdapter recyclerViewAdapter;

    public RiwayatFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_riwayat, container, false);
        setHasOptionsMenu(true);

        progressBar     = view.findViewById(R.id.progress_bar);
        recyclerView    = view.findViewById(R.id.recycler_view);
        riwayatNotFound = view.findViewById(R.id.riwayat_not_found);

        recyclerViewAdapter = new RecyclerViewAdapter(getActivity(), parkir);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recyclerViewAdapter);

        viewRiwayat();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        ((Dashboard) getActivity()).getSupportActionBar().setTitle("Riwayat");
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void viewRiwayat(){
        DatabaseHandler db = new DatabaseHandler(getActivity());
        String id_user = db.select();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ParkirAPI api = retrofit.create(ParkirAPI.class);
        Call<Value> call = api.select_riwayat_user(id_user);
        call.enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                String status = response.body().getStatus();

                progressBar.setVisibility(View.GONE);
                if (status.equals("1")){
                    parkir = response.body().getResult_parkir();
                    recyclerViewAdapter = new RecyclerViewAdapter(getActivity(), parkir);
                    recyclerView.setAdapter(recyclerViewAdapter);
                }else{
                    riwayatNotFound.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                riwayatNotFound.setText("Jaringan Error");
                riwayatNotFound.setVisibility(View.VISIBLE);
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        viewRiwayat();
    }
}
