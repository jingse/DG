package com.example.guihuan.chatwifitest;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.guihuan.chatwifitest.contacts.ContactsFragment;
import com.example.guihuan.chatwifitest.contacts.MyFragmentPagerAdapter;
import com.example.guihuan.chatwifitest.jsip_ua.impl.DeviceImpl;
import com.example.guihuan.chatwifitest.messages.MsgFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.guihuan.chatwifitest.Var.myName;
import static com.example.guihuan.chatwifitest.Var.serverSip;


public class MainActivity extends FragmentActivity {

    private ViewPager viewpager;//页卡内容
    private ImageView imageView;// 动画图片
    private TextView textView1, textView2;
    private ArrayList<Fragment> listfragment;// Tab页面列表
    private int offset = 0;// 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号
    private int bmpW;// 动画图片宽度


    private RelativeLayout mainToolBar;
    private ImageButton more_btn;
    private PopupWindow more_window;
    private View menuView;
    private ListView menuListView;
    private List<Map<String, String>> listRight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE); // 注意顺序
        setContentView(R.layout.main);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.main_title_bar);

        Intent intent = this.getIntent();
        myName = intent.getExtras().getString("user_name");

        // 将标题栏的用户名设为正在聊天的人的用户名
        TextView my_name = findViewById(R.id.my_name);
        my_name.setText( myName);


        InitImageView();
        InitTextView();
        InitViewPager();
        initParam();
    }

    private void InitViewPager() {
        viewpager = findViewById(R.id.vPager);
        listfragment = new ArrayList<>(); //new一个List<Fragment>
        Fragment f1 = new MsgFragment();
        Fragment f2 = new ContactsFragment();
        listfragment.add(f1);
        listfragment.add(f2);

        FragmentManager fm = getSupportFragmentManager();
        MyFragmentPagerAdapter mfpa = new MyFragmentPagerAdapter(fm, listfragment);

        viewpager.setAdapter(mfpa);
        viewpager.setCurrentItem(0); //设置当前页是第一页
        viewpager.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    /**
     * 初始化头标
     */

    private void InitTextView() {
        textView1 = findViewById(R.id.text1);
        textView2 = findViewById(R.id.text2);

        textView1.setOnClickListener(new MyOnClickListener(0));
        textView2.setOnClickListener(new MyOnClickListener(1));
    }

    /**
     * 2      * 初始化动画
     * 3
     */

    private void InitImageView() {
        imageView = findViewById(R.id.cursor);
        BitmapFactory.Options options = new BitmapFactory.Options();
        BitmapFactory.decodeResource(getResources(), R.drawable.ic_downline, options);
        bmpW = options.outWidth;
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;// 获取分辨率宽度
        offset = screenW / 2;// 计算偏移量
        Matrix matrix = new Matrix();
        matrix.postTranslate(0, 0);
        imageView.setImageMatrix(matrix);// 设置动画初始位置
    }

    /**
     * 头标点击监听 3
     */
    private class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        public void onClick(View v) {
            viewpager.setCurrentItem(index);
        }

    }


    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        public void onPageScrollStateChanged(int arg0) {
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        public void onPageSelected(int arg0) {
            Animation animation = new TranslateAnimation(offset * currIndex, offset * arg0, 0, 0);
            currIndex = arg0;
            animation.setFillAfter(true);// True:图片停在动画结束位置
            animation.setDuration(300);
            imageView.startAnimation(animation);
            //Toast.makeText(MainActivity.this, "您选择了"+ viewpager.getCurrentItem()+"页卡", Toast.LENGTH_SHORT).show();
        }

    }


    private void initParam() {
        mainToolBar = this.findViewById(R.id.main_toolbar);

        more_btn = this.findViewById(R.id.more);
        more_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (more_window != null && more_window.isShowing()) {
                    more_window.dismiss();
                } else {
                    menuView = getLayoutInflater().inflate(R.layout.more_menu, null);
                    menuListView = menuView.findViewById(R.id.menulist);
                    SimpleAdapter listAdapter = new SimpleAdapter(
                            MainActivity.this, listRight, R.layout.more_menu_item,
                            new String[]{"item"},
                            new int[]{R.id.menuitem});
                    menuListView.setAdapter(listAdapter);

                    // 点击listview中item的处理
                    menuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                @Override
                                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                                    String strItem = listRight.get(arg2).get("item");
                                    Toast.makeText(MainActivity.this, strItem, Toast.LENGTH_SHORT).show();

                                    if(strItem.equals("添加好友")) {

                                    }
                                    if(strItem.equals("创建群聊")) {

                                    }
                                    if(strItem.equals("退出登录")) {

                                        Intent intent = new Intent();  //回到登陆界面
                                        intent.setClass(MainActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        DeviceImpl.getInstance().SendMessage(serverSip, myName, "GOODBYE");

                                    }
                                    //more_btn.setText(strItem);


                                    if (more_window != null && more_window.isShowing()) {
                                        more_window.dismiss();
                                    }
                                }
                            });

                    more_window = new PopupWindow(menuView, 5 * more_btn.getWidth(), LayoutParams.WRAP_CONTENT);

                    //ColorDrawable cd = new ColorDrawable(#ffff);
                    //more_window.setBackgroundDrawable(cd);
                    more_window.setAnimationStyle(R.style.PopupAnimation);
                    more_window.update();
                    more_window.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
                    more_window.setTouchable(true); // 设置popupwindow可点击
                    more_window.setOutsideTouchable(true); // 设置popupwindow外部可点击
                    more_window.setFocusable(true); // 获取焦点

                    // 设置popupwindow的位置
                    int topBarHeight = mainToolBar.getBottom();
                    more_window.showAsDropDown(more_btn, 0, (topBarHeight - more_btn.getHeight()) / 2);

                    more_window.setTouchInterceptor(new View.OnTouchListener() {

                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            // 如果点击了popupwindow的外部，popupwindow也会消失
                            if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                                more_window.dismiss();
                                return true;
                            }
                            return false;
                        }
                    });
                }
            }
        });

        // 初始化数据项
        listRight = new ArrayList<>();
        HashMap<String, String> mapTemp = new HashMap<>();
        mapTemp.put("item", "添加好友");
        listRight.add(mapTemp);

        HashMap<String, String> mapTemp2 = new HashMap<>();
        mapTemp2.put("item", "创建群聊");
        listRight.add(mapTemp2);

        HashMap<String, String> mapTemp3 = new HashMap<>();
        mapTemp3.put("item", "退出登录");
        listRight.add(mapTemp3);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_more_menu, menu);
        return true;
    }


}
