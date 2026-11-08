# Proguard rules for Fitman Native
-keep class com.fitman.nativeapp.data.model.** { *; }
-keepclassmembers class * {
    @androidx.compose.runtime.Composable *;
}
