package com.example.group26.inclass13;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.plus.Plus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    AlertDialog alertDialog;
    String selectedPlaceType;
    Spinner mSpinner;

    Map<String, Integer> placeTypeCodes = new HashMap<String, Integer>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_maps);
        setContentView(R.layout.blanklayout);
        placeTypeCodes.put("airport", 2);
        placeTypeCodes.put("atm", 6);
        placeTypeCodes.put("bank", 8);
        placeTypeCodes.put("cafe", 15);
        placeTypeCodes.put("parking", 70);
        placeTypeCodes.put("food", 38);
        placeTypeCodes.put("school", 82);



//
//        mGoogleApiClient = new GoogleApiClient
//                .Builder(this)
//                .addApi(Places.GEO_DATA_API)
//                .addApi(Places.PLACE_DETECTION_API)
//                .build();

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        mGoogleApiClient.connect();

        LayoutInflater li = LayoutInflater.from(this);

        View promptsView = li.inflate(R.layout.my_dialog_layout, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setView(promptsView);

        // set dialog message

        alertDialogBuilder.setTitle("Select the place type");

        // create alert dialog
        alertDialog = alertDialogBuilder.create();

        mSpinner= (Spinner) promptsView
                .findViewById(R.id.mySpinner);
        final Button mButton = (Button) promptsView
                .findViewById(R.id.spinnerButton);
        mButton.setOnClickListener(new SpinnerButtonClickListener());

        // show it
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

            try {
                Log.d("demo", "got this far 1");
                PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi
                        .getCurrentPlace(mGoogleApiClient, null);
                result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
                    @Override
                    public void onResult(PlaceLikelihoodBuffer likelyPlaces) {
                        Log.d("demo", "got this far 2 ");
                        Log.d("demo", likelyPlaces.getStatus().getStatusMessage());
                        Log.d("demo", likelyPlaces.getStatus().toString());
                        Log.d("demo", likelyPlaces.getCount() + "");

                        LatLngBounds.Builder builder = new LatLngBounds.Builder();

                        for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                            Log.d("demo", String.format("Place '%s' has likelihood: %g",
                                    placeLikelihood.getPlace().getName(),
                                    placeLikelihood.getLikelihood()));
                                    LatLng latLng = placeLikelihood.getPlace().getLatLng();

                            // need to check if place matches the place chosen by the user, if it does change the color to blue
                            Marker marker;

                            if(placeLikelihood.getPlace().getPlaceTypes().contains(placeTypeCodes.get(selectedPlaceType))) {
                                marker = mMap.addMarker(new MarkerOptions().position(latLng)
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                                        .title(placeLikelihood.getPlace().getName().toString()));
                            }
                            else {
                                marker = mMap.addMarker(new MarkerOptions().position(latLng).title(placeLikelihood.getPlace().getName().toString()));
                            }
                            marker.showInfoWindow();
                            builder.include(marker.getPosition());

                        }
                        likelyPlaces.release();

                        LatLngBounds bounds = builder.build();

                        int padding = 0; // offset from edges of the map in pixels
                        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

                        mMap.moveCamera(cu);

                    }
                });
            }
            catch (SecurityException e){
                Log.d("demo", e.getMessage());
                Log.d("demo", e.getStackTrace().toString());
            }

        // Add a marker in Sydney and move the camera

//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));



    }

    private class SpinnerButtonClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            selectedPlaceType = mSpinner.getSelectedItem().toString();
            Toast.makeText(MapsActivity.this, "Button Clicked with: " + selectedPlaceType, Toast.LENGTH_SHORT).show();


            alertDialog.cancel();
            setContentView(R.layout.activity_maps);

            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
            mapFragment.getMapAsync(MapsActivity.this);

        }
    }
}
