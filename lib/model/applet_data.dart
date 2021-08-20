/// data : [{"label":"test1","value":"__UNI__9840009"}]

class AppletData {
  List<Data>? _data;

  List<Data>? get data => _data;

  AppletData({List<Data>? data}) {
    _data = data;
  }

  AppletData.fromJson(dynamic json) {
    if (json['data'] != null) {
      _data = [];
      json['data'].forEach((v) {
        _data?.add(Data.fromJson(v));
      });
    }
  }

  Map<String, dynamic> toJson() {
    var map = <String, dynamic>{};
    if (_data != null) {
      map['data'] = _data?.map((v) => v.toJson()).toList();
    }
    return map;
  }
}

/// label : "test1"
/// value : "__UNI__9840009"

class Data {
  String? _label;
  String? _value;

  String? get label => _label;
  String? get value => _value;

  Data({String? label, String? value}) {
    _label = label;
    _value = value;
  }

  Data.fromJson(dynamic json) {
    _label = json['label'];
    _value = json['value'];
  }

  Map<String, dynamic> toJson() {
    var map = <String, dynamic>{};
    map['label'] = _label;
    map['value'] = _value;
    return map;
  }
}
