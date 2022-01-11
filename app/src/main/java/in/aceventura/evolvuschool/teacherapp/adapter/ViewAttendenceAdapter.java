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

public class ViewAttendenceAdapter extends RecyclerView.Adapter<ViewAttendenceAdapter.DataAttendence> {
    private Context context;
    private ArrayList<DailyAttendancePojo> dailyAttendancePojoArrayList;

    public ViewAttendenceAdapter(Context context, ArrayList<DailyAttendancePojo> dailyAttendancePojoArrayList) {
        this.context = context;
        this.dailyAttendancePojoArrayList = dailyAttendancePojoArrayList;
    }

    private DailyAttendancePojo dailyAttendancePojo;


    @Override
    public DataAttendence onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.viewattendenceadapter_lay,parent,false);
        return new DataAttendence(view);
    }

    @Override
    public void onBindViewHolder(DataAttendence holder, int position) {

        holder.txtroll.setText(dailyAttendancePojoArrayList.get(position).getRollno());
        holder.txtstudname.setText(dailyAttendancePojoArrayList.get(position).getfName()+ " " + dailyAttendancePojoArrayList.get(position).getlName());
        String status=dailyAttendancePojoArrayList.get(position).getAttendencestatus();
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

    public class DataAttendence extends RecyclerView.ViewHolder {
        TextView txtroll,txtstudname,txtstatus;
        public DataAttendence(View itemView) {
            super(itemView);
            txtroll=itemView.findViewById(R.id.txtrollstud);
            txtstudname=itemView.findViewById(R.id.txtstudname);
            //txtstatus=itemView.findViewById(R.id.txtstatusstud);
        }
    }
}
