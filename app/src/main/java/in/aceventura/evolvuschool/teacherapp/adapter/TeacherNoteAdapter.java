package in.aceventura.evolvuschool.teacherapp.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import in.aceventura.evolvuschool.teacherapp.R;
import in.aceventura.evolvuschool.teacherapp.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.teacherapp.activities.EditTeacherNoteACtivity;
import in.aceventura.evolvuschool.teacherapp.activities.RequestHandler;
import in.aceventura.evolvuschool.teacherapp.activities.TeacherNoteViewedByActivity;
import in.aceventura.evolvuschool.teacherapp.activities.TeachersNoteACtivity;
import in.aceventura.evolvuschool.teacherapp.activities.ViewTeacherNoteActivity;
import in.aceventura.evolvuschool.teacherapp.pojo.TeacherNoteePojo;

public class TeacherNoteAdapter extends RecyclerView.Adapter<TeacherNoteAdapter.DataTeacher> {
    private Context context;
    private ArrayList<TeacherNoteePojo> teacherNoteArrayList;
    private String name;
    private String newUrl;

    public TeacherNoteAdapter(Context context, ArrayList<TeacherNoteePojo> teacherNoteArrayList) {
        this.context = context;
        this.teacherNoteArrayList = teacherNoteArrayList;

        DatabaseHelper mDatabaseHelper = new DatabaseHelper(context);
        name = mDatabaseHelper.getName(1);
        newUrl = mDatabaseHelper.getTURL(1);

        if (name == null || name.equals("")) {
            name = mDatabaseHelper.getName(1);
            newUrl = mDatabaseHelper.getTURL(1);
        }
    }

