# Avocarrot Banner Ads Adapter for AdMob and DFP Android SDK

This is an adapter to be used in conjunction with the AdMob or DFP Android SDK.

## Requirements

* Google Mobile Ads SDK version 15.0.1.
* Avocarrot Android SDK v4.10.3.

## Instructions

### Integrating Avocarrot Android SDK

Add the compile dependency with the latest version of the **Avocarrot** adapter in the `build.gradle` file:

```
repositories {
  maven { url  "https://s3.amazonaws.com/avocarrot-android-builds/dist" }
}

dependencies {
  compile 'com.avocarrot.sdk:adapter-admob-banner:1.4.15'
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
