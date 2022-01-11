package in.aceventura.evolvuschool.teacherapp.adapter;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


import in.aceventura.evolvuschool.teacherapp.R;
import in.aceventura.evolvuschool.teacherapp.activities.NoticeActivity;
import in.aceventura.evolvuschool.teacherapp.activities.ViewNoticeActivity;
import in.aceventura.evolvuschool.teacherapp.pojo.NoticePojo;


public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.Datahold> {
    private Context context;
    private ArrayList<NoticePojo> noticePojoArrayList;
    private DownloadManager downloadManager;


    public NoticeAdapter(Context context, ArrayList<NoticePojo> noticePojoArrayList) {
        this.context = context;
        this.noticePojoArrayList = noticePojoArrayList;
    }

    @Override
    public Datahold onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notice_adapter_lay, parent, false);
        return new Datahold(view);
    }

    @Override
    public void onBindViewHolder(Datahold holder, final int position) {
        NoticePojo noticePojo = noticePojoArrayList.get(position);
        holder.txtdate.setText(noticePojoArrayList.get(position).getNotice_date());
        holder.txtCreatedBy.setText(noticePojoArrayList.get(position).getName());
        holder.txtSubject.setText(noticePojoArrayList.get(position).getSubject());
        holder.noticetype.setText(noticePojoArrayList.get(position).getNotice_type());
        holder.txtclassnoticcenm.setText(noticePojo.getClassname());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,ViewNoticeActivity.class);
                intent.putExtra("noticetype",noticePojoArrayList.get(position).getNotice_type());
                intent.putExtra("Noticeid",noticePojoArrayList.get(position).getNotice_id());
                intent.putExtra("imagename",noticePojoArrayList.get(position).getImagename());
                intent.putExtra("classname",noticePojoArrayList.get(position).getClassname());
                intent.putExtra("classid",noticePojoArrayList.get(position).getClass_id());

                String url= NoticeActivity.getSelectedHomeworkStatus();
                intent.putExtra("url",url);

                context.startActivity(intent);
                ((Activity)context).finish();
            }
        });

    }

    @Override
    public int getItemCount() {
        return noticePojoArrayList.size();
    }

    public class Datahold extends RecyclerView.ViewHolder {
        private TextView txtdate, txtCreatedBy, txtSubject,txtclassnoticcenm,noticetype;
        TextView tv_loading;

        public Datahold(View itemView) {
            super(itemView);
            txtdate = itemView.findViewById(R.id.notice_date);
            txtCreatedBy = itemView.findViewById(R.id.notice_createdby);
            txtSubject = itemView.findViewById(R.id.subject_notice);
            txtclassnoticcenm = itemView.findViewById(R.id.classnoticcenm);
            noticetype = itemView.findViewById(R.id.noticetype);
        }
    }
}
