package com.github.keyboard3.developerinterview.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.keyboard3.developerinterview.MainActivity;
import com.github.keyboard3.developerinterview.ProblemActivity;
import com.github.keyboard3.developerinterview.R;
import com.github.keyboard3.developerinterview.entity.Subject;

import java.util.List;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.ViewHolder>{
    private List<Subject> mSubjectList;
    Context context;
    static class ViewHolder extends RecyclerView.ViewHolder{
        View subjectView;
        ImageView subjectImage;
        TextView subjectName;
        public ViewHolder(View view){
            super(view);
            subjectView=view;
            subjectImage=(ImageView) view.findViewById(R.id.subject_image);
            subjectName=(TextView) view.findViewById(R.id.subject_name);
        }
    }
    public SubjectAdapter(List<Subject> subjectList, Context context){
        mSubjectList=subjectList;
        this.context=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_item,parent,false);
        final ViewHolder holder=new ViewHolder(view);
        holder.subjectView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position=holder.getAdapterPosition();
                Intent intent=new Intent(context, ProblemActivity.class);
                intent.putExtra("subjectPosition",position);
                context.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        Subject subject=mSubjectList.get(position);
        holder.subjectImage.setImageResource(subject.getImageId());
        holder.subjectName.setText(subject.getName());
    }

    @Override
    public  int getItemCount(){
        return mSubjectList.size();
    }
}
