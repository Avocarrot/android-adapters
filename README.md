<img width="300" src="https://cloud.githubusercontent.com/assets/1907604/7618436/f8c371de-f9a9-11e4-8846-772f67f53513.jpg"/>


# android-adapters
Example project on how to use the Avocarrot SDK with other Ad Networks, in your Android projects.

## Contents
* [Mopub](#mopub)
  * [Getting Started](#getting-started)
  * [Setup Mopub](#create-avocarrot-network-in-mopub-dashboard-and-connect-with-your-ad-units)
  * [Interstitial](#interstitial)
  * [Native](#native)
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

![_setup](https://cloud.githubusercontent.com/assets/1907604/8231932/c2019c76-15c3-11e5-81a9-703a0e986398.png)

===

#### a) Interstitial
For Interstitial Ad Units you need to :

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
For Native Ad Units (such as `List`, `Feed` in Avocarrot's Dashboard) you need to :

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

Congratulations! You have successfully integrated **Avocarrot**. 

If you have any problem, you can enable the logger or/and sandbox mode by adding `"sandbox":"true" , "logger":"true"` in Custom Event Class Data. For any technical help, please [get in touch](https://app.avocarrot.com/#/docs/contact)



### Clone the Example Project
* git clone https://github.com/Avocarrot/android-adapters.git
* git submodule update --init --recursive
* Update your Mediation Ad Unit Ids in res/string.xml
