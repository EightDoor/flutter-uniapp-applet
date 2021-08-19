import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        primarySwatch: Colors.blue,
        visualDensity: VisualDensity.adaptivePlatformDensity,
      ),
      home: MyHomePage(),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({Key key}) : super(key: key);

  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  static const platform = const MethodChannel('samples.flutter.dev/battery');

  Future<void> _openMiniProgram3() async {
    try {
      final int result = await platform.invokeMethod('openMiniProgram3');
      print(result);
    } on PlatformException catch (e) {
      print(e);
    }
  }

  Future<void> _openMiniProgram2() async {
    try {
      final int result = await platform.invokeMethod('openMiniProgram2');
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
        body: Column(
          children: [
            FlatButton(
              child: Text(
                "跳转小程序",
              ),
              onPressed: () {
                _openMiniProgram3();
              },
            ),
            FlatButton(
              child: Text(
                "打开本地资源小程序",
              ),
              onPressed: () {
                _openMiniProgram2();
              },
            ),
          ],
        ));
  }
}
