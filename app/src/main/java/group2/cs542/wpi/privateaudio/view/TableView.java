package group2.cs542.wpi.privateaudio.view;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import group2.cs542.wpi.privateaudio.database.DBOperator;

/**
 * Created by sylor on 3/25/17.
 */

public class TableView extends TableLayout {
    public TableView(Context context, String tableName)
    {
        super(context);
        String sql = "select * from " + tableName + ";";
        DBOperator op = DBOperator.getInstance();
        Cursor cursor = op.execQuery(sql, null);
        this.extractData(context, cursor);
    }
    public TableView(Context context, Cursor cursor)
    {
        super(context);
        this.extractData(context, cursor);
    }
    /*
     * fill data in table view with a cursor
     */
    private void extractData(Context context,Cursor cursor)
    {
        TextView textView;
        TableRow row;
        boolean first = true;
        while (cursor.moveToNext())
        {
			/*
 * Before displaying the first row,
 * display column names as a header
 */
            if (first){
                row = new TableRow(context);
                int length = cursor.getColumnCount();
                String[] columnNames = cursor.getColumnNames();
                for (int i=0;i<length;i++)
                {
                    if (i>0){
                        textView = new TextView(context);
                        textView.setWidth(10);
                        textView.setText("|");
                        row.addView(textView);
                    }
                    textView = new TextView(context);
                    textView.setWidth(200);
                    textView.setText(columnNames[i]);
                    row.addView(textView);
                }
                this.addView(row);
                //show separation line
                View line = new View(context);
                line.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,2));
                line.setBackgroundColor(0xFF909090);
                this.addView(line);
                first = false;
            }
//show values in a row
            row = new TableRow(context);
            int length = cursor.getColumnCount();
            for (int i=0;i<length;i++)
            {
                if (i>0){
                    textView = new TextView(context);
                    textView.setWidth(10);
                    textView.setText("|");
                    row.addView(textView);
                }
                textView = new TextView(context);
                textView.setWidth(200);
                textView.setText(cursor.getString(i));
                row.addView(textView);
            }
            this.addView(row);
        }
/*
 * Do not forget to close the cursor!
 * Otherwise database exceptions will be thrown
 */
        cursor.close();
    }
}
