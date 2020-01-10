package com.github.keyboard3.developerinterview.pattern;

import com.github.keyboard3.developerinterview.R;

/**
 * 自己的作品状态
 *
 * @author keyboard3
 * @date 2017/9/7
 */

public class JavaState extends BaseProblemState {
    public static final int ID = 8;
    public static final String NAME = "ProblemJava";
    public static final int ICON = R.mipmap.ic_menu_java;
    public static final int MENU_ID = R.id.menu_java;

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
