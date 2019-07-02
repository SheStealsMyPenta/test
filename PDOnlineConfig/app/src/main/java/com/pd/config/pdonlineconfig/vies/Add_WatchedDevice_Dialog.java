package com.pd.config.pdonlineconfig.vies;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import com.pd.config.pdonlineconfig.R;

public class Add_WatchedDevice_Dialog extends Dialog implements View.OnClickListener{
    private TextView contentTxt;
    private TextView titleTxt;
    private TextView submitTxt;
    private TextView cancelTxt;
    private TextView textView11;
    private TextView textView12;
    private TextView textView13;

    private Spinner spinner;
    private Context mContext;
    private String content;
    private OnCloseListener listener;
    private String positiveName;
    private String negativeName;
    private String title;

    public Add_WatchedDevice_Dialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public Add_WatchedDevice_Dialog(Context context, int themeResId, String content) {
        super(context, themeResId);
        this.mContext = context;
        this.content = content;
    }

    public Add_WatchedDevice_Dialog(Context context, int themeResId, String content, OnCloseListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.content = content;
        this.listener = listener;
    }

    protected Add_WatchedDevice_Dialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    public Add_WatchedDevice_Dialog setTitle(String title){
        this.title = title;
        return this;
    }

    public Add_WatchedDevice_Dialog setPositiveButton(String name){
        this.positiveName = name;
        return this;
    }

    public Add_WatchedDevice_Dialog setNegativeButton(String name){
        this.negativeName = name;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_watched_device_dialog);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView(){
//        contentTxt = (TextView)findViewById(R.id.content);
        titleTxt = (TextView)findViewById(R.id.title);
        submitTxt = (TextView)findViewById(R.id.submit);
        submitTxt.setOnClickListener(this);
        cancelTxt = (TextView)findViewById(R.id.cancel);
        textView11 = findViewById(R.id.text11);
        textView12 = findViewById(R.id.text12);
        textView13 = findViewById(R.id.text13);
        cancelTxt.setOnClickListener(this);
        spinner = findViewById(R.id.typeOfWatchedDevice);
        String[] mItems = getContext().getResources().getStringArray(R.array.watchedDevice);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),R.layout.drop_down_item, mItems);
        adapter.setDropDownViewResource(R.layout.drop_down_item);
        spinner.setAdapter(adapter);

//        contentTxt.setText(content);
        if(!TextUtils.isEmpty(positiveName)){
            submitTxt.setText(positiveName);
        }

        if(!TextUtils.isEmpty(negativeName)){
            cancelTxt.setText(negativeName);
        }

        if(!TextUtils.isEmpty(title)){
            titleTxt.setText(title);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel:
                this.dismiss();
                break;
            case R.id.submit:
                if(listener != null){
                    listener.onClick(this, true);
                }
                break;
        }
    }

    public interface OnCloseListener{
        void onClick(Dialog dialog, boolean confirm);
    }
}