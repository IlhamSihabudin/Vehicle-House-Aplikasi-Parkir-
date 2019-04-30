package com.example.ilham.vehiclehouse;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.ilham.vehiclehouse.API.ParkirAPI;
import com.example.ilham.vehiclehouse.Model.Value;
import com.santalu.maskedittext.MaskEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegistrasiUser extends AppCompatActivity {
    public static final String URL = "http://192.168.1.124/vehicle_house/";
    ProgressDialog progress;
    String jenisKendaraan;

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
    @BindView(R.id.edtUsername)
    EditText edtUsername;
    @BindView(R.id.edtPassword)
    EditText edtPassword;
    @BindView(R.id.edtConPassword)
    EditText edtConPassword;

    @OnClick(R.id.btnRegister) void register(){
        if (edtNama.equals("")) {
            edtNama.setError("Nama Lengkap Wajib Diisi");
        }else if (edtMerk.equals("")){
            edtMerk.setError("Merk Kendaraan Wajib Diisi");
        }else if (edtNoTlp.equals("")){
            edtNoTlp.setError("No Telepon Wajib Diisi");
        }else if (edtPlatNomor.equals("")){
            edtPlatNomor.setError("Plat Nomor Wajib Diisi");
        }else if (edtUsername.equals("")){
            edtUsername.setError("Username Wajib Diisi");
        }else if (edtPassword.equals("")){
            edtPassword.setError("Password Wajib Diisi");
        }else if (edtConPassword.equals("")){
            edtConPassword.setError("Konfirmasi Wajib Diisi");
        }else{
            if (edtPassword.getText().toString().equals(edtConPassword.getText().toString())){
                int selectedId = rgJenisKendaraan.getCheckedRadioButtonId();
                if (selectedId == radioMobil.getId()){
                    jenisKendaraan = radioMobil.getText().toString();
                }else if (selectedId == radioMotor.getId()){
                    jenisKendaraan = radioMotor.getText().toString();
                }

                progress = new ProgressDialog(this);
                progress.setCancelable(false);
                progress.setMessage("Tunggu Sebentar...");
                progress.show();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                ParkirAPI api = retrofit.create(ParkirAPI.class);
                Call<Value> call = api.daftar(edtNama.getText().toString(), edtMerk.getText().toString(), jenisKendaraan, edtPlatNomor.getText().toString(), edtNoTlp.getText().toString(), edtUsername.getText().toString(), edtPassword.getText().toString());
                call.enqueue(new Callback<Value>() {
                    @Override
                    public void onResponse(Call<Value> call, Response<Value> response) {
                        String message  = response.body().getMessage();
                        String status   = response.body().getStatus();
                        progress.dismiss();
                        if (status.equals("1")){
                            Toast.makeText(RegistrasiUser.this, message, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), UserLogin.class));
                            finish();
                        }else{
                            Toast.makeText(RegistrasiUser.this, message, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Value> call, Throwable t) {
                        progress.dismiss();
                        t.printStackTrace();
                        Toast.makeText(RegistrasiUser.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }else{
                Toast.makeText(this, "Password dan Konfirmasi Password Tidak Sama", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @OnClick(R.id.btnToLogin) void toLogin(){
        startActivity(new Intent(getApplicationContext(), UserLogin.class));
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrasi_user);
        ButterKnife.bind(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }
}
