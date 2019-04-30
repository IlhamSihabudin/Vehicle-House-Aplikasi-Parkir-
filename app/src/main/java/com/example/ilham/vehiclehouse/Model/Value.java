package com.example.ilham.vehiclehouse.Model;

import java.util.List;

public class Value {
    String message;
    String status;
    String id_user;
    List<User> result_user;
    List<Parkir> result_parkir;

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }

    public List<User> getResult_user() {
        return result_user;
    }

    public String getId_user() {
        return id_user;
    }

    public List<Parkir> getResult_parkir() {
        return result_parkir;
    }
}
