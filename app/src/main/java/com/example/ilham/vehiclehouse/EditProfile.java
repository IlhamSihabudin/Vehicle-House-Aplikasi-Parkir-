package com.example.ilham.vehiclehouse;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ilham.vehiclehouse.API.ParkirAPI;
import com.example.ilham.vehiclehouse.Model.User;
import com.example.ilham.vehiclehouse.Model.Value;
import com.google.gson.Gson;
import com.santalu.maskedittext.MaskEditText;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditProfile extends AppCompatActivity {
    private static final String URL = "http://192.168.1.124/vehicle_house/";
    ProgressDialog progress;
    String jenisKendaraan;
    List<User> user = new ArrayList<>();

    @BindView(R.id.edtNama)
    EditText edtNama;
    @BindView(R.id.rgJenisKendaraan)
    RadioGroup rgJenisKendaraan;
    @BindView(R.id.radio_mobil)
    RadioButton radioMobil;
    @BindView(R.id.radio_motor)
    RadioButton radioMotor;
    @BindView(R.id.edtMerk)
    EditText edtMerk;
    @BindView(R.id.edtNoTlp)
    MaskEditText edtNoTlp;
    @BindView(R.id.edtPlatNomor)
    EditText edtPlatNomor;

    @OnClick(R.id.btnSimpanPerubahan) void simpanPerubahan(){
        progress = new ProgressDialog(this);
        progress.setCancelable(false);
        progress.setMessage("Tunggu Sebentar...");
        progress.show();

        int selectedId = rgJenisKendaraan.getCheckedRadioButtonId();
        if (selectedId == radioMobil.getId()){
            jenisKendaraan = radioMobil.getText().toString();
        }else if (selectedId == radioMotor.getId()){
            jenisKendaraan = radioMotor.getText().toString();
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        String id_user = db.select();

        ParkirAPI api = retrofit.create(ParkirAPI.class);
        Call<Value> call = api.update(id_user, edtNama.getText().toString(), edtMerk.getText().toString(), jenisKendaraan, edtPlatNomor.getText().toString(), edtNoTlp.getText().toString());
        call.enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                progress.dismiss();
                String message  = response.body().getMessage();
                String status   = response.body().getStatus();
                progress.dismiss();
                if (status.equals("1")){
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), Dashboard.class));
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                progress.dismiss();
                t.printStackTrace();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);

        progress = new ProgressDialog(this);
        progress.setCancelable(false);
        progress.setMessage("Tunggu Sebentar...");
        progress.show();

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
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
                User data_user = user.get(0);

                edtNama.setText(data_user.getNama());

                if (data_user.getJenis_kendaraan().equals("Mobil")){
                    radioMobil.setChecked(true);
                    radioMotor.setChecked(false);
                }else{
                    radioMobil.setChecked(false);
                    radioMotor.setChecked(true);
                }

                edtMerk.setText(data_user.getMerk_kendaraan());
                edtNoTlp.setText(data_user.getNo_tlp());
                edtPlatNomor.setText(data_user.getPlat_nomor());
                progress.dismiss();
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                progress.dismiss();
                t.printStackTrace();
            }
        });
    }
}
