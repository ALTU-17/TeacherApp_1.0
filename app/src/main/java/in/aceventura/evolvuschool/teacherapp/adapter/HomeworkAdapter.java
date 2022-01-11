package in.aceventura.evolvuschool.teacherapp.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.aceventura.evolvuschool.teacherapp.R;
import in.aceventura.evolvuschool.teacherapp.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.teacherapp.activities.EditHomeworkActivity;
import in.aceventura.evolvuschool.teacherapp.activities.HomeworkActivity;
import in.aceventura.evolvuschool.teacherapp.activities.HomeworkViewedByActivity;
import in.aceventura.evolvuschool.teacherapp.activities.RequestHandler;
import in.aceventura.evolvuschool.teacherapp.activities.SharedClass;
import in.aceventura.evolvuschool.teacherapp.activities.ViewHomeworkActivity;
import in.aceventura.evolvuschool.teacherapp.pojo.HomeworkPojo;


/**
 * Created by Admin on 7/5/2018.
 */

public class HomeworkAdapter extends RecyclerView.Adapter<HomeworkAdapter.Datahold> {
    SharedClass sharedClass;
    private String name;
    private String newUrl;
    private Context context;
    private ArrayList<HomeworkPojo> homeworkPojoArrayList;


    public HomeworkAdapter(Context context, ArrayList<HomeworkPojo> homeworkPojoArrayList) {
        this.context = context;
        this.homeworkPojoArrayList = homeworkPojoArrayList;

        DatabaseHelper mDatabaseHelper = new DatabaseHelper(context);
        name = mDatabaseHelper.getName(1);
        String dUrl = mDatabaseHelper.getURL(1);
        newUrl= mDatabaseHelper.getTURL(1);

        if (name == null || name.equals("")) {
            name = mDatabaseHelper.getName(1);
            dUrl = mDatabaseHelper.getURL(1);
            newUrl = mDatabaseHelper.getTURL(1);
        }
    }

