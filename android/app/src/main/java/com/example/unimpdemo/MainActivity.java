//package com.example.unimpdemo;
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.Toast;
//
//import androidx.annotation.Nullable;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import io.dcloud.feature.sdk.DCUniMPJSCallback;
//import io.dcloud.feature.sdk.DCUniMPSDK;
//
//public class MainActivity extends Activity {
//    Context mContext;
//    Handler mHandler;
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        mContext = this;
//        mHandler = new Handler();
//        setContentView(R.layout.main);
//        Button button1 = findViewById(R.id.button1);
//
//        button1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
//                    DCUniMPSDK.getInstance().startApp(mContext,"__UNI__04E3A11", MySplashView.class);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//
//        Button button2 = findViewById(R.id.button2);
//
//        button2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
//                    DCUniMPSDK.getInstance().startApp(mContext,"__UNI__04E3A11");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        Button button3 = findViewById(R.id.button3);
//
//        button3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
//                    //"pages/tabBar/extUI/extUI" "pages/component/scroll-view/scroll-view"
//                    DCUniMPSDK.getInstance().startApp(mContext,"__UNI__04E3A11", "pages/tabBar/extUI/extUI?aaa=123&bbb=456");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        Button button4 = findViewById(R.id.button4);
//
//        button4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
//                    DCUniMPSDK.getInstance().startApp(mContext,"__UNI__04E3A11", "pages/component/view/view");
////                    mHandler.postDelayed(new Runnable() {
////                        @Override
////                        public void run() {
////                            Log.e("unimp", "延迟5秒结束 开始关闭当前小程序");
////                            DCUniMPSDK.getInstance().closeCurrentApp();
////                        }
////                    }, 5000);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        Button button5 = findViewById(R.id.button5);
//
//        button5.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                JSONObject info = DCUniMPSDK.getInstance().getAppVersionInfo("__UNI__04E3A11");
//                if(info != null) {
//                    Log.e("unimp", "info==="+info.toString());
//                }
//            }
//        });
//
//        Button button6 = findViewById(R.id.button6);
//        button6.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
//                    DCUniMPSDK.getInstance().startApp(mContext,"__UNI__2108B0A");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        Button button7 = findViewById(R.id.button7);
//        button7.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
//                    DCUniMPSDK.getInstance().startApp(mContext,"__UNI__2108B0A", "pages/sample/send-event");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        Button button8 = findViewById(R.id.button8);
//        button8.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    DCUniMPSDK.getInstance().startApp(mContext,"__UNI__2108B0A", "pages/sample/ext-module");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//
//        DCUniMPSDK.getInstance().setDefMenuButtonClickCallBack(new DCUniMPSDK.IMenuButtonClickCallBack() {
//            @Override
//            public void onClick(String appid, String id) {
//                switch (id) {
//                    case "gy":{
//                        Log.e("unimp", "点击了关于" + appid);
//                        //宿主主动触发事件
//                        JSONObject data = new JSONObject();
//                        try {
//                            data.put("sj", "点击了关于");
//                            DCUniMPSDK.getInstance().sendUniMPEvent("gy", data);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        break;
//                    }
//                    case "hqdqym" :{
//                        Log.e("unimp", "当前页面url=" + DCUniMPSDK.getInstance().getCurrentPageUrl());
//                        break;
//                    }
//                    case "gotoTestPage" :
//
//
//
//                }
//            }
//        });
//
//        DCUniMPSDK.getInstance().setUniMPOnCloseCallBack(new DCUniMPSDK.IUniMPOnCloseCallBack() {
//            @Override
//            public void onClose(String appid) {
//                Log.e("unimp", appid+"被关闭了");
//                Toast.makeText(mContext, appid+"被关闭了", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        DCUniMPSDK.getInstance().setOnUniMPEventCallBack(new DCUniMPSDK.IOnUniMPEventCallBack() {
//            @Override
//            public void onUniMPEventReceive(String event, Object data, DCUniMPJSCallback callback) {
//                Log.i("cs", "onUniMPEventReceive    event="+event);
//                //回传数据给小程序
//                callback.invoke( "收到消息");
//            }
//        });
//
//    }
//}