-keep class org.tensorflow.** { *; }
-keep class com.google.mlkit.** { *; }
-keep class com.google.firebase.** { *; }

-keep class androidx.room.** { *; }
-keepclassmembers class * { @androidx.room.* *; }

-keepattributes Signature
-keepattributes *Annotation*
