package com.github.keyboard3.developerinterview.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.keyboard3.developerinterview.R;
import com.github.keyboard3.developerinterview.entity.Problem;
import com.github.keyboard3.developerinterview.pattern.ProblemStateFactory;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ProblemAdapter extends BaseAdapter<ProblemAdapter.MyViewHolder, Problem> {


    public ProblemAdapter(List<Problem> data, Activity activity) {
        super(data, activity);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder viewHolder = new MyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_problem, parent, false));
        initViewHolder(viewHolder);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.setPosition(position);
        Problem entity = data.get(position);

        ProblemStateFactory.getProblemStateById(entity.type)
                            .setImageViewIcon(holder.icon);

        holder.tvTitle.setText(entity.title);
        holder.tvContent.setText(entity.content);
        holder.like.setText("0赞");
        holder.review.setText("0评论");
        holder.forward.setText("0转发");
    }

    public static class MyViewHolder extends BaseAdapter.ViewHolder {
        @BindView(R.id.iv_type) public ImageView icon;
        @BindView(R.id.tv_title) public TextView tvTitle;
        @BindView(R.id.tv_content) public TextView tvContent;
        @BindView(R.id.like) public TextView like;
        @BindView(R.id.review) public TextView review;
        @BindView(R.id.forward) public TextView forward;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
