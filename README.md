# IranApps In-app Billing Helper

this library is a small helper with a simple API that makes it very easy to communicate with IranApps in-app billing service.

in the project there's also a sample of how to use the helper.

you can find [IranApps in-app billing documentation here](http://developer.iranapps.ir/docs/inappbilling)

### Current version 2.0

### Gradle Dependency (jCenter)  
the library is uploaded to jCenter and you can see it below:  
[ ![Download](https://api.bintray.com/packages/iranapps/maven/billing-helper/images/download.svg) ](https://bintray.com/iranapps/maven/billing-helper/_latestVersion)

---


### Import in Android Studio
Easily reference the library in your Android projects using this dependency in your module's `build.gradle` file:  
```Gradle
dependencies {
    compile 'ir.tgbs.iranapps:billing-helper:2.0'
}
```

---

### Import in Eclipse  
to use this library in your eclpise projects to the following:  
1. download the library's jar file from [jCenter at here](https://bintray.com/artifact/download/iranapps/maven/ir/tgbs/iranapps/billing-helper/2.0/billing-helper-2.0.jar)  
2. add the jar as a library to your project.  
3. download IranApps in-app billing aidl file [from here](https://raw.githubusercontent.com/IranApps/InAppBillingHelper/master/helper/src/main/aidl/ir/tgbs/iranapps/billing/IranAppsIabService.aidl)  
4. put the downloaded aidl file in `src/ir/tgbs/iranapps/billing`  
5. add IranApps billing permission to your manifest `<uses-permission android:name="ir.tgbs.iranapps.permission.BILLING"/>`  
6. your ready to go, Enjoy

---

## Methods You Must Override

* **in the activity that you want to do in-app billing requests create a new instance of `InAppHelper` inside `onCreate`.**  
```
@Override
public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
    super.onCreate(savedInstanceState, persistentState);
    setContentView(R.layout.activity_main);
    inAppHelper = new InAppHelper(this, new InAppHelper.InAppHelperListener() {
        @Override
        public void onConnectedToIABService() {
        }

        @Override
        public void onCantConnectToIABService(InAppError inAppError) {
        }

        @Override
        public void onConnectionLost() {
        }
    });
}
```
* **Override `onDestroy` in that activity and call `onActivityDestroy` in `InAppHelper`**  
```
@Override
protected void onDestroy() {
    super.onDestroy();
    inAppHelper.onActivityDestroy();
}
```
* **Override `onActivityResult` in that activity and call `onActivityResult` in `InAppHelper`**  
```
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    inAppHelper.onActivityResult(requestCode, resultCode, data);
}
```

---

## Activity recreation support (config changes)
if your activity is able to be destroyed and recreated you must save and restore the InAppHelper of that activity and update the listener of any operation that is currently in progress.
for an example you can see the [BaseInAppActivity](https://github.com/IranApps/InAppBillingHelper/blob/master/sample/src/main/java/ir/tgbs/iranapps/inappbilling/sample/activity/BaseInAppActivity.java) and the implementation of 'InAppHolderFragment' that saves the data and can be retained in activity's onCreate method(also see the onCreate() method inside BaseInAppActivity on how the InAppHelper instance is created). and see [PurchaseActivity](https://github.com/IranApps/InAppBillingHelper/blob/master/sample/src/main/java/ir/tgbs/iranapps/inappbilling/sample/activity/PurchasesActivity.java) on how the buy product operation's listener is updated.


## Proguard support
if your using proguard in your app add this line to you proguard-rules.pro file
```
-keep class ir.tgbs.iranapps.billing.IranAppsIabService
```


## Run the sample
this project is based on Android Studio.  
to localy run the sample you need to do the following:

1. download the project from [**here**](https://github.com/IranApps/InAppBillingHelper/archive/master.zip)
2. extract the project zip
3. from Android studio go to File->import project and select the folder of extracted files
4. now run the sample module.

## Contributing

1. Fork it!
2. Create your feature branch: `git checkout -b my-new-feature`
3. Commit your changes: `git commit -am 'Add some feature'`
4. Push to the branch: `git push origin my-new-feature`
5. Submit a pull request :D

## Credits

TODO: Write credits

## License

TODO: Write license
