<img width="300" src="https://cloud.githubusercontent.com/assets/1907604/7618436/f8c371de-f9a9-11e4-8846-772f67f53513.jpg"/>


# android-adapters
Example project on how to use the Avocarrot SDK with other Ad Networks, in your Android projects.

For any technical help or questions, please get in touch with [support](https://app.avocarrot.com/#/docs/contact)

## Contents
* [Mopub](#mopub)
  * [1. Setup SDKs](#1-setup-sdks)
  * [2. Setup Mopub Dashboard](#2-setup-mopub-dashboard)
    * [a) Interstitial](#a-interstitial)
    * [b) Native](#b-native)
  * [3. Ready to GO!](#3-ready-to-go)
* [Troubleshooting](#troubleshooting)   
* [How to Clone the Example Project](#clone-the-example-project)  


===

### Mopub
You can use **Avocarrot** as a `Network` in **Mopub's** Mediation platform.

===

#### 1. Setup SDKs

* Integrate with Mopub SDK (https://github.com/mopub/mopub-android-sdk/wiki/Getting-Started)
* Install Avocarrot SDK  [(Download SDK)](https://s3.amazonaws.com/avocarrot-android-builds/avocarrot-sdk.zip) <br/>
*More info how to install Avocarrot SDK on [Document Section](https://app.avocarrot.com/#/docs/getting-started/android)*


===

#### 2. Setup Mopub Dashboard

Create an "Avocarrot" `Network` in Mopub's dashboard and connect it to your Ad Units.

* In Mopub's dashboard select `Networks`  > `Add New network`

![_networks](https://cloud.githubusercontent.com/assets/1907604/8231788/d78cf0dc-15c2-11e5-9bce-ed3e1e056325.png)

* Then select `Custom Native Network`

![_add-new-network](https://cloud.githubusercontent.com/assets/1907604/8231640/d721a6ac-15c1-11e5-892e-a317787adc9e.png)

* Complete the fields accordingly to the Ad Unit that you want to use

![_setup](https://cloud.githubusercontent.com/assets/6909699/8268600/01a399de-1794-11e5-8253-07df0154c259.png)

===

#### a) Interstitial
For Interstitial Ad Units (`Natural Pause` in Avocarrot => `Fullscreen`, `Medium` etc formats in Mopub), you need to :

- Complete the following in Mopub Dashboard:

**Custom Event Class**
```java
com.mopub.mobileads.AvocarrotInterstitialMopub
```

**Custom Event Class Data**
```javascript
{"apiKey":"<AvocarrotApiKey>","placementKey":"<AvocarrotPlacementKey>"}
```

Get your api & placement keys from the [Avocarrot Dashboard](https://app.avocarrot.com/#/apps/overview).
Please note that for `Mopub Interstitial` you should create an `Avocarrot "Natural Pause"` placement. 

- Include the [`AvocarrotInterstitialMopub`](https://github.com/Avocarrot/android-adapter/blob/master/avocarrotadapter/src/main/java/com/mopub/mobileads/AvocarrotInterstitialMopub.java) in your project.


===

#### b) Native
For Native Ad Units (such as `List`, `Feed` in Avocarrot's Dashboard => `Native (Custom Layout)` Format in Mopub) you need to :

- Complete the following in Mopub Dashboard:

**Custom Event Class**
```java
com.mopub.nativeads.AvocarrotNativeMopub
```

**Custom Event Class Data**
```javascript
{"apiKey":"<AvocarrotApiKey>","placementKey":"<AvocarrotPlacementKey>"}
```

Get your api & placement keys from the [Avocarrot Dashboard](https://app.avocarrot.com/#/apps/overview).
Please note that for `Mopub Native` you should create an `Avocarrot "Create your own"` placement. 

- Include the [`AvocarrotNativeMopub`](https://github.com/Avocarrot/android-adapter/blob/master/avocarrotadapter/src/main/java/com/mopub/nativeads/AvocarrotNativeMopub.java) in your project.

===

#### 3. Ready to GO!

Congratulations! You have now successfully integrated **Avocarrot** and you should have received your first ad.


#### Troubleshooting

- If you have any problem, you can enable the logger or/and sandbox mode by adding `"sandbox":"true" , "logger":"true"` in Custom Event Class Data.
- Please have in mind, that any time you make a change to the Mopub dashboard, try to fetch an ad a couple times and then wait a few minutes for Mopub's cache to clear.
- You can also try cloning the example project below to make sure everything is running ok
- If at any point you need any technical help, please get in touch with [support] (https://app.avocarrot.com/#/docs/contact)

### Clone the Example Project
* git clone https://github.com/Avocarrot/android-adapters.git
* cd android-adapters
* git submodule update --init --recursive
* Import to Android Studio
* Update your Mediation Ad Unit Ids in res/string.xml
* Run in the emulator and you should receive ads normally in the Example App