    @NonNull
    @Override
    public Datahold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.homework_adapter_lay,parent,false);
        return new Datahold(view);
    }
    private void closeSubMenusFab(){
    }
    @Override
    public void onBindViewHolder(final Datahold holder, final int position) {
        HomeworkPojo homeworkPojo = homeworkPojoArrayList.get(position);
        holder.txthome_subject.setText(homeworkPojo.getSubjectnm());
        holder.txthomeclass.setText(homeworkPojo.getClassname()+" "+ homeworkPojo.getSectionname());
        String desc=homeworkPojoArrayList.get(position).getDescription();
        holder.noteDesc.setText(desc);

        //published On

        String cdate = homeworkPojoArrayList.get(position).getAssigndate();
        String pdate = homeworkPojoArrayList.get(position).getPublishdate();


        if (pdate != null && !pdate.isEmpty() && !pdate.equals("null")){
            holder.tvdate.setVisibility(View.GONE);
            holder.txtassigndate.setText(pdate);
            holder.tvCdate.setVisibility(View.GONE);
        }
        else{

            holder.txtassigndate.setText(cdate);
            holder.tvCdate.setVisibility(View.GONE);
            holder.tvdate.setVisibility(View.GONE);

        }

        //submit On
        holder.txthomesubdate.setText(homeworkPojoArrayList.get(position).getSubmissiondate());

        String publishStatus = homeworkPojo.getPublish();
        if(publishStatus.equals("Y")){
            holder.imgview.setVisibility(View.VISIBLE);
            holder.viewdBy.setVisibility(View.VISIBLE);

            holder.comment_count_layout.setVisibility(View.VISIBLE);// TODO: 16-07-2020

            if(homeworkPojoArrayList.get(position).getParent_comment_count().equals("0")){
                holder.tv_parent_comment.setVisibility(View.GONE);
                holder.comment_count.setVisibility(View.GONE);
            }else{
                holder.tv_parent_comment.setVisibility(View.VISIBLE);
                holder.comment_count.setText(homeworkPojoArrayList.get(position).getParent_comment_count());// TODO: 16-07-2020
            }


            holder.threedots1.setVisibility(View.GONE);

            holder.imgview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String teacherid = homeworkPojoArrayList.get(position).getTeacherid();
                    String classid = homeworkPojoArrayList.get(position).getClassid();
                    String academicYr = homeworkPojoArrayList.get(position).getAcdyr();
                    String sectionid = homeworkPojoArrayList.get(position).getSectionid();
                    String subjectid = homeworkPojoArrayList.get(position).getSubjectid();
                    String desc = homeworkPojoArrayList.get(position).getDescription();
                    String date = homeworkPojoArrayList.get(position).getAssigndate();
                    String enddate = homeworkPojoArrayList.get(position).getSubmissiondate();
                    String hmkid = homeworkPojoArrayList.get(position).getHomeworkid();
                    Intent intent = new Intent(context, ViewHomeworkActivity.class);
                    intent.putExtra("teacherid", teacherid);
                    intent.putExtra("hmkid", hmkid);
                    intent.putExtra("classname", classid);
                    intent.putExtra("academicYr", academicYr);
                    intent.putExtra("sectionname", sectionid);
                    intent.putExtra("subjectname", subjectid);
                    intent.putExtra("desc", desc);
                    intent.putExtra("date", date);
                    intent.putExtra("enddate",enddate);
                    context.startActivity(intent);
                    ((Activity)context).finish();

                }
            });

            //viewed by icon
            holder.viewdBy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String class_id = homeworkPojoArrayList.get(position).getClassid();
                    String section_id = homeworkPojoArrayList.get(position).getSectionid();
                    String desc = homeworkPojoArrayList.get(position).getDescription();
                    String hmkid = homeworkPojoArrayList.get(position).getHomeworkid();

                    Intent intent = new Intent(context, HomeworkViewedByActivity.class);
                    intent.putExtra("hw_desc", desc);
                    intent.putExtra("hw_id", hmkid);
                    intent.putExtra("class_id", class_id);
                    intent.putExtra("section_id", section_id);
                    context.startActivity(intent);
                }
            });

        }else{
            holder.imgview.setVisibility(View.GONE);
            holder.viewdBy.setVisibility(View.GONE);
            holder.threedots1.setVisibility(View.VISIBLE);

        }

        if(homeworkPojoArrayList.get(position).getExpanded()){
            Log.d("EXPANDED", ""+homeworkPojoArrayList.get(position).getExpanded());
            holder.fabdelete.setVisibility(View.VISIBLE);
            holder.fabpublish.setVisibility(View.VISIBLE);
            holder.fabedit.setVisibility(View.VISIBLE);
        }else{
            Log.d("EXPANDED", ""+homeworkPojoArrayList.get(position).getExpanded());
            holder.fabdelete.setVisibility(View.GONE);
            holder.fabpublish.setVisibility(View.GONE);
            holder.fabedit.setVisibility(View.GONE);

        }


        holder.threedots1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(homeworkPojoArrayList.get(position).getExpanded()){
                    Log.d("EXPANDED", ""+homeworkPojoArrayList.get(position).getExpanded());
                    homeworkPojoArrayList.get(position).setExpanded(false);
                    notifyDataSetChanged();
                }else{
                    Log.d("EXPANDED", ""+homeworkPojoArrayList.get(position).getExpanded());
                    homeworkPojoArrayList.get(position).setExpanded(true);
                    notifyDataSetChanged();

                }
            }
        });


        holder.fabedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String teacherid = homeworkPojoArrayList.get(position).getTeacherid();
                String classname = homeworkPojoArrayList.get(position).getClassname();
                String academicYr = homeworkPojoArrayList.get(position).getAcdyr();
                String sectionname = homeworkPojoArrayList.get(position).getSectionname();
                String subjectname = homeworkPojoArrayList.get(position).getSubjectnm();
                String desc = homeworkPojoArrayList.get(position).getDescription();
                String startdate = homeworkPojoArrayList.get(position).getAssigndate();
                String enddate = homeworkPojoArrayList.get(position).getSubmissiondate();
                String class_id = homeworkPojoArrayList.get(position).getClassid();
                String section_id = homeworkPojoArrayList.get(position).getSectionid();
                String subject_id = homeworkPojoArrayList.get(position).getSubjectid();

                String hmkid=homeworkPojoArrayList.get(position).getHomeworkid();

                Intent intent=new Intent(context, EditHomeworkActivity.class);
                intent.putExtra("classname1", classname);
                intent.putExtra("subjectname1", subjectname);
                intent.putExtra("sectionname1", sectionname);
                intent.putExtra("HOMEWORKID", hmkid);
                intent.putExtra("CLASSID", class_id);
                intent.putExtra("SECTIONID", section_id);
                intent.putExtra("SUBJECTID", subject_id);
                intent.putExtra("HOMEWORKDESC", desc);
                intent.putExtra("SUBMITDATE", enddate);
                intent.putExtra("startdate", startdate);
                intent.putExtra("teacherid", teacherid);
                intent.putExtra("academicyr", academicYr);
                context.startActivity(intent);
                ((Activity)context).finish();

            }
        });

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }
        };


        holder.fabpublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Do you want to publish this homework?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String homeworkid=homeworkPojoArrayList.get(position).getHomeworkid();
                        String teacherid=homeworkPojoArrayList.get(position).getTeacherid();
                        String acdyr=homeworkPojoArrayList.get(position).getAcdyr();
                        String classid=homeworkPojoArrayList.get(position).getClassid();
                        String sectionid=homeworkPojoArrayList.get(position).getSectionid();
                        String smid=homeworkPojoArrayList.get(position).getSubjectid();
                        String enddate=homeworkPojoArrayList.get(position).getSubmissiondate();
                        String desc=homeworkPojoArrayList.get(position).getDescription();
                        holder.fabpublish.setVisibility(View.GONE);
                        holder.fabedit.setVisibility(View.GONE);
                        holder.fabdelete.setVisibility(View.GONE);
                        holder.imgadd.setVisibility(View.GONE);
                        holder.imgview.setVisibility(View.VISIBLE);
                        publishRemark(homeworkid,teacherid,acdyr,classid,sectionid,smid,enddate,desc);

                        view.refreshDrawableState();
                        holder.btnView.setVisibility(View.VISIBLE);
                        //visibilityButton.setVisibility(View.VISIBLE);
                    }

                });

                //if response is negative nothing is being done
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                //creating and displaying the alert dialog
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        holder.fabdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String hmkid=homeworkPojoArrayList.get(position).getHomeworkid();
                String reg_id=homeworkPojoArrayList.get(position).getTeacherid();
                String academic_yr=homeworkPojoArrayList.get(position).getAcdyr();
                String classid=homeworkPojoArrayList.get(position).getClassid();
                String sectionid=homeworkPojoArrayList.get(position).getSectionid();
                String subjectid=homeworkPojoArrayList.get(position).getSubjectid();
                removeRemark(position,hmkid,reg_id,academic_yr,classid,sectionid,subjectid);
                view.refreshDrawableState();

            }
        });

        holder.btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String teacherid = homeworkPojoArrayList.get(position).getTeacherid();
                String classid = homeworkPojoArrayList.get(position).getClassid();
                String academicYr = homeworkPojoArrayList.get(position).getAcdyr();
                String sectionid = homeworkPojoArrayList.get(position).getSectionid();
                String subjectid = homeworkPojoArrayList.get(position).getSubjectid();
                String desc = homeworkPojoArrayList.get(position).getDescription();
                String date = homeworkPojoArrayList.get(position).getAssigndate();
                String enddate = homeworkPojoArrayList.get(position).getSubmissiondate();
                String hmkid = homeworkPojoArrayList.get(position).getHomeworkid();
                Intent intent = new Intent(context, ViewHomeworkActivity.class);

                intent.putExtra("teacherid", teacherid);
                intent.putExtra("hmkid", hmkid);
                intent.putExtra("classname", classid);
                intent.putExtra("academicYr", academicYr);
                intent.putExtra("sectionname", sectionid);
                intent.putExtra("subjectname", subjectid);
                intent.putExtra("desc", desc);
                intent.putExtra("date", date);
                intent.putExtra("enddate",enddate);
                context.startActivity(intent);
                ((Activity)context).finish();

            }
        });


    }


    private void removeRemark(final int position, final String hmkid, final String reg_id,final String acayr,final String classid,final String sectionid,final String subid) {
        //Creating an alert dialog to confirm the deletion
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Do you want to Delete Homework?");

        //if the response is positive in the alert
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, newUrl+"AdminApi/"+"homework", response -> {
                    if(response==null)
                    {
                        Toast.makeText(context,"Can not Delete Record",Toast.LENGTH_LONG).show();
                    }
                    Toast.makeText(context,"Homework Deleted Successfully",Toast.LENGTH_SHORT).show();
                }, error -> Toast.makeText(context,error.toString(), Toast.LENGTH_LONG).show()){
                    @Override
                    protected Map<String,String> getParams(){
                        Map<String,String> params = new HashMap<>();
                        params.put("homework_id",hmkid);
                        params.put("publish","N");
                        params.put("operation","delete");
                        params.put("login_type","T");
                        params.put("academic_yr ",acayr);
                        params.put("teacher_id ",reg_id);
                        params.put("class_id ",classid);
                        params.put("section_id ",sectionid);
                        params.put("sm_id ",subid);
                        params.put("short_name",name);
                        return params;
                    }

                };
                RequestHandler.getInstance(context).addToRequestQueue(stringRequest);

                //removing the item
                homeworkPojoArrayList.remove(position);
                //reloading the list
                notifyDataSetChanged();
            }

        });

        //if response is negative nothing is being done
        builder.setNegativeButton("No", (dialogInterface, i) -> {

        });

        //creating and displaying the alert dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void publishRemark(final String homeworkid, final String teacherid, final String acdyr, final String classid, final String sectionid, final String smid, final String enddate, final String desc) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, newUrl+"AdminApi/"+"homework",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //Toast.makeText(activity,response,Toast.LENGTH_LONG).show();
                        if(response==null)
                        {
                            Toast.makeText(context,"Failed to Publish Homework",Toast.LENGTH_LONG).show();
                        }
                        Toast.makeText(context,"Homework Published Successfully",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(context, HomeworkActivity.class);
                        context.startActivity(intent);
                        ((Activity)context).finish();
                        //Toast.makeText(StudentProfile.this,response,Toast.LENGTH_LONG).show();
                    }
                }, error -> Toast.makeText(context,"Error occurred: Try Again  "+ error.toString(), Toast.LENGTH_LONG).show()){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                //params.put(SharedPrefManager.KEY_STUDENT_ID,studentid);

                params.put("homework_id",homeworkid);
                params.put("teacher_id",teacherid);
                params.put("operation","publish");
                params.put("login_type","T");
                params.put("publish","Y");
                params.put("class_id",classid);
                params.put("section_id",sectionid);
                params.put("sm_id",smid);
                params.put("academic_yr",acdyr);
                params.put("end_date",enddate);
                params.put("description",desc);
                params.put("short_name",name);

                return params;

            }

        };
        RequestHandler.getInstance(context).addToRequestQueue(stringRequest);
        //RequestQueue requestQueue = Volley.newRequestQueue(this);
        //requestQueue.add(stringRequest);
    }
    public void filterlist(ArrayList<HomeworkPojo> filtername) {
        this.homeworkPojoArrayList=filtername;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return homeworkPojoArrayList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class Datahold extends RecyclerView.ViewHolder {
        TextView txthome_subject,txthomesubdate,txthomeclass,txtassigndate,txtpublishhmkview,noteDesc,tvdate,tvCdate,comment_count,tv_parent_comment;
        ImageView imgadd,fabdelete,fabedit,fabpublish,imgview,viewdBy;
        private Button btnView;
        LinearLayout threedots1,comment_count_layout;

        Datahold(View itemView) {
            super(itemView);
            noteDesc=itemView.findViewById(R.id.noteDesc);
            tvCdate=itemView.findViewById(R.id.tvCdate);
            tvdate=itemView.findViewById(R.id.tvdate);
            threedots1=itemView.findViewById(R.id.threedots1);
            txthome_subject=itemView.findViewById(R.id.home_subject);
            txtassigndate=itemView.findViewById(R.id.homeassign_edate);
            txthomeclass=itemView.findViewById(R.id.home_class);
            txthomesubdate=itemView.findViewById(R.id.homesubmit_edate);
            Button btnEdit = itemView.findViewById(R.id.btn_edit_home);
            Button btnDelete = itemView.findViewById(R.id.btn_delete_home);
            btnView = itemView.findViewById(R.id.btn_visibility_home);
            Button btnPublish = itemView.findViewById(R.id.btn_publish_home);
            imgadd=itemView.findViewById(R.id.fabadd);
            imgview=itemView.findViewById(R.id.fabviewhmk);
            fabpublish=itemView.findViewById(R.id.fabpublish);
            fabdelete=itemView.findViewById(R.id.fabdelete);
            fabedit=itemView.findViewById(R.id.fabedit);
            viewdBy = itemView.findViewById(R.id.viewdBy);
            comment_count_layout = itemView.findViewById(R.id.comment_count_layout);
            comment_count = itemView.findViewById(R.id.comment_count);
            tv_parent_comment = itemView.findViewById(R.id.tv_parent_comment);

        }
    }
}
