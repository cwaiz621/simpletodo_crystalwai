package com.example.crystal.simpletodo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Crystal on 7/6/15. //added writeItems and delete items
 */
public class customAdapter extends ArrayAdapter<String>{

    ArrayList<String> list;
    Context context;
    private static final String TAG = "cadapter";


    public customAdapter(Context context, int resource, ArrayList<String> array) {
            super(context, resource, array);
            list = array;
            this.context=context;


        }

        @Override
        public View getView(int position, View rowView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            String taskitem = getItem(position);

            //initializing views
            rowView = inflater.inflate(R.layout.listlayout, parent, false);

            final TextView task = (TextView) rowView.findViewById(R.id.task);
            task.setText(taskitem);

            Button submit = (Button) rowView.findViewById(R.id.submit);
            submit.setTag(new Integer(position));
            submit.setBackgroundResource(R.drawable.submit);

            final Button delete = (Button) rowView.findViewById(R.id.trash);
            delete.setTag(new Integer(position));
            delete.setBackgroundResource(R.drawable.trashblue);

            final CheckBox cb = (CheckBox) rowView.findViewById(R.id.cb);
            cb.setTag(new Integer(position));

            final EditText edit = (EditText) rowView.findViewById(R.id.edit);


            final ViewSwitcher TaskEdit = (ViewSwitcher) rowView.findViewById(R.id.vs);


/*When item is checked disable row item, and set completion date. When item is unchecked,
enable row item and reset the creation date*/
            /*cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int pos = (int) cb.getTag();
                    if (cb.isChecked()) {
                        Log.d(TAG, String.valueOf(pos));
                        task.setTextColor(Color.GRAY);
                        TaskEdit.setClickable(false);
                        delete.setBackgroundResource(R.drawable.trashblue);
                    }else{
                        TaskEdit.setClickable(true);
                        task.setTextColor(Color.BLACK);
                        delete.setBackgroundResource(R.drawable.trashblue);

                    }
                }
            });
            */

//When a textview task title is pressed, textview changes to edit text to enable title change
            TaskEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    edit.setText(task.getText());
                    TaskEdit.showNext();
                    edit.requestFocus();

                    edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            TaskEdit.showPrevious();

                        }
                    });
                }
            });
//Upon submit, update task name in array and text view
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Integer pos = (Integer) v.getTag();
                    edit.requestFocus();
                    String temp = edit.getText().toString();
                    list.set(pos, temp);
                    TaskEdit.reset();
                    notifyDataSetChanged();
                    writeItems();
                    edit.clearFocus();
                }
            });

//delete upon pressing delete
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = (int) v.getTag();
                    String removedTask = list.get(pos);
                    Log.d(TAG, list.get(pos));
                    list.remove(pos);
                    writeItems();
                    notifyDataSetChanged();
                    Toast.makeText(getContext(),removedTask,Toast.LENGTH_SHORT);
                }
            });

            return rowView;
        }

    public void add(String text){
        list.add(text);
    }


    private void readItems(){
        File filesDir = getContext().getFilesDir();
        File todoFile = new File(filesDir,"todo.txt");
        try{
            list = new ArrayList<String>(FileUtils.readLines(todoFile));}
        catch (IOException e){
            list = new ArrayList<String>();
        }
    }

    private void writeItems(){
        File filesDir = getContext().getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try{
            list =  new ArrayList<String>(FileUtils.readLines(todoFile));}
        catch (IOException e){
            e.printStackTrace();
        }
    }
}