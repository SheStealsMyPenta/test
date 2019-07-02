package PDChartsPack.pack_commen;

import android.app.Activity;
import android.app.Application;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by SONG on 2018/5/22 9:40.
 * The final explanation right belongs to author
 */
public class System_Proc extends Application {
    private List<Activity> mList = new LinkedList<Activity>();
    private static System_Proc instance;

    private System_Proc() {
    }

    public synchronized static System_Proc getInstance() {
        if (null == instance) {
            instance = new System_Proc();
        }
        return instance;
    }

    public void addActivity(Activity activity) {
        mList.add(activity);
    }

    public void exit() {
        try {
            for (Activity activity : mList) {
                if (activity != null) activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }

    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }
}
