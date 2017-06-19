package cast.lh.cn.weather;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class MainActivity extends Activity implements View.OnClickListener {

    private TextView select_city,select_weather,select_temp,select_wind,select_pm;
    private Map<String,String> map;
    private List<Map<String,String>> list;
    private String temp,weather,name,pm,wind;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        select_city = (TextView) findViewById(R.id.select_city);
        select_weather = (TextView) findViewById(R.id.select_weather);
        select_temp = (TextView) findViewById(R.id.temp);
        select_wind = (TextView) findViewById(R.id.wind);
        select_pm = (TextView) findViewById(R.id.pm);
        findViewById(R.id.city_ld).setOnClickListener(this);
        findViewById(R.id.city_xy).setOnClickListener(this);
        findViewById(R.id.city_cs).setOnClickListener(this);
        try {
            List<WeatherInfo> infos = WeatherService.getWeatherInfos(
                    getAssets().open("weather.xml"));
            list = new ArrayList<Map<String,String>>();
            for(WeatherInfo info:infos){
                map = new HashMap<String,String>();
                map.put("temp",info.getTemp());
                map.put("weather",info.getWeather());
                map.put("name",info.getName());
                map.put("pm",info.getPm());
                map.put("wind",info.getWind());
                list.add(map);
            }
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this,"解析信息失败",Toast.LENGTH_LONG).show();
        }
        getMap(1,R.drawable.sun);
    }
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.city_ld:
                getMap(0,R.drawable.clounds);
                break;
            case R.id.city_cs:
                getMap(1,R.drawable.sun);
                break;
            case R.id.city_xy:
                getMap(2,R.drawable.clounds);
        }
    }
    private void getMap(int number,int iconNumber){
        Map<String,String> bjMap = list.get(number);
        temp = bjMap.get("temp");
        weather = bjMap.get("weather");
        name=bjMap.get("name");
        pm=bjMap.get("pn");
        wind=bjMap.get("wind");
        select_city.setText(name);
        select_wind.setText("风力："+wind);
        select_pm.setText("pm："+pm);
        select_weather.setText(weather);
        select_temp.setText(""+temp);
    }
}
