package PDChartsPack.pack_commen;

import android.content.Context;
import android.util.Log;
import android.widget.Button;

import com.pdonlineport.R;


/**
 * Created by SONG on 2018/8/9 10:03.
 * The final explanation right belongs to author
 */
public class System_Activity_Tools {
    /**设置按钮使能**/
    public   static  void SetButtonEnable(Context context, Button btn, boolean enabled)
    {
//        if(enabled) {
//            btn.setBackground(context.getResources().getDrawable(R.drawable.style_used_btn_selter));
//            btn.setClickable(true);
//        }
//        else
//        {
//            btn.setBackground(context.getResources().getDrawable(R.drawable.style_unused_btn_selter));
//            btn.setClickable(false);
//        }

    }

    public static void DEBUG_MSG(String MSG)
    {
        Log.e("DEBUG", "DEBUG_MSG: "+ MSG);

    }
}
