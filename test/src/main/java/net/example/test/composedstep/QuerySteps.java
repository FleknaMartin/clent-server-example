package net.example.test.composedstep;

import org.jbehave.core.annotations.Composite;
import org.jbehave.core.annotations.Then;
import org.springframework.stereotype.Component;

@Component
public class QuerySteps {
    @Then("validate payment result in database with context data")
    @Composite(steps = {
            "When this query is performed on [Server]:\n" +
                    "select ch.FIRST_NAME, ch.LAST_NAME, c.CARD_NUMBER, p.AMOUNT from card c\n" +
                    "join card_holder ch on c.card_holder_id = ch.id\n" +
                    "join payment p on p.card_id = c.id\n" +
                    "where c.card_number = :cardNumber\n" +
                    "and ch.first_name = :firstName\n" +
                    "and ch.last_Name = :lastName\n" +
                    "with parameters:\n" +
                    "| name      | data  |\n" +
                    "| cardNumber | {CP:cardNumber}|\n" +
                    "| firstName | {CP:firstName}|\n" +
                    "| lastName | {CP:lastName}|\n" +
                    "\n" +
                    "Then these rows match the query result:\n" +
                    "| first_name            | last_name    | card_number | amount |\n" +
                    "| {CP:firstName}        | {CP:lastName}| {CP:cardNumber}|{CP:amount}|"
    })
    public void validateDataInDatabase(){
        // nosonar
    }
}
