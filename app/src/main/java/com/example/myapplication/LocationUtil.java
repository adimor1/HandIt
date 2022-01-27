package com.example.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import org.json.JSONException;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationUtil  extends Activity{
    private final Context context;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    private PermissionUtil my_permission_handle;
    private FusedLocationProviderClient fusedLocationClient;
    private String fullAddress;
    private LocationManager locationManager = null;
    private LocationListener gpsLocationListener = null;
    private LocationListener networkLocationListener = null;
    private final TaskListener listener;
    private Location location;
    private Double distance;
    private boolean isShowGPSDisabledAlertToUser=true;
    private boolean isuserDidNotWantToTurnOnGps=false;
    private boolean isLocationPermissionGranted=false;
    private boolean isTaskComplete = false;

    public LocationUtil (final Context context,TaskListener l){
        this.context=context;
        listener=l;
        my_permission_handle = new PermissionUtil(context);
        if (my_permission_handle.has_permission(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            isLocationPermissionGranted=true;
            startFindLocation();
        }
        else {
            my_permission_handle.ask_permission(Manifest.permission.ACCESS_COARSE_LOCATION,
                    MY_PERMISSIONS_REQUEST_LOCATION,
                    ("Location permission"),
                    ("location permission must be confirmed"));
        }
    }

    private void showGPSDisabledAlertToUser(){
        if (isShowGPSDisabledAlertToUser){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setMessage("GPS off, do you want to turn it on?")
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.yes,
                            new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialog, int id){
                                    Intent callGPSSettingIntent = new Intent(
                                            android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    Activity activity = (Activity)context;
                                    activity.startActivityForResult(callGPSSettingIntent, 96);
                                }
                            });
            alertDialogBuilder.setNegativeButton(android.R.string.no,
                    new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int id){
                            try {
                                isuserDidNotWantToTurnOnGps=true;
                                if (!isTaskComplete) {
                                    isTaskComplete=true;
                                    listener.taskComplete(true);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
            AlertDialog alert = alertDialogBuilder.create();
            alert.show();
            isShowGPSDisabledAlertToUser=false;
        }
        else{
            try {
                isuserDidNotWantToTurnOnGps=true;
                if (!isTaskComplete) {
                    isTaskComplete=true;
                    listener.taskComplete(true);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public double CalculationByDistance(double initialLat, double initialLong, double finalLat, double finalLong){
        int R = 6371; // km (Earth radius)
        double dLat = toRadians(finalLat-initialLat);
        double dLon = toRadians(finalLong-initialLong);
        initialLat = toRadians(initialLat);
        finalLat = toRadians(finalLat);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(initialLat) * Math.cos(finalLat);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return R * c;
    }

    public double toRadians(double deg) {
        return deg * (Math.PI/180);
    }

    @SuppressLint("MissingPermission")
    public void setAddress (Location location){
        String cityName = null;
        Geocoder gcd = new Geocoder(context, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(location.getLatitude(),
                    location.getLongitude(), 1);
            if (addresses.size() > 0) {
                System.out.println(addresses.get(0).getLocality());
                cityName = addresses.get(0).getLocality();
                fullAddress =  addresses.get(0).getAddressLine(0);
                if (gpsLocationListener!=null)
                    locationManager.removeUpdates(gpsLocationListener);
                if (networkLocationListener!=null)
                    locationManager.removeUpdates(networkLocationListener);
                try {
                    if (!isTaskComplete) {
                        isTaskComplete=true;
                        listener.taskComplete(true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Location getLocation(){
        return location;
    }

    public Double getDistnace (double targetLatitude,double targetLongitude){
        distance = CalculationByDistance (location.getLatitude(),location.getLongitude(),targetLatitude,targetLongitude);
        return distance;
    }

    public String getAddress(){
        return fullAddress;
    }

    public boolean getIsuserDidNotWantToTurnOnGps(){
        return isuserDidNotWantToTurnOnGps;
    }

    public void setUserDidNotWantToTurnOnGps(boolean isUserDidNotWantToTurnOnGps){
        isuserDidNotWantToTurnOnGps=isUserDidNotWantToTurnOnGps;
        if (isuserDidNotWantToTurnOnGps){
            try {
                if (!isTaskComplete) {
                    isTaskComplete=true;
                    listener.taskComplete(true);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean getIsLocationPermissionGranted(){
        return isLocationPermissionGranted;
    }


    public void setIsLocationPermissionGranted(boolean IsLocationPermissionGranted){
        isLocationPermissionGranted=IsLocationPermissionGranted;
        if (!IsLocationPermissionGranted){
            try {
                if (!isTaskComplete) {
                    isTaskComplete=true;
                    listener.taskComplete(true);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class MyLocationListener  implements LocationListener {
        @Override
        public void onLocationChanged(Location loc) {
            if (loc!=null && fullAddress==null){
                location = loc;
                setAddress(loc);
            }
        }
        @Override
        public void onProviderDisabled(String provider) {}
        @Override
        public void onProviderEnabled(String provider) {}
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    }

    @SuppressLint("MissingPermission")
    public void startFindLocation(){
        try {
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            if (locationManager!=null) {
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    gpsLocationListener = new MyLocationListener();
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 0, gpsLocationListener);
                    //in any case that gps can't find location stop task and punch after 15 seconds
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // do something after 15s = 15000 milliseconds
                            if (fullAddress == null) {
                                try {
                                    if (!isTaskComplete) {
                                        isTaskComplete=true;
                                        listener.taskComplete(true);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }, 15000); //Time in milliseconds*/

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // do something after 3.2s = 32000 miliseconds
                            if (fullAddress == null) {
                                if (!isTaskComplete) {
                                    Toast.makeText(context, "low gps signal", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    }, 3200); //Time in milliseconds*/
                }
                else {
                    if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                        networkLocationListener = new MyLocationListener();
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, networkLocationListener);
                    } else
                        //showGPSDisabledAlertToUser();
                        displayLocationSettingsRequest(context);
                }
                if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // do something after 0.5s = 500 milliseconds
                            if (location == null) {
                                locationManager = (LocationManager)
                                        context.getSystemService(Context.LOCATION_SERVICE);
                                networkLocationListener = new MyLocationListener();
                                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, networkLocationListener);
                            }
                        }
                    }, 7000); //Time in milliseconds*/
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayLocationSettingsRequest(final Context context) {
        PendingResult<LocationSettingsResult> result = null;
        try {
            GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                    .addApi(LocationServices.API).build();
            googleApiClient.connect();
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(10000);
            locationRequest.setFastestInterval(10000 / 2);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
            builder.setAlwaysShow(true);
            result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        } catch (Exception e) {
            showGPSDisabledAlertToUser();
        }
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        //Log.i(TAG, "All location settings are satisfied.");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        //Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");
                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult((Activity)context, 95);
                        } catch (IntentSender.SendIntentException e) {
                            //Log.i(TAG, "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        //Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }
}

