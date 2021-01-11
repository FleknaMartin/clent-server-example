package net.example.test.composedstep;

import org.jbehave.core.annotations.Composite;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.springframework.stereotype.Component;

@Component
public class MainPageSteps {

    @Given("fill main page with context data")
    @Composite(steps = {
            "Given on [main] page FILL firstName with {CP:firstName}",
            "Given on [main] page FILL lastName with {CP:lastName}",
            "Given on [main] page FILL cardNumber with {CP:cardNumber}",
            "Given on [main] page FILL amount with {CP:amount}",
            "Then create web test evidence filledMainPage"
    })
    public void fillMainPageWithContextData(){
        // nosonar
    }

    @Then("validate main page with context data")
    @Composite(steps = {
            "Then on [main] page element fullName_1 fulfill TEXT property with value {CP:firstName} {CP:lastName}",
            "Then on [main] page element cardNumber_1 fulfill TEXT property with value {CP:cardNumber}",
            "Then on [main] page element amount_1 fulfill TEXT property with value {CP:amount}",
            "Then on [main] page element result_1 fulfill TEXT property with value {CP:expectedResult}",
            "Then on [main] page element message_1 fulfill TEXT property with value {CP:expectedMessage}",
            "Then create web test evidence filledMainPage"
    })
    public void validateResultWithContextData(){
        // nosonar
    }
}
