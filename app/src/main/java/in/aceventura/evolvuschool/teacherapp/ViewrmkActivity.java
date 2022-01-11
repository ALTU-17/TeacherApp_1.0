
package in.aceventura.evolvuschool.teacherapp;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import in.aceventura.evolvuschool.teacherapp.activities.RemarksActivity;


public class ViewrmkActivity extends AppCompatActivity {
TextView txtdesc;
    String dsc;
    Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewrmk);
        back=findViewById(R.id.backbtn);
        txtdesc= findViewById(R.id.desc);
        Intent intent=getIntent();
        dsc=intent.getStringExtra("desc");
        txtdesc.setText(dsc);
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(ViewrmkActivity.this,RemarksActivity.class);
                startActivity(intent1);
                finish();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(ViewrmkActivity.this, RemarksActivity.class);
        startActivity(intent);
        finish();

    }
}
