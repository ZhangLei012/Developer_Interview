package com.github.keyboard3.developerinterview.pattern;

import com.github.keyboard3.developerinterview.R;



public class NLPState extends BaseProblemState {
    public static final int ID = 6;
    public static final String NAME = "NLP";
    public static final int ICON = R.mipmap.ic_nlp;
    public static final int MENU_ID = R.id.menu_nlp;

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
