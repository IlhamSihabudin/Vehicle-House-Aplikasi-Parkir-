package com.example.ilham.vehiclehouse;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ilham.vehiclehouse.API.ParkirAPI;
import com.example.ilham.vehiclehouse.Model.User;
import com.example.ilham.vehiclehouse.Model.Value;

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

public class EditAkun extends AppCompatActivity {
    private static final String URL = "http://192.168.1.124/vehicle_house/";
    ProgressDialog progress;
    List<User> user = new ArrayList<>();

    @BindView(R.id.edtUsername)
    EditText edtUsername;
    @BindView(R.id.edtPassword)
    EditText edtPassword;

    @OnClick(R.id.btnSimpanPerubahan) void simpanPerubahan(){
        progress = new ProgressDialog(this);
        progress.setCancelable(false);
        progress.setMessage("Tunggu Sebentar...");
        progress.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        String id_user = db.select();

        ParkirAPI api = retrofit.create(ParkirAPI.class);
        Call<Value> call = api.update_akun(id_user, edtUsername.getText().toString(), edtPassword.getText().toString());
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
        setContentView(R.layout.activity_edit_akun);
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

                edtUsername.setText(data_user.getUsername());
                edtPassword.setText(data_user.getPassword());
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