    @Override
    public DataTeacher onCreateViewHolder(ViewGroup parent, int viewType) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_techernote, parent, false);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        return new DataTeacher(view);
    }

    @Override
    public void onBindViewHolder(final DataTeacher holder, final int position) {

        TeacherNoteePojo teacherNote = teacherNoteArrayList.get(position);
        String a = teacherNoteArrayList.get(position).getSubjectname();
        if (a.equals("null")) {
            holder.txtnote_subject.setText("");
            holder.vetical3.setVisibility(View.GONE);
        } else {
            holder.txtnote_subject.setText(teacherNoteArrayList.get(position).getSubjectname());
            holder.vetical3.setVisibility(View.VISIBLE);
        }
        holder.note_tdesc.setText(teacherNoteArrayList.get(position).getDescription());
        String pubdate = teacherNoteArrayList.get(position).getpDate();
        String cdate = teacherNoteArrayList.get(position).getDate();


        if (pubdate != null && !pubdate.isEmpty() && !pubdate.equals("null")) {
            SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd");
            Date newDate = null;
            try {
                newDate = spf.parse(pubdate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            spf = new SimpleDateFormat("dd-MM-yyyy");
            pubdate = spf.format(newDate);

            holder.tvpub.setVisibility(View.GONE);
            holder.vetical1.setVisibility(View.VISIBLE);
            holder.txtedate.setVisibility(View.VISIBLE);
            holder.txtedate.setText(pubdate);
            holder.tvcdate.setVisibility(View.GONE);
        } else {
            holder.tvcdate.setVisibility(View.GONE);
            holder.txtedate.setText(cdate);
            holder.txtedate.setVisibility(View.VISIBLE);
            holder.tvpub.setVisibility(View.GONE);
            holder.vetical1.setVisibility(View.VISIBLE);
        }

        holder.txtnoteclass.setText(teacherNoteArrayList.get(position).getClassname() + " " + teacherNoteArrayList.get(position).getSectionname());

        if (teacherNoteArrayList.get(position).getExpanded()) {
            Log.d("EXPANDED", "" + teacherNoteArrayList.get(position).getExpanded());
            holder.imgpublish.setVisibility(View.VISIBLE);
            holder.imgdelete.setVisibility(View.VISIBLE);
            holder.imgedit.setVisibility(View.VISIBLE);
        } else {
            Log.d("EXPANDED", "" + teacherNoteArrayList.get(position).getExpanded());
            holder.imgpublish.setVisibility(View.GONE);
            holder.imgedit.setVisibility(View.GONE);
            holder.imgdelete.setVisibility(View.GONE);

        }

        String publishStatus = teacherNote.getPublish();
        if (publishStatus.equals("Y")) {
            holder.threedots.setVisibility(View.GONE);
            holder.imgView.setVisibility(View.VISIBLE);
            holder.viewedBy.setVisibility(View.VISIBLE);

            holder.imgView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String teacherid = teacherNoteArrayList.get(position).getTeacher_id();
                    String classname = teacherNoteArrayList.get(position).getClassname();
                    String academicYr = teacherNoteArrayList.get(position).getAcademic_yr();
                    String sectionname = teacherNoteArrayList.get(position).getSectionname();
                    String subjectname = teacherNoteArrayList.get(position).getSubjectname();
                    String date = teacherNoteArrayList.get(position).getDate();
                    String pbndate = teacherNoteArrayList.get(position).getpDate();
                    String sectionid = teacherNoteArrayList.get(position).getSection_id();
                    String desc = teacherNoteArrayList.get(position).getDescription();
                    String noteid = teacherNoteArrayList.get(position).getNotes_id();

                    Intent intent = new Intent(context, ViewTeacherNoteActivity.class);
                    intent.putExtra("teacherid", teacherid);
                    intent.putExtra("classname", classname);
                    intent.putExtra("academicYr", academicYr);
                    intent.putExtra("sectionname", sectionname);
                    intent.putExtra("subjectname", subjectname);
                    intent.putExtra("desc1", desc);
                    intent.putExtra("date", date);
                    intent.putExtra("noteid", teacherNoteArrayList.get(position).getNotes_id());


                    if (!pbndate.equals("null")) {
                        SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd");
                        Date newDate = null;
                        try {
                            newDate = spf.parse(pbndate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        spf = new SimpleDateFormat("dd-MM-yyyy");
                        pbndate = spf.format(newDate);
                        intent.putExtra("publish_date", pbndate);
                    } else {
                        pbndate = "";
                        intent.putExtra("publish_date", pbndate);
                    }

                    intent.putExtra("sectionid", sectionid);
                    context.startActivity(intent);
                    ((Activity) context).finish();

                }
            });


            holder.viewedBy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String class_id = teacherNoteArrayList.get(position).getClass_id();
                    String section_id = teacherNoteArrayList.get(position).getSection_id();
                    String desc = teacherNoteArrayList.get(position).getDescription();
                    String noteid = teacherNoteArrayList.get(position).getNotes_id();

                    Intent intent = new Intent(context, TeacherNoteViewedByActivity.class);
                    intent.putExtra("note_desc", desc);
                    intent.putExtra("note_id", noteid);
                    intent.putExtra("class_id", class_id);
                    intent.putExtra("section_id", section_id);
                    context.startActivity(intent);
                }
            });

        } else {
            holder.threedots.setVisibility(View.VISIBLE);
            holder.imgView.setVisibility(View.GONE);
            holder.viewedBy.setVisibility(View.GONE);
        }

        holder.threedots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (teacherNoteArrayList.get(position).getExpanded()) {
                    Log.d("EXPANDED", "" + teacherNoteArrayList.get(position).getExpanded());
                    teacherNoteArrayList.get(position).setExpanded(false);
                    notifyDataSetChanged();
                } else {
                    Log.d("EXPANDED", "" + teacherNoteArrayList.get(position).getExpanded());
                    teacherNoteArrayList.get(position).setExpanded(true);
                    notifyDataSetChanged();
                }
            }
        });


        holder.imgpublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Do you want to publish this note?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String notes_id = teacherNoteArrayList.get(position).getNotes_id();
                        String acdyr = teacherNoteArrayList.get(position).getAcademic_yr();
                        String classid = teacherNoteArrayList.get(position).getClassnm();
                        String sectionid = teacherNoteArrayList.get(position).getSection_id();
                        String subjectid = teacherNoteArrayList.get(position).getSubjectnm();
                        String regid = teacherNoteArrayList.get(position).getTeacher_id();
                        publishRemark(notes_id, acdyr, regid, classid, sectionid, subjectid);

                        holder.threedots.setVisibility(View.GONE);
                        holder.linearLayout.setVisibility(View.GONE);
                        holder.imgpublish.setVisibility(View.GONE);
                        holder.imgedit.setVisibility(View.GONE);
                        holder.imgdelete.setVisibility(View.GONE);
                        holder.imgView.setVisibility(View.VISIBLE);
                        view.refreshDrawableState();

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
        holder.imgdelete.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                String notes_id = teacherNoteArrayList.get(position).getNotes_id();
                String reg_id = teacherNoteArrayList.get(position).getTeacher_id();
                removeRemark(position, notes_id, reg_id);
                view.refreshDrawableState();
                return false;
            }
        });

        holder.imgedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String teacherid = teacherNoteArrayList.get(position).getTeacher_id();
                String classname = teacherNoteArrayList.get(position).getClassname();
                String academicYr = teacherNoteArrayList.get(position).getAcademic_yr();
                String sectionname = teacherNoteArrayList.get(position).getSectionname();
                String subjectname = teacherNoteArrayList.get(position).getSubjectname();
                String desc = teacherNoteArrayList.get(position).getDescription();
                String date = teacherNoteArrayList.get(position).getDate();
                String class_id = teacherNoteArrayList.get(position).getClassnm();
                String section_id = teacherNoteArrayList.get(position).getSection_id();
                String subject_id = teacherNoteArrayList.get(position).getSubjectnm();

                String notes_id = teacherNoteArrayList.get(position).getNotes_id();

                Intent intent = new Intent(context, EditTeacherNoteACtivity.class);
                intent.putExtra("teacherid1", teacherid);
                intent.putExtra("classname1", classname);
                intent.putExtra("academicYr1", academicYr);
                intent.putExtra("sectionname1", sectionname);
                intent.putExtra("subjectname1", subjectname);
                intent.putExtra("desc1", desc);
                intent.putExtra("date1", date);
                intent.putExtra("notes_id", notes_id);
                intent.putExtra("class_id", class_id);
                intent.putExtra("section_id", section_id);
                intent.putExtra("subject_id", subject_id);
                context.startActivity(intent);
                ((Activity) context).finish();
                view.refreshDrawableState();

            }
        });

    }

    @Override
    public int getItemCount() {
        return teacherNoteArrayList.size();
    }

    public void filterlist(ArrayList<TeacherNoteePojo> filtername) {
        this.teacherNoteArrayList = filtername;
        notifyDataSetChanged();
    }

    private void removeRemark(final int position, final String notes_id, final String reg_id) {
        //Creating an alert dialog to confirm the deletion
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Do you want to Delete Note?");

        //if the response is positive in the alert
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, newUrl + "AdminApi/" + "daily_notes",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response == null) {
                                    Toast.makeText(context, "Can't Delete Record", Toast.LENGTH_LONG).show();
                                }
                                Toast.makeText(context, "Note Deleted Successfully", Toast.LENGTH_SHORT).show();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("notes_id", notes_id);
                        params.put("operation", "delete");
                        params.put("login_type", "T");
                        params.put("short_name", name);
                        return params;
                    }
                };
                RequestHandler.getInstance(context).addToRequestQueue(stringRequest);
                //removing the item
                teacherNoteArrayList.remove(position);
                //reloading the list
                notifyDataSetChanged();
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

    private void publishRemark(final String notes_id, final String acdyr, final String regid, final String classid, final String sectionid, final String subjectid) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, newUrl + "AdminApi/" + "daily_notes",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            if (response != null) {
                                Toast.makeText(context, "Teacher note published !!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(context, TeachersNoteACtivity.class);
                                context.startActivity(intent);
                                ((Activity) context).finish();
                            } else {
                                Toast.makeText(context, "Failed to Publish Note", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Error occurred: Try Again", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("notes_id", notes_id);
                params.put("publish", "Y");
                params.put("operation", "publish");
                params.put("login_type", "T");
                params.put("reg_id", regid);
                params.put("acd_yr", acdyr);
                params.put("class_id", classid);
                params.put("section_id", sectionid);
                params.put("subject_id", subjectid);
                params.put("short_name", name);
                return params;

            }

        };
        RequestHandler.getInstance(context).addToRequestQueue(stringRequest);
    }

    class DataTeacher extends RecyclerView.ViewHolder {
        TextView txtnote_subject, txtnoteclass, txtedate, txtnoteview, vetical1, note_tdesc, tvpub, vetical3, tvcdate;
        //FloatingActionButton fab;
        LinearLayout linearLayout, threedots;
        ImageView imgaddnote, imgedit, imgdelete, imgpublish, imgView, viewedBy;

        DataTeacher(View itemView) {
            super(itemView);
            vetical1 = itemView.findViewById(R.id.vetical1);
            note_tdesc = itemView.findViewById(R.id.note_tdesc);
            txtnote_subject = itemView.findViewById(R.id.note_subject);
            imgView = itemView.findViewById(R.id.fabviewnote);
            linearLayout = itemView.findViewById(R.id.teacherlayout);
            threedots = itemView.findViewById(R.id.threedots);
            imgaddnote = itemView.findViewById(R.id.fabaddnote);
            imgedit = itemView.findViewById(R.id.fabeditnote);
            imgdelete = itemView.findViewById(R.id.fabdeletenote);
            imgpublish = itemView.findViewById(R.id.fabpublishnote);
            txtnoteclass = itemView.findViewById(R.id.note_class);
            txtedate = itemView.findViewById(R.id.note_edate);
            tvpub = itemView.findViewById(R.id.tvpub);
            vetical3 = itemView.findViewById(R.id.vetical3);
            tvcdate = itemView.findViewById(R.id.tvcdate);
            txtnoteview = itemView.findViewById(R.id.txtviewpublish);
            viewedBy = itemView.findViewById(R.id.viewdBy);
        }
    }
}
