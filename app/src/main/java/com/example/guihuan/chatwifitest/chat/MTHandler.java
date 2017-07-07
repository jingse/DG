package com.example.guihuan.chatwifitest.chat;

import android.content.Context;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;

import com.example.guihuan.chatwifitest.picture.MSpan;

import org.xml.sax.XMLReader;

/**
 * Created by GuiHuan on 2017/7/7.
 */

class MTagHandler implements Html.TagHandler {

    private int sIndex = 0;
    private int eIndex = 0;
    private final Context mContext;

    public MTagHandler(Context context) {
        mContext = context;
    }

    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
        // TODO Auto-generated method stub
        if (tag.toLowerCase().equals("img")) {
            if (opening) {
                sIndex = output.length();
            } else {
                eIndex = output.length();
                output.setSpan(new MSpan(), sIndex, eIndex,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }
}



