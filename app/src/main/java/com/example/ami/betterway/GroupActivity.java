package com.example.ami.betterway;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.RemoteException;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;

public class GroupActivity extends AppCompatActivity implements BeaconConsumer {
    protected static final String TAG = "Monitoring+RangingActivity";
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);
    private Beacon blueBeacon;
    private Beacon greenBeacon;
    private int room = 0;
    private String androidID;
    private int master=0; //0=slave, 1=master
    public Group group;
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
    TextView noDanger, gr_child, gr_key, gr_people,gr_disabled, pathGuide, helptx;
    ImageView yellowExit, greenExit;
    Button safe;
    Button help;
    Button lost;

    @Override
    public void onBeaconServiceConnect() {

        RangeNotifier rangeNotifier = new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (master == 1) {
                    if (beacons.size() > 0) {

                        for (Beacon var : beacons) {
                            if (var.getId3().equals(Identifier.fromInt(60004))) {   //green beacon --> red Exit
                                greenBeacon = var;
                            } else if (var.getId3().equals(Identifier.fromInt(5012))) {  //blue beacon --> yellow Exit
                                blueBeacon = var;
                            }
                        }
                        //zone recognition
/*
                            //home version
                            if (greenBeacon.getRunningAverageRssi() > -60 && greenBeacon.getRunningAverageRssi() < -40) {
                                room = 1;
                                myRef.child("Group").child("room").setValue(room);
                                Log.d(TAG, "ROOM 1\n");
                            }

                            if (greenBeacon.getRunningAverageRssi() > -90 && greenBeacon.getRunningAverageRssi() < -80) {
                                room = 2;
                                myRef.child("Group").child("room").setValue(room);
                                Log.d(TAG, "ROOM 2 \n");
                            }*/

                        //for LADISPE -->salvare valori nel db

                        if (greenBeacon.getRunningAverageRssi() > -80 && greenBeacon.getRunningAverageRssi() < -50
                                && blueBeacon.getRunningAverageRssi() > -95 && blueBeacon.getRunningAverageRssi() < -70) {
                            room=1;
                            myRef.child("Group").child("room").setValue(room);
                            Log.d(TAG, "ROOM 1\n");
                        } else if (greenBeacon.getRunningAverageRssi() > -95 && greenBeacon.getRunningAverageRssi() < -75
                                && blueBeacon.getRunningAverageRssi() > -85 && blueBeacon.getRunningAverageRssi() < -55) {
                            room=2;
                            myRef.child("Group").child("room").setValue(room);
                            Log.d(TAG, "ROOM 2 \n");
                        }

                    }
                }
            }

        };
        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
            beaconManager.addRangeNotifier(rangeNotifier);
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
            beaconManager.addRangeNotifier(rangeNotifier);
        } catch (RemoteException e) {   }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        androidID = Settings.Secure.getString(this.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        gr_child = (TextView) findViewById(R.id.gr_child);
        gr_key = (TextView) findViewById(R.id.gr_key);
        gr_people = (TextView) findViewById(R.id.gr_people);
        gr_disabled = (TextView) findViewById(R.id.gr_disabled);
        noDanger = (TextView) findViewById(R.id.noDanger);
        pathGuide = (TextView) findViewById(R.id.pathGuide);
        helptx = (TextView) findViewById(R.id.help);
        yellowExit = (ImageView) findViewById(R.id.yellowexit);
        greenExit = (ImageView) findViewById(R.id.greenexit);

        //adding icons
        gr_people.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_people, 0, 0, 0);
        gr_child.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_child, 0, 0, 0);
        gr_key.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock, 0, 0, 0);
        gr_disabled.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_acc, 0, 0, 0);

        safe = (Button) findViewById(R.id.btn_safe);
        help = (Button) findViewById(R.id.btn_help);
        lost = (Button) findViewById(R.id.lost);


        safe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("Group").child("exitReached").setValue(true);
                Intent intent = new Intent(GroupActivity.this, ExitReachedActivity.class);
                startActivity(intent);
            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("Group").child("needHelp").setValue(true);

                helptx.append(Integer.toString(room));
                helptx.setVisibility(View.VISIBLE);
                help.setEnabled(false);
            }
        });

        lost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.child("Group").child("smarrimento").setValue(1);
                Toast.makeText(getBaseContext(),"You can reach your group in Room " + room,Toast.LENGTH_LONG).show();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android M Permission check
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("This app needs location access");
                builder.setMessage("Please grant location access so this app can detect beacons in the background.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                    @TargetApi(23)
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                PERMISSION_REQUEST_COARSE_LOCATION);
                    }

                });
                builder.show();
            }
        }


    }


    @Override
    protected void onStart() {
        super.onStart();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                /*Retriving group info from db*/
                String name = dataSnapshot.child("Group").child("groupName").getValue(String.class);
                Boolean child = dataSnapshot.child("Group").child("children").getValue(Boolean.class);
                Boolean handicap = dataSnapshot.child("Group").child("handicap").getValue(Boolean.class);
                int people = dataSnapshot.child("Group").child("people").getValue(int.class);
                int led = dataSnapshot.child("ledStatus").getValue(int.class); //led=0 no fire, led=1 yellow exit, led=2 red exit
                String dbMaster = dataSnapshot.child("Group").child("masterId").getValue(String.class);
                room = dataSnapshot.child("Group").child("room").getValue(int.class);



                if(dbMaster.equals(androidID)) {
                    master = 1;
                    help.setVisibility(View.VISIBLE);
                    safe.setVisibility(View.VISIBLE);
                    lost.setVisibility(View.INVISIBLE);
                }
                else{
                    master=0;
                    help.setVisibility(View.INVISIBLE);
                    safe.setVisibility(View.INVISIBLE);
                    lost.setVisibility(View.VISIBLE);
                }


                switch(led){
                    case 0: //no danger
                        safe.setEnabled(false);
                        help.setEnabled(false);
                        noDanger.setVisibility(View.VISIBLE);
                        pathGuide.setVisibility(View.INVISIBLE);
                        greenExit.setVisibility(View.INVISIBLE);
                        yellowExit.setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        safe.setEnabled(true);
                        help.setEnabled(true);
                        noDanger.setVisibility(View.INVISIBLE);
                        pathGuide.setVisibility(View.VISIBLE);
                        pathGuide.setText("FOLLOW YELLOW LIGTHS");
                        pathGuide.setTextColor(Color.rgb(230, 223, 37));
                        greenExit.setVisibility(View.INVISIBLE);
                        yellowExit.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        safe.setEnabled(true);
                        help.setEnabled(true);
                        noDanger.setVisibility(View.INVISIBLE);
                        pathGuide.setVisibility(View.VISIBLE);
                        pathGuide.setText("FOLLOW GREEN LIGTHS");
                        pathGuide.setTextColor(Color.rgb(53, 222, 38));
                        greenExit.setVisibility(View.VISIBLE);
                        yellowExit.setVisibility(View.INVISIBLE);
                        break;
                }

                group = new Group(name, people, child, handicap);  //every logged user has an instance of the same Group object


                gr_key.setText(group.getPin());
                gr_people.setText(Integer.toString(group.getPeople()));
                setTitle(group.getGroupName() + " group");

                if (group.getChildren()) gr_child.setText("YES");
                else gr_child.setText("NO");

                if (group.getHandicap()) gr_disabled.setText("YES");
                else gr_disabled.setText("NO");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        BeaconReferenceApplication application = ((BeaconReferenceApplication) this.getApplicationContext());
        application.setMonitoringActivity(this);
        beaconManager.bind(this);

    }

    @Override
    public void onPause() {
        super.onPause();
        ((BeaconReferenceApplication) this.getApplicationContext()).setMonitoringActivity(null);
        beaconManager.unbind(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Log.d(TAG, "coarse location permission granted");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }

                    });
                    builder.show();
                }
                return;
            }
        }
    }

    @Override
    public void onBackPressed() {
            /* Nothing to do
                override of onBackPressed method to disable back button
             */
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
