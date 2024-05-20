package com.mylaneza.jamarte.entities;

/**
 * Created by mylaneza on 07/07/2018.
 */
public class Step {
    public long id;
    public String name;
    public int count;
    public String base;
    public String descriptionLeader;
    public String descriptionFollower;
    public String videoFilePath;

    public String toString(){
        return name +","+ count +","+base+","+ descriptionLeader +","+ descriptionFollower;
    }

}
