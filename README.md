Convata
==========

A currency conversion application that allows a user to view a given amount in a given currency converted into other currencies. It has an offline capability to allow conversions with locally saved rates.


## Getting Started and Installation

1. Download the project and in Android Studio, under the file menu select open, then select an existing project and navigate to the project you just unzipped.

2. Build the project using `./gradlew build`.

3. Run the application on a connected device or emulator.


### Prerequisites

1. [Set up Android Studio](https://developer.android.com/studio/install)

2. [Enable Kotlin in Android Studio](https://medium.com/@elye.project/setup-kotlin-for-android-studio-1bffdf1362e8)

3. [Run application on emulator](https://developer.android.com/studio/run/emulator)

4. [Run application on android device](https://developer.android.com/studio/run/device)


## Running the tests (Unit and Instrumentation tests)

1. Run tests using `./gradlew testDebugUnitTest` or `./gradlew connectedDebugAndroidTest` or `./gradlew connectedCheck`.

Run tests using one of the following:
~~~~
./gradlew testDebugUnitTest
~~~~

~~~~
./gradlew connectedDebugAndroidTest
~~~~

Jacoco Tests report.
~~~~
./gradlew connectedCheck
~~~~


## Architecture
* Model View ViewModel (MVVM)


## Consumed API Endpoints

```
    https://api.exchangerate.host/latest?base=""
    https://api.exchangerate.host/symbols
```


## Dependencies

* [Android](https://www.android.com/) - Operating System.
* [Room](https://developer.android.com/jetpack/androidx/releases/room) - A persistence library that provides an abstraction layer over SQLite to allow for more robust database access.
* [Retrofit](https://square.github.io/retrofit/) - HTTP Requests.
* [Kotlin](https://kotlinlang.org/) - Programing language.
* [Koin](https://insert-koin.io/) - Dependency Injection.
* [Mockito](https://javadoc.io/doc/org.mockito/mockito-core/2.23.0/org/mockito/Mockito.html) - Library that allows for mock creation, verification and stubbing while writing tests.


## Credits
* [Exchange Host](https://exchangerate.host/) - Currencies and Latest Conversion rates
* [Compact Decimal Formatting](https://gist.github.com/nprk/999faa0d324e54bd59bae7def933b495) - Format currency in abbreviated forms suitable for limited space real estate
