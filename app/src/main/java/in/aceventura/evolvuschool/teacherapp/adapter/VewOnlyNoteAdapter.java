package in.aceventura.evolvuschool.teacherapp.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import in.aceventura.evolvuschool.teacherapp.R;
import in.aceventura.evolvuschool.teacherapp.activities.ViewTeacherOnlyNoteActivity;
import in.aceventura.evolvuschool.teacherapp.pojo.Viewonlynotespojo;

public class VewOnlyNoteAdapter extends RecyclerView.Adapter<VewOnlyNoteAdapter.Datahold> {
    private Context context;
    private ArrayList<Viewonlynotespojo> viewonlynotespojoArrayList;

    public VewOnlyNoteAdapter(Context context, ArrayList<Viewonlynotespojo> viewonlynotespojoArrayList) {
        this.context = context;
        this.viewonlynotespojoArrayList = viewonlynotespojoArrayList;
    }

    @Override
    public Datahold onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewonlynoteadapter, parent, false);
        return new Datahold(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(Datahold holder, final int position) {
        String noteid = viewonlynotespojoArrayList.get(position).getNotesid();
 holder.txtclass.setText(viewonlynotespojoArrayList.get(position).getClasname()+viewonlynotespojoArrayList.get(position).getSectionname());
String subname=viewonlynotespojoArrayList.get(position).getSubjectname();
if (subname.equalsIgnoreCase("null")){
    holder.txtsub.setVisibility(View.GONE);
    holder.vetical1.setVisibility(View.GONE);

}
else {
    holder.txtsub.setText(viewonlynotespojoArrayList.get(position).getSubjectname());

}
        String pdate = viewonlynotespojoArrayList.get(position).getPublishDate();
        if (pdate != null && !pdate.isEmpty() && !pdate.equals("null")){
            SimpleDateFormat spf=new SimpleDateFormat("yyyy-MM-dd");
            Date newDate= null;
            try {
                newDate = spf.parse(pdate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            spf= new SimpleDateFormat("dd-MM-yyyy");
            pdate = spf.format(newDate);
            holder.txtdate.setText(pdate);

        }


holder.descriptionnote.setText(viewonlynotespojoArrayList.get(position).getNotice_desc());
            holder.itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
//                    String classsection=viewonlynotespojoArrayList.get(position).getClasname()+viewonlynotespojoArrayList.get(position).getSectionname();
//                    String desc=viewonlynotespojoArrayList.get(position).getNotice_desc();



//                    String teacherid = viewonlynotespojoArrayList.get(position).getTeacher_id();
                    String classname = viewonlynotespojoArrayList.get(position).getClasname();
//                    String academicYr = viewonlynotespojoArrayList.get(position).getAcademic_yr();
                    String sectionname = viewonlynotespojoArrayList.get(position).getSectionname();
                    String subjectname = viewonlynotespojoArrayList.get(position).getSubjectname();
                    String desc = viewonlynotespojoArrayList.get(position).getNotice_desc();
                    String date = viewonlynotespojoArrayList.get(position).getNotes_date();
                    String pbndate=viewonlynotespojoArrayList.get(position).getPublishDate();
                    String sectionid = viewonlynotespojoArrayList.get(position).getSectionid();
                    String noteid =  viewonlynotespojoArrayList.get(position).getNotesid();

                    Intent intent = new Intent(context, ViewTeacherOnlyNoteActivity.class);
//                    intent.putExtra("teacherid", teacherid);
                    intent.putExtra("classname", classname);
//                    intent.putExtra("academicYr", academicYr);
                    intent.putExtra("sectionname", sectionname);
                    intent.putExtra("subjectname", subjectname);
                    intent.putExtra("desc1", desc);
                    intent.putExtra("date", date);
                    intent.putExtra("noteid",noteid);


                    if (!pbndate.equals("null")){
                        SimpleDateFormat spf=new SimpleDateFormat("yyyy-MM-dd");
                        Date newDate= null;
                        try {
                            newDate = spf.parse(pbndate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        spf= new SimpleDateFormat("dd-MM-yyyy");
                        pbndate = spf.format(newDate);
                        intent.putExtra("publish_date",pbndate);
                    }else {
                        pbndate="";
                        intent.putExtra("publish_date",pbndate);
                    }

                    intent.putExtra("sectionid", sectionid);
                    context.startActivity(intent);
                    ((Activity)context).finish();


                    /*final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.popup);
                    dialog.setTitle("Details");
                    dialog.setCancelable(false);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    TextView txt=dialog.findViewById(R.id.classnmhmk);
                    TextView txtseac=dialog.findViewById(R.id.deschmk);
                    TextView txtcloe=dialog.findViewById(R.id.closePopupBtnhmk);
                    txtseac.setText(desc);
                    txt.setText(classsection);
                    txtcloe.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();*/
                    return false;
                }
            });
    }

    @Override
    public int getItemCount() {
        return viewonlynotespojoArrayList.size();
    }

    class Datahold extends RecyclerView.ViewHolder {
        TextView txtdate,txtclass,txtsub,vetical1,descriptionnote;
        private ImageView imageView;
        Datahold(View itemView) {
            super(itemView);
            txtdate=itemView.findViewById(R.id.viewonlynotedate);
            txtsub=itemView.findViewById(R.id.viewonlynotesub);
            txtclass=itemView.findViewById(R.id.viewonlynoteclass);
            imageView=itemView.findViewById(R.id.viewnoteonly);
            vetical1=itemView.findViewById(R.id.vetical1);
            descriptionnote=itemView.findViewById(R.id.descriptionnote);

        }
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }

}
