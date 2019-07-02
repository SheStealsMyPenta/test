package PDChartsPack.pack_commen;

/**
 * Created by SONG on 2018/8/6 9:36.
 * The final explanation right belongs to author
 */
public class System_Commen {
    /**
     * 测试类型代码
     */
    public final static byte UHF = 0x00;
    public final static byte AE = 0x01;
    public final static byte HF = 0x02;
    public final static byte TEV = 0x03;
    public final static byte AA = 0x04;

    /**
     * 接触式超声最大值
     */
    public final static int AE_MAX_VIEW = 2000;

    /**
     * 测试数据对应偏移量
     */
    public final static int UHF_OFFSET = -80;
    public final static int AE_OFFSET = 0;
    public final static int HF_OFFSET = 0;
    public final static int TEV_OFFSET = 0;
    public final static int AA_OFFSET = -7;
    public final static int[] OFFSET = {UHF_OFFSET, AE_OFFSET, HF_OFFSET, TEV_OFFSET, AA_OFFSET};

    /**
     * 屏幕尺寸
     */
    public static float Width = 1000;
    public static float Height = 1920;

    /**
     * 测试类型名称
     */
    public final static String s_UHF = "特高频";
    public final static String s_AE = "接触式超声";
    public final static String s_HF = "高频电流";
    public final static String s_TEV = "暂态地电压";
    public final static String s_AA = "空气式超声";
    public final static String[] TEST_TYPES = {s_UHF, s_AE, s_HF, s_TEV, s_AA};


    /*************RUNING_RUNING~~~~~~*************************/

    /***
     * 相位单元
     */
    public final static int PHASE_COUNT = 64;
    public final static int VOLUME_VALUE = 80;
    public final static int NET_RECV_LARGE_COUNT = 700;
    public final static int NET_RECV_LARGE_DATA_COUNT = 690;
    public final static int NET_RECV_SMALL_COUNT = 50;
    public final static int NET_RECV_SMALL_DATA_COUNT = 40;


    /***
     * 授权组
     */
    public static byte[] Instrument_permisson = {(byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04};


    /**
     * 数据顶层目录
     */
    public static String APP_COMMEN_PATH = "CXJ_DATA";
    /**
     * 运行日志缓存
     */
    public static String APP_LOG_FILE_NAME = "RUN_LOG.txt";


    public static boolean DEFINE_CX_VERSION = true;

    /**
     * 获取测试类型名称
     *
     * @param type
     * @return
     */
    public static String GetTestType(byte type) {
        if ((type >= 0) && (type <= 4)) {
            return TEST_TYPES[type];
        }
        return "";
    }

    /***
     * 授权查询
     * @param filename
     * @return
     */
    public static boolean Is_File_Permisson(String filename) {
        byte temp = -1;
        String last = filename.substring(filename.lastIndexOf("."));
        if (last.compareTo(".UHF") == 0) {
            temp = UHF;
        } else if (last.compareTo(".AE") == 0) {
            temp = AE;
        } else if (last.compareTo(".HF") == 0) {
            temp = HF;
        } else if (last.compareTo(".TEV") == 0) {
            temp = TEV;
        } else if (last.compareTo(".AA") == 0) {
            temp = AA;
        }
        return Is_Permisson(temp);
    }

    /**
     * 测试授权查询
     *
     * @param ID
     * @return
     */
    public static boolean Is_Permisson(byte ID) {

        for (byte temp : Instrument_permisson) {
            if (temp == ID) {
                return true;
            }
        }
        return false;
    }
}
