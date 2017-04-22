package group2.cs542.wpi.privateaudio;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import group2.cs542.wpi.privateaudio.database.DBOperator;
import group2.cs542.wpi.privateaudio.database.SQLCommand;

/**
 * Created by sylor on 3/17/17.
 */

public class user_index extends Activity {
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private TextView title;
    private Button toNeighbor;
    private Button toSelfpage;
    private Button toFriends;
    private Button record;
    private Button logout;
    private String user_name;
    private String user_uid;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        setContentView(R.layout.activity_index);
        user_name = getIntent().getStringExtra("User Name");
        user_uid = getIntent().getStringExtra("User UID");

        title = (TextView) findViewById(R.id.index_tv_title);
        title.setText("Welcome User " + user_name);

        toNeighbor = (Button) findViewById(R.id.index_bt_toNeighbor);
        toSelfpage = (Button) findViewById(R.id.index_bt_toSelfpage);
        toFriends  = (Button) findViewById(R.id.index_bt_toFriend);
        record     = (Button) findViewById(R.id.index_bt_record);
        logout     = (Button) findViewById(R.id.index_bt_logout);


        toNeighbor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("go neighbor");
                launchNeighbor();
            }
        });

        toSelfpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("go selfpage");
                launchSelfpage();
            }
        });

        toFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("go friend");
                launchFriends();
            }
        });

        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("go record");
                launchRecord();
            }
        });

        logout.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                System.out.println("logout");
                launchLogout();
            }
        });

    }

    private void launchRecord() {
        Intent intent = new Intent(this, audio_record.class);
        intent.putExtra("User Name", user_name);
        intent.putExtra("User UID", user_uid);
        startActivity(intent);
    }

    private void launchLogout() {
        // delete from active user
        String sql_args[] = new String[1];
        sql_args[0] = user_uid;
        // execute sql
        DBOperator.getInstance().execSQL(SQLCommand.Logout, sql_args);
        // jump to login
        Intent intent = new Intent(this, user_login.class);
        startActivity(intent);
    }


    private void launchNeighbor() {
        Intent intent = new Intent(this, list_neighbor.class);
        intent.putExtra("User Name", user_name);
        intent.putExtra("User UID", user_uid);
        startActivity(intent);
    }

    private void launchSelfpage() {
        Intent intent = new Intent(this, list_selfpage.class);
        intent.putExtra("User Name", user_name);
        intent.putExtra("User UID", user_uid);
        startActivity(intent);
    }

    private void launchFriends() {
        Intent intent = new Intent(this, list_friends.class);
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
                .setName("user_index Page") // TODO: Define a title for the content shown.
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
}
