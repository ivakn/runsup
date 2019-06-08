package si.uni_lj.fri.pbd2019.runsup;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class HistoryListAdapter extends ArrayAdapter<Item> {

    LayoutInflater inflater;
    private List<Item> itemList;
    private Context context;

    public HistoryListAdapter(@NonNull Context context,int resource, ArrayList<Item> list) {
        super(context ,resource,list);
        this.itemList=list;
        this.context=context;

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return itemList.size();
    }
    @Override
    public Item getItem(int position) {
        return itemList.get(position);
    }
    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("Adapter","Usao");
        View listItem = convertView;
        if(listItem == null)
            inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            listItem = inflater.inflate(R.layout.adapter_history,null);

        Item currentItem = itemList.get(position);

        TextView title = (TextView) listItem.findViewById(R.id.textview_history_title);
        title.setText(currentItem.getTitle());

        TextView date = (TextView) listItem.findViewById(R.id.textview_history_datetime);
        date.setText(currentItem.getDate());

        TextView details = (TextView) listItem.findViewById(R.id.textview_history_sportactivity);
        details.setText(currentItem.getDuration()+" | "+currentItem.getDistance()+" | "+ currentItem.getCalories());

        return listItem;
    }
}

