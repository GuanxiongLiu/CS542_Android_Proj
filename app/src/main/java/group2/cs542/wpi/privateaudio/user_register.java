package group2.cs542.wpi.privateaudio;

import android.app.Activity;
import android.app.AlertDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by sylor on 3/8/17.
 */

public class user_register extends Activity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private EditText user_id;
    private EditText user_pwd;
    private EditText user_pwd_re;
    private Button submit;
    private Button reset;
    private AlertDialog.Builder dlgAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        setContentView(R.layout.activity_register);


        user_id = (EditText) findViewById(R.id.register_user_id);
        user_pwd = (EditText) findViewById(R.id.register_user_pwd);
        user_pwd_re = (EditText) findViewById(R.id.register_user_pwd_re);
        submit = (Button) findViewById(R.id.register_bt_submit);
        reset = (Button) findViewById(R.id.register_bt_reset);

        dlgAlert = new AlertDialog.Builder(this);


        submit.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                System.out.println(user_id.getText().toString());

                if (user_id.getText().toString().equals("")) {
                    user_id.getText().clear();
                    user_pwd.getText().clear();
                    user_pwd_re.getText().clear();
                    dlgAlert.setMessage("Empty User Name");
                    dlgAlert.setPositiveButton("OK", null);
                    dlgAlert.setCancelable(true);
                    dlgAlert.create().show();
                }
                else if (user_pwd.getText().toString().equals("") || user_pwd_re.getText().toString().equals("")) {
                    user_id.getText().clear();
                    user_pwd.getText().clear();
                    user_pwd_re.getText().clear();
                    dlgAlert.setMessage("Empty Password");
                    dlgAlert.setPositiveButton("OK", null);
                    dlgAlert.setCancelable(true);
                    dlgAlert.create().show();
                }
                else if (!(user_pwd.getText().toString().equals(user_pwd_re.getText().toString()))) {
                    user_id.getText().clear();
                    user_pwd.getText().clear();
                    user_pwd_re.getText().clear();
                    dlgAlert.setMessage("Different Password");
                    dlgAlert.setPositiveButton("OK", null);
                    dlgAlert.setCancelable(true);
                    dlgAlert.create().show();
                }
                else {
                    System.out.println("New user added");
                    setContentView(R.layout.activity_login);
                }
            }
        });

        reset.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                user_id.getText().clear();
                user_pwd.getText().clear();
                user_pwd_re.getText().clear();
            }
        });
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("user_register Page") // TODO: Define a title for the content shown.
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
