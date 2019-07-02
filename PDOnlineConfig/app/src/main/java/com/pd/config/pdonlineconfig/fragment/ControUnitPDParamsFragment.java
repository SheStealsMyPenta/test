package com.pd.config.pdonlineconfig.fragment;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import com.pd.config.pdonlineconfig.R;
import com.pd.config.pdonlineconfig.constants.CacheData;
import com.pd.config.pdonlineconfig.constants.CommandTypes;
import com.pd.config.pdonlineconfig.impls.InternetManagerImpl;
import com.pd.config.pdonlineconfig.interfaces.NetListener;
import com.pd.config.pdonlineconfig.net.ControlUnitMessager;
import com.pd.config.pdonlineconfig.pojo.PdParams;

public class  ControUnitPDParamsFragment extends Fragment implements NetListener {
    private ControUnitPDParamsFragment paramsFragment;
    private boolean isFetched;
    private boolean settingSucceed;
    private TextInputLayout threholdContainer;
    private TextInputLayout kValueContainer;
    private TextInputLayout bValueContainer;
    private TextInputLayout aaSignalContainer;
    private TextInputLayout aaKValueContainer;
    private TextInputLayout aaBValueContainer;
    private TextInputLayout frequencyContainer;
    private Spinner syncType;
    private Spinner filterFrequency;
    private EditText kValue;
    private EditText bValue;
    private EditText aaSignal;
    private EditText aaKValue;
    private EditText aaBValue;
    private EditText frequency;
    private EditText threholdValue;
    private Button settingBtn;

