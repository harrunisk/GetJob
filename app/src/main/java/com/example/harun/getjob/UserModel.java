package com.example.harun.getjob;

/**
 * Created by mayne on 2.12.2017.
 */

public class UserModel {

    public String email,username,password,isim,rol;

    public UserModel(){}

    public UserModel(String email, String username, String password, String isim, String rol) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.isim = isim;
        this.rol=rol;
    }

    public String getEmail() {
        return email;
    }

    public String getIsim() {
        return isim;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol(){return rol;}

    public void setRol(){this.rol=rol;}


}
