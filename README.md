# client - server example

## Usage
To start the application, run the following command in the project root directory:
### build integration
 
        > cd integration
        > mvn clean install
        
### build server
 
        > cd server
        > mvn clean install
        
Check test results

        > mvn allure:serve 
        
### Start server
Open server/src/main/resources/application.yml and set some postgres database

        > mvn spring-boot:run
 
 

