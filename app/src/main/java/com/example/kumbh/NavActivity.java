package com.example.kumbh;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NavActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ArrayList<String> list=new ArrayList<String>();
    List <Road> StringsList = new ArrayList<>();

    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        read();
//        ViewStub stub = (ViewStub) findViewById(R.id.layout_stub);
//        stub.setLayoutResource(R.layout.notification_item);
//        View inflated = stub.inflate();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }
    public void addNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.map);
        builder.setContentTitle("Stay Alert!");
        String notifyText = Integer.toString(list.size()) + " roads are blocked\n";
        for(int i = 0 ; i < list.size(); i++){
            notifyText = notifyText + " " + list.get(i) + " \n";
        }
        builder.setContentText(notifyText);

        Intent notificationIntent = new Intent(this, NavActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());

        builder.setStyle(new NotificationCompat.BigTextStyle()
                .bigText(notifyText)).build();

    }
    private void read() {
        final ProgressBar yourProgressBar = findViewById(R.id.progressBar1);
        yourProgressBar.setVisibility(View.VISIBLE);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Blocked_Roads");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Log.e("Count " ,""+dataSnapshot.getChildrenCount());
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Road road = postSnapshot.getValue(Road.class);
                    Log.e("Get Data", road.Road_name );
                    Log.e("Get Data", road.Title );
                    Log.e("Get Data", road.Text );
                    Log.e("Get Data", road.Constraint );

                    Date x = road.exp;
                    SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Calendar calendar = Calendar.getInstance();
                    String str1 = sfd.format(new Date(x.getTime()));
                    String currentDateTimeString = sfd.format(calendar.getTime());

                    Date date1 = null;
                    try {
                        date1 = sfd.parse(str1);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    Date date2 = null;
                    try {
                        date2 = sfd.parse(currentDateTimeString);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if (date1.compareTo(date2)<0)
                    {

                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef1 = database.getReference("Blocked_Roads").child(postSnapshot.getKey());
                        myRef1.removeValue();
                        Log.e("OKKK", str1);
                    }
                    else
                    {
                        list.add(road.Road_name);
                        StringsList.add(road);

                    }
                }
                yourProgressBar.setVisibility(View.INVISIBLE);
                addNotification();
                Log.e("OKKK", Integer.toString(StringsList.size()));


            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("A", "loadPost:onCancelled", databaseError.toException());
            }


        });


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_logout) {
            FirebaseAuth auth;
            auth = FirebaseAuth.getInstance();
            auth.signOut();
            startActivity(new Intent(NavActivity.this, LoginActivity.class));
            finish();

        } else if (id == R.id.nav_gallery) {


            Intent intent = new Intent(NavActivity.this, KmlDemoActivity.class);

            intent.putExtra("LIST1", (Serializable) StringsList);
            startActivity(intent);

        }else if (id == R.id.nav_gallery1) {



            Intent intent = new Intent(NavActivity.this, KmlDemo2Activity.class);

            startActivity(intent);

        }
        else if (id == R.id.nav_slideshow) {
            Intent intent1 = new Intent(NavActivity.this, DetailActivity.class);
            intent1.putExtra("LIST", (Serializable) StringsList);
            startActivity(intent1);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
