package in.aceventura.evolvuschool.teacherapp.bottombar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.aceventura.evolvuschool.teacherapp.HomePageDrawerActivity;
import in.aceventura.evolvuschool.teacherapp.R;
import in.aceventura.evolvuschool.teacherapp.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.teacherapp.activities.SharedClass;
import in.aceventura.evolvuschool.teacherapp.activities.TeacherProfileActivity;
import in.aceventura.evolvuschool.teacherapp.adapter.CalendarEventsDecorator;
import in.aceventura.evolvuschool.teacherapp.adapter.Calendar_EventsWishAdapter;
import in.aceventura.evolvuschool.teacherapp.pojo.CalendarModel;

public class MyCalendar extends AppCompatActivity implements OnDateSelectedListener, OnMonthChangedListener {
    MaterialCalendarView calendarView;
    Date date1 = null;
    Date date2 = null;
    SimpleDateFormat sdf;
    List<CalendarModel> eventsWishList = new ArrayList<>();
    RecyclerView rv_events;
    Calendar_EventsWishAdapter calendar_eventsWishAdapter;
    DatabaseHelper mDatabaseHelper;

    String name, newUrl, dUrl, pid, rnewUrl;
    String url, rurl;
    String class_id, section_id;
    ProgressDialog dialog;
    String col = "";//dot color on calender
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("My Calendar");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_calendar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        calendarView = findViewById(R.id.calendarView);
        getSupportActionBar().hide();

