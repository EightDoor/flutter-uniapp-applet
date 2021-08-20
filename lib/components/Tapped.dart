import 'package:flutter/material.dart';

class Tapped extends StatelessWidget {
  final Widget child;
  final GestureTapCallback onTap;
  const Tapped({
    Key? key,
    required this.child,
    required this.onTap,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return InkWell(
      child: child,
      onTap: onTap,
    );
  }
}
