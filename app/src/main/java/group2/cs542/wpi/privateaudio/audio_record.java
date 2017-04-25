package group2.cs542.wpi.privateaudio;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;

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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import group2.cs542.wpi.privateaudio.database.DBOperator;
import group2.cs542.wpi.privateaudio.database.SQLCommand;

/**
 * Created by sylor on 4/22/17.
 */

public class audio_record extends Activity implements View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private GoogleApiClient client;
    private Location lastLoc;
    private LocationRequest mLocationRequest;
    private Button start;
    private Button stop;
    private Button replay;
    private Button submit;
    private String user_name;
    private String user_uid;
    private TextView date;
    private TextView lat;
    private TextView lng;
    private EditText tag;
    private EditText filename;
    private MediaPlayer mediaPlayer;
    private MediaRecorder recorder;
    private String OUTPUT_FILE;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set up view
        setContentView(R.layout.activity_record);
        OUTPUT_FILE= Environment.getExternalStorageDirectory()+"/audiorecorder.3gpp";


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(AppIndex.API).build();

        // get input info
        user_name = getIntent().getStringExtra("User Name");
        user_uid = getIntent().getStringExtra("User UID");

        // find elements
        start = (Button) findViewById(R.id.record_bt_start);
        stop  = (Button) findViewById(R.id.record_bt_stop);
        replay = (Button) findViewById(R.id.record_bt_replay);
        submit = (Button) findViewById(R.id.record_bt_submit);
        date = (TextView) findViewById(R.id.record_tv_date);
        lat = (TextView) findViewById(R.id.record_tv_lat);
        lng = (TextView) findViewById(R.id.record_tv_lng);
        tag = (EditText) findViewById(R.id.record_et_tag);
        filename = (EditText) findViewById(R.id.record_et_name);

        // set onclick
        start.setOnClickListener(this);
        stop.setOnClickListener(this);
        replay.setOnClickListener(this);
        submit.setOnClickListener(this);
    }

    private void visualLocation() {
        // get lat and lng
        String loc_args[] = new String[1];
        loc_args[0] = user_uid;
        Cursor cursor = DBOperator.getInstance().execQuery(SQLCommand.Get_Location, loc_args);
        // set lat and lng
        cursor.moveToFirst();
        lat.setText(cursor.getString(0));
        lng.setText(cursor.getString(1));
    }

    private void visualDate() {
        Date today = new Date();
        String year = String.valueOf(today.getYear()+1900);
        String month, day;
        if (today.getMonth() < 9) {
            month = "0" + String.valueOf(today.getMonth()+1);
        }
        else {
            month = String.valueOf(today.getMonth()+1);
        }
        if (today.getDate() < 10) {
            day = "0" + String.valueOf(today.getDate());
        }
        else {
            day = String.valueOf(today.getDate());
        }
        String currentDate = year + "-" + month + "-" + day;
        date.setText(currentDate);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.record_bt_start) {
            try {
                beginRecording();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (id == R.id.record_bt_stop) {
            stopRecording();
        }
        else if (id == R.id.record_bt_replay) {
            try {
                playRecording();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (id == R.id.record_bt_submit) {
            // submit button
            String insert_args[] = new String[6];
            insert_args[0] = user_uid;
            insert_args[1] = tag.getText().toString();
            insert_args[2] = lat.getText().toString();
            insert_args[3] = lng.getText().toString();
            insert_args[4] = date.getText().toString();
            insert_args[5] = filename.getText().toString();

            DBOperator.getInstance().execSQL(SQLCommand.Insert_Audio, insert_args);

            Intent intent = new Intent(this, user_index.class);
            intent.putExtra("User Name", user_name);
            intent.putExtra("User UID", user_uid);
            startActivity(intent);
        }
    }

    private void beginRecording() throws IOException {
        ditchMediaRecorder();
        File outFile = new File(OUTPUT_FILE);

        if(outFile.exists())
            outFile.delete();

        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(OUTPUT_FILE);
        recorder.prepare();
        recorder.start();

    }

    private void stopRecording() {
        if(recorder != null)
            recorder.stop();
    }

    private void playRecording() throws IOException {
        ditchMediaPlayer();
        mediaPlayer=new MediaPlayer();
        mediaPlayer.setDataSource(OUTPUT_FILE);
        mediaPlayer.prepare();
        mediaPlayer.start();
    }

    private void ditchMediaRecorder() {
        if(recorder != null)
            recorder.release();
    }

    private void ditchMediaPlayer(){
        if(recorder != null)
        {
            try{
                recorder.release();
            }catch(Exception e)
            {
                e.printStackTrace();
            }
        }

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
        // update location
        String update_args[] = new String[3];
        update_args[0] = String.valueOf(lastLocation.getLatitude());
        update_args[1] = String.valueOf(lastLocation.getLongitude());
        update_args[2] = user_uid;
        DBOperator.getInstance().execSQL(SQLCommand.Update_Loc, update_args);
        // init view
        visualDate();
        visualLocation();
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

        // update location
        String update_args[] = new String[3];
        update_args[0] = String.valueOf(lastLoc.getLatitude());
        update_args[1] = String.valueOf(lastLoc.getLongitude());
        update_args[2] = user_uid;
        DBOperator.getInstance().execSQL(SQLCommand.Update_Loc, update_args);
    }
}
