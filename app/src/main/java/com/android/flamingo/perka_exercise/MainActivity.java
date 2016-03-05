package com.android.flamingo.perka_exercise;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.woxthebox.draglistview.DragItem;
import com.woxthebox.draglistview.DragListView;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends Activity {
    //this will be the list of counters.
    private List<count> datalist;
    //this will be the container that holds the counters, there will only be one of it by default
    private countList list;
    //custom listview adapter
    private countAdapter listAdapter;
    private String new_name;
    //3rd party listview for drag and drop
    private DragListView mDragListView;
    //a temporary instance of counter for creation and renaming purposes
    private count c;
    private int size;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // add a new counter
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                c=new count(list,"New Counter",datalist.size());
                //adds new counter to our datalist
                datalist.add(c);
                //notify the adapter a new item has been added
                listAdapter.notifyDataSetChanged();
                //shows the renaming pop up right after
                showNameDialog();
            }
        });
        //populates our listview data
        try{
            populateData();
        }catch(SQLException e){
            e.printStackTrace(System.out);

        }
        //initialize draglistview
        mDragListView = (DragListView) findViewById(R.id.drag_list_view);
        mDragListView.getRecyclerView().setVerticalScrollBarEnabled(true);
        mDragListView.setDragListListener(new DragListView.DragListListener() {
            @Override
            public void onItemDragStarted(int position) {
            }

            @Override
            public void onItemDragEnded(int fromPosition, int toPosition) {
                if (fromPosition != toPosition) {
                    updatePosition();
                }

            }
        });
        setupListRecyclerView();

    }
    public void updatePosition(){
        for(int i=0;i<datalist.size();i++){
            try {
                datalist.set(i,getdb.getCountDB(this).queryForId(datalist.get(i).getid()));
                datalist.get(i).updatePosition(i);
                updateCountDB(datalist.get(i));
            }catch (SQLException e){
                e.printStackTrace(System.out);
            }
        }
    }
    private void populateData()throws SQLException{
        //fetch the list container from db
        this.list=getdb.getCountListDB(this).queryForId(countList.CONTAINER_ID);
        //init datalist
        this.datalist=new ArrayList<>();
        //if list container is null, this will be the first time this app is being launched,
        //therefore we will need to create a new container and update it in the db
        if(list==null){
            list=new countList();
            updateListDB();

        }else {
            //grabs the datalist from the container and check for any lazy deletion
            for(count itt:list.getCounts()){
                if(!itt.ifDel()){
                    this.datalist.add(itt);
                }
            }
            //sorts the datalist base on their position
            Collections.sort(datalist,new Comparator<count>(){
                public int compare(count l,count r){
                    if(l.getPosition() == r.getPosition())
                        return 0;
                    return l.getPosition() < r.getPosition() ? -1 : 1;
                }
            });
        }

    }
    //updates container db
    public  void updateListDB() throws SQLException{
        getdb.getCountListDB(this).createOrUpdate(list);
    }
    //updates count db
    public void updateCountDB(count c) throws SQLException{
        getdb.getCountDB(this).createOrUpdate(c);
    }
    //renaming a counter
    private void showNameDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add new counter");
        final EditText input = new EditText(this);
        input.setText("New Counter ");
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                c.setName(input.getText().toString());
                try {
                    updateCountDB(c);

                } catch (SQLException e) {
                    e.printStackTrace(System.out);

                }

                listAdapter.notifyDataSetChanged();

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                datalist.remove(c);
                listAdapter.notifyDataSetChanged();
                dialog.cancel();
            }
        });

        builder.show();
    }
    //set up the drag and drop listview
    private void setupListRecyclerView() {
        mDragListView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        listAdapter = new countAdapter(this,datalist, R.layout.countlist, R.id.counter_name, false);
        mDragListView.setAdapter(listAdapter, true);
        mDragListView.setCanDragHorizontally(false);
        mDragListView.setCustomDragItem(new MyDragItem(getApplicationContext(), R.layout.countlist));
    }
    //sets the name of the counter as the trigger for drag and drop
    private static class MyDragItem extends DragItem {

        public MyDragItem(Context context, int layoutId) {
            super(context, layoutId);
        }

        @Override
        public void onBindDragView(View clickedView, View dragView) {
            CharSequence name = ((TextView) clickedView.findViewById(R.id.counter_name)).getText();
            ((TextView) dragView.findViewById(R.id.counter_name)).setText(name);
            CharSequence count = ((TextView) clickedView.findViewById(R.id.count_num)).getText();
            ((TextView) dragView.findViewById(R.id.count_num)).setText(count);
            dragView.setBackgroundColor(dragView.getResources().getColor(R.color.list_item_background));
        }
    }




}
