package com.android.flamingo.perka_exercise;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.woxthebox.draglistview.DragItemAdapter;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Qiyuan on 3/3/2016.
 */
public class countAdapter extends DragItemAdapter<count, countAdapter.ViewHolder> {

    private count c;
    private int mLayoutId;
    private int mGrabHandleId;
    private Context context;

    public countAdapter(Context context, List<count> data, int layoutId, int grabHandleId, boolean dragOnLongPress) {
        super(dragOnLongPress);
        mLayoutId = layoutId;
        mGrabHandleId = grabHandleId;
        setHasStableIds(true);
        setItemList(data);
        this.context = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(mLayoutId, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        // gets the update the item in position
        try {
            c = getUpdate(mItemList.get(position).getid());
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
        // if c == null, this counter has just been created and have not yet been added to db, therefore no need to get update of it in db
        if (c == null) {
            c = mItemList.get(position);
        }
        holder.position = position;
        holder.name_view.setText(c.getName());
        holder.count_view.setText(c.getNum() + "");
        holder.itemView.setTag(c.getName());
    }
    //returns the item's db id
    @Override
    public long getItemId(int position) {
        return mItemList.get(position).getid();
    }
    //add value to counter
    public void addNum(count c) throws SQLException {
        c.addNum();
        updateCountDB(c);
        notifyDataSetChanged();

    }
    //take away value from counter
    public void minusNum(count c) throws SQLException {
        c.delNum();
        updateCountDB(c);
        notifyDataSetChanged();

    }
    //update countdb
    public void updateCountDB(count c) throws SQLException {
        getdb.getCountDB(context).createOrUpdate(c);
    }
    //gets updated version of count
    public count getUpdate(int x) throws SQLException {
        return getdb.getCountDB(context).queryForId(x);
    }

    public class ViewHolder extends DragItemAdapter<count, countAdapter.ViewHolder>.ViewHolder {
        public TextView name_view;
        public TextView count_view;
        public int position;
        private count c;
        public ViewHolder(final View itemView) {
            super(itemView, mGrabHandleId);
            name_view = (TextView) itemView.findViewById(R.id.counter_name);
            count_view = (TextView) itemView.findViewById(R.id.count_num);
            //sets the button listener for add and subtract
            ((Button) itemView.findViewById(R.id.sub)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        c = getUpdate(mItemList.get(position).getid());
                        minusNum(c);

                    } catch (SQLException e) {
                        e.printStackTrace(System.out);
                    }
                }
            });
            ((Button) itemView.findViewById(R.id.add)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        c = getUpdate(mItemList.get(position).getid());
                        addNum(c);
                    } catch (SQLException e) {
                        e.printStackTrace(System.out);
                    }
                }
            });

        }

        // when a item is long pressed, it trigger the delete dialog
        @Override
        public boolean onItemLongClicked(View view) {
            showDelDialog(position);
            return true;
        }
        private void showDelDialog(final int pos) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Are you sure you want to delete?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //removes item from adapter
                    //lazy deletes the item from db
                    try {
                        c=mItemList.get(position);
                        removeItem(pos);
                        c.delete();
                        updateCountDB(c);
                    }catch (SQLException e){
                        e.printStackTrace(System.out);
                    }
                    notifyDataSetChanged();


                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
        }
    }



}
