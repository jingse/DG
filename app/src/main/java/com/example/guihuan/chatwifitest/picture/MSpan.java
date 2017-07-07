package com.example.guihuan.chatwifitest.picture;

import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;

/**
 * Created by GuiHuan on 2017/7/7.
 */

public class MSpan extends ClickableSpan implements View.OnClickListener {
    @Override
    public void onClick(View widget) {
        // TODO Auto-generated method stub
        Log.e("test", "aaaaaaaaaaaa");
        //Toast.makeText(ChatActivity.class, "sdfdsfsdfdsf", 1000).show();
    }
}