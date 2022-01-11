package in.aceventura.evolvuschool.teacherapp.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;

import in.aceventura.evolvuschool.teacherapp.R;


public class MyProfileACtivity extends AppCompatActivity {
    private ImageButton imgBtnPlus, imgBtnMinus;
    private LinearLayout linearLayout, l1, l2, l3;
    EditText edtadahr,edtADdress, edtPhnumber, edtEmailId, edtDOB, edtBldGrp, edtReligion, edtJoinigdate,
            edtDesigntion,edtExperience,edtSuboforded;
    private Spinner QualificationSpin, TrainingstatusSpin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile_activity);
        getIds();
        getProfile();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void getProfile() {
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void getIds() {
        imgBtnPlus = findViewById(R.id.imagePlus);
        edtadahr = findViewById(R.id.adharno);
        edtADdress = findViewById(R.id.address);
        edtPhnumber = findViewById(R.id.phonenumbermy);
        edtEmailId = findViewById(R.id.emailMy);
        edtDOB = findViewById(R.id.dateofbirth);
        edtBldGrp = findViewById(R.id.bloodgrp);
        edtReligion = findViewById(R.id.religion);
        edtJoinigdate = findViewById(R.id.joiningdate);
        edtDesigntion = findViewById(R.id.Designation);
        edtExperience = findViewById(R.id.experience);
        edtSuboforded = findViewById(R.id.subforDedBed);
        QualificationSpin=findViewById(R.id.professionquali);
        TrainingstatusSpin=findViewById(R.id.trainingstatus);

        l1 = findViewById(R.id.id1);
        l2 = findViewById(R.id.id2);
        l3 = findViewById(R.id.id3);
        linearLayout = findViewById(R.id.linearLayoutacdQualification);
        imgBtnMinus = findViewById(R.id.imageminus);

        imgBtnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgBtnPlus.setVisibility(View.GONE);
                imgBtnMinus.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.VISIBLE);
            }
        });

        imgBtnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgBtnMinus.setVisibility(View.GONE);
                imgBtnPlus.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.GONE);
            }
        });
    }

    private void getEdit() {
        l1.setBackgroundResource(R.color.white);
        l2.setBackgroundResource(R.color.white);
        l3.setBackgroundResource(R.color.white);
        edtadahr.setEnabled(true);
        edtADdress.setEnabled(true);
        edtPhnumber.setEnabled(true);
        edtEmailId.setEnabled(true);
        edtDOB.setEnabled(true);
        edtBldGrp.setEnabled(true);
        edtReligion.setEnabled(true);
        edtJoinigdate.setEnabled(true);
        edtDesigntion.setEnabled(true);
        edtExperience.setEnabled(true);
        edtSuboforded.setEnabled(true);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menuedit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menu) {
        switch (menu.getItemId()) {
            case R.id.menuedit1:
                getEdit();
                return true;

            default:
                return super.onOptionsItemSelected(menu);
        }
    }

}
