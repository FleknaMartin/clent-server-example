package net.example.test.step.web;

import net.example.test.allure.AllureReporter;
import net.example.test.core.webaction.PageUtils;
import org.assertj.core.api.SoftAssertions;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehavesupport.core.expression.ExpressionEvaluatingParameter;
import org.jbehavesupport.core.internal.ExampleTableConstraints;
import org.jbehavesupport.core.verification.Verifier;
import org.jbehavesupport.core.verification.VerifierNames;
import org.jbehavesupport.core.verification.VerifierResolver;
import org.jbehavesupport.core.web.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.jbehavesupport.core.web.WebScreenshotType.DEBUG;


@Component
public class CustomSteps {

   private static final String ELEMENT = "element";
   private static final String PROPERTY = "property";

   @Autowired
   AllureReporter allureJbehave;

   @Autowired
   PageUtils pageUtils;

   @Autowired
   ApplicationEventPublisher applicationEventPublisher;

   @Autowired
   WebActionResolver actionResolver;

   @Autowired
   WebPropertyResolver propertyResolver;

   @Autowired
   VerifierResolver verifierResolver;

   @Then("create web test evidence $testEvidenceName")
   public void createWebEvidence(String testEvidenceName){
      pageUtils.waitForPageToLoad();
      allureJbehave.createWebTestEvidence(testEvidenceName);
   }

   @Given(value = "on [$page] page $action $element with $value", priority = 1)
   public void elementActionWithValue(String page, String action,  String element, ExpressionEvaluatingParameter<String> value){
      WebAction webAction = actionResolver.resolveAction(action);

      WebActionContext actionContext = WebActionContext.builder()
              .page(page)
              .element(element)
              .data(value.getValue())
              .build();

      webAction.perform(actionContext);
      applicationEventPublisher.publishEvent(new WebScreenshotEvent(this, DEBUG));
   }

   @Given("on [$page] page $action $element")
   public void elementActionWithoutValue(String page, String action, String element){
      WebAction webAction = actionResolver.resolveAction(action);

      WebActionContext actionContext = WebActionContext.builder()
              .page(page)
              .element(element)
              .build();

      webAction.perform(actionContext);
      applicationEventPublisher.publishEvent(new WebScreenshotEvent(this, DEBUG));
   }

   @Then("on [$page] page element $element fulfill $property property with value $value")
   public void elementActionWithoutValue(String page, String element, String property, ExpressionEvaluatingParameter<String> value){
      SoftAssertions softly = new SoftAssertions();
      Map<String, String> values = new LinkedHashMap<>();
      values.put(ExampleTableConstraints.DATA, value.getValue());
      values.put(PROPERTY, property);
      values.put(ELEMENT, element);

      Object expected = values.get(ExampleTableConstraints.DATA);
      Object actual = resolvePropertyValue(page, values);
      String verifierName = resolveVerifierName(values);

      Verifier verifier = verifierResolver.getVerifierByName(verifierName);
      softly.assertThatCode(() -> verifier.verify(actual, expected))
              .as("element [" + values.get(ELEMENT) + "], property [" + values.get(PROPERTY) + "]")
              .doesNotThrowAnyException();
      softly.assertAll();
   }

   private Object resolvePropertyValue(String page, Map<String, String> row) {
      WebProperty property = propertyResolver.resolveProperty(row.get(PROPERTY));

      WebPropertyContext ctx = WebPropertyContext.builder()
              .page(page)
              .element(row.get(ELEMENT))
              .build();

      return property.value(ctx);
   }

   private String resolveVerifierName(Map<String, String> row) {
      String operator = VerifierNames.EQ;
      if (row.containsKey(ExampleTableConstraints.OPERATOR) && !row.get(ExampleTableConstraints.OPERATOR).isEmpty()) {
         operator = row.get(ExampleTableConstraints.OPERATOR);
      } else if (row.containsKey(ExampleTableConstraints.VERIFIER) && !row.get(ExampleTableConstraints.VERIFIER).isEmpty()) {
         operator = row.get(ExampleTableConstraints.VERIFIER);
      }
      return operator;
   }

}
