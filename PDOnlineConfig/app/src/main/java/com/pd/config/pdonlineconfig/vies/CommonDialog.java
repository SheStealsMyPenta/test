package com.pd.config.pdonlineconfig.vies;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import com.pd.config.pdonlineconfig.R;
import com.pd.config.pdonlineconfig.constants.CacheData;

public class CommonDialog extends Dialog implements View.OnClickListener {
    private TextView contentTxt;
    private TextView titleTxt;
    private TextView submitTxt;
    private TextView cancelTxt;
    private TextView textView11;
    private TextView textView12;
    private TextView textView13;
    private LinearLayout addressLayout;
    private Spinner spinner;
    private Context mContext;
    private String content;
    private OnCloseListener listener;
    private String positiveName;
    private String negativeName;
    private String title;
    private TextView codeOfDevice;

    public CommonDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public CommonDialog(Context context, int themeResId, String content) {
        super(context, themeResId);
        this.mContext = context;
        this.content = content;
    }

    public CommonDialog(Context context, int themeResId, String content, OnCloseListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.content = content;
        this.listener = listener;
    }

    protected CommonDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    public CommonDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public CommonDialog setPositiveButton(String name) {
        this.positiveName = name;
        return this;
    }

    public CommonDialog setNegativeButton(String name) {
        this.negativeName = name;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog);
        setCanceledOnTouchOutside(false);
        initView();
    }

    public void changeTheView() {
        textView11.setText("设备名称");
        textView12.setText("设备编号");
        textView13.setText("设备类型");
        String[] mItems = getContext().getResources().getStringArray(R.array.watchedDevice);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.drop_down_item, mItems);
        adapter.setDropDownViewResource(R.layout.drop_down_item);
        spinner.setAdapter(adapter);
    }

    private void initView() {
//        contentTxt = (TextView)findViewById(R.id.content);
        titleTxt = (TextView) findViewById(R.id.title);
        submitTxt = (TextView) findViewById(R.id.submit);
        submitTxt.setOnClickListener(this);
        cancelTxt = (TextView) findViewById(R.id.cancel);
        textView11 = findViewById(R.id.text11);
        textView12 = findViewById(R.id.text12);
        textView13 = findViewById(R.id.text13);
        codeOfDevice = findViewById(R.id.codeOfDevice);
        addressLayout = findViewById(R.id.addressLayout);
        cancelTxt.setOnClickListener(this);
        spinner = findViewById(R.id.typeOfDevice);

        String[] mItems = getContext().getResources().getStringArray(R.array.typeOfDevice);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.drop_down_item, mItems);
        adapter.setDropDownViewResource(R.layout.drop_down_item);

        spinner.setAdapter(adapter);
        if (spinner.getSelectedItem().toString().equals("环境监测")) {
            codeOfDevice.setText(CacheData.currentUnit.getCodeOfDevice());
            addressLayout.setVisibility(View.GONE);
        } else {
            addressLayout.setVisibility(View.VISIBLE);
        }
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinner.getSelectedItem().toString().equals("环境监测")) {
                    codeOfDevice.setText(CacheData.currentUnit.getCodeOfDevice());
                    addressLayout.setVisibility(View.GONE);
                } else if(spinner.getSelectedItem().toString().equals("开关柜监测")) {
                    addressLayout.setVisibility(View.VISIBLE);
                    codeOfDevice.setText("A3");
                }else if(spinner.getSelectedItem().toString().equals("无线测温")){
                    addressLayout.setVisibility(View.VISIBLE);
                    codeOfDevice.setText("A2");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//        contentTxt.setText(content);
        if (!TextUtils.isEmpty(positiveName)) {
            submitTxt.setText(positiveName);
        }

        if (!TextUtils.isEmpty(negativeName)) {
            cancelTxt.setText(negativeName);
        }

        if (!TextUtils.isEmpty(title)) {
            titleTxt.setText(title);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                this.dismiss();
                break;
            case R.id.submit:
                if (listener != null) {
                    listener.onClick(this, true);
                }
                break;
        }
    }

    public interface OnCloseListener {
        void onClick(Dialog dialog, boolean confirm);
    }
}