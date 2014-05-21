package com.example;

import static org.junit.Assert.assertEquals;

import java.util.Properties;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.openejb.api.LocalClient;
import org.junit.Test;


@LocalClient
public class PingRSTest extends
    // AOpenEjbTest
    ATomeeTest
{
    protected void setProperties(Properties properties) {
        super.setProperties(properties);
        properties.setProperty("javax.ejb.embeddable.modules",
            "/stf/prj/tmp/tomee-embedded-trial/target/"
                + "tomee-embedded-trial-0.0.1-SNAPSHOT");
        // properties.setProperty("openejb.tempclassloader.skip",
        // "annotations");
        properties.setProperty("openejb.validation.output.level", "VERBOSE");
    }
    @Test
    public void ping() {
        final String message =
            WebClient.create("http://localhost:8080").path(
                // "/" + getClass().getSimpleName() +
                "/tomee-embedded-trial/MyRestApplication" +
                "/ping")
                .get(String.class);
        System.out.println("got message: " + message);
        assertEquals("pong", message);
    }

}
