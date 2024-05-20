package com.mylaneza.jamarte.entities;

import androidx.annotation.NonNull;

/**
 * Created by mylaneza on 07/07/2018.
 */
public class Member {
    public long id;
    public String name;
    public String nickname;
    public String lastNameParent;
    public String lastNameMother;
    public String birthday;
    public int gender;
    public int progressByGender;

    @NonNull
    public String toString(){
        return name +","+nickname+","+ lastNameParent +","+ lastNameMother +","+ birthday;
    }
}
