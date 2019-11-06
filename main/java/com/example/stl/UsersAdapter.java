package com.example.stl;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.CustomViewHolder>{
    private ArrayList<PersonalData> UList=null;
    private Activity Ucontext=null;

    public UsersAdapter(Activity context, ArrayList<PersonalData> list){
        this.Ucontext=context;
        this.UList=list;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder{
        protected TextView starting_point;
        protected TextView point;
        protected TextView destination;

        public CustomViewHolder(View view){
            super(view);
            this.point=(TextView)view.findViewById(R.id.point);
            this.starting_point=(TextView)view.findViewById(R.id.textView_list_start);
            this.destination=(TextView)view.findViewById(R.id.textView_list_destination);

        }
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_item_list,null);
        CustomViewHolder viewHolder=new CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position){
        viewholder.starting_point.setText(UList.get(position).getMember_id());
        viewholder.point.setText("->");
        viewholder.destination.setText(UList.get(position).getMember_name());
    }

    @Override
    public int getItemCount(){
        return (null!=UList?UList.size():0);
    }

}
