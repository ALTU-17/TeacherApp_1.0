package in.aceventura.evolvuschool.teacherapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.aceventura.evolvuschool.teacherapp.R;
import in.aceventura.evolvuschool.teacherapp.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.teacherapp.pojo.ViewedByListPojo;

public class HomeworkViewedByAdapter extends RecyclerView.Adapter<HomeworkViewedByAdapter.DataHold> {
    private Context context;
    private ArrayList<ViewedByListPojo> viewedByArrayListPojo;

    public HomeworkViewedByAdapter(Context context, ArrayList<ViewedByListPojo> viewedByArrayListPojo) {
        this.context = context;
        this.viewedByArrayListPojo = viewedByArrayListPojo;


        DatabaseHelper mDatabaseHelper = new DatabaseHelper(context);
        String name = mDatabaseHelper.getName(1);
        String newUrl = mDatabaseHelper.getTURL(1);

        if (name == null || name.equals("")) {
            name = mDatabaseHelper.getName(1);
            newUrl = mDatabaseHelper.getTURL(1);
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public DataHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.homework_viewdby_item, parent, false);
        return new DataHold(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DataHold holder, final int position) {
        //get roll num
        if ((viewedByArrayListPojo.get(position).getRollno()== null) || viewedByArrayListPojo.get(position).getRollno().equals("null") || viewedByArrayListPojo.get(position).getRollno().equals("")){
            holder.txtRollno.setText("");
        }else{
            holder.txtRollno.setText(viewedByArrayListPojo.get(position).getRollno());
        }

        //get the list of students
        if (viewedByArrayListPojo.get(position).getlName().equalsIgnoreCase("null")) {
            holder.txtname.setText(viewedByArrayListPojo.get(position).getfName());
        } else {
            holder.txtname.setText(viewedByArrayListPojo.get(position).getfName() + " " + viewedByArrayListPojo.get(position).getlName());
        }

        //setting viewed by icon
        //read
        if (viewedByArrayListPojo.get(position).getRead_status().equals("1")){
            holder.viewedByIcon.setImageResource(R.drawable.ic_user_check_solid);
        }
        //unread
        else if (viewedByArrayListPojo.get(position).getRead_status().equals("0")){
            holder.viewedByIcon.setImageResource(R.drawable.ic_user_slash_solid);
        }


    }

    @Override
    public int getItemCount() {
        return viewedByArrayListPojo.size();
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
        ImageView viewedByIcon;


        DataHold(View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.linearattendance);
            txtname = itemView.findViewById(R.id.txtname);
            txtRollno = itemView.findViewById(R.id.txtrollno);
            viewedByIcon = itemView.findViewById(R.id.viewedByIcon);
        }
    }
}
