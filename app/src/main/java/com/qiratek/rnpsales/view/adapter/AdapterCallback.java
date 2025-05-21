package com.qiratek.rnpsales.view.adapter;

import android.view.View;

public interface AdapterCallback {
    void bindView(View view, Object object);
    View.OnClickListener onClickItem(Object object);
}
