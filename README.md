<img width="300" src="https://cloud.githubusercontent.com/assets/1907604/7618436/f8c371de-f9a9-11e4-8846-772f67f53513.jpg"/>


# android-adapter
Example project how to use Avocarrot SDK with other Ad Networks, in your Android projects.

## Contents
* [Mopub](#mopub)
  * [Getting Started](#getting-started)
  * [Setup Mopub](#create-avocarrot-network-in-mopub-dashboard-and-connect-with-your-ad-units)
  * [Interstitial](#interstitial)
  * [Native](#native)
* [How to Clone the Example Project](#clone-project)  

===

### Mopub
You can use **Avocarrot** as a `Network` in **Mopub's** Mediation platform.

===

#### Getting Started 

* Integrate with Mopub SDK (https://github.com/mopub/mopub-android-sdk/wiki/Getting-Started)
* Install Avocarrot SDK  [(Download SDK)](https://s3.amazonaws.com/avocarrot-android-builds/avocarrot-sdk.zip) <br/>
*More info how to install Avocarrot SDK on [Document Section](https://app.avocarrot.com/#/docs/getting-started/android)*


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

- Complete the following in Mopub Dashboard:

**Custom Event Class**
```java
com.mopub.mobileads.AvocarrotInterstitialMopub
```

**Custom Event Class Data**
```javascript
{"appId":"ReplaceWithYourAvocarrotAppId","placement":"ReplaceWithYourAvocarrotPlacement"}
```

- Include the [`AvocarrotInterstitialMopub`](https://github.com/Avocarrot/android-adapter/blob/master/avocarrotadapter/src/main/java/com/mopub/mobileads/AvocarrotInterstitialMopub.java) in your project.

- [Login to your dashboard](https://app.avocarrot.com/#/login) and visit your **"My Apps"** tab 

- If you haven't created an app click on the **"Create App"** button to create one 

- Click on the **"Create Placement"** button 

![Create Placement](https://cloud.githubusercontent.com/assets/1907604/8241257/29fd09f0-1600-11e5-9493-fd3ecd319256.jpg)

- Select **"Natural Pause"** placement type in Avocarrot Dashboard.

![Natural Pause](https://cloud.githubusercontent.com/assets/6909699/8232322/7328d42a-15d7-11e5-97de-e17370468f63.png)


===

#### Native
For Native Ad Units (such as `List`, `Feed` in Avocarrot's Dashboard) you need to :

- Complete the following in Mopub Dashboard:

**Custom Event Class**
```java
com.mopub.nativeads.AvocarrotNativeMopub
```

**Custom Event Class Data**
```javascript
{"appId":"ReplaceWithYourAvocarrotAppId","placement":"ReplaceWithYourAvocarrotPlacement"}
```

- Include the [`AvocarrotNativeMopub`](https://github.com/Avocarrot/android-adapter/blob/master/avocarrotadapter/src/main/java/com/mopub/nativeads/AvocarrotNativeMopub.java) in your project.

- [Login to your dashboard](https://app.avocarrot.com/#/login) and visit your **"My Apps"** tab 

- If you haven't created an app click on the **"Create App"** button to create one 

- Click on the **"Create Placement"** button 

![Create Placement](https://cloud.githubusercontent.com/assets/1907604/8241257/29fd09f0-1600-11e5-9493-fd3ecd319256.jpg)

- Select **"Create your own"** placement type in Avocarrot Dashboard.

![Create your own](https://cloud.githubusercontent.com/assets/6909699/8232332/7e086d9c-15d7-11e5-81ef-1878ac2fdbdc.png)

===

### Clone the Example Project
* git clone https://github.com/Avocarrot/android-adapters.git
* git submodule update --init --recursive
* Update your Mediation Ad Unit Ids in res/string.xml
