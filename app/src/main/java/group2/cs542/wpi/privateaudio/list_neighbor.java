package group2.cs542.wpi.privateaudio;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleCursorAdapter;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.server.converter.StringToIntConverter;
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
    private ListView list_view;
    private Button back;
    private Button filt;
    private EditText dis;
    private EditText tag;


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapHandle = googleMap;
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

        // find element
        list_view = (ListView) findViewById(R.id.neighborpage_lv_list);
        back = (Button) findViewById(R.id.neighbor_bt_back);
        filt = (Button) findViewById(R.id.neighbor_bt_filt);
        dis = (EditText) findViewById(R.id.neighbor_et_dis);
        tag = (EditText) findViewById(R.id.neighbor_et_tag);

        mapF = (MapFragment) getFragmentManager().findFragmentById(R.id.neighbor_map);
        mapF.getMapAsync(this);

        // setup button on click
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("go index");
                launchIndex();
            }
        });

        filt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("filtering result");
                filtResult();
            }
        });
    }

    private void filtResult() {
        String distance = dis.getText().toString();
        String tag_context = tag.getText().toString();

        // filt query
        String filt_args[] = new String[4];
        filt_args[0] = user_name;
        filt_args[1] = user_name;
        filt_args[2] = tag_context;
        filt_args[3] = distance;
        Cursor filt_res = DBOperator.getInstance().execQuery(SQLCommand.Neighbor_Filt, filt_args);

        // bind the data to list
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                getApplicationContext(), R.layout.activity_listitem, filt_res,
                new String[] { "acc", "_id", "tag", "time" }, new int[] {
                R.id.account, R.id.voiceid, R.id.voicetag, R.id.voicetime },
                SimpleCursorAdapter.IGNORE_ITEM_VIEW_TYPE);

        // show result
        list_view.setAdapter(adapter);

        // get marker locations
        mapHandle.clear();
        Cursor filt_marker = DBOperator.getInstance().execQuery(SQLCommand.Filt_Marker, filt_args);
        while (filt_marker.moveToNext()) {
            float lat, lng;
            lat = Float.parseFloat(filt_marker.getString(filt_marker.getColumnIndex("v.latitude")));
            lng = Float.parseFloat(filt_marker.getString(filt_marker.getColumnIndex("v.longitude")));
            LatLng MarkerLoc = new LatLng(lat, lng);
            mapHandle.addMarker(new MarkerOptions()
                    .position(MarkerLoc)
                    .title("Initial"));
//            mapHandle.moveCamera(CameraUpdateFactory.newLatLngZoom(MarkerLoc, (float) 12.0));
        }
    }

    private void launchIndex() {
        Intent intent = new Intent(this, user_index.class);
        intent.putExtra("User Name", user_name);
        intent.putExtra("User UID", user_uid);
        startActivity(intent);
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
        // request location updates
        LocationServices.FusedLocationApi.requestLocationUpdates(client, mLocationRequest, this);
        // update current locations
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(client);
        updateMap(lastLocation);
    }

    private void updateMap(Location lastLocation) {
        // update location
        String update_args[] = new String[3];
        update_args[0] = String.valueOf(lastLocation.getLatitude());
        update_args[1] = String.valueOf(lastLocation.getLongitude());
        update_args[2] = user_uid;
        DBOperator.getInstance().execSQL(SQLCommand.Update_Loc, update_args);

        // move camera
        LatLng center = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
        mapHandle.moveCamera(CameraUpdateFactory.newLatLngZoom(center, (float) 12.0));

        // init query
        String init_args[] = new String[2];
        init_args[0] = user_uid;
        init_args[1] = user_uid;
        Cursor init_res = DBOperator.getInstance().execQuery(SQLCommand.Neighbor_Audio, init_args);

        // bind the data to list
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                getApplicationContext(), R.layout.activity_listitem, init_res,
                new String[] { "acc", "_id", "tag", "time" }, new int[] {
                R.id.account, R.id.voiceid, R.id.voicetag, R.id.voicetime },
                SimpleCursorAdapter.IGNORE_ITEM_VIEW_TYPE);

        // show result
        list_view.setAdapter(adapter);

        // get marker locations
        Cursor init_marker = DBOperator.getInstance().execQuery(SQLCommand.Neighbor_Marker, init_args);
        while (init_marker.moveToNext()) {
            float lat, lng;
            lat = Float.parseFloat(init_marker.getString(init_marker.getColumnIndex("v.latitude")));
            lng = Float.parseFloat(init_marker.getString(init_marker.getColumnIndex("v.longitude")));
            LatLng MarkerLoc = new LatLng(lat, lng);
            mapHandle.addMarker(new MarkerOptions()
                    .position(MarkerLoc)
                    .title("Initial"));
//            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(MarkerLoc, (float) 12.0));
        }
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

        // update location
        String update_args[] = new String[3];
        update_args[0] = String.valueOf(lastLoc.getLatitude());
        update_args[1] = String.valueOf(lastLoc.getLongitude());
        update_args[2] = user_uid;
        DBOperator.getInstance().execSQL(SQLCommand.Update_Loc, update_args);

//        currentMaker.remove();
//        currentMaker = mapHandle.addMarker(new MarkerOptions()
//                .position(center)
//                .title("Current"));
    }
}
