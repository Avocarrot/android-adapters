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
You can use **Avocarrot** as a Network in Mopub Mediation

===

#### Getting Started 
* Integrate with Avocarrot SDK (https://app.avocarrot.com/#/docs/getting-started/android)
* Integrate Mopub SDK (https://github.com/mopub/mopub-android-sdk/wiki/Getting-Started)

===

#### Create "Avocarrot Network" in Mopub dashboard and connect with your Ad Units.
* On Mopub dashboard select "Network" > "Add New network"

![_networks](https://cloud.githubusercontent.com/assets/1907604/8231639/d71a256c-15c1-11e5-9d4e-d125a773f2c0.png)

* Select "Custom Native Network"

![_add-new-network](https://cloud.githubusercontent.com/assets/1907604/8231640/d721a6ac-15c1-11e5-892e-a317787adc9e.png)

* Complete fields depends on your ad unit

![_setup](https://cloud.githubusercontent.com/assets/1907604/8231638/d70fbdac-15c1-11e5-92d1-09699b53cdeb.png)

===

#### Interstitial
For using Interstitial you need to fill :

**Custom Event Class**
```java
com.mopub.mobileads.AvocarrotInterstitialMopub
```

**Custom Event Class Data**
```javascript
{"appId":"ReplaceWithYourAvocarrotAppId","placement":"ReplaceWithYourAvocarrotPlacement"}
```

Also you must include the [`AvocarrotInterstitialMopub`](https://github.com/Avocarrot/android-adapter/blob/master/avocarrotadapter/src/main/java/com/mopub/mobileads/AvocarrotInterstitialMopub.java) into your project.

===

#### Native
For using Native Ad (aka List in Avocarrot Dashboard) you need to fill :

**Custom Event Class**
```java
com.mopub.nativeads.AvocarrotNativeMopub
```

**Custom Event Class Data**
```javascript
{"appId":"ReplaceWithYourAvocarrotAppId","placement":"ReplaceWithYourAvocarrotPlacement"}
```

Also you must include the [`AvocarrotNativeMopub`](https://github.com/Avocarrot/android-adapter/blob/master/avocarrotadapter/src/main/java/com/mopub/nativeads/AvocarrotNativeMopub.java) into your project.

===
