package in.aceventura.evolvuschool.teacherapp.activities;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import in.aceventura.evolvuschool.teacherapp.R;


public class BookReqACtivity extends AppCompatActivity {
private FloatingActionButton fabbookReqbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_req_activity);
        fabbookReqbtn= findViewById(R.id.fabbookReq);
        fabbookReqbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(BookReqACtivity.this,CreateBookReqACtivity.class);
                startActivity(intent);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
