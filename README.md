# Weather-App (MVVM, Hilt, Koltin, Coroutine,WorkManager, Retrofit)
## Application
The purpose of this assignment is to create a simple App to display the current weather forecast using the OpenWeatherMap free weather API.  Details of this API can be found at http://openweathermap.org/API.   You will need an API key to request this data

##Objectives
•	Create a simple App that displays the current weather forecast for your device current location, and display location and all relevant information returned in the UI;
•	Persist the response so that it can be retrieved again without having to make a further network request;
•	Schedule a request for every 2 hours to update and persist the latest weather response.  There should not be more than 1 request in a 2-hour period.  All background requests should only be made if Wi-Fi is connected;
•	Send a HTTP request to the OpenWeatherMap API to retrieve the current local weather forecast and parse the response;
