# Predator
-dontnote com.crazyhitty.chdev.ks.predator.ui.base.BaseAppCompatActivity
-keepclasseswithmembernames class android.support.design.widget.TabLayout { *; }
-keepclasseswithmembernames class android.support.design.widget.TabLayout$Tab { *; }
-keepclasseswithmembernames class android.support.design.widget.TabLayout$TabView { *; }
-keep, includedescriptorclasses class com.crazyhitty.chdev.ks.predator.ui.activities.OnboardActivity {
    void onContinue(com.crazyhitty.chdev.ks.predator.events.OnboardContinueEvent);
    void onOAuthTokenReceivedFromProductHunt(com.crazyhitty.chdev.ks.predator.events.OAuthTokenEvent);
}
-keep, includedescriptorclasses class com.crazyhitty.chdev.ks.predator.ui.activities.SearchActivity { void onNetworkConnectivityChanged(com.crazyhitty.chdev.ks.predator.events.NetworkEvent); }
-keep, includedescriptorclasses class com.crazyhitty.chdev.ks.predator.ui.views.LoadingDialogHeaderView { void setType(com.crazyhitty.chdev.ks.predator.ui.views.LoadingDialogHeaderView$TYPE); }
-keep, includedescriptorclasses class com.crazyhitty.chdev.ks.predator.ui.views.LoadingView {
    void setOnRetryClickListener(com.crazyhitty.chdev.ks.predator.ui.views.LoadingView$OnRetryClickListener);
    void setAnimationStateChangeListener(com.crazyhitty.chdev.ks.predator.ui.views.LoadingView$AnimationStateChangeListener);
    void setLoadingType(com.crazyhitty.chdev.ks.predator.ui.views.LoadingView$TYPE);
}
-keep, includedescriptorclasses class com.crazyhitty.chdev.ks.predator.ui.views.OnboardingSecondView { void animateViews(com.crazyhitty.chdev.ks.predator.events.OnboardSecondAnimateEvent); }
-keep, includedescriptorclasses class com.crazyhitty.chdev.ks.predator.ui.views.OnboardingThirdView { void animateViews(com.crazyhitty.chdev.ks.predator.events.OnboardThirdAnimateEvent); }
# Application classes that will be serialized/deserialized over Gson
-keep class com.crazyhitty.chdev.ks.producthunt_wrapper.models.** { *; }
#----////Predator end////----#


# Butterknife
-keep class butterknife.*
-keepclasseswithmembernames class * { @butterknife.* <methods>; }
-keepclasseswithmembernames class * { @butterknife.* <fields>; }
#----////Butterknife end////----#


# Eventbus
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
#----////Eventbus end////----#


# Fresco
-dontnote com.facebook.common.webp.WebpSupportStatus
-dontnote com.facebook.imagepipeline.animated.factory.AnimatedFactoryProvider
-dontnote com.facebook.imagepipeline.nativecode.WebpTranscoderFactory
# Keep our interfaces so they can be used by other ProGuard rules.
# See http://sourceforge.net/p/proguard/bugs/466/
-keep,allowobfuscation @interface com.facebook.common.internal.DoNotStrip
# Do not strip any method/class that is annotated with @DoNotStrip
-keep @com.facebook.common.internal.DoNotStrip class *
-keepclassmembers class * {
    @com.facebook.common.internal.DoNotStrip *;
}
# Keep native methods
-keepclassmembers class * {
    native <methods>;
}
-dontwarn okio.**
-dontwarn com.squareup.okhttp.**
-dontwarn okhttp3.**
-dontwarn javax.annotation.**
-dontwarn com.android.volley.toolbox.**
-dontwarn com.facebook.infer.**
-keep, includedescriptorclasses class com.facebook.drawee.view.DraweeView {
    void setHierarchy(com.facebook.drawee.interfaces.DraweeHierarchy);
    void setController(com.facebook.drawee.interfaces.DraweeController);
}
-keep, includedescriptorclasses class com.facebook.drawee.view.GenericDraweeView { GenericDraweeView(android.content.Context,com.facebook.drawee.generic.GenericDraweeHierarchy); }
-keep, includedescriptorclasses class com.facebook.imagepipeline.animated.factory.AnimatedFactoryImpl { AnimatedFactoryImpl(com.facebook.imagepipeline.bitmaps.PlatformBitmapFactory,com.facebook.imagepipeline.core.ExecutorSupplier); }
#----////Fresco end////----#


