# Building two Element X apps

The optional `-PelementSecondInstance=true` Gradle property changes a release build to package ID `io.element.android.x.second`, launcher label “Element X Second”, and a separate OAuth redirect scheme. Without it, release builds retain their usual package ID, label, and redirect scheme.

Build and copy the primary APK before the second build overwrites Gradle's universal APK:

```bash
./gradlew :app:assembleFdroidRelease
cp app/build/outputs/apk/fdroid/release/app-fdroid-universal-release.apk ./element-x-primary.apk
./gradlew :app:assembleFdroidRelease -PelementSecondInstance=true
cp app/build/outputs/apk/fdroid/release/app-fdroid-universal-release.apk ./element-x-second.apk
```

Upload `element-x-primary.apk` and `element-x-second.apk` manually as assets to the desired GitHub Release. This procedure does not publish a release automatically.

On the phone, open the GitHub Release in a browser and download both APKs. Open each downloaded APK from the browser's download notification or the Downloads app, allow that browser or file manager to install unknown apps if prompted, then confirm installation. Both apps can be installed because they use different package IDs.

Release APKs use the repository's `app/signature/debug.keystore`. An existing primary installed from the Play Store with a different signing key cannot be upgraded directly. Future upgrades must use the same signing key as the installed app.
