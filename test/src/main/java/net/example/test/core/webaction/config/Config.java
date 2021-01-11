package net.example.test.core.webaction.config;

import net.example.test.core.webaction.PageUtils;
import org.jbehavesupport.core.internal.web.action.ClickWebAction;
import org.jbehavesupport.core.internal.web.action.FillWebAction;
import org.jbehavesupport.core.web.WebActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

   @Autowired
   PageUtils pageUtils;

   @Bean
   public ClickWebAction clickWebAction(){
      return new ClickWebAction(){
         @Override
         public void perform(WebActionContext ctx) {
            pageUtils.waitForPageToLoad();
            super.perform(ctx);
            pageUtils.waitForPageToLoad();
         }
      };
   }

   @Bean
   public FillWebAction fillWebAction(){
      return new FillWebAction(){
         @Override
         public void perform(WebActionContext ctx) {
            pageUtils.waitForPageToLoad();
            super.perform(ctx);
         }
      };
   }
}
