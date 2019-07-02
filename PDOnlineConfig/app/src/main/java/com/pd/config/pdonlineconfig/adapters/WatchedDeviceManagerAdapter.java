package com.pd.config.pdonlineconfig.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.pd.config.pdonlineconfig.R;
import com.pd.config.pdonlineconfig.pojo.WatchedDevice;

import java.util.List;

public class WatchedDeviceManagerAdapter extends ArrayAdapter {
    private Context context;
    private List<WatchedDevice> listOfControlUnit;
    private int currentPosition=-1;

    public WatchedDeviceManagerAdapter(Context context, int resource, List<WatchedDevice> list) {
        super(context, resource);
        listOfControlUnit = list;
        this.context = context;

    }



    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;

    }

    private static class ViewHolder{
        TextView sort;
        TextView nameOfWatchedDevice;
        TextView codeOfWathcedDevice;
        LinearLayout layout ;
        TextView typeOfWatchedDevice;

    }
    @Override
    public int getCount() {
        return listOfControlUnit.size();
    }

    @Override
    public WatchedDevice getItem(int position) {
        return  listOfControlUnit.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        WatchedDeviceManagerAdapter.ViewHolder viewHolder = null;
        if(convertView==null){
            viewHolder = new ViewHolder();
            LayoutInflater mInflater = LayoutInflater.from(context);
            convertView = mInflater.inflate(R.layout.used_bt_task_format_tri_detected,null);
            viewHolder.layout = convertView.findViewById(R.id.used_task_double_line);
            viewHolder.nameOfWatchedDevice = convertView.findViewById(R.id.nameOfWatchedDevice);
            viewHolder.codeOfWathcedDevice = convertView.findViewById(R.id.codeOfWatchedDevice);
            viewHolder.typeOfWatchedDevice = convertView.findViewById(R.id.typeOfWatchedDevice);
            viewHolder.sort = convertView.findViewById(R.id.sortText);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (WatchedDeviceManagerAdapter.ViewHolder) convertView.getTag();
        }
        WatchedDevice unit = listOfControlUnit.get(position);

        if(unit!=null){
            if(position==currentPosition){
                viewHolder.sort.setText(position+1+"");

                viewHolder.sort.setBackground(context.getDrawable(R.drawable.bg_edittext_focused));
                viewHolder.nameOfWatchedDevice.setText(unit.getNameOfWatchedDevice());
                viewHolder.codeOfWathcedDevice.setText(unit.getCodeOfWatchedDevice());
                viewHolder.typeOfWatchedDevice.setText(unit.getTypeOfWatchedDevice());
                viewHolder.layout.setBackground(context.getDrawable(R.drawable.bg_edittext_focused));
            }else {
                viewHolder.sort.setText(position+1+"");
                viewHolder.sort.setBackground(context.getDrawable(R.drawable.bg_edittext));
                viewHolder.nameOfWatchedDevice.setText(unit.getNameOfWatchedDevice());
                viewHolder.codeOfWathcedDevice.setText(unit.getCodeOfWatchedDevice());
                viewHolder.typeOfWatchedDevice.setText(unit.getTypeOfWatchedDevice());
                viewHolder.layout.setBackground(context.getDrawable(R.drawable.bg_edittext));
            }

        }
        return convertView;
    }
}
