package com.example;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.openejb.api.LocalClient;
import org.junit.Test;

@LocalClient
public class PingRSEmbeddedTomeeTest extends
    ATomeeTest
{
    protected void setProperties(Properties properties) {
        try {
            Enumeration<URL> ejbJars =
                this.getClass().getClassLoader().getResources(
                    "META-INF/ejb-jar.xml");
            while (ejbJars.hasMoreElements()) {
                URL url = ejbJars.nextElement();
                System.out.println("app = " + url);
            }

            ejbJars =
                this.getClass().getClassLoader().getResources(
                    "WEB-INF/ejb-jar.xml");
            while (ejbJars.hasMoreElements()) {
                URL url = ejbJars.nextElement();
                System.out.println("app = " + url);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        super.setProperties(properties);

        /**
         * Unfortunately this requires the built war file content to exist
         * already which may only work with failsafe integration tests
         * that run after install.
         */
        // loadProperties(properties, "jndi.properties");
        properties.setProperty("javax.ejb.embeddable.modules",
            "/stf/prj/tmp/tomee-embedded-trial/target/"
                + "tomee-embedded-trial-0.0.1-SNAPSHOT");
        // properties.setProperty("javax.ejb.embeddable.modules",
        // "/stf/prj/tmp/tomee-embedded-trial/target/classes");
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
        assertEquals("pong Reservoir Dogs", message);
    }


}
