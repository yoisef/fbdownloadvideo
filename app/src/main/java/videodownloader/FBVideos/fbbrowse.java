package videodownloader.FBVideos;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;


import videodownloader.FBVideos.adapter.HomeFragment;

import java.util.ArrayList;
import java.util.List;

public class fbbrowse extends BaseActivity {


    HomeFragment homeFragment;
    ViewPager myviewpager;
    Intent intent;
    String geturl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fbbrowse);

        intent=getIntent();
       geturl=intent.getStringExtra("url");
        loadinterstial();
        interstiallistener();
        intilizebannerad();
        bannerlisener();
        homeFragment = new HomeFragment();

        myviewpager = findViewById(R.id.pagerfb);
        setupViewPager(myviewpager);


    }

    public String geturlvalue()
    {
       return geturl;
    }
    private void setupViewPager(ViewPager viewPager) {
        fbbrowse.ViewPagerAdapter adapter = new fbbrowse.ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment(), "Home");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        private String[] titles = { "Videos List","Paste Link" };

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0:{
                    return HomeFragment.newInstance(position);
                }


            }
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


}
