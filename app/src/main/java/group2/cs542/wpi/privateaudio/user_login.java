package group2.cs542.wpi.privateaudio;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.text.DateFormat;
import java.util.Date;

import group2.cs542.wpi.privateaudio.database.DBOperator;
import group2.cs542.wpi.privateaudio.database.SQLCommand;

/**
 * Created by sylor on 3/7/17.
 */

public class user_login extends Activity {
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private EditText user_acc;
    private EditText user_pwd;
    private Button login;
    private Button reset;
    private Button register;
    private int security_counter = 5;
    private String user_name;
    private String user_uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        setContentView(R.layout.activity_login);

        try{
            DBOperator.copyDB(getBaseContext());
        }catch(Exception e){
            e.printStackTrace();
        }

        user_acc = (EditText) findViewById(R.id.login_user_name);
        user_pwd = (EditText) findViewById(R.id.login_user_pwd);
        login = (Button) findViewById(R.id.login_bt_login);
        reset = (Button) findViewById(R.id.login_bt_reset);
        register = (Button) findViewById(R.id.login_bt_register);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res = DBOperator.getInstance().execQuery(SQLCommand.Login_Check, login_args());
                String num_match = "-1";
                user_uid = "-1";
                if (res.moveToFirst()) { // data?
                    num_match = res.getString(res.getColumnIndex("num_match"));
                    user_uid = res.getString(res.getColumnIndex("uid"));
                }
                res.close(); // that's important too, otherwise you're gonna leak cursors

                if (num_match.equals("1")) {
                    // remove existing active user
                    String remove_args[] = new String[1];
                    remove_args[0] = user_uid;
                    DBOperator.getInstance().execSQL(SQLCommand.Remove_Act, remove_args);
                    // add new activate user
                    Date date = new Date();
                    String year = String.valueOf(date.getYear()+1900);
                    String month, day;
                    if (date.getMonth() < 9) {
                        month = "0" + String.valueOf(date.getMonth()+1);
                    }
                    else {
                        month = String.valueOf(date.getMonth()+1);
                    }
                    if (date.getDate() < 10) {
                        day = "0" + String.valueOf(date.getDate());
                    }
                    else {
                        day = String.valueOf(date.getDate());
                    }
                    String currentDate = year + "-" + month + "-" + day;
                    String update_args[] = new String[2];
                    update_args[0] = user_uid;
                    update_args[1] = currentDate;
                    DBOperator.getInstance().execSQL(SQLCommand.Update_Act, update_args);
                    System.out.println(currentDate);

                    launchIndex();
                }
                else {
                    security_counter--;
                    if (security_counter == 0) {
                        login.setEnabled(false);
                        System.out.println("Login Disabled");
                    }
                }
            }
        });

        reset.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                user_acc.getText().clear();
                user_pwd.getText().clear();
            }
        });

        register.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                launchRegister();
            }
        });
    }

    private void launchRegister() {
        Intent intent = new Intent(this, user_register.class);
        startActivity(intent);
    }

    private void launchIndex() {
        Intent intent = new Intent(this, user_index.class);
        user_name = user_acc.getText().toString();
        intent.putExtra("User Name", user_name);
        intent.putExtra("User UID", user_uid);
        startActivity(intent);
    }

    private String[] login_args() {
        String args[] = new String[2];

        args[0] = user_acc.getText().toString();
        args[1] = user_pwd.getText().toString();

        return args;
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("user_login Page") // TODO: Define a title for the content shown.
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
