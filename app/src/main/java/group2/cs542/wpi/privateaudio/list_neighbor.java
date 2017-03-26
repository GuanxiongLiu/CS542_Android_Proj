package group2.cs542.wpi.privateaudio;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.widget.ScrollView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import group2.cs542.wpi.privateaudio.database.DBOperator;
import group2.cs542.wpi.privateaudio.database.SQLCommand;
import group2.cs542.wpi.privateaudio.view.TableView;

/**
 * Created by sylor on 3/17/17.
 */

public class list_neighbor extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private MapFragment mapF;
    private Location lastLoc;
    private GoogleMap mapHandle;
    private LocationRequest mLocationRequest;
    private Marker currentMaker;
    private String user_name;
    private String user_uid;
    private ScrollView list_view;


    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng HomeLoc = new LatLng(42.2, -71.8);
//        currentMaker = googleMap.addMarker(new MarkerOptions()
//                                            .position(HomeLoc)
//                                            .title("Initial"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(HomeLoc, (float) 12.0));
        mapHandle = googleMap;

        // init query
        String init_args[] = new String[2];
        init_args[0] = user_uid;
        init_args[1] = user_uid;
        Cursor init_res = DBOperator.getInstance().execQuery(SQLCommand.Neighbor_Audio, init_args);
        list_view.addView(new TableView(this.getBaseContext(),init_res));
        // get marker locations
        Cursor init_marker = DBOperator.getInstance().execQuery(SQLCommand.Neighbor_Marker, init_args);
        while (init_marker.moveToNext()) {
            float lat, lng;
            lat = Float.parseFloat(init_marker.getString(init_marker.getColumnIndex("v.latitude")));
            lng = Float.parseFloat(init_marker.getString(init_marker.getColumnIndex("v.longitude")));
            LatLng MarkerLoc = new LatLng(lat, lng);
            googleMap.addMarker(new MarkerOptions()
                                .position(MarkerLoc)
                                .title("Initial"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(MarkerLoc, (float) 12.0));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(AppIndex.API).build();

        setContentView(R.layout.activity_neighbor);
        user_name = getIntent().getStringExtra("User Name");
        user_uid = getIntent().getStringExtra("User UID");
        System.out.println(user_uid);

        list_view = (ScrollView) findViewById(R.id.neighbor_sv_list);

        mapF = (MapFragment) getFragmentManager().findFragmentById(R.id.neighbor_map);
        mapF.getMapAsync(this);

    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("list_neighbor Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);

        startLocationUpdates();
    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(client, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        lastLoc = location;
        LatLng center = new LatLng(lastLoc.getLatitude(), lastLoc.getLongitude());
        mapHandle.moveCamera(CameraUpdateFactory.newLatLngZoom(center, (float) 12.0));

//        currentMaker.remove();
//        currentMaker = mapHandle.addMarker(new MarkerOptions()
//                .position(center)
//                .title("Current"));
    }
}
