package in.aceventura.evolvuschool.teacherapp.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


import in.aceventura.evolvuschool.teacherapp.R;
import in.aceventura.evolvuschool.teacherapp.pojo.TeacherimetablePojo;

public class TimetableAdapter extends RecyclerView.Adapter<TimetableAdapter.DataTimetable> {

    private Context context;
    private ArrayList<TeacherimetablePojo> teacherimetablePojoArrayList;
    TeacherimetablePojo teacherimetablePojo;

    public TimetableAdapter(Context context, ArrayList<TeacherimetablePojo> teacherimetablePojoArrayList) {
        this.context = context;
        this.teacherimetablePojoArrayList = teacherimetablePojoArrayList;
    }

    @Override
    public DataTimetable onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.timetableadapter, parent, false);
        return new DataTimetable(view);
    }

    @Override
    public void onBindViewHolder(DataTimetable holder, int position) {
        teacherimetablePojo=teacherimetablePojoArrayList.get(position);


        holder.txtperiod.setText("1");
        holder.txtsub.setText("English");
        holder.txtclass.setText("1A");
    }

    @Override
    public int getItemCount() {
        return teacherimetablePojoArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class DataTimetable extends RecyclerView.ViewHolder {
        TextView txtperiod,txtsub,txtclass;
        public DataTimetable(View itemView) {
            super(itemView);
            txtperiod=itemView.findViewById(R.id.txtperiodno);
            txtsub=itemView.findViewById(R.id.txtsubnametimetable);
            txtclass=itemView.findViewById(R.id.txtclassdiv);
        }
    }
}
