package com.example.kumbh;

import android.app.Activity;
import android.os.Bundle;

public class NotificationView extends Activity{
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);
//        if(getIntent().getExtras()!=null){
//            Toast.makeText(this, "", Toast.LENGTH_SHORT).show().makeText(this, "Click", Toast.LENGTH_SHORT).show();
//        }
    }
}