# Boolean-Bros-Gym-Android
This is an android project to create an mobile UI for interacting with the clientele list.

The Android application requires the companion Java Server project to be running before database functionality is available.

TODO
animated loading screen for edit,
look in to parent classes,
look in to all the hooks in one class,
views search filter,
upload spreadsheet,
Better data validation,
search based on two criteria(two fields search),
small formatting discrepancies in xml's(order of id to layout to text etc),
Page scrolling on all pages,
Spinner should go in helper class,
debate whether back button with is only ever pressed once should be in its own method,
If data base gets too large, laoding the whole thing everytime would be a bad idea,
parseMemberLine should probably go in a helper class with getBetween,
remove colour changing thing in edit, its stupid,
make it so I can edit one collum at a time,
splitFullName should go in a helper class,
clearSearchFields MAYBE should go in a helper class,
checkincomingmemeber could be made a helper with a little bit of effort,
attemptSearchMemebr could be made a helper,
Stream line showRemoveConfermation in remove, as it's different from edit,

Update readme.


# Boolean Bros Android Application

## Requirements

* Android Studio
* Android Emulator or Android Device
* Java Server Project running on the same machine

## Running the Application

1. Open the Android project in Android Studio.
2. Start an Android emulator.
3. Open and run the Java Server project.
4. Start the server by entering:

   ```
   S
   ```
5. Verify the server displays:

   ```
   Database has connected. Database name: sys
   Server is listening on port 1234
   ```
6. Run the Android application.
