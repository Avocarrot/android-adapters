# Avocarrot Interstitial Ads Adapter for MoPub Android SDK

This is an adapter to be used in conjunction with the MoPub Android SDK.

## Requirements

* MoPub Android SDK v4.15.0 or later.
* Avocarrot Android SDK v4.6.1 or later.

## Instructions

### Integrating Avocarrot Android SDK

Add the compile dependency with the latest version of the **Avocarrot** adapter in the `build.gradle` file:

```
repositories {
  maven { url  "https://s3.amazonaws.com/avocarrot-android-builds/dist" }
}

dependencies {
  compile 'com.avocarrot.sdk:adapter-mopub-interstitial:1.4.2'
}
```

The Ampiri SDK should be initialised and started before requiring for ads. To ensure that the metrics are not over-counted, it is highly recommended 
that the Ampiri SDK be called in the `Application` class.

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

### Setup MoPub Dashboard

Create an "Avocarrot" Network in MoPub's dashboard and connect it to your Ad Units.

* In MoPub's dashboard select `Networks` > `Add New network`

![_networks](https://cloud.githubusercontent.com/assets/6909699/24693099/a11c06b4-19e4-11e7-8a26-c5b3ba104b6c.png)

* Then select `Custom Native Network`

![_add-new-network](https://cloud.githubusercontent.com/assets/6909699/24693100/a1398f04-19e4-11e7-92a7-a46f3d729aa7.png)

* For Native Ad Units you need complete the following:
  
![_setup](https://cloud.githubusercontent.com/assets/6909699/24693101/a14a981c-19e4-11e7-9559-06d2e54ff5b6.png)

**Custom Event Class**

```java
com.mopub.mobileads.AvocarrotInterstitial
```

**Custom Event Class Data**

```javascript
{"adUnit":"<AvocarrotAdUnit>"}
```

Get your adUnit from the [Avocarrot dashboard](https://login.avocarrot.com/login).
