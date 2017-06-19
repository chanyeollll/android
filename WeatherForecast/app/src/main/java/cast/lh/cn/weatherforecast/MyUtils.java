package cast.lh.cn.weatherforecast;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by lihao on 2017/6/15.
 */

public class MyUtils {
    public static boolean getNetworkIsAvailable(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(context.CONNECTIVITY_SERVICE);//返回网络连接状态实例
        NetworkInfo info = manager.getActiveNetworkInfo();//得到当前网络连接的实例
        if (info == null || !info.isConnected()) {
            return false;
        }
        if (info.isRoaming()) {
            return true;
        }
        return true;
    }
}
