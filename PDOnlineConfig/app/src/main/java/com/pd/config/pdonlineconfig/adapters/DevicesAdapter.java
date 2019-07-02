package com.pd.config.pdonlineconfig.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.pd.config.pdonlineconfig.R;
import com.pd.config.pdonlineconfig.pojo.DeviceInfo;

import java.util.List;

public class DevicesAdapter extends ArrayAdapter<DeviceInfo> {
    private Context context;
    private List<DeviceInfo> listOfControlUnit;
    private int currentPosition=-1;

    public DevicesAdapter(Context context, int resource, List<DeviceInfo> list) {
        super(context, resource);
        listOfControlUnit = list;
        this.context = context;

    }

    public List<DeviceInfo> getListOfControlUnit() {
        return listOfControlUnit;
    }

    public void setListOfControlUnit(List<DeviceInfo> listOfControlUnit) {
        this.listOfControlUnit = listOfControlUnit;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    private static class ViewHolder{
        TextView sort;
        TextView ipAddress;
        TextView codeOfDevice;
        LinearLayout layout ;

    }
    @Override
    public int getCount() {
        return listOfControlUnit.size();
    }

    @Override
    public DeviceInfo getItem(int position) {
        return  listOfControlUnit.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView==null){
            viewHolder = new ViewHolder();
            LayoutInflater mInflater = LayoutInflater.from(context);
            convertView = mInflater.inflate(R.layout.used_bt_task_format_double,null);
            viewHolder.layout = convertView.findViewById(R.id.used_task_double_line);
            viewHolder.ipAddress = convertView.findViewById(R.id.ipAddress);
            viewHolder.codeOfDevice = convertView.findViewById(R.id.codeOfDevice);
            viewHolder.sort = convertView.findViewById(R.id.sortText);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
       DeviceInfo unit = listOfControlUnit.get(position);

        if(unit!=null){
            if(position==currentPosition){
                viewHolder.sort.setText(position+1+"");
                viewHolder.sort.setBackground(context.getDrawable(R.drawable.bg_edittext_focused));
                viewHolder.codeOfDevice.setText(unit.getCodeOfDevice());
                viewHolder.ipAddress.setText(unit.getIpAddress());
                viewHolder.layout.setBackground(context.getDrawable(R.drawable.bg_edittext_focused));
            }else {
                viewHolder.sort.setText(position+1+"");
                viewHolder.sort.setBackground(context.getDrawable(R.drawable.bg_edittext));
                viewHolder.codeOfDevice.setText(unit.getCodeOfDevice());
                viewHolder.ipAddress.setText(unit.getIpAddress());
                viewHolder.layout.setBackground(context.getDrawable(R.drawable.bg_edittext));
            }

        }
        return convertView;
    }
}
