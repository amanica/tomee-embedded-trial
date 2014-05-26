package com.example;

import static org.junit.Assert.assertEquals;

import java.util.Properties;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.openejb.api.LocalClient;
import org.junit.Test;

@LocalClient
public class PingRSEmbeddedOpenEjbTest extends AOpenEjbTest {
    @EJB
    private Movies movies;

    protected void setProperties(Properties properties) {
        super.setProperties(properties);
        // loadProperties(properties, "jndi.properties");
        // properties.setProperty("javax.ejb.embeddable.modules",
        // "/stf/prj/tmp/tomee-embedded-trial/target/"
        // + "tomee-embedded-trial-0.0.1-SNAPSHOT");
        // properties.setProperty("javax.ejb.embeddable.modules",
        // "/stf/prj/tmp/tomee-embedded-trial/target/classes");
        // properties.setProperty("openejb.base",
        // "/stf/prj/tmp/tomee-embedded-trial/target/test-classes");

        properties.setProperty("openejb.validation.output.level", "VERBOSE");
        // properties.setProperty("openejb.embedded.remotable", "true");
        // properties.setProperty("openejb.log.factory", "slf4j");
        // properties.setProperty("openejb.tempclassloader.skip",
        // "annotations");
        // properties.setProperty(
        // "openejb.deployments.classpath.require.descriptor", "NONE");

        properties.setProperty(EJBContainer.APP_NAME, "myAppName");
        properties.put("movieDatabase", "new://Resource?type=DataSource");
        properties.put("movieDatabase.JdbcDriver", "org.hsqldb.jdbcDriver");
        properties.put("movieDatabase.JdbcUrl", "jdbc:hsqldb:mem:moviedb");
    }

    @Test
    public void ping() {
        String message =
            WebClient.create("http://localhost:" + webPort).path(
                "/myAppName" +
                    // "/tomee-embedded-trial/MyRestApplication" +
                "/ping")
                .get(String.class);
        System.out.println("got message: " + message);
        assertEquals("pong Reservoir Dogs", message);

        message =
            WebClient.create("http://localhost:" + webPort).path(
                "/myAppName" +
                    // "/tomee-embedded-trial/MyRestApplication" +
                    "/ping2")
                .get(String.class);
        System.out.println("got message: " + message);
        assertEquals("pong2 Reservoir Dogs", message);
    }

}
