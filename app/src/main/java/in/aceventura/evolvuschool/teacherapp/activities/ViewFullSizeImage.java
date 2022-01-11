package in.aceventura.evolvuschool.teacherapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;


import in.aceventura.evolvuschool.teacherapp.R;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ViewFullSizeImage extends AppCompatActivity {

    String url;
    ImageView imageView;
    ProgressDialog progressDialog;
    PhotoViewAttacher photoViewAttacher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_full_size_image);
        imageView=findViewById(R.id.imageviewview);
        progressDialog=new ProgressDialog(this);
        progressDialog.show();
        progressDialog.setTitle("Loading...");
        Intent intent=getIntent();
        url=intent.getStringExtra("url");

        //Picasso
        Picasso.get()
                .load(url)
                .fit()
                .centerInside()
//                .placeholder(R.drawable.loading_placeholder)
                .error(R.drawable.error_placeholder)
                .into(imageView)
        ;

        progressDialog.dismiss();
        photoViewAttacher=new PhotoViewAttacher(imageView);
        photoViewAttacher.update();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ViewFullSizeImage.this.finish();

    }
}
