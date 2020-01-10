package com.github.keyboard3.developerinterview.pattern;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import com.github.keyboard3.developerinterview.fragment.ProblemsFragment;
import com.github.keyboard3.developerinterview.R;



public class MachineLearningState extends BaseProblemState {
    public static final int ID = 1;
    public static final String NAME = "ProblemMachineLearning";
    public static final int ICON = R.mipmap.ic_machine_learning;
    public static final int MENU_ID = R.id.menu_machine_learning;

    @Override
    protected String getProblemStateName() {
        return NAME;
    }

    @Override
    protected int getProblemStateId() {
        return ID;
    }

    @Override
    protected int getProblemIcon() {
        return ICON;
    }
}
