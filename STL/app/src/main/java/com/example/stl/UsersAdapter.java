package com.example.stl;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.CustomViewHolder>{
    private ArrayList<PersonalData> UList=null;
    private Activity Ucontext=null;
    private OnItemClickListener UListener=null;

    public UsersAdapter(Activity context, ArrayList<PersonalData> list){
        this.Ucontext=context;
        this.UList=list;
    }

    // SearchListener Activity에서 어댑터에 리스너 객체 참조를 전달
    public void setOnItemClickListener(OnItemClickListener listener){
        this.UListener=listener;
    }

    // 새 OnItemClickListener 인터페이스 정의
    public interface OnItemClickListener{
        void GetItem(View v, int position);
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

            //View가 클릭되면
            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){

                    //getAdapterPosition을 통해 pos을 받아온다.
                    int pos=getAdapterPosition();

                    // View Holder가 갱신중에는 null을 반환하기 때문에
                    // null인지 check 해줘야 한다.
                    if(UListener!=null)
                        UListener.GetItem(v,pos);
                }
            });


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
        viewholder.starting_point.setText(UList.get(position).getMember_Start());
        viewholder.point.setText("->");
        viewholder.destination.setText(UList.get(position).getMember_Goal());
    }

    @Override
    public int getItemCount(){
        return (null!=UList?UList.size():0);
    }

}
