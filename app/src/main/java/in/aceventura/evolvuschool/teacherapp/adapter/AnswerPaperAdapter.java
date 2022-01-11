package in.aceventura.evolvuschool.teacherapp.adapter;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
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
import in.aceventura.evolvuschool.teacherapp.pojo.AnsPaperModel;

/**
 * Created by "Manoj Waghmare" on 19,Aug,2020
 **/
public class AnswerPaperAdapter extends RecyclerView.Adapter<AnswerPaperAdapter.ViewHolder> {

    String newUrl, name;
    ProgressDialog progressDialog;
    private Context context;
    private List<AnsPaperModel> ansList;


    //constructor
    public AnswerPaperAdapter(Context context, List<AnsPaperModel> ansList) {
        this.context = context;
        this.ansList = ansList;
        DatabaseHelper mDatabaseHelper = new DatabaseHelper(context);
        name = mDatabaseHelper.getName(1);
        newUrl = mDatabaseHelper.getURL(1);
        String dUrl = mDatabaseHelper.getPURL(1);
        progressDialog = new ProgressDialog(context);

        if (name == null || name.equals("")) {
            name = mDatabaseHelper.getName(1);
            newUrl = mDatabaseHelper.getURL(1);
            dUrl = mDatabaseHelper.getPURL(1);
        }
    }

    //setting layout file
    @NonNull
    @Override
    public AnswerPaperAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.ans_paper_list_item, parent, false));
    }

    //Logic & Setting the values to UI
    @Override
    public void onBindViewHolder(@NonNull AnswerPaperAdapter.ViewHolder holder, final int position) {
        String question_bank_type = ansList.get(position).getQuestion_bank_type();
        if (ansList.get(position).getImage_name().contains(".PDF") || ansList.get(position).getImage_name().contains(".pdf")) {
            holder.iv_qp.setBackground(ContextCompat.getDrawable(context, R.drawable.file_pdf));
        }
        else if (ansList.get(position).getImage_name().contains(".doc") || ansList.get(position).getImage_name().contains(".docx") || ansList.get(position).getImage_name().contains(".txt")) {
            holder.iv_qp.setBackground(ContextCompat.getDrawable(context, R.drawable.file_word));
        }
        else {
            holder.iv_qp.setBackground(ContextCompat.getDrawable(context, R.drawable.file_image));
        }


        holder.tv_qpName.setText(ansList.get(position).getImage_name());

        holder.qp_download.setOnClickListener(v -> {
            String ansPaperName = ansList.get(position).getImage_name();
            String ap_Id = ansList.get(position).getQuestion_bank_id();
            String download_url = ansList.get(position).getUrl();
            String ans_id = ansList.get(position).getUploaded_qp_id();

            downloadAnswerPaper(ansPaperName, ap_Id, download_url, question_bank_type, ans_id);
            Toast.makeText(context, "Downloading AnswerPaper  " + ansPaperName, Toast.LENGTH_LONG).show();
        });
    }
    private void downloadAnswerPaper(String ansPaperName, String ap_Id, String download_url,
                                     String question_bank_type, String ans_id) {
        if (isReadStorageAllowed()) {
            DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
//            https://sfs.evolvu.in/onlineexam/test/uploads/SFSNW/uploaded_ans_paper/169/544604.jpeg
            Uri uri;
            if (question_bank_type.equals("mcq")) {
                uri =
                        Uri.parse(download_url + name + "/" + "mcq_answers/" + ap_Id + "/" + ans_id + "/" + ansPaperName.trim());
            }
            else {
                uri =
                        Uri.parse(download_url + name + "/" + "uploaded_ans_paper/" + ap_Id + "/" + ansPaperName.trim());
            }
//            uri = Uri.parse(download_url + name + "/" + "uploaded_ans_paper/" + ap_Id + "/" + ansPaperName
//            .trim());
            System.out.println("A_PAPER_URL - " + uri.toString());
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setMimeType("application/");
            request.allowScanningByMediaScanner();

            String folder = "Evolvuschool/Teacher";
            request.setDestinationInExternalPublicDir(folder, ansPaperName);

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

    //setting count
    @Override
    public int getItemCount() {
        return ansList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
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

    //Binding the views with components from layout file
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_qp, delete;
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