        try {

            rv_events = findViewById(R.id.rv_events);
            mDatabaseHelper = new DatabaseHelper(this);

            mContext = this;

            class_id = getIntent().getStringExtra("class_id");
            section_id = getIntent().getStringExtra("section_id");
            //   class_id = SharedClass.getInstance(MyCalendar.this).getClas;
            // section_id = SharedPrefManager.getInstance(MyCalendar.this).getSectionId();


            Log.e("val", "dafclass" + class_id);
            Log.e("val", "dafsec");
            name = mDatabaseHelper.getName(1);
            newUrl = mDatabaseHelper.getURL(1);
            rnewUrl = mDatabaseHelper.getURL(1);
            dUrl = mDatabaseHelper.getPURL(1);

            if (name == null || name.equals("")) {
                name = mDatabaseHelper.getName(1);
                newUrl = mDatabaseHelper.getURL(1);
                dUrl = mDatabaseHelper.getPURL(1);
            }


//todo NO Delate below below code in comment section it add event google calender need letter-
      /*  try {
            String year = "2020";


            Calendar beginTime = Calendar.getInstance();
            beginTime.set(Integer.parseInt(year), 10, 30, 7, 30);
            Calendar endTime = Calendar.getInstance();
            endTime.set(2020, 10, 30, 8, 30);

            Intent m = new Intent(Intent.ACTION_INSERT)
                    .setData(CalendarContract.Events.CONTENT_URI)
                    .putExtra(CalendarContract.Events.TITLE, "Ashif Calender") // Simple title
                    .putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false)
                    // Only date part is considered when ALL_DAY is true; Same as DTSTART
                    .putExtra(CalendarContract.Events.EVENT_LOCATION, "Hong Kong")
                    .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                    .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())

                    .putExtra(CalendarContract.Events.LAST_DATE, "30/11/2020")
                    .putExtra(CalendarContract.Events.DESCRIPTION, "DESCRIPTION") // Description
                    .putExtra(Intent.EXTRA_EMAIL, "fooInviteeOne@gmail.com,fooInviteeTwo@gmail.com")// Recurrence rule
                    .putExtra(CalendarContract.Events.ACCESS_LEVEL, CalendarContract.Events.ACCESS_PRIVATE)
                    .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_FREE);
            startActivity(m);
        } catch (Exception e) {

            widget.setOnDateChangedListener(this);
            widget.setOnMonthChangedListener(this);


            Toast.makeText(getApplicationContext(), "google calender is not install use ", Toast.LENGTH_SHORT).show();
        }*/


            //Top Bar
            View tb_main1 = findViewById(R.id.icd_tb_mycalender);
            TextView school_title = tb_main1.findViewById(R.id.school_title);
            TextView ht_Teachernote = tb_main1.findViewById(R.id.ht_Teachernote);
            TextView tv_academic_yr = tb_main1.findViewById(R.id.tv_academic_yr);
            ImageView ic_back = tb_main1.findViewById(R.id.ic_back);
            ImageView drawer = tb_main1.findViewById(R.id.drawer);
            tv_academic_yr.setText("(" + SharedClass.getInstance(getApplicationContext()).getAcademicYear() + ")");
            school_title.setText(name + " Evolvu Parent App");
            ht_Teachernote.setText("My Calendar");
            ic_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                    finish();
                }
            });


            //---------Code to change the logo dynamically based on urls (NEW) -----------------
            String logoUrl;
            if (name.equals("SACS")) {
                logoUrl = dUrl + "uploads/logo.png";
            } else {
                logoUrl = dUrl + "uploads/" + name + "/logo.png";
            }
            Log.e("LogoUrl", "Values:" + logoUrl);
            Glide.with(this).load(logoUrl)
                    .thumbnail(0.5f)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(drawer);

            //-----------------------------------------------------------


            Log.e("Calender", "ValuesSN=>" + name);
            Log.e("Calender", "ValuesURL=>" + newUrl + "get_all_published_events");
            //  Log.e("Calender", "class_id=>" + SharedClass.getInstance(MyCalendar.this).getChildClass() + "class_id fbn=" + class_id);
            //  Log.e("Calender", "section_id=>" + SharedPrefManager.getInstance(MyCalendar.this).getChildSection() + "section fn=" + section_id);
            Log.e("Calender", "ValuesURLD=>" + dUrl);
            //  Log.e("Calender", "ValuesaY=>" + SharedPrefManager.getInstance(MyCalendar.this).getAcademicYear());


            url = dUrl + "index.php/AdminApi/get_all_published_events";
            ///https://sfs.evolvu.in/test/msfs_test/index.php/AdminApi/get_all_published_events
            // https://sfs.evolvu.in/test/msfs_test/index.php/AdminApi/get_all_published_events

            //https://www.aceventura.in/demo/SACSv4test/index.php/AdminApi/get_all_published_events
            //https://www.aceventura.in/demo/SACSv4test/index.php/AdminApi/get_all_published_events
            Log.e("Calender", "URL=>" + url);
            calendarView.setOnDateChangedListener(this);
            calendarView.setOnMonthChangedListener(this);

            Calendar c = Calendar.getInstance();
            int cyear = c.get(Calendar.YEAR);//calender year starts from 1900 so you must add 1900 to the value recevie.i.e., 1990+112 = 2012
            int cmonth = c.get(Calendar.MONTH);//this is april so you will receive  3 instead of 4.
            Log.e("Calender", "year=>" + cyear + "==" + "Month" + cmonth);
            int cday = c.get(Calendar.DAY_OF_MONTH);

            eventsWishList.clear();
            getCalenderEvent((cmonth + 1), cyear);
            getCalenderRestriction();
            //"https://aceventura.in/demo/Test_ParentAppService/get_all_published_events"
            //"https://aceventura.in/demo/Test_ParentAppService/get_all_published_events
            //newUrl + "get_all_published_events"
            try {
                View view = findViewById(R.id.icd_bottom);
                TextView supportEmail = view.findViewById(R.id.email);
                //---------------Support email--------------------------
                if (name != null) {
                    String supportname = name.toLowerCase();

                    supportEmail.setText("For app support email to : " + "support" + supportname + "@aceventura.in");
                } else {
                    supportEmail.setText("For app support email to : " + "aceventuraservices@gmail.com");
                    return;
                }


                BottomBar bottomBar = (BottomBar) view.findViewById(R.id.bottomBar);
                bottomBar.setDefaultTabPosition(1);
                try {

                    bottomBar.setActiveTabColor(getResources().getColor(R.color.bottomactivateColor));

                } catch (Exception e) {

                    e.printStackTrace();

                }
                bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
                    @Override
                    public void onTabSelected(@IdRes int tabId) {
                        if (tabId == R.id.tab_dashboard) {
                            //
                            Intent intent = new Intent(MyCalendar.this, HomePageDrawerActivity.class);
                            intent.putExtra("class_id", class_id);
                            intent.putExtra("section_id", section_id);
                            startActivity(intent);
                        }

                        if (tabId == R.id.tab_calendar) {
                            // The tab with id R.id.tab_favorites was selected,
                            // change your content accordingly.

                     /*   Intent intent = new Intent(MyCalendar.this, MyCalendar.class);
                        intent.putExtra("class_id", class_id);
                        intent.putExtra("section_id", section_id);
                        startActivity(intent);*/
                        }
                        if (tabId == R.id.tab_profile) {
                            // The tab with id R.id.tab_favorites was selected,
                            // change your content accordingly.
                            Intent intent = new Intent(MyCalendar.this, TeacherProfileActivity.class);
                            intent.putExtra("class_id", class_id);
                            intent.putExtra("section_id", section_id);
                            startActivity(intent);
                        }


                    }
                });
                bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
                    @Override
                    public void onTabReSelected(int tabId) {

                        if (tabId == R.id.tab_calendar) {
                            // The tab with id R.id.tab_favorites was selected,
                            // change your content accordingly.
                            Intent intent = new Intent(MyCalendar.this, MyCalendar.class);
                            intent.putExtra("class_id", class_id);
                            intent.putExtra("section_id", section_id);
                            startActivity(intent);
                        }
                        if (tabId == R.id.tab_profile) {
                            // The tab with id R.id.tab_favorites was selected,
                            // change your content accordingly.
                            Intent intent = new Intent(MyCalendar.this, TeacherProfileActivity.class);
                            intent.putExtra("class_id", class_id);
                            intent.putExtra("section_id", section_id);
                            startActivity(intent);
                        }
                        if (tabId == R.id.tab_dashboard) {
                            // The tab with id R.id.tab_favorites was selected,
                            // change your content accordingly.
                            Intent intent = new Intent(MyCalendar.this, HomePageDrawerActivity.class);
                            intent.putExtra("class_id", class_id);
                            intent.putExtra("section_id", section_id);
                            startActivity(intent);
                        }
                    }
                });
            } catch (Exception e) {
                e.getMessage();
            }


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "No Event Load !", Toast.LENGTH_SHORT).show();
        }
    }

    private void getCalenderRestriction() {
        try {
            rurl = rnewUrl + "get_academic_yr_from_to_dates";
            Log.e("MCVRestriction", "Restriction url=>" + rurl);
            RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
            StringRequest request = new StringRequest(Request.Method.POST, rurl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {

                        Log.e("MCVRestriction", "Restriction responce=>" + response);


                        String respi = response.substring(0, response.length() - 1);
                        Log.e("MCVRestriction", "Restriction responce=>" + respi);
                        JSONArray jsonArray = new JSONArray(respi);
                        JSONObject object = jsonArray.getJSONObject(0);
                        String academic_yr_from = object.getString("academic_yr_from");
                        String academic_yr_to = object.getString("academic_yr_to");
                        String[] from = academic_yr_from.split("-");
                        String[] to = academic_yr_to.split("-");
                        String fYear = from[0];
                        String fMonth = String.valueOf(Integer.parseInt(from[1]) - 1);
                        String fDay = from[2];
                        String tYear = to[0];
                        String tMonth = String.valueOf(Integer.parseInt(to[1]) - 1);
                        String tDay = to[2];

                        calendarView.state().edit()
                                .setFirstDayOfWeek(Calendar.WEDNESDAY)
                                .setMinimumDate(CalendarDay.from(Integer.parseInt(fYear), Integer.parseInt(fMonth), Integer.parseInt(fDay)))
                                .setMaximumDate(CalendarDay.from(Integer.parseInt(tYear), Integer.parseInt(tMonth), Integer.parseInt(tDay)))
                                .setCalendarDisplayMode(CalendarMode.MONTHS)
                                .commit();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("calres", "error" + error.getStackTrace());
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    if (name == null || name.equals("")) {
                        name = mDatabaseHelper.getName(1);
                        newUrl = mDatabaseHelper.getURL(1);
                        dUrl = mDatabaseHelper.getPURL(1);
                    }

                    params.put("short_name", name);//name//"SACS"
                    params.put("academic_yr", SharedClass.getInstance(MyCalendar.this).getAcademicYear());//name//"SACS"
               /* params.put("academic_yr", SharedPrefManager.getInstance(MyCalendar.this).getAcademicYear());//"2020-2021"//SharedPrefManager.getInstance(MyCalendar.this).getAcademicYear()
                params.put("class_id", class_id);
                //params.put("section_id", section_id);
             *//*   params.put("month", String.valueOf(cmonth));
                params.put("year", String.valueOf(cyear));
             *//*
                params.put("reg_id", String.valueOf(SharedPrefManager.getInstance(getApplicationContext()).getRegId()));
                Log.e("mycalca", "para" + params);*/
                    Log.e("MCVRestriction", "Restriction params=>" + params);

                    return params;
                }
            };
            requestQueue.add(request);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Calender", "Error In Restrinction " + e.getMessage());
        }
    }

    private void getCalenderEvent(int cmonth, int cyear) {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        if (response == null) {

                            Toast.makeText(getApplicationContext(), "No Data Available", Toast.LENGTH_SHORT).show();
                        }

                        Log.e("mycal", "responce==>>lll--" + response);
                        Log.e("mycalender", "responce==>>" + response);
                        Log.e("mycalender", "url==>>" + url);
                        eventsWishList.clear();

                        CalendarModel evCalendarModel = null;
                        CalendarModel evCalendarModel1 = null;
                        CalendarModel evCalendarModelends = null;
                        CalendarModel evCalendarModel2 = null;
                        sdf = new SimpleDateFormat("dd-MM-yyyy");
                        /* sdf = new SimpleDateFormat("yyyy/MM/dd");*/
                        String values = "" + response.trim();
                        values=values.trim();
                        values = values.substring(1, values.length() - 1);
                        values = values.replaceAll("\\\\", "");
                        Log.e("values", "values===?" + values);

                        JSONObject jsonObject = new JSONObject(values);
                        try {
                            JSONArray Events = jsonObject.getJSONArray("Events");
                            for (int i = 0; i < Events.length(); i++) {
                                evCalendarModel = new CalendarModel();
                                JSONObject object = Events.getJSONObject(i);
                                evCalendarModel.setTitle(object.getString("title"));
                                evCalendarModel.setEvent_desc(object.getString("event_desc"));
                                evCalendarModel.setColor(object.getString("colorcode"));

                                try {
                                    String evStart_date = object.getString("start_date");
                                    date1 = sdf.parse(evStart_date);
                                    evCalendarModel.setStart_date(date1.getTime());
                                } catch (ParseException e) {
                                    Log.e("Tag", "errror" + e.getMessage());
                                    e.printStackTrace();
                                }
                                eventsWishList.add(evCalendarModel);
                            }
                        } catch (Exception e) {
                            e.getStackTrace();
                            Log.e("Tag", "errror" + e.getMessage());
                            e.getMessage();
                        }
                        try {
                            JSONArray Holidays = jsonObject.getJSONArray("Holidays");
                            for (int i = 0; i < Holidays.length(); i++) {
                                evCalendarModel1 = new CalendarModel();
                                evCalendarModelends = new CalendarModel();
                                JSONObject object = Holidays.getJSONObject(i);
                                evCalendarModel1.setTitle(object.getString("title"));

                                evCalendarModel1.setEvent_desc(object.getString("event_desc"));
                                evCalendarModel1.setColor(object.getString("colorcode"));

                                try {
                                    String hoHoliday_Date = object.getString("start_date");
                                    String end_date = object.getString("end_date");
                                    eventsWishList.add(evCalendarModel1);
                                    if (end_date.equals("null")) {

                                    } else {
                                       String[] start= hoHoliday_Date.split("-");
                                       String[] end_date1= end_date.split("-");
                                        if(start[1].equalsIgnoreCase(end_date1[1])) {
                                            evCalendarModelends.setTitle(object.getString("title") + " Ends");
                                            evCalendarModelends.setColor(object.getString("colorcode"));
                                            date2 = sdf.parse(end_date);
                                            // evCalendarModel1.setEnd_date(date2.getTime());
                                            // Log.e("ValuesEnd", end_date+"<<EndDate><"+ DateFormat.format("dd-MM-yyyy", new Date(Long.parseLong(String.valueOf(date2.getTime())))));
                                            //     evCalendarModel1.setStart_date(date1.getTime());
                                            evCalendarModelends.setStart_date(date2.getTime());
                                            eventsWishList.add(evCalendarModelends);
                                        }
                                        Log.e("ValuesEnd", "EndDateooo><"+start[1]+"ggg"+end_date1[1]);

                                    }
                                    date1 = sdf.parse(hoHoliday_Date);
                                    evCalendarModel1.setStart_date(date1.getTime());
                                    // evCalendarModel1.setStart_date(date2.getTime());

                                } catch (ParseException e) {
                                    e.getMessage();
                                    Log.e("Tag", "errror" + e.getMessage());
                                    e.printStackTrace();
                                }


                            }
                        } catch (Exception e) {
                            e.getMessage();
                            Log.e("Tag", "errror" + e.getMessage());
                            e.getStackTrace();
                        }
                        try {
                            JSONArray Homework = jsonObject.getJSONArray("Homework");//homework
                            Log.e("HomeWork", "Values=?" + Homework);
                            for (int i = 0; i < Homework.length(); i++) {
                                evCalendarModel2 = new CalendarModel();
                                JSONObject object = Homework.getJSONObject(i);
                                evCalendarModel2.setTitle(object.getString("title"));
                                evCalendarModel2.setEvent_desc(object.getString("event_desc"));
                                evCalendarModel2.setColor(object.getString("colorcode"));
                                try {
                                    String hoHoliday_Date = object.getString("start_date");
                                    date1 = sdf.parse(hoHoliday_Date);
                                    evCalendarModel2.setStart_date(date1.getTime());
                                } catch (ParseException e) {

                                    Log.e("Tag", "errror" + e.getMessage());
                                    e.printStackTrace();
                                }
                                eventsWishList.add(evCalendarModel2);
                            }
                        } catch (Exception e) {
                            e.getMessage();
                            Log.e("Tag", "errror" + e.getMessage());
                            e.getStackTrace();
                        }
                    } catch (Exception e) {
                        e.getStackTrace();
                        Log.e("Tag", "errror" + e.getMessage());
                    }
                    try {
                        calendar_eventsWishAdapter = new Calendar_EventsWishAdapter(MyCalendar.this, eventsWishList);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        rv_events.setLayoutManager(mLayoutManager);
                        rv_events.setItemAnimator(new DefaultItemAnimator());
                        rv_events.setAdapter(calendar_eventsWishAdapter);
                        calendar_eventsWishAdapter.notifyDataSetChanged();

                        addCalendarDots(eventsWishList);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("error", "msg" + e.getMessage());

                    }

                }
            }, error -> {

                Toast.makeText(mContext, "No Data for Load.", Toast.LENGTH_SHORT).show();


                //  Toast.makeText(getApplicationContext(), "values==?" + error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("MyCalender", "error==>>" + error.getMessage());
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    if (name == null || name.equals("")) {
                        name = mDatabaseHelper.getName(1);
                        newUrl = mDatabaseHelper.getURL(1);
                        dUrl = mDatabaseHelper.getPURL(1);
                    }

                    params.put("short_name", name);//name//"SACS"
                    params.put("academic_yr", SharedClass.getInstance(MyCalendar.this).getAcademicYear());//"2020-2021"//SharedPrefManager.getInstance(MyCalendar.this).getAcademicYear()
                    //params.put("class_id", class_id);
                    //params.put("section_id", section_id);
                    params.put("month", String.valueOf(cmonth));
                    params.put("year", String.valueOf(cyear));
                    params.put("reg_id", String.valueOf(SharedClass.getInstance(getApplicationContext()).getRegId()));
                    Log.e("mycal", "para" + params);

                    return params;
                }
            };
            requestQueue.add(request);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Calender", "CalenderEvent " + e.getMessage());

        }
    }


    private void addCalendarDots(List<CalendarModel> eventsWishList) {
        try {
            List<CalendarDay> calEventsDotList = new ArrayList<>();
            for (int i = 0; i < eventsWishList.size(); i++) {
                long timeInMillis = eventsWishList.get(i).getStart_date();
                calEventsDotList.add(CalendarDay.from(new Date(timeInMillis)));
                col = eventsWishList.get(i).getColor();
            }
            calendarView.addDecorator(new CalendarEventsDecorator(Color.parseColor(col), calEventsDotList));
        } catch (Exception e) {
            e.getMessage();
            Log.e("Calender", "CalenderEvent " + e.getMessage());
            Log.e("log", "mjh+" + e.getMessage());
        }
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
        getCalenderEvent((date.getMonth() + 1), date.getYear());
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(MyCalendar.this, HomePageDrawerActivity.class);
        i.putExtra("CLASSID", class_id);
        i.putExtra("SECTIONID", section_id);
        startActivity(i);
        finish();
    }
}