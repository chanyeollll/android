package cast.lh.cn.weather;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lihao on 2017/6/16.
 */

public class WeatherService {
    public static List<WeatherInfo> getWeatherInfos(InputStream is) throws IOException, XmlPullParserException {
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(is,"utf-8");
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        List<WeatherInfo> weatherInfos = null;
        WeatherInfo weatherInfo = null;
        int type = 0;
        try {
            type = parser.getEventType();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        while(type!=XmlPullParser.END_DOCUMENT){
            switch (type){
                case XmlPullParser.START_TAG:
                    if ("infos".equals(parser.getName())){
                        weatherInfos = new ArrayList<WeatherInfo>();
                    }else if ("city".equals(parser.getName())){
                        weatherInfo = new WeatherInfo();
                        String idStr = parser.getAttributeValue(0);
                        weatherInfo.setId(Integer.parseInt(idStr));
                    }else if ("temp".equals(parser.getName())){
                        String temp = parser.nextText();
                        weatherInfo.setTemp(temp);
                    }else if ("weather".equals(parser.getName())){
                        String weather = parser.nextText();
                        weatherInfo.setWeather(weather);
                    }else if ("name".equals(parser.getName())){
                        String name = parser.nextText();
                        weatherInfo.setName(name);
                    }else if ("pm".equals(parser.getName())){
                        String pm = parser.nextText();
                        weatherInfo.setPm(pm);
                    }else if ("wind".equals(parser.getName())){
                        String wind = parser.nextText();
                        weatherInfo.setWind(wind);
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if("city".equals(parser.getName())){
                        weatherInfos.add(weatherInfo);
                        weatherInfo=null;
                    }
                    break;
            }
            type=parser.next();
        }
        return weatherInfos;
    }
}
