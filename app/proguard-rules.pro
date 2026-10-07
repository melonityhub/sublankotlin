# Keep Flutter bridge if any, keep sherpa, keep revenuecat
-keep class com.androyal.subxplayer.** { *; }
-keep class com.revenuecat.** { *; }
-keep class com.google.mlkit.** { *; }
-keep class androidx.media3.** { *; }
-keep class sh.sherpa.** { *; }
-dontwarn org.apache.tika.**
-dontwarn com.ichi2.anki.**
-keepattributes Signature, InnerClasses, EnclosingMethod
