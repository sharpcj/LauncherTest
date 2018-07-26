package com.example.sharpcj.launchertest;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRvTest;
    private List<ResolveInfo> mlsResolveInfo;
    private RvTestAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRvTest = findViewById(R.id.rv_test);
        mlsResolveInfo = new ArrayList<>();

        mRvTest.setLayoutManager(new GridLayoutManager(this, 4));
        mAdapter = new RvTestAdapter(MainActivity.this, mlsResolveInfo, new RvTestAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                ResolveInfo resolveInfo = mlsResolveInfo.get(position);
                startAppByResolveInfo(resolveInfo);
                MainActivity.this.finish();
            }
        });

        mRvTest.setAdapter(mAdapter);

        List<ResolveInfo> resolveInfos = queryMainActivitiesInfo();
        mlsResolveInfo.addAll(resolveInfos);
        mAdapter.notifyDataSetChanged();

    }


    /**
     * 查询所有包含启动 intent 的 Activity 信息（去掉本应用）
     *
     * @return
     */
    private List<ResolveInfo> queryMainActivitiesInfo() {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resolveInfos = getPackageManager().queryIntentActivities(mainIntent, 0);
        // 去掉本应用
        Iterator<ResolveInfo> iterator = resolveInfos.iterator();
        while (iterator.hasNext()) {
            ResolveInfo resolveInfo = iterator.next();
            String packageName = resolveInfo.activityInfo.packageName;
            if (packageName.equals(getApplication().getPackageName())) {
                iterator.remove();
            }
        }
        return resolveInfos;
    }


    private void startAppByResolveInfo(ResolveInfo resolveInfo) {
        String pkg = resolveInfo.activityInfo.packageName;
        String cls = resolveInfo.activityInfo.name;
        ComponentName componet = new ComponentName(pkg, cls);
        //打开该应用的主activity
        Intent intent = new Intent();
        intent.setComponent(componet);
        startActivity(intent);
    }

}
