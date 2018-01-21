# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\soft\androidSDK/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-keep class com.hyphenate.** {*;}
-dontwarn  com.hyphenate.**
-keepattributes EnclosingMetho

-dontoptimize
-dontpreverify

-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }

-dontwarn cn.jiguang.**
-keep class cn.jiguang.** { *; }


-dontwarn com.google.**
-keep class com.google.gson.** {*;}
-keep class com.google.protobuf.** {*;}

-dontusemixedcaseclassnames
-dontshrink
-dontoptimize
-dontwarn com.google.android.maps.**
-dontwarn android.webkit.WebView
-dontwarn com.umeng.**
-dontwarn com.tencent.weibo.sdk.**
-dontwarn com.facebook.**
-keep public class javax.**
-keep public class android.webkit.**
-dontwarn android.support.v4.**
-keep enum com.facebook.**
-keepattributes Exceptions,InnerClasses,Signature
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable

-keep public interface com.facebook.**
-keep public interface com.tencent.**
-keep public interface com.umeng.socialize.**
-keep public interface com.umeng.socialize.sensor.**
-keep public interface com.umeng.scrshot.**
-keep class com.android.dingtalk.share.ddsharemodule.** { *; }
-keep public class com.umeng.socialize.* {*;}


-keep class com.facebook.**
-keep class com.facebook.** { *; }
-keep class com.umeng.scrshot.**
-keep public class com.tencent.** {*;}
-keep class com.umeng.socialize.sensor.**
-keep class com.umeng.socialize.handler.**
-keep class com.umeng.socialize.handler.*
-keep class com.umeng.weixin.handler.**
-keep class com.umeng.weixin.handler.*
-keep class com.umeng.qq.handler.**
-keep class com.umeng.qq.handler.*
-keep class UMMoreHandler{*;}
-keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage {*;}
-keep class com.tencent.mm.sdk.modelmsg.** implements   com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}
-keep class im.yixin.sdk.api.YXMessage {*;}
-keep class im.yixin.sdk.api.** implements im.yixin.sdk.api.YXMessage$YXMessageData{*;}
-keep class com.tencent.mm.sdk.** {
 *;
}
-dontwarn twitter4j.**
-keep class twitter4j.** { *; }

-keep class com.tencent.** {*;}
-dontwarn com.tencent.**
-keep public class com.umeng.com.umeng.soexample.R$*{
public static final int *;
}
-keep public class com.linkedin.android.mobilesdk.R$*{
public static final int *;
    }
-keepclassmembers enum * {
public static **[] values();
public static ** valueOf(java.lang.String);
}

-keep class com.tencent.open.TDialog$*
-keep class com.tencent.open.TDialog$* {*;}
-keep class com.tencent.open.PKDialog
-keep class com.tencent.open.PKDialog {*;}
-keep class com.tencent.open.PKDialog$*
-keep class com.tencent.open.PKDialog$* {*;}

-keep class com.sina.** {*;}
-dontwarn com.sina.**
-keep class  com.alipay.share.sdk.** {
   *;
}
-keepnames class * implements android.os.Parcelable {
public static final ** CREATOR;
}

-keep class com.linkedin.** { *; }
-keepattributes Signature
-keepattributes EnclosingMetho
-keepattributes InnerClasses
-dontoptimize
-optimizations optimization_filter



##百度
-keepclassmembers class * extends android.app.Activity {    public void *(android.view.View); }
-keepclassmembers enum * {     public static **[] values();     public static ** valueOf(java.lang.String); }
-keep class com.baidu.mobads.*.** { *; }









##---------------sdk混淆-------------------------
#-keep class com.gx.dfttsdk.sdk.** { *; }
#-keep class uk.co.senab.photoview.** { *; }
#
##---------------jsoup混淆-------------------------
#-keep class org.jsoup.**
#
###---------------Rxjava混淆---------------
#-dontwarn sun.misc.**
#-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
# long producerIndex;
# long consumerIndex;
#}
#-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
# rx.internal.util.atomic.LinkedQueueNode producerNode;
#}
#-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
# rx.internal.util.atomic.LinkedQueueNode consumerNode;
#}
#
###---------------ijk混淆---------------
#-keep class tv.danmaku.ijk.media.player.** { *; }
#
###---------------Gson混淆----------
#-keep class com.google.**{*;}
#-keepclassmembers class * implements java.io.Serializable {
#    static final long serialVersionUID;
#    private static final java.io.ObjectStreamField[] serialPersistentFields;
#    private void writeObject(java.io.ObjectOutputStream);
#    private void readObject(java.io.ObjectInputStream);
#    java.lang.Object writeReplace();
#    java.lang.Object readResolve();
#}
#-keep class sun.misc.Unsafe { *; }
#
###---------------OkHttp混淆---------------
#-dontwarn okhttp3.**
#-keep class okhttp3.**{*;}
#-keepattributes SourceFile,LineNumberTable
#-keep class com.parse.*{ *; }
#-dontwarn com.parse.**
#-dontwarn com.squareup.picasso.**
#-keepclasseswithmembernames class * {
#    native <methods>;
#}
#-dontwarn okio.**
#-keep class okio.**{*;}
#
###---------------universal-image-loader混淆-------------
#-keep class com.nostra13.universalimageloader.** { *; }
#-keepclassmembers class com.nostra13.universalimageloader.** {*;}
#
##---------------WebView混淆-------------------------
#-keepclassmembers class fqcn.of.javascript.interface.for.Webview {
#   public *;
#}
#-keepclassmembers class * extends android.webkit.WebViewClient {
#    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
#    public boolean *(android.webkit.WebView, java.lang.String);
#}
#-keepclassmembers class * extends android.webkit.WebViewClient {
#    public void *(android.webkit.WebView, jav.lang.String);
#}
###---------------EventBus混淆---------------
#-keepattributes *Annotation*
#-keepclassmembers class ** {
#    @org.greenrobot.eventbus.Subscribe <methods>;
#}
#-keep enum org.greenrobot.eventbus.ThreadMode { *; }
#-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
#    <init>(java.lang.Throwable);
#}
#-keepclassmembers class ** {
#    public void onEvent*(**);
#}
#
###---------------nineoldandroids混淆---------------
#-dontwarn com.nineoldandroids.*
#-keep class com.nineoldandroids.** { *;}
#
###---------------log4j-1.2.17.jar混淆---------------
#-keep public class * implements java.io.Serializable {*;}
#-dontwarn org.apache.log4j.**
#-dontnote org.apache.log4j.**
#
###---------------org.apache混淆---------------
#-keep class org.apache.**{*;}



-keep class com.songheng.newsapisdk.** { *; }