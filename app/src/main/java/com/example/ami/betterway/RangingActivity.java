package com.example.ami.betterway;

import android.app.Activity;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.widget.EditText;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
//import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;

public class RangingActivity extends Activity implements BeaconConsumer {
    protected static final String TAG = "RangingActivity";
    private BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);
    /*private Beacon blueBeacon;
    private Beacon greenBeacon;
    private boolean room1 = false;
    private boolean room2 = false;
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranging);
    }

    @Override 
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override 
    protected void onPause() {
        super.onPause();
        beaconManager.unbind(this);
    }

    @Override 
    protected void onResume() {
        super.onResume();
        beaconManager.bind(this);
    }

    @Override
    public void onBeaconServiceConnect() {

        RangeNotifier rangeNotifier = new RangeNotifier() {
           @Override
           public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
              if (beacons.size() > 0) {
                  //Log.d(TAG, "didRangeBeaconsInRegion called with beacon count:  "+beacons.size());
                  Beacon firstBeacon = beacons.iterator().next();
                  Log.d(TAG, "The first beacon " + firstBeacon.toString() + " is about " + firstBeacon.getDistance() + " meters away.");
                  //logToDisplay("The first beacon " + firstBeacon.toString() + " is about " + firstBeacon.getDistance() + " meters away.");
              /*for (Beacon var : beacons) {
                        if (var.getId3().equals(Identifier.fromInt(60004))) {   //green beacon --> red Exit
                            greenBeacon = var;
                        } else if (var.getId3().equals(Identifier.fromInt(5012))) {  //blue beacon --> yellow Exit
                            blueBeacon = var;
                            logToDisplay("The blue beacon is about " + var.getDistance() + " meters away and has an RSSI of " + var.getRssi() + " dB\n");
                        }}
              */
              }
               
               //zone recognition
               
               //for LADISPE
               /*
               if(greenBeacon.getRunningAverageRssi()>-80 && greenBeacon.getRunningAverageRssi()<-50
                    && blueBeacon.getRunningAverageRssi()>-96 && blueBeacon.getRunningAverageRssi()<-76)
                    room1 = true;

                else if(greenBeacon.getRunningAverageRssi()>-90 && greenBeacon.getRunningAverageRssi()<-70
                        && blueBeacon.getRunningAverageRssi()>-80 && blueBeacon.getRunningAverageRssi()<-50)
                    room2 = true;
               */
               
               //for home
               /*
               if (blueBeacon.getRunningAverageRssi()> -60 && blueBeacon.getRunningAverageRssi()< -40)
                    room1 = true;
                logToDisplay("ROOM 1\n");

                if (blueBeacon.getRunningAverageRssi()> -90 && blueBeacon.getRunningAverageRssi()< -80)
                    room2 = true;
                logToDisplay("ROOM 2 \n");
               */
           }
        };
        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
            beaconManager.addRangeNotifier(rangeNotifier);
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
            beaconManager.addRangeNotifier(rangeNotifier);
        } catch (RemoteException e) {   }
    }

    private void logToDisplay(final String line) {
        runOnUiThread(new Runnable() {
            public void run() {
                EditText editText = (EditText)RangingActivity.this.findViewById(R.id.rangingText);
                editText.append(line+"\n");
            }
        });
    }
}
