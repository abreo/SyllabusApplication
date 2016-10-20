package com.example.daidaijie.syllabusapplication.other;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.base.BaseActivity;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;

public class AboutUsActivity extends BaseActivity {

    @BindView(R.id.titleTextView)
    TextView mTitleTextView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.syllabusIconView)
    SimpleDraweeView mSyllabusIconView;
    @BindView(R.id.descTextView)
    TextView mDescTextView;
    @BindView(R.id.activity_about_us)
    LinearLayout mActivityAboutUs;
    @BindView(R.id.versionTextView)
    TextView mVersionTextView;
    @BindView(R.id.backendDeveloperHeadImageView)
    SimpleDraweeView mBackendDeveloperHeadImageView;
    @BindView(R.id.backendDeveloper)
    LinearLayout mBackendDeveloper;
    @BindView(R.id.iOSDeveloperHeadImageView)
    SimpleDraweeView mIOSDeveloperHeadImageView;
    @BindView(R.id.iOSDeveloper)
    LinearLayout mIOSDeveloper;
    @BindView(R.id.androidDeveloperHeadImageView)
    SimpleDraweeView mAndroidDeveloperHeadImageView;
    @BindView(R.id.androidDeveloper)
    LinearLayout mAndroidDeveloper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupTitleBar(mToolbar);

        mIOSDeveloperHeadImageView.setImageURI("https://avatars3.githubusercontent.com/u/11157226?v=3&s=64");
        mBackendDeveloperHeadImageView.setImageURI("https://avatars1.githubusercontent.com/u/12378280?v=3&s=64");
        mAndroidDeveloperHeadImageView.setImageURI("https://avatars3.githubusercontent.com/u/12541192?v=3&s=64");
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_about_us;
    }
}
