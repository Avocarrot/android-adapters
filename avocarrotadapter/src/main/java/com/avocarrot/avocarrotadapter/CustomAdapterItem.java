package com.avocarrot.avocarrotadapter;

import android.content.res.Resources;

public class CustomAdapterItem {

    private String name;
    private Class customAdapterClassName;
    private Class activityToLoad;

    public CustomAdapterItem(String name, Class customAdapterClassName, Class activityToLoad) {
        this.name = name;
        this.customAdapterClassName = customAdapterClassName;
        this.activityToLoad = activityToLoad;
    }

    public Class getCustomAdapterClassName() {
        return customAdapterClassName;
    }

    public Class getActivityToLoad() {
        return activityToLoad;
    }

    @Override
    public String toString() {
        return name;
    }
}
