package com.example;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.openejb.api.LocalClient;
import org.junit.Test;

@LocalClient
public class PingRSEmbeddedTomeeIT extends ATomeeIT {
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
    }

    @Test
    public void ping() {
        final String message =
            WebClient.create(getBaseUrl()).path(
                // "/" + getClass().getSimpleName() +
                "tomee-embedded-trial/MyRestApplication" +
                "/ping")
                .get(String.class);
        System.out.println("got message: " + message);
        assertEquals("pong Reservoir Dogs", message);
    }

    @Test
    public void giveMeADuck() {
        String message =
            WebClient.create(getBaseUrl()).path(
                "tomee-embedded-trial/MyRestApplication" +
                    "/duck")
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get(String.class);
        System.out.println("got message: " + message);
        // FIXME: not sure why running this in integration test,
        // doesn't give us indented json :(
        assertEquals("{" +
            "\"name\":\"Donald\"," +
            "\"nicknames\":[\"Don\"]," +
            "\"nephews\":[\"Huey\",\"Dewey\",\"Louie\"]," +
            "\"children\":null" +
            "}", message);
    }

}
