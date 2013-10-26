package com.njupt.zhb.test;
public class TestMain {
  public static void main(String[] args) {
    // Load the native library.
    System.loadLibrary("opencv_java246");
    new DetectFaceToImages("D:\\lire\\imagecache\\coreImageDatabase").start();
  }
}
