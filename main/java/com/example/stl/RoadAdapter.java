package com.example.stl;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class RoadAdapter extends RecyclerView.Adapter<RoadAdapter.CustomViewHolder>{
    private ArrayList<RoadData> RList=null;
    private Activity Rcontext=null;

    public RoadAdapter(Activity context, ArrayList<RoadData> list){
        this.Rcontext=context;
        this.RList=list;
    }


    class CustomViewHolder extends RecyclerView.ViewHolder{
        protected ImageView Guide_image;
        protected TextView Guide;


        public CustomViewHolder(View view){
            super(view);
            this.Guide=(TextView)view.findViewById(R.id.route_route);
            this.Guide_image=(ImageView)view.findViewById(R.id.route_image);
        }
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.route_item_list,null);
        CustomViewHolder viewHolder=new CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position){
        String Guide;
        char guide_sep;
        Guide=RList.get(position).getMember_Guide();
        guide_sep=Guide.charAt(0);

        //직진
        if('0'<=guide_sep && guide_sep<='9'){
            viewholder.Guide_image.setImageResource(R.mipmap.go);
        }else if(guide_sep=='오'){
            viewholder.Guide_image.setImageResource(R.mipmap.right_arrow);
        }else if(guide_sep=='왼'){
            viewholder.Guide_image.setImageResource(R.mipmap.left);
        }else{
            viewholder.Guide_image.setImageResource(R.mipmap.pin_mini);
        }

        viewholder.Guide.setText(Guide);
    }

    @Override
    public int getItemCount(){
        return (null!=RList?RList.size():0);
    }

}
