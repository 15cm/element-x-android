# Building two Element X apps

The optional `-PelementSecondInstance=true` Gradle property changes a release build to package ID `io.element.android.x.second`, launcher label “Element X Second”, and a separate OAuth redirect scheme. Without it, release builds retain their usual package ID, label, and redirect scheme.

Build and copy both APKs before signing. The second build overwrites Gradle's universal APK:

```bash
./gradlew :app:assembleFdroidRelease
cp app/build/outputs/apk/fdroid/release/app-fdroid-universal-release.apk ./element-x-primary-unsigned.apk
./gradlew :app:assembleFdroidRelease -PelementSecondInstance=true
cp app/build/outputs/apk/fdroid/release/app-fdroid-universal-release.apk ./element-x-second-unsigned.apk
```

Gradle signs release outputs with `app/signature/debug.keystore`; do not distribute those outputs directly. Sign both staging APKs with the canonical local key used for distributable APKs. Set `ELEMENT_X_KEYSTORE_PATH` to the local `element-x-local.jks` path from `AGENTS.md`, and set `ELEMENT_X_KEYSTORE_PASSWORD` and `ELEMENT_X_KEY_PASSWORD` from the secure local setup. Keep all signing secrets out of the repository and shell history.

```bash
export ELEMENT_X_KEYSTORE_PATH="/path/to/element-x-local.jks"
export ELEMENT_X_KEYSTORE_PASSWORD="$(cat /path/to/password)"
read -rsp "APK private-key password: " ELEMENT_X_KEY_PASSWORD
export ELEMENT_X_KEY_PASSWORD
printf '\n'

"$ANDROID_HOME/build-tools/37.0.0/apksigner" sign \
  --alignment-preserved true \
  --ks "$ELEMENT_X_KEYSTORE_PATH" \
  --ks-pass env:ELEMENT_X_KEYSTORE_PASSWORD \
  --ks-key-alias element-x-local \
  --key-pass env:ELEMENT_X_KEY_PASSWORD \
  --min-sdk-version 24 \
  --out element-x-primary.apk element-x-primary-unsigned.apk
"$ANDROID_HOME/build-tools/37.0.0/apksigner" sign \
  --alignment-preserved true \
  --ks "$ELEMENT_X_KEYSTORE_PATH" \
  --ks-pass env:ELEMENT_X_KEYSTORE_PASSWORD \
  --ks-key-alias element-x-local \
  --key-pass env:ELEMENT_X_KEY_PASSWORD \
  --min-sdk-version 24 \
  --out element-x-second.apk element-x-second-unsigned.apk
"$ANDROID_HOME/build-tools/37.0.0/apksigner" verify --verbose --print-certs element-x-primary.apk
"$ANDROID_HOME/build-tools/37.0.0/apksigner" verify --verbose --print-certs element-x-second.apk
```

Compare the signer SHA-256 digests; they must match. Upload only `element-x-primary.apk` and `element-x-second.apk` manually as assets to the desired GitHub Release. This procedure does not publish a release automatically.

On the phone, open the GitHub Release in a browser and download both APKs. Open each downloaded APK from the browser's download notification or the Downloads app, allow that browser or file manager to install unknown apps if prompted, then confirm installation. Both apps can be installed because they use different package IDs.

Updates require the installed app's signing key. On September 24, 2026, the v26.09.4 release assets were replaced with APKs signed by the same `element-x-local.jks` key as v26.09.3. Copies of the original v26.09.4 APKs downloaded before replacement still use the old certificate and cannot update to the corrected APKs; uninstall those copies before installing the corrected ones. An existing Play Store install signed with another key also cannot be upgraded directly. Future releases must keep using the same key.
