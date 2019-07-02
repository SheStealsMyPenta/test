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
import com.pd.config.pdonlineconfig.pojo.MonitorDevice;

import java.util.List;

public class MonitorDeviceManagerAdapter extends ArrayAdapter<MonitorDevice> {
    private Context context;
    private List<MonitorDevice> listOfControlUnit;
    private int currentPosition = -1;

    public MonitorDeviceManagerAdapter(Context context, int resource, List<MonitorDevice> list) {
        super(context, resource);
        listOfControlUnit = list;
        this.context = context;

    }

    public List<MonitorDevice> getListOfControlUnit() {
        return listOfControlUnit;
    }

    public void setListOfControlUnit(List<MonitorDevice> listOfControlUnit) {
        this.listOfControlUnit = listOfControlUnit;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;

    }

    private static class ViewHolder {
        TextView sort;
        TextView codeOfDevice;
        TextView physicAddress;
        LinearLayout layout;
        TextView typeOfDevice;
        TextView test;

    }

    @Override
    public int getCount() {
        return listOfControlUnit.size();
    }

    @Override
    public MonitorDevice getItem(int position) {
        return listOfControlUnit.get(position);
    }

    public DeviceInfo getADeviceInfo(int position) {

        MonitorDevice item = getItem(position);
        DeviceInfo info = new DeviceInfo();
        info.setCodeOfDevice(item.getCodeOfMonitor());
        info.setPhysicAddress(item.getPhysicAddress());
        info.setTypeOfMonitorDevice(item.getTypeOfTheMonitor());
        return info;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MonitorDeviceManagerAdapter.ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater mInflater = LayoutInflater.from(context);
            convertView = mInflater.inflate(R.layout.used_bt_task_format_tri, null);
            viewHolder.layout = convertView.findViewById(R.id.used_task_double_line);
            viewHolder.physicAddress = convertView.findViewById(R.id.physicAddress);
            viewHolder.typeOfDevice = convertView.findViewById(R.id.typeOfDevice);
            viewHolder.codeOfDevice = convertView.findViewById(R.id.codeOfDevice);

            viewHolder.sort = convertView.findViewById(R.id.sortText);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MonitorDeviceManagerAdapter.ViewHolder) convertView.getTag();
        }
        MonitorDevice unit = listOfControlUnit.get(position);

        if (unit != null) {
            if (position == currentPosition) {
                viewHolder.sort.setText(position + 1 + "");
                viewHolder.sort.setBackground(context.getDrawable(R.drawable.bg_edittext_focused));
                viewHolder.codeOfDevice.setText(unit.getCodeOfMonitor());
                viewHolder.physicAddress.setText(unit.getPhysicAddress());
                viewHolder.typeOfDevice.setText(unit.getTypeOfTheMonitor());
                viewHolder.layout.setBackground(context.getDrawable(R.drawable.bg_edittext_focused));
            } else {
                viewHolder.sort.setText(position + 1 + "");
                viewHolder.sort.setBackground(context.getDrawable(R.drawable.bg_edittext));
                viewHolder.codeOfDevice.setText(unit.getCodeOfMonitor());
                viewHolder.typeOfDevice.setText(unit.getTypeOfTheMonitor());
                viewHolder.physicAddress.setText(unit.getPhysicAddress());
                viewHolder.layout.setBackground(context.getDrawable(R.drawable.bg_edittext));
            }

        }
        return convertView;
    }
}
