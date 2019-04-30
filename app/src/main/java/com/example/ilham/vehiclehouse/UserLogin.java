package com.example.ilham.vehiclehouse;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ilham.vehiclehouse.API.ParkirAPI;
import com.example.ilham.vehiclehouse.Model.Value;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserLogin extends AppCompatActivity {
    public static final String URL = "http://192.168.1.124/vehicle_house/";
    ProgressDialog progress;
    String username, password;

    @BindView(R.id.edtUsername)
    EditText edtUsername;
    @BindView(R.id.edtPassword)
    EditText edtPassword;

    @OnClick(R.id.btnLogin) void log_in(){
        if (edtUsername.getText().toString().equals("")){
            edtUsername.setError("Username Wajib Diisi");
        }else if(edtPassword.getText().toString().equals("")){
            edtPassword.setError("Password Wajib Diisi");
        }else{
            progress = new ProgressDialog(this);
            progress.setCancelable(false);
            progress.setMessage("Tunggu Sebentar...");
            progress.show();

            username = edtUsername.getText().toString();
            password = edtPassword.getText().toString();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ParkirAPI api = retrofit.create(ParkirAPI.class);
            Call<Value> call = api.login(username, password);
            call.enqueue(new Callback<Value>() {
                @Override
                public void onResponse(Call<Value> call, Response<Value> response) {
                    String status   = response.body().getStatus();
                    String message  = response.body().getMessage();
                    String id_user  = response.body().getId_user();
                    progress.dismiss();

                    if (status.equals("1")){
                        DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());
                        databaseHandler.addRecord(id_user);
                        startActivity(new Intent(getApplicationContext(), Dashboard.class));
                        finish();
                    }else{
                        Toast.makeText(UserLogin.this, message, Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<Value> call, Throwable t) {
                    t.printStackTrace();
                    progress.dismiss();
                    Toast.makeText(UserLogin.this, "Jaringan Error", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @OnClick(R.id.btnToRegister) void toRegister(){
        startActivity(new Intent(getApplicationContext(), RegistrasiUser.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        ButterKnife.bind(this);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }
}
