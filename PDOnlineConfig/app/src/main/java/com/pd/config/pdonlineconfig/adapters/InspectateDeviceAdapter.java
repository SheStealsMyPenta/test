package com.pd.config.pdonlineconfig.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.pd.config.pdonlineconfig.R;
import com.pd.config.pdonlineconfig.constants.CacheData;
import com.pd.config.pdonlineconfig.pojo.MonitorDevice;
import com.pd.config.pdonlineconfig.pojo.ResultObj;
import com.suke.widget.SwitchButton;

import java.util.ArrayList;
import java.util.List;

public class InspectateDeviceAdapter extends ArrayAdapter {
    public boolean canBeModify = false;
    private Context context;
    private List<ResultObj> listOfControlUnit;
    private int currentPosition = -1;
    private String currentType = "";
    private String currentPDType = "";
    private String currentMonitorDevice = "";
    private int sizeOfList;
    private List<ResultObj> listOfResultObj;
    private boolean isInit = true;

    public List<ResultObj> getResultList() {
        return listOfResultObj;
    }
    @Override
    public int getViewTypeCount() {
        //Count=Size of ArrayList.
        return listOfControlUnit.size();
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public String getCurrentType() {
        return currentType;
    }

    public void setCurrentType(String currentType) {
        this.currentType = currentType;
    }

    public InspectateDeviceAdapter(Context context, int resource, List<ResultObj> list, String currentType) {
        super(context, resource);
        listOfControlUnit = list;
        this.context = context;
        this.currentType = currentType;
        listOfResultObj = list;
//        if (currentType.equals("环境监测装置")) {
//            listOfResultObj = list;
////            listOfResultObj = new ArrayList<>();
////
////            listOfResultObj.add(new ResultObj("空气式超声局放监测", "", "", currentType, false));
////            listOfResultObj.add(new ResultObj("特高频局放监测", "", "", currentType, false));
////            listOfResultObj.add(new ResultObj("红外热像监测", "", "", currentType, false));
////            listOfResultObj.add(new ResultObj("视频图像监测", "", "", currentType, false));
////            listOfResultObj.add(new ResultObj("气体监测", "", "", currentType, false));
//        } else {
////            listOfResultObj = new ArrayList<>();
////            listOfResultObj.add(new ResultObj("暂态地电压局放监测", "", "", currentType, false));
////            listOfResultObj.add(new ResultObj("空气式超声局放监测", "", "", currentType, false));
////            listOfResultObj.add(new ResultObj("特高频局放监测", "", "", currentType, false));
////            listOfResultObj.add(new ResultObj("高频电流局放监测", "", "", currentType, false));
////            listOfResultObj.add(new ResultObj("电缆仓温湿度监测", "", "", currentType, false));
////            listOfResultObj.add(new ResultObj("仪表仓温湿度监测", "", "", currentType, false));
////            listOfResultObj.add(new ResultObj("断路器机械特性监测", "", "", currentType, false));
////            listOfResultObj.add(new ResultObj("开关上触头A相温度监测", "", "", currentType, false));
////            listOfResultObj.add(new ResultObj("开关上触头B相温度监测", "", "", currentType, false));
////            listOfResultObj.add(new ResultObj("开关上触头C相温度监测", "", "", currentType, false));
////            listOfResultObj.add(new ResultObj("开关下触头A相温度监测", "", "", currentType, false));
////            listOfResultObj.add(new ResultObj("开关下触头B相温度监测", "", "", currentType, false));
////            listOfResultObj.add(new ResultObj("开关下触头C相温度监测", "", "", currentType, false));
////            listOfResultObj.add(new ResultObj("电缆连接头A相温度监测", "", "", currentType, false));
////            listOfResultObj.add(new ResultObj("电缆连接头B相温度监测", "", "", currentType, false));
////            listOfResultObj.add(new ResultObj("电缆连接头C相温度监测", "", "", currentType, false));
//
//
//        }

    }

    public List<ResultObj> getListOfControlUnit() {
        return listOfControlUnit;
    }

    public void setListOfControlUnit(List<ResultObj> listOfControlUnit) {
        this.listOfControlUnit = listOfControlUnit;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    private static class ViewHolder {
        TextView nameOfMonitor;
        Spinner monitorDevice;
        Spinner channel;
        EditText channelEdit;
        SwitchButton switchButton;
    }

    @Override
    public int getCount() {
        return listOfControlUnit.size();
    }

    @Override
    public ResultObj getItem(int position) {
        return listOfControlUnit.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        InspectateDeviceAdapter.ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater mInflater = LayoutInflater.from(context);
            convertView = mInflater.inflate(R.layout.inspect_list_adapter, null);
            viewHolder.nameOfMonitor = convertView.findViewById(R.id.nameOfMonitor);
            viewHolder.channel = convertView.findViewById(R.id.channel);
            viewHolder.monitorDevice = convertView.findViewById(R.id.monitorDevice);
            viewHolder.channelEdit = convertView.findViewById(R.id.channelEdit);
            viewHolder.switchButton = convertView.findViewById(R.id.switchBtn);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (InspectateDeviceAdapter.ViewHolder) convertView.getTag();
        }
        ResultObj unit = listOfControlUnit.get(position);

        viewHolder.nameOfMonitor.setText(unit.getTypeOfMonitor());

        List<MonitorDevice> monitorDevice = CacheData.listOfMonitorDevice;
        ArrayList<MonitorDevice> devices = listFilter(monitorDevice, unit.getNameOfMoniorDevice());
        String[] monitorDeviceArr = getStringArrForMonitorDevice(devices);
// 建立Adapter并且绑定数据源
        ArrayAdapter<String> _Adapter = new ArrayAdapter<String>(getContext(), R.layout.drop_down_item, monitorDeviceArr);
//        setAdapterForChannel(viewHolder, unit);
        ViewHolder finalViewHolder = viewHolder;
        ViewHolder finalViewHolder1 = viewHolder;
        ViewHolder finalViewHolder2 = viewHolder;
        ViewHolder finalViewHolder4 = viewHolder;

        if (    unit.getTypeOfMonitor().equals("开关上触头A相温度监测") ||
                unit.getTypeOfMonitor().equals("开关上触头B相温度监测") ||
                unit.getTypeOfMonitor().equals("开关上触头C相温度监测") ||
                unit.getTypeOfMonitor().equals("开关下触头A相温度监测") ||
                unit.getTypeOfMonitor().equals("开关下触头B相温度监测") ||
                unit.getTypeOfMonitor().equals("开关下触头C相温度监测") ||
                unit.getTypeOfMonitor().equals("电缆连接头A相温度监测") ||
                unit.getTypeOfMonitor().equals("电缆连接头B相温度监测") ||
                unit.getTypeOfMonitor().equals("电缆连接头C相温度监测")
          ) {
            //如果是开关柜监测装置则把温度通道变为输入
            viewHolder.channelEdit.setVisibility(View.VISIBLE);
            viewHolder.channel.setVisibility(View.GONE);
            ViewHolder finalViewHolder5 = viewHolder;
            ViewHolder finalViewHolder6 = viewHolder;
            setEditForChannel(finalViewHolder, unit, currentMonitorDevice);
            if (!currentType.equals("环境监测装置")) {
                setState(finalViewHolder, unit, currentMonitorDevice);
            }

            viewHolder.channelEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        switch (finalViewHolder5.nameOfMonitor.getText().toString()) {
                            case "开关上触头A相温度监测":
                                listOfResultObj.get(7).setChannel(finalViewHolder6.channelEdit.getText().toString());
                                break;
                            case "开关上触头B相温度监测":
                                listOfResultObj.get(8).setChannel(finalViewHolder6.channelEdit.getText().toString());
                                break;
                            case "开关上触头C相温度监测":
                                listOfResultObj.get(9).setChannel(finalViewHolder6.channelEdit.getText().toString());
                                break;
                            case "开关下触头A相温度监测":
                                listOfResultObj.get(10).setChannel(finalViewHolder6.channelEdit.getText().toString());
                                break;
                            case "开关下触头B相温度监测":
                                listOfResultObj.get(11).setChannel(finalViewHolder6.channelEdit.getText().toString());
                                break;
                            case "开关下触头C相温度监测":
                                listOfResultObj.get(12).setChannel(finalViewHolder6.channelEdit.getText().toString());
                                break;
                            case "电缆连接头A相温度监测":
                                listOfResultObj.get(13).setChannel(finalViewHolder6.channelEdit.getText().toString());
                                break;
                            case "电缆连接头B相温度监测":
                                listOfResultObj.get(14).setChannel(finalViewHolder6.channelEdit.getText().toString());
                                break;
                            case "电缆连接头C相温度监测":
                                listOfResultObj.get(15).setChannel(finalViewHolder6.channelEdit.getText().toString());
                                break;
                        }
                    }
                }
            });
        } else {
            viewHolder.channelEdit.setVisibility(View.GONE);
            viewHolder.channel.setVisibility(View.VISIBLE);
        }
        viewHolder.switchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (currentType.equals("环境监测装置")) {
                    if (unit.getTypeOfMonitor().equals("特高频局放监测")) {
                        listOfResultObj.get(0).setOpen(isChecked);
                    } else if (unit.getTypeOfMonitor().equals("空气式超声局放监测")) {
                        listOfResultObj.get(1).setOpen(isChecked);
                    } else if (unit.getTypeOfMonitor().equals("红外热像监测")) {
                        listOfResultObj.get(2).setOpen(isChecked);
                    } else if (unit.getTypeOfMonitor().equals("视频图像监测")) {
                        listOfResultObj.get(3).setOpen(isChecked);
                    } else if (unit.getTypeOfMonitor().equals("气体监测")) {
                        listOfResultObj.get(4).setOpen(isChecked);
                    } else if (unit.getTypeOfMonitor().equals("环境温湿度监测")){
                        listOfResultObj.get(5).setOpen(isChecked);
                    }
                } else {
                    if (unit.getTypeOfMonitor().equals("暂态地电压局放监测")) {
                        listOfResultObj.get(0).setOpen(isChecked);
                    } else if (unit.getTypeOfMonitor().equals("空气式超声局放监测")) {
                        listOfResultObj.get(1).setOpen(isChecked);
                    } else if (unit.getTypeOfMonitor().equals("特高频局放监测")) {
                        listOfResultObj.get(2).setOpen(isChecked);
                    } else if (unit.getTypeOfMonitor().equals("高频电流局放监测")) {
                        listOfResultObj.get(3).setOpen(isChecked);
                    } else if (unit.getTypeOfMonitor().equals("电缆仓温湿度监测")) {
                        listOfResultObj.get(4).setOpen(isChecked);
                    } else if (unit.getTypeOfMonitor().equals("仪表仓温湿度监测")) {
                        listOfResultObj.get(5).setOpen(isChecked);
                    } else if (unit.getTypeOfMonitor().equals("断路器机械特性监测")) {
                        listOfResultObj.get(6).setOpen(isChecked);
                    } else if (unit.getTypeOfMonitor().equals("开关上触头A相温度监测")) {
                        listOfResultObj.get(7).setOpen(isChecked);
                    } else if (unit.getTypeOfMonitor().equals("开关上触头B相温度监测")) {
                        listOfResultObj.get(8).setOpen(isChecked);
                    } else if (unit.getTypeOfMonitor().equals("开关上触头C相温度监测")) {
                        listOfResultObj.get(9).setOpen(isChecked);
                    } else if (unit.getTypeOfMonitor().equals("开关下触头A相温度监测")) {
                        listOfResultObj.get(10).setOpen(isChecked);
                    } else if (unit.getTypeOfMonitor().equals("开关下触头B相温度监测")) {
                        listOfResultObj.get(11).setOpen(isChecked);
                    } else if (unit.getTypeOfMonitor().equals("开关下触头C相温度监测")) {
                        listOfResultObj.get(12).setOpen(isChecked);
                    } else if (unit.getTypeOfMonitor().equals("电缆连接头A相温度监测")) {
                        listOfResultObj.get(13).setOpen(isChecked);
                    } else if (unit.getTypeOfMonitor().equals("电缆连接头B相温度监测")) {
                        listOfResultObj.get(14).setOpen(isChecked);
                    } else if (unit.getTypeOfMonitor().equals("电缆连接头C相温度监测")) {
                        listOfResultObj.get(15).setOpen(isChecked);
                    }
                }
            }
        });
        viewHolder.monitorDevice.setAdapter(_Adapter);
        ViewHolder finalViewHolder3 = viewHolder;
        ViewHolder finalViewHolder7 = viewHolder;
        ViewHolder finalViewHolder8 = viewHolder;
        viewHolder.monitorDevice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentMonitorDevice = finalViewHolder2.monitorDevice.getItemAtPosition(position).toString();
                if (currentType.equals("环境监测装置")) {
                    if (unit.getTypeOfMonitor().equals("特高频局放监测")) {
                        listOfResultObj.get(0).setNameOfMoniorDevice(currentMonitorDevice);
                        listOfResultObj.get(0).setCurrentMonitorDevicePosition(position);
                    } else if (unit.getTypeOfMonitor().equals("空气式超声局放监测")) {
                        listOfResultObj.get(1).setNameOfMoniorDevice(currentMonitorDevice);
                        listOfResultObj.get(1).setCurrentMonitorDevicePosition(position);
                    } else if (unit.getTypeOfMonitor().equals("红外热像监测")) {
                        listOfResultObj.get(2).setNameOfMoniorDevice(currentMonitorDevice);
                        listOfResultObj.get(2).setCurrentMonitorDevicePosition(position);
                    } else if (unit.getTypeOfMonitor().equals("视频图像监测")) {
                        listOfResultObj.get(3).setNameOfMoniorDevice(currentMonitorDevice);
                        listOfResultObj.get(3).setCurrentMonitorDevicePosition(position);
                    } else if (unit.getTypeOfMonitor().equals("气体监测")) {
                        listOfResultObj.get(4).setNameOfMoniorDevice(currentMonitorDevice);
                        listOfResultObj.get(4).setCurrentMonitorDevicePosition(position);
                    } else if(unit.getTypeOfMonitor().equals("环境温湿度监测")){
                        listOfResultObj.get(5).setNameOfMoniorDevice(currentMonitorDevice);
                        listOfResultObj.get(5).setCurrentMonitorDevicePosition(position);
                    }
                } else {
                    if (unit.getTypeOfMonitor().equals("暂态地电压局放监测")) {
                        listOfResultObj.get(0).setNameOfMoniorDevice(currentMonitorDevice);
                        listOfResultObj.get(0).setCurrentMonitorDevicePosition(position);
                    } else if (unit.getTypeOfMonitor().equals("空气式超声局放监测")) {
                        listOfResultObj.get(1).setNameOfMoniorDevice(currentMonitorDevice);
                        listOfResultObj.get(1).setCurrentMonitorDevicePosition(position);
                    } else if (unit.getTypeOfMonitor().equals("特高频局放监测")) {
                        listOfResultObj.get(2).setNameOfMoniorDevice(currentMonitorDevice);
                        listOfResultObj.get(2).setCurrentMonitorDevicePosition(position);
                    } else if (unit.getTypeOfMonitor().equals("高频电流局放监测")) {
                        listOfResultObj.get(3).setNameOfMoniorDevice(currentMonitorDevice);
                        listOfResultObj.get(3).setCurrentMonitorDevicePosition(position);
                    } else if (unit.getTypeOfMonitor().equals("电缆仓温湿度监测")) {
                        listOfResultObj.get(4).setNameOfMoniorDevice(currentMonitorDevice);
                        listOfResultObj.get(4).setCurrentMonitorDevicePosition(position);
                    } else if (unit.getTypeOfMonitor().equals("仪表仓温湿度监测")) {
                        listOfResultObj.get(5).setNameOfMoniorDevice(currentMonitorDevice);
                        listOfResultObj.get(5).setCurrentMonitorDevicePosition(position);
                    } else if (unit.getTypeOfMonitor().equals("断路器机械特性监测")) {
                        listOfResultObj.get(6).setNameOfMoniorDevice(currentMonitorDevice);
                        listOfResultObj.get(6).setCurrentMonitorDevicePosition(position);
                    } else if (unit.getTypeOfMonitor().equals("开关上触头A相温度监测")) {
                        listOfResultObj.get(7).setNameOfMoniorDevice(currentMonitorDevice);
                        listOfResultObj.get(7).setCurrentMonitorDevicePosition(position);
                    } else if (unit.getTypeOfMonitor().equals("开关上触头B相温度监测")) {
                        listOfResultObj.get(8).setNameOfMoniorDevice(currentMonitorDevice);
                        listOfResultObj.get(8).setCurrentMonitorDevicePosition(position);
                    } else if (unit.getTypeOfMonitor().equals("开关上触头C相温度监测")) {
                        listOfResultObj.get(9).setNameOfMoniorDevice(currentMonitorDevice);
                        listOfResultObj.get(9).setCurrentMonitorDevicePosition(position);
                    } else if (unit.getTypeOfMonitor().equals("开关下触头A相温度监测")) {
                        listOfResultObj.get(10).setNameOfMoniorDevice(currentMonitorDevice);
                        listOfResultObj.get(10).setCurrentMonitorDevicePosition(position);
                    } else if (unit.getTypeOfMonitor().equals("开关下触头B相温度监测")) {
                        listOfResultObj.get(11).setNameOfMoniorDevice(currentMonitorDevice);
                        listOfResultObj.get(11).setCurrentMonitorDevicePosition(position);
                    } else if (unit.getTypeOfMonitor().equals("开关下触头C相温度监测")) {
                        listOfResultObj.get(12).setNameOfMoniorDevice(currentMonitorDevice);
                        listOfResultObj.get(12).setCurrentMonitorDevicePosition(position);
                    } else if (unit.getTypeOfMonitor().equals("电缆连接头A相温度监测")) {
                        listOfResultObj.get(13).setNameOfMoniorDevice(currentMonitorDevice);
                        listOfResultObj.get(13).setCurrentMonitorDevicePosition(position);
                    } else if (unit.getTypeOfMonitor().equals("电缆连接头B相温度监测")) {
                        listOfResultObj.get(14).setNameOfMoniorDevice(currentMonitorDevice);
                        listOfResultObj.get(14).setCurrentMonitorDevicePosition(position);
                    } else if (unit.getTypeOfMonitor().equals("电缆连接头C相温度监测")) {
                        listOfResultObj.get(15).setNameOfMoniorDevice(currentMonitorDevice);
                        listOfResultObj.get(15).setCurrentMonitorDevicePosition(position);
                    }
                }

                if (!currentMonitorDevice.equals("")) {
                    if (finalViewHolder7.channelEdit.getVisibility() == View.GONE) {
                        setAdapterForChannel(finalViewHolder, unit, currentMonitorDevice);
                    } else {


                    }

                } else {
                    String[] channelArr = new String[1];
                    ArrayAdapter adapterForChannel = new ArrayAdapter<String>(getContext(), R.layout.drop_down_item
                            , channelArr);
                    finalViewHolder3.channel.setAdapter(adapterForChannel);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        viewHolder.monitorDevice.setSelection(sizeOfList);


        if (currentType.equals("环境监测装置")) {
            switch (unit.getTypeOfMonitor()) {
                case "特高频局放监测":
                    if (!listOfResultObj.get(0).getNameOfMoniorDevice().equals("")) {
                        viewHolder.monitorDevice.setSelection(listOfResultObj.get(0).getCurrentMonitorDevicePosition());
                        viewHolder.channel.setSelection(listOfResultObj.get(0).getCurrentChannelPosition());
                    }
                    break;
                case "空气式超声局放监测":
                    if (!listOfResultObj.get(1).getNameOfMoniorDevice().equals("")) {
                        viewHolder.monitorDevice.setSelection(listOfResultObj.get(1).getCurrentMonitorDevicePosition());
                        viewHolder.channel.setSelection(listOfResultObj.get(1).getCurrentChannelPosition());
                    }
                    break;
                case "红外热像监测":
                    if (!listOfResultObj.get(2).getNameOfMoniorDevice().equals("")) {
                        viewHolder.monitorDevice.setSelection(listOfResultObj.get(2).getCurrentMonitorDevicePosition());
                        viewHolder.channel.setSelection(listOfResultObj.get(2).getCurrentChannelPosition());
                    }
                    break;
                case "视频图像监测":
                    if (!listOfResultObj.get(3).getNameOfMoniorDevice().equals("")) {
                        viewHolder.monitorDevice.setSelection(listOfResultObj.get(3).getCurrentMonitorDevicePosition());
                        viewHolder.channel.setSelection(listOfResultObj.get(3).getCurrentChannelPosition());
                    }
                    break;
                case "气体监测":
                    if (!listOfResultObj.get(4).getNameOfMoniorDevice().equals("")) {
                        viewHolder.monitorDevice.setSelection(listOfResultObj.get(4).getCurrentMonitorDevicePosition());
                        viewHolder.channel.setSelection(listOfResultObj.get(4).getCurrentChannelPosition());
                    }
                    break;
                case "环境温湿度监测":
                    if (!listOfResultObj.get(5).getNameOfMoniorDevice().equals("")) {
                        viewHolder.monitorDevice.setSelection(listOfResultObj.get(5).getCurrentMonitorDevicePosition());
                        viewHolder.channel.setSelection(listOfResultObj.get(5).getCurrentChannelPosition());
                    }
                    break;
            }
        } else {
            switch (unit.getTypeOfMonitor()) {
                case "暂态地电压局放监测":
                    if (!listOfResultObj.get(0).getNameOfMoniorDevice().equals("")) {
                        viewHolder.monitorDevice.setSelection(listOfResultObj.get(0).getCurrentMonitorDevicePosition());

                    }
                    break;
                case "空气式超声局放监测":
                    if (!listOfResultObj.get(1).getNameOfMoniorDevice().equals("")) {
                        viewHolder.monitorDevice.setSelection(listOfResultObj.get(1).getCurrentMonitorDevicePosition());

                    }
                    break;
                case "特高频局放监测":
                    if (!listOfResultObj.get(2).getNameOfMoniorDevice().equals("")) {
                        viewHolder.monitorDevice.setSelection(listOfResultObj.get(2).getCurrentMonitorDevicePosition());

                    }
                    break;
                case "高频电流局放监测":
                    if (!listOfResultObj.get(3).getNameOfMoniorDevice().equals("")) {
                        viewHolder.monitorDevice.setSelection(listOfResultObj.get(3).getCurrentMonitorDevicePosition());

                    }
                    break;
                case "电缆仓温湿度监测":
                    if (!listOfResultObj.get(4).getNameOfMoniorDevice().equals("")) {
                        viewHolder.monitorDevice.setSelection(listOfResultObj.get(4).getCurrentMonitorDevicePosition());

                    }
                    break;
                case "仪表仓温湿度监测":
                    if (!listOfResultObj.get(5).getNameOfMoniorDevice().equals("")) {
                        viewHolder.monitorDevice.setSelection(listOfResultObj.get(5).getCurrentMonitorDevicePosition());

                    }
                    break;
                case "断路器机械特性监测":
                    if (!listOfResultObj.get(6).getNameOfMoniorDevice().equals("")) {
                        viewHolder.monitorDevice.setSelection(listOfResultObj.get(6).getCurrentMonitorDevicePosition());
                    }
                    break;
                case "开关上触头A相温度监测":
                    if (!listOfResultObj.get(7).getNameOfMoniorDevice().equals("")) {
                        viewHolder.monitorDevice.setSelection(listOfResultObj.get(7).getCurrentMonitorDevicePosition());
                    }
                    break;
                case "开关上触头B相温度监测":
                    if (!listOfResultObj.get(8).getNameOfMoniorDevice().equals("")) {
                        viewHolder.monitorDevice.setSelection(listOfResultObj.get(8).getCurrentMonitorDevicePosition());
                    }
                    break;
                case "开关上触头C相温度监测":
                    if (!listOfResultObj.get(9).getNameOfMoniorDevice().equals("")) {
                        viewHolder.monitorDevice.setSelection(listOfResultObj.get(9).getCurrentMonitorDevicePosition());
                    }
                    break;
                case "开关下触头A相温度监测":
                    if (!listOfResultObj.get(10).getNameOfMoniorDevice().equals("")) {
                        viewHolder.monitorDevice.setSelection(listOfResultObj.get(10).getCurrentMonitorDevicePosition());
                    }
                    break;
                case "开关下触头B相温度监测":
                    if (!listOfResultObj.get(11).getNameOfMoniorDevice().equals("")) {
                        viewHolder.monitorDevice.setSelection(listOfResultObj.get(11).getCurrentMonitorDevicePosition());
                    }
                    break;
                case "开关下触头C相温度监测":
                    if (!listOfResultObj.get(12).getNameOfMoniorDevice().equals("")) {
                        viewHolder.monitorDevice.setSelection(listOfResultObj.get(12).getCurrentMonitorDevicePosition());
                    }
                    break;
                case "电缆连接头A相温度监测":
                    if (!listOfResultObj.get(13).getNameOfMoniorDevice().equals("")) {
                        viewHolder.monitorDevice.setSelection(listOfResultObj.get(13).getCurrentMonitorDevicePosition());
                    }
                    ;
                    break;
                case "电缆连接头B相温度监测":
                    if (!listOfResultObj.get(14).getNameOfMoniorDevice().equals("")) {
                        viewHolder.monitorDevice.setSelection(listOfResultObj.get(14).getCurrentMonitorDevicePosition());
                    }
                    break;
                case "电缆连接头C相温度监测":
                    if (!listOfResultObj.get(15).getNameOfMoniorDevice().equals("")) {
                        viewHolder.monitorDevice.setSelection(listOfResultObj.get(15).getCurrentMonitorDevicePosition());
                    }
                    break;
            }
        }


//        String[] channelArr = new String[1];
//        ArrayAdapter adapterForChannel = new ArrayAdapter<String>(getContext(), R.layout.drop_down_item
//                , channelArr);
//        finalViewHolder3.channel.setAdapter(adapterForChannel);

        updateAllState();
        return convertView;
    }

    private void setState(ViewHolder viewHolder, ResultObj unit, String currentMonitorDevice) {
//        viewHolder.switchButton.setChecked(false);
        switch (unit.getTypeOfMonitor()) {

            case "暂态地电压局放监测":
                if (listOfResultObj.get(0).isOpen()) {
                    viewHolder.switchButton.setChecked(true);
                } else {
                    viewHolder.switchButton.setChecked(false);
                }
                break;
            case "空气式超声局放监测":
                if (listOfResultObj.get(1).isOpen()) {
                    viewHolder.switchButton.setChecked(true);
                } else {
                    viewHolder.switchButton.setChecked(false);
                }
                break;
            case "特高频局放监测":
                if (listOfResultObj.get(2).isOpen()) {
                    viewHolder.switchButton.setChecked(true);
                } else {
                    viewHolder.switchButton.setChecked(false);
                }
                break;
            case "环境温湿度监测":{
                if (listOfResultObj.get(5).isOpen()) {
                    viewHolder.switchButton.setChecked(true);
                } else {
                    viewHolder.switchButton.setChecked(false);
                }
                break;
            }
            case "高频电流局放监测":
                if (listOfResultObj.get(3).isOpen()) {
                    viewHolder.switchButton.setChecked(true);
                } else {
                    viewHolder.switchButton.setChecked(false);
                }
                break;
            case "电缆仓温湿度监测":
                if (listOfResultObj.get(4).isOpen()) {
                    viewHolder.switchButton.setChecked(true);
                } else {
                    viewHolder.switchButton.setChecked(false);
                }
                break;
            case "仪表仓温湿度监测":
                if (listOfResultObj.get(5).isOpen()) {
                    viewHolder.switchButton.setChecked(true);
                } else {
                    viewHolder.switchButton.setChecked(false);
                }
                break;
            case "断路器机械特性监测":
                if (listOfResultObj.get(6).isOpen()) {
                    viewHolder.switchButton.setChecked(true);
                } else {
                    viewHolder.switchButton.setChecked(false);
                }
                break;
            case "开关上触头A相温度监测":
                if (listOfResultObj.get(7).isOpen()) {
                    viewHolder.switchButton.setChecked(true);
                } else {
                    viewHolder.switchButton.setChecked(false);
                }
                break;
            case "开关上触头B相温度监测":
                if (listOfResultObj.get(8).isOpen()) {
                    viewHolder.switchButton.setChecked(true);
                } else {
                    viewHolder.switchButton.setChecked(false);
                }
                break;
            case "开关上触头C相温度监测":
                if (listOfResultObj.get(9).isOpen()) {
                    viewHolder.switchButton.setChecked(true);
                } else {
                    viewHolder.switchButton.setChecked(false);
                }
                break;
            case "开关下触头A相温度监测":
                if (listOfResultObj.get(10).isOpen()) {
                    viewHolder.switchButton.setChecked(true);
                } else {
                    viewHolder.switchButton.setChecked(false);
                }
                break;
            case "开关下触头B相温度监测":
                if (listOfResultObj.get(11).isOpen()) {
                    viewHolder.switchButton.setChecked(true);
                } else {
                    viewHolder.switchButton.setChecked(false);
                }
                break;
            case "开关下触头C相温度监测":
                if (listOfResultObj.get(12).isOpen()) {
                    viewHolder.switchButton.setChecked(true);
                } else {
                    viewHolder.switchButton.setChecked(false);
                }
                break;
            case "电缆连接头A相温度监测":
                if (listOfResultObj.get(13).isOpen()) {
                    viewHolder.switchButton.setChecked(true);
                } else {
                    viewHolder.switchButton.setChecked(false);
                }

                break;
            case "电缆连接头B相温度监测":
                if (listOfResultObj.get(14).isOpen()) {
                    viewHolder.switchButton.setChecked(true);
                } else {
                    viewHolder.switchButton.setChecked(false);
                }
                break;
            case "电缆连接头C相温度监测":
                if (listOfResultObj.get(15).isOpen()) {
                    viewHolder.switchButton.setChecked(true);
                } else {
                    viewHolder.switchButton.setChecked(false);
                }
                break;

        }
    }

    private void setEditForChannel(ViewHolder viewHolder, ResultObj unit, String currentMonitorDevice) {
        switch (unit.getTypeOfMonitor()) {

            case "暂态地电压局放监测":
                viewHolder.channelEdit.setText(listOfResultObj.get(0).getChannel());
                break;
            case "空气式超声局放监测":
                viewHolder.channelEdit.setText(listOfResultObj.get(1).getChannel());
                break;
            case "特高频局放监测":
                viewHolder.channelEdit.setText(listOfResultObj.get(2).getChannel());
                break;
            case "高频电流局放监测":
                viewHolder.channelEdit.setText(listOfResultObj.get(3).getChannel());
                break;
            case "电缆仓温湿度监测":
                viewHolder.channelEdit.setText(listOfResultObj.get(4).getChannel());
                break;
            case "仪表仓温湿度监测":
                viewHolder.channelEdit.setText(listOfResultObj.get(5).getChannel());
                break;
            case "断路器机械特性监测":
                viewHolder.channelEdit.setText(listOfResultObj.get(6).getChannel());
                break;
            case "开关上触头A相温度监测":
                viewHolder.channelEdit.setText(listOfResultObj.get(7).getChannel());
                break;
            case "开关上触头B相温度监测":
                viewHolder.channelEdit.setText(listOfResultObj.get(8).getChannel());
                break;
            case "开关上触头C相温度监测":
                viewHolder.channelEdit.setText(listOfResultObj.get(9).getChannel());
                break;
            case "开关下触头A相温度监测":
                viewHolder.channelEdit.setText(listOfResultObj.get(10).getChannel());
                break;
            case "开关下触头B相温度监测":
                viewHolder.channelEdit.setText(listOfResultObj.get(11).getChannel());
                break;
            case "开关下触头C相温度监测":
                viewHolder.channelEdit.setText(listOfResultObj.get(12).getChannel());
                break;
            case "电缆连接头A相温度监测":
                viewHolder.channelEdit.setText(listOfResultObj.get(13).getChannel());

                break;
            case "电缆连接头B相温度监测":
                viewHolder.channelEdit.setText(listOfResultObj.get(14).getChannel());
                break;
            case "电缆连接头C相温度监测":
                viewHolder.channelEdit.setText(listOfResultObj.get(15).getChannel());
                break;
        }

    }

    private void updateAllState() {

    }

    private void setAdapterForChannel(ViewHolder viewHolder, ResultObj unit, String currentMonitorDevice) {
        String[] channelArr;
        ArrayAdapter adapterForChannel;
        channelArr = getChannelArr(currentMonitorDevice, unit.getTypeOfMonitor());
        adapterForChannel = new ArrayAdapter<String>(getContext(), R.layout.drop_down_item
                , channelArr);
        viewHolder.channel.setAdapter(adapterForChannel);
        viewHolder.channel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //通道被选择的监听
                if (currentType.equals("环境监测装置")) {

                    switch (unit.getTypeOfMonitor()) {
                        case "特高频局放监测":
                            listOfResultObj.get(0).setChannel((String) viewHolder.channel.getItemAtPosition(position));
                            listOfResultObj.get(0).setCurrentChannelPosition(position);
                            break;
                        case "空气式超声局放监测":
                            listOfResultObj.get(1).setChannel((String) viewHolder.channel.getItemAtPosition(position));
                            listOfResultObj.get(1).setCurrentChannelPosition(position);
                            break;
                        case "红外热像监测":
                            listOfResultObj.get(2).setChannel((String) viewHolder.channel.getItemAtPosition(position));
                            listOfResultObj.get(2).setCurrentChannelPosition(position);
                            break;
                        case "视频图像监测":
                            listOfResultObj.get(3).setChannel((String) viewHolder.channel.getItemAtPosition(position));
                            listOfResultObj.get(3).setCurrentChannelPosition(position);
                            break;
                        case "气体监测":
                            listOfResultObj.get(4).setChannel((String) viewHolder.channel.getItemAtPosition(position));
                            listOfResultObj.get(4).setCurrentChannelPosition(position);
                            break;
                        case "环境温湿度监测":
                            listOfResultObj.get(5).setChannel((String) viewHolder.channel.getItemAtPosition(position));
                            listOfResultObj.get(5).setCurrentChannelPosition(position);
                            break;
                    }
                } else {
                    switch (unit.getTypeOfMonitor()) {
                        case "暂态地电压局放监测":
                            if ((String) viewHolder.channel.getItemAtPosition(position) != null) {
                                listOfResultObj.get(0).setChannel((String) viewHolder.channel.getItemAtPosition(position));
                                listOfResultObj.get(0).setCurrentChannelPosition(position);
                            }

                            break;
                        case "空气式超声局放监测":
                            if (viewHolder.channel.getItemAtPosition(position) != null) {
                                listOfResultObj.get(1).setChannel((String) viewHolder.channel.getItemAtPosition(position));
                                listOfResultObj.get(1).setCurrentChannelPosition(position);
                            }

                            break;
                        case "特高频局放监测":
                            if (viewHolder.channel.getItemAtPosition(position) != null) {
                                listOfResultObj.get(2).setChannel((String) viewHolder.channel.getItemAtPosition(position));
                                listOfResultObj.get(2).setCurrentChannelPosition(position);
                            }

                            break;
                        case "高频电流局放监测":
                            if (viewHolder.channel.getItemAtPosition(position) != null) {
                                listOfResultObj.get(3).setChannel((String) viewHolder.channel.getItemAtPosition(position));
                                listOfResultObj.get(3).setCurrentChannelPosition(position);
                            }

                            break;
                        case "电缆仓温湿度监测":
                            if (viewHolder.channel.getItemAtPosition(position) != null) {
                                listOfResultObj.get(4).setChannel((String) viewHolder.channel.getItemAtPosition(position));
                                listOfResultObj.get(4).setCurrentChannelPosition(position);
                            }
                            break;
                        case "仪表仓温湿度监测":
                            if (viewHolder.channel.getItemAtPosition(position) != null) {
                                listOfResultObj.get(5).setChannel((String) viewHolder.channel.getItemAtPosition(position));
                                listOfResultObj.get(5).setCurrentChannelPosition(position);
                            }
                            break;
                        case "断路器机械特性监测":
                            if (viewHolder.channel.getItemAtPosition(position) != null) {
                                listOfResultObj.get(6).setChannel((String) viewHolder.channel.getItemAtPosition(position));
                                listOfResultObj.get(6).setCurrentChannelPosition(position);
                            }
                            break;


                    }
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if (currentType.equals("环境监测装置")) {
            switch (unit.getTypeOfMonitor()) {
                case "特高频局放监测":
                    if (!listOfResultObj.get(0).getNameOfMoniorDevice().equals("")) {

                        viewHolder.channel.setSelection(listOfResultObj.get(0).getCurrentChannelPosition());
                    }
                    break;
                case "空气式超声局放监测":
                    if (!listOfResultObj.get(1).getNameOfMoniorDevice().equals("")) {

                        viewHolder.channel.setSelection(listOfResultObj.get(1).getCurrentChannelPosition());
                    }
                    break;
                case "红外热像监测":
                    if (!listOfResultObj.get(2).getNameOfMoniorDevice().equals("")) {

                        viewHolder.channel.setSelection(listOfResultObj.get(2).getCurrentChannelPosition());
                    }
                    break;
                case "视频图像监测":
                    if (!listOfResultObj.get(3).getNameOfMoniorDevice().equals("")) {
                        viewHolder.channel.setSelection(listOfResultObj.get(3).getCurrentChannelPosition());
                    }
                    break;
                case "气体监测":
                    if (!listOfResultObj.get(4).getNameOfMoniorDevice().equals("")) {

                        viewHolder.channel.setSelection(listOfResultObj.get(4).getCurrentChannelPosition());
                    }
                    break;
                case "环境温湿度监测":
                    if (!listOfResultObj.get(4).getNameOfMoniorDevice().equals("")) {

                        viewHolder.channel.setSelection(listOfResultObj.get(5).getCurrentChannelPosition());
                    }
                    break;
            }
        } else {
            switch (unit.getTypeOfMonitor()) {
                case "暂态地电压局放监测":
                    if (!listOfResultObj.get(0).getNameOfMoniorDevice().equals("")) {
                        viewHolder.channel.setSelection(listOfResultObj.get(0).getCurrentChannelPosition());

                    }
                    break;
                case "空气式超声局放监测":
                    if (!listOfResultObj.get(1).getNameOfMoniorDevice().equals("")) {
                        viewHolder.channel.setSelection(listOfResultObj.get(1).getCurrentChannelPosition());

                    }
                    break;
                case "特高频局放监测":
                    if (!listOfResultObj.get(2).getNameOfMoniorDevice().equals("")) {
                        viewHolder.channel.setSelection(listOfResultObj.get(2).getCurrentChannelPosition());

                    }
                    break;
                case "高频电流局放监测":
                    if (!listOfResultObj.get(3).getNameOfMoniorDevice().equals("")) {
                        viewHolder.channel.setSelection(listOfResultObj.get(3).getCurrentChannelPosition());

                    }
                    break;
                case "电缆仓温湿度监测":
                    if (!listOfResultObj.get(4).getNameOfMoniorDevice().equals("")) {
                        viewHolder.channel.setSelection(listOfResultObj.get(4).getCurrentChannelPosition());

                    }
                    break;
                case "仪表仓温湿度监测":
                    if (!listOfResultObj.get(5).getNameOfMoniorDevice().equals("")) {
                        viewHolder.channel.setSelection(listOfResultObj.get(5).getCurrentChannelPosition());
                    }
                    break;
                case "断路器机械特性监测":
                    if (!listOfResultObj.get(6).getNameOfMoniorDevice().equals("")) {
                        viewHolder.channel.setSelection(listOfResultObj.get(6).getCurrentChannelPosition());
                    }
                    break;
                case "开关上触头A相温度监测":
                    if (!listOfResultObj.get(7).getNameOfMoniorDevice().equals("")) {
                        viewHolder.channel.setSelection(listOfResultObj.get(7).getCurrentChannelPosition());
                    }
                    break;
                case "开关上触头B相温度监测":
                    if (!listOfResultObj.get(8).getNameOfMoniorDevice().equals("")) {
                        viewHolder.channel.setSelection(listOfResultObj.get(8).getCurrentChannelPosition());
                    }
                    break;
                case "开关上触头C相温度监测":
                    if (!listOfResultObj.get(9).getNameOfMoniorDevice().equals("")) {
                        viewHolder.channel.setSelection(listOfResultObj.get(9).getCurrentChannelPosition());
                    }
                    break;
                case "开关下触头A相温度监测":
                    if (!listOfResultObj.get(10).getNameOfMoniorDevice().equals("")) {
                        viewHolder.channel.setSelection(listOfResultObj.get(10).getCurrentChannelPosition());
                    }
                    break;
                case "开关下触头B相温度监测":
                    if (!listOfResultObj.get(11).getNameOfMoniorDevice().equals("")) {
                        viewHolder.channel.setSelection(listOfResultObj.get(11).getCurrentChannelPosition());
                    }
                    break;
                case "开关下触头C相温度监测":
                    if (!listOfResultObj.get(12).getNameOfMoniorDevice().equals("")) {
                        viewHolder.channel.setSelection(listOfResultObj.get(12).getCurrentChannelPosition());
                    }
                    break;
                case "电缆连接相A相温度监测":
                    if (!listOfResultObj.get(13).getNameOfMoniorDevice().equals("")) {
                        viewHolder.channel.setSelection(listOfResultObj.get(13).getCurrentChannelPosition());
                    }
                    break;
                case "电缆连接头B相温度监测":
                    if (!listOfResultObj.get(14).getNameOfMoniorDevice().equals("")) {
                        viewHolder.channel.setSelection(listOfResultObj.get(14).getCurrentChannelPosition());
                    }
                    break;
                case "电缆连接头C相温度监测":
                    if (!listOfResultObj.get(15).getNameOfMoniorDevice().equals("")) {
                        viewHolder.channel.setSelection(listOfResultObj.get(15).getCurrentChannelPosition());
                    }
                    break;


            }
        }


    }


    private String[] getChannelArr(String monitorDevice, String currentMonitorDevice) {
        String[] result = new String[0];
        String substring = monitorDevice.substring(0, 2);

        if (substring.equals("A0")) {
            result = new String[1];
            switch (currentMonitorDevice) {
                case "特高频局放监测":
                    result[0] = "UHF";
                    break;
                case "空气式超声局放监测":
                    result[0] = "AE";
                    break;

                case "红外热像监测":
                    result[0] = "INFRARED";

                    break;
                case "视频图像监测":
                    result[0] = "VIDEO";
                    break;
                case "气体监测":
                    result[0] = "GAS";
                    break;
                case "环境温湿度监测":
                    result[0] = "HUM/TEMP";
                    break;
                case "开关上触头A相温度监测":
//                    viewHolder.channelEdit.setVisibility(View.VISIBLE);
//                    viewHolder.channel.setVisibility(View.GONE);
                    break;
            }
        } else if (substring.equals("A3")) {
            switch (currentMonitorDevice) {
                case "特高频局放监测":
                    result = new String[1];
                    result[0] = "UHF";
                    break;
                case "空气式超声局放监测":
                    result = new String[1];
                    result[0] = "AE";
                    break;

                case "暂态地电压局放监测":
                    result = new String[1];
                    result[0] = "TEV";

                    break;
                case "高频电流局放监测":
                    result = new String[1];
                    result[0] = "HF";
                    break;
                case "断路器机械特性监测":
                    result = new String[1];
                    result[0] = "mechanicalAttr";
                    break;
                case "电缆仓温湿度监测":
                    result = new String[2];
                    result[0] = "温湿度1";
                    result[1] = "温湿度2";
                    break;
                case "仪表仓温湿度监测":
                    result = new String[2];
                    result[0] = "温湿度1";
                    result[1] = "温湿度2";
//                    viewHolder.channelEdit.setVisibility(View.VISIBLE);
//                    viewHolder.channel.setVisibility(View.GONE);
                    break;
            }
        } else if (substring.equals("A2")) {


        }
        return result;
    }

    private String[] getStringArrForMonitorDevice(ArrayList<MonitorDevice> devices) {
        sizeOfList = devices.size();
        String[] result = new String[devices.size() + 1];
        result[devices.size()] = "";
        for (int i = 0; i < devices.size(); i++) {
            result[i] = devices.get(i).getCodeOfMonitor();
        }
        return result;

    }

    private ArrayList<MonitorDevice> listFilter(List<MonitorDevice> monitorDevice, String nameOfTheMonitor) {
        ArrayList<MonitorDevice> list = new ArrayList<>();
        if (currentType.equals("环境监测装置")) {
            for (int i = 0; i < monitorDevice.size(); i++) {
                if (monitorDevice.get(i).getTypeOfTheMonitor().equals(currentType)) {
                    list.add(monitorDevice.get(i));
                }
            }
        } else if (currentType.equals("开关柜监测装置")) {
            if     (nameOfTheMonitor.equals("暂态地电压局放监测") ||
                    nameOfTheMonitor.equals("空气式超声局放监测") ||
                    nameOfTheMonitor.equals("特高频局放监测") ||
                    nameOfTheMonitor.equals("高频电流局放监测") ||
                    nameOfTheMonitor.equals("电缆仓温湿度监测") ||
                    nameOfTheMonitor.equals("仪表仓温湿度监测") ||
                    nameOfTheMonitor.equals("断路器机械特性监测")
            ) {
                for (int i = 0; i < monitorDevice.size(); i++) {
                    if (monitorDevice.get(i).getTypeOfTheMonitor().equals(currentType)) {
                        list.add(monitorDevice.get(i));
                    }
                }
            } else {
                //如果是触头测温的
                for (int i = 0; i < monitorDevice.size(); i++) {
                    if (monitorDevice.get(i).getTypeOfTheMonitor().equals("无线测温装置")) {
                        list.add(monitorDevice.get(i));
                    }
                }
            }

        }

        return list;
    }
}
