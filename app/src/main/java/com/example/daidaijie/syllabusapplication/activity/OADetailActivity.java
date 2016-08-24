package com.example.daidaijie.syllabusapplication.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daidaijie.syllabusapplication.App;
import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.bean.OABean;
import com.example.daidaijie.syllabusapplication.util.AssetUtil;
import com.example.daidaijie.syllabusapplication.util.FileUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import butterknife.BindView;

public class OADetailActivity extends BaseActivity {

    @BindView(R.id.titleTextView)
    TextView mTitleTextView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.oAWebView)
    WebView mOAWebView;

    OABean mOABean;

    String oaContent = "";

    String label = "!@#$%^&*";

    public static final String EXTRA_OABEAN = "com.example.daidaijie.syllabusapplication.activity" +
            ".OADetailActivity.OABean";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mToolbar.setTitle("");
        setupToolbar(mToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mOAWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mOAWebView.getSettings().setLoadWithOverviewMode(true);
        mOAWebView.getSettings().setJavaScriptEnabled(true);

        mOABean = (OABean) getIntent().getSerializableExtra(EXTRA_OABEAN);
        oaContent = mOABean.getDOCCONTENT();
        int index = oaContent.indexOf(label) + label.length();
        oaContent = oaContent.substring(index);


        Document doc = Jsoup.parse(AssetUtil.getStringFromPath("index.html"));
        Element div = doc.select("div#div_doc").first();
        div.append(oaContent);
        div = doc.select("div#div_accessory").first();

        if (mOABean.getACCESSORYCOUNT() == 0) {
            div.remove();
        } else {
        }

        mOAWebView.setDrawingCacheEnabled(true);
        mOAWebView.loadData(doc.toString(), "text/html; charset=UTF-8", null);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_oadetail;
    }

    public static Intent getIntent(Context context, OABean oaBean) {
        Intent intent = new Intent(context, OADetailActivity.class);
        intent.putExtra(EXTRA_OABEAN, oaBean);
        return intent;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_oa_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_screenshot) {
            screenShot();
        }
        return super.onOptionsItemSelected(item);
    }

    private void screenShot() {
        Bitmap webViewScreen = captureScreen(this);
        try {
            saveFile(webViewScreen, "STUOA" + SystemClock.currentThreadTimeMillis() + ".jpg", "STUOA");
            Toast.makeText(this, "已保存到图库", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "保存失败", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 截屏
     *
     * @param context
     * @return
     */
    private Bitmap captureScreen(Activity context) {
        Picture picture = mOAWebView.capturePicture();
        Bitmap b = Bitmap.createBitmap(picture.getWidth(),
                picture.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);

        picture.draw(c);
        return b;
    }

    public static void saveFile(Bitmap bm, String fileName, String path) throws IOException {
        String subForder = FileUtil.get_app_folder(true) + path;
        File foder = new File(subForder);
        if (!foder.exists()) {
            foder.mkdirs();
        }
        File myCaptureFile = new File(subForder, fileName);
        if (!myCaptureFile.exists()) {
            myCaptureFile.createNewFile();
        }
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();

        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(myCaptureFile);
        intent.setData(uri);
        App.getContext().sendBroadcast(intent);
    }
}
