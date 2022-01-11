package in.aceventura.evolvuschool.teacherapp.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import in.aceventura.evolvuschool.teacherapp.activities.EditLeaveAPplicationActivity;
import in.aceventura.evolvuschool.teacherapp.activities.RequestHandler;
import in.aceventura.evolvuschool.teacherapp.pojo.LeavePojo;

public class LeaveAdapter extends RecyclerView.Adapter<LeaveAdapter.ViewHolder> {

    Context context;
    private ArrayList<LeavePojo> leavePojosArraylist;

    private String name;
    private String newUrl;
    private String operation;
    private String leave_app_id;

    public LeaveAdapter(ArrayList<LeavePojo> mSearchArrayList) {
        this.leavePojosArraylist = mSearchArrayList;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.leave_list_adapter, parent, false);
        context = parent.getContext();


        DatabaseHelper mDatabaseHelper = new DatabaseHelper(LeaveAdapter.this.context);
        name = mDatabaseHelper.getName(1);
        String dUrl = mDatabaseHelper.getURL(1);
        newUrl = mDatabaseHelper.getTURL(1);
        return new ViewHolder(view);

    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        LeavePojo leavePojo = leavePojosArraylist.get(position);


        holder.txtnoofdays.setText(leavePojosArraylist.get(position).getNo_of_date());
        holder.txtleaveType.setText(leavePojosArraylist.get(position).getName());
        String start_date = (leavePojosArraylist.get(position).getLeave_start_date());
        String end_date = (leavePojosArraylist.get(position).getLeave_end_date());

        leavePojosArraylist.get(position).getLeave_type_id();

        String status = (leavePojosArraylist.get(position).getStatus());
        String reason_rejection = (leavePojosArraylist.get(position).getReason_for_rejection());
        String staff_id = (leavePojosArraylist.get(position).getStaff_id());
        String acd_yr = (leavePojosArraylist.get(position).getAcademic_yr());
        leave_app_id = (leavePojosArraylist.get(position).getLeave_app_id());

        switch (status) {
            case "A":
                holder.statusleave.setText("Applied");
                break;
            case "P":
                holder.statusleave.setText("Approved");
                break;
            case "H":
                holder.statusleave.setText("Hold");
                break;
            case "R":
                holder.statusleave.setText("Reject");
                break;
        }


        if (status.equals("A") || status.equals("H")) {
            holder.threedots2.setVisibility(View.VISIBLE);
        }

        if (status.equals("R") || status.equals("P")) {
            holder.btnedit.setVisibility(View.GONE);
            holder.btndelete.setVisibility(View.GONE);
            holder.threedots2.setVisibility(View.GONE);
        }

        holder.threedots2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.btnedit.isShown() && holder.btndelete.isShown()) {
                    holder.btnedit.setVisibility(View.GONE);
                    holder.btndelete.setVisibility(View.GONE);

                } else {
                    holder.btnedit.setVisibility(View.VISIBLE);
                    holder.btndelete.setVisibility(View.VISIBLE);
                }
            }
        });

        holder.btnedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditLeaveAPplicationActivity.class);
                intent.putExtra("leave_type_id", leavePojosArraylist.get(position).getLeave_type_id());
                intent.putExtra("start_date", leavePojosArraylist.get(position).getLeave_start_date());
                intent.putExtra("end_date", leavePojosArraylist.get(position).getLeave_end_date());
                intent.putExtra("no_of_days", leavePojosArraylist.get(position).getNo_of_date());
                intent.putExtra("status", leavePojosArraylist.get(position).getStatus());
                intent.putExtra("reason_rejection", leavePojosArraylist.get(position).getReason_for_rejection());
                intent.putExtra("staff_id", leavePojosArraylist.get(position).getStaff_id());
                intent.putExtra("acd_yr", leavePojosArraylist.get(position).getAcademic_yr());
                intent.putExtra("operation", operation);
                intent.putExtra("reason", leavePojosArraylist.get(position).getReason());
                intent.putExtra("leave_type_name", leavePojosArraylist.get(position).getName());
                intent.putExtra("leave_app_id", leavePojosArraylist.get(position).getLeave_app_id());
                intent.putExtra("leave_name", (leavePojosArraylist.get(position).getName()));
                context.startActivity(intent);


            }
        });

        holder.btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteData(position);
            }
        });


        if (!start_date.equals("null")) {
            SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd");
            Date newDate = null;
            try {
                newDate = spf.parse(start_date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            spf = new SimpleDateFormat("dd-MM-yyyy");
            start_date = spf.format(newDate);
            holder.txtstartdate.setText(start_date);

        }

        if (!end_date.equals("null")) {
            SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd");
            Date newDate = null;
            try {
                newDate = spf.parse(end_date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            spf = new SimpleDateFormat("dd-MM-yyyy");
            end_date = spf.format(newDate);
            holder.txtenddate.setText(end_date);
        }
    }

    private void deleteData(final int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Do you want to Delete Leave Application ?");

        //if the response is positive in the alert
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, newUrl+"AdminApi/leave_application",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response == null) {
                                    Toast.makeText(context, "Can not Delete Record", Toast.LENGTH_LONG).show();
                                }
                                Toast.makeText(context, "Leave Application Deleted Successfully", Toast.LENGTH_SHORT).show();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("leave_app_id", leave_app_id);
                        params.put("operation", "delete");
                        params.put("short_name", name);
                        return params;
                    }
                };
                RequestHandler.getInstance(context).addToRequestQueue(stringRequest);

                //removing the item
                leavePojosArraylist.remove(position);
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
        return leavePojosArraylist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtnoofdays, txtleaveType, txtstatus, txtstartdate, txtenddate, statusleave;
        ImageView btnedit, btndelete;

        LinearLayout threedots2;

        public ViewHolder(View itemView) {
            super(itemView);
            txtnoofdays = itemView.findViewById(R.id.noofdays);
            txtleaveType = itemView.findViewById(R.id.leaveType);
            txtstartdate = itemView.findViewById(R.id.startdatetxt);
            txtenddate = itemView.findViewById(R.id.enddatetxt);
            btndelete = itemView.findViewById(R.id.delete);
            btnedit = itemView.findViewById(R.id.update);
            threedots2 = itemView.findViewById(R.id.threedots2);
            statusleave = itemView.findViewById(R.id.statusleave);
        }
    }
}
