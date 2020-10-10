Multi-module application for making orders, includes order class and customers.

Application characteristics:
- multi-module application divided into: persistence, service, ui.
Persistence is a module that contains models, service contains business logic, and ui is used to communication.
- the application uses the gson library to convert data from and to json format.
- the ui module uses the spark framework to generate http requests, 
and also allows communication with the user through consoles and convenient menu.
- the application has been tested using the JUnit 5 library. 
The project also uses the extension tool to generate test data from a json file.
- the application in the service module contains the logic for sending emails.
- the application also has the ability to download the exchange rate from "http://api.nbp.pl/api/exchangerates/rates/c/usd/2016-04-04/?format=json". 
It is a multi-threaded solution using CompletableFuture.
- the application is available on both github and dockerhub:.