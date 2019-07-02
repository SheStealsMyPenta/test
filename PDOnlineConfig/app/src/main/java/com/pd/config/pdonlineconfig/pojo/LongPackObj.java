package com.pd.config.pdonlineconfig.pojo;

import com.pd.config.pdonlineconfig.utils.ConversionTool;

import java.io.Serializable;
import java.util.List;

public class LongPackObj implements Serializable {
    private int currentPack;
    private int numberOfPack;

    public List<byte[]> getListOfBytes() {
        return listOfBytes;
    }

    public void setListOfBytes(List<byte[]> listOfBytes) {
        this.listOfBytes = listOfBytes;
    }

    private List<byte[]> listOfBytes;
    private byte[] data;
    private boolean succeed;
    private int offset;
    int lengthOfAll = 0;

    public int getLengthOfAll() {
        return lengthOfAll;
    }

    public void setLengthOfAll(int lengthOfAll) {
        this.lengthOfAll = lengthOfAll;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public boolean getDataFromSource(byte[] data, int lengthOfPack) {
        byte[] bytes = data;
        short currentPack = ConversionTool.toshort(new byte[]{bytes[1], bytes[2]});
        lengthOfAll += lengthOfPack;
        byte[] realdata = new byte[lengthOfPack];
        System.arraycopy(data, 10, realdata, 0, realdata.length);

        if (listOfBytes!=null){
            listOfBytes.add(realdata);
        }
//        for (int i = 0; i < 60; i++) {
//            System.out.println(realdata[i + 1]);
//        }
        if(currentPack-offset!=1){
            System.out.println("掉包");
            return false;
        }
        offset++;
        if (offset == numberOfPack) {
            succeed = true;
        }
        return true;
    }
    public void getJsonFromSource(byte[] data,int lengthOfPack){
        lengthOfAll += lengthOfPack;
        byte[] realdata = new byte[lengthOfPack];
        System.arraycopy(data, 10, realdata, 0, realdata.length);
        listOfBytes.add(realdata);
//        for (int i = 0; i < 60; i++) {
//            System.out.println(realdata[i + 1]);
//        }
        offset++;
        if (offset == numberOfPack) {
            succeed = true;
        }
    }
    public boolean isSucceed() {
        return succeed;
    }

    public void setSucceed(boolean succeed) {
        this.succeed = succeed;
    }

    public int getCurrentPack() {
        return currentPack;
    }

    public void setCurrentPack(int currentPack) {
        this.currentPack = currentPack;
    }

    public int getNumberOfPack() {
        return numberOfPack;
    }

    public void setNumberOfPack(int numberOfPack) {
        this.numberOfPack = numberOfPack;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
