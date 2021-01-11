Scenario: Empty input validation
Meta:
    @Issue: JT-3
    @Suite: Gui validation

Given the following values are saved:
|name|data|
|firstName|{RANDOM_STRING:20}|
|misFN| First Name is required.|
|lastName|{RANDOM_STRING:20}|
|misLN| Last Name is required.|
|cardNumber|{RANDOM_CARD_NUMBER}|
|misCN| Card Number is required.|
|amount|{RANDOM_NUMBER:5}|
|misA| Amount is required.|
|expectedResult|CREATED|
|expectedMessage||

Given [Client] homepage is open
Given on [main] page CLICK send
Then on [main] page element validationResult fulfill TEXT property with value {CP:misFN} {CP:misLN} {CP:misCN} {CP:misA}
Given on [main] page FILL firstName with {CP:firstName}
Given on [main] page CLICK send
Then on [main] page element validationResult fulfill TEXT property with value {CP:misLN} {CP:misCN} {CP:misA}
Given on [main] page FILL lastName with {CP:lastName}
Given on [main] page CLICK send
Then on [main] page element validationResult fulfill TEXT property with value {CP:misCN} {CP:misA}
Given on [main] page FILL cardNumber with {CP:cardNumber}
Given on [main] page CLICK send
Then on [main] page element validationResult fulfill TEXT property with value {CP:misA}
Given on [main] page FILL amount with {CP:amount}
Given on [main] page CLICK send
Then validate main page with context data
Then validate payment result in database with context data