Scenario: Success
Meta:
    @Issue: JT-1
    @Suite: E2E

Given the following values are saved:
|name|data|
|firstName|{RANDOM_STRING:20}|
|lastName|{RANDOM_STRING:20}|
|cardNumber|{RANDOM_CARD_NUMBER}|
|amount|{RANDOM_NUMBER:5}|
|expectedResult|CREATED|
|expectedMessage||

Given [Client] homepage is open
Given fill main page with context data
Given on [main] page CLICK send
Then validate main page with context data
Then validate payment result in database with context data


