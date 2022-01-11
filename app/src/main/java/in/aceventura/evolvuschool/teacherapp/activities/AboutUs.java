package in.aceventura.evolvuschool.teacherapp.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import in.aceventura.evolvuschool.teacherapp.HomePageDrawerActivity;
import in.aceventura.evolvuschool.teacherapp.R;
import in.aceventura.evolvuschool.teacherapp.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.teacherapp.bottombar.MyCalendar;

public class AboutUs extends AppCompatActivity {
    TextView tv1,tv2,tv3,tv4,tv5,tv6,emailat,contactmail;
    ImageView iv1,iv2,iv3,iv4;
    DatabaseHelper mDatabaseHelper;
    String name, newUrl, dUrl, reg_id, academic_yr, class_id, subject_id, section_id, qb_name, question_bank_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        getSupportActionBar().hide();
        View tb_main1 = findViewById(R.id.icd_tb_homeworkdetails);
        TextView school_title = tb_main1.findViewById(R.id.school_title);
        TextView ht_Teachernote = tb_main1.findViewById(R.id.ht_Teachernote);
        TextView tv_academic_yr = tb_main1.findViewById(R.id.tv_academic_yr);
        ImageView ic_back = tb_main1.findViewById(R.id.ic_back);
        ImageView drawer = tb_main1.findViewById(R.id.drawer);
        tv_academic_yr.setText("("+SharedClass.getInstance(getApplicationContext()).getAcademicYear()+")");

        school_title.setText(" Evolvu Teacher App");
        ht_Teachernote.setText("About Us");
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

        mDatabaseHelper = new DatabaseHelper(this);
        name = mDatabaseHelper.getName(1);
        dUrl = mDatabaseHelper.getURL(1);
        newUrl = mDatabaseHelper.getTURL(1);

        tv1=findViewById(R.id.tv1);
        tv2=findViewById(R.id.tv2);
        tv3=findViewById(R.id.tv3);
        tv4=findViewById(R.id.tv4);
        tv5=findViewById(R.id.tv5);
        tv6=findViewById(R.id.tv6);
        iv2=findViewById(R.id.iv2);
        iv3=findViewById(R.id.iv3);
        iv4=findViewById(R.id.iv4);
        emailat=findViewById(R.id.emailat);
        contactmail=findViewById(R.id.contactmail);



        emailat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String TO = "aceventuraservices@gmail.com";
                Intent email = new Intent(Intent.ACTION_SEND);
//                email.setData(Uri.parse("mailto:"));
                email.setDataAndType(Uri.parse(TO),"message/rfc822");
//                email.putExtra(Intent.EXTRA_EMAIL,TO);
                /*email.putExtra(Intent.EXTRA_SUBJECT, subject);
                email.putExtra(Intent.EXTRA_TEXT, message);*/

                //need this to prompts email client only
//                email.setType("message/rfc822");

                startActivity(Intent.createChooser(email, "Send Mail:"));
            }
        });


        contactmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String TO ="contact@aceventura.in";
                Intent email = new Intent(Intent.ACTION_SEND);
//                email.setData(Uri.parse("mailto:"));

//                email.putExtra(Intent.EXTRA_EMAIL,TO);
//              email.putExtra(Intent.EXTRA_EMAIL,"contact@aceventura.in");

                email.setDataAndType(Uri.parse(TO),"message/rfc822");
                //need this to prompts email client only
//                email.setType("message/rfc822");
//                email.setData();

                startActivity(Intent.createChooser(email, "Contact us"));
            }
        });

        iv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.facebook.com/AceVenturaSol/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        iv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://in.linkedin.com/company/aceventura-services";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        iv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*String url = "";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);*/
                Toast.makeText(AboutUs.this, "PlaystoreLink", Toast.LENGTH_SHORT).show();
            }
        });


        String logoUrl;
        if (name.equals("SACS")) {
            logoUrl = dUrl + "uploads/logo.png";
        } else {
            logoUrl = dUrl + "uploads/" + name + "/logo.png";
        }
        Log.e("LogoUrl", "Values:" + logoUrl);
        Glide.with(this).load(logoUrl)
                .thumbnail(0.5f)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target,
                                                boolean isFirstResource) {


                        if (name != null) {
                            switch (name) {
                                case "SACS":
                                    drawer.setBackgroundResource(R.drawable.newarnolds_logo);
                                    break;
                                case "SFSNE":
                                    drawer.setBackgroundResource(R.drawable.transparentsfsne);
                                    break;
                                case "SFSNW":
                                    drawer.setBackgroundResource(R.drawable.transparentsfsnw);
                                    break;
                                case "SJSKW":
                                    drawer.setBackgroundResource(R.drawable.sjskw);
                                    break;
                                case "SFSPUNE":
                                    drawer.setBackgroundResource(R.drawable.sfspune);
                                    break;
                            }
                        } else {
                            drawer.setBackgroundResource(R.drawable.evolvuteacer);
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target,
                                                   DataSource dataSource, boolean isFirstResource) {
                        //on load success
                        return false;
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(drawer);


        try {
            BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
            //

            View view = findViewById(R.id.bb_bookavailability);
            TextView supportEmail = view.findViewById(R.id.email);
            //---------------Support email--------------------------
            if (name != null) {
                String supportname = name.toLowerCase();

                supportEmail.setText("For app support email to : " + "support" + supportname + "@aceventura.in");
            } else {
                supportEmail.setText("For app support email to : " + "aceventuraservices@gmail.com");
                return;
            }
            bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
                @Override
                public void onTabSelected(@IdRes int tabId) {


                    if (tabId == R.id.tab_profile) {
                        Intent intent = new Intent(AboutUs.this, TeacherProfileActivity.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_calendar) {
                        Intent intent = new Intent(AboutUs.this, MyCalendar.class);
                        startActivity(intent);
                    }
                }
            });
            bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
                @Override
                public void onTabReSelected(int tabId) {


                    if (tabId == R.id.tab_profile) {
                        Intent intent = new Intent(AboutUs.this, TeacherProfileActivity.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_dashboard) {

                        Intent intent = new Intent(AboutUs.this, HomePageDrawerActivity.class);
                        startActivity(intent);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("bottomErrr", "wee" + e.getMessage());
        }

    }
}
