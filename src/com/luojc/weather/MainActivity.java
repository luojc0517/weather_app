
package com.luojc.weather;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import com.luojc.weather.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private JSONObject weather_info_json;
    private Handler mHandler = new Handler();
    private ProgressDialog dialog = null;
    private String mCityCode = "101010100";
    private String weather_info_string;// Json数据的文本形式

    private TextView time, cityname, temperature, weather, wind;
    private TextView cy, zwx, xc, ly, sszs, cl, ls, gm;
    private TextView future1, future2, future3, future4, future5;
    private ImageView weather_img1, weather_img2;
    private ImageButton choose_city,show_list;
    private DrawerLayout mDrawerLayout;

    // 城市名
    String city;
    // 日期
    String date_y;
    // 星期几
    String week;
    // 摄氏温度
    String temp1;
    String temp2;
    String temp3;
    String temp4;
    String temp5;
    String temp6;
    // 华氏温度
    String tempF1;
    String tempF2;
    String tempF3;
    String tempF4;
    String tempF5;
    String tempF6;
    // 天气描述
    String weather1;
    String weather2;
    String weather3;
    String weather4;
    String weather5;
    String weather6;
    // 天气描述对应图片
    String img1;
    String img2;
    String img3;
    String img4;
    String img5;
    String img6;
    String img7;
    String img8;
    String img9;
    String img10;
    String img11;
    String img12;
    String img_single;
    // 图片名称
    String img_title1;
    String img_title2;
    String img_title3;
    String img_title4;
    String img_title5;
    String img_title6;
    String img_title7;
    String img_title8;
    String img_title9;
    String img_title10;
    String img_title11;
    String img_title12;
    String img_title_single;
    // 风俗描述
    String wind1;
    String wind2;
    String wind3;
    String wind4;
    String wind5;
    String wind6;
    // 风速级别描述
    String fx1;
    String fx2;
    String fl1;
    String fl2;
    String fl3;
    String fl4;
    String fl5;
    String fl6;
    // 今天穿衣指数
    String index;
    String index_d;
    // 48小时穿衣指数
    String index48;
    String index48_d;
    // 紫外线及48小时紫外线
    String index_uv;
    String index48_uv;
    // 洗车
    String index_xc;
    // 旅游
    String index_tr;
    // 舒适指数
    String index_co;
    // 晨练
    String index_cl;
    // 晾晒
    String index_ls;
    // 过敏
    String index_ag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 接收从Test Activity传过来的城市码
        Intent intent = getIntent();
        if (intent.getStringExtra("cityCode") != null) {
            mCityCode = intent.getStringExtra("cityCode");
        }

        // 初始化一些控件
        time = (TextView) findViewById(R.id.time);
        cityname = (TextView) findViewById(R.id.cityname);
        temperature = (TextView) findViewById(R.id.temperature);
        weather = (TextView) findViewById(R.id.weather);
        wind = (TextView) findViewById(R.id.wind);
        cy = (TextView) findViewById(R.id.cy);
        zwx = (TextView) findViewById(R.id.zwx);
        xc = (TextView) findViewById(R.id.xc);
        ly = (TextView) findViewById(R.id.ly);
        sszs = (TextView) findViewById(R.id.sszs);
        cl = (TextView) findViewById(R.id.cl);
        ls = (TextView) findViewById(R.id.ls);
        gm = (TextView) findViewById(R.id.gm);
        future1 = (TextView) findViewById(R.id.future1);
        future2 = (TextView) findViewById(R.id.future2);
        future3 = (TextView) findViewById(R.id.future3);
        future4 = (TextView) findViewById(R.id.future4);
        future5 = (TextView) findViewById(R.id.future5);
        weather_img1 = (ImageView) findViewById(R.id.img1);
        weather_img2 = (ImageView) findViewById(R.id.img2);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        choose_city = (ImageButton) findViewById(R.id.btnChoose);
        choose_city.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Test.class);
                startActivity(intent);
                MainActivity.this.finish();
            }

        });
        show_list = (ImageButton) findViewById(R.id.btnList);
        show_list.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                mDrawerLayout.openDrawer(Gravity.LEFT);
            }

        });

        // 产生一个对话框，在网络连接不好的时候显示正在获取数据...
        dialog = new ProgressDialog(MainActivity.this);
        dialog.setMessage(getResources().getString(
                R.string.getting_weather_info));
        // 执行主方法，获取天气信息
        getWeatherInfo();
    }

    /**
     * 开启新线程，获取天气信息
     */
    void getWeatherInfo() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                // Handler的post方法会将Runnable转成Message，多用于更新组件
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        // 在getWeatherInfo(mCityCode)阻塞时，显示提示框
                        dialog.show();
                    }
                });
                // 真正的获取并解析Json天气信息
                getWeatherInfo(mCityCode);
            }
        }.start();
    }

    /**
     * 访问天气url，获取文本数据，转化成JSONObject
     * 
     * @param citycode
     * @return
     * @throws Exception
     */
    JSONObject getWeatherData(String citycode) throws Exception {
        HttpClient client = new DefaultHttpClient();
        String url = getResources().getString(R.string.base_url) + citycode;
        HttpGet request = new HttpGet(url);
        HttpResponse response = client.execute(request);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            StringBuilder builder = new StringBuilder();// 用于拼接字符串
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(
                            response.getEntity().getContent()));
            for (String s = bufferedReader.readLine(); s != null; s = bufferedReader
                    .readLine()) {
                builder.append(s);
            }
            weather_info_string = builder.toString();
            Log.d("luojc", "builder==" + weather_info_string);
            JSONObject jsonObject = new JSONObject(weather_info_string)
                    .getJSONObject("forecast");
            Log.d("luojc", "jsonObject==" + jsonObject);
            return jsonObject;
        } else {
            return null;
        }
    }

    /**
     * 解析JSONObject，为各个天气属性赋值
     * 
     * @param citycode
     */
    void getWeatherInfo(String citycode) {
        try {
            weather_info_json = getWeatherData(citycode);
        } catch (Exception e) {// 无法获取时，对话框消失，显示toast，接着从本地文件获取
            e.printStackTrace();
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    Toast.makeText(MainActivity.this, "无法获取数据,启用本地数据",
                            Toast.LENGTH_SHORT).show();
                }
            });
            // 当发生异常无法正常获取天气信息数据时,改用之前保存的天气信息数据
            weather_info_string = getinfoFromxml(citycode);
            if (weather_info_string != null) {
                try {
                    weather_info_json = new JSONObject(weather_info_string)
                            .getJSONObject("forecast");
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            } else {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "没有保存的数据",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }// catch块结束

        if (weather_info_json != null) {
            try {
                // 城市名
                city = weather_info_json.getString("city");
                // 日期
                date_y = weather_info_json.getString("date_y");
                // 星期几
                week = weather_info_json.getString("week");
                // 预报摄氏温度
                temp1 = weather_info_json.getString("temp1");
                temp2 = weather_info_json.getString("temp2");
                temp3 = weather_info_json.getString("temp3");
                temp4 = weather_info_json.getString("temp4");
                temp5 = weather_info_json.getString("temp5");
                temp6 = weather_info_json.getString("temp6");
                // 华氏温度
                tempF1 = weather_info_json.getString("tempF1");
                tempF2 = weather_info_json.getString("tempF2");
                tempF3 = weather_info_json.getString("tempF3");
                tempF4 = weather_info_json.getString("tempF4");
                tempF5 = weather_info_json.getString("tempF5");
                tempF6 = weather_info_json.getString("tempF6");
                // 天气描述
                weather1 = weather_info_json.getString("weather1");
                weather2 = weather_info_json.getString("weather2");
                weather3 = weather_info_json.getString("weather3");
                weather4 = weather_info_json.getString("weather4");
                weather5 = weather_info_json.getString("weather5");
                weather6 = weather_info_json.getString("weather6");
                // 天气描述对应图片
                img1 = weather_info_json.getString("img1");
                img2 = weather_info_json.getString("img2");
                img3 = weather_info_json.getString("img3");
                img4 = weather_info_json.getString("img4");
                img5 = weather_info_json.getString("img5");
                img6 = weather_info_json.getString("img6");
                img7 = weather_info_json.getString("img7");
                img8 = weather_info_json.getString("img8");
                img9 = weather_info_json.getString("img9");
                img10 = weather_info_json.getString("img10");
                img11 = weather_info_json.getString("img11");
                img12 = weather_info_json.getString("img12");
                img_single = weather_info_json.getString("img_single");
                // 图片名称
                img_title1 = weather_info_json.getString("img_title1");
                img_title2 = weather_info_json.getString("img_title2");
                img_title3 = weather_info_json.getString("img_title3");
                img_title4 = weather_info_json.getString("img_title4");
                img_title5 = weather_info_json.getString("img_title5");
                img_title6 = weather_info_json.getString("img_title6");
                img_title7 = weather_info_json.getString("img_title7");
                img_title8 = weather_info_json.getString("img_title8");
                img_title9 = weather_info_json.getString("img_title9");
                img_title10 = weather_info_json.getString("img_title10");
                img_title11 = weather_info_json.getString("img_title11");
                img_title12 = weather_info_json.getString("img_title12");
                img_title_single = weather_info_json
                        .getString("img_title_single");
                // 风俗描述
                wind1 = weather_info_json.getString("wind1");
                wind2 = weather_info_json.getString("wind2");
                wind3 = weather_info_json.getString("wind3");
                wind4 = weather_info_json.getString("wind4");
                wind5 = weather_info_json.getString("wind5");
                wind6 = weather_info_json.getString("wind6");
                // 风速级别描述
                fx1 = weather_info_json.getString("fx1");
                fx2 = weather_info_json.getString("fx2");
                fl1 = weather_info_json.getString("fl1");
                fl2 = weather_info_json.getString("fl2");
                fl3 = weather_info_json.getString("fl3");
                fl4 = weather_info_json.getString("fl4");
                fl5 = weather_info_json.getString("fl5");
                fl6 = weather_info_json.getString("fl6");
                // 今天穿衣指数
                index = weather_info_json.getString("index");
                index_d = weather_info_json.getString("index_d");
                // 48小时穿衣指数
                index48 = weather_info_json.getString("index48");
                index48_d = weather_info_json.getString("index48_d");
                // 紫外线及48小时紫外线
                index_uv = weather_info_json.getString("index_uv");
                index48_uv = weather_info_json.getString("index48_uv");
                // 洗车
                index_xc = weather_info_json.getString("index_xc");
                // 旅游
                index_tr = weather_info_json.getString("index_tr");
                // 舒适指数
                index_co = weather_info_json.getString("index_co");
                // 晨练
                index_cl = weather_info_json.getString("index_cl");
                // 晾晒
                index_ls = weather_info_json.getString("index_ls");
                // 过敏
                index_ag = weather_info_json.getString("index_ag");

                // 当天气信息中的时间值与保存在xml中时间值不同时就保存当前天气信息到xml以备无网络情况下使用
                if (!date_y.equals(getdateFromXml())) {
                    saveinfo2xml(weather_info_string, city, citycode, date_y);
                }

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        // 取消提示框
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                        setInfo();// 将属性值设置到组件中显示
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 为组件设置值
     */
    void setInfo() {
        time.setText(date_y + " " + week);
        cityname.setText(city);
        temperature.setText(temp1);
        weather.setText(weather1);
        wind.setText(wind1);
        cy.setText(index);
        zwx.setText(index_uv);
        xc.setText(index_xc);
        ly.setText(index_tr);
        sszs.setText(index_co);
        cl.setText(index_cl);
        ls.setText(index_ls);
        gm.setText(index_ag);
        future1.setText(getDateTime(date_y, 1) + ": " + temp2 + " " + weather2
                + " " + wind2);
        future2.setText(getDateTime(date_y, 2) + ": " + temp3 + " " + weather3
                + " " + wind3);
        future3.setText(getDateTime(date_y, 3) + ": " + temp4 + " " + weather4
                + " " + wind4);
        future4.setText(getDateTime(date_y, 4) + ": " + temp5 + " " + weather5
                + " " + wind5);
        future5.setText(getDateTime(date_y, 5) + ": " + temp6 + " " + weather6
                + " " + wind6);
        int resId1 = getResources().getIdentifier("b" + img1, "drawable",
                getPackageName());
        int resId2 = getResources().getIdentifier("b" + img2, "drawable",
                getPackageName());
        weather_img1.setImageResource(resId1);
        weather_img2.setImageResource(resId2);
    }

    // 根据给定日期推算出addday之后的日期
    String getDateTime(String basetime, int addday) {
        Date date = null;
        String[] tempy = basetime.split("年");
        String year = tempy[0];
        String[] tempm = tempy[1].split("月");
        String month = tempm[0];
        String[] tempd = tempm[1].split("日");
        String day = tempd[0];
        basetime = year + "-" + month + "-" + day;
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = f.parse(basetime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_YEAR, addday);
        Date c = cal.getTime();
        basetime = f.format(c).toString();
        String[] temp = basetime.split("-");
        basetime = temp[0] + "/" + temp[1] + "/" + temp[2];
        return basetime;
    }

    // 保存天气信息到/data/data/packagename/shared_prefs/目录下
    void saveinfo2xml(String weatherinfo, String city, String citycode,
            String date) {
        String spName = getPackageName() + "_forecast";
        SharedPreferences sp = getSharedPreferences(spName, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("city", city);
        editor.putString("citycode", citycode);
        editor.putString("date", date);
        editor.putString("forecast", weatherinfo);
        editor.commit();
    }

    // 从/data/data/packagename/shared_prefs/目录下读取保存的天气信息
    String getinfoFromxml(String citycode) {
        String spName = getPackageName() + "_forecast";
        SharedPreferences sp = getSharedPreferences(spName, MODE_PRIVATE);
        if (citycode.equals(sp.getString("citycode", null))) {
            return sp.getString("forecast", null);
        } else {
            return null;
        }
    }

    // 从/data/data/packagename/shared_prefs/目录下读取保存的信息中的date值
    String getdateFromXml() {
        String spName = getPackageName() + "_forecast";
        SharedPreferences sp = getSharedPreferences(spName, MODE_PRIVATE);
        return sp.getString("date", null);
    }
}
