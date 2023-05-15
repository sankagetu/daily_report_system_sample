package com.techacademy;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.ajp.AjpNio2Protocol;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TomcatConfiguration implements WebServerFactoryCustomizer<TomcatServletWebServerFactory>{

    @Override
    public void customize(TomcatServletWebServerFactory factory) {
        factory.addAdditionalTomcatConnectors(ajpConnector());
    }

    private Connector ajpConnector() {
        Connector connector = new Connector("org.apache.coyote.ajp.AjpNio2Protocol");
        connector.setPort(8009);
        AjpNio2Protocol protocol = (AjpNio2Protocol)connector.getProtocolHandler();
        protocol.setSecretRequired(false);
        return connector;
    }
}
