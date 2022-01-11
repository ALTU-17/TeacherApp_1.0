package in.aceventura.evolvuschool.teacherapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import in.aceventura.evolvuschool.teacherapp.R;
import in.aceventura.evolvuschool.teacherapp.pojo.CalendarModel;

public class Calendar_EventsWishAdapter extends RecyclerView.Adapter<Calendar_EventsWishAdapter.ViewHolder> {

    private List<CalendarModel> mValues;
    private List<CalendarModel> mValuesCopy = new ArrayList<>();

    private Context context;

    public Calendar_EventsWishAdapter(Context context, List<CalendarModel> items) {
        mValues = items;

        this.context = context;
        this.mValuesCopy.addAll(mValues);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_events, parent, false);
        return new ViewHolder(view);
    }

    private static final String TAG = "CalendarEventsAdapter";

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        CalendarModel model = mValuesCopy.get(position);

        holder.tvEventName.setText(DateFormat.format("dd-MM-yyyy", new Date(Long.parseLong(String.valueOf(model.getStart_date())))).toString() +"   "+ model.getTitle());
        try {
            holder.ll_color.setBackgroundColor(Color.parseColor((model.getColor())));//#000000

        } catch (Exception e) {
            e.printStackTrace();
        }
       /* if (String.valueOf(model.getEnd_date()).toString().equals("null")){

        }else {
            holder.tvEventName.setText(DateFormat.format("dd-MM-yyyy", new Date(Long.parseLong(String.valueOf(model.getEnd_date())))).toString() +"   "+ model.getTitle()+"End's");
            try {
                holder.ll_color.setBackgroundColor(Color.parseColor((model.getColor())));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/



    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public CalendarModel mItem;
        public final TextView tvEventName;
        LinearLayout ll_color;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            tvEventName = view.findViewById(R.id.tv_event_name);

            ll_color = view.findViewById(R.id.ll_color);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + tvEventName.getText() + "'";
        }
    }


}
