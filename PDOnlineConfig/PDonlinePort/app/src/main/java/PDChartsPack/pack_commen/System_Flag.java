package PDChartsPack.pack_commen;

/**
 * Created by user on 2018/1/29.
 */

public final class System_Flag {
    public static boolean g_bIsPasswordChecked = false;
    public static float Current_Sync_Freq = 50.0f;
    public static byte Current_Sync_Type = 0;

    public static byte HUF_Current_Felter_Type = 2;
    public static byte HF_Current_Felter_Type = 0;

    public static byte AA_Height = 0x01;
    public static byte AA_Low = 0x10;

    public static byte Ae_Height = 0x00;
    public static byte Ae_Low = 0x00;


    public static int PRE_COLOR = 0xffffd700;
    public static int Normal_Color = 0xff00ff00;
    public static int War_Color = 0xffff0000;



    public static String Felter_Type_Low(byte type) {

        switch (type) {
            case 0x00:
                return "低通100kHz";
            case 0x10:
                return "低通200kHz";
            default:
                return null;
        }
    }

    public static String Felter_Type_Height(byte type) {
        switch (type) {
            case 0x00:
                return "高通10kHz  ";
            case 0x01:
                return "高通20kHz  ";
            case 0x02:
                return "高通80kHz  ";
            default:
                return null;
        }
    }

    public static float Felter_Low(byte type) {

        switch (type) {
            case 0x00:
                return 100000.0f;
            case 0x10:
                return 200000.0f;
            default:
                return 0;
        }
    }

    public static float Felter_Height(byte type) {
        switch (type) {
            case 0x00:
                return 10000.0f;
            case 0x01:
                return 20000.0f;
            case 0x02:
                return 80000.0f;
            default:
                return 0;
        }
    }

    public static String SyncType(byte Type) {
        if (Type == 0) {
            return "内同步";
        } else if (Type == 1) {
            return "外同步";

        } else {
            return "自动";
        }

    }

    /***
     * 滤波类型解析
     * @param Type
     * @return
     */
    public static String Feltetr_Type(byte Type) {
        if (Type == 0) {
            return "全频段";
        } else if (Type == 1) {
            return "低频段";
        } else {
            return "高频段";
        }
    }
}
