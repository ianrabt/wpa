# wpa
Wellness and Productivity App

## Build and Set Up Instructions

1. First import the project into Android Studio using File / New / Project from
   Version Control / Git
2. In order for Firebase's Google Authentication to work, you must add the
   fingerprint of your Android Debug key to our project's [Firebase
   Console](https://console.firebase.google.com) (you should have received an
   email invite from Michael)
   1. First run `keytool -exportcert -list -v -alias 'androiddebugkey' -keystore
      ~/.android/debug.keystore` and copy the `SHA1` fingerprint which is
      printed in the console.
   2. Then open the Firebase Console, and navigate to the **HabitTrackerWPA**
      project, use the sidebar to click the gear icon, and then click **Project
      Settings**.
   3. Scroll down, click **Add Fingerprint** and paste in the fingerprint you
      copied earlier.
   4. Now, authentication will not fail on the APKs you build. (**TODO:** *Is
      this the best way to set up developer keys for this project?*)
3. You should now be able to build and run the app as usual!
