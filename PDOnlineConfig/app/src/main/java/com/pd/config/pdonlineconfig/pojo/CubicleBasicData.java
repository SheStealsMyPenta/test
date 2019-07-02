package com.pd.config.pdonlineconfig.pojo;

public class CubicleBasicData {
    private String  Data_1;
    private String Data_2;

    public CubicleBasicData(String data_1, String data_2, String data_3) {
        Data_1 = data_1;
        Data_2 = data_2;
        Data_3 = data_3;
    }

    public String getData_1() {
        return Data_1;
    }

    public void setData_1(String data_1) {
        Data_1 = data_1;
    }

    public String getData_2() {
        return Data_2;
    }

    public void setData_2(String data_2) {
        Data_2 = data_2;
    }

    public String getData_3() {
        return Data_3;
    }

    public void setData_3(String data_3) {
        Data_3 = data_3;
    }

    private String Data_3;

    public CubicleBasicData() {
    }
}
