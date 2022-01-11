package in.aceventura.evolvuschool.teacherapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.aceventura.evolvuschool.teacherapp.R;
import in.aceventura.evolvuschool.teacherapp.activities.ViewFullSizeImage;
import in.aceventura.evolvuschool.teacherapp.pojo.AnsPaperThumbnailModel;

/**
 * Created by "Manoj Waghmare" on 07,Sep,2020
 **/
public class AnsPaperAdapter extends RecyclerView.Adapter<AnsPaperAdapter.ViewHolder> {

    private Context context;
    private ArrayList<AnsPaperThumbnailModel> ansPapersList;

    //constructor
    public AnsPaperAdapter(Context context, ArrayList<AnsPaperThumbnailModel> ansPapersList) {
        this.context = context;
        this.ansPapersList = ansPapersList;
    }

    //setting layout file
    @NonNull
    @Override
    public AnsPaperAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.ans_paper_thumbnail_view, parent, false)
        );
    }

    //Logic & Setting the values to UI
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final AnsPaperThumbnailModel ansPaperThumbnailModel = ansPapersList.get(position);
        holder.iv_anspaper.setBackground(ContextCompat.getDrawable(context, R.drawable.file_image));
        holder.img_name.setText(ansPaperThumbnailModel.getImgName());

        //on click of open it full screen View
        holder.card_Thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewFullSizeImage.class);// TODO: 07-09-2020 Change the activity
                intent.putExtra("url", ansPaperThumbnailModel.getImgUrl());
                context.startActivity(intent);
            }
        });
    }

    //setting count
    @Override
    public int getItemCount() {
        return ansPapersList.size();
    }

    //Get ItemId from position
    @Override
    public long getItemId(int position) {
        return position;
    }

    //Binding the views with components from layout file
    public static class ViewHolder extends RecyclerView.ViewHolder {

        CardView card_Thumbnail;
        ImageView iv_anspaper;
        TextView img_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            card_Thumbnail = itemView.findViewById(R.id.thumbnail);
            iv_anspaper = itemView.findViewById(R.id.iv_anspaper);
            img_name = itemView.findViewById(R.id.img_name);
        }
    }
}

