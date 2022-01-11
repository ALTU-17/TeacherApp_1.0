package in.aceventura.evolvuschool.teacherapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.aceventura.evolvuschool.teacherapp.R;
import in.aceventura.evolvuschool.teacherapp.pojo.Checkbalance;

public class CheckLeaveBalanceAdapter extends RecyclerView.Adapter<CheckLeaveBalanceAdapter.DataHold> {

    private Context context;
    private ArrayList<Checkbalance> checkbalanceArrayList;
    private String leaveAlloted, availedleave;
    private float balanceleave;

    public CheckLeaveBalanceAdapter(Context context, ArrayList<Checkbalance> checkbalanceArrayList) {
        this.context = context;
        this.checkbalanceArrayList = checkbalanceArrayList;
    }

    @Override
    public DataHold onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.checkbalanceadapter_lay, parent, false);
        return new DataHold(view);
    }

    @Override
    public void onBindViewHolder(DataHold holder, int position) {

        holder.txtleaveType.setText(checkbalanceArrayList.get(position).getLeavetype());
        leaveAlloted = checkbalanceArrayList.get(position).getLeavesalloted();
        availedleave = checkbalanceArrayList.get(position).getLeavesavailed();
        balanceleave = Float.valueOf(leaveAlloted) - Float.valueOf(availedleave);
        String myLeavesBalance = Float.toString(balanceleave);
        holder.txtleaveBalance.setText(myLeavesBalance);
    }

    @Override
    public int getItemCount() {
        return checkbalanceArrayList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    public class DataHold extends RecyclerView.ViewHolder {
        TextView txtleaveType, txtleaveBalance;

        public DataHold(View itemView) {
            super(itemView);
            txtleaveType = itemView.findViewById(R.id.leavetype);
            txtleaveBalance = itemView.findViewById(R.id.leavebalance);
        }
    }
}
