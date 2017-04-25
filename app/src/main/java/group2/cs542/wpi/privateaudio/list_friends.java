package group2.cs542.wpi.privateaudio;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.media.MediaRecorder;
import group2.cs542.wpi.privateaudio.database.DBOperator;
import group2.cs542.wpi.privateaudio.database.SQLCommand;
import group2.cs542.wpi.privateaudio.view.TableView;

import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

/**
 * Created by sylor on 3/24/17.
 */

public class list_friends extends Activity {

    private String user_name;
    private String user_uid;
    private ListView list_view;
    private Button back;
    private Button filt;
    private EditText tag;
    private EditText name;
    private DatePicker date;
    private TextView selectedAudio;
    private Button play;
    private Button pause;
    private MediaPlayer mediaPlayer;
    private MediaRecorder recorder;
    private String path;
    private String OUTPUT_FILE;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_friends);
        user_name = getIntent().getStringExtra("User Name");
        user_uid = getIntent().getStringExtra("User UID");


        // find element
        list_view = (ListView) findViewById(R.id.friendpage_lv_list);
        back = (Button) findViewById(R.id.friends_bt_back);
        filt = (Button) findViewById(R.id.friends_bt_filt);
        tag = (EditText) findViewById(R.id.friend_et_tag);
        name = (EditText) findViewById(R.id.friend_et_name);
        date = (DatePicker) findViewById(R.id.friends_dp);
        selectedAudio = (TextView) findViewById(R.id.friend_tv_selected);
        play = (Button) findViewById(R.id.friend_bt_play);
        pause = (Button) findViewById(R.id.friend_bt_pause);

        // init query
        String init_args[] = new String[1];
        init_args[0] = user_name;
        Cursor init_res = DBOperator.getInstance().execQuery(SQLCommand.Friend_Audio, init_args);

        // bind the data to list
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                getApplicationContext(), R.layout.activity_listitem, init_res,
                new String[] { "acc", "_id", "tag", "time" }, new int[] {
                R.id.account, R.id.voiceid, R.id.voicetag, R.id.voicetime },
                SimpleCursorAdapter.IGNORE_ITEM_VIEW_TYPE);

        // show result
        list_view.setAdapter(adapter);

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

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String AUDIO_FILE = Environment.getExternalStorageDirectory()+"/DemoAudios/"+path;
                mediaPlayer=new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(AUDIO_FILE);
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.start();
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.pause();
            }
        });

        list_view.setOnItemClickListener(new ItemClickListener());

    }


    private void launchIndex() {
        Intent intent = new Intent(this, user_index.class);
        intent.putExtra("User Name", user_name);
        intent.putExtra("User UID", user_uid);
        startActivity(intent);
    }

    private void filtResult() {
        String tag_context = tag.getText().toString();
        String name_context = name.getText().toString();
        String year = String.valueOf(date.getYear());
        String month, day;
        if (date.getMonth() < 9) {
            month = "0" + String.valueOf(date.getMonth()+1);
        }
        else {
            month = String.valueOf(date.getMonth()+1);
        }
        if (date.getDayOfMonth() < 10) {
            day = "0" + String.valueOf(date.getDayOfMonth());
        }
        else {
            day = String.valueOf(date.getDayOfMonth());
        }
        String dt = year + "-" + month + "-" + day;

        // filt query
        String filt_args[] = new String[4];
        filt_args[0] = user_name;
        filt_args[1] = name_context;
        filt_args[2] = tag_context;
        filt_args[3] = dt;
        Cursor filt_res = DBOperator.getInstance().execQuery(SQLCommand.Friend_Filt, filt_args);

        // bind the data to list
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                getApplicationContext(), R.layout.activity_listitem, filt_res,
                new String[] { "acc", "_id", "tag", "time" }, new int[] {
                R.id.account, R.id.voiceid, R.id.voicetag, R.id.voicetime },
                SimpleCursorAdapter.IGNORE_ITEM_VIEW_TYPE);

        // show result
        list_view.setAdapter(adapter);
    }

    class ItemClickListener implements AdapterView.OnItemClickListener {
        public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
            Cursor cursor = (Cursor) list_view.getItemAtPosition(position);
            selectedAudio.setText(cursor.getString(1));
            path = cursor.getString(4);
            play.setVisibility(View.VISIBLE);
            pause.setVisibility(View.VISIBLE);
        }
    }
}
