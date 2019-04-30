package com.example.ilham.vehiclehouse;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ilham.vehiclehouse.API.ParkirAPI;
import com.example.ilham.vehiclehouse.Model.User;
import com.example.ilham.vehiclehouse.Model.Value;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {
    private static final String URL = "http://192.168.1.124/vehicle_house/";
    ProgressDialog progress;
    List<User> user = new ArrayList<>();

    ImageView mJenisKendaraan;
    TextView txtNama, txtPlatNomor, txtMerkKendataan, txtContact;
    CardView statParkir, statTidakParkir;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        View view   = inflater.inflate(R.layout.fragment_home, container, false);

        mJenisKendaraan     = view.findViewById(R.id.gJenisKendaraan);
        txtNama             = view.findViewById(R.id.txt_nama);
        txtPlatNomor        = view.findViewById(R.id.txt_plat_nomor);
        txtMerkKendataan    = view.findViewById(R.id.txt_merek_kendaraan);
        txtContact          = view.findViewById(R.id.txt_contact);
        statParkir          = view.findViewById(R.id.stat_parkir);
        statTidakParkir     = view.findViewById(R.id.stat_tidak_parkir);

        progress = new ProgressDialog(getActivity());
        progress.setCancelable(false);
        progress.setMessage("Tunggu Sebentar...");
        progress.show();

        viewUserProfile();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Menu QrCode
        menu.clear();
        inflater.inflate(R.menu.menu_qrcode, menu);

        //Title Bar
        ((Dashboard) getActivity()).getSupportActionBar().setTitle("Beranda");
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_qr_code:
                startActivity(new Intent(getActivity(), QRCode.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        viewUserProfile();
    }

    public void viewUserProfile(){
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
                progress.dismiss();
                user = response.body().getResult_user();
                User data_user = user.get(0);

                txtNama.setText(data_user.getNama());
                txtPlatNomor.setText(data_user.getPlat_nomor());
                if (data_user.getJenis_kendaraan().equals("Motor")){
                    Glide.with(getActivity())
                            .load(R.drawable.ic_bike)
                            .into(mJenisKendaraan);
                }

                txtMerkKendataan.setText(data_user.getMerk_kendaraan());
                txtContact.setText(data_user.getNo_tlp());

                if (data_user.getStatus() == 0){
                    statParkir.setVisibility(View.GONE);
                    statTidakParkir.setVisibility(View.VISIBLE);
                }else{
                    statParkir.setVisibility(View.VISIBLE);
                    statTidakParkir.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
