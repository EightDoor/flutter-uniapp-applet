import 'package:batterylevel/model/applet_data.dart';
import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_easyloading/flutter_easyloading.dart';

class MyHomePage extends StatefulWidget {
  const MyHomePage({Key? key}) : super(key: key);

  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  static const platform = const MethodChannel('samples.flutter.dev/battery');
  List<Data> list = [];
  List<Data> sourceData = [];
  List<Map<String, dynamic>> defaultSourceData = [
    {
      "label": "基础组件介绍",
      "value": '__UNI__C3C3FD5',
    },
    {
      "label": "本地静态资源测试",
      "value": "__UNI__9840009",
    }
  ];
  @override
  void initState() {
    getData();
    super.initState();
  }

  void getData() async {
    EasyLoading.show(status: "数据加载中...");
    try {
      var response = await Dio().get("http://www.start7.cn/data.json");
      AppletData appletData = AppletData.fromJson(response.data);
      sourceData = [];
      defaultSourceData.forEach((e) {
        sourceData.add(Data.fromJson(e));
      });
      list = appletData.data!;
      setState(() {});
      EasyLoading.dismiss();
      EasyLoading.showToast("数据加载完成");
    } catch (err) {
      print(err);
    }
  }

  Future<void> remoteApplet(String? name) async {
    try {
      final int result = await platform.invokeMethod(
        'remoteApplet',
        name,
      );
      print(result);
    } on PlatformException catch (e) {
      print(e);
    }
  }

  Future<void> localResourceApplet(String value) async {
    try {
      final int result = await platform.invokeMethod(
        'localResourceApplet',
        value,
      );
      print(result);
    } on PlatformException catch (e) {
      print(e);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(
          "首页",
        ),
      ),
      body: Padding(
        padding: EdgeInsets.all(
          15,
        ),
        child: Column(
          children: [
            _itemText("远程资源小程序"),
            ...list
                .map(
                  (e) => FlatButton(
                    onPressed: () {
                      remoteApplet(e.value);
                    },
                    child: ListTile(
                      title: Text(
                        e.label ?? "",
                      ),
                      trailing: Text(
                        "跳转小程序",
                      ),
                    ),
                  ),
                )
                .toList(),
            _itemText("本地资源小程序"),
            ...sourceData
                .map(
                  (e) => FlatButton(
                    onPressed: () {
                      localResourceApplet(
                        e.value ?? "",
                      );
                    },
                    child: ListTile(
                      title: Text(
                        e.label ?? "",
                      ),
                      trailing: Text(
                        "跳转小程序",
                      ),
                    ),
                  ),
                )
                .toList(),
            FlatButton(
              onPressed: () {
                getData();
              },
              child: Text(
                "刷新远程小程序资源列表",
              ),
            )
          ],
        ),
      ),
    );
  }

  Widget _itemText(String title) {
    return Align(
      alignment: Alignment.centerLeft,
      child: Text(
        title,
        style: TextStyle(
          color: Colors.black,
          fontSize: 18,
        ),
      ),
    );
  }
}
