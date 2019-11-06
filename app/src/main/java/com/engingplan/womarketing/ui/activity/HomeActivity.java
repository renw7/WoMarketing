package com.engingplan.womarketing.ui.activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.engingplan.womarketing.fragment.FragmentIndex;
import com.engingplan.womarketing.fragment.FragmentMe;
import com.engingplan.womarketing.fragment.FragmentPhoneRecord;
import com.engingplan.womarketing.fragment.FragmentTask;
import com.engingplan.womarketing.common.ViewPagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private BottomNavigationView bottomNavigationView;
    private ViewPagerAdapter viewPagerAdapter;
    private MenuItem menuItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bv_home_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mViewPager = (ViewPager) findViewById(R.id.vp_home_pager);//获取到ViewPager
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (menuItem != null) {
                    menuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                menuItem = bottomNavigationView.getMenu().getItem(position);
                menuItem.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // 获取传来页面的参数
        Bundle bundle = this.getIntent().getExtras();

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(viewPagerAdapter);

        FragmentIndex fragmentIndex = FragmentIndex.newInstance("首页");
        FragmentTask fragmentTask = FragmentTask.newInstance("任务管理");
        FragmentPhoneRecord fragmentPhoneRecord = FragmentPhoneRecord.newInstance("通话记录");
        FragmentMe fragmentMe = FragmentMe.newInstance("我的");
        fragmentIndex.setArguments(bundle);
        fragmentTask.setArguments(bundle);
        fragmentPhoneRecord.setArguments(bundle);
        fragmentMe.setArguments(bundle);

        List<Fragment> list = new ArrayList<>();
        list.add(fragmentIndex);
        list.add(fragmentTask);
        list.add(fragmentPhoneRecord);
        list.add(fragmentMe);
        viewPagerAdapter.setList(list);
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            menuItem = item;
            switch (item.getItemId()) {
                case R.id.menu_home:
                    mViewPager.setCurrentItem(0);
                    //跳到对应ViewPager的page
                    break;
                case R.id.home_found:
                    mViewPager.setCurrentItem(1);
                    break;
                case R.id.home_message:
                    mViewPager.setCurrentItem(2);
                    break;
                case R.id.home_me:
                    mViewPager.setCurrentItem(3);
                    break;
            }
            return false;
        }
    };
}

//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId())
//                {
//                    //根据navagatin.xml中item的id进行case
//                    case R.id.menu_home:
//                        mViewPager.setCurrentItem(0);
//                        //跳到对应ViewPager的page
//                        break;
//                    case R.id.home_found:
//                        mViewPager.setCurrentItem(1);
//                        break;
//                    case R.id.home_message:
//                        mViewPager.setCurrentItem(2);
//                        break;
//                    case R.id.home_me:
//                        mViewPager.setCurrentItem(3);
//                        break;
//                }
//                return false;
//            }
//        });
//        //ViewPager的监听
//        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//
//                bottomNavigationView.getMenu().getItem(position).setChecked(true);
//                //滑动页面后做的事，这里与BottomNavigationView结合，使其与正确page对应
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
//
//        //底部导航栏有几项就有几个Fragment
//        final ArrayList<Fragment> fgLists=new ArrayList<>(4);
//        fgLists.add(new FragmentIndex());
//        fgLists.add(new FragmentTask());
//        fgLists.add(new FragmentPhoneRecord());
//        fgLists.add(new FragmentMe());
//
//
//        //设置适配器用于装载Fragment
//        FragmentPagerAdapter mPagerAdapter=new FragmentPagerAdapter(getSupportFragmentManager()) {
//            @Override
//            public Fragment getItem(int position) {
//                return fgLists.get(position);  //得到Fragment
//            }
//
//            @Override
//            public int getCount() {
//                return fgLists.size();  //得到数量
//            }
//        };
//        mViewPager.setAdapter(mPagerAdapter);   //设置适配器
//        mViewPager.setOffscreenPageLimit(3); //预加载剩下三页
//
//        //以上就将Fragment装入了ViewPager
//    }
//
//}