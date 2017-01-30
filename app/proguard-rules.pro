# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\7__PROGRAMMS\Android\sdk/tools/proguard/proguard-android.txt
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

-keep class by.vshkl.** { *; }
-dontobfuscate

# Jsoup ----------------------------------------------------------------------------------------------------------------
-keeppackagenames org.jsoup.nodes

# OkHttp3 --------------------------------------------------------------------------------------------------------------
-keepattributes Signature
-keepattributes *Annotation*
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**

# Caligraphy -----------------------------------------------------------------------------------------------------------
-keep class uk.co.chrisjenx.calligraphy.* { *; }
-keep class uk.co.chrisjenx.calligraphy.*$* { *; }

# Fresco ---------------------------------------------------------------------------------------------------------------
-keep,allowobfuscation @interface com.facebook.common.internal.DoNotStrip
-keep @com.facebook.common.internal.DoNotStrip class *
-keepclassmembers class * {
    @com.facebook.common.internal.DoNotStrip *;
}
-keepclassmembers class * {
    native <methods>;
}
-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn com.android.volley.toolbox.**

# DbFlow ---------------------------------------------------------------------------------------------------------------
-keep class * extends com.raizlabs.android.dbflow.config.DatabaseHolder { *; }
