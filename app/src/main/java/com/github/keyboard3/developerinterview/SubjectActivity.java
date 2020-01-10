package com.github.keyboard3.developerinterview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.github.keyboard3.developerinterview.adapter.SubjectAdapter;
import com.github.keyboard3.developerinterview.entity.Subject;

import java.util.ArrayList;
import java.util.List;

public class SubjectActivity extends AppCompatActivity {
    private List<Subject> subjectList=new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subject_classification);
        initSubjects();
        RecyclerView subjectRecycleView = (RecyclerView)findViewById(R.id.subject_recycler_view);
        StaggeredGridLayoutManager layoutManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        subjectRecycleView.setLayoutManager(layoutManager);
        SubjectAdapter adapter=new SubjectAdapter(subjectList,SubjectActivity.this);
        subjectRecycleView.setAdapter(adapter);
    }

    private void initSubjects(){
        Subject machineLearning=new Subject("MachineLearning",R.mipmap.machine_learning_pic);
        subjectList.add(machineLearning);
    }
}
