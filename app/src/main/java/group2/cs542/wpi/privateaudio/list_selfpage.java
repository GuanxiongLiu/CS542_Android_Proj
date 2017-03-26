package group2.cs542.wpi.privateaudio;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.widget.ScrollView;

import group2.cs542.wpi.privateaudio.database.DBOperator;
import group2.cs542.wpi.privateaudio.database.SQLCommand;
import group2.cs542.wpi.privateaudio.view.TableView;

/**
 * Created by sylor on 3/19/17.
 */

public class list_selfpage extends Activity {

    private String user_name;
    private ScrollView list_view;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_selfpage);
        user_name = getIntent().getStringExtra("User Name");

        list_view = (ScrollView) findViewById(R.id.selfpage_sv_list);

        // init query
        String init_args[] = new String[1];
        init_args[0] = user_name;
        Cursor init_res = DBOperator.getInstance().execQuery(SQLCommand.Self_Audio, init_args);
        list_view.addView(new TableView(this.getBaseContext(),init_res));
    }



}
