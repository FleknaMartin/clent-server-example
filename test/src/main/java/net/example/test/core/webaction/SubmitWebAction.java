package net.example.test.core.webaction;

import org.jbehavesupport.core.internal.web.action.AbstractWebAction;
import org.jbehavesupport.core.web.WebActionContext;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SubmitWebAction extends AbstractWebAction {

    @Autowired
    PageUtils pageUtils;

    @Override
    public String name() {
        return "SUBMIT";
    }

    @Override
    public void perform(WebActionContext ctx) {
        WebElement element = findElement(ctx);

        scrollIntoView(element);

        pageUtils.waitForPageToLoad();
        new Actions(driver)
            .sendKeys(element, Keys.RETURN)
            .perform();
        pageUtils.waitForPageToLoad();
    }

}
