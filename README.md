# client - server example

# Usage
To start the application, run the following command in the project root directory:
## build integration
 
        > cd integration
        > mvn clean install
        
## build server
 
        > cd server
        > mvn clean install
        
Check test results

        > mvn allure:serve 
        
## build client
 
        > cd client
        > mvn clean install
        
Check test results

        > mvn allure:serve 
        
## Start server
Open server/src/main/resources/application.yml and set some postgres database or define ports.

        > mvn spring-boot:run
        
## Start client
In client/src/main/resources/application.yml set ports if some changed in server configuration or set client ui port.

        > mvn spring-boot:run
        
## Run integration tests
Server and Client app must run.
In test/src/test/resources/test.yaml set host and ports if some changed in client or server configuration.
 
        > mvn clean test

Check test results

        > mvn allure:serve 
        
# Example UC
1. Start only client application
2. open client gui form, by default http://localhost:7070/
3. fill data (example valid card number: 4461573087662441)
4. click send
5. check new result is present in result table and is in status PENDING
6. start server application
7. check result change to CREATED after client job runs and reprocess payment
 

