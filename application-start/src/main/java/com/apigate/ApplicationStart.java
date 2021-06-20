package com.apigate;

import com.apigate.config.Config;
import com.apigate.customer_info_service.repository.CustomRepositoryImpl;
import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author Bayu Utomo
 * @date 20/5/2021 10:00 AM
 */
@SpringBootApplication
@PropertySources({
        @PropertySource("classpath:database.properties"),
        @PropertySource("classpath:monitoring.properties")
})
@EnableJpaRepositories(repositoryBaseClass = CustomRepositoryImpl.class)
public class ApplicationStart {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationStart.class);
    }

    @Bean
    @DependsOn("application_config_reader")
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> sslConnectorCustomizer() {
        return (tomcat) -> tomcat.addAdditionalTomcatConnectors(createMonitoringPort());
    }

    private Connector createMonitoringPort() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");

        Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();
        try {
            connector.setPort(com.apigate.config.Config.getServerMonitoringPort());
            protocol.setMaxThreads(Config.getServerMonitoringMaxThread());
            return connector;
        } catch (Exception ex) {
            throw new IllegalStateException(
                    "can't access keystore: [" + "keystore" + "] or truststore: [" + "keystore" + "]", ex);
        }
    }
}
