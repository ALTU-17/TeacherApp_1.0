package in.aceventura.evolvuschool.teacherapp.adapter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


import in.aceventura.evolvuschool.teacherapp.R;
import in.aceventura.evolvuschool.teacherapp.activities.ViewHomeworkActivity;
import in.aceventura.evolvuschool.teacherapp.pojo.ViewHmkPojo;


/**
 * Created by Admin on 7/12/2018.
 */

public class ViewHmkAdapter extends RecyclerView.Adapter<ViewHmkAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ViewHmkPojo> viewHmkPojoArrayList;
    private ViewHmkPojo viewHmkPojo;
    String tCommnt, statusHmk;
    private ProgressDialog progressDialog;


    public ViewHmkAdapter(Context context, ArrayList<ViewHmkPojo> viewHmkPojoArrayList) {
        this.context = context;
        this.viewHmkPojoArrayList = viewHmkPojoArrayList;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_view_hmk, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        progressDialog = new ProgressDialog(context);
        String fname = viewHmkPojoArrayList.get(position).getStudFname();
        String lname = viewHmkPojoArrayList.get(position).getStudLname();
        holder.txtstudnm.setText(fname + " " + lname);
        holder.txtparentcomment.setText(viewHmkPojoArrayList.get(position).getParentcomment());
        holder.txtteachrcomment.setText(viewHmkPojoArrayList.get(position).getTeachercommet());
        // holder.edtaddcomment.setText(viewHmkPojo.getAddcomment().toString());

        if (viewHmkPojoArrayList.get(position).getParentcomment().equalsIgnoreCase("")) {
            holder.pcommentlayout.setVisibility(View.GONE);
        } else {
            holder.pcommentlayout.setVisibility(View.VISIBLE);
        }

        if (viewHmkPojoArrayList.get(position).getTeachercommet().equalsIgnoreCase("")) {
            holder.Tcommentlayout.setVisibility(View.GONE);
        } else {
            holder.Tcommentlayout.setVisibility(View.VISIBLE);
        }
        if (viewHmkPojoArrayList.get(position).getHomework_status().equalsIgnoreCase("")) {
            holder.txtstatus.setText("Assigned");

        } else {
            holder.txtstatus.setText(viewHmkPojoArrayList.get(position).getHomework_status());

        }


        holder.chk.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {


               String status = ViewHomeworkActivity.getSelectedHomeworkStatus();
                viewHmkPojoArrayList.get(position).setHomework_status(status);
                viewHmkPojoArrayList.get(position).setCheckedStatus(true);

                if(!viewHmkPojoArrayList.get(position).getTeachercommet().equals("")){
                    holder.txtteachrcomment.setVisibility(View.VISIBLE);
                    holder.txtteachrcomment.setText(viewHmkPojoArrayList.get(position).getTeachercommet());
                }

            } else {
                viewHmkPojoArrayList.get(position).setCheckedStatus(false);
                viewHmkPojoArrayList.get(position).setHomework_status("Assigned");
            }
            notifyDataSetChanged();
        });



        holder.imgupdate.setOnTouchListener((view, motionEvent) -> {

            Toast.makeText(context, "Feature Under Working", Toast.LENGTH_SHORT).show();
            return false;
        });



            holder.optionscomments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                   // Toast.makeText(context, position +" clicked", Toast.LENGTH_SHORT).show();
                    Log.d("ADAPTER", ""+position);
                    holder.addcommentlayout.setVisibility(View.VISIBLE);
                    holder.edtaddcomment.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                            Log.d("ADAPTER", "tect changed on :"+position);
                            viewHmkPojoArrayList.get(position).setTeachercommet(holder.edtaddcomment.getText().toString());
                            holder.txtteachrcomment.setText(holder.edtaddcomment.getText().toString());

                        }

                    });
                }
            });
//        holder.optionscomments.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                holder.addcommentlayout.setVisibility(View.VISIBLE);
//               // holder.chk.setChecked(false);
//                return false;
//            }
//        });

        holder.txtstatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(context, viewHmkPojo.getHomework_status(), Toast.LENGTH_SHORT).show();
                PopupMenu popup = new PopupMenu(context, holder.txtstatus);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.menu_family_relations, popup.getMenu());

                //registering popup with OnMenuItemClickListener

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu1:
                                holder.txtstatus.setText("Assigned");
                                viewHmkPojoArrayList.get(position).setHomework_status("Assigned");
//                                holder.btnadd.setVisibility(View.VISIBLE);

                                break;
                            case R.id.menu2:
                                holder.txtstatus.setText("Completed");
                                viewHmkPojoArrayList.get(position).setHomework_status("Completed");
                                //  //Toast.makeText(context, "comple", Toast.LENGTH_SHORT).show();
                                // holder.btnadd.setVisibility(View.VISIBLE);


                                break;
                            case R.id.menu3:
                                holder.txtstatus.setText("Partial");
                                viewHmkPojoArrayList.get(position).setHomework_status("Partial");
                                //  holder.btnadd.setVisibility(View.VISIBLE);

                                break;
                        }
                        return false;
                    }
                });

                popup.setGravity(Gravity.CENTER);

                popup.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return viewHmkPojoArrayList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void filterlist(ArrayList<ViewHmkPojo> filtername) {
        this.viewHmkPojoArrayList = filtername;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtrollno, txtstudnm, txtteachrcomment, txtparentcomment, txtstatus;
        private Spinner statusspin;
        private EditText edtaddcomment;
        private Button add, optionscomments;

        ImageView imgupdate;
        private CheckBox chk;
        LinearLayout Tcommentlayout, pcommentlayout, addcommentlayout;

        public ViewHolder(View itemView) {
            super(itemView);
            edtaddcomment = itemView.findViewById(R.id.addcomment);
            txtrollno = itemView.findViewById(R.id.rollno);
            txtstudnm = itemView.findViewById(R.id.studnm);
            txtteachrcomment = itemView.findViewById(R.id.teachercomment);
            txtparentcomment = itemView.findViewById(R.id.parentcomment);
            statusspin = itemView.findViewById(R.id.sttusspin);
            //add = itemView.findViewById(R.id.addreply);
            txtstatus = itemView.findViewById(R.id.txtstatusmenu);
            optionscomments = itemView.findViewById(R.id.optioncomments);
            Tcommentlayout = itemView.findViewById(R.id.tCOmment);
            pcommentlayout = itemView.findViewById(R.id.parntcmt);
            addcommentlayout = itemView.findViewById(R.id.addcommentoption);
            imgupdate = itemView.findViewById(R.id.updatestud);
            chk = itemView.findViewById(R.id.checkBoxStatus);

        }
    }
}
