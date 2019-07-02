package PDChartsPack.pack_commen;

/**
 * Created by SONG on 2017/12/18 16:55.
 * The final explanation right belongs to author
 */

public final class Cmd_Parse {

    /**
     * 版本号翻译
     * @param data 原始数据
     * @param offset 偏移位置
     * @return 字符串的版本号
     */
    public static String Parse_Version(byte[] data, int offset) {
        byte version[] = new byte[4];
        System.arraycopy(data, offset, version, 0, 4);
        String result = Integer.toString((int)version[0]);
        result += ".";
        result += Integer.toString((int)version[1]);
        result += ".";
        byte version1[] = new byte[2];
        System.arraycopy(version,2,version1,0,2);
        result+= Integer.toString(Convert_Tools.toshort(version1)&0xffff);
        return result;
    }

    /**
     * 获取设备类型
     * @param data 类型
     * @return 类型字符串
     */

    public static String Parse_Type(byte data)
    {
        switch ((int)data)
        {
            case 0:
                return "特高频";
            case 1:
                return "接触式超声";
            case 2:
                return "高频电流";
            case 3:
                return "暂态地电压";
            case 4:
                return "空气式超声";
            default:
                return "未识别设备";

        }
    }



}
