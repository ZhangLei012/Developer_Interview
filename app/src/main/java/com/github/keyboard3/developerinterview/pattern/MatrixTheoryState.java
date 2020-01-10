package com.github.keyboard3.developerinterview.pattern;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import com.github.keyboard3.developerinterview.R;
import com.github.keyboard3.developerinterview.fragment.ProductsFragment;



public class MatrixTheoryState extends BaseProblemState {
    public static final int ID = 7;
    public static final String NAME = "MatrixTheory";
    public static final int ICON = R.mipmap.ic_matrix_theory;
    public static final int MENU_ID = R.id.menu_matrix_theory;

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
