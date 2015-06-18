# android-adapter
Example project how to use Avocarrot SDK with other Ad Networks, in your Android projects.


## Contents
* [Mopub](#mopub)
  * [Getting Started](#getting-started)
  * [Setup Mopub](#create-avocarrot-network-in-mopub-dashboard-and-connect-with-your-ad-units)
  * [Interstitial](#interstitial)
  * [Native](#native)

===

## Mopub
You can use **Avocarrot** as a Network in Mopub Mediation

===

### Getting Started 
* Integrate with Avocarrot SDK (https://app.avocarrot.com/#/docs/getting-started/android)
* Integrate Mopub SDK (https://github.com/mopub/mopub-android-sdk/wiki/Getting-Started)

===

### Create "Avocarrot Network" in Mopub dashboard and connect with your Ad Units.
* On Mopub dashboard select "Network" > "Add New network"
![_networks](https://cloud.githubusercontent.com/assets/6909699/8229858/5feec64e-15c3-11e5-9921-7585b9f57861.png)
* Select "Custom Native Network"
![_add-new-network](https://cloud.githubusercontent.com/assets/6909699/8229862/68b9b7ca-15c3-11e5-9e0a-76537a52734a.png)
* Complete fields depends on your ad unit
![_setup](https://cloud.githubusercontent.com/assets/6909699/8229864/6b65cbc6-15c3-11e5-9aa4-277aa7ecd775.png)

===

### Interstitial
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

### Native
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
