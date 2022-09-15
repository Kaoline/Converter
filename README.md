# README
This is a simple currency converter. Enter any amount, choose a base currency, and the app will display the amount converted in all currencies available.  
This app uses https://openexchangerates.org/ with a free user account.

## Architecture
The code follows MVVM architecture for the front, coupled with Clean Architecture for the organisation of domain and data layers.
We could have implemented a MVI pattern but it's overkill for an app of this size.

Use of:
- Coroutines for asynchronicity
- Koin for dependency injection
- Retrofit and Okhttp for network handling
- Room for the database

## Possible improvements
- Rework the user interface to make it more beautiful
- Add an option to filter the currencies to convert to (and / or to choose from)
