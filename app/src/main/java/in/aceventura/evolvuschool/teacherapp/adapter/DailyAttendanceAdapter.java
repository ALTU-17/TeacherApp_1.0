package in.aceventura.evolvuschool.teacherapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.aceventura.evolvuschool.teacherapp.R;
import in.aceventura.evolvuschool.teacherapp.pojo.DailyAttendancePojo;

public class DailyAttendanceAdapter extends RecyclerView.Adapter<DailyAttendanceAdapter.DataHold> {
    private Context context;
    private ArrayList<DailyAttendancePojo> dailyAttendancePojoArrayList;

    public DailyAttendanceAdapter(Context context, ArrayList<DailyAttendancePojo> dailyAttendancePojoArrayList, ArrayList<String> selectedCheckboxes) {
        this.context = context;
        this.dailyAttendancePojoArrayList = dailyAttendancePojoArrayList;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public DataHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.daily_att_adapter, parent, false);
        return new DataHold(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DataHold holder, final int position) {

        if (dailyAttendancePojoArrayList.size() != 0 && dailyAttendancePojoArrayList != null) {
            holder.txtRollno.setText(dailyAttendancePojoArrayList.get(position).getRollno());
            //get the list of students
            if (dailyAttendancePojoArrayList.get(position).getlName().equalsIgnoreCase("null")) {
                holder.txtname.setText(dailyAttendancePojoArrayList.get(position).getfName());
            } else {
                holder.txtname.setText(dailyAttendancePojoArrayList.get(position).getfName() + " " + dailyAttendancePojoArrayList.get(position).getlName());
            }

            holder.chk.setOnCheckedChangeListener((compoundButton, b) -> {
                if (holder.chk.isChecked()) {
                    dailyAttendancePojoArrayList.get(position).setCheckedStatus(true);
                    holder.txtname.setTextColor(ContextCompat.getColor(context, R.color.orange));

                    dailyAttendancePojoArrayList.get(position).setAttendencestatus("1");
                } else {
                    dailyAttendancePojoArrayList.get(position).setCheckedStatus(false);
                    holder.txtname.setTextColor(ContextCompat.getColor(context, R.color.black));
                    dailyAttendancePojoArrayList.get(position).setAttendencestatus("0");
                }
            });

            holder.checkBoxSelect.setOnCheckedChangeListener((compoundButton, b) -> {
                if (holder.checkBoxSelect.isChecked()) {
                    dailyAttendancePojoArrayList.get(position).setCheckedLeftStatus(true);
                } else {
                    dailyAttendancePojoArrayList.get(position).setCheckedLeftStatus(false);
                }
            });

            if (dailyAttendancePojoArrayList.get(position).isCheckedLeftStatus()) {
                holder.checkBoxSelect.setChecked(true);
            } else {
                holder.checkBoxSelect.setChecked(false);
            }

            if (dailyAttendancePojoArrayList.get(position).isCheckedStatus()) {
                holder.chk.setChecked(true);
            } else {
                holder.chk.setChecked(false);
            }


            //if Absent set orange and checked
            //i.e right chkbx is checked
            if (dailyAttendancePojoArrayList.get(position).getAttendencestatus().equalsIgnoreCase("1")) {
                holder.txtname.setTextColor(ContextCompat.getColor(context, R.color.orange));
                dailyAttendancePojoArrayList.get(position).setCheckedStatus(true);
                holder.chk.setChecked(true);
            }
            //if  present  black and unchecked...
            //i.e right chkbx is unchecked...
            else {
                holder.chk.setChecked(false);
                dailyAttendancePojoArrayList.get(position).setCheckedStatus(false);
                holder.txtname.setTextColor(ContextCompat.getColor(context, R.color.black));
            }

        }
    }

    @Override
    public int getItemCount() {
        return dailyAttendancePojoArrayList.size();
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
        TextView txtname, txtRollno;
        RelativeLayout linearLayout;
        TextView txtPresenty;

        //chk = right && checkBoxSelect = left;
        CheckBox chk, checkBoxSelect;

        DataHold(View itemView) {
            super(itemView);
            checkBoxSelect = itemView.findViewById(R.id.checkBoxSelect1);
            txtname = itemView.findViewById(R.id.txtname);
            chk = itemView.findViewById(R.id.checkBoxStatusAtten);
            txtRollno = itemView.findViewById(R.id.txtrollno);
            txtPresenty = itemView.findViewById(R.id.txtpresenty);
            linearLayout = itemView.findViewById(R.id.linearattendance);
        }
    }
}
