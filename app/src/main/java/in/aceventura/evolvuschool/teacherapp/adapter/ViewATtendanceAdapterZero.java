package in.aceventura.evolvuschool.teacherapp.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


import in.aceventura.evolvuschool.teacherapp.R;
import in.aceventura.evolvuschool.teacherapp.pojo.DailyAttendancePojo;

public class ViewATtendanceAdapterZero extends RecyclerView.Adapter<ViewATtendanceAdapterZero.Datahold> {
    private Context context;
    private ArrayList<DailyAttendancePojo> dailyAttendancePojoArrayList;

    public ViewATtendanceAdapterZero(Context context, ArrayList<DailyAttendancePojo> dailyAttendancePojoArrayList) {
        this.context = context;
        this.dailyAttendancePojoArrayList = dailyAttendancePojoArrayList;
    }

    @Override
    public Datahold onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.viewattendanceadapterzero_layout,parent,false);
        return new Datahold(view);
    }

    @Override
    public void onBindViewHolder(Datahold holder, int position) {
        holder.txtstudname.setText(dailyAttendancePojoArrayList.get(position).getfName()+" "+dailyAttendancePojoArrayList.get(position).getlName());
        holder.txtrollno.setText(dailyAttendancePojoArrayList.get(position).getRollno());
    }

    @Override
    public int getItemCount() {
        return dailyAttendancePojoArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class Datahold extends RecyclerView.ViewHolder {
        TextView txtrollno,txtstudname;
        Datahold(View itemView) {
            super(itemView);
            txtrollno=itemView.findViewById(R.id.txtrollstudzero);
            txtstudname=itemView.findViewById(R.id.txtstudnamezero);
        }
    }
}
