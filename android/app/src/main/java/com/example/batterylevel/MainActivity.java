package com.example.batterylevel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import io.dcloud.common.DHInterface.ICallBack;
import io.dcloud.feature.sdk.DCSDKInitConfig;
import io.dcloud.feature.sdk.DCUniMPSDK;


import io.dcloud.feature.sdk.MenuActionSheetItem;
import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.MethodChannel;


import android.Manifest;
import android.content.Context;

import android.content.pm.PackageManager;

import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FlutterActivity {
    private static final String CHANNEL = "samples.flutter.dev/battery";
    private static final String downloadUrl = "http://www.start7.cn/";
    Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this.getContext();
        ImplementNetWork();

        MenuActionSheetItem item = new MenuActionSheetItem("关于", "gy");
        List<MenuActionSheetItem> sheetItems = new ArrayList<>();
        sheetItems.add(item);
        DCSDKInitConfig config = new DCSDKInitConfig.Builder()
                .setCapsule(true)
                .setMenuDefFontSize("16px")
                .setMenuDefFontColor("#ff00ff")
                .setMenuDefFontWeight("normal")
                .setMenuActionSheetItems(sheetItems)
                .build();
        DCUniMPSDK.getInstance().initialize(this, config, new DCUniMPSDK.IDCUNIMPPreInitCallback() {
            @Override
            public void onInitFinished(boolean isSuccess) {
                Log.e("unimp", "onInitFinished-----------"+isSuccess);
            }
        });

        android.util.Log.d("", "onCreate: ");
    }

    @Override
    public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
        super.configureFlutterEngine(flutterEngine);

        checkNeedPermissions();

        new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), CHANNEL)
                .setMethodCallHandler(
                        (call, result) -> {
                            // Note: this method is invoked on the main thread.
                             if (call.method.equals("localResourceApplet")) {
                                try {
                                    if(!call.arguments.toString().isEmpty()) {
                                        DCUniMPSDK.getInstance().startApp(mContext, call.arguments());
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }else if (call.method.equals("remoteApplet")) {
                                 if(!call.arguments.toString().isEmpty()) {
                                     String sdPath = Environment.getExternalStorageDirectory() + "/Download/";
                                       String r = DCUniMPSDK.getInstance().getAppBasePath(mContext);
                                        android.util.Log.d("小程序保存地址是:", r);
                                        Thread thread = new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                downloadFun(sdPath, call.arguments());
                                            }
                                        });
                                        thread.start();
                                 }
                            }else {
                                result.notImplemented();
                            }
                        }
                );
    }

    private void checkNeedPermissions(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //多个权限一起申请
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, 1);
        }
    }

    private void getRemoteDown(String pathName, String sdPath) {
        String wgtPath = sdPath + pathName + ".wgt";
        android.util.Log.d("当前下载的路径", "configureFlutterEngine: " + wgtPath);
        android.util.Log.d("当前下载的名称", "configureFlutterEngine: " + pathName);
        try {
            DCUniMPSDK.getInstance().releaseWgtToRunPathFromePath(pathName, wgtPath, new ICallBack() {
                @Override
                public Object onCallBack(int code, Object pArgs) {
                    if(code == 1) {//释放wgt完成
                        try {
                            Toast.makeText(getApplicationContext(), wgtPath,
                                    Toast.LENGTH_SHORT).show();
                            DCUniMPSDK.getInstance().startApp(mContext, pathName);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else{//释放wgt失败
                        Toast.makeText(mContext, "资源释放失败", Toast.LENGTH_SHORT).show();
                    }
                    return null;
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    void downloadFun(String path, String name) {
        checkNeedPermissions();
        String tag = "下载小程序";
        DownloadUtil.get().download(downloadUrl, path, name + ".wgt",
                new DownloadUtil.OnDownloadListener() {
                    @Override
                    public void onDownloadSuccess(File file) {
                        android.util.Log.d(tag, "下载成功");
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                getRemoteDown(name, path);
                            }
                        });
                        thread.start();
                    }

                    @Override
                    public void onDownloading(int progress) {
                        android.util.Log.d(tag, "下载进度" + progress);
                    }

                    @Override
                    public void onDownloadFailed(Exception e) {
                        ShowToast("下载失败");
                        android.util.Log.d(tag, "下载失败!");
                    }
                });
    }


    void ShowToast(String title) {
        Toast.makeText(getApplicationContext(), title,
                Toast.LENGTH_SHORT).show();
    }

    // 主线程可以执行网络操作
    void ImplementNetWork() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
}