    public static ControUnitPDParamsFragment newInstance() {
        ControUnitPDParamsFragment fragment = new ControUnitPDParamsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("init", "init");
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pd_params_layout, null);
        initComponents(view);
        return view;
    }

    private void initComponents(View view) {
        threholdContainer = view.findViewById(R.id.thresholdContainer);
        threholdValue = view.findViewById(R.id.threshold);
        kValueContainer = view.findViewById(R.id.kValueLayout);
        kValue = view.findViewById(R.id.kValue);
        bValueContainer = view.findViewById(R.id.bValueLayout);
        bValue = view.findViewById(R.id.bValue);
        aaSignalContainer = view.findViewById(R.id.aaSignalLayout);
        aaSignal = view.findViewById(R.id.aaSignal);
        aaKValueContainer = view.findViewById(R.id.aaKValueLayout);
        aaKValue = view.findViewById(R.id.aaKValue);
        aaBValueContainer = view.findViewById(R.id.aaBValueLayout);
        aaBValue = view.findViewById(R.id.aaBValue);
        frequencyContainer = view.findViewById(R.id.frequencyLayout);
        frequency = view.findViewById(R.id.frequency);
        syncType = view.findViewById(R.id.syncType);
        filterFrequency = view.findViewById(R.id.filterFrequency);
        threholdValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isValidateThrehold(s)) {
                    threholdContainer.setHint("范围为0-79");
                    threholdContainer.setErrorEnabled(true);
                    threholdContainer.setError("!!");

                } else {
                    threholdContainer.setErrorEnabled(false);
                    threholdContainer.setHint("");

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("")) {
                    threholdValue.setHint("");
                    threholdContainer.setHintEnabled(true);
                }
            }
        });
        aaSignal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isValidateThrehold(s)) {
                    aaSignalContainer.setHint("范围为0-79");
                    aaSignalContainer.setErrorEnabled(true);
                    aaSignalContainer.setError("!!");

                } else {
                    aaSignalContainer.setErrorEnabled(false);
                    aaSignalContainer.setHint("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        settingBtn = view.findViewById(R.id.settingBtn);
        settingBtn.setOnClickListener(v -> {
            boolean pass = checkValue();
            if (pass) {
                PdParams params = getValues();
                if (CacheData.ip != null) {
                    ControlUnitMessager messager = new ControlUnitMessager(CacheData.ip, CacheData.port, CommandTypes.SET_PD_TESTPARAMS);
                    messager.setManager(new InternetManagerImpl());
//                    messager.setPdParams(params);
//                    messager.start();
                }

            }
        });
    }


    private PdParams getValues() {
        PdParams pdParams = new PdParams();
        pdParams.setThresholdValue((byte) Integer.parseInt(threholdValue.getText().toString()));
        pdParams.setkValue(Float.parseFloat(kValue.getText().toString()));
        pdParams.setBValue(Float.parseFloat(bValue.getText().toString()));
        pdParams.setAaSignalValue((byte) Integer.parseInt(aaSignal.getText().toString()));
        pdParams.setAaKValue(Float.parseFloat(aaKValue.getText().toString()));
        pdParams.setAaBValue(Float.parseFloat(aaBValue.getText().toString()));
        pdParams.setSyncFrequency(Float.parseFloat(frequency.getText().toString()));
        String filterFre = (String) filterFrequency.getSelectedItem();
        if (filterFre.equals("全频段")) {
            pdParams.setFilterFrequency((byte) 0);
        } else if (filterFre.equals("低频段")) {
            pdParams.setFilterFrequency((byte) 1);
        } else {
            pdParams.setFilterFrequency((byte) 2);
        }
        String sync = (String) syncType.getSelectedItem();
        if (sync.equals("外同步")) {
            pdParams.setTypeOfSync((byte) 1);
        } else {
            pdParams.setTypeOfSync((byte) 0);
        }
        return pdParams;
    }

    private boolean checkValue() {
        if (kValue.getText().toString().equals("")) return false;
        if (threholdValue.getText().toString().equals("")) return false;
        try {
            int threhold = Integer.parseInt(threholdValue.getText().toString());
            if (threhold < 0 || threhold > 79) return false;
        } catch (Exception e) {
            return false;
        }
        if (bValue.getText().toString().equals("")) return false;
        if (aaSignal.getText().toString().equals("")) return false;
        try {
            int aaSignalInt = Integer.parseInt(aaSignal.getText().toString());
            if (aaSignalInt < 0 || aaSignalInt > 79) return false;
        } catch (Exception e) {
            return false;
        }
        if (aaKValue.getText().toString().equals("")) return false;
        if (aaBValue.getText().toString().equals("")) return false;
        if (frequency.getText().toString().equals("")) return false;
        return true;
    }

    public boolean isValidateThrehold(CharSequence sequence) {
        String valueStr = sequence.toString();
        if (valueStr.contains(".")) {
            return false;
        }
        if (valueStr.equals("")) return false;
        try {
            int anInt = Integer.parseInt(valueStr);
            if (anInt > 79 || anInt < 0) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            return false;
        }


    }

    @Override
    public void handleMessage(Message msg) {
        int code = msg.what;
        switch (code) {
            case 0x05:
                //设置成功
                System.out.println("设置参数成功");
                break;
            case 0x02:
                //获取成功
                Bundle data = msg.getData();
                PdParams params = (PdParams) data.getSerializable("pdParams");
                if (params != null) {
                    threholdValue.setText(params.getThresholdValue());
                    byte filter= params.getFilterFrequency();
                    if(filter==0){
                        filterFrequency.setSelection(0);
                    }else if(filter==1){
                        filterFrequency.setSelection(1);
                    }else {
                        filterFrequency.setSelection(2);
                    }
                    kValue.setText(Float.valueOf(params.getkValue()).toString());
                    bValue.setText(Float.valueOf(params.getBValue()).toString());
                    aaSignal.setText(params.getAaSignalValue());
                    aaKValue.setText(Float.valueOf(params.getAaKValue()).toString());
                    aaBValue.setText(Float.valueOf(params.getAaBValue()).toString());
                    frequency.setText(Float.valueOf(params.getSyncFrequency()).toString());
                    byte sync = params.getTypeOfSync();
                    if(sync==1){
                        syncType.setSelection(1);
                    }else {
                        syncType.setSelection(0);
                    }
                }

                break;
            case 0x04:
                //获取超时

                break;
        }
    }
}
