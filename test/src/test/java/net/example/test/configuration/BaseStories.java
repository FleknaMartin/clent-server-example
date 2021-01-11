package net.example.test.configuration;

import net.example.test.allure.AllureReporter;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.failures.FailingUponPendingStep;
import org.jbehave.core.failures.RethrowingFailure;
import org.jbehave.core.io.LoadFromClasspath;
import org.jbehave.core.reporters.FreemarkerViewGenerator;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.ParameterControls;
import org.jbehave.core.steps.ParameterConverters;
import org.jbehave.core.steps.spring.SpringStepsFactory;
import org.jbehavesupport.core.AbstractSpringStories;
import org.jbehavesupport.core.internal.SuffixRemovingStoryNameResolver;
import org.jbehavesupport.core.internal.parameterconverters.ExamplesEvaluationTableConverter;
import org.jbehavesupport.runner.JUnitRunner;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;

import static org.jbehave.core.reporters.Format.CONSOLE;

@RunWith(JUnitRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class BaseStories extends AbstractSpringStories {

   private static final Long storyTimeout = 600L;

   @Autowired
   ApplicationContext applicationContext;

   @Autowired
   AllureReporter allureJbehave;

   @Autowired
   private ParameterConverters parameterConverters;

   private Configuration configuration;

   public BaseStories() {
      super(storyTimeout);
   }

   public BaseStories(Long timeout) {
      super(timeout);
   }

   @Override
   public Configuration configuration() {
      configuration = new MostUsefulConfiguration()
            .useStoryLoader(new LoadFromClasspath())
            .useStoryReporterBuilder(new StoryReporterBuilder()
                  .withDefaultFormats()
                  .withFormats(CONSOLE)
                  .withReporters(allureJbehave))
            .useParameterControls(new ParameterControls().useDelimiterNamedParameters(true))
            .usePendingStepStrategy(new FailingUponPendingStep())
            .useViewGenerator(new FreemarkerViewGenerator(new SuffixRemovingStoryNameResolver()))
            .useParameterConverters(parameterConverters)
            .useFailureStrategy(new RethrowingFailure());
      applicationContext.getBean(ExamplesEvaluationTableConverter.class).setConfiguration(configuration);
      return configuration;
   }

   @Override
   public InjectableStepsFactory stepsFactory() {
      return new SpringStepsFactory(configuration, applicationContext);
   }

}
