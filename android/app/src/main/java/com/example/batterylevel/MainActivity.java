package com.example.batterylevel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import io.dcloud.common.DHInterface.ICallBack;
import io.dcloud.feature.sdk.DCSDKInitConfig;
import io.dcloud.feature.sdk.DCUniMPSDK;


import io.dcloud.feature.sdk.MenuActionSheetItem;
import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.MethodChannel;
import okhttp3.Response;
import okhttp3.ResponseBody;


import android.Manifest;
import android.content.Context;

import android.content.pm.PackageManager;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import com.ejlchina.okhttps.HTTP;

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
                                     String sdPath = mContext.getExternalCacheDir().getPath() + "/Download/";
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
//                            Toast.makeText(getApplicationContext(), wgtPath,
//                                    Toast.LENGTH_SHORT).show();
                            DCUniMPSDK.getInstance().startApp(mContext, pathName);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else{//释放wgt失败
//                        Toast.makeText(mContext, "资源释放失败", Toast.LENGTH_SHORT).show();
                    }
                    return null;
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    void handler(Runnable run) {
        new Handler(Looper.getMainLooper()).post(run);
    }

    void downloadFun(String path, String name) {
        checkNeedPermissions();
        try {
            HTTP http = HTTP.builder()
                    .config( builder -> builder.addInterceptor(chain -> {
                        Response res = chain.proceed(chain.request());
                        ResponseBody body = res.body();
                        ResponseBody newBody = null;
                        if (body != null) {
                            newBody = ResponseBody.create(body.contentType(), body.bytes());
                        }
                        return res.newBuilder().body(newBody).build();
                    }))
                    .callbackExecutor((Runnable run) -> {
                handler(run); // 在主线程执行
            }).build();
            String nameStr = name + ".wgt";
            String resultName = path + nameStr;
            http.sync(downloadUrl + nameStr).get().getBody().toFile(resultName).start();
            Log.d("下载", "存储路径为: " + resultName);
            getRemoteDown(name, path);
        }catch (Exception e) {
            e.printStackTrace();
        }
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
