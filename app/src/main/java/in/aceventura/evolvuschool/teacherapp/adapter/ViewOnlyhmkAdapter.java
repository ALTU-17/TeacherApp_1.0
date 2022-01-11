package in.aceventura.evolvuschool.teacherapp.adapter;

import android.app.Dialog;
import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import in.aceventura.evolvuschool.teacherapp.R;
import in.aceventura.evolvuschool.teacherapp.pojo.ViewOnlyHmkPojo;

public class ViewOnlyhmkAdapter extends RecyclerView.Adapter<ViewOnlyhmkAdapter.Datahold>{
    private Context context;
private ArrayList<ViewOnlyHmkPojo> viewOnlyHmkPojoArrayList;

    public ViewOnlyhmkAdapter(Context context, ArrayList<ViewOnlyHmkPojo> viewOnlyHmkPojoArrayList) {
        this.context = context;
        this.viewOnlyHmkPojoArrayList = viewOnlyHmkPojoArrayList;
    }

    @Override
    public Datahold onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewonlyhmkadapter, parent, false);
        return new Datahold(view);

    }

    @Override
    public void onBindViewHolder(Datahold holder, final int position) {

        holder.txtclass.setText(viewOnlyHmkPojoArrayList.get(position).getClassname()+viewOnlyHmkPojoArrayList.get(position).getSectionname());
//    holder.txtassigndate.setText(viewOnlyHmkPojoArrayList.get(position).getAssigndate());
//    holder.txtassigndate.setText(viewOnlyHmkPojoArrayList.get(position).getPubDate());

        String cdate = viewOnlyHmkPojoArrayList.get(position).getAssigndate();
        String pdate = viewOnlyHmkPojoArrayList.get(position).getPubDate();



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
            holder.txtassigndate.setText(pdate);
        }
        /*else{
//            holder.tvcdate.setVisibility(View.VISIBLE);
            holder.txtrmkdate.setText(cdate);
//            holder.txtedate.setText("");
            holder.tvCrdate.setVisibility(View.VISIBLE);
            holder.tvPrdate.setVisibility(View.GONE);
//            holder.vetical1.setVisibility(View.VISIBLE);
        }*/














        holder.txtsubmitdate.setText(viewOnlyHmkPojoArrayList.get(position).getSubmitdate());
    holder.txtsub.setText(viewOnlyHmkPojoArrayList.get(position).getSubject());
    holder.imageviewhmkonly.setOnTouchListener(new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            String classsection=viewOnlyHmkPojoArrayList.get(position).getClassname()+viewOnlyHmkPojoArrayList.get(position).getSectionname();
            String desc=viewOnlyHmkPojoArrayList.get(position).getDesc();
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.popup);
            dialog.setTitle("Details");
            dialog.setCancelable(false);
//            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            TextView txt=dialog.findViewById(R.id.classnmhmk);
            TextView txtseac=dialog.findViewById(R.id.deschmk);
            Button txtcloe=dialog.findViewById(R.id.closePopupBtnhmk);
            txtseac.setText(desc);
            txt.setText(classsection);
            txtcloe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
            return false;
        }
    });
    }

    @Override
    public int getItemCount() {
        return viewOnlyHmkPojoArrayList.size();
    }

    public class Datahold extends RecyclerView.ViewHolder {
        private TextView txtassigndate,txtsubmitdate,txtclass,txtsub;

        private ImageView imageviewhmkonly;
        public Datahold(View itemView) {
            super(itemView);
            txtsub=itemView.findViewById(R.id.viewonlyhmksub);
            txtassigndate=itemView.findViewById(R.id.viewonlyhmkassigndate);
            txtsubmitdate=itemView.findViewById(R.id.viewonlyhmksubmitdate);
            txtclass=itemView.findViewById(R.id.viewonlyhmkclass);
            imageviewhmkonly=itemView.findViewById(R.id.viewhmkonly);

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
