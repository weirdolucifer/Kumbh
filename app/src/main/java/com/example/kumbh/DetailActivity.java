package com.example.kumbh;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
    List<Road> StringsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent i = getIntent();
        List<Road> StringsList = new ArrayList<Road>();
        StringsList = (List<Road>) i.getSerializableExtra("LIST");
        //read();
        ListView listView = (ListView) findViewById(R.id.listView);

        Adapter Adapter = new Adapter(this, R.layout.activity_adapter, StringsList);
        listView.setAdapter(Adapter);


    }





}
