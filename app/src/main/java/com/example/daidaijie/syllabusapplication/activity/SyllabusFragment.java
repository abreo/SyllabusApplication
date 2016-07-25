/*
package com.example.daidaijie.syllabusapplication.activity;


import android.animation.Animator;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.bean.Syllabus;
import com.example.daidaijie.syllabusapplication.bean.SyllabusGrid;
import com.example.daidaijie.syllabusapplication.model.User;
import com.example.daidaijie.syllabusapplication.util.GsonUtil;
import com.example.daidaijie.syllabusapplication.util.RetrofitUtil;
import com.example.daidaijie.syllabusapplication.util.SnackbarUtil;
import com.example.daidaijie.syllabusapplication.widget.SyllabusScrollView;
import com.example.daidaijie.syllabusapplication.service.UserInfoService;
import com.example.daidaijie.syllabusapplication.bean.Lesson;
import com.example.daidaijie.syllabusapplication.bean.UserInfo;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


*/
/**
 * A simple {@link Fragment} subclass.
 *//*

public class SyllabusFragment extends Fragment {

    private String TAG = "SyllabusFragment";

    private static final String WEEK_DAY = "WeekDate";

    private static final String IS_SWIPE_ENABLE = "isSwipeEnable";

    //储存用户的课表
    private Syllabus mSyllabus;

    //这里除了显示，在程序中皆从0开始，为第一周
    private int mWeek;

    */
/**
     * 布局
     *//*

    private CoordinatorLayout syllabusRootLayout;
    private GridLayout syllabusGridLayout;
    private LinearLayout dateLinearLayout;
    private LinearLayout timeLinearLayout;
    private SyllabusScrollView syllabusScrollView;
    private SwipeRefreshLayout syllabusRefreshLayout;

    */
/**
     * 长宽,像素为单位
     *//*

    private int deviceWidth;
    private int devideHeight;
    private int timeWidth;
    private int gridWidth;
    private int gridHeight;


    public static SyllabusFragment newInstance(int week) {
        SyllabusFragment fragment = new SyllabusFragment();
        Bundle args = new Bundle();
        args.putInt(WEEK_DAY, week);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mWeek = getArguments().getInt(WEEK_DAY);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blank, container, false);
        syllabusRootLayout = (CoordinatorLayout) view.findViewById(R.id.syllabusRootLayout);
        syllabusGridLayout = (GridLayout) view.findViewById(R.id.syllabusGridLayout);
        dateLinearLayout = (LinearLayout) view.findViewById(R.id.dateLinearLayout);
        timeLinearLayout = (LinearLayout) view.findViewById(R.id.timeLinearLayout);
        syllabusRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.syllabusRefreshLayout);
        syllabusScrollView = (SyllabusScrollView) view.findViewById(R.id.syllabusScrollView);

        syllabusRefreshLayout.setColorSchemeResources(
                R.color.colorPrimary,
                R.color.colorAccent,
                android.R.color.holo_green_light,
                android.R.color.holo_purple
        );

        syllabusScrollView.setSwipeRefreshLayout(syllabusRefreshLayout);
        syllabusRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getSyllabus();
            }
        });


        deviceWidth = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        devideHeight = getActivity().getWindowManager().getDefaultDisplay().getHeight();

        gridWidth = deviceWidth * 2 / 15;
        timeWidth = deviceWidth - gridWidth * 7;
        gridHeight = getResources().getDimensionPixelOffset(R.dimen.syllabus_grid_height);

        showDate();

        showTime();

        SharedPreferences sharedPreferencesCompat = getActivity()
                .getSharedPreferences(Syllabus.SYLLABUS_GSON_FILE, Context.MODE_PRIVATE);

        String syllabusGson = sharedPreferencesCompat.getString(Syllabus.SYLLABUS_GSON, null);

        if (syllabusGson != null) {
            mSyllabus = GsonUtil.getDefault().fromJson(syllabusGson, Syllabus.class);
//            Log.d(TAG, "onCreateView: " + syllabusGson);
        } else {
            mSyllabus = new Syllabus();
            syllabusRefreshLayout.setRefreshing(true);
            getSyllabus();
        }
        showSyllabus();

        return view;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(IS_SWIPE_ENABLE, syllabusRefreshLayout.isEnabled());
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            syllabusRefreshLayout.setEnabled(savedInstanceState.getBoolean(IS_SWIPE_ENABLE));
        }
    }

    */