# Stetho
-dontnote com.facebook.stetho.inspector.runtime.RhinoDetectingRuntimeReplFactory
-keep, includedescriptorclasses class com.facebook.stetho.dumpapp.Dumper {
    Dumper(java.lang.Iterable,org.apache.commons.cli.CommandLineParser);
    void dumpPluginOutput(java.io.InputStream,java.io.PrintStream,java.io.PrintStream,org.apache.commons.cli.CommandLine);
}
-keep, includedescriptorclasses class com.facebook.stetho.dumpapp.DumperContext { DumperContext(java.io.InputStream,java.io.PrintStream,java.io.PrintStream,org.apache.commons.cli.CommandLineParser,java.util.List); }
-keep, includedescriptorclasses class com.facebook.stetho.okhttp3.StethoInterceptor { okhttp3.Response intercept(okhttp3.Interceptor$Chain); }
-keep, includedescriptorclasses class com.facebook.stetho.okhttp3.StethoInterceptor$ForwardingResponseBody { StethoInterceptor$ForwardingResponseBody(okhttp3.ResponseBody,java.io.InputStream); }
-keep, includedescriptorclasses class com.facebook.stetho.okhttp3.StethoInterceptor$OkHttpInspectorRequest { StethoInterceptor$OkHttpInspectorRequest(java.lang.String,okhttp3.Request,com.facebook.stetho.inspector.network.RequestBodyHelper); }
-keep, includedescriptorclasses class com.facebook.stetho.okhttp3.StethoInterceptor$OkHttpInspectorResponse { StethoInterceptor$OkHttpInspectorResponse(java.lang.String,okhttp3.Request,okhttp3.Response,okhttp3.Connection); }
#----////Stetho end////----#

# Crashlytics
-dontnote com.crashlytics.android.core.CrashlyticsController
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception
-printmapping mapping.txt
#----////Crashlytics end////----#


# Retrofit
-dontnote retrofit2.Platform$IOS$MainThreadExecutor
# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions
#----////Retrofit end////----#


# Lottie
-keep, includedescriptorclasses class com.airbnb.lottie.LottieAnimationView {
    void setComposition(com.airbnb.lottie.LottieComposition);
    void setImageAssetDelegate(com.airbnb.lottie.ImageAssetDelegate);
    void setFontAssetDelegate(com.airbnb.lottie.FontAssetDelegate);
    void setTextDelegate(com.airbnb.lottie.TextDelegate);
}
#----////Lottie end////----#


# Gson
-dontnote com.google.gson.internal.UnsafeAllocator
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature
# For using GSON @Expose annotation
-keepattributes *Annotation*
# Gson specific classes
-dontwarn sun.misc.**
# Prevent proguard from stripping interface information from TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
#----////Gson end////----#


# Okhttp
-dontnote okhttp3.internal.platform.**
-keep, includedescriptorclasses class okhttp3.ResponseBody {
    okhttp3.ResponseBody create(okhttp3.MediaType,java.lang.String);
    okhttp3.ResponseBody create(okhttp3.MediaType,byte[]);
    okhttp3.ResponseBody create(okhttp3.MediaType,long,okio.BufferedSource);
    okhttp3.ResponseBody create(okhttp3.MediaType,long,okio.BufferedSource);
}
#----////Okhttp end////----#


# Google messaging service
-dontnote com.google.android.gms.common.**
-dontnote com.google.android.gms.internal.*
-dontnote com.google.android.gms.dynamite.DynamiteModule
-keep, includedescriptorclasses class com.google.android.gms.flags.impl.FlagProviderImpl { void init(com.google.android.gms.dynamic.IObjectWrapper); }
-keep, includedescriptorclasses class com.google.android.gms.measurement.AppMeasurement {
    void setEventInterceptor(com.google.android.gms.measurement.AppMeasurement$EventInterceptor);
    void registerOnMeasurementEventListener(com.google.android.gms.measurement.AppMeasurement$OnEventListener);
    void unregisterOnMeasurementEventListener(com.google.android.gms.measurement.AppMeasurement$OnEventListener);
    void registerOnScreenChangeCallback(com.google.android.gms.measurement.AppMeasurement$zza);
    void unregisterOnScreenChangeCallback(com.google.android.gms.measurement.AppMeasurement$zza);
}
#----////Google messaging service end////----#


# Fabric
-dontnote io.fabric.sdk.android.**
#----////Fabric end////----#


# Firebase
-keep, includedescriptorclasses class com.google.firebase.iid.FirebaseInstanceId { com.google.firebase.iid.FirebaseInstanceId getInstance(com.google.firebase.FirebaseApp); }
#----////Firebase end////----#


# Page indicator view
-keep, includedescriptorclasses class com.rd.PageIndicatorView { void setAnimationType(com.rd.animation.AnimationType); }
#----////Page indicator view end////----#


# Changelog library
-keep, includedescriptorclasses class it.gmariotti.changelibs.library.view.ChangeLogListView { void setAdapter(it.gmariotti.changelibs.library.internal.ChangeLogAdapter); }
#----////Changelog library end////----#


# Photo drawee view
-keep, includedescriptorclasses class me.relex.photodraweeview.PhotoDraweeView {
    void setOnScaleChangeListener(me.relex.photodraweeview.OnScaleChangeListener);
    void setOnPhotoTapListener(me.relex.photodraweeview.OnPhotoTapListener);
    void setOnViewTapListener(me.relex.photodraweeview.OnViewTapListener);
}
#----////Photo drawee view end////----#


# Others
-dontnote org.apache.http.params.*
-dontnote org.apache.http.conn.*
-dontnote org.apache.http.conn.scheme.*
-dontnote android.net.http.*
#----////Others end////----#