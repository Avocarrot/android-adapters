# Avocarrot Interstitial Ads Adapter for AdMob Android SDK

This is an adapter to be used in conjunction with the AdMob Android SDK.

## Requirements

* Google Mobile Ads SDK version 11.0.4.
* Avocarrot Android SDK v4.5.1.

## Instructions

### Integrating Avocarrot Android SDK

Add the compile dependency with the latest version of the **Avocarrot** adapter in the `build.gradle` file:

```
repositories {
  maven { url  "https://s3.amazonaws.com/avocarrot-android-builds/dist" }
}

dependencies {
  compile 'com.avocarrot.sdk:adapter-admob-interstitial:1.4.1'
}
```

The Avocarrot SDK should be initialised and started before requiring for ads. To ensure that the metrics are not over-counted, it is highly recommended 
that the Avocarrot SDK be called in the `Application` class.

```java
public class YourApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Avocarrot.onApplicationCreated(this);
        //...
    }
    //...
}
```

Don't forget to add application name to your `AndroidManifest.xml` file.

```xml
<application android:name=".YourApplication">
  <!-- activities goes here -->
</application>
```