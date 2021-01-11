Scenario: Bad card number
Meta:
    @Issue: JT-2
    @Suite: E2E

Given the following values are saved:
|name|data|
|firstName|{RANDOM_STRING:20}|
|lastName|{RANDOM_STRING:20}|
|cardNumber|1234567891234567|
|amount|{RANDOM_NUMBER:5}|
|expectedResult|FAILED_SERVER|
|expectedMessage|1234567891234567 is not valid card number|

Given [Client] homepage is open
Given fill main page with context data
Given on [main] page CLICK send
Then validate main page with context data


