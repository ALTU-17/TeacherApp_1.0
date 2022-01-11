package in.aceventura.evolvuschool.teacherapp.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import in.aceventura.evolvuschool.teacherapp.R;
import in.aceventura.evolvuschool.teacherapp.activities.ViewNoticeStaffActivity;
import in.aceventura.evolvuschool.teacherapp.pojo.NoticeStaffPojo1;

public class NoticeStaffAdapter1 extends RecyclerView.Adapter<NoticeStaffAdapter1.Datahold>{
    private Context context;
    private ArrayList<NoticeStaffPojo1> noticeStaffPojoArrayList1;

    public NoticeStaffAdapter1(Context context, ArrayList<NoticeStaffPojo1> noticeStaffPojoArrayList1) {
        this.context = context;
        this.noticeStaffPojoArrayList1 = noticeStaffPojoArrayList1;
    }

    @Override
    public Datahold onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.noticestaff_adapter_lay, parent, false);
        return new Datahold(view);
    }

    @Override
    public void onBindViewHolder(Datahold holder, final int position) {
        holder.noticeStaffPojo1 = noticeStaffPojoArrayList1.get(position);
        holder.txtdate.setText(noticeStaffPojoArrayList1.get(position).getNotice_date());
        holder.txtCreatedBy.setText(noticeStaffPojoArrayList1.get(position).getName());
        holder.txtSubject.setText(noticeStaffPojoArrayList1.get(position).getSubject());
        holder.noticetype.setText(noticeStaffPojoArrayList1.get(position).getNotice_type());
//      holder.txtclassnoticcenm.setText("Class  : " + noticeStaffPojo.getClassname());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, ViewNoticeStaffActivity.class);
                intent.putExtra("noticetype",noticeStaffPojoArrayList1.get(position).getNotice_type());
                intent.putExtra("notice_id",noticeStaffPojoArrayList1.get(position).getNotice_id());
                intent.putExtra("imagename",noticeStaffPojoArrayList1.get(position).getImagename());
//              intent.putExtra("classname",noticeStaffPojoArrayList.get(position).getClassname());
                intent.putExtra("classid",noticeStaffPojoArrayList1.get(position).getClass_id());
//                context.startActivity(intent);
//                ((Activity)context).finish();

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
//                ((Activity)context).finish();

            }
        });

    }

    @Override
    public int getItemCount() {
        return noticeStaffPojoArrayList1.size();
    }

    public class Datahold extends RecyclerView.ViewHolder {
         NoticeStaffPojo1 noticeStaffPojo1;
        private TextView txtdate, txtCreatedBy, txtSubject, noticetype;
        TextView tv_loading;

        public Datahold(View itemView) {
            super(itemView);
            noticetype = itemView.findViewById(R.id.t_noticetype);
            txtdate = itemView.findViewById(R.id.t_notice_date);
            txtSubject = itemView.findViewById(R.id.t_subject_notice);
            txtCreatedBy = itemView.findViewById(R.id.t_notice_createdby);

        }
    }
}