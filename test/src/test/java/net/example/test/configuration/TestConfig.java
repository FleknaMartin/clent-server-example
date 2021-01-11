package net.example.test.configuration;

import org.apache.commons.dbcp2.BasicDataSource;
import org.jbehavesupport.core.JBehaveDefaultConfig;
import org.jbehavesupport.core.support.YamlPropertySourceFactory;
import org.jbehavesupport.core.web.WebSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;

import javax.sql.DataSource;

@Configuration
@ContextConfiguration
@ComponentScan(basePackages = "net.example.test", excludeFilters = {})
@Import({JBehaveDefaultConfig.class})
@PropertySource(value = "test.yaml", factory = YamlPropertySourceFactory.class)
public class TestConfig {

    @Autowired
    Environment environment;

    @Bean
    @Qualifier("Client")
    public WebSetting client() {
        return WebSetting.builder()
                .homePageUrl(environment.getProperty("ui.url"))
                .elementLocatorsSource("client.yaml")
                .build();
    }

    @Bean
    @Qualifier("Server")
    public DataSource testDatasource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(environment.getProperty("db.driver"));
        dataSource.setUrl(environment.getProperty("db.url"));
        dataSource.setUsername(environment.getProperty("db.username"));
        dataSource.setPassword(environment.getProperty("db.password"));
        return dataSource;
    }
}
