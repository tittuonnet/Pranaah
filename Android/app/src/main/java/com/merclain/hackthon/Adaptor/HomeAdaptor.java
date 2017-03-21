package com.merclain.hackthon.Adaptor;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.merclain.hackthon.DetailView.MedicalData;
import com.merclain.hackthon.Model.PranaahList;
import com.merclain.hackthon.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ajith on 21/3/17.
 */

public class HomeAdaptor extends RecyclerView.Adapter<HomeAdaptor.ViewHolder> {

    private Context context;
    List<PranaahList> Events;
    public HomeAdaptor(List<PranaahList> Events, Context context){
        super();
        this.Events = Events;
        this.context = context;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.familyview, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final PranaahList family = Events.get(position);
        holder.textViewName.setText(family.getName());
        holder.textViewDOB.setText(family.getDob());
        holder.teamOneGender.setText(family.getGender());
        holder.pulse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               context.startActivity(new Intent(context, MedicalData.class));
            }
        });

    }
    @Override
    public int getItemCount() {
        return Events.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewName;
        public TextView textViewDOB;
        public TextView teamOneGender;
        public ImageView pulse;
        public CircleImageView ProfilePhoto;
        public ViewHolder(View itemView) {
            super(itemView);

            textViewName = (TextView) itemView.findViewById(R.id.TVUserName);
            textViewDOB = (TextView) itemView.findViewById(R.id.TVUserDob);
            teamOneGender = (TextView) itemView.findViewById(R.id.tvUserGender);
            pulse = (ImageView) itemView.findViewById(R.id.userMedicalId);
        }

    }
}
