package com.example.daidaijie.syllabusapplication.model;

import android.util.Log;
import android.widget.Toast;

import com.example.daidaijie.syllabusapplication.App;
import com.example.daidaijie.syllabusapplication.activity.OADetailActivity;
import com.example.daidaijie.syllabusapplication.bean.SubCompany;
import com.example.daidaijie.syllabusapplication.util.AssetUtil;
import com.example.daidaijie.syllabusapplication.util.GsonUtil;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by daidaijie on 2016/8/23.
 */
public class OAModel {

    public Retrofit mRetrofit;

    public List<SubCompany> mSubCompanies;

    private static OAModel ourInstance = new OAModel();

    public static OAModel getInstance() {
        return ourInstance;
    }

    private OAModel() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl("http://wechat.stu.edu.cn/webservice_oa/oa_stu_/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        String subCompanyString = AssetUtil.getStringFromPath("subcompany.json");
        mSubCompanies = GsonUtil.getDefault().fromJson(
                subCompanyString, new TypeToken<List<SubCompany>>() {
                }.getType()
        );
    }


}
