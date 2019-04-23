package com.openwudi.animal.base;

import android.content.Context;

/**
 * Created by diwu on 16/7/6.
 */
public abstract class BasePresenter<E, T> {
    public Context mContext;
    public E mModel;
    public T mView;

    public void setVM(Context context, T v, E m) {
        this.mContext = context;
        setVM(v, m);
    }

    public void setVM(T v, E m) {
        this.mView = v;
        this.mModel = m;
        this.onStart();
    }

    public abstract void onStart();

    public void onDestroy() {
    }
}
