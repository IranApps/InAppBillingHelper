# IranApps In-app Billing Helper

this library is a small helper with a simple API that makes it very easy to communicate with IranApps in-app billing service.

in the project there's also a sample of how to use the helper.

you can find [IranApps in-app billing documentation here](http://developer.iranapps.ir/docs/inappbilling)

### Current version 1.0

### Gradle Dependency (jCenter)  
Easily reference the library in your Android projects using this dependency in your module's `build.gradle` file:  
```Gradle
dependencies {
    compile 'ir.tgbs.iranapps:billing-helper:1.0'
}
```
[ ![Download](https://api.bintray.com/packages/iranapps/maven/billing-helper/images/download.svg) ](https://bintray.com/iranapps/maven/billing-helper/_latestVersion)

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
