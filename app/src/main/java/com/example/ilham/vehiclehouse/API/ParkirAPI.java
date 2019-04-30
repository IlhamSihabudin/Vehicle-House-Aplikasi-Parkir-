package com.example.ilham.vehiclehouse.API;

import com.example.ilham.vehiclehouse.Model.Value;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ParkirAPI {
    @FormUrlEncoded
    @POST("login.php")
    Call<Value> login(@Field("username") String username,
                      @Field("password") String password);

    @FormUrlEncoded
    @POST("daftar.php")
    Call<Value> daftar(@Field("nama") String nama,
                       @Field("merk_kendaraan") String merk_kendaraan,
                       @Field("jenis_kendaraan") String jenis_kendaraan,
                       @Field("plat_nomor") String plat_nomor,
                       @Field("no_tlp") String no_tlp,
                       @Field("username") String username,
                       @Field("password") String password);

    @FormUrlEncoded
    @POST("select_where.php")
    Call<Value> select_where(@Field("id_user") String id_user);

    @FormUrlEncoded
    @POST("select_riwayat_user.php")
    Call<Value> select_riwayat_user(@Field("id_user") String id_user);

    @FormUrlEncoded
    @POST("update.php")
    Call<Value> update(@Field("id_user") String id_user,
                       @Field("nama") String nama,
                       @Field("merk_kendaraan") String merk_kendaraan,
                       @Field("jenis_kendaraan") String jenis_kendaraan,
                       @Field("plat_nomor") String plat_nomor,
                       @Field("no_tlp") String no_tlp);

    @FormUrlEncoded
    @POST("update_akun.php")
    Call<Value> update_akun(@Field("id_user") String id_user,
                            @Field("username") String username,
                            @Field("password") String password);
}
