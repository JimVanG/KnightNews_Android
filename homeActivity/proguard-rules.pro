# To enable ProGuard in your project, edit project.properties
# to define the proguard.config property as described in that file.
#
# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in ${sdk.dir}/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the ProGuard
# include property in project.properties.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

#Parse / Facebook
-keep class com.parse.FacebookAuthenticationProvider
-dontwarn com.parse.FacebookAuthenticationProvider
-keep class com.parse.FacebookAuthenticationProvider$1
-dontwarn com.parse.FacebookAuthenticationProvider$1
-keep class com.parse.FacebookAuthenticationProvider$1
-dontwarn com.parse.FacebookAuthenticationProvider$1
-keep class com.parse.FacebookAuthenticationProvider$2$1
-dontwarn com.parse.FacebookAuthenticationProvider$2$1
-keep class com.parse.FacebookAuthenticationProvider$2$1
-dontwarn com.parse.FacebookAuthenticationProvider$2$1
-keep class com.parse.FacebookAuthenticationProvider$2
-dontwarn com.parse.FacebookAuthenticationProvider$2
-keep class com.parse.FacebookAuthenticationProvider$2
-dontwarn com.parse.FacebookAuthenticationProvider$2
-keepattributes *Annotation*
-keep class com.parse.** { *; }

#ParseFacebookUtils
-keep class com.parse.ParseFacebookUtils
-dontwarn com.parse.ParseFacebookUtils

#Picasso
-keep class com.squareup.picasso.OkHttpDownloader
-dontwarn com.squareup.picasso.OkHttpDownloader

#appcompat
-dontnote android.support.**
-dontwarn android.support.v4.**
-keep class android.support.v4.** { *; }
-keep interface android.support.v4.** { *; }
-dontwarn android.support.v7.**
-keep class android.support.v7.** { *; }
-keep interface android.support.v7.** { *; }

#GooglePlayServices
-keep class * extends java.util.ListResourceBundle {
    protected Object[][] getContents();
}
-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
    public static final *** NULL;
}
-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
    @com.google.android.gms.common.annotation.KeepName *;
}
-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}