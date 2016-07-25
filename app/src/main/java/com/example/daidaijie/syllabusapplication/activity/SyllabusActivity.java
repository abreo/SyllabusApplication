package com.example.daidaijie.syllabusapplication.activity;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.presenter.SyllabusMainPresenter;
import com.example.daidaijie.syllabusapplication.util.SnackbarUtil;
import com.example.daidaijie.syllabusapplication.view.ISyllabusMainView;
import com.example.daidaijie.syllabusapplication.widget.SyllabusScrollView;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;

public class SyllabusActivity extends BaseActivity implements ISyllabusMainView, SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.titleTextView)
    TextView mTitleTextView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.syllabusViewPager)
    ViewPager mSyllabusViewPager;
    @BindView(R.id.mainRootLayout)
    LinearLayout mMainRootLayout;
    @BindView(R.id.nav_view)
    NavigationView mNavView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.dateLinearLayout)
    LinearLayout mDateLinearLayout;
    @BindView(R.id.syllabusScrollView)
    SyllabusScrollView mSyllabusScrollView;
    @BindView(R.id.syllabusRefreshLayout)
    SwipeRefreshLayout mSyllabusRefreshLayout;

    private RelativeLayout navHeadRelativeLayout;
    private SimpleDraweeView headImageDraweeView;
    private TextView nicknameTextView;

//    private SyllabusPagerAdapter syllabusPagerAdapter;

    private final static String SAVED_PAGE_POSITION = "pagePositon";

    private SyllabusMainPresenter mSyllabusMainPresenter = new SyllabusMainPresenter();

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSyllabusMainPresenter.attach(this);

        //获取NavavNavigationView中的控件
        navHeadRelativeLayout = (RelativeLayout) mNavView.getHeaderView(0);
        headImageDraweeView = (SimpleDraweeView) navHeadRelativeLayout.findViewById(R.id.headImageDraweeView);
        nicknameTextView = (TextView) navHeadRelativeLayout.findViewById(R.id.nicknameTextView);

        //解决滑动冲突
        mSyllabusScrollView.setSwipeRefreshLayout(mSyllabusRefreshLayout);

        setupToolbar();

        mSyllabusRefreshLayout.setOnRefreshListener(this);

        mSyllabusMainPresenter.setUserInfo();
        mSyllabusMainPresenter.loadWallpaper();

        /*FragmentManager manager = getSupportFragmentManager();
        syllabusPagerAdapter = new SyllabusPagerAdapter(manager);
        mSyllabusViewPager.setAdapter(syllabusPagerAdapter);

        int pageIndex = 0;
        if (savedInstanceState != null) {
            pageIndex = savedInstanceState.getInt(SAVED_PAGE_POSITION);
        }

        mToolbar.setTitle("");
        mTitleTextView.setText("第 " + (pageIndex + 1) + " 周");

        mMainRootLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.background));
        BitmapDrawable drawable = (BitmapDrawable) mMainRootLayout.getBackground();


        mSyllabusViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position
                    , float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mTitleTextView.setText("第 " + (position + 1) + " 周");
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });*/

    }

    private void setupToolbar() {
        mToolbar.setTitle("");
        //透明状态栏并且适应Toolbar的高度
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup.LayoutParams layoutParams = mToolbar.getLayoutParams();
            layoutParams.height = layoutParams.height + getStatusBarHeight();
        }

        setSupportActionBar(mToolbar);
        //添加toolbar drawer的开关
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSyllabusMainPresenter.detach();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_PAGE_POSITION, mSyllabusViewPager.getCurrentItem());
    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;

    }


    @Override
    public void setHeadImageView(Uri uri) {
        headImageDraweeView.setImageURI(uri);
    }

    @Override
    public void setNickName(String nickName) {
        nicknameTextView.setText(nickName);
    }

    @Override
    public void setToolBarTitle(String title) {
        mTitleTextView.setText(title);
    }

    @Override
    public void setBackground(Bitmap bitmap) {
        Drawable drawable = new BitmapDrawable(getResources(), bitmap);
        mMainRootLayout.setBackground(drawable);
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                Palette.Swatch swatch = palette.getVibrantSwatch();
                if (swatch != null) {
                    mToolbar.setBackgroundColor(ColorUtils.setAlphaComponent(swatch.getRgb()
                            , 192));
                    navHeadRelativeLayout.setBackgroundColor(swatch.getRgb());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().setStatusBarColor(ColorUtils.setAlphaComponent(swatch.getRgb()
                                , 188));
                    }
                } else {
                    mToolbar.setBackgroundColor(ColorUtils.setAlphaComponent(
                            getResources().getColor(R.color.colorPrimary)
                            , 192));
                }
            }
        });
    }

    @Override
    public void showSuccessBanner() {
        SnackbarUtil.ShortSnackbar(
                mMainRootLayout,
                "课表同步成功",
                SnackbarUtil.Confirm
        ).show();
    }

    @Override
    public void showFailBannner() {
        SnackbarUtil.LongSnackbar(
                mMainRootLayout,
                "课表同步失败",
                SnackbarUtil.Alert
        ).setAction("再次同步", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSyllabusRefreshLayout.setRefreshing(true);
                mSyllabusMainPresenter.getSyllabus();
            }
        }).show();
    }

    @Override
    public void showLoading() {
        mSyllabusRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        mSyllabusRefreshLayout.setRefreshing(false);
    }

    @Override
    public Resources getActivityResources() {
        return getResources();
    }

    @Override
    public void onRefresh() {
        mSyllabusMainPresenter.getSyllabus();
    }


}