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
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.aceventura.evolvuschool.teacherapp.R;
import in.aceventura.evolvuschool.teacherapp.pojo.RemarkStudentListPojo;

public class RemarkStudentsListAdapter extends RecyclerView.Adapter<RemarkStudentsListAdapter.DataHold> {
    private Context context;
    private ArrayList<RemarkStudentListPojo> listStudent;
    private ArrayList idArrayList;


    public RemarkStudentsListAdapter(Context context, ArrayList<RemarkStudentListPojo> listStudent, ArrayList idArrayList) {
        this.context = context;
        this.listStudent = listStudent;
        this.idArrayList = idArrayList;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public DataHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.remarks_students_list, parent, false);
        return new DataHold(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DataHold holder, final int position) {
        holder.txtRollno.setText(listStudent.get(position).getRollno());
        holder.checkBoxRemarkStatus.setTag(listStudent.get(position).getStudent_id());

        if (listStudent.get(position).getlName().equalsIgnoreCase("null")) {
            holder.txtname.setText(listStudent.get(position).getfName());
        } else {
            holder.txtname.setText(listStudent.get(position).getfName() + " " + listStudent.get(position).getlName());
        }


        //checking if checkboxes are already checked...
        if (listStudent.get(position).isCheckedStatus()){
            holder.checkBoxRemarkStatus.setChecked(true);
        }
        else{
            holder.checkBoxRemarkStatus.setChecked(false);
        }

        holder.checkBoxRemarkStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (holder.checkBoxRemarkStatus.isChecked()) {
                    listStudent.get(position).setCheckedStatus(true);
//                    String StudentId = holder.checkBoxRemarkStatus.getTag().toString();
                    String StudentId ='"' + holder.checkBoxRemarkStatus.getTag().toString() + '"';
                    idArrayList.add(StudentId);
                } else if (!holder.checkBoxRemarkStatus.isChecked()) {
                    listStudent.get(position).setCheckedStatus(false);
//                    String StudentId =holder.checkBoxRemarkStatus.getTag().toString();
                    String StudentId ='"' + holder.checkBoxRemarkStatus.getTag().toString()+ '"';
                    idArrayList.remove(StudentId);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listStudent.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class DataHold extends RecyclerView.ViewHolder {
        TextView txtname, txtRollno;
        RelativeLayout linearLayout;
        CheckBox checkBoxRemarkStatus;

        DataHold(View itemView) {
            super(itemView);
            txtname = itemView.findViewById(R.id.txtname);
            checkBoxRemarkStatus = itemView.findViewById(R.id.checkBoxRemarkStatus);
            txtRollno = itemView.findViewById(R.id.txtrollno);
            linearLayout = itemView.findViewById(R.id.linearattendance);
        }
    }
}
