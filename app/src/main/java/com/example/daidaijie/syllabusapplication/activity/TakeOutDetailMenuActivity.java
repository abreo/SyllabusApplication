package com.example.daidaijie.syllabusapplication.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.adapter.DishesAdapter;
import com.example.daidaijie.syllabusapplication.adapter.SubMenuAdapter;
import com.example.daidaijie.syllabusapplication.bean.Dishes;
import com.example.daidaijie.syllabusapplication.bean.TakeOutBuyBean;
import com.example.daidaijie.syllabusapplication.bean.TakeOutInfoBean;
import com.example.daidaijie.syllabusapplication.bean.TakeOutSubMenu;
import com.example.daidaijie.syllabusapplication.model.BmobModel;
import com.example.daidaijie.syllabusapplication.model.TakeOutModel;
import com.example.daidaijie.syllabusapplication.model.ThemeModel;
import com.example.daidaijie.syllabusapplication.service.TakeOutMenuDetailService;
import com.example.daidaijie.syllabusapplication.util.SnackbarUtil;
import com.example.daidaijie.syllabusapplication.widget.BuyPopWindow;
import com.example.daidaijie.syllabusapplication.widget.MyItemAnimator;
import com.liaoinstan.springview.utils.DensityUtil;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.BindView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TakeOutDetailMenuActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, DishesAdapter.OnNumChangeListener {

    @BindView(R.id.titleTextView)
    TextView mTitleTextView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.dishesRecyclerView)
    RecyclerView mDishesRecyclerView;
    @BindView(R.id.tv_sticky_header_view)
    LinearLayout mTvStickyHeaderView;
    @BindView(R.id.stickyTextView)
    TextView mStickyTextView;

    public static final String EXTRA_POSITION = "com.example.daidaijie.syllabusapplication.activity" +
            ".TakeOutDetailMenuActivity";
    @BindView(R.id.subMenuRecyclerView)
    RecyclerView mSubMenuRecyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout mToolbarLayout;
    @BindView(R.id.app_bar)
    AppBarLayout mAppBar;
    @BindView(R.id.takeoutNameTextView)
    TextView mTakeoutNameTextView;
    @BindView(R.id.longNumberTextView)
    TextView mLongNumberTextView;
    @BindView(R.id.conditionTextView)
    TextView mConditionTextView;
    @BindView(R.id.takeoutNameLayout)
    RelativeLayout mTakeoutNameLayout;
    @BindView(R.id.fab)
    FloatingActionButton mFab;
    @BindView(R.id.shortNumberTextView)
    TextView mShortNumberTextView;
    @BindView(R.id.shoppingImg)
    ImageView mShoppingImg;
    @BindView(R.id.buyNumTextView)
    TextView mBuyNumTextView;
    @BindView(R.id.shoppingLayout)
    RelativeLayout mShoppingLayout;
    @BindView(R.id.bottomBgLayout)
    LinearLayout mBottomBgLayout;
    @BindView(R.id.bottomLayout)
    RelativeLayout mBottomLayout;
    @BindView(R.id.contentLayout)
    CoordinatorLayout mContentLayout;
    @BindView(R.id.sumPriceTextView)
    TextView mSumPriceTextView;
    @BindView(R.id.div_line)
    View mDivLine;
    @BindView(R.id.unCalcNumTextView)
    TextView mUnCalcNumTextView;
    @BindView(R.id.submitButton)
    Button mSubmitButton;

    private int mPosition;

    private TakeOutInfoBean mTakeOutInfoBean;

    private List<Dishes> mDishesList;

    private DishesAdapter mTakeOutMenuAdapter;

    private SubMenuAdapter mSubMenuAdapter;

    private boolean move;

    private LinearLayoutManager mLinearLayoutManager;

    private int mIndex;

    private TakeOutBuyBean mTakeOutBuyBean;

    private enum CollapsingToolbarLayoutState {
        EXPANDED,
        COLLAPSED,
        INTERNEDIATE
    }

    private CollapsingToolbarLayoutState state;

    FrameLayout aniLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        aniLayout = new FrameLayout(this);

        final ViewGroup decorView = (ViewGroup) this.getWindow().getDecorView();
        decorView.addView(aniLayout);

        mToolbarLayout.setTitle("");
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mPosition = getIntent().getIntExtra(EXTRA_POSITION, 0);

        mTakeOutBuyBean = new TakeOutBuyBean();

        mTakeOutInfoBean = TakeOutModel.getInstance().getTakeOutInfoBeen().get(mPosition);
        setUpTakoutInfo();
        mTitleTextView.setText(mTakeOutInfoBean.getName());
        mTitleTextView.setVisibility(View.GONE);
        mAppBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {
                    if (state != CollapsingToolbarLayoutState.EXPANDED) {
                        state = CollapsingToolbarLayoutState.EXPANDED;//修改状态标记为展开
                        mTitleTextView.setVisibility(View.GONE);
                    }
                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    if (state != CollapsingToolbarLayoutState.COLLAPSED) {
                        mTitleTextView.setVisibility(View.VISIBLE);
                        state = CollapsingToolbarLayoutState.COLLAPSED;//修改状态标记为折叠
                    }
                } else {
                    if (state != CollapsingToolbarLayoutState.INTERNEDIATE) {
                        mTitleTextView.setVisibility(View.GONE);
                        state = CollapsingToolbarLayoutState.INTERNEDIATE;//修改状态标记为中间
                    }
                }
            }
        });


        if (mTakeOutInfoBean.getTakeOutSubMenus() == null) {
            mSwipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(true);
                    getDetailMenu();
                }
            });
        } else {
            mDishesList = mTakeOutInfoBean.getDishes();
        }

        mSwipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );

        mSwipeRefreshLayout.setOnRefreshListener(this);

        setupRecyclerView();


        mBottomBgLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopWindows();
            }
        });
    }

    private void setUpTakoutInfo() {
        mTakeoutNameTextView.setText(mTakeOutInfoBean.getName());
        mLongNumberTextView.setText("长号 : " + mTakeOutInfoBean.getLong_number());
        mShortNumberTextView.setText("短号 : " + mTakeOutInfoBean.getShort_number());
        mConditionTextView.setText("备注 : " + mTakeOutInfoBean.getCondition());
    }

    private void setupRecyclerView() {
        mSubMenuAdapter = new SubMenuAdapter(this, mTakeOutInfoBean.getTakeOutSubMenus());
        mSubMenuRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mSubMenuRecyclerView.setAdapter(mSubMenuAdapter);

        mLinearLayoutManager = new LinearLayoutManager(this);
        mTakeOutMenuAdapter = new DishesAdapter(this, mDishesList, mTakeOutBuyBean.getBuyMap());
        mDishesRecyclerView.setLayoutManager(mLinearLayoutManager);
        mDishesRecyclerView.setAdapter(mTakeOutMenuAdapter);
        mDishesRecyclerView.setItemAnimator(new MyItemAnimator());

        mDishesRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // Get the sticky information from the topmost view of the screen.
                View stickyInfoView = recyclerView.findChildViewUnder(
                        mTvStickyHeaderView.getMeasuredWidth() / 2, 5);

                if (stickyInfoView != null && stickyInfoView.getContentDescription() != null) {
                    int subPos = Integer.parseInt(String.valueOf(stickyInfoView.getContentDescription()));
                    mStickyTextView.setText(mTakeOutInfoBean.getTakeOutSubMenus().get(subPos).getName());
                    if (subPos != mSubMenuAdapter.getSelectItem()) {
                        mSubMenuRecyclerView.scrollToPosition(subPos);
                        mSubMenuAdapter.setSelectItem(subPos);
                        mSubMenuAdapter.notifyDataSetChanged();
                    }
                }

                // Get the sticky view's translationY by the first view below the sticky's height.
                View transInfoView = recyclerView.findChildViewUnder(
                        mTvStickyHeaderView.getMeasuredWidth() / 2, mTvStickyHeaderView.getMeasuredHeight() + 1);

                if (transInfoView != null && transInfoView.getTag() != null) {
                    int transViewStatus = (int) transInfoView.getTag();
                    int dealtY = transInfoView.getTop() - mTvStickyHeaderView.getMeasuredHeight();
                    if (transViewStatus == mTakeOutMenuAdapter.HAS_STICKY_VIEW) {
                        // If the first view below the sticky's height scroll off the screen,
                        // then recovery the sticky view's translationY.
                        if (transInfoView.getTop() > 0) {
                            mTvStickyHeaderView.setTranslationY(dealtY);
                        } else {
                            mTvStickyHeaderView.setTranslationY(0);
                        }
                    } else if (transViewStatus == mTakeOutMenuAdapter.NONE_STICKY_VIEW) {
                        mTvStickyHeaderView.setTranslationY(0);
                    }
                }

            }
        });

        mTakeOutMenuAdapter.setOnNumChangeListener(this);

        mSubMenuAdapter.setOnItemClickListener(new SubMenuAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                int pos = mTakeOutInfoBean
                        .getTakeOutSubMenus().get(position).getFirstItemPos();
                moveToPosition(pos);
            }
        });

        mDishesRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (move) {
                    move = false;
                    //获取要置顶的项在当前屏幕的位置，mIndex是记录的要置顶项在RecyclerView中的位置
                    int n = mIndex - mLinearLayoutManager.findFirstVisibleItemPosition();
                    if (0 <= n && n < mDishesRecyclerView.getChildCount()) {
                        //获取要置顶的项顶部离RecyclerView顶部的距离
                        int top = mDishesRecyclerView.getChildAt(n).getTop();
                        //最后的移动
                        mDishesRecyclerView.scrollBy(0, top);
                    }
                }
            }
        });
    }

    private void moveToPosition(int n) {
        //先从RecyclerView的LayoutManager中获取第一项和最后一项的Position
        int firstItem = mLinearLayoutManager.findFirstVisibleItemPosition();
        int lastItem = mLinearLayoutManager.findLastVisibleItemPosition();
        //然后区分情况
        if (n <= firstItem) {
            //当要置顶的项在当前显示的第一个项的前面时
            mDishesRecyclerView.scrollToPosition(n);
        } else if (n <= lastItem) {
            //当要置顶的项已经在屏幕上显示时
            int top = mDishesRecyclerView.getChildAt(n - firstItem).getTop();
            mDishesRecyclerView.scrollBy(0, top);
        } else {
            //当要置顶的项在当前显示的最后一项的后面时
            mDishesRecyclerView.scrollToPosition(n);
            //这里这个变量是用在RecyclerView滚动监听里面的
            mIndex = n;
            move = true;
        }
    }

    @Override
    public void onAddNum(View v, int position) {
        Dishes dishes = mDishesList.get(position);
        mTakeOutBuyBean.addDishes(dishes);
        showPrice();

        mTakeOutMenuAdapter.notifyItemChanged(position);
        int[] location = new int[2];
        v.getLocationInWindow(location);

        final TextView text = new TextView(this);
        text.setText(Html.fromHtml("<b>1</b>"));
        text.setGravity(Gravity.CENTER);
        GradientDrawable drawable = (GradientDrawable) getResources().getDrawable(R.drawable.bg_add_dishes);
        drawable.setColor(ThemeModel.getInstance().colorPrimary);
        text.setBackgroundDrawable(drawable);
        text.setTextColor(getResources().getColor(R.color.material_white));
        text.setTextSize(12);

        aniLayout.addView(text);
        int width = DensityUtil.dip2px(this, 24);
        text.getLayoutParams().width = width;
        text.getLayoutParams().height = width;

        int lf = location[0];
        int tf = location[1];

        text.setX(lf);
        text.setY(tf);

        int[] shoppingLocation = new int[2];
        mShoppingImg.getLocationInWindow(shoppingLocation);


        ObjectAnimator anix = ObjectAnimator.ofFloat(
                text, "x", lf, shoppingLocation[0]
        );

        ObjectAnimator aniy = ObjectAnimator.ofFloat(
                text, "y", tf, shoppingLocation[1]
        );
        anix.setInterpolator(new LinearInterpolator());
        aniy.setInterpolator(new AccelerateInterpolator());
        AnimatorSet dumpAni = new AnimatorSet();
        dumpAni.play(anix).with(aniy);
        dumpAni.setDuration(500);
        dumpAni.start();


        dumpAni.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                aniLayout.removeView(text);
                ObjectAnimator aniScaleX = ObjectAnimator.ofFloat(
                        mShoppingLayout, "scaleX", 0.7f, 1.0f
                );
                ObjectAnimator aniScaleY = ObjectAnimator.ofFloat(
                        mShoppingLayout, "scaleY", 0.7f, 1.0f
                );
                AnimatorSet scaleAni = new AnimatorSet();
                scaleAni.play(aniScaleX).with(aniScaleY);
                scaleAni.setDuration(300);
                scaleAni.setInterpolator(new AnticipateOvershootInterpolator());
                scaleAni.start();
            }
        });


    }

    @Override
    public void onReduceNum(int position) {
        Dishes dishes = mDishesList.get(position);
        mTakeOutBuyBean.removeDishes(dishes);
        showPrice();

        mTakeOutMenuAdapter.notifyItemChanged(position);

    }


    private void getDetailMenu() {
        TakeOutMenuDetailService service = BmobModel.getInstance().mRetrofit.create(TakeOutMenuDetailService.class);
        service.getTokenResult(mTakeOutInfoBean.getObjectId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TakeOutInfoBean>() {
                    @Override
                    public void onCompleted() {
                        mSwipeRefreshLayout.setRefreshing(false);
                        mDishesList = mTakeOutInfoBean.getDishes();

                        mTakeOutMenuAdapter.setDishesList(mDishesList);
                        mTakeOutMenuAdapter.notifyDataSetChanged();

                        mSubMenuAdapter.setSubMenus(mTakeOutInfoBean.getTakeOutSubMenus());
                        mSubMenuAdapter.notifyDataSetChanged();

                        setUpTakoutInfo();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        showFailMessage("获取失败");
                    }

                    @Override
                    public void onNext(TakeOutInfoBean takeOutInfoBean) {
                        mTakeOutInfoBean = takeOutInfoBean;
                        mTakeOutInfoBean.loadTakeOutSubMenus();
                        for (TakeOutSubMenu subMenu : mTakeOutInfoBean.getTakeOutSubMenus()) {
                            Logger.t("subMenu").e(subMenu.getName());
                        }
                        TakeOutModel.getInstance().getTakeOutInfoBeen().set(mPosition, mTakeOutInfoBean);
                        TakeOutModel.getInstance().save();
                    }
                });
    }


    private void showFailMessage(String msg) {
        SnackbarUtil.ShortSnackbar(
                mDishesRecyclerView, msg, SnackbarUtil.Alert
        ).show();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_take_out_detail_menu;
    }

    public static Intent getIntent(Context context, int position) {
        Intent intent = new Intent(context, TakeOutDetailMenuActivity.class);
        intent.putExtra(EXTRA_POSITION, position);
        return intent;
    }

    @Override
    public void onRefresh() {
        getDetailMenu();
    }


    private void showPopWindows() {
        BuyPopWindow popWindow = new BuyPopWindow(this, mTakeOutBuyBean);
        popWindow.setOnDataChangeListener(new BuyPopWindow.OnDataChangeListener() {
            @Override
            public void onChange(int position) {
                mTakeOutMenuAdapter.notifyItemChanged(position);
                showPrice();
            }

            @Override
            public void onChangeAll() {
                mTakeOutMenuAdapter.notifyDataSetChanged();
                showPrice();
            }
        });
        popWindow.show();
    }

    private void showPrice() {
        mBuyNumTextView.setText(mTakeOutBuyBean.getNum() + "");
        mSumPriceTextView.setText("¥" + mTakeOutBuyBean.getSumPrice());
        if (mTakeOutBuyBean.getUnCalcNum() != 0) {
            mDivLine.setVisibility(View.VISIBLE);
            mUnCalcNumTextView.setVisibility(View.VISIBLE);
            mUnCalcNumTextView.setText("不可计价份数: " + mTakeOutBuyBean.getUnCalcNum());
        } else {
            mDivLine.setVisibility(View.GONE);
            mUnCalcNumTextView.setVisibility(View.GONE);
        }
    }
}