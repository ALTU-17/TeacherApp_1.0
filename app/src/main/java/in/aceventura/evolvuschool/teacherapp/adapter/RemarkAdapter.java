package in.aceventura.evolvuschool.teacherapp.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import in.aceventura.evolvuschool.teacherapp.R;
import in.aceventura.evolvuschool.teacherapp.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.teacherapp.activities.EditRemarkActivity;
import in.aceventura.evolvuschool.teacherapp.activities.RemarksActivity;
import in.aceventura.evolvuschool.teacherapp.activities.RequestHandler;
import in.aceventura.evolvuschool.teacherapp.pojo.RemarkPojo;


/**
 * Created by Admin on 7/5/2018.
 */

public class RemarkAdapter extends RecyclerView.Adapter<RemarkAdapter.DataHold> {
    private Context context;
    private ArrayList<RemarkPojo> remarkPojoArrayList;
    private String name;
    private String newUrl;
    private String read_status;

    public RemarkAdapter(Context context, ArrayList<RemarkPojo> remarkPojoArrayList) {
        this.context = context;
        this.remarkPojoArrayList = remarkPojoArrayList;

        DatabaseHelper mDatabaseHelper = new DatabaseHelper(context);
        name = mDatabaseHelper.getName(1);
        String dUrl = mDatabaseHelper.getURL(1);
        newUrl = mDatabaseHelper.getTURL(1);

        if (name == null || name.equals("")) {
            name = mDatabaseHelper.getName(1);
            dUrl = mDatabaseHelper.getURL(1);
            newUrl = mDatabaseHelper.getTURL(1);
        }
    }

