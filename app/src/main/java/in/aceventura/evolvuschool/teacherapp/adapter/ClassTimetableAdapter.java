package in.aceventura.evolvuschool.teacherapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;


import in.aceventura.evolvuschool.teacherapp.R;
import in.aceventura.evolvuschool.teacherapp.pojo.DataSet;



public class ClassTimetableAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<DataSet> DataList;


    public ClassTimetableAdapter(Activity activity, List<DataSet> timetableItems) {
        this.activity = activity;
        this.DataList = timetableItems;
    }

    @Override
    public int getCount() {
        return DataList.size();
    }

    @Override
    public Object getItem(int location) {
        return DataList.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.timetable_row, null);

        //Integer period_no = 0;
        TextView period_no = convertView.findViewById(R.id.period_no);
        TextView time = convertView.findViewById(R.id.class_time);
        TextView sub_monday = convertView.findViewById(R.id.sub_monday);
        TextView sub_tuesday = convertView.findViewById(R.id.sub_tuesday);
        TextView sub_wednesday = convertView.findViewById(R.id.sub_wednesday);
        TextView sub_thrusday = convertView.findViewById(R.id.sub_thrusday);
        TextView sub_friday = convertView.findViewById(R.id.sub_friday);
        TextView sub_sat_time = convertView.findViewById(R.id.sub_saturday_period);
        //TextView sub_sat_class_out = (TextView) convertView.findViewById(R.id.sub_saturday_period);
        TextView sub_saturday = convertView.findViewById(R.id.sub_saturday);
        DataSet m = DataList.get(position);

        period_no.setText(m.getSubject());
        time.setText(m.getClassIn()+"-"+m.getClassOut());
        sub_monday.setText(m.getTtMonday());
        sub_tuesday.setText(m.getTtTuesday());
        sub_wednesday.setText(m.getTtWednesday());
        sub_thrusday.setText(m.getTtThursday());
        sub_friday.setText(m.getTtFriday());
        sub_sat_time.setText(m.getSatClassIn()+"-"+m.getSatClassOut());
        sub_saturday.setText(m.getTtSaturday());
        return convertView;
    }

}

