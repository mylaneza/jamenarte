package com.mylaneza.jamarte.entities;

import java.util.Vector;

/**
 * Created by mylaneza on 16/08/2018.
 */
public class Lesson {

    public long id;
    public String level;
    public String name;
    public String school;
    public String objective;
    public String description;

    public Vector<Step> steps = new Vector<Step>();
}
