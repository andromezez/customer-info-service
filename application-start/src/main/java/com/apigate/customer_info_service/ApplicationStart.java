package com.apigate.customer_info_service;

import com.apigate.config.Config;
import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;

/**
 * @author Bayu Utomo
 * @date 20/5/2021 10:00 AM
 */
@SpringBootApplication
/*TODO enable this later
@PropertySources({
        @PropertySource("classpath:database.properties"),
        @PropertySource("classpath:monitoring.properties")
})*/
public class ApplicationStart {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationStart.class);
    }

    @Bean
    @DependsOn("application_config_reader")
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> sslConnectorCustomizer() {
        return (tomcat) -> tomcat.addAdditionalTomcatConnectors(createHealthCheckPort());
    }

    private Connector createHealthCheckPort() {
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