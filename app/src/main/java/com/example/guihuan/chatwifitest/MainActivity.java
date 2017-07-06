package com.example.guihuan.chatwifitest;

import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.guihuan.chatwifitest.contacts.ContactsFragment;
import com.example.guihuan.chatwifitest.contacts.MyFragmentPagerAdapter;
import com.example.guihuan.chatwifitest.messages.MsgFragment;

import java.util.ArrayList;


public class MainActivity  extends AppCompatActivity {

    private ViewPager viewpager;//页卡内容
    private ImageView imageView;// 动画图片
    private TextView textView1,textView2;
    private ArrayList<Fragment> listfragment;// Tab页面列表
    private int offset = 0;// 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号
    private int bmpW;// 动画图片宽度

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE); // 注意顺序
        setContentView(R.layout.main);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,      // 注意顺序
                //R.layout.main_title_bar);

        InitImageView();
        InitTextView();
        InitViewPager();
    }

    private void InitViewPager() {
        viewpager=(ViewPager) findViewById(R.id.vPager);
        listfragment=new ArrayList<Fragment>(); //new一个List<Fragment>
        Fragment f1 = new MsgFragment();
        Fragment f2 = new ContactsFragment();
        listfragment.add(f1);
        listfragment.add(f2);

        FragmentManager fm=getSupportFragmentManager();
        MyFragmentPagerAdapter mfpa=new MyFragmentPagerAdapter(fm, listfragment);

        viewpager.setAdapter(mfpa);
        viewpager.setCurrentItem(0); //设置当前页是第一页
        viewpager.setOnPageChangeListener(new MyOnPageChangeListener());
    }
    /**
     *  初始化头标
     */

    private void InitTextView() {
        textView1 = (TextView) findViewById(R.id.text1);
        textView2 = (TextView) findViewById(R.id.text2);

        textView1.setOnClickListener(new MyOnClickListener(0));
        textView2.setOnClickListener(new MyOnClickListener(1));
    }

    /**
     2      * 初始化动画
     3 */

    private void InitImageView() {
        imageView= (ImageView) findViewById(R.id.cursor);
        BitmapFactory.Options options = new BitmapFactory.Options();
        BitmapFactory.decodeResource(getResources(), R.drawable.ic_downline,options);
        bmpW = options.outWidth;
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;// 获取分辨率宽度
        offset = screenW /4 - bmpW*6;// 计算偏移量
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        imageView.setImageMatrix(matrix);// 设置动画初始位置
    }

    /**
     *
     * 头标点击监听 3 */
    private class MyOnClickListener implements View.OnClickListener {
        private int index=0;
        public MyOnClickListener(int i){
            index=i;
        }
        public void onClick(View v) {
            viewpager.setCurrentItem(index);
        }

    }


    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        public void onPageScrollStateChanged(int arg0) {
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        public void onPageSelected(int arg0) {
            int one = offset * 2-bmpW/2*3;// 页卡1 -> 页卡2 偏移量
            Animation animation = new TranslateAnimation(one*currIndex, one*arg0, 0, 0);
            currIndex = arg0;
            animation.setFillAfter(true);// True:图片停在动画结束位置
            animation.setDuration(300);
            imageView.startAnimation(animation);
            //Toast.makeText(MainActivity.this, "您选择了"+ viewpager.getCurrentItem()+"页卡", Toast.LENGTH_SHORT).show();
        }

    }

}
