package in.aceventura.evolvuschool.teacherapp;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Context;
import android.util.Log;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by abhinav on 29/9/17.
 */

public class SystemTime  {
    int pHour, pSecond;
    int pMinute;
    EditText editText;
    TimePickerDialog timePickerDialog;
    Context context;

    public SystemTime() {

    }

    public static String getSystemTime()
    {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = timeFormat.format(new Date());
        Log.i("Time===>", "Yes " + time);
        return time;
    }

    public static String getSystemTime2()
    {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat timeFormat = new SimpleDateFormat("yyyyMMdd HHmmss");
        String time = timeFormat.format(new Date());
        Log.i("Time===>", "Yes " + time);
        return time;
    }

    public static String getSystemDate()
    {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");
        String time = timeFormat.format(new Date());
        Log.i("Time===>", "Yes " + time);
        return time;
    }

    public static String getSystemDate1()
    {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat timeFormat = new SimpleDateFormat("dd-MM-yyyy");
        String time = timeFormat.format(new Date());
        Log.i("Time===>", "Yes " + time);
        return time;
    }

    public static double getSystemDateChart()
    {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat timeFormat = new SimpleDateFormat("dd.MM");
        String time = timeFormat.format(new Date());
        Log.i("Time===>", "Yes " + time);
        double value = Double.parseDouble(time);
        return value;
    }
}
