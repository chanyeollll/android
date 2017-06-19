package cast.lh.cn.weatherforecast;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class MainActivity extends Activity {

    private EditText et_city=null;
    private Button btn_query=null;
    private TextView tv_show=null;
    private ProgressDialog dialog = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.et_city=(EditText) super.findViewById(R.id.edt_city);
        this.btn_query=(Button) super.findViewById(R.id.query_btn);
        this.tv_show=(TextView) super.findViewById(R.id.show_info);
        this.dialog=new ProgressDialog(MainActivity.this);
        this.btn_query.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!UIHelper.checkInputIsEmpty(et_city)){
                    if (MyUtils.getNetworkIsAvailable(MainActivity.this)){
                        new QueryTask().execute(et_city.getText().toString().trim());
                    }else{
                        UIHelper.showToastMsg(MainActivity.this,
                                R.string.str_neterror);
                    }
                } else {
                    UIHelper.showToastMsg(MainActivity.this,
                            R.string.str_inputwaring);
                }

            }

        });
        showWeatherPage();//显示天气界面
    }
    /**
     * 显示天气界面
     */
    private void showWeatherPage() {
        // 显示自动搜素框
        // 定位城市即通过地图定位
        // 根据首字母排列所有的省市
        et_city.setHint(R.string.str_hint_weather);
        btn_query.setText(R.string.str_submit);
    }

    /**
     * 异步查询
     *
     * @author simaben
     *
     */
    class QueryTask extends AsyncTask<String, Void, Integer> {
        private final String NAMESPACE =MainActivity.this.getResources().getString(R.string.weather_namespace);
        // WebService地址
        private static final String URL = "http://www.webxml.com.cn/webservices/weatherwebservice.asmx";
        // 这个方法会返回查询地区三天内的天气状况等信息
        private static final String METHOD_NAME = "getWeatherbyCityName";
        private static final String SOAP_ACTION = "http://WebXml.com.cn/getWeatherbyCityName";
        private String weatherToday;
        @Override
        protected Integer doInBackground(String... params) {
            getWebServiceInfos(params[0]);
            return null;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            UIHelper.showProDialog(dialog);
        }
        @Override
        protected void onPostExecute(Integer result){
            super.onPostExecute(result);
            sendMsg(1);
            tv_show.setText(weatherToday);
        }

        private void getWebServiceInfos(String cityName){
            // 判断输入的城市是否为中国的省级区域
            if (!isCityNameInList(cityName)){
                try {
                    // 第一：实例化SoapObject
                    // 对象，指定webService的命名空间（从相关WSDL文档中可以查看命名空间），以及调用方法名称
                    SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
                    // 第二步：假设方法有参数的话,设置调用方法参数
                    rpc.addProperty("theCityName", cityName);
                    // 第三步：设置SOAP请求信息(参数部分为SOAP协议版本号，与你要调用的webService中版本号一致)
                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                            SoapEnvelope.VER11);
                    // 第四步：注册Envelope
                    envelope.bodyOut = rpc;
                    envelope.dotNet = true;
                    envelope.setOutputSoapObject(rpc);
                    // 第五步：构建传输对象，并指明WSDL文档URL
                    HttpTransportSE ht = new HttpTransportSE(URL);
                    ht.debug = true;
                    // 第六步:调用WebService(其中参数为1：命名空间+方法名称，2：Envelope对象)
                    ht.call(SOAP_ACTION, envelope);
                    SoapObject detail = (SoapObject) envelope.getResponse();
                    // 第七步：解析返回数据
                    parseWeather(detail);

                } catch (SoapFault e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
            } else {
                weatherToday = getResources().getString(
                        R.string.str_citynonsupport);
            }

        }
        /**
         * 输入参数：theCityName = 城市中文名称(国外城市可用英文)或城市代码(不输入默认为上海市)，如：上海 或
         * 58367，如有城市名称重复请使用城市代码查询(可通过 getSupportCity 或 getSupportDataSet
         * 获得)；返回数据： 一个一维数组 String(22)，共有23个元素。 String(0) 到
         * String(4)：省份，城市，城市代码，城市图片名称，最后更新时间。String(5) 到 String(11)：当天的
         * 气温，概况，风向和风力
         * ，天气趋势开始图片名称(以下称：图标一)，天气趋势结束图片名称(以下称：图标二)，现在的天气实况，天气和生活指数。String(12) 到
         * String(16)：第二天的 气温，概况，风向和风力，图标一，图标二。String(17) 到 String(21)：第三天的
         * 气温，概况，风向和风力，图标一，图标二。String(22) 被查询的城市或地区的介绍
         */
        private void parseWeather(SoapObject detail)
                throws UnsupportedEncodingException {
            System.out.println(detail.toString());
            // 这里根据不同的借口返回的数据也会不同，所以要参考webservice的文档
            // 根据不同的放回结果得到的参数也要调整

            String date = detail.getProperty(6).toString();
            weatherToday = "今天：" + date.split(" ")[0];
            weatherToday = weatherToday + "\n天气：" + date.split(" ")[1];
            weatherToday = weatherToday + "\n气温："
                    + detail.getProperty(5).toString();
            weatherToday = weatherToday + "\n风力："
                    + detail.getProperty(7).toString() + "\n";

            // 生活指数
            String listState = detail.getProperty(11).toString();
            weatherToday = weatherToday + "\n" + listState + "\n";

            // 最后更新时间
            String lastUpdateTime = detail.getProperty(4).toString();
            weatherToday = weatherToday + "\n最后更新时间：" + lastUpdateTime + "\n";
        }
    }
    /**
     * 判断用户输入的是否为中国的省级区域
     *
     * @param cityName
     * @return 如果输入的是省返回true
     */
    private boolean isCityNameInList(String cityName) {

        String[] allCitys = getResources()
                .getStringArray(R.array.province_item);
        for (String city : allCitys) {
            if (city.equals(cityName)) {
                return true;
            }
        }
        return false;
    }
    public void sendMsg(int i) {
        Message msg = new Message();
        msg.what = i;
        handler.sendMessage(msg);
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            dialog.dismiss();
        };
    };
}
