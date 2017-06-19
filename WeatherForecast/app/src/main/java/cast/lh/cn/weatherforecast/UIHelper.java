package cast.lh.cn.weatherforecast;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by lihao on 2017/6/15.
 */

public class UIHelper {
    /**
     * 显示toast
     *
     * @param context
     *            上下文对象
     * @param msg
     *            显示的文本内容
     */
    public static void showToastMsg(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showToastMsg(Context context, int resStrId) {
        Toast.makeText(context, resStrId, Toast.LENGTH_SHORT).show();
    }
    /**
     * 检查输入是否为空
     *
     * @return
     */
    public static boolean checkInputIsEmpty(EditText et){
        if ("".equals(et.getText().toString().trim())) {
            return true;
        }
        return false;
    }
    /**
     * 显示dialog
     */
    public static void showProDialog(ProgressDialog dialog) {
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("提示");
        dialog.setMessage("请稍等，正在查询...");
        dialog.setIndeterminate(false);
        dialog.setCancelable(true);
        dialog.show();
    }
}
