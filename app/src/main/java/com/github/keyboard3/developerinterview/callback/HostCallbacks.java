package com.github.keyboard3.developerinterview.callback;

import android.content.Context;
import android.content.Intent;

import com.github.keyboard3.developerinterview.ConfigConst;
import com.github.keyboard3.developerinterview.http.HttpClient;
import com.qihoo360.replugin.RePluginCallbacks;



public class HostCallbacks extends RePluginCallbacks {
    private static String TAG = "HostCallbacks";

    public HostCallbacks(Context context) {
        super(context);
    }

    @Override
    public boolean onPluginNotExistsForActivity(final Context context, String plugin, Intent intent, int process) {
        return super.onPluginNotExistsForActivity(context, plugin, intent, process);
    }
}
