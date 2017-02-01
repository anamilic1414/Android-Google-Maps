package com.example.nemanja.mapa;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng pozega = new LatLng(43.8466407, 20.0305178);
        mMap.addMarker(new MarkerOptions().position(pozega).title("Pozega"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pozega, 10));

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true); //moja lokacija! :)
            AlertDialog.Builder poruka = new AlertDialog.Builder(this);
            poruka.setMessage("Pre početka proverite da li je vaša lokacija omogućena").setPositiveButton("Nastavi", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).setTitle("Upozorenje!").create();
            poruka.show();
        }

    }


    public void ukljucenaPretraga(View view){
        EditText lokacija_tf = (EditText)findViewById(R.id.TFadresa);
        String lokacija = lokacija_tf.getText().toString();
        List<Address> addressList = null;


        if(lokacija!=null || !lokacija.equals("")){
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(lokacija, 1);
            }catch(IOException e){
                e.printStackTrace();
            }
            Address address = addressList.get(0); //pamti geografsku sirinu i duzinu
            LatLng latLng = new LatLng(address.getLatitude() , address.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        }
    }

    public void oNama(View view){
        AlertDialog.Builder poruka = new AlertDialog.Builder(this);
        poruka.setMessage("Ana, Jovana, Aleksandra").setPositiveButton("Nastavi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setTitle("Autori: ").create();
        poruka.show();
    }

    public void promeniTipMape(View view){
        if(mMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL){
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        }else
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    public void onZoom(View view){
        if(view.getId() == R.id.zoomin){
            mMap.animateCamera(CameraUpdateFactory.zoomIn());
        }
        if (view.getId() == R.id.zoomout){
            mMap.animateCamera(CameraUpdateFactory.zoomOut());
        }
    }

}
