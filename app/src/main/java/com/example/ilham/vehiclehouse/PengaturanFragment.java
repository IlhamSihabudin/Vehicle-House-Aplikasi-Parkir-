package com.example.ilham.vehiclehouse;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ilham.vehiclehouse.API.ParkirAPI;
import com.example.ilham.vehiclehouse.Model.User;
import com.example.ilham.vehiclehouse.Model.Value;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class PengaturanFragment extends Fragment {

    private static final String URL = "http://192.168.1.124/vehicle_house/";
    Button btnEdit,btnEditAkun , btnKeluar;
    List<User> user = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public PengaturanFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pengaturan, container, false);
        setHasOptionsMenu(true);

        btnEdit     = view.findViewById(R.id.btnEdit);
        btnKeluar   = view.findViewById(R.id.btnKeluar);
        btnEditAkun = view.findViewById(R.id.btnEditAkun);

        btnKeluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setTitle("Peringatan!");

                alertDialogBuilder.setMessage("Anda yakin ingin keluar dari akun ini?")
                        .setPositiveButton("Keluar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseHandler db = new DatabaseHandler(getActivity());
                                db.truncate();
                                startActivity(new Intent(getActivity(), UserLogin.class));
                                getActivity().finish();
                            }
                        })
                        .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();

                alertDialog.show();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHandler db = new DatabaseHandler(getActivity());
                String id_user = db.select();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                ParkirAPI api = retrofit.create(ParkirAPI.class);
                Call<Value> call = api.select_where(id_user);
                call.enqueue(new Callback<Value>() {
                    @Override
                    public void onResponse(Call<Value> call, Response<Value> response) {
                        user = response.body().getResult_user();
                        User user_data = user.get(0);

                        if (user_data.getStatus() == 0){
                            startActivity(new Intent(getActivity(), EditProfile.class));
                        }else{
                            Toast.makeText(getActivity(), "Tidak bisa mengubah profil ketika kendaraan sedang parkir!", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<Value> call, Throwable t) {
                        t.printStackTrace();
                    }
                });

            }
        });

        btnEditAkun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), EditAkun.class));
            }
        });


        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        ((Dashboard) getActivity()).getSupportActionBar().setTitle("Pengaturan");
        super.onCreateOptionsMenu(menu, inflater);
    }
}
