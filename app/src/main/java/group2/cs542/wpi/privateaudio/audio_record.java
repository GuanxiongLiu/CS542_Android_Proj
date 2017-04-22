package group2.cs542.wpi.privateaudio;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

/**
 * Created by sylor on 4/22/17.
 */

public class audio_record extends Activity implements View.OnClickListener {
    private Button start;
    private Button stop;
    private Button replay;
    private Button submit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set up view
        setContentView(R.layout.activity_record);
        // find elements
        start = (Button) findViewById(R.id.record_bt_start);
        stop  = (Button) findViewById(R.id.record_bt_stop);
        replay = (Button) findViewById(R.id.record_bt_replay);
        submit = (Button) findViewById(R.id.record_bt_submit);
        // set onclick
        start.setOnClickListener(this);
        stop.setOnClickListener(this);
        replay.setOnClickListener(this);
        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.record_bt_start) {
            // start button
        }
        else if (id == R.id.record_bt_stop) {
            // stop button
        }
        else if (id == R.id.record_bt_replay) {
            // replay button
        }
        else if (id == R.id.record_bt_submit) {
            // submit button
        }
    }
}
