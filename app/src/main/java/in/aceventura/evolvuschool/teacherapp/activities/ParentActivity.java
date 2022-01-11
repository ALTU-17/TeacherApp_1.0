package in.aceventura.evolvuschool.teacherapp.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

import in.aceventura.evolvuschool.teacherapp.R;


public class ParentActivity extends AppCompatActivity {

    private TextView edtet_fname,edtoccupation,edtofcaddress,edttelephone,edtMobNo,edtEmailid,edtAdharno,edtMothernm,
    edtmotheret_moccupation,edtmotherofcAddress,edtmothertelephone,edtmothermobile,edtmotheremailid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getIds();
        getParentInfo();

    }

    private void getParentInfo() {
    }

    private void getIds() {
        edtet_fname=findViewById(R.id.et_fname);
        edtoccupation=findViewById(R.id.et_fname);
        edtofcaddress=findViewById(R.id.et_fname);
        edttelephone=findViewById(R.id.et_fname);
        edtMobNo=findViewById(R.id.et_fname);
        edtEmailid=findViewById(R.id.et_fname);
        edtAdharno=findViewById(R.id.et_fname);
        edtMothernm=findViewById(R.id.et_fname);
        edtmotheret_moccupation=findViewById(R.id.et_fname);
        edtmotherofcAddress=findViewById(R.id.et_fname);
        edtmothertelephone=findViewById(R.id.et_fname);
        edtmothermobile=findViewById(R.id.et_fname);
        edtmotheremailid=findViewById(R.id.et_fname);
    }

    @Override
    public boolean onSupportNavigateUp() {

        onBackPressed();
        return true;
    }
}