/**
     * 显示时间
     *//*

    private void showTime() {
        for (int i = 1; i <= 13; i++) {
            TextView timeTextView = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.week_grid, null, false);
            timeTextView.setText(i + "");
            if (i == 13) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    timeTextView.setBackground(getResources().getDrawable(R.drawable.bg_grid_time_end));
                } else {
                    timeTextView.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_grid_time_end));
                }
            }
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    timeWidth, gridHeight);
            timeLinearLayout.addView(timeTextView, layoutParams);
        }

    }

    */
/**
     * 显示日期
     *//*

    private void showDate() {
        {
            TextView blankTextView = (TextView) LayoutInflater.from(getActivity())
                    .inflate(R.layout.week_grid, null, false);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    timeWidth, ViewGroup.LayoutParams.MATCH_PARENT
            );
            dateLinearLayout.addView(blankTextView, layoutParams);
        }
        for (int i = 0; i < 7; i++) {
            String[] weekString = new String[]{"周日", "周一", "周二", "周三", "周四", "周五", "周六"};

            TextView weekTextView = (TextView) LayoutInflater.from(getActivity())
                    .inflate(R.layout.week_grid, null, false);
            weekTextView.setText(weekString[i]);
            if (i + 1 == 7) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    weekTextView.setBackground(getResources().getDrawable(R.drawable.bg_grid_week_end));
                } else {
                    weekTextView.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_grid_week_end));
                }
            }
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    gridWidth, ViewGroup.LayoutParams.MATCH_PARENT);
            dateLinearLayout.addView(weekTextView, layoutParams);
        }
    }

    */
/**
     * 显示课程表
     *//*

    private void showSyllabus() {

        syllabusGridLayout.removeAllViews();
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 13; j++) {
                SyllabusGrid syllabusGrid = mSyllabus.getSyllabusGrids().get(i).get(j);
                Lesson lesson = null;
                if (syllabusGrid.getLessons().size() != 0) {
                    lesson = syllabusGrid.getLessons().get(0);
                }
                LinearLayout lessonLinearLayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.lesson_grid, null, false);
                TextView lessonTextView = (TextView) lessonLinearLayout.findViewById(R.id.lessonTextView);
                int span = 1;

                if (lesson != null) {
                    GradientDrawable shape = (GradientDrawable) getResources().getDrawable(R.drawable.grid_background);

                    shape.setColor(ColorUtils.setAlphaComponent(getResources().getColor(
                            lesson.getBgColor()), 192));
                    lessonTextView.setText(lesson.getTrueName() + "\n@" + lesson.getRoom());

                    lessonTextView.setBackgroundDrawable(shape);
                    for (int k = j + 1; k < 13; k++) {
                        SyllabusGrid nextSyllabusGrid = mSyllabus.getSyllabusGrids().get(i).get(k);
                        if (nextSyllabusGrid.getLessons().size() == 0) break;
                        Lesson nextlesson = nextSyllabusGrid.getLessons().get(0);
                        if (nextlesson.getId().equals(lesson.getId())) {
                            span++;
                        }
                    }

                } else {
                    lessonLinearLayout.setVisibility(View.INVISIBLE);
                }
                lessonTextView.setWidth(gridWidth);
                lessonTextView.setHeight(gridHeight * span);
                GridLayout.Spec rowSpec = GridLayout.spec(j, span);
                GridLayout.Spec columnSpec = GridLayout.spec(i);
                syllabusGridLayout.addView(lessonLinearLayout, new GridLayout.LayoutParams(rowSpec, columnSpec));
                j += span - 1;

            }

        }

        syllabusGridLayout.post(new Runnable() {
            @Override
            public void run() {
                if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
                    return;
                }
                for (int i = 0; i < syllabusGridLayout.getChildCount(); i++) {
                    View syllabusGridview = syllabusGridLayout.getChildAt(i);
                    Animator animator = ViewAnimationUtils.createCircularReveal(
                            syllabusGridview,
                            syllabusGridview.getWidth() / 2,
                            syllabusGridview.getHeight() / 2,
                            0,
                            Math.max(syllabusGridview.getHeight(), syllabusGridview.getWidth()));
                    animator.setInterpolator(new AccelerateInterpolator());
                    animator.setDuration(500);
                    animator.start();
                }
            }
        });


    }

    private void getSyllabus() {

    }
}
*/
