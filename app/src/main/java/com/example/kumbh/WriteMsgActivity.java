package com.example.kumbh;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Activity that is opened when the user clicked on "Write Message..." in order to specify the message content
 * such as Title, Text, location, expiretime, topic.
 */
public class WriteMsgActivity extends AppCompatActivity {

    private SupportMapFragment mapFragment;
    ;
    private ScrollView mScrollView;
    private final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 1012;
    private GoogleMap mMap;
    private LinearLayout mapcontainer;
    private Marker msgLocMarker = null;
    private boolean markerPlacedPreviously = false;

    private String selected_topic;
    private String msg_title = "";
    private String msg_txt = "";
    private LatLng msg_pos = null;
    private Date msg_exp = null;
    private Date msg_create = null;
    private String msg_topic = "";
    private Boolean msg_locationChecked;
    private Boolean msg_shareToServer;
    private boolean expTimeCreatedCustomly = false;

    private DatePicker dp;
    private TimePicker tp;
    private int expDate_mins;
    private int expDate_hours;
    private SimpleDateFormat D_format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    public final String DEFAULT_ZONE_ID = "UMPA-UMPA-UMPA-TÖTÖRÖÖÖ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mess);

        // add Back Button on Actionbar:
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Change default behaviour of the edittext MessageTitle: On Enter: close EditText:
        final EditText edt_msgTitle = (EditText) findViewById(R.id.edt_msgTitle);
        edt_msgTitle.setFocusableInTouchMode(true);
        edt_msgTitle.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if ((actionId == EditorInfo.IME_ACTION_DONE) || ((event.getKeyCode() == KeyEvent.KEYCODE_ENTER) && (event.getAction() == KeyEvent.ACTION_DOWN))) {
                    // if the "enter"-key was pressed, close the shown Keyboard
                    InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                    in.hideSoftInputFromWindow(v.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    return true;
                } else {
                    return false;
                }
            }
        });

        // Change default behaviour of the edittext for the MessageText: On Enter: close EditText:
        final EditText edt_msgText = (EditText) findViewById(R.id.edt_msgText);
        edt_msgText.setFocusableInTouchMode(true);
        edt_msgText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if ((actionId == EditorInfo.IME_ACTION_DONE) || ((event.getKeyCode() == KeyEvent.KEYCODE_ENTER) && (event.getAction() == KeyEvent.ACTION_DOWN))) {
                    // if the "enter"-key was pressed, close the shown Keyboard
                    InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                    in.hideSoftInputFromWindow(v.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    return true;
                } else {
                    return false;
                }
            }
        });

        // which topic was selected?
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            selected_topic = extras.getString("TOPIC");
            setTitle(selected_topic);
        }

        // Add categories to the spinner:
        Spinner spn_category = (Spinner) findViewById(R.id.spn_category);


        String[] topics = new String[]{"watar atm - pantoon bridge 3",
                "triveni sangam upar chauraha - A Waypoint no. 1",
                "3d wall presentation - A mahavir marg sector 4",
                "kali sadak - A pontoon bridge 6",
                "uppcl Triveni bandh - A axis Bank atm",
                "PAC camp Triveni road - police station",
                "fun palace - ganga Manch",
                "lalloji deravala - A kali mukti charaha",
                "kali mukti charaha - A mukti mahavir charaha",
                "akbar fort - A sector 3 parking",
                "baba neeb karauri - juna akahada",
                "vaidik vashno sewa shram sivir - A baba neeb karauri",
                "vaidik vashno sewa shram sivir - A vaidik vashno sewa ashram",
                "chauki phool mandi - toilets",
                "bathing ghat - A indraprastham",
                "mukti sankatharan chauraha end - A sankashtharan marg",
                "pontoon pool 10 gangoli shivala bridge end - A gangoli marg",
                "shri daau ji dham khalsa - A pontoon pool no. 11",
                "alop marg - chauraha",
                "sachha baba ashram - jal nigam",
                "kashni aranGya mandir - A bajrangdas gangeshwar chauraha",
                "mela end phaphamau - union bank",
                "pac - pac",
                "sector 16 mukti marg - A harinarayan rasik maharaj",
                "surya swar - kali mukti chauraha",
                "mori shankarchariya chauraha - A shankar chariya marg",
                "swastik society - uttam seva dham",
                "mahant swamikhinmaya das ji - A mori sharkacharya chauraha",
                "mori sharkacharya chauraha - A shankar chariya marg",
                "jal nigam - police station"};


        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this,
                android.R.layout.simple_spinner_item, topics);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spn_category.setAdapter(adapter);
        spn_category.setSelection(0);



        // Add constraints to the spinner:
        Spinner spn_constrains = (Spinner) findViewById(R.id.spn_constraint);


        String[] constraints = new String[]{"Blocked","Oneway"};


        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter_c = new ArrayAdapter<CharSequence>(this,
                android.R.layout.simple_spinner_item, constraints);
        // Specify the layout to use when the list of choices appears
        adapter_c.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spn_constrains.setAdapter(adapter_c);
        spn_constrains.setSelection(0);








        // select expire date:
        Spinner spn_selectExpireTime = (Spinner) findViewById(R.id.spn_expireTime);
        final String[] expireDefaults = new String[]{"1 week", "5 days", "3 days", "2 days", "24 hours", "18 hours", "12 hours", "6 hours", "3 hours", "custom..."};
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter2 = new ArrayAdapter<CharSequence>(this,
                android.R.layout.simple_spinner_item, expireDefaults);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spn_selectExpireTime.setAdapter(adapter2);
        spn_selectExpireTime.setSelection(0);
        spn_selectExpireTime.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // if "custom..." is selected, open a time chooser dialog
                if (position == 9) {
                    expTimeCreatedCustomly = true;
                    Log.d("WriteMsgActivity", "onCustomTimeSelected");
                    final Dialog dialog = new Dialog(WriteMsgActivity.this);
                    dialog.setContentView(R.layout.custom_dialog);
                    dialog.setTitle("Pick the expiring date");

                    dp = (DatePicker) dialog.findViewById(R.id.datePicker1);
                    dp.setMinDate(new Date().getTime());                // minimum day is in today.
                    dp.setMaxDate(new Date().getTime() + 1000 * 3600 * 24 * 7); // maximum day is 1 week in future.

                    tp = (TimePicker) dialog.findViewById(R.id.timePicker1);

                    tp.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                        @Override
                        public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                            expDate_hours = hourOfDay;
                            expDate_mins = minute;
                            Log.d("OnTimeChanged", "hours: " + hourOfDay + ", mins: " + minute);
                        }
                    });

                    Button btn_setExpireDate = (Button) dialog.findViewById(R.id.btn_selectExpireTime);
                    btn_setExpireDate.setOnClickListener(new View.OnClickListener() {

                        /**
                         * @return a java.util.Date
                         */
                        private Date getDateFromPickers() {
                            // create the date:
                            int day = dp.getDayOfMonth();
                            int month = dp.getMonth();
                            int year = dp.getYear();

                            Calendar calendar = Calendar.getInstance();
                            calendar.set(year, month, day);
                            calendar.set(Calendar.HOUR_OF_DAY, expDate_hours);
                            calendar.set(Calendar.MINUTE, expDate_mins);

                            Log.i("WriteMsgActivity", "Calendar Time: " + calendar.getTime());

                            // add the hours and minutes:
                            Date expDate = calendar.getTime();
                            return expDate;
                        }

                        @Override
                        public void onClick(View v) {
                            try {
                                msg_exp = getDateFromPickers();
                            } catch (NullPointerException npe) {
                                Log.e("WriteMsgActivity", " confirm custom expiring time selected without selected date and time before: " + npe.getMessage());
                            }
                            Log.d("WriteMsgActivity", "Expire Date selected: " + msg_exp);
                            Date now = new Date(new Date().getTime() - 1000 * 3600 * 24);
                            if (new Date().after(msg_exp)) {
                                Log.i("WriteMsgActivity", "Selected Date is in the past! User must select another Date");
                                Toast.makeText(getApplicationContext(), "The selected expiring Date is in past.", Toast.LENGTH_LONG).show();
                            } else {
                                Log.i("WriteMsgActivity", "Selected Date is fine! now: " + new Date() + " < " + msg_exp);
                                TextView txt_expiresAt = (TextView) findViewById(R.id.txt_expiresAt);
                                txt_expiresAt.setText("Expires at: " + msg_exp);
                                dialog.cancel();
                            }
                        }
                    });

                    dialog.show();
                } else {
                    expTimeCreatedCustomly = false;
                    // calculate expiring Date based upon current Date:
                    msg_create = new Date(); // now
                    long theFuture = 0;
                    switch (position) {
                        case 0: // + 7 days
                            theFuture = System.currentTimeMillis() + (86400 * 7 * 1000);
                            break;
                        case 1: // + 5 days
                            theFuture = System.currentTimeMillis() + (86400 * 5 * 1000);
                            break;
                        case 2:
                            theFuture = System.currentTimeMillis() + (86400 * 3 * 1000);
                            break;
                        case 3:
                            theFuture = System.currentTimeMillis() + (86400 * 2 * 1000);
                            break;
                        case 4:
                            theFuture = System.currentTimeMillis() + (86400 * 1 * 1000);
                            break;
                        case 5:
                            theFuture = System.currentTimeMillis() + (18 * 3600 * 1000);
                            break;
                        case 6:
                            theFuture = System.currentTimeMillis() + (12 * 3600 * 1000);
                            break;
                        case 7:
                            theFuture = System.currentTimeMillis() + (6 * 3600 * 1000);
                            break;
                        case 8:
                            theFuture = System.currentTimeMillis() + (3 * 3600 * 1000);
                            break;
                        default:
                            break;
                    }
                    msg_exp = new Date(theFuture);
                    TextView txt_expiresAt = (TextView) findViewById(R.id.txt_expiresAt);
                    txt_expiresAt.setText("Expires at: " + msg_exp);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // do nothing.
            }
        });


        Button btn_submit = (Button) findViewById(R.id.btn_submitMsg);
        btn_submit.setOnClickListener(new View.OnClickListener()

                                      {
                                          FirebaseDatabase database = FirebaseDatabase.getInstance();
                                          DatabaseReference myRef = database.getReference("Blocked_Roads");
                                          @Override
                                          public void onClick(View v) {

                                              EditText edt_msgTitle = (EditText) findViewById(R.id.edt_msgTitle);
                                              EditText edt_msgText = (EditText) findViewById(R.id.edt_msgText);
                                              Spinner spn_selectTopic = (Spinner) findViewById(R.id.spn_category);
                                              Spinner spn_selectExpire = (Spinner) findViewById(R.id.spn_expireTime);
                                              Spinner spn_constraints = (Spinner) findViewById(R.id.spn_constraint);

                                              String title = edt_msgTitle.getText().toString();
                                              String txt = edt_msgText.getText().toString();
                                              String roadname = spn_selectTopic.getSelectedItem().toString();
                                              String constraint = spn_constraints.getSelectedItem().toString();

                                              if (isFormFilled()) {

                                                  expTimeCreatedCustomly = false;
                                                  Date exp = new Date(); // now
                                                  long theFuture = 0;
                                                  switch (spn_selectExpire.getSelectedItemPosition()) {
                                                      case 0: // + 7 days
                                                          theFuture = System.currentTimeMillis() + (86400 * 7 * 1000);
                                                          break;
                                                      case 1: // + 5 days
                                                          theFuture = System.currentTimeMillis() + (86400 * 5 * 1000);
                                                          break;
                                                      case 2:
                                                          theFuture = System.currentTimeMillis() + (86400 * 3 * 1000);
                                                          break;
                                                      case 3:
                                                          theFuture = System.currentTimeMillis() + (86400 * 2 * 1000);
                                                          break;
                                                      case 4:
                                                          theFuture = System.currentTimeMillis() + (86400 * 1 * 1000);
                                                          break;
                                                      case 5:
                                                          theFuture = System.currentTimeMillis() + (18 * 3600 * 1000);
                                                          break;
                                                      case 6:
                                                          theFuture = System.currentTimeMillis() + (12 * 3600 * 1000);
                                                          break;
                                                      case 7:
                                                          theFuture = System.currentTimeMillis() + (6 * 3600 * 1000);
                                                          break;
                                                      case 8:
                                                          theFuture = System.currentTimeMillis() + (3 * 3600 * 1000);
                                                          break;
                                                      case 9:
                                                          expTimeCreatedCustomly = true;
                                                          break;
                                                  }
                                                  if (expTimeCreatedCustomly) {
                                                      // msg_exp is set alrdy.
                                                  } else
                                                      exp = new Date(theFuture);





                                                  String id = myRef.push().getKey();

                                                  Road road = new Road(title,txt,roadname,constraint,exp);

                                                  myRef.child(id).setValue(road);
                                                  Intent intent = new Intent(WriteMsgActivity.this, Nav2Activity.class);
                                                  startActivity(intent);
                                                  finish();

                                              }
                                              else {
                                                  // no title set:
                                                  if (msg_title.length() < 1) {
                                                      Toast.makeText(getApplicationContext(), "You must set a title!", Toast.LENGTH_LONG).show();
                                                  }
                                                  // no title set:
                                                  if (msg_txt.length() < 1) {
                                                      Toast.makeText(getApplicationContext(), "You must set a message text!", Toast.LENGTH_LONG).show();
                                                  }
                                                  // no location marked, but checked:
                                                  if ((msg_locationChecked) && (msg_pos == null)) {
                                                      Toast.makeText(getApplicationContext(), "You must mark a position if u select adding a location!", Toast.LENGTH_LONG).show();
                                                  }
                                              }



                                          }
                                      }

        );
    }

    private boolean isFormFilled() {
        boolean allFilled = true;
        EditText edt_msgTitle = (EditText) findViewById(R.id.edt_msgTitle);
        EditText edt_msgText = (EditText) findViewById(R.id.edt_msgText);
        Spinner spn_selectTopic = (Spinner) findViewById(R.id.spn_category);
        Spinner spn_selectExpire = (Spinner) findViewById(R.id.spn_expireTime);


        CheckBox chb_shareToServer = (CheckBox) findViewById(R.id.chb_allowServerSharing);

        // get title, and check if it's set
        msg_title = edt_msgTitle.getText().toString();
        if (msg_title.length() < 1)
            allFilled = false;

        // get text, and check if it's set
        msg_txt = edt_msgText.getText().toString();
        if (msg_txt.length() < 1)
            allFilled = false;

        // calculate expiring Date based upon current Date:
        expTimeCreatedCustomly = false;
        msg_create = new Date(); // now
        long theFuture = 0;
        switch (spn_selectExpire.getSelectedItemPosition()) {
            case 0: // + 7 days
                theFuture = System.currentTimeMillis() + (86400 * 7 * 1000);
                break;
            case 1: // + 5 days
                theFuture = System.currentTimeMillis() + (86400 * 5 * 1000);
                break;
            case 2:
                theFuture = System.currentTimeMillis() + (86400 * 3 * 1000);
                break;
            case 3:
                theFuture = System.currentTimeMillis() + (86400 * 2 * 1000);
                break;
            case 4:
                theFuture = System.currentTimeMillis() + (86400 * 1 * 1000);
                break;
            case 5:
                theFuture = System.currentTimeMillis() + (18 * 3600 * 1000);
                break;
            case 6:
                theFuture = System.currentTimeMillis() + (12 * 3600 * 1000);
                break;
            case 7:
                theFuture = System.currentTimeMillis() + (6 * 3600 * 1000);
                break;
            case 8:
                theFuture = System.currentTimeMillis() + (3 * 3600 * 1000);
                break;
            case 9:
                expTimeCreatedCustomly = true;
                break;
        }
        if (expTimeCreatedCustomly) {
            // msg_exp is set alrdy.
        } else
            msg_exp = new Date(theFuture);



        // Share to server?
        if (chb_shareToServer.isChecked()) {
            msg_shareToServer = true;
        } else {
            msg_shareToServer = false;
        }

        // get selected topic:
        msg_topic = spn_selectTopic.getSelectedItem().toString();

        return allFilled;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_ACCESS_COARSE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.d("MapTap", "Permission granted, hooray");
                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    try {
                        mMap.setMyLocationEnabled(true);
                        // enable Location service on phone if its not enabled already:
                        LocationManager lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
                        boolean gps_enabled = false;
                        boolean network_enabled = false;

                        try {
                            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
                        } catch (Exception ex) {
                        }

                        try {
                            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                        } catch (Exception ex) {
                        }

                        if (!gps_enabled && !network_enabled) {
                            // activate Location Service
                            Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            WriteMsgActivity.this.startActivity(myIntent);
                        }
                    } catch (SecurityException e) {
                        Log.d("MapTap", "2nd attempt also failed on security:" + e);
                    }

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getApplicationContext(), "Grant location permission for HappyShare in your phone settings for a location-button.", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
