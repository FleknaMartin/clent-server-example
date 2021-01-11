package net.example.test.allure;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Allure;
import io.qameta.allure.jbehave.AllureJbehave;
import io.qameta.allure.model.*;
import org.apache.commons.lang3.StringUtils;
import org.jbehave.core.model.Scenario;
import org.jbehavesupport.core.TestContext;
import org.jbehavesupport.core.internal.parameterconverters.ExamplesEvaluationTableConverter;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
public class AllureReporter extends AllureJbehave {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${allure.link.issue.pattern:#{null}}")
    String jiraBaseUrl;

    @Autowired
    WebDriver webDriver;

    @Autowired
    TestContext testContext;

    @Autowired
    private ExamplesEvaluationTableConverter converter;

    @Override
    public void successful(final String step) {
        getLifecycle().updateStep(result -> result.setStatus(Status.PASSED));
        getLifecycle().stopStep();
    }

    @Override
    public void failed(String step, Throwable cause) {
        if (webDriver != null && ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.BYTES) != null) {
            try {
                byte[] screenshot = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.BYTES);
                getLifecycle().addAttachment("errorScreenshot", "picture", ".jpg", screenshot);
                byte[] pageSource = webDriver.getPageSource().getBytes();
                getLifecycle().addAttachment(webDriver.getCurrentUrl(), "html", ".html", pageSource);
            } catch (Exception e) {
                logger.error("Exception in reporter on failed step.", e);
            }
        }
        super.failed(step, cause);
    }

    public void createWebTestEvidence(String testEvidenceName) {
        byte[] screenshot = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.BYTES);
        getLifecycle().addAttachment(testEvidenceName, "picture", ".jpg", screenshot);
    }

    @Override
    public void beforeScenario(Scenario scenario) {
        super.beforeScenario(scenario);
        getLifecycle().updateTestCase(result -> result.setStatus(Status.PASSED));
        String issue = scenario.getMeta().getProperty("Issue:");
        if (StringUtils.isNotBlank(issue))
            Allure.issue(issue, jiraBaseUrl + issue);
    }

    @Override
    public void afterScenario() {
        Map<String, Object> contextMap = new HashMap<>();
        for (String key : testContext.keySet()) {
            contextMap.put(key, testContext.get(key));
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            byte[] contextValues = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(contextMap);
            getLifecycle().addAttachment("testContext", "text", ".txt", contextValues);
        } catch (IOException e) {
            throw new AllureReportException(e);
        }
        super.afterScenario();
    }

    public void createTextAttachment(String name, Object attachment) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            byte[] contextValues = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(attachment);
            getLifecycle().addAttachment(name, "text", ".txt", contextValues);
        } catch (IOException e) {
            throw new AllureReportException(e);
        }
    }

    class AllureReportException extends RuntimeException{
        public AllureReportException(Throwable cause) {
            super(cause);
        }
    }
}
