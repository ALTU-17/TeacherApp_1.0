package in.aceventura.evolvuschool.teacherapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;

import in.aceventura.evolvuschool.teacherapp.R;

public class ViewFullSizePdf extends AppCompatActivity {

    String url;
    PDFView pdfView;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pdf);
        pdfView=findViewById(R.id.pdfviewer);
        progressDialog=new ProgressDialog(this);
        progressDialog.show();
        Intent intent=getIntent();
        url=intent.getStringExtra("url");

        pdfView.fromAsset(url).load();

        progressDialog.dismiss();
        /*PhotoViewAttacher photoViewAttacher=new PhotoViewAttacher(imageView);
        photoViewAttacher.update();
*/
    }
}
