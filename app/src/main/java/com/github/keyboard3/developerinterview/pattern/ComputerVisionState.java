package com.github.keyboard3.developerinterview.pattern;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import com.github.keyboard3.developerinterview.fragment.ProblemsFragment;
import com.github.keyboard3.developerinterview.R;


public class ComputerVisionState extends BaseProblemState {
    public static final int ID = 5;
    public static final String NAME = "ComputerVision";
    public static final int ICON = R.mipmap.ic_computer_vision;
    public static final int MENU_ID = R.id.menu_computer_vision;

    static class Single {
        static ComputerVisionState instance = new ComputerVisionState();
    }

    public static ComputerVisionState getInstance() {
        return Single.instance;
    }

    @Override
    public BaseProblemState setFragmentByProblemStateName(FloatingActionButton fab, FragmentManager fragmentManager) {
        fab.setVisibility(View.VISIBLE);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, ProblemsFragment.newInstance(this));
        fragmentTransaction.commit();
        return this;
    }

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
