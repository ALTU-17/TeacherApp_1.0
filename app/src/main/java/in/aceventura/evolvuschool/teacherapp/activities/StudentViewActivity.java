package in.aceventura.evolvuschool.teacherapp.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import in.aceventura.evolvuschool.teacherapp.R;


public class StudentViewActivity extends AppCompatActivity {

    private TextView edtFname,edtMName,edtLname,edtDOB,edtDOA,edtGRN,edtAdharno,edtHouse,edtclass,edtdiv,
            edtroll,edtgender,edtbldgrp,edtnationality,edtaddress,edtcity,edtstate,edtpincode,edtreligion,edtcaste,
    edtcategory,edtemergencynm,edtemergencyaddress,edtemergencycontact,edttansportno,edtvehicleno,edtallergies,edtht,edtwt;

    private CheckBox checkBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_profile);
        getId();
        getStudInfo();
         getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void getStudInfo() {
    }

    private void getId() {

        //put R.id.proper this task remaining
        
        edtFname=findViewById(R.id.et_firstname);
        edtMName=findViewById(R.id.et_middlename);
        edtLname=findViewById(R.id.et_lastname);
        edtDOB=findViewById(R.id.et_dob);
        edtDOA=findViewById(R.id.et_doa);
        edtGRN=findViewById(R.id.et_grnno);
        edtAdharno=findViewById(R.id.et_stuaadhaarno);
        edtHouse=findViewById(R.id.edthouse);
        edtclass=findViewById(R.id.et_class);
        edtdiv=findViewById(R.id.et_division);
        edtroll=findViewById(R.id.et_rollno);
        edtgender=findViewById(R.id.gender);
        edtbldgrp=findViewById(R.id.bldgrpp);
        edtnationality=findViewById(R.id.et_nationality);
        edtaddress=findViewById(R.id.et_address);
        edtcity=findViewById(R.id.et_city);
        edtstate=findViewById(R.id.et_state);
        edtpincode=findViewById(R.id.et_pincode);
        edtreligion=findViewById(R.id.et_religion);
        edtcaste=findViewById(R.id.et_caste);
        edtcategory=findViewById(R.id.et_category);
        edtemergencynm=findViewById(R.id.et_emername);
        edtemergencyaddress=findViewById(R.id.et_emeradd);
        checkBox=findViewById(R.id.eAddressCheckbox);
        edtemergencycontact=findViewById(R.id.et_emercontact);
        edttansportno=findViewById(R.id.tpmode);
        edtvehicleno=findViewById(R.id.vehiclno);
        edtallergies=findViewById(R.id.et_allergy);
        edtht=findViewById(R.id.et_height);
        edtwt=findViewById(R.id.et_weight);

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String a=edtaddress.getText().toString();
                edtemergencyaddress.setText(a);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.parentinfo, menu);
        return true;

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.parent) {
            Intent intent = new Intent(StudentViewActivity.this, ParentActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
