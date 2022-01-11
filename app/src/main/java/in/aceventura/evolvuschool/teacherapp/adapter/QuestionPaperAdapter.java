package in.aceventura.evolvuschool.teacherapp.adapter;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

import in.aceventura.evolvuschool.teacherapp.R;
import in.aceventura.evolvuschool.teacherapp.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.teacherapp.pojo.QuesPaperModel;

/**
 * Created by "Manoj Waghmare" on 19,Aug,2020
 **/
public class QuestionPaperAdapter extends RecyclerView.Adapter<QuestionPaperAdapter.ViewHolder> {

    private Context context;
    private List<QuesPaperModel> qpList;
    private DatabaseHelper mDatabaseHelper;
    private String newUrl, name, dUrl, download_url, question_bank_id,question_bank_type;

    //constructor
    public QuestionPaperAdapter(Context context, List<QuesPaperModel> qpList) {
        this.context = context;
        this.qpList = qpList;
        mDatabaseHelper = new DatabaseHelper(context);
        name = mDatabaseHelper.getName(1);
        newUrl = mDatabaseHelper.getURL(1);
        dUrl = mDatabaseHelper.getPURL(1);

        if (name == null || name.equals("")) {
            name = mDatabaseHelper.getName(1);
            newUrl = mDatabaseHelper.getURL(1);
            dUrl = mDatabaseHelper.getPURL(1);
        }
    }

    //setting layout file
    @NonNull
    @Override
    public QuestionPaperAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.ques_paper_list_item, parent, false));
    }

    //Logic & Setting the values to UI
    @Override
    public void onBindViewHolder(@NonNull QuestionPaperAdapter.ViewHolder holder, final int position) {
        if (qpList.get(position).getImage_name().contains(".PDF") || qpList.get(position).getImage_name().contains(".pdf")) {
            holder.iv_qp.setBackground(ContextCompat.getDrawable(context, R.drawable.file_pdf));
        }
        else if (qpList.get(position).getImage_name().contains(".doc") || qpList.get(position).getImage_name().contains(".docx") || qpList.get(position).getImage_name().contains(".txt")) {
            holder.iv_qp.setBackground(ContextCompat.getDrawable(context, R.drawable.file_word));
        }
        else {
            holder.iv_qp.setBackground(ContextCompat.getDrawable(context, R.drawable.file_image));
        }

        holder.tv_qpName.setText(qpList.get(position).getImage_name());

        question_bank_type = qpList.get(position).getQuestion_bank_type();

        holder.qp_download.setOnClickListener(v -> {
            String quesPaperName = qpList.get(position).getImage_name();
            String qb_Id = qpList.get(position).getQuestion_bank_id();
            String download_url = qpList.get(position).getUrl();
            String ques_id = qpList.get(position).getUp_id();
            QuestionPaperAdapter.this.downloadQuestionPaper(quesPaperName, qb_Id, download_url,
                    question_bank_type, ques_id);
            Toast.makeText(context, "Downloading Question Paper  " + quesPaperName, Toast.LENGTH_LONG).show();
        });
    }

    private void downloadQuestionPaper(String quesPaperName, String qb_Id, String download_url,
                                       String question_bank_type, String ques_id) {
        if (isReadStorageAllowed()) {
            DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            //https://sfs.evolvu.in/onlineexam/test/uploads/SFSNW/uploaded_ques_paper/169/544052.jpeg
            Uri uri;
            if (question_bank_type.equals("mcq")) {
//                https://sfs.evolvu.in/onlineexam/test/uploads/SACS/mcq_ques_bank/2/null/onlineexam.png
                uri = Uri.parse(download_url + name + "/mcq_ques_bank/" + qb_Id + "/" + ques_id + "/" + quesPaperName.trim());
            }
            else {

                uri = Uri.parse(download_url + name + "/uploaded_ques_paper/" + qb_Id + "/" + quesPaperName.trim());
            }

            System.out.println("Q_PAPER_URL - " + uri.toString());
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setMimeType("application/");
            request.allowScanningByMediaScanner();

            String folder = "Evolvuschool/Teacher";
            request.setDestinationInExternalPublicDir(folder, quesPaperName);

            File directory = new File(folder);

            //If directory not exists create one....
            if (!directory.exists()) {
                directory.mkdirs();
            }
            dm.enqueue(request);
            return;

        }
        requestStoragePermission();
    }

    private boolean isReadStorageAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(context,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        //If permission is granted returning true
        return result == PackageManager.PERMISSION_GRANTED;

        //If permission is not granted returning false
    }

    //Requesting permission
    private void requestStoragePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
            Toast.makeText(context, "To Download Question Paper Please Allow the Storage Permission",
                    Toast.LENGTH_LONG).show();
        }

        //And finally ask for the permission
        int STORAGE_PERMISSION_CODE = 23;
        ActivityCompat.requestPermissions((Activity) context,
                new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    //setting count
    @Override
    public int getItemCount() {
        return qpList.size();
    }

    /*//This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
    grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //Displaying a toast
                // Toast.makeText(this,"Permission granted now you can read the storage",Toast.LENGTH_LONG).show();

            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(context, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }*/

    @Override
    public long getItemId(int position) {
        return position;
    }

    //Binding the views with components from layout file
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_qp;
        TextView tv_qpName;
        LinearLayout qp_download;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_qp = itemView.findViewById(R.id.iv_qp);
            tv_qpName = itemView.findViewById(R.id.tv_qpName);
            qp_download = itemView.findViewById(R.id.qp_download);
        }
    }
}

