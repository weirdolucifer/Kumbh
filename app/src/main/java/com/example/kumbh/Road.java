package com.example.kumbh;

import java.io.Serializable;
import java.util.Date;

public class Road implements Serializable {

    public String Title;
    public String Text;
    public String Road_name;
    public Date exp;
    public String Constraint;

    public Road(){}

    public Road(String title, String text, String road_name,String constraint, Date exp) {

        this.Title = title;
        this.Text = text;
        this.Road_name = road_name;
        this.Constraint = constraint;
        this.exp = exp;
    }


}
