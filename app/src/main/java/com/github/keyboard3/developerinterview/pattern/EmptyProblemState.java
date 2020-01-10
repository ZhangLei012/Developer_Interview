package com.github.keyboard3.developerinterview.pattern;

import com.github.keyboard3.developerinterview.R;



public class EmptyProblemState extends BaseProblemState {
    @Override
    protected String getProblemStateName() {
        return "空题目类型";
    }

    @Override
    protected int getProblemStateId() {
        return 0;
    }

    @Override
    protected int getProblemIcon() {
        return R.mipmap.ic_launcher;
    }
}
