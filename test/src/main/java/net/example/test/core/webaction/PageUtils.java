package net.example.test.core.webaction;

import org.junit.Assert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PageUtils {

   Logger logger = LoggerFactory.getLogger(PageUtils.class);

   @Autowired
   WebDriver webDriver;

   public void waitForPageToLoad() {
      ExpectedCondition<Boolean> expectation = driver -> {
         Boolean documentReady = ((JavascriptExecutor) driver).executeScript("return document.readyState").toString().equals("complete");
         Boolean jQueryReady = true;
         try {
            jQueryReady = ((JavascriptExecutor) driver).executeScript("return jQuery.active").toString().equals("0");
         } catch (Exception e){
            logger.info("jQuery ready: exception");
         }
         logger.info("document ready: {}", documentReady);
         logger.info("jQuery ready: {}", jQueryReady);
         return documentReady && jQueryReady;
      };
      try {
         WebDriverWait wait = new WebDriverWait(webDriver, 30);
         wait.until(expectation);
      } catch (Exception e) {
         Assert.fail("Timeout waiting for Page Load Request to complete.");
      }
   }
}
