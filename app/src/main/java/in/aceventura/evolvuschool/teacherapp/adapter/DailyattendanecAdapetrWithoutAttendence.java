package in.aceventura.evolvuschool.teacherapp.adapter;

import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;


import in.aceventura.evolvuschool.teacherapp.R;
import in.aceventura.evolvuschool.teacherapp.pojo.DailyAttendancePojo;

public class DailyattendanecAdapetrWithoutAttendence extends RecyclerView.Adapter<DailyattendanecAdapetrWithoutAttendence.Dataattencnce> {

    private Context context;
    private ArrayList<DailyAttendancePojo> dailyAttendancePojoArrayListnew;

    public DailyattendanecAdapetrWithoutAttendence(Context context, ArrayList<DailyAttendancePojo> dailyAttendancePojoArrayListnew) {
        this.context = context;
        this.dailyAttendancePojoArrayListnew = dailyAttendancePojoArrayListnew;
    }

    @Override
    public Dataattencnce onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.daily_att_adapter, parent, false);
        return new Dataattencnce(view);
    }

    @Override
    public void onBindViewHolder(final Dataattencnce holder, final int position) {
        holder.chk.setTag(dailyAttendancePojoArrayListnew.get(position));


        holder.chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if (b) {
                if (holder.chk.isChecked()) {
                    dailyAttendancePojoArrayListnew.get(position).setCheckedStatus(true);
                    holder.txtname.setTextColor(ContextCompat.getColor(context,R.color.orange));
//                    notifyDataSetChanged();
                } else {
                    dailyAttendancePojoArrayListnew.get(position).setCheckedStatus(false);
                    holder.txtname.setTextColor(ContextCompat.getColor(context,R.color.black));
//                    notifyDataSetChanged();
                }
            }
        });

        if (dailyAttendancePojoArrayListnew.get(position).getlName().equalsIgnoreCase("null")){
            holder.txtname.setText(dailyAttendancePojoArrayListnew.get(position).getfName());
        }
        else {
            holder.txtname.setText(dailyAttendancePojoArrayListnew.get(position).getfName()+" "+dailyAttendancePojoArrayListnew.get(position).getlName());

        }
        holder.txtRollno.setText(dailyAttendancePojoArrayListnew.get(position).getRollno());

    }

    @Override
    public int getItemCount() {
        return dailyAttendancePojoArrayListnew.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class Dataattencnce extends RecyclerView.ViewHolder {

        TextView txtname,txtRollno;
        CheckBox chk,checkboxSelect;
        CardView card;
        Dataattencnce(View itemView) {
            super(itemView);
            txtname=itemView.findViewById(R.id.txtname);
            chk=itemView.findViewById(R.id.checkBoxStatusAtten);
       //     checkboxSelect=itemView.findViewById(R.id.checkBoxSelect2);
            txtRollno=itemView.findViewById(R.id.txtrollno);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

    }
}