    public void filterlist(ArrayList<RemarkPojo> filtername) {
        this.remarkPojoArrayList = filtername;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DataHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.remark_adapter_lay, parent, false);
        return new DataHold(view);
    }

    @SuppressLint({"SimpleDateFormat", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull final DataHold holder, final int position) {


        // TODO: Setting Remark type to TextView
        if (remarkPojoArrayList.get(position).getRemark_type().equals("Observation")) {
            holder.typeRemark.setText("Observation");
        } else if (remarkPojoArrayList.get(position).getRemark_type().equals("Remark")) {
            holder.typeRemark.setText("Remark");
        }

        String ab = remarkPojoArrayList.get(position).getRmkSubNm();
        if (ab.equals("null")) {
            holder.txtrmk_subject.setVisibility(View.GONE);
            holder.man9.setVisibility(View.GONE);
        } else {
            holder.txtrmk_subject.setVisibility(View.VISIBLE);
            holder.txtrmk_subject.setText(remarkPojoArrayList.get(position).getRmkSubNm());
            holder.man9.setVisibility(View.VISIBLE);

        }
        holder.txtrmkclass.setText(remarkPojoArrayList.get(position).getRmkClassnm() + " " + remarkPojoArrayList.get(position).getRmksecnm());
        holder.txtrmkdate.setText(remarkPojoArrayList.get(position).getPublishremDate());


        String cdate = remarkPojoArrayList.get(position).getRmkDate();
        String pdate = remarkPojoArrayList.get(position).getPublishremDate();


        if (pdate != null && !pdate.isEmpty() && !pdate.equals("null")) {
            SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd");
            Date newDate = null;
            try {
                newDate = spf.parse(pdate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            spf = new SimpleDateFormat("dd-MM-yyyy");
            pdate = spf.format(newDate);
            holder.tvPrdate.setVisibility(View.GONE);
            holder.txtrmkdate.setText(pdate);
            holder.tvCrdate.setVisibility(View.GONE);
        } else {
            holder.txtrmkdate.setText(cdate);
            holder.tvCrdate.setVisibility(View.GONE);
            holder.tvPrdate.setVisibility(View.GONE);
        }


        String asub = remarkPojoArrayList.get(position).getRemarksubject();

        if (asub.equals("null")) {
            holder.txtrmkofsub.setText("");
        } else {
            holder.txtrmkofsub.setText(remarkPojoArrayList.get(position).getRemarksubject());
        }

        //student name
        if (remarkPojoArrayList.get(position).getRmkmnm().equals("null")) {
            holder.rmkStudnm.setText(remarkPojoArrayList.get(position).getRmkfnm() + " " + remarkPojoArrayList.get(position).getRmklnm());
        }
        else if (remarkPojoArrayList.get(position).getRmklnm().equals("null") && remarkPojoArrayList.get(position).getRmkmnm().equals("null")) {
            holder.rmkStudnm.setText(remarkPojoArrayList.get(position).getRmkfnm());
        }
        else if (remarkPojoArrayList.get(position).getRmklnm().equals("null")) {
            holder.rmkStudnm.setText(remarkPojoArrayList.get(position).getRmkfnm() + " " + remarkPojoArrayList.get(position).getRmkmnm());
        }
        else {
            holder.rmkStudnm.setText(remarkPojoArrayList.get(position).getRmkfnm() + " " + remarkPojoArrayList.get(position).getRmkmnm() + " " + remarkPojoArrayList.get(position).getRmklnm());
        }


        if (remarkPojoArrayList.get(position).getExpanded()) {
            Log.d("EXPANDED", "" + remarkPojoArrayList.get(position).getExpanded());

            // TODO: Visibility of Publish based on Remark Type
            if (remarkPojoArrayList.get(position).getRemark_type().equals("Observation")) {
                holder.imgpublish.setVisibility(View.GONE);
                holder.imgdelete.setVisibility(View.VISIBLE);
                holder.imgedit.setVisibility(View.VISIBLE);
            } else {
                holder.imgpublish.setVisibility(View.VISIBLE);
                holder.imgdelete.setVisibility(View.VISIBLE);
                holder.imgedit.setVisibility(View.VISIBLE);
            }

        } else {
            Log.d("EXPANDED", "" + remarkPojoArrayList.get(position).getExpanded());
            holder.imgpublish.setVisibility(View.GONE);
            holder.imgedit.setVisibility(View.GONE);
            holder.imgdelete.setVisibility(View.GONE);

        }

        holder.threedots2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (remarkPojoArrayList.get(position).getExpanded()) {
                    Log.d("EXPANDED", "" + remarkPojoArrayList.get(position).getExpanded());
                    remarkPojoArrayList.get(position).setExpanded(false);
                    notifyDataSetChanged();
                } else {
                    Log.d("EXPANDED", "" + remarkPojoArrayList.get(position).getExpanded());
                    remarkPojoArrayList.get(position).setExpanded(true);
                    notifyDataSetChanged();
                }
            }
        });


        String ack = remarkPojoArrayList.get(position).getRmkack();
        if (ack.equalsIgnoreCase("Y")) {

            holder.threedots2.setVisibility(View.GONE);
            holder.imgthumb.setVisibility(View.VISIBLE);
            holder.imgview.setVisibility(View.VISIBLE);

        }

        String publishStatus = remarkPojoArrayList.get(position).getRmkpublish();


        if (publishStatus.equals("Y")) {
            holder.threedots2.setVisibility(View.GONE);
            holder.imgview.setVisibility(View.VISIBLE);

            //viewdBy icon visibility
            if (remarkPojoArrayList.get(position).getRead_status().equals("1")){
                holder.viewdBy.setVisibility(View.VISIBLE);
            } else{
                holder.viewdBy.setVisibility(View.GONE);
            }

            //api code for viewed by
            /*String academic_yr = (SharedClass.getInstance(context).loadSharedPreference_acdYear());
            final HashMap<String, String> params = new HashMap<>();
            params.put("acd_yr", academic_yr);
            params.put("remark_id", remarkPojoArrayList.get(position).getRmkid());
            params.put("short_name", name);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "AdminApi/" + "get_student_remark_viewed", new JSONObject(params), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if (response.getString("status").equals("true")) {
                            read_status = response.getString("read_status");
                            if (read_status.equals("1")){
                                holder.viewdBy.setVisibility(View.VISIBLE);
                            }
                            else{
                                holder.viewdBy.setVisibility(View.GONE);
                            }

                        } else {
                            System.out.println(response.toString());
                        }



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println(error.toString());
                }
            });
            RequestQueue requestQueue1 = Volley.newRequestQueue(context);

            jsonObjectRequest.setRetryPolicy(
                    new DefaultRetryPolicy(
                            500000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                    )
            );
            requestQueue1.add(jsonObjectRequest);
*/

            holder.imgview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String desc = remarkPojoArrayList.get(position).getRmkdesc();
                    Intent i = new Intent(context, ViewRemarkAttachmentsActivity.class);
                    i.putExtra("description", desc);
                    i.putExtra("rm_id", remarkPojoArrayList.get(position).getRmkid());
                    i.putExtra("rem_date", remarkPojoArrayList.get(position).getRmkDate());
                    i.putExtra("sectionid", remarkPojoArrayList.get(position).getRmksecid());
                    context.startActivity(i);
                    ((Activity) context).finish();


                }

            });

        } else {
            holder.imgview.setVisibility(View.GONE);
            holder.viewdBy.setVisibility(View.GONE);
            holder.threedots2.setVisibility(View.VISIBLE);
        }

        holder.imgedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditRemarkActivity.class);
                intent.putExtra("rmkid", remarkPojoArrayList.get(position).getRmkid());
                intent.putExtra("acdyr", remarkPojoArrayList.get(position).getRmkacdyr());
                intent.putExtra("teacherid", remarkPojoArrayList.get(position).getRmkteacherid());
                intent.putExtra("sectionid", remarkPojoArrayList.get(position).getRmksecid());
                intent.putExtra("sectionname", remarkPojoArrayList.get(position).getRmksecnm());
                intent.putExtra("classid", remarkPojoArrayList.get(position).getRmkclassid());
                intent.putExtra("subject_id", remarkPojoArrayList.get(position).getRmksubid());
                intent.putExtra("student_id", remarkPojoArrayList.get(position).getRmkstudid());
                intent.putExtra("remark_desc", remarkPojoArrayList.get(position).getRmkdesc());
                intent.putExtra("remark_subject", remarkPojoArrayList.get(position).getRemarksubject());

                // TODO: Passing value of remark_type to EditRemark Activity
                if (remarkPojoArrayList.get(position).getRemark_type().equals("Observation")) {
                    intent.putExtra("remark_type", "Observation");
                } else if (remarkPojoArrayList.get(position).getRemark_type().equals("Remark")) {
                    intent.putExtra("remark_type", "Remark");
                }
                //new
                else{
                    intent.putExtra("remark_type", "");
                }

                intent.putExtra("studfnm", remarkPojoArrayList.get(position).getRmkfnm());
                if (remarkPojoArrayList.get(position).getRmkmnm().equals("")) {
                    intent.putExtra("studmnm", "");
                } else {
                    intent.putExtra("studmnm", remarkPojoArrayList.get(position).getRmkmnm());
                }


                if (remarkPojoArrayList.get(position).getRmklnm().equals("")) {
                    intent.putExtra("studlnm", "");
                } else {
                    intent.putExtra("studlnm", remarkPojoArrayList.get(position).getRmklnm());
                }

                intent.putExtra("classnm", remarkPojoArrayList.get(position).getRmkClassnm());
                intent.putExtra("subnm", remarkPojoArrayList.get(position).getRmkSubNm());
                intent.putExtra("rem_date", remarkPojoArrayList.get(position).getRmkDate());

                context.startActivity(intent);
                ((Activity) context).finish();
            }
        });


        holder.imgdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String rmkid = remarkPojoArrayList.get(position).getRmkid();
                String teacherid = remarkPojoArrayList.get(position).getRmkteacherid();
                String academic_yr = remarkPojoArrayList.get(position).getRmkacdyr();
                String classid = remarkPojoArrayList.get(position).getRmkclassid();
                String sectionid = remarkPojoArrayList.get(position).getRmksecid();
                String subjectid = remarkPojoArrayList.get(position).getRmksubid();
                String rmkdate = remarkPojoArrayList.get(position).getRmkDate();
                String rmkdesc = remarkPojoArrayList.get(position).getRmkdesc();
                String rmksub = remarkPojoArrayList.get(position).getRemarksubject();
                String studid = remarkPojoArrayList.get(position).getRmkstudid();
                removeRemark(position, rmkid, teacherid, academic_yr, classid, sectionid, subjectid, rmkdate, rmkdesc, rmksub, studid);
                view.refreshDrawableState();
            }
        });

        holder.imgpublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("\"Do you want to publish this remark?\"");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String rmkid = remarkPojoArrayList.get(position).getRmkid();
                        String teacherid = remarkPojoArrayList.get(position).getRmkteacherid();
                        String academic_yr = remarkPojoArrayList.get(position).getRmkacdyr();
                        String classid = remarkPojoArrayList.get(position).getRmkclassid();
                        String sectionid = remarkPojoArrayList.get(position).getRmksecid();
                        String subjectid = remarkPojoArrayList.get(position).getRmksubid();
                        String rmkdate = remarkPojoArrayList.get(position).getRmkDate();
                        String rmkdesc = remarkPojoArrayList.get(position).getRmkdesc();
                        String rmksub = remarkPojoArrayList.get(position).getRemarksubject();
                        String studid = remarkPojoArrayList.get(position).getRmkstudid();
                        // String studid=remarkPojoArrayList.get(position).getSubjectid();
                        publishrmk(rmkid, teacherid, academic_yr, classid, sectionid, subjectid, rmkdate, rmkdesc, rmksub, studid);

                        holder.imgpublish.setVisibility(View.GONE);
                        holder.imgedit.setVisibility(View.GONE);
                        holder.imgdelete.setVisibility(View.GONE);
                        holder.threedots2.setVisibility(View.GONE);
                        holder.imgview.setVisibility(View.VISIBLE);
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

    }

    private void publishrmk(final String rmkid, final String teacherid, final String academic_yr, final String classid, final String sectionid, final String subid, final String date, final String dec, final String subrmk, final String studid) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, newUrl + "AdminApi/" + "remark",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response == null) {
                            Toast.makeText(context, "Failed to Publish Remark", Toast.LENGTH_LONG).show();
                        }
                        Toast.makeText(context, "Remark Published Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, RemarksActivity.class);
                        context.startActivity(intent);
                        ((Activity) context).finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Error occurred: Try Again" + error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("remark_id", rmkid);
                params.put("publish", "Y");
                params.put("operation", "publish");
                params.put("login_type", "T");
                params.put("teacher_id", teacherid);
                params.put("student_id", studid);
                params.put("short_name", name);
                return params;
            }
        };
        RequestHandler.getInstance(context).addToRequestQueue(stringRequest);
    }

    private void removeRemark(final int position, final String rmkid, final String teacherid, final String academic_yr, final String classid, final String sectionid, final String subid, final String date, final String dec, final String subrmk, final String studid) {
        //Creating an alert dialog to confirm the deletion
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Do you want to Delete Remark?");

        //if the response is positive in the alert
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, newUrl + "AdminApi/" + "remark",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response == null) {
                                    Toast.makeText(context, "Can not Delete Record", Toast.LENGTH_LONG).show();
                                }
                                Toast.makeText(context, "Remark Deleted Successfully", Toast.LENGTH_SHORT).show();
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
                        params.put("remark_id", rmkid);
                        params.put("publish", "N");
                        params.put("acknowledge", "N");
                        params.put("operation", "delete");
                        params.put("login_type", "T");
                        params.put("academic_yr ", academic_yr);
                        params.put("teacher_id ", teacherid);
                        params.put("class_id ", classid);
                        params.put("section_id ", sectionid);
                        params.put("subject_id ", subid);
                        params.put("remark_desc ", dec);
                        params.put("student_id ", studid);
                        params.put("remark_date ", date);
                        params.put("remark_subject ", subrmk);
                        params.put("short_name", name);
                        return params;


                    }

                };
                RequestHandler.getInstance(context).addToRequestQueue(stringRequest);


                //removing the item
                remarkPojoArrayList.remove(position);

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

    @Override
    public int getItemCount() {
        return remarkPojoArrayList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class DataHold extends RecyclerView.ViewHolder {
        TextView txtrmk_subject, txtrmkdate, txtrmkclass, txtrmkofsub, rmkStudnm, txtviewpublishrmk, man9, tvPrdate, tvCrdate, typeRemark;
        Button closePopupBtn;
        ImageView imgpublish;
        ImageView imgedit;
        ImageView imgdelete;
        ImageView imgview,viewdBy;
        LinearLayout linearLayout1, threedots2;
        private ImageButton imgthumb;

        DataHold(View itemView) {
            super(itemView);
            tvPrdate = itemView.findViewById(R.id.tvPrdate);
            tvCrdate = itemView.findViewById(R.id.tvCrdate);
            imgview = itemView.findViewById(R.id.fabviewrmk);
            man9 = itemView.findViewById(R.id.vetical2);
            threedots2 = itemView.findViewById(R.id.threedots2);
            imgpublish = itemView.findViewById(R.id.fabpublishrmk);
            imgdelete = itemView.findViewById(R.id.fabdeletermk);
            imgedit = itemView.findViewById(R.id.fabeditrmk);
            txtrmk_subject = itemView.findViewById(R.id.remark_subject);
            txtrmkclass = itemView.findViewById(R.id.remark_class);
            txtrmkdate = itemView.findViewById(R.id.remark_edate);
            txtrmkofsub = itemView.findViewById(R.id.subOfrmk);
            rmkStudnm = itemView.findViewById(R.id.remark_studnm);
            Button btnEdit = itemView.findViewById(R.id.btn_edit_remark);
            Button btnDelete = itemView.findViewById(R.id.btn_delete_remark);
            Button btnView = itemView.findViewById(R.id.btn_visibility_remark);
            Button btnPublish = itemView.findViewById(R.id.btn_publish_remark);
            imgthumb = itemView.findViewById(R.id.btn_thumb);
           // closePopupBtn = itemView.findViewById(R.id.closePopupBtn);
            linearLayout1 = itemView.findViewById(R.id.linearLayout1);
            txtviewpublishrmk = itemView.findViewById(R.id.txtviewpublishrmk);
            typeRemark = itemView.findViewById(R.id.typeRemark);
            viewdBy = itemView.findViewById(R.id.viewdBy);


        }
    }
}
