<img width="300" src="https://cloud.githubusercontent.com/assets/1907604/7618436/f8c371de-f9a9-11e4-8846-772f67f53513.jpg"/>


# android-adapter
Example project how to use Avocarrot SDK with other Ad Networks, in your Android projects.

## Contents
* [Mopub](#mopub)
  * [Getting Started](#getting-started)
  * [Setup Mopub](#create-avocarrot-network-in-mopub-dashboard-and-connect-with-your-ad-units)
  * [Interstitial](#interstitial)
  * [Native](#native)

===

### Mopub
You can use **Avocarrot** as a `Network` in **Mopub's** Mediation platform.

===

#### Getting Started 

* Integrate with Avocarrot SDK (https://app.avocarrot.com/#/docs/getting-started/android)
* Integrate with Mopub SDK     (https://github.com/mopub/mopub-android-sdk/wiki/Getting-Started)

===

#### Create an "Avocarrot" `Network` in Mopub's dashboard and connect it to your Ad Units.

* In Mopub's dashboard select `Networks`  > `Add New network`

![_networks](https://cloud.githubusercontent.com/assets/1907604/8231788/d78cf0dc-15c2-11e5-9bce-ed3e1e056325.png)

* Then select `Custom Native Network`

![_add-new-network](https://cloud.githubusercontent.com/assets/1907604/8231640/d721a6ac-15c1-11e5-892e-a317787adc9e.png)

* Complete the fields accordingly to the Ad Unit that you want to use

![_setup](https://cloud.githubusercontent.com/assets/1907604/8231932/c2019c76-15c3-11e5-81a9-703a0e986398.png)

===

#### Interstitial
For Interstitial Ad Units you need to :

- complete the following in Mopub Dashboard:

**Custom Event Class**
```java
com.mopub.mobileads.AvocarrotInterstitialMopub
```

**Custom Event Class Data**
```javascript
{"appId":"ReplaceWithYourAvocarrotAppId","placement":"ReplaceWithYourAvocarrotPlacement"}
```

- include the [`AvocarrotInterstitialMopub`](https://github.com/Avocarrot/android-adapter/blob/master/avocarrotadapter/src/main/java/com/mopub/mobileads/AvocarrotInterstitialMopub.java) in your project.

- select **"Natural Pause"** placement type in Avocarrot Dashboard.


===

#### Native
For Native Ad Units (such as `List`, `Feed` in Avocarrot's Dashboard) you need to :

- complete the following in Mopub Dashboard:

**Custom Event Class**
```java
com.mopub.nativeads.AvocarrotNativeMopub
```

**Custom Event Class Data**
```javascript
{"appId":"ReplaceWithYourAvocarrotAppId","placement":"ReplaceWithYourAvocarrotPlacement"}
```

- include the [`AvocarrotNativeMopub`](https://github.com/Avocarrot/android-adapter/blob/master/avocarrotadapter/src/main/java/com/mopub/nativeads/AvocarrotNativeMopub.java) in your project.

- select **"Create your own"** placement type in Avocarrot Dashboard.

===
