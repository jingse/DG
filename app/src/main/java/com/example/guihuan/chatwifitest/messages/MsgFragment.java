package com.example.guihuan.chatwifitest.messages;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.guihuan.chatwifitest.R;
import com.example.guihuan.chatwifitest.chat.ChatActivity;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.util.PtrLocalDisplay;


public class MsgFragment extends Fragment {


    private List<Msg> msgList = new ArrayList<>();
    private MsgAdapter adapter;
    private int actionSwitch;//用来切换对list的相应事件


    private PtrFrameLayout mPtrFrame;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_msg, container, false);

        mPtrFrame = view.findViewById(R.id.ptr);

        actionSwitch = 1;

        /**
         * 在 xml 中配置过了，就不要在这里配置了。
         */
        /*mPtrFrame.setResistance(1.7f); //阻尼系数 默认: 1.7f，越大，感觉下拉时越吃力。
        mPtrFrame.setRatioOfHeaderHeightToRefresh(1.2f); //触发刷新时移动的位置比例 默认，1.2f，移动达到头部高度1.2倍时可触发刷新操作。
        mPtrFrame.setDurationToClose(200);//回弹延时 默认 200ms，回弹到刷新高度所用时间
        mPtrFrame.setDurationToCloseHeader(1000);//头部回弹时间 默认1000ms
        mPtrFrame.setPullToRefresh(false);// 刷新是保持头部 默认值 true.
        mPtrFrame.setKeepHeaderWhenRefresh(true);//下拉刷新 / 释放刷新 默认为释放刷新*/

        /**
         * 经典 风格的头部实现
         */
        final PtrClassicDefaultHeader header = new PtrClassicDefaultHeader(getContext());
        header.setPadding(0, PtrLocalDisplay.dp2px(15), 0, 0);


        /**
         * StoreHouse风格的头部实现
         */
        /*final StoreHouseHeader header = new StoreHouseHeader(this);
        header.setPadding(0, PtrLocalDisplay.dp2px(15), 0, 0);*/

        /**
         * using a string, support: A-Z 0-9 - .
         * you can add more letters by {@link in.srain.cube.views.ptr.header.StoreHousePath#addChar}
         */
        // header.initWithString("Alibaba");


        /**
         * Material Design风格的头部实现
         */
        /*final MaterialHeader header = new MaterialHeader(getContext());
        header.setPadding(0, PtrLocalDisplay.dp2px(15), 0, 0);//显示相关工具类，用于获取用户屏幕宽度、高度以及屏幕密度。同时提供了dp和px的转化方法。*/


        /**
         * Rentals Style风格的头部实现
         * 这个需要引入这两个类RentalsSunDrawable.java ; RentalsSunHeaderView.java
         * 在人家git上的daemon中能找到
         */
       /* final RentalsSunHeaderView header = new RentalsSunHeaderView(this);

        header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
        header.setPadding(0, LocalDisplay.dp2px(15), 0, LocalDisplay.dp2px(10));
        header.setUp(mPtrFrame);
        mPtrFrame.setLoadingMinTime(1000);
        mPtrFrame.setDurationToCloseHeader(1500);*/


        // mPtrFrame = (PtrFrameLayout) findViewById(R.id.ptr);
        mPtrFrame.setHeaderView(header);
        // mPtrFrame.setPinContent(true);//刷新时，保持内容不动，仅头部下移,默认,false
        mPtrFrame.addPtrUIHandler(header);
        //mPtrFrame.setKeepHeaderWhenRefresh(true);//刷新时保持头部的显示，默认为true
        //mPtrFrame.disableWhenHorizontalMove(true);//如果是ViewPager，设置为true，会解决ViewPager滑动冲突问题。
        mPtrFrame.setPtrHandler(new PtrHandler() {

            //需要加载数据时触发
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                System.out.println("MainActivity.onRefreshBegin");
                mPtrFrame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPtrFrame.refreshComplete();



                        //mPtrFrame.autoRefresh();//自动刷新
                    }
                }, 1800);

            }

            /**
             * 检查是否可以执行下来刷新，比如列表为空或者列表第一项在最上面时。
             */
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                System.out.println("MainActivity.checkCanDoRefresh");
                // 默认实现，根据实际情况做改动
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
                // return true;
            }
        });




        adapter = new MsgAdapter(getContext(), R.layout.msg_item, msgList);
        ListView listView = view.findViewById(R.id.list_view);
        listView.setAdapter(adapter);

        initMessages();

        listView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Msg msg = msgList.get(position);
                msg.setClicked(true);

                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                int imageId = msg.getImageId();
                bundle.putString("chatting_friend_name", msg.getName()); //将朋友名称传进去
                bundle.putInt("chatting_friend_head", imageId); //将朋友头像传进去
                intent.setClass(getContext(), ChatActivity.class);
                intent.putExtras(bundle);

                startActivity(intent);

                /*if(actionSwitch == 1) {

                } else if(actionSwitch == 2) {

                }*/


            }
        });

        return view;
    }


    private void initMessages() {
        Msg friend1 = new Msg("1", R.drawable.head1, "我在南锣鼓巷", "10:00", false);
        msgList.add(friend1);
        Msg friend2 = new Msg("2", R.drawable.head2, "你好", "刚刚", false);
        msgList.add(friend2);
        Msg friend3 = new Msg("3", R.drawable.head3, "再见咯", "昨天", false);
        msgList.add(friend3);
        Msg friend4 = new Msg("4", R.drawable.head4, "吃了么？", "12:23", false);
        msgList.add(friend4);
        Msg group1 = new Msg("群", R.drawable.headgroup, "大家好", "13:30", false);
        msgList.add(group1);
    }

}
