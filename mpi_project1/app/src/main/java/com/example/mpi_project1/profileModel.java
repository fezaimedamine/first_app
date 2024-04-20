package com.example.mpi_project1;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public class profileModel {
    public String name,fname,email,profileimage;
    boolean state;
    profileModel(){
    }
    public profileModel(String name,String fname,String email,boolean state,String profileimage){
        this.name=name;
        this.fname=fname;
        this.email=email;
        this.profileimage=profileimage;
        this.state=state;
    }

    public String getProfileimage() {
        return profileimage;
    }

    public String getName() {
        return name;
    }

    public String getFname() {
        return fname;
    }

    public String getEmail() {
        return email;
    }

    public boolean isState() {
        return state;
    }
}

