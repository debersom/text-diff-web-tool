# Text Diff Web Tool


This project is a JAVA Spring Boot based solution, that expose 2 REST APIs to add and compare texts.

## Assumptions

To implement this application, I did some assumptions:

- The project will use Spring Boot and JAVA 8.
- The API documentation will use Swagger with annotations.
- The project has follow a clean architecture
- The APIs should not have authentication or authorization, they will have public access
- This first version will not be used on a High-Availability environment
- To keep the project simple as we have no requirements to have a HA environment, the will be used an in-memory repository (Map) with no persistence/durability

## Installation

To run this Web Application you need to clone this repository and have installed JAVA 8 in you local machine.

### Steps

1) clone the repo on the local machine
2) open the console on the project root folder
3) run the maven command to build, test and generate JAR file: 'mvn install'
4) run the command to start the server: java -jar target/text-diff-web-tool-1.0-SNAPSHOT.jar
5) open the link on your browser to check the API documentation

## Using the application

To use the application you need to call the API to add the left and right texts then call to get the difference between two texts.

### API

1) `POST /api/v1/diff/{key}/{side}` 

    **key**: the unique identifier used to relate the two text and generate the comparison.
    **side**: the side of the text, could be *left* or *right*.
    **body**: the body of the POST should contains a JSON object with the text on an attribute named as value.
    
     
2) `GET /api/v1/diff/{key}`
    
    **key**: the unique identifier used to relate the two text previously sent and generate the comparison.
    
    The response is a JSON object with the values:
    
    - equal: true only if the two texts are equals, otherwise false.
    - sizeDiff: the length difference between two text
    - offset: the start position for the first difference
    - length: the length of the different part of the text on right
    - diff: the different part of the text on right
    
## Improvements

1) Add new APIs to enable user registration, authentication, and authorization using tokens to restrict the access to the APIS
2) Improve the TextComparator implementation to provide a complete diff information
3) Create an error handler to improve the response messages
4) Improve the repository to use a SQL database instead of in-memory allowing to scale the application in multiple servers

## Support

For question or issues please reach me out.

email : deberson@murashige.com.br

## License
Apache License 2.0